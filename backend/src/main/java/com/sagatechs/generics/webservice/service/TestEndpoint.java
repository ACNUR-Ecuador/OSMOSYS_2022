package com.sagatechs.generics.webservice.service;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.servicio.UserService;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.enums.AreaType;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@SuppressWarnings("ALL")
@Path("test")
public class TestEndpoint {
    private static final Logger LOGGER = Logger.getLogger(TestEndpoint.class);

    @Inject
    UserService userService;

    @Path("test")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String test2() throws GeneralAppException {
        return "ya - !!";
    }
    @Path("testenum")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AreaType[] test3() throws GeneralAppException {
        return AreaType.values();
    }

    @Path("setPass/{username}")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String setPass(@PathParam("username") String username) throws GeneralAppException {
        LOGGER.error(username);
        this.userService.changePasswordTest(username, "1234");
        return "ya!!!";
    }
}

