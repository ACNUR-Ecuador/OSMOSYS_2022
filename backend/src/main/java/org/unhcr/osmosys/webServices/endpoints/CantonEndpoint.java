package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.CantonService;
import org.unhcr.osmosys.webServices.model.CantonWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/cantons")
@RequestScoped
public class CantonEndpoint {

    @Inject
    CantonService cantonService;


    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<CantonWeb> getAll() {
        return this.cantonService.getAll();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<CantonWeb> getByState(@PathParam("state") State state) {
        return this.cantonService.getByState(state);
    }
}
