package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.OfficeService;
import org.unhcr.osmosys.webServices.model.AreaWeb;
import org.unhcr.osmosys.webServices.model.OfficeWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/offices")
@RequestScoped
public class OfficeEndpoint {

    @Inject
    OfficeService officeService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(OfficeWeb officeWeb) throws GeneralAppException {
        return this.officeService.save(officeWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(OfficeWeb officeWeb) throws GeneralAppException {
        return this.officeService.update(officeWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<OfficeWeb> getAll() {
        return this.officeService.getAll(false);
    }

    @Path("/active")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<OfficeWeb> getActive() {
        return this.officeService.getByState(State.ACTIVO, false);
    }

    @Path("/withChilds")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<OfficeWeb> getAllWithChildren() {
        return this.officeService.getAll(true);
    }

    @Path("/tree")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<OfficeWeb> getTree() {
        return this.officeService.getOfficeTree();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<OfficeWeb> getByState(@PathParam("state") State state) {
        return this.officeService.getByState(state, false);
    }
}
