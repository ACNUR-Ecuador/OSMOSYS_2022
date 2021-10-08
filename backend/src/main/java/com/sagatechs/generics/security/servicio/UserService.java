package com.sagatechs.generics.security.servicio;

import com.sagatechs.generics.exceptions.AccessDeniedException;
import com.sagatechs.generics.exceptions.AuthorizationException;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.dao.RoleAssigmentDao;
import com.sagatechs.generics.security.dao.UserDao;
import com.sagatechs.generics.security.model.Role;
import com.sagatechs.generics.security.model.RoleAssigment;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.service.EmailService;
import com.sagatechs.generics.utils.SecurityUtils;
import com.sagatechs.generics.webservice.webModel.RoleWeb;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import javax.crypto.SecretKey;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Stateless
public class UserService implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(UserService.class);


    @Inject
    UserDao userDao;

    @Inject
    SecurityUtils securityUtils;

    @Inject
    EmailService emailService;


    @Inject
    RoleService roleService;

    @Inject
    RoleAssigmentDao roleAssigmentDao;


    private static final int EXPIRATION_TIME_SECONDS = 6400;
    private static final int EXPIRATION_TIME_SECONDS_REFRESH = 86400 * 7;// 6400;

    private static final String SECRET_KEY = "xaE5cHuY4NCQm0v_BnsE93x3aa6tcRNUDJBKnHKUqhagrMIeTALKwkYHYPr77dBbPddJ5o207mWaF1ibL3zdDkDBv5MywlcPfu3_Awy2zDbCTDp6pZm-h245ZuC-ieVsDvBi3c1X15YEvmiqsE4BTKKQiHraIzT9kPwO2cqNJFfQPFMu_TWXeSpU14fLG5uFip2MltirPJLAeYS2kB4x--PLacTNo9Tb9zW3d0Il768xLOgPpdBqNkwUwLKrPtfXOl5mgXbv2l6G2k3z-JIysZJlRnDCTKp4R8Vvucp3i8p4e5UadenCT2Bl6qPMyYpXfS2j8jv08unn5xQiwkusiQ";

    private SecretKey key = null;

    public static final String salt = "NwhZ2MFDH0JDXmUSM8q5JydFiVg";


    /**
     * Verifica las credenciales del usuario, por nombre de usuario y contraseña
     *
     * @param username
     * @param password
     * @return
     */
    @SuppressWarnings("unused")
    public User verifyUsernamePassword(String username, String password, String appCode) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(appCode)) {
            return null;
        }

        // obtengo el hash del pass enviado
        byte[] hashedPass = this.securityUtils.hashPasswordByte(password, salt);
        return this.userDao.findByUserNameAndPasswordWithRoles(username, hashedPass, State.ACTIVE);

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

        List<RoleWeb> roles = new ArrayList<>();
        for (RoleAssigment userRoleAssigment : user.getRoleAssigments()) {
            if (userRoleAssigment.getState().equals(State.ACTIVE) && userRoleAssigment.getRole().getState().equals(State.ACTIVE)) {
                roles.add(this.roleService.roleToRoleWeb(userRoleAssigment.getRole()));
            }
        }
        userWeb.setRoles(roles);

        return userWeb;
    }


    public String issueTokenForLogin(UserWeb userWeb) {


        return buildToken(userWeb);

    }

    private String buildToken(UserWeb userWeb) {
        if (userWeb != null) {
            Date now = new Date();

            String token = Jwts.builder()
                    // .serializeToJsonWith(serializer)// (1)
                    .setSubject(userWeb.getUsername()) // (2)
                    .setIssuedAt(now)
                    .setExpiration(getExpirationDate(now))
                    .claim("roles", userWeb.getRoles())
                    .claim("username", userWeb.getUsername())
                    .claim("name", userWeb.getName())
                    .claim("id", userWeb.getId())
                    .claim("email", userWeb.getEmail())
//                    .claim("projectImplementer", userWeb.getProjectImplementer())
                    .signWith(getSecretKey()).compact();
            LOGGER.debug(token);
            return token;
        }
        throw new AccessDeniedException("usuario no encontrado");
    }

    public String refreshTokenFromToken(String token) {

        UserWeb user = this.validateTokenGetUserWeb(token);

        return buildToken(user);

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

    public UserWeb validateTokenGetUserWeb(String token) {
        // obtengo el usuario
        try {
            Jws<Claims> jws = Jwts.parser() // (1)
                    // .deserializeJsonWith(deserializer)
                    .setSigningKey(getSecretKey()) // (2)
                    .parseClaimsJws(token); // (3)

            // hasta ahi se valida el token*
            UserWeb user = new UserWeb();
            Long id = null;
            if (jws.getBody().get("id") != null) {
                try {
                    id = ((Integer) jws.getBody().get("id")).longValue();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            user.setId(id);
            user.setUsername(jws.getBody().getSubject());
            user.setEmail((String) jws.getBody().get("email"));
            //noinspection unchecked
            List<HashMap> rolesMaps = (List<HashMap>) jws.getBody().get("roles");
            LOGGER.info(rolesMaps);
            List<RoleWeb> rolesWeb = new ArrayList<>();
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
            LinkedHashMap projectImplementerMap = (LinkedHashMap) jws.getBody().get("projectImplementer");
/*
            if (projectImplementerMap!=null && projectImplementerMap.size() > 0) {
                ProjectImplementerWeb projectImplementerWeb = new ProjectImplementerWeb();
                projectImplementerWeb.setId( Long.valueOf((Integer)projectImplementerMap.get("id")));
                projectImplementerWeb.setState(State.valueOf((String) projectImplementerMap.get("state")));
                projectImplementerWeb.setCode((String) projectImplementerMap.get("code"));
                projectImplementerWeb.setDescription((String) projectImplementerMap.get("description"));
            }*/
            //user.setProjectImplementer(projectImplementerWeb);
            return user;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new AccessDeniedException("token invalido");
        }
    }

    public void changePassworSimple(String username, String oldPassword, String newPassword)
            throws GeneralAppException {
        // recupero un usuario
        if (StringUtils.isBlank(username) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(oldPassword)) {
            throw new GeneralAppException("Los datos no son correctos", Response.Status.BAD_REQUEST.getStatusCode());
        }
        byte[] hashedPass = this.securityUtils.hashPasswordByte(oldPassword, salt);
        User user = this.userDao.findByUserNameAndPassword(username, hashedPass);
        if (user == null) {
            throw new GeneralAppException("No se encontró un usuario con el nombre de usuario: " + username,
                    Response.Status.NOT_FOUND.getStatusCode());

        }

        // ya que esta verificado, reseteo el pass

        byte[] newHashedPass = this.securityUtils.hashPasswordByte(newPassword, UserService.salt);
        user.setPassword(newHashedPass);
        this.userDao.update(user);

    }

    public void changePasswordTest(String username, String newPassword)
            throws GeneralAppException {
        // recupero un usuario

        User user = this.findByUserName(username);
        if (user == null) {
            throw new GeneralAppException("Usuario no encontrado: " + username, Response.Status.NOT_FOUND.getStatusCode());
        }

        byte[] newHashedPass = this.securityUtils.hashPasswordByte(newPassword, UserService.salt);
        user.setPassword(newHashedPass);
        this.userDao.update(user);
    }

    public User findByUserName(String username) {
        return this.userDao.findByUserName(username);
    }


    /**
     * Persiste un nuevo usuario
     *
     * @param user usuario ya asignado roles y contraseñas
     */
    public void saveOrUpdate(User user) {
        if (user.getId() == null) {
            this.userDao.save(user);
        } else {
            this.userDao.update(user);
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
        return this.userDao.findByUserNameAndPasswordWithRoles(username, hashedPass, State.ACTIVE);

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
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Genera fecha de exìracion de token
     *
     * @param date
     * @param refreshToken
     * @return
     */
    private Date getExpirationDate(Date date, boolean refreshToken) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(date); // sets calendar time/date
        if (refreshToken) {
            // cal.add(Calendar.MINUTE, 3);
            cal.add(Calendar.SECOND, EXPIRATION_TIME_SECONDS_REFRESH);
            // return null;
        } else {
            cal.add(Calendar.SECOND, EXPIRATION_TIME_SECONDS); // adds one 24
        }

        return cal.getTime(); // returns new date object, one hour in the future
    }


    public List<UserWeb> getAllUsersWeb() {
        List<User> users = this.userDao.findAllWithRoles();
        return this.usersToUsersWeb(users);
    }

    public void createUser(UserWeb userWeb) throws GeneralAppException {
        // primero busco si existe el usuario
        User user = this.userDao.findByUserName(userWeb.getUsername());
        if (user != null) {
            throw new GeneralAppException("El usuario " + userWeb.getUsername() + " ya existe", Response.Status.CONFLICT.getStatusCode());
        }
        user = new User();

        user.setUsername(userWeb.getUsername());
        user.setEmail(userWeb.getEmail());
        user.setName(userWeb.getName());
        user.setState(userWeb.getState());
        Set<RoleAssigment> userRoleAssigments = new HashSet<>();
        for (RoleWeb roleWeb : userWeb.getRoles()) {
            Role role = this.roleService.findById(roleWeb.getId());
            if (role == null) {
                throw new GeneralAppException("El role " + roleWeb.getName() + " no es válido", Response.Status.CONFLICT.getStatusCode());
            }
            RoleAssigment userRoleAssigment = new RoleAssigment(user, role);
            userRoleAssigments.add(userRoleAssigment);
        }
        user.setRoleAssigments(userRoleAssigments);
        String password = this.securityUtils.generateRamdomPassword();
        byte[] pass = this.securityUtils.hashPasswordByte(password, UserService.salt);
        user.setPassword(pass);

        this.userDao.save(user);

        for (RoleAssigment userRoleAssigment : user.getRoleAssigments()) {
            this.roleAssigmentDao.save(userRoleAssigment);
        }

        // send email
        String message = "<p>Bienvenid@:</p>" +
                "<p>Se ha creado un nuevo usuario para su acceso al Sistema de Monitoreo de Programas.</p>" +
                "<p>Puede acceder al sistema utilizando los siguientes datos:</p>" +
                "<p>Direcci&oacute;n: <a href=\"https://imecuador.unhcr.org/programs\">" + "https://imecuador.unhcr.org/programs" + "</a> (Se recomienda el uso de Google Chrome)</p>" +
                "<p>Nombre de usuario: " + user.getUsername() + "</p>" +
                "<p>Contrase&ntilde;a: " + password + "</p>" +
                "<p>&nbsp;</p>" +

                "<p>Al ingresar al sistema, comprende y acepta que la informaci&oacute;n presentada es de uso interno de la organización y no ha de ser reproducida/compartida con otros actores sin consentimiento por escrito por parte del equipo de IM-ACNUR.</p>" +
                "<p>&nbsp;</p>" +
                "<p>Si necesitas ayuda por favor cont&aacute;ctate con la Unidad de Gesti&oacute;n de la Informaci&oacute;n con <a href=\"\\&quot;mailto:salazart@unhcr.org\\&quot;\">salazart@unhcr.org.</a></p>";

        this.emailService.sendEmailMessage(user.getEmail()
                , "Bienvenid@ al Sistema de Monitoreo de Programas",
                message
        );
    }

    public void updateUser(UserWeb userWeb) throws GeneralAppException {
        User user = this.userDao.find(userWeb.getId());
        if (user == null) {
            throw new GeneralAppException("El usuario " + userWeb.getUsername() + " no existe.(" + userWeb.getId() + ")", Response.Status.BAD_REQUEST.getStatusCode());
        }
        user.setUsername(userWeb.getUsername());
        user.setEmail(userWeb.getEmail());
        user.setName(userWeb.getName());
        user.setState(userWeb.getState());
        /*if (userWeb.getProjectImplementer() == null || userWeb.getProjectImplementer().getId() == null) {
            user.setProjectImplementer(null);
        } else {
            ProjectImplementer projectImplementer = this.projectImplementerService.find(userWeb.getProjectImplementer().getId());
            user.setProjectImplementer(projectImplementer);
        }*/

        this.userDao.save(user);

        // verifico los roles
        Set<RoleAssigment> userRoleAssigments = new HashSet<>();

        // vreo q tenga todos
        Set<RoleAssigment> rolesToCreate = new HashSet<>();
        for (RoleWeb roleWeb : userWeb.getRoles()) {
            Role role = this.roleService.findById(roleWeb.getId());
            for (RoleAssigment userRoleAssigmentCurrent : user.getRoleAssigments()) {
                if (userRoleAssigmentCurrent.getRole().getId().equals(role.getId())) {
                    // ya existe
                    // me aseguro de q este activo
                    userRoleAssigmentCurrent.setState(State.ACTIVE);
                } else {
                    // creo
                    RoleAssigment userRoleAssigment = new RoleAssigment(user, role);
                    rolesToCreate.add(userRoleAssigment);
                }

            }
        }

        // veo lo q tengo que borrar
        for (RoleAssigment userRoleAssigmentCurrent : user.getRoleAssigments()) {
            Boolean delete = Boolean.TRUE;
            for (RoleWeb roleWeb : userWeb.getRoles()) {
                if (roleWeb.getId().equals(userRoleAssigmentCurrent.getRole().getId())) {
                    delete = Boolean.FALSE;
                    break;
                }
            }
            if (delete) {
                userRoleAssigmentCurrent.setState(State.INACTIVE);
            }
        }
        user.getRoleAssigments().addAll(rolesToCreate);

        for (RoleAssigment userRoleAssigment : user.getRoleAssigments()) {
            this.roleAssigmentDao.saveOrUpdate(userRoleAssigment);
        }

    }

    public void recoverPassword(String email, String appcode) throws GeneralAppException {
        User user = this.userDao.getByEmail(email);
        if (user == null) {
            throw new GeneralAppException("Usuario no encontrado", Response.Status.NOT_FOUND.getStatusCode());
        } else {

            String password = this.securityUtils.generateRamdomPassword();
            byte[] pass = this.securityUtils.hashPasswordByte(password, UserService.salt);
            user.setPassword(pass);

            userDao.save(user);
            String message = "<p>Bienvenid@:</p>" +
                    "<p>Se ha generado una nueva contraseña para el acceso al Sistema de Monitoreo de Programas.</p>" +
                    "<p>Puede acceder al sistema utilizando los siguientes datos:</p>" +
                    "<p>Direcci&oacute;n: <a href=\"https://imecuador.unhcr.org/programs\">" + "https://imecuador.unhcr.org/programs" + "</a> (Se recomienda el uso de Google Chrome)</p>" +
                    "<p>Nombre de usuario: " + user.getUsername() + "</p>" +
                    "<p>Contraseña: " + password + "</p>" +
                    "<p>&nbsp;</p>" +
                    "<p>Si necesitas ayuda por favor cont&aacute;ctate con la Unidad de Gesti&oacute;n de la Informaci&oacute;n con <a href=\"\\&quot;mailto:salazart@unhcr.org\\&quot;\">salazart@unhcr.org.</a></p>";

            this.emailService.sendEmailMessage(user.getEmail()
                    , "Bienvenid@ al Sistema de Monitoreo de Programas.",
                    message
            );
        }
    }

    public List<User> getUNHCRUsersByState(State state) {
        return this.userDao.getUNHCRUsersByState(state);
    }

    public List<UserWeb> getUNHCRUsersWebByState(State state) {
        return this.usersToUsersWeb(this.getUNHCRUsersByState(state));
    }

    public User getById(Long userId) {
        return this.userDao.find(userId);
    }

    public List<UserWeb> getOfficeUsersByOfficeId(Long officeId) {
        List<User> user = this.userDao.getOfficeUsersByOfficeId(officeId);
        return this.usersToUsersWeb(user);
    }
}
