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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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

    @Path("/updateDirectImplementationIndicatorExecutionLocationAssigment/{indicatorExecutionId}")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateDirectImplementationIndicatorExecutionLocationAssigment(@PathParam("indicatorExecutionId") Long indicatorExecutionId, List<CantonWeb> cantones) throws GeneralAppException {
        return this.indicatorExecutionService.updateDirectImplementationIndicatorExecutionLocationAssigment(indicatorExecutionId, cantones);
    }

    @SuppressWarnings("DuplicatedCode")
    @Path("/getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionWeb> getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId(
            @Context UriInfo uriInfo
    ) throws GeneralAppException {
        String userIdS = uriInfo.getQueryParameters().getFirst("userId");
        String periodIdS = uriInfo.getQueryParameters().getFirst("periodId");
        String officeIdS = uriInfo.getQueryParameters().getFirst("officeId");
        String supervisorS = uriInfo.getQueryParameters().getFirst("supervisor");
        String responsibleS = uriInfo.getQueryParameters().getFirst("responsible");
        String backupS = uriInfo.getQueryParameters().getFirst("backup");

        Long userId = null;
        Long periodId = null;
        Long officeId = null;
        boolean supervisor;
        boolean responsible;
        boolean backup;
        try {
            if (userIdS != null && !userIdS.equalsIgnoreCase("null")) {
                userId = Long.valueOf(userIdS);
            }
            if (officeIdS != null && !officeIdS.equalsIgnoreCase("null")) {
                officeId = Long.valueOf(officeIdS);
            }
            if (periodIdS != null && !periodIdS.equalsIgnoreCase("null")) {
                periodId = Long.valueOf(periodIdS);
            }
        } catch (NumberFormatException e) {
            throw new GeneralAppException("parametros incorrectos", Response.Status.BAD_REQUEST.getStatusCode());
        }
        supervisor = supervisorS != null && supervisorS.equalsIgnoreCase("true");
        responsible = supervisorS != null && responsibleS.equalsIgnoreCase("true");
        backup = backupS != null && backupS.equalsIgnoreCase("true");

        if (periodId == null) {
            throw new GeneralAppException("El periodo es obligatorio", Response.Status.BAD_REQUEST.getStatusCode());
        }
        if (officeId == null && userId == null) {
            throw new GeneralAppException("Es necesario seleccionar o una oficina o un usuario", Response.Status.BAD_REQUEST.getStatusCode());
        }
        if (userId != null && !responsible && !backup && !supervisor) {
            throw new GeneralAppException("Si selecciona un usuario, debe seleccionar al menos un rol", Response.Status.BAD_REQUEST.getStatusCode());
        }

        return this.indicatorExecutionService.getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId(userId, periodId, officeId, supervisor, responsible, backup);
    }

}
