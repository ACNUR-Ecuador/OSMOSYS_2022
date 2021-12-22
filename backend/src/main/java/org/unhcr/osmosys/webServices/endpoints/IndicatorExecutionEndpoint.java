package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import com.sagatechs.generics.service.EmailService;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.webServices.model.IndicatorExecutionGeneralIndicatorAdministrationResumeWeb;
import org.unhcr.osmosys.webServices.model.TargetUpdateDTOWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/indicatorExecutions")
@RequestScoped
public class IndicatorExecutionEndpoint {

    private final static Logger LOGGER = Logger.getLogger(IndicatorExecutionEndpoint.class);
    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Path("/general/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionGeneralIndicatorAdministrationResumeWeb> getGeneralIndicatorExecutionsByProjectId(@PathParam("projectId") Long projectId) {
        return this.indicatorExecutionService.getGeneralIndicatorExecutionsByProjectId(projectId);
    }
    @Path("/performanceByProject/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionGeneralIndicatorAdministrationResumeWeb> getPerformanceIndicatorExecutionsByProjectId(@PathParam("projectId") Long projectId) {
        return this.indicatorExecutionService.getPerformanceIndicatorExecutionsByProjectId(projectId);
    }

    @Path("/targetsUpdate")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void updateTargets(TargetUpdateDTOWeb targetUpdateDTOWeb) throws GeneralAppException {
        LOGGER.debug(targetUpdateDTOWeb);
        this.indicatorExecutionService.updateTargets(targetUpdateDTOWeb);

    }
}
