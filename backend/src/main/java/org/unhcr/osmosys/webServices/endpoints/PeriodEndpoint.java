package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.PeriodService;
import org.unhcr.osmosys.webServices.model.PeriodWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/periods")
@RequestScoped
public class PeriodEndpoint {

    @Inject
    PeriodService periodService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(PeriodWeb periodWeb) throws GeneralAppException {
        return this.periodService.save(periodWeb);
    }

    @Path("/")
    @PUT
    //@Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(PeriodWeb periodWeb) throws GeneralAppException {
        return this.periodService.update(periodWeb);
    }

    @Path("/withGeneralIndicatorAll")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<PeriodWeb> getWithGeneralIndicatorAll() {
        return this.periodService.getWithGeneralIndicatorAll();
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<PeriodWeb> getAll() {
        return this.periodService.getWithGeneralIndicatorAll();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<PeriodWeb> getByState(@PathParam("state") State state) {
        return this.periodService.getByState(state);
    }

    @Path("/{id}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public PeriodWeb getByid(@PathParam("id") Long id) {
        return this.periodService.getWebWithAllDataById(id);
    }

    @Path("/withGeneralIndicator/{id}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public PeriodWeb getWithGeneralIndicatorByid(@PathParam("id") Long id) {
        return this.periodService.getWebWithAllDataById(id);
    }
}
