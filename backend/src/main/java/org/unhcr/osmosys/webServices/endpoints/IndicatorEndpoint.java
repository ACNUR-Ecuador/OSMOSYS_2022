package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.IndicatorService;
import org.unhcr.osmosys.webServices.model.IndicatorWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/indicators")
@RequestScoped
public class IndicatorEndpoint {

    @Inject
    IndicatorService indicatorService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(IndicatorWeb indicatorWeb) throws GeneralAppException {
        return this.indicatorService.save(indicatorWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(IndicatorWeb indicatorWeb) throws GeneralAppException {
        return this.indicatorService.update(indicatorWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorWeb> getAll() {
        return this.indicatorService.getAll();
    }
}
