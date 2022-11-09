package com.sagatechs.generics.webservice.service;

import com.sagatechs.generics.appConfiguration.AppConfiguration;
import com.sagatechs.generics.appConfiguration.AppConfigurationKey;
import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/appconfiguration")
@RequestScoped
public class AppConfigurationEndpoint {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(AppConfigurationEndpoint.class);

    @Inject
    AppConfigurationService appConfigurationService;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppConfiguration> getAll() {
        return this.appConfigurationService.findAll();
    }

    @Path("/value/{key}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getValueByKey(@PathParam("key") AppConfigurationKey key) {
        return this.appConfigurationService.findValorByClave(key);
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void save(AppConfiguration appConfiguration) {
        this.appConfigurationService.updateAppConfiguration(appConfiguration.getId(), appConfiguration.getValor());
    }
}
