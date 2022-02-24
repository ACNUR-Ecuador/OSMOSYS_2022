package com.sagatechs.generics.webservice.service;


import com.sagatechs.generics.exceptions.GeneralAppException;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.webServices.model.IndicatorExecutionWeb;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("dataport")
public class DataPortEndpoint {
    private static final Logger LOGGER = Logger.getLogger(DataPortEndpoint.class);


    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Path("test")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test2() {
        return "ya";
    }

    @Path("partnersImplementation/{year}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionWeb> getPartnersImplementationFullResume(@PathParam("year") Integer year) throws GeneralAppException {
        return this.indicatorExecutionService.getActiveProjectIndicatorExecutionsByPeriodYear(year);
    }

    @Path("directImplementation/{year}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionWeb> getActiveDirectImplementationIndicatorExecutionsByPeriodYear(@PathParam("year") Integer year) throws GeneralAppException {
        return this.indicatorExecutionService.getActiveDirectImplementationIndicatorExecutionsByPeriodYear(year);
    }

}

