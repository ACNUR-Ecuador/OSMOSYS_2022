package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.JobStatusService;
import org.unhcr.osmosys.services.PeriodService;
import org.unhcr.osmosys.webServices.model.PeriodWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
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
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(PeriodWeb periodWeb) throws GeneralAppException {
        return this.periodService.update(periodWeb);
    }

    @Path("/asyncUpdate")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response asyncUpdate(PeriodWeb periodWeb) throws GeneralAppException {
        String jobId = JobStatusService.createJob();
        // Inicia un hilo para procesar la tarea asíncrona
        new Thread(() -> {
            try {
                Long result =  this.periodService.update(periodWeb, jobId);
                JobStatusService.finalizeJob(jobId, result);
            } catch (GeneralAppException ex) {

            }
        }).start();
        return Response.ok(Collections.singletonMap("jobId", jobId)).build();
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
        return this.periodService.getWebById(id);
    }

    @Path("/withGeneralIndicator/{id}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public PeriodWeb getWithGeneralIndicatorByid(@PathParam("id") Long id) {
        return this.periodService.getWebWithAllDataById(id);
    }
}
