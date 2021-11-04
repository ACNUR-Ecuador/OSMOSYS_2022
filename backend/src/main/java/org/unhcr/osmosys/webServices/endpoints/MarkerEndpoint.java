package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.MarkerService;
import org.unhcr.osmosys.webServices.model.MarkerWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/markers")
@RequestScoped
public class MarkerEndpoint {

    @Inject
    MarkerService markerService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(MarkerWeb markerWeb) throws GeneralAppException {
        return this.markerService.save(markerWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(MarkerWeb markerWeb) throws GeneralAppException {
        return this.markerService.update(markerWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MarkerWeb> getAll() {
        return this.markerService.getAll();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MarkerWeb> getByState(@PathParam("state") State state) {
        return this.markerService.getByState(state);
    }
}
