package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.enums.DissagregationType;
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
    public List<IndicatorExecutionWeb> getGeneralIndicatorExecutionsAdminByProjectId(@PathParam("projectId") Long projectId) throws GeneralAppException {
        return this.indicatorExecutionService.getGeneralIndicatorExecutionsAdministrationByProjectId(projectId);
    }

    @Path("/generalByProjectId/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionWeb> getGeneralIndicatorExecutionsByProjectId(@PathParam("projectId") Long projectId) throws GeneralAppException {
        return this.indicatorExecutionService.getGeneralIndicatorExecutionsByProjectId(projectId, State.ACTIVO);
    }

    @Path("/performanceAdminByProject/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionWeb> getPerformanceIndicatorExecutionsAdminByProjectId(@PathParam("projectId") Long projectId) throws GeneralAppException {
        return this.indicatorExecutionService.getPerformanceIndicatorExecutionsAdministrationByProjectId(projectId);
    }

    @Path("/performanceByProjectId/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionWeb> getPerformanceIndicatorExecutionsByProjectId(@PathParam("projectId") Long projectId) throws GeneralAppException {
        return this.indicatorExecutionService.getPerformanceIndicatorExecutionsByProjectId(projectId, State.ACTIVO);
    }

    @Path("/performanceAllDirectImplementationByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionWeb> getAllPerformanceIndicatorExecutionsDirectImplementation(
            @PathParam("periodId") Long periodId) throws GeneralAppException {
        return this.indicatorExecutionService.getAllDirectImplementationIndicatorByPeriodId(periodId);
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

    @Path("/assignPerformanceIndicatorDirectImplementation")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long assignPerformanceIndicatoDirectImplementation(IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb) throws GeneralAppException {

        return this.indicatorExecutionService.assignPerformanceIndicatoDirectImplementation(indicatorExecutionAssigmentWeb);

    }

    @Path("/updateAssignPerformanceIndicatoToProject")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateAssignPerformanceIndicatoToProject(IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb) throws GeneralAppException {

        return this.indicatorExecutionService.updateAssignPerformanceIndicatoToProject(indicatorExecutionAssigmentWeb);

    }

    @Path("/updateAssignPerformanceIndicatorDirectImplementation")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateAssignPerformanceIndicatorDirectImplementation(IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb) throws GeneralAppException {

        return this.indicatorExecutionService.updateAssignPerformanceIndicatorDirectImplementation(indicatorExecutionAssigmentWeb);

    }

    @Path("/getResumeAdministrationPerformanceIndicatorById")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public IndicatorExecutionWeb getResumeAdministrationPerformanceIndicatorById(Long id) throws GeneralAppException {

        return this.indicatorExecutionService.getResumeAdministrationPerformanceIndicatorById(id, false);

    }

    @Path("/updateMonthValues/{indicatorExecutionId}")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateMonthValues(@PathParam("indicatorExecutionId") Long indicatorExecutionId, MonthValuesWeb monthValuesWeb) throws GeneralAppException {
        return this.indicatorExecutionService.updateMonthValues(indicatorExecutionId, monthValuesWeb);
    }

    @Path("/getDirectImplementationIndicatorExecutionsByIds")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionWeb> getDirectImplementationIndicatorExecutionsByIds(List<Long> indicatorExecutionIds) throws GeneralAppException {
        LOGGER.error(indicatorExecutionIds);
        return this.indicatorExecutionService.getDirectImplementationIndicatorExecutionsByIds(indicatorExecutionIds);
    }

}
