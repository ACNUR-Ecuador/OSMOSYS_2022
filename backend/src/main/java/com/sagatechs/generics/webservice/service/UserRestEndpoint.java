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


    @Path("/users")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createUser(@HeaderParam("appCode") String appCode, UserWeb user) throws GeneralAppException {
        this.userService.creaUser(user);
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

}
