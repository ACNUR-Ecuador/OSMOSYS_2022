package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.SituationService;
import org.unhcr.osmosys.webServices.model.SituationWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/situations")
@RequestScoped
public class SituationEndpoint {

    @Inject
    SituationService situationService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(SituationWeb situationWeb) throws GeneralAppException {
        return this.situationService.save(situationWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(SituationWeb situationWeb) throws GeneralAppException {
        return this.situationService.update(situationWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<SituationWeb> getAll() {
        return this.situationService.getAll();
    }
}
