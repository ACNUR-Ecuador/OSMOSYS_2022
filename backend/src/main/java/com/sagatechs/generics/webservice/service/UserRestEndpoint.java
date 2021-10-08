package com.sagatechs.generics.webservice.service;

import com.sagatechs.generics.exceptions.AccessDeniedException;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import com.sagatechs.generics.security.servicio.RoleService;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.webservice.webModel.ChangePasswordSimple;
import com.sagatechs.generics.webservice.webModel.CredentialsWeb;
import com.sagatechs.generics.webservice.webModel.RoleWeb;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/authentication")
@RequestScoped
public class UserRestEndpoint {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(UserRestEndpoint.class);

    @Inject
    UserService userService;

    @Inject
    RoleService roleService;


    /**
     * Autentica usuarios
     *
     * @param credentials
     * @return
     * @throws AccessDeniedException
     */
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserWeb authenticateUser(CredentialsWeb credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        return userService.authenticateRest(username, password);

    }

    /**
     * cambio contraseña
     *
     * @return
     * @throws AccessDeniedException
     */
    @Secured
    @Path("/changepasswordsimple")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void changePasswordSimple(@Context SecurityContext securityContext, ChangePasswordSimple changePasswordSimple) throws GeneralAppException {

        String username = securityContext.getUserPrincipal().getName();
        this.userService.changePassworSimple(username, changePasswordSimple.getOldPassword(), changePasswordSimple.getNewPassword());
    }


    @Path("/recoverpasswordsimple")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void recoverPasswordSimple(@HeaderParam("appCode") String appCode, ChangePasswordSimple changePasswordSimple) throws GeneralAppException {


        this.userService.recoverPassword(changePasswordSimple.getNewPassword(), appCode);
    }

    /**
     * Información de usuarios
     *
     * @return
     * @throws AccessDeniedException
     */
    @Secured
    @Path("/users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserWeb> getAllUser() {

        return this.userService.getAllUsersWeb();
    }

    /**
     * roles de usuario
     *
     * @return
     * @throws AccessDeniedException
     */
    @Secured
    @Path("/roles")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RoleWeb> getAllRoles() {

        return this.roleService.getAllRolesWeb();
    }

    /**
     * roles de usuario
     *
     * @return
     * @throws AccessDeniedException
     */
    @Secured
    @Path("/users")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createUser(@HeaderParam("appCode") String appCode, UserWeb user) throws GeneralAppException {
        this.userService.createUser(user);
    }

    @Secured
    @Path("/users")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void updateUser(UserWeb user) throws GeneralAppException {
        this.userService.updateUser(user);
    }


    @Path("/users/active/UNHCR")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserWeb> getActiveUNHCRUsers() {
        return this.userService.getUNHCRUsersWebByState(State.ACTIVE);
    }

}
