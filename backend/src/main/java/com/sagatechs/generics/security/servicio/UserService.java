package com.sagatechs.generics.security.servicio;

import com.sagatechs.generics.appConfiguration.AppConfigurationKey;
import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import com.sagatechs.generics.exceptions.AccessDeniedException;
import com.sagatechs.generics.exceptions.AuthorizationException;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.dao.RoleAssigmentDao;
import com.sagatechs.generics.security.dao.UserDao;
import com.sagatechs.generics.security.model.Role;
import com.sagatechs.generics.security.model.RoleAssigment;
import com.sagatechs.generics.security.model.RoleType;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.service.AsyncService;
import com.sagatechs.generics.utils.SecurityUtils;
import com.sagatechs.generics.webservice.webModel.RoleWeb;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.Indicator;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.model.OfficeAdministrator;
import org.unhcr.osmosys.model.Organization;
import org.unhcr.osmosys.model.enums.OfficeType;
import org.unhcr.osmosys.services.OfficeService;
import org.unhcr.osmosys.services.OrganizacionService;
import org.unhcr.osmosys.webServices.model.OfficeWeb;
import org.unhcr.osmosys.webServices.model.OrganizationWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.crypto.SecretKey;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@SuppressWarnings("JavadocDeclaration")
@Stateless
public class UserService implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(UserService.class);


    @Inject
    UserDao userDao;

    @Inject
    OrganizacionService organizacionService;

    @Inject
    OfficeService officeService;


    @Inject
    SecurityUtils securityUtils;


    @EJB
    AsyncService asyncService;


    @Inject
    RoleService roleService;

    @Inject
    RoleAssigmentDao roleAssigmentDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;
    @Inject
    AppConfigurationService appConfigurationService;


    private static final int EXPIRATION_TIME_SECONDS = 6400;
    private static final int EXPIRATION_TIME_SECONDS_REFRESH = 86400 * 7;// 6400;

    private static final String SECRET_KEY = "xaE5cHuY4NCQm0v_BnsE93x3aa6tcRNUDJBKnHKUqhagrMIeTALKwkYHYPr77dBbPddJ5o207mWaF1ibL3zdDkDBv5MywlcPfu3_Awy2zDbCTDp6pZm-h245ZuC-ieVsDvBi3c1X15YEvmiqsE4BTKKQiHraIzT9kPwO2cqNJFfQPFMu_TWXeSpU14fLG5uFip2MltirPJLAeYS2kB4x--PLacTNo9Tb9zW3d0Il768xLOgPpdBqNkwUwLKrPtfXOl5mgXbv2l6G2k3z-JIysZJlRnDCTKp4R8Vvucp3i8p4e5UadenCT2Bl6qPMyYpXfS2j8jv08unn5xQiwkusiQ";

    private SecretKey key = null;

    public static final String salt = "NwhZ2MFDH0JDXmUSM8q5JydFiVg";

    @SuppressWarnings("UnusedReturnValue")
    public User createUser(UserWeb userWeb) throws GeneralAppException {
        // primero busco si existe el usuario

        this.validateUserWeb(userWeb);
        User user = this.userDao.findByUserName(userWeb.getUsername());
        if (user != null) {
            throw new GeneralAppException("El usuario " + userWeb.getUsername() + " ya existe", Response.Status.CONFLICT.getStatusCode());
        }
        user = new User();

        user.setUsername(userWeb.getUsername());
        user.setEmail(userWeb.getEmail());
        user.setName(userWeb.getName());
        user.setState(userWeb.getState());

        Organization organization = this.organizacionService.getById(userWeb.getOrganization().getId());
        if (organization == null) {
            throw new GeneralAppException("Organización requerida", Response.Status.BAD_REQUEST);
        }
        user.setOrganization(organization);

        if (organization.getAcronym().equalsIgnoreCase("acnur")) {
            Office office = this.officeService.getById(userWeb.getOffice().getId());
            if (office == null) {
                throw new GeneralAppException("Oficina requerida", Response.Status.BAD_REQUEST);
            }
            user.setOffice(office);
        }
        List<RoleWeb> userValidRoles = validateUserRoles(userWeb);

        for (RoleWeb roleWeb : userValidRoles) {
            if (roleWeb.getState().equals(State.ACTIVO)) {
                try {
                    RoleType roleType = RoleType.valueOf(roleWeb.getName());
                    Role role = this.roleService.findByRoleType(roleType);
                    if (role == null) {
                        throw new GeneralAppException("El role " + roleWeb.getName() + " no es válido", Response.Status.CONFLICT.getStatusCode());
                    }
                    user.addRole(role);
                } catch (IllegalArgumentException e) {
                    throw new GeneralAppException("Permiso no válido", Response.Status.BAD_REQUEST);
                }
            }
        }
        String password = this.securityUtils.generateRamdomPassword();
        byte[] pass = this.securityUtils.hashPasswordByte(password, UserService.salt);
        user.setPassword(pass);

        this.userDao.save(user);

        for (RoleAssigment userRoleAssigment : user.getRoleAssigments()) {
            this.roleAssigmentDao.save(userRoleAssigment);
        }

        // send email
        String message = "<p>Bienvenid@:</p>" +
                "<p>Se ha creado un nuevo usuario para su acceso Osmosys.</p>" +
                "<p>Puede acceder al sistema utilizando los siguientes datos:</p>"+
                "<p>Direcci&oacute;n: <a href=\""+this.appConfigurationService.getAppUrl()+"\">" + this.appConfigurationService.getAppUrl() + "</a> (Se recomienda el uso de Google Chrome)</p>" +
                "<p>Nombre de usuario: " + user.getUsername() + "</p>" +
                "<p>Contrase&ntilde;a: " + password + "</p>" +
                "<p>&nbsp;</p>" +

                "<p>Al ingresar al sistema, comprende y acepta que la informaci&oacute;n presentada es de uso interno de la organización y no ha de ser reproducida/compartida con otros actores sin consentimiento por escrito por parte del equipo de IM-ACNUR.</p>" +
                "<p>&nbsp;</p>" +
                "<p>Si necesitas ayuda por favor cont&aacute;ctate con la Unidad de Gesti&oacute;n de la Informaci&oacute;n con <a href=\"\\&quot;mailto:"+this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL)+"\\&quot;\">"+this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL)+".</a></p>";

        // todo quitar email
        this.asyncService.sendEmail(
                user.getEmail(), null
                //"salazart@unhcr.org"
                , "Bienvenid@ a OSMOSYS ACNUR",

                message
        );

        return user;

    }

    /**
     * Autentica Rests y genera tolens
     *
     * @param username
     * @param password
     * @return tokens
     * @throws AccessDeniedException
     */
    public UserWeb authenticateRest(String username, String password) {
        User user = this.verifyUsernamePassword(username, password);
        if (user != null) {
            return this.userToUserWeb(user);
        } else {
            throw new AuthorizationException(
                    "Acceso denegado. Por favor ingrese correctamente el nombre de usuario y contraseña.");
        }

    }

    /**
     * Verifica las credenciales del usuario, por nombre de usuario y contraseña
     *
     * @param username
     * @param password
     * @return
     */
    public User verifyUsernamePassword(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return null;
        }

        // obtengo el hash del pass enviado
        byte[] hashedPass = this.securityUtils.hashPasswordByte(password, salt);
        return this.userDao.findByUserNameAndPasswordWithRoles(username, hashedPass, State.ACTIVO);

    }

    public void changePasswordTest(String username, String newPassword)
            throws GeneralAppException {
        // recupero un usuario

        User user = this.userDao.findByUserName(username);
        if (user == null) {
            throw new GeneralAppException("Usuario no encontrado: " + username, Response.Status.NOT_FOUND.getStatusCode());
        }

        byte[] newHashedPass = this.securityUtils.hashPasswordByte(newPassword, UserService.salt);
        user.setPassword(newHashedPass);
        this.userDao.update(user);
    }

    private void validateUserWeb(UserWeb userWeb) throws GeneralAppException {
        if (userWeb == null) {
            throw new GeneralAppException("Usuario es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(userWeb.getUsername())) {
            throw new GeneralAppException("Nombre de usuario no válido", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(userWeb.getEmail())) {
            throw new GeneralAppException("correo no válido", Response.Status.BAD_REQUEST);
        }
        if (!EmailValidator.getInstance().isValid(userWeb.getEmail())) {
            throw new GeneralAppException("Correo no válido", Response.Status.BAD_REQUEST);

        }

        if (userWeb.getState() == null) {
            throw new GeneralAppException("Estado no válido", Response.Status.BAD_REQUEST);
        }

        if (userWeb.getOrganization() == null) {
            throw new GeneralAppException("Organización requerida", Response.Status.BAD_REQUEST);
        }


        if (userWeb.getOffice() == null && userWeb.getOrganization().getAcronym().equalsIgnoreCase("acnur")) {
            throw new GeneralAppException("Oficina requerida", Response.Status.BAD_REQUEST);
        }

    }

    private List<UserWeb> usersToUsersWeb(List<User> users) {
        List<UserWeb> userWebs = new ArrayList<>();
        for (User user : users) {
            userWebs.add(this.userToUserWeb(user));
        }
        return userWebs;
    }


    public UserWeb userToUserWeb(User user) {
        if (user == null) {
            return null;
        }
        UserWeb userWeb = new UserWeb();
        userWeb.setId(user.getId());
        userWeb.setName(user.getName());
        userWeb.setEmail(user.getEmail());
        userWeb.setUsername(user.getUsername());
        userWeb.setState(user.getState());
        userWeb.setOffice(this.modelWebTransformationService.officeToOfficeWeb(user.getOffice(), true, false));
        userWeb.setOrganization(this.modelWebTransformationService.organizationToOrganizationWeb(user.getOrganization()));
        List<RoleWeb> roles = new ArrayList<>();
        for (RoleAssigment userRoleAssigment : user.getRoleAssigments()) {
            if (userRoleAssigment.getState().equals(State.ACTIVO) && userRoleAssigment.getRole().getState().equals(State.ACTIVO)) {
                roles.add(this.roleService.roleToRoleWeb(userRoleAssigment.getRole()));
            }
        }
        userWeb.setRoles(roles);
        List<Office> administeredOfficess = user.getOfficeAdministrators().stream().filter(officeAdministrator -> officeAdministrator.getState().equals(State.ACTIVO))
                .map(OfficeAdministrator::getOffice).collect(Collectors.toList());
        // find focal points ()
        List<Long> projectsIds = this.userDao.findProjectsFocalPoints(user.getId());
        //result manager Indicators
        List<Indicator> resultManagerIndicators = this.userDao.findIndicatorsByResultManager(user.getId());
        //partner Manager(supervisor reporte) Projects
        List<Long> partnerManagerProjects=this.userDao.findProjectsPartnerManager(user.getId());
        //impl dir supervisor Indicatorexecutions
        List<Long> supervisorDirectImplementations=this.userDao.findSupervisorDirectImplementations(user.getId());
        if (CollectionUtils.isNotEmpty(projectsIds)) {
            RoleWeb fpr = new RoleWeb();
            fpr.setId(0L);
            fpr.setName(RoleType.PUNTO_FOCAL.name());
            fpr.setState(State.ACTIVO);
            userWeb.getRoles().add(fpr);
            userWeb.setFocalPointProjects(projectsIds);
        }
        if (CollectionUtils.isNotEmpty(administeredOfficess)) {
            RoleWeb fpr = new RoleWeb();
            fpr.setId(-1L);
            fpr.setName(RoleType.ADMINISTRADOR_OFICINA.name());
            fpr.setState(State.ACTIVO);
            userWeb.getRoles().add(fpr);
            userWeb.setAdministratedOffices(this.modelWebTransformationService.officesToOfficesWeb(administeredOfficess,false, false));
        }
        if (CollectionUtils.isNotEmpty(resultManagerIndicators)) {
            RoleWeb fpr = new RoleWeb();
            fpr.setId(-2L);
            fpr.setName(RoleType.RESULT_MANAGER.name());
            fpr.setState(State.ACTIVO);
            userWeb.getRoles().add(fpr);
        }
        if (CollectionUtils.isNotEmpty(partnerManagerProjects)) {
            RoleWeb fpr = new RoleWeb();
            fpr.setId(-3L);
            fpr.setName(RoleType.SUPERVISOR_REPORTE_SOCIO.name());
            fpr.setState(State.ACTIVO);
            userWeb.getRoles().add(fpr);
        }
        if (CollectionUtils.isNotEmpty(supervisorDirectImplementations)) {
            RoleWeb fpr = new RoleWeb();
            fpr.setId(-4L);
            fpr.setName(RoleType.SUPERVISOR_REPORTE_ID.name());
            fpr.setState(State.ACTIVO);
            userWeb.getRoles().add(fpr);
        }




        return userWeb;
    }

    public String issueTokenForLogin(UserWeb userWeb) {


        return buildToken(userWeb);

    }

    private String buildToken(UserWeb userWeb) {
        if (userWeb != null) {
            Date now = new Date();

            // LOGGER.debug(token);
            return Jwts.builder()
                    // .serializeToJsonWith(serializer)// (1)
                    .setSubject(userWeb.getUsername()) // (2)
                    .setIssuedAt(now)
                    .setExpiration(getExpirationDate(now))
                    .claim("roles", userWeb.getRoles())
                    .claim("username", userWeb.getUsername())
                    .claim("name", userWeb.getName())
                    .claim("id", userWeb.getId())
                    .claim("email", userWeb.getEmail())
                    .claim("organization", userWeb.getOrganization())
                    .claim("office", userWeb.getOffice())
                    .claim("focalPointProjects", userWeb.getFocalPointProjects())
                    .claim("administratedOffices", userWeb.getAdministratedOffices())
                    .signWith(getSecretKey()).compact();
        }
        throw new AccessDeniedException("usuario no encontrado");
    }

    /**
     * Genera fecha de expìracion de token
     *
     * @param date
     * @return
     */
    private Date getExpirationDate(Date date) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(date); // sets calendar time/date
        cal.add(Calendar.SECOND, EXPIRATION_TIME_SECONDS);
        return cal.getTime(); // returns new date object, one hour in the future
    }

    /**
     * Crea la clave secret de jwt
     *
     * @return
     */
    private SecretKey getSecretKey() {

        try {
            if (key == null) {
                key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            }
            return key;
        } catch (WeakKeyException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;

    }

    public String refreshTokenFromToken(String token) {

        UserWeb user = this.validateTokenGetUserWeb(token);

        return buildToken(user);

    }

    public UserWeb validateTokenGetUserWeb(String token) {
        // obtengo el usuario
        try {
            Jws<Claims> jws = Jwts.parser() // (1)
                    // .deserializeJsonWith(deserializer)
                    .setSigningKey(getSecretKey()) // (2)
                    .build()
                    .parseClaimsJws(token); // (3)

            // hasta ahi se valida el token*
            UserWeb user = new UserWeb();
            Long id = null;
            if (jws.getBody().get("id") != null) {
                try {
                    id = ((Integer) jws.getBody().get("id")).longValue();
                } catch (NumberFormatException e) {
                    LOGGER.error(ExceptionUtils.getStackTrace(e));
                }
            }

            user.setId(id);
            user.setUsername(jws.getBody().getSubject());
            user.setEmail((String) jws.getBody().get("email"));
            //noinspection unchecked
            @SuppressWarnings("rawtypes")
            List<HashMap> rolesMaps = (List<HashMap>) jws.getBody().get("roles");
            // LOGGER.info(rolesMaps);
            List<RoleWeb> rolesWeb = new ArrayList<>();
            //noinspection unchecked
            for (Map<String, Object> rolesS : rolesMaps) {
                RoleWeb roleWeb = new RoleWeb();
                Integer roleId = (Integer) rolesS.get("id");
                if (roleId != null) {
                    roleWeb.setId(roleId.longValue());
                }
                roleWeb.setName((String) rolesS.get("name"));
                roleWeb.setState(State.valueOf((String) rolesS.get("state")));
                rolesWeb.add(roleWeb);
            }
            user.setRoles(rolesWeb);
            user.setName((String) jws.getBody().get("name"));

            @SuppressWarnings("rawtypes")
            LinkedHashMap organizationMap = (LinkedHashMap) jws.getBody().get("organization");
            if (organizationMap != null && !organizationMap.isEmpty()) {
                OrganizationWeb organizationWeb = new OrganizationWeb();
                organizationWeb.setId(Long.valueOf((Integer) organizationMap.get("id")));
                organizationWeb.setState(State.valueOf((String) organizationMap.get("state")));
                organizationWeb.setCode((String) organizationMap.get("code"));
                organizationWeb.setDescription((String) organizationMap.get("description"));
                organizationWeb.setAcronym((String) organizationMap.get("acronym"));
                user.setOrganization(organizationWeb);
            }
            @SuppressWarnings("rawtypes")
            LinkedHashMap officeMap = (LinkedHashMap) jws.getBody().get("office");
            if (officeMap != null && !officeMap.isEmpty()) {
                OfficeWeb officeWeb = new OfficeWeb();
                officeWeb.setId(Long.valueOf((Integer) officeMap.get("id")));
                officeWeb.setState(State.valueOf((String) officeMap.get("state")));
                officeWeb.setType(OfficeType.valueOf((String) officeMap.get("type")));
                officeWeb.setDescription((String) officeMap.get("description"));
                officeWeb.setAcronym((String) officeMap.get("acronym"));
                user.setOffice(officeWeb);
            }
            @SuppressWarnings("unchecked")
            List<Long> focalPointProjectsMap = (List<Long>) jws.getBody().get("focalPointProjects");
            if (focalPointProjectsMap != null && !focalPointProjectsMap.isEmpty()) {
                user.setFocalPointProjects(focalPointProjectsMap);
            } else {
                user.setFocalPointProjects(null);
            }
            List<OfficeWeb> administratedOffices = (List<OfficeWeb>) jws.getBody().get("administratedOffices");
            if (administratedOffices != null && !administratedOffices.isEmpty()) {
                user.setAdministratedOffices(administratedOffices);
            } else {
                user.setFocalPointProjects(null);
            }
            return user;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            // e.printStackTrace();
            throw new AccessDeniedException("token invalido");
        }
    }

    public List<UserWeb> getUNHCRUsersWebByState(State state) {
        return this.modelWebTransformationService.usersToUsersWebSimple(this.userDao.getUNHCRUsersByState(state), true, true);
    }

    public List<User> getUNHCRUsersByOfficeIdAndState(State state, Long officeId) {
        return this.userDao.getUNHCRUsersByOfficeIdAndState(state, officeId);
    }

    public User getUNHCRUsersByName(String name) throws GeneralAppException {
        return this.userDao.getUNHCRUsersByName(name);
    }

    public User getUNHCRUsersByUsername(String username) throws GeneralAppException {
        return this.userDao.getUNHCRUsersByUsername(username);
    }

    public User getById(Long id) {
        return this.userDao.find(id);
    }

    public UserWeb getWebById(Long id) {
        return this.userToUserWeb(this.getById(id));
    }

    public List<UserWeb> getAllUsers() {
        return this.usersToUsersWeb(this.userDao.getAllUsers());
    }

    public Long updateUser(UserWeb userWeb) throws GeneralAppException {
        User user = this.userDao.findWithRoles(userWeb.getId());
        if (user == null) {
            throw new GeneralAppException("El usuario " + userWeb.getUsername() + " no existe.(" + userWeb.getId() + ")", Response.Status.BAD_REQUEST.getStatusCode());
        }
        // user.setUsername(userWeb.getUsername());
        user.setEmail(userWeb.getEmail());
        user.setName(userWeb.getName());
        user.setState(userWeb.getState());
        user.setOffice(this.modelWebTransformationService.officeWebToOffice(userWeb.getOffice()));
        user.setOrganization(this.modelWebTransformationService.organizationWebToOrganization(userWeb.getOrganization()));


        this.userDao.update(user);
        // roles
        List<RoleWeb> userValidRoles = validateUserRoles(userWeb);

        for (RoleWeb roleWeb : userValidRoles) {
            try {
                RoleType roleType = RoleType.valueOf(roleWeb.getName());
                Role role = this.roleService.findByRoleType(roleType);
                if (roleWeb.getState().equals(State.ACTIVO)) {
                    user.addRole(role);
                } else {
                    user.removeRole(role);
                }
            } catch (IllegalArgumentException e) {
                throw new GeneralAppException("El usuario " + userWeb.getUsername() + " tiene un permiso no válido.(" + roleWeb.getName() + ")", Response.Status.BAD_REQUEST.getStatusCode());
            }

        }
        user.getRoleAssigments().forEach(roleAssigment -> this.roleAssigmentDao.saveOrUpdate(roleAssigment));
        return user.getId();
    }

    public void recoverPassword(String email) throws GeneralAppException {
        User user = this.userDao.getByEmail(email);
        if (user == null) {
            throw new GeneralAppException("Usuario no encontrado", Response.Status.NOT_FOUND.getStatusCode());
        } else {
            if (user.getState().equals(State.INACTIVO)) {
                throw new GeneralAppException("Usuario desactivado, comuníquese con el administrador del sistema", Response.Status.NOT_FOUND.getStatusCode());
            }

            String password = this.securityUtils.generateRamdomPassword();
            byte[] pass = this.securityUtils.hashPasswordByte(password, UserService.salt);
            user.setPassword(pass);

            userDao.save(user);
            String message = "<p>Bienvenid@:</p>" +
                    "<p>Se ha generado una nueva contraseña para el acceso al Osmosys.</p>" +
                    "<p>Puede acceder al sistema utilizando los siguientes datos:</p>" +
                    "<p>Direcci&oacute;n: <a href=\""+this.appConfigurationService.getAppUrl()+"\">" + this.appConfigurationService.getAppUrl() + "</a> (Se recomienda el uso de Google Chrome)</p>" +
                    "<p>Nombre de usuario: " + user.getUsername() + "</p>" +
                    "<p>Contraseña: " + password + "</p>" +
                    "<p>&nbsp;</p>" +
                    "<p>&nbsp;</p>";
            LOGGER.debug("------------------llamo");
            this.asyncService.sendEmail(user.getEmail(), null,
                    "Bienvenid@ al Sistema de Monitoreo de Programas.",
                    message);
        }
        LOGGER.debug("------------llamo termino");
    }


    public void changePassword(String username, String oldPassword, String newPassword) throws GeneralAppException {
        // recupero un usuario
        if (StringUtils.isBlank(username) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(oldPassword)) {
            throw new GeneralAppException("Los datos no son correctos", Response.Status.BAD_REQUEST.getStatusCode());
        }
        byte[] hashedPass = this.securityUtils.hashPasswordByte(oldPassword, salt);
        User user = this.userDao.findByUserNameAndPassword(username, hashedPass);
        if (user == null) {
            throw new GeneralAppException("No se encontró un usuario con el nombre de usuario: " + username + " o la contraseña actual es incorrecta",
                    Response.Status.NOT_FOUND.getStatusCode());

        }

        // ya que esta verificado, reseteo el pass

        byte[] newHashedPass = this.securityUtils.hashPasswordByte(newPassword, UserService.salt);
        user.setPassword(newHashedPass);
        this.userDao.update(user);
    }

    public List<User> getActivePartnerUsers(Long organizationId) {
        return this.userDao.getActivePartnerUsers(organizationId);
    }

    public List<User> getFocalPointsByOrganizationIdAndPeriodId(Long organizationId, Long periodId) {
        return this.userDao.getFocalPointsByOrganizationIdAndPeriodId(organizationId,periodId);
    }

    public List<User> getActiveResponsableDirectImplementationUsers(Long periodId) {
        return this.userDao.getActiveResponsableDirectImplementationUsers(periodId);
    }

    public List<UserWeb> getActiveResponsableDirectImplementationUserWebs(Long periodId) {
        List<User> r = this.userDao.getActiveResponsableDirectImplementationUsers(periodId);
        return this.modelWebTransformationService.usersToUsersWebSimple(r, true, false);
    }
    public List<UserWeb> getActiveSupervisorDirectImplementationUserWebs(Long periodId) {
        List<User> r = this.userDao.getActiveResponsableDirectImplementationUsers(periodId);
        return this.modelWebTransformationService.usersToUsersWebSimple(r, true, false);
    }

    public List<User> getActiveSupervisorsDirectImplementationUsers(Long periodId) {
        return this.userDao.getActiveSupervisorsDirectImplementationUsers(periodId);
    }

    public User getByEmail(String email) {
        return this.userDao.getByEmail(email);
    }

    public List<RoleWeb> validateUserRoles(UserWeb userWeb){
        Long orgId=userWeb.getOrganization().getId();
        Set<RoleType> invalidRoles = new HashSet<>();
        invalidRoles.add(RoleType.PUNTO_FOCAL);
        invalidRoles.add(RoleType.ADMINISTRADOR_OFICINA);
        invalidRoles.add(RoleType.RESULT_MANAGER);
        invalidRoles.add(RoleType.SUPERVISOR_REPORTE_SOCIO);
        invalidRoles.add(RoleType.SUPERVISOR_REPORTE_ID);

        Set<RoleType> userAcnurRoles = new HashSet<>();
        userAcnurRoles.add(RoleType.SUPER_ADMINISTRADOR);
        userAcnurRoles.add(RoleType.ADMINISTRADOR_REGIONAL);
        userAcnurRoles.add(RoleType.ADMINISTRADOR_LOCAL);
        userAcnurRoles.add(RoleType.MONITOR_ID);
        userAcnurRoles.add(RoleType.EJECUTOR_ID);


        List<RoleWeb> validRoles = new ArrayList<>();
        for (RoleWeb roleWeb : userWeb.getRoles()) {
            RoleType roleType = RoleType.valueOf(roleWeb.getName());
            if(orgId == 1){
                if(!userAcnurRoles.contains(roleType) || invalidRoles.contains(roleType)){
                    continue;
                }
                validRoles.add(roleWeb);

            }else{
                if(!invalidRoles.contains(roleType) && !userAcnurRoles.contains(roleType)){
                    validRoles.add(roleWeb);
                }
            }
        }
        return validRoles;
    }


}

