package com.sagatechs.generics.webservice.service;

import com.sagatechs.generics.security.annotations.Secured;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("testsecured")
public class TestSecuredEndpoint {


    @Secured
    @Path("test")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getHtml() {
        return "scured test";
    }

}
