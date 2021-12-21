package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.model.GeneralIndicator;
import org.unhcr.osmosys.services.GeneralIndicatorService;
import org.unhcr.osmosys.webServices.model.GeneralIndicatorWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/generalIndicators")
@RequestScoped
public class GeneralIndicatorEndpoint {

    @Inject
    GeneralIndicatorService generalIndicatorService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        return this.generalIndicatorService.save(generalIndicatorWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        GeneralIndicator generalIndicator = this.generalIndicatorService.update(generalIndicatorWeb);
        return generalIndicator.getId();
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<GeneralIndicatorWeb> getAll() {
        return this.generalIndicatorService.getAll();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<GeneralIndicatorWeb> getByState(@PathParam("state") State state) {
        return this.generalIndicatorService.getByState(state);
    }
    @Path("/byPeriodId/{periodId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public GeneralIndicatorWeb getByState(@PathParam("periodId") Long periodId) throws GeneralAppException {
        return this.generalIndicatorService.getByPeriodId(periodId);
    }
}
