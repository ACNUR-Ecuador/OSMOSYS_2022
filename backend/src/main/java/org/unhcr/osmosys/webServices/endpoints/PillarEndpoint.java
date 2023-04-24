package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.PillarService;
import org.unhcr.osmosys.webServices.model.PillarWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/pillars")
@RequestScoped
public class PillarEndpoint {

    @Inject
    PillarService pillarService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(PillarWeb pillarWeb) throws GeneralAppException {
        return this.pillarService.save(pillarWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(PillarWeb pillarWeb) throws GeneralAppException {
        return this.pillarService.update(pillarWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<PillarWeb> getAll() {
        return this.pillarService.getAll();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<PillarWeb> getByState(@PathParam("state") State state) {
        return this.pillarService.getWebByState(state);
    }
}
