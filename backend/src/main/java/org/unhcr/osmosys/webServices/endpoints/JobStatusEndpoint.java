package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import com.sagatechs.generics.security.servicio.UserService;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.JobStatus;
import org.unhcr.osmosys.services.AreaService;
import org.unhcr.osmosys.services.JobStatusService;
import org.unhcr.osmosys.webServices.model.AreaResumeWeb;
import org.unhcr.osmosys.webServices.model.AreaWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/jobStatus")
@RequestScoped
public class JobStatusEndpoint {

    private static final Logger LOGGER = Logger.getLogger(JobStatusEndpoint.class);

    @GET
    @Path("/{jobId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJobStatus(@PathParam("jobId") String jobId) {
        JobStatus status = JobStatusService.getJobStatus(jobId);
        if (status == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Job no encontrado").build();
        }
        LOGGER.debug("Job status: " + status);
        return Response.ok(status).build();
    }
}