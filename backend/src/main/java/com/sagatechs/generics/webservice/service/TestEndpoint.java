package com.sagatechs.generics.webservice.service;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.webservice.jsonSerializers.LocalDateDeserializer;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.Project;
import org.unhcr.osmosys.model.enums.AreaType;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.services.PeriodService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.Month;


@SuppressWarnings("ALL")
@Path("test")
public class TestEndpoint {
    private static final Logger LOGGER = Logger.getLogger(TestEndpoint.class);

    @Inject
    UserService userService;

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    PeriodService periodService;

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

    @Path("testYearQuarter")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public void testYearQuarter() throws GeneralAppException {
        LocalDate starDate = LocalDate.of(2022, Month.APRIL, 1);
        LocalDate endDate = LocalDate.of(2023, Month.APRIL, 30);

        Project p = new Project();
        p.setEndDate(endDate);
        p.setStartDate(starDate);
        p.setCode("test123");
        p.setName("test123");
        p.setState(State.ACTIVO);
        p.setPeriod(this.periodService.getById(1L));
        this.indicatorExecutionService.createGeneralIndicatorForProject(p);




    }
}

