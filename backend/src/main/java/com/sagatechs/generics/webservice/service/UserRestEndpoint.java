package com.sagatechs.generics.webservice.service;

import com.sagatechs.generics.exceptions.AccessDeniedException;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.webservice.webModel.ChangePasswordSimple;
import com.sagatechs.generics.webservice.webModel.CredentialsWeb;
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

    /**
     * get  user
     *
     * @param appCode
     * @return
     * @throws GeneralAppException
     */
    @Path("/users/{userId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserWeb getUserById(@HeaderParam("appCode") String appCode, @PathParam("userId") Long userId) {
        return this.userService.getWebById(userId);
    }

    /**
     * creates new user
     *
     * @param appCode
     * @param user
     * @throws GeneralAppException
     */
    @Path("/users")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createUser(@HeaderParam("appCode") String appCode, UserWeb user) throws GeneralAppException {
        this.userService.createUser(user);
    }

    /**
     * update CurrentUser
     *
     * @param user
     * @throws GeneralAppException
     */
    @Secured
    @Path("/users")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateUser(UserWeb user) throws GeneralAppException {
        return this.userService.updateUser(user);
    }

    @Path("/users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserWeb> getAllUsers(@HeaderParam("appCode") String appCode) {
        return this.userService.getAllUsers();
    }


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

    @Path("/users/active/UNHCR")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserWeb> getActiveUNHCRUsers() {
        return this.userService.getUNHCRUsersWebByState(State.ACTIVO);
    }

    @Path("/recoverpassword")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void recoverPasswordSimple(ChangePasswordSimple changePasswordSimple) throws GeneralAppException {
        this.userService.recoverPassword(changePasswordSimple.getNewPassword());
    }

    /**
     * cambio contraseña
     *
     * @return
     * @throws AccessDeniedException
     */
    @Secured
    @Path("/changepassword")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void changePasswordSimple(@Context SecurityContext securityContext, ChangePasswordSimple changePasswordSimple) throws GeneralAppException {

        String username = securityContext.getUserPrincipal().getName();
        this.userService.changePassword(username, changePasswordSimple.getOldPassword(), changePasswordSimple.getNewPassword());
    }

}
