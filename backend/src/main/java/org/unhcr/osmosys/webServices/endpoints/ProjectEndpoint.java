package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.services.JobStatusService;
import org.unhcr.osmosys.services.ProjectService;
import org.unhcr.osmosys.services.dataImport.ProjectsImportService;
import org.unhcr.osmosys.webServices.model.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("/projects")
@RequestScoped
public class ProjectEndpoint {
    private static final Logger LOGGER = Logger.getLogger(IndicatorEndpoint.class);

    @Inject
    ProjectService projectService;

    @Inject
    ProjectsImportService projectsImportService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(ProjectWeb projectWeb) throws GeneralAppException {
        return this.projectService.save(projectWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(ProjectWeb projectWeb) throws GeneralAppException {
        return this.projectService.update(projectWeb);
    }

  
    @Path("/updateAsync")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAsync(ProjectWeb projectWeb) {
        // Se recomienda usar un ExecutorService administrado por el contenedor en producción.

        String jobId = JobStatusService.createJob();
        // Inicia un hilo para procesar la tarea asíncrona
        new Thread(() -> {
            try {
                Long result = projectService.update(projectWeb, jobId);
                JobStatusService.finalizeJob(jobId, result);

            } catch (GeneralAppException ex) {

            }
        }).start();
        return Response.ok(Collections.singletonMap("jobId", jobId)).build();
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectWeb> getAll() {
        return this.projectService.getAll();
    }

    @Path("/{id}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectWeb getById(@PathParam("id") Long id) {
        return this.projectService.getWebById(id);
    }

    @Path("/byIds/{ids}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectWeb> getByIds(@PathParam("ids") List<Long> ids) {
        return this.projectService.getWebByIds(ids);
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectWeb> getByState(@PathParam("state") State state) {
        return this.projectService.getByState(state);
    }

    @Path("/getProjectResumenWebByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectResumeWeb> getProjectResumenWebByPeriodId(@PathParam("periodId") Long periodId) {
        return this.projectService.getProjectResumenWebByPeriodId(periodId);
    }

    @Path("/getProjectResumenWebByPeriodIdAndFocalPointId/{periodId}/{focalPointId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectResumeWeb> getProjectResumenWebByPeriodIdAndFocalPointId(
            @PathParam("periodId") Long periodId,
            @PathParam("focalPointId") Long focalPointId
    ) {
        return this.projectService.getProjectResumenWebByPeriodIdAndFocalPointId(periodId, focalPointId);
    }

    @Path("/getProjectResumenWebByPeriodIdAndOrganizationId/{periodId}/{organizationId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectResumeWeb> getProjectResumenWebByPeriodIdAndOrganizationId(
            @PathParam("periodId") Long periodId,
            @PathParam("organizationId") Long organizationId
    ) {
        return this.projectService.getProjectResumenWebByPeriodIdAndOrganizationId(periodId, organizationId);
    }

    @Path("/getQuartersStateByProjectId/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuarterStateWeb> getQuartersStateByProjectId(
            @PathParam("projectId") Long projectId
    ) {
        return this.projectService.getQuartersStateByProjectId(projectId);
    }

    @Path("/getMonthsStateByProjectId/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MonthStateWeb> getMonthsStateByProjectId(
            @PathParam("projectId") Long projectId
    ) {
        return this.projectService.getMonthsStateByProjectId(projectId);
    }

    @Path("/blockQuarterStateByProjectId/{projectId}")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuarterStateWeb> blockQuartersStateByProjectId(
            @PathParam("projectId") Long projectId, QuarterStateWeb quarterStateWeb
    ) {
        return this.projectService.blockQuarterStateByProjectId(projectId, quarterStateWeb);
    }

    @Path("/changeMonthStateByProjectId/{projectId}")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void changeMonthStateByProjectId(
            @PathParam("projectId") Long projectId, MonthStateWeb monthStateWeb
    ) {
        this.projectService.changeMonthStateByProjectId(projectId, monthStateWeb);
    }

    @Path("/getImportTemplate/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getImportTemplate(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getIndicatorsImportTemplate:" + principal.getName());
        String fileName = null;
        try {
            fileName = "importador_proyectos_plantilla.xlsm";
            ByteArrayOutputStream template = this.projectsImportService.generateTemplate(periodId);
            return Response.ok(template.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + fileName + "\"").build();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new GeneralAppException("Error al obtener el template " + fileName, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/importCatalog")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response importStatementsCatalog(ImportFileWeb importFileWeb) throws GeneralAppException {
        LOGGER.debug(importFileWeb);
        this.projectsImportService.projectsImport(importFileWeb);
        return null;
    }

    @Path("/getProjectCantonAsignations/{projectId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<CantonWeb> getProjectCantonAsignations(
            @PathParam("projectId") Long projectId
    ) {
        return this.projectService.getProjectCantonAsignations(projectId);
    }

    @Path("/updateProjectLocations/{projectId}")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void updateProjectLocations(
            @PathParam("projectId") Long projectId,
            List<CantonWeb> cantonesWeb
    ) throws GeneralAppException {
        LOGGER.info(cantonesWeb);
        this.projectService.updateProjectLocations(Set.copyOf(cantonesWeb), projectId);
    }

}