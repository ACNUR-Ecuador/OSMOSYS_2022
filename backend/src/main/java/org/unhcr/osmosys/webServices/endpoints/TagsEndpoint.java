package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.TagsService;
import org.unhcr.osmosys.webServices.model.StatementWeb;
import org.unhcr.osmosys.webServices.model.TagsWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/tags")
@RequestScoped
public class TagsEndpoint {
    @Inject
    TagsService tagsService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(TagsWeb tagsWeb) throws GeneralAppException {
        return this.tagsService.save(tagsWeb);
    }
    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(TagsWeb tagWeb) throws GeneralAppException {
        return this.tagsService.update(tagWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<TagsWeb> getAll() {
        return this.tagsService.getAll();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<TagsWeb> getByState(@PathParam("state") State state) {
        return this.tagsService.getByState(state);
    }

    @Path("/getActiveByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<TagsWeb> getByState(@PathParam("periodId") Long periodId) {
        return this.tagsService.getActiveByPeriodId(periodId, State.ACTIVO);
    }



}
