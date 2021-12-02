package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.CustomDissagregationService;
import org.unhcr.osmosys.webServices.model.CustomDissagregationWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/customDissagregations")
@RequestScoped
public class CustomDissagregationEndpoint {

    @Inject
    CustomDissagregationService customDissagregationService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(CustomDissagregationWeb customDissagregationWeb) throws GeneralAppException {
        return this.customDissagregationService.save(customDissagregationWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(CustomDissagregationWeb customDissagregationWeb) throws GeneralAppException {
        return this.customDissagregationService.update(customDissagregationWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomDissagregationWeb> getAll() {
        return this.customDissagregationService.getAll();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomDissagregationWeb> getByState(@PathParam("state") State state) {
        return this.customDissagregationService.getByState(state);
    }
}
