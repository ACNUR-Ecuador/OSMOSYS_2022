package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.ProjectService;
import org.unhcr.osmosys.webServices.model.ProjectResumeWeb;
import org.unhcr.osmosys.webServices.model.ProjectWeb;
import org.unhcr.osmosys.webServices.model.QuarterStateWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/projects")
@RequestScoped
public class ProjectEndpoint {

    @Inject
    ProjectService projectService;

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

    @Path("/blockQuarterStateByProjectId/{projectId}")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuarterStateWeb> blockQuartersStateByProjectId(
            @PathParam("projectId") Long projectId, QuarterStateWeb quarterStateWeb
    ) {
        return this.projectService.blockQuarterStateByProjectId(projectId,quarterStateWeb);
    }

}