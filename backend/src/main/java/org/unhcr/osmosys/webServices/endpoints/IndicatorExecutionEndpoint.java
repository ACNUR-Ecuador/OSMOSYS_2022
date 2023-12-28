package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.services.dataImport.DirectImplementationIndicatorsImportService;
import org.unhcr.osmosys.services.dataImport.ProjectIndicatorsImportService;
import org.unhcr.osmosys.webServices.model.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.List;

@Path("/indicatorExecutions")
@RequestScoped
public class IndicatorExecutionEndpoint {

    private final static Logger LOGGER = Logger.getLogger(IndicatorExecutionEndpoint.class);
    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    ProjectIndicatorsImportService projectIndicatorsImportService;

    @Inject
    DirectImplementationIndicatorsImportService directImplementationIndicatorsImportService;

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
        return this.indicatorExecutionService.getGeneralIndicatorExecutionsByProjectIdAndState(projectId, State.ACTIVO);
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

    @Path("/quartersTargetUpdate")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void quartersTargetUpdate(List<QuarterWeb> quarterWebs) throws GeneralAppException {
        LOGGER.debug(quarterWebs);
        this.indicatorExecutionService.quartersTargetUpdate(quarterWebs);

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

    // todo para que uso?
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
        return this.indicatorExecutionService.getDirectImplementationIndicatorExecutionsByIds(indicatorExecutionIds, State.ACTIVO);
    }

    @Path("/updateDirectImplementationIndicatorExecutionLocationAssigment/{indicatorExecutionId}")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateDirectImplementationIndicatorExecutionLocationAssigment(@PathParam("indicatorExecutionId") Long indicatorExecutionId, List<CantonWeb> cantones) throws GeneralAppException {
        return this.indicatorExecutionService.updateDirectImplementationIndicatorExecutionLocationAssigment(indicatorExecutionId, cantones);
    }

    @Path("/updatePartnerIndicatorExecutionLocationAssigment/{indicatorExecutionId}")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long updatePartnerIndicatorExecutionLocationAssigment(@PathParam("indicatorExecutionId") Long indicatorExecutionId, List<CantonWeb> cantones) throws GeneralAppException {
        return this.indicatorExecutionService.updatePartnerIndicatorExecutionLocationAssigment(indicatorExecutionId, cantones);
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

    @Path("/getProjectIndicatorsImportTemplate/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getProjectIndicatorsImportTemplate(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getIndicatorsImportTemplate:" + principal.getName());
        String fileName = null;
        try {
            fileName = "importador_indicadores_proyectos_plantilla.xlsm";
            ByteArrayOutputStream template = this.projectIndicatorsImportService.generateProjectIndicators(periodId, false);
            return Response.ok(template.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + fileName + "\"").build();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new GeneralAppException("Error al obtener el template " + fileName, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Path("/getProjectIndicatorsImportTemplateTotalTarget/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getProjectIndicatorsImportTemplateTotalTarget(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getIndicatorsImportTemplate:" + principal.getName());
        String fileName = null;
        try {
            fileName = "importador_indicadores_proyectos_plantilla.xlsm";
            ByteArrayOutputStream template = this.projectIndicatorsImportService.generateProjectIndicators(periodId, true);
            return Response.ok(template.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + fileName + "\"").build();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new GeneralAppException("Error al obtener el template " + fileName, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/importProjectIndicators/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response importProjectIndicators(@PathParam("projectId") Long projectId, ImportFileWeb importFileWeb) throws GeneralAppException {
        this.projectIndicatorsImportService.projectIndicatorsImport(projectId, importFileWeb, false);
        return Response.ok().build();
    }

    @POST
    @Path("/importProjectIndicatorsTotalTarget/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response importProjectIndicatorsTotalTarget(@PathParam("projectId") Long projectId, ImportFileWeb importFileWeb) throws GeneralAppException {
        this.projectIndicatorsImportService.projectIndicatorsImport(projectId, importFileWeb, false);
        return Response.ok().build();
    }

    @Path("/getDirectImplementationIndicatorsImportTemplate/{periodId}/{officeId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getDirectImplementationIndicatorsImportTemplate(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId,
            @PathParam("officeId") Long officeId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getIndicatorsImportTemplate:" + principal.getName());
        String fileName = null;
        try {
            fileName = "importador_indicadores_implementacion_directa_plantilla.xlsm";
            ByteArrayOutputStream template = this.directImplementationIndicatorsImportService.generateTemplate(periodId, officeId);
            return Response.ok(template.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + fileName + "\"").build();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new GeneralAppException("Error al obtener el template " + fileName, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/importDirectImplementationIndicators/{periodId}/{officeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response importDirectImplementationIndicators(
            @PathParam("periodId") Long periodId,
            @PathParam("officeId") Long officeId,
            ImportFileWeb importFileWeb) throws GeneralAppException {
        this.directImplementationIndicatorsImportService.directImplementationIndicatorsImport(periodId, officeId, importFileWeb);
        return Response.ok().build();
    }


}
