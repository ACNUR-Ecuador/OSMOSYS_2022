package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.services.AreaService;
import org.unhcr.osmosys.webServices.model.AreaWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/areas")
@RequestScoped
public class AreaEndpoint {

    @Inject
    AreaService areaService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(AreaWeb areaWeb) throws GeneralAppException {
        return this.areaService.save(areaWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(AreaWeb areaWeb) throws GeneralAppException {
        return this.areaService.update(areaWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AreaWeb> getAll() {
        return this.areaService.getAll();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AreaWeb> getByState(@PathParam("state") State state) {
        return this.areaService.getByState(state);
    }
}
