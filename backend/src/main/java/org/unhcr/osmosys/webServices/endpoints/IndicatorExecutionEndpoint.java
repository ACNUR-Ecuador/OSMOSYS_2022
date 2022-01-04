package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.webServices.model.*;

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

    @Path("/generalAdmin/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionGeneralIndicatorAdministrationResumeWeb> getGeneralIndicatorExecutionsAdminByProjectId(@PathParam("projectId") Long projectId) {
        return this.indicatorExecutionService.getGeneralIndicatorExecutionsAdministrationByProjectId(projectId);
    }

    @Path("/generalByProjectId/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionGeneralIndicatorResumeWeb> getGeneralIndicatorExecutionsByProjectId(@PathParam("projectId") Long projectId) {
        return this.indicatorExecutionService.getGeneralIndicatorExecutionsByProjectId(projectId, State.ACTIVO);
    }

    @Path("/performanceAdminByProject/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb> getPerformanceIndicatorExecutionsAdminByProjectId(@PathParam("projectId") Long projectId) {
        return this.indicatorExecutionService.getPerformanceIndicatorExecutionsAdministrationByProjectId(projectId);
    }

    @Path("/performanceByProjectId/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionPerformanceIndicatorResumeWeb> getPerformanceIndicatorExecutionsByProjectId(@PathParam("projectId") Long projectId) {
        return this.indicatorExecutionService.getPerformanceIndicatorExecutionsByProjectId(projectId, State.ACTIVO);
    }



    @Path("/targetsUpdate")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void updateTargets(TargetUpdateDTOWeb targetUpdateDTOWeb) throws GeneralAppException {
        LOGGER.debug(targetUpdateDTOWeb);
        this.indicatorExecutionService.updateTargets(targetUpdateDTOWeb);

    }

    @Path("/assignPerformanceIndicatoToProject")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long assignPerformanceIndicatoToProject(IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb) throws GeneralAppException {

        return this.indicatorExecutionService.assignPerformanceIndicatoToProject(indicatorExecutionAssigmentWeb).getId();

    }

    @Path("/getResumeAdministrationPerformanceIndicatorById")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb getResumeAdministrationPerformanceIndicatorById(Long id) throws GeneralAppException {

        return this.indicatorExecutionService.getResumeAdministrationPerformanceIndicatorById(id);

    }

    @Path("/updateMonthValues/{indicatorExecutionId}")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateMonthValues(@PathParam("indicatorExecutionId") Long indicatorExecutionId, MonthValuesWeb monthValuesWeb) throws GeneralAppException {
        return this.indicatorExecutionService.updateMonthValues(indicatorExecutionId, monthValuesWeb);
    }
}
