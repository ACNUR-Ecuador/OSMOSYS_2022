package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.services.ProjectService;
import org.unhcr.osmosys.webServices.model.ProjectWeb;
import org.unhcr.osmosys.webServices.model.ResultManagerIndicatorWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/resultManagerIndicators")
@RequestScoped
public class ResultManagerIndicatorEndPoint {
    @Inject
    IndicatorExecutionService indicatorExecutionService;
    @Path("/{periodId}/{userId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ResultManagerIndicatorWeb> getResultManagerIndicators(@PathParam("periodId") Long periodId, @PathParam("userId") Long userId ) throws GeneralAppException {
        return indicatorExecutionService.getResultManagerIndicators(userId, periodId);
    }
}
