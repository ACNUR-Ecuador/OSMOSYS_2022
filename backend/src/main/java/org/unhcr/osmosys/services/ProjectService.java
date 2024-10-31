package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.AuditAction;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.CustomPrincipal;
import com.sagatechs.generics.security.UserSecurityContext;
import com.sagatechs.generics.security.dao.UserDao;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.ProjectDao;
import org.unhcr.osmosys.daos.standardDissagregations.StandardDissagregationOptionDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.QuarterEnum;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class ProjectService {

    @Inject
    ProjectDao projectDao;
    @Inject
    StandardDissagregationOptionDao standardDissagregationOptionDao;

    @Inject
    PeriodService periodService;

    @Inject
    QuarterService quarterService;

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    UserService userService;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    MonthService monthService;

    @Context
    private SecurityContext securityContext;

    @Inject
    UserDao userDao;

    @Inject
    AuditService auditService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(ProjectService.class);

    public Project getById(Long id) {
        return this.projectDao.find(id);
    }

    public Project saveOrUpdate(Project project) {
        if (project.getId() == null) {
            this.projectDao.save(project);
        } else {
            this.projectDao.update(project);
        }
        return project;
    }

    public Long save(ProjectWeb projectWeb) throws GeneralAppException {

        if (projectWeb == null) {
            throw new GeneralAppException("No se puede guardar un project null", Response.Status.BAD_REQUEST);
        }
        if (projectWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un project con id", Response.Status.BAD_REQUEST);
        }
        this.validate(projectWeb);

        UserWeb principal = UserSecurityContext.getCurrentUser();

        User responsibleUser = principal != null ? userDao.findByUserName(principal.getUsername()) : null;


        Project project = new Project();
        project.setName(projectWeb.getName());
        project.setState(projectWeb.getState());
        project.setCode(projectWeb.getCode());
        Period period = this.periodService.getWithAllDataById(projectWeb.getPeriod().getId());
        project.setPeriod(period);
        project.setOrganization(this.modelWebTransformationService.organizationWebToOrganization(projectWeb.getOrganization()));
        project.setStartDate(projectWeb.getStartDate());
        project.setEndDate(projectWeb.getEndDate());
        User focalPoint = null;
        if (projectWeb.getFocalPoint() != null) {
            focalPoint = this.userService.getById(projectWeb.getFocalPoint().getId());
        }

        project.setFocalPoint(focalPoint);
        List<Long> idsCanton = projectWeb.getLocations().stream().map(CantonWeb::getId).collect(Collectors.toList());
        List<Canton> cantones = this.standardDissagregationOptionDao.getCantonByIds(idsCanton);
        for (Canton canton : cantones) {
            ProjectLocationAssigment projectLocationAssigment = new ProjectLocationAssigment();
            projectLocationAssigment.setLocation(canton);
            projectLocationAssigment.setState(State.ACTIVO);
            project.addProjectLocationAssigment(projectLocationAssigment);
        }

        // veo si hay q crear general indicator
        if (period.getGeneralIndicator() != null) {
            IndicatorExecution generalIndicatorIE = this.indicatorExecutionService.createGeneralIndicatorForProject(project);
            project.addIndicatorExecution(generalIndicatorIE);
        }
        this.saveOrUpdate(project);

        // Registrar auditoría
        auditService.logAction("Proyecto", project.getId(), AuditAction.INSERT,responsibleUser , null, project, State.ACTIVO);
        return project.getId();
    }

    public Long update(ProjectWeb projectWeb) throws GeneralAppException {
        if (projectWeb == null) {
            throw new GeneralAppException("No se puede actualizar un proyecto null", Response.Status.BAD_REQUEST);
        }
        if (projectWeb.getId() == null) {
            throw new GeneralAppException("No se puede actualizar un proyecto sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(projectWeb);
        Project project = this.projectDao.find(projectWeb.getId());

        Project oldProject = this.projectDao.find(projectWeb.getId()).deepCopy();
        UserWeb principal = UserSecurityContext.getCurrentUser();
        User responsibleUser = principal != null ? userDao.findByUserName(principal.getUsername()) : null;

        if (project == null) {
            throw new GeneralAppException("No se puede encontrar el proyecto con id " + projectWeb.getId(), Response.Status.NOT_FOUND);
        }
        project.setOrganization(this.modelWebTransformationService.organizationWebToOrganization(projectWeb.getOrganization()));
        project.setCode(projectWeb.getCode());
        project.setPeriod(this.modelWebTransformationService.periodWebToPeriod(projectWeb.getPeriod()));
        project.setState(projectWeb.getState());
        project.setName(projectWeb.getName());
        User focalPoint = null;
        if (projectWeb.getFocalPoint() != null) {
            focalPoint = this.userService.getById(projectWeb.getFocalPoint().getId());
        }

        project.setFocalPoint(focalPoint);
        Boolean projectDatesChanged = Boolean.FALSE;
        if (!project.getStartDate().isEqual(projectWeb.getStartDate()) || !project.getEndDate().isEqual(projectWeb.getEndDate())) {
            projectDatesChanged = Boolean.TRUE;
            project.setStartDate(projectWeb.getStartDate());
            project.setEndDate(projectWeb.getEndDate());
        }

        if (projectDatesChanged) {
            this.indicatorExecutionService.updateIndicatorExecutionProjectDates(project, project.getStartDate(), project.getEndDate());
        }
        // las localidades
        this.updateProjectLocations(projectWeb.getLocations(), project.getId());

        // Registrar auditoría
        auditService.logAction("Proyecto", project.getId(), AuditAction.UPDATE, responsibleUser, oldProject, project, State.ACTIVO);

        this.saveOrUpdate(project);



        return project.getId();
    }


    // todo 2024 remove
/*    public void updateProjectLocations(Set<CantonWeb> cantonWebs, Project project, Boolean updateAllLocationsIndicators) throws GeneralAppException {

        LocationToActivateDesativate result = this.setLocationsInProject(project, List.copyOf(cantonWebs));

        if (updateAllLocationsIndicators != null && updateAllLocationsIndicators) {
            for (IndicatorExecution indicatorExecution : project.getIndicatorExecutions()) {
                this.indicatorExecutionService.updateIndicatorExecutionLocationsAssignations(indicatorExecution, result.locationsToActivate, result.locationsToDissable);
            }
        } else {
            List<IndicatorExecution> generalIes = project.getIndicatorExecutions().stream().filter(indicatorExecution -> indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)).collect(Collectors.toList());
            for (IndicatorExecution indicatorExecution : generalIes) {
                this.indicatorExecutionService.updateIndicatorExecutionLocationsAssignations(indicatorExecution, result.locationsToActivate, result.locationsToDissable);
            }
        }
    }*/

    public void updateProjectLocations(Set<CantonWeb> cantonWebs, Long projectId) throws GeneralAppException {
        Project project = this.getById(projectId);
        // recupero el proyecto
        LocationToActivateDesativate locationsToActivateDesactive = this.setLocationsInProject(project, List.copyOf(cantonWebs));
        for (IndicatorExecution indicatorExecution : project.getIndicatorExecutions()) {
            this.indicatorExecutionService.updateIndicatorExecutionsLocations(indicatorExecution, locationsToActivateDesactive.locationsToActivate, locationsToActivateDesactive.locationsToDissable);
        }
    }

    public LocationToActivateDesativate setLocationsInProject(Project project, List<CantonWeb> cantonsWeb) {
        Set<Canton> locationsToActivate = new HashSet<>();
        Set<Canton> locationsToDissable = new HashSet<>();
        // ***********project location assigments
        // busco los cantones a desactivar
        project.getProjectLocationAssigments().forEach(projectLocationAssigment -> {
            Optional<CantonWeb> cantonWebFound = cantonsWeb.stream()
                    .filter(cantonWeb -> projectLocationAssigment.getLocation().getId().equals(cantonWeb.getId())).findFirst();
            if (cantonWebFound.isEmpty()) {
                projectLocationAssigment.setState(State.INACTIVO);
                locationsToDissable.add(projectLocationAssigment.getLocation());
            } else {
                projectLocationAssigment.setState(State.ACTIVO);
                locationsToActivate.add(projectLocationAssigment.getLocation());
            }
        });

        // busco los cantones a activar
        cantonsWeb.forEach(cantonWeb -> {
            Optional<ProjectLocationAssigment> assignmentFound = project.getProjectLocationAssigments()
                    .stream()
                    .filter(projectLocationAssigment ->
                            projectLocationAssigment.getLocation().getId()
                                    .equals(cantonWeb.getId()))
                    .findFirst();
            if (assignmentFound.isPresent()) {
                assignmentFound.get().setState(State.ACTIVO);
                locationsToActivate.add(assignmentFound.get().getLocation());
            } else {
                Canton canton = (Canton) this.standardDissagregationOptionDao.find(cantonWeb.getId());
                ProjectLocationAssigment projectLocationAssigment = new ProjectLocationAssigment();
                projectLocationAssigment.setLocation(canton);
                projectLocationAssigment.setState(State.ACTIVO);
                project.addProjectLocationAssigment(projectLocationAssigment);
                locationsToActivate.add(canton);
            }
        });
        return new LocationToActivateDesativate(locationsToActivate, locationsToDissable);
    }

    public List<CantonWeb> getProjectCantonAsignations(Long projectId) {

        Project project = this.projectDao.find(projectId);
        Set<ProjectLocationAssigment> projectLocationsAsignations = project.getProjectLocationAssigments();
        List<Canton> activeCantons =
                projectLocationsAsignations.stream()
                        .filter(projectLocationAssigment -> projectLocationAssigment.getState().equals(State.ACTIVO))
                        .map(ProjectLocationAssigment::getLocation)
                        .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
                        .sorted((o1, o2) -> o1.getProvincia().getDescription().compareToIgnoreCase(o2.getProvincia().getDescription()))
                        .collect(Collectors.toList());
        return this.modelWebTransformationService.cantonsToCantonsWeb(activeCantons);

    }

    static class LocationToActivateDesativate {
        public final Set<Canton> locationsToActivate;
        public final Set<Canton> locationsToDissable;

        public LocationToActivateDesativate(Set<Canton> locationsToActivate, Set<Canton> locationsToDissable) {
            this.locationsToActivate = locationsToActivate;
            this.locationsToDissable = locationsToDissable;
        }
    }


    public List<ProjectWeb> getAll() {
        return this.modelWebTransformationService.projectsToProjectsWeb(this.projectDao.findAll());
    }


    public List<ProjectWeb> getByState(State state) {
        return this.modelWebTransformationService.projectsToProjectsWeb(this.projectDao.getByState(state));
    }


    public void validate(ProjectWeb projectWeb) throws GeneralAppException {
        if (projectWeb == null) {
            throw new GeneralAppException("Proyecto es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(projectWeb.getCode())) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(projectWeb.getName())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(projectWeb.getLocations())) {
            throw new GeneralAppException("No tiene asignado sitios/cantones de trabajo", Response.Status.BAD_REQUEST);
        }
        if (projectWeb.getOrganization() == null) {
            throw new GeneralAppException("No tiene asignado una organización", Response.Status.BAD_REQUEST);
        }
        if (projectWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }
        if (projectWeb.getPeriod() == null) {
            throw new GeneralAppException("Periodo no válido", Response.Status.BAD_REQUEST);
        }
        if (projectWeb.getStartDate() == null || projectWeb.getEndDate() == null) {
            throw new GeneralAppException("Las fechas de inicio y fin del proyecto son datos obligatorios", Response.Status.BAD_REQUEST);
        } else {
            if (projectWeb.getEndDate().isBefore(projectWeb.getStartDate())) {
                throw new GeneralAppException("La fecha de fin del proyecto debe ser posterior a la fecha de inicio", Response.Status.BAD_REQUEST);
            }
        }

        Project itemRecovered = this.projectDao.getByCode(projectWeb.getCode());
        if (itemRecovered != null) {
            if (projectWeb.getId() == null || !projectWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un proyecto con este código", Response.Status.BAD_REQUEST);
            }
        }

        itemRecovered = this.projectDao.getByNameAndPeriodId(projectWeb.getName(), projectWeb.getPeriod().getId());
        if (itemRecovered != null) {
            if (projectWeb.getId() == null || !projectWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un proyecto con esta descripción corta", Response.Status.BAD_REQUEST);
            }
        }

    }


    public List<ProjectResumeWeb> getProjectResumenWebByPeriodId(Long periodId) {
        return this.projectDao.getProjectResumenWebByPeriodId(periodId);
    }

    public List<ProjectResumeWeb> getProjectResumenWebByPeriodIdAndOrganizationId(Long periodId, Long organizationId) {
        return this.projectDao.getProjectResumenWebByPeriodIdAndOrganizationId(periodId, organizationId);
    }

    public List<ProjectResumeWeb> getProjectResumenWebByPeriodIdAndFocalPointId(Long periodId, Long focalPointId) {
        return this.projectDao.getProjectResumenWebByPeriodIdAndFocalPointId(periodId, focalPointId);
    }

    public ProjectWeb getWebById(Long id) {
        Project project = this.projectDao.findWithData(id);
        return this.modelWebTransformationService.projectToProjectWeb(project);
    }

    public List<ProjectWeb> getWebByIds(List<Long> ids) {
        return this.modelWebTransformationService.projectsToProjectsWeb(this.projectDao.getByIds(ids));
    }

    public List<Project> getByPeriodId(Long periodId) {
        return this.projectDao.getByPeriodId(periodId);
    }

    public void createProjectGeneralStatements(Long periodId) throws GeneralAppException {
        List<Project> projects = this.getByPeriodId(periodId);
        for (Project project : projects) {
            IndicatorExecution ieg = this.indicatorExecutionService.createGeneralIndicatorForProject(project);
            this.indicatorExecutionService.saveOrUpdate(ieg);
        }
    }

    public List<Project> getByPeriodIdWithDataToUpdateGeneralIndicator(Long periodId) {
        return this.projectDao.getByPeriodIdWithDataToUpdateGeneralIndicator(periodId);
    }

    public List<QuarterStateWeb> getQuartersStateByProjectId(Long projectId) {
        return this.projectDao.getQuartersStateByProjectId(projectId);
    }

    public List<MonthStateWeb> getMonthsStateByProjectId(Long projectId) {
        return this.projectDao.getMonthsStateByProjectId(projectId);
    }

    public List<QuarterStateWeb> blockQuarterStateByProjectId(Long projectId, QuarterStateWeb quarterStateWeb) {
        this.quarterService.blockQuarterStateByProjectId(projectId, QuarterEnum.valueOf(quarterStateWeb.getQuarter()), quarterStateWeb.getYear(), quarterStateWeb.getBlockUpdate());
        return this.getQuartersStateByProjectId(projectId);
    }

    public void changeMonthStateByProjectId(Long projectId, MonthStateWeb monthStateWeb) {
        // recupero todos los meses del proyecto
        this.monthService.getActiveMonthsByProjectIdAndMonthAndYear(projectId, monthStateWeb.getMonth(), monthStateWeb.getYear(), monthStateWeb.getBlockUpdate());
    }

    public Project getByCode(String code) throws GeneralAppException {
        return this.projectDao.getByCode(code);
    }

    public List<User> getFocalPointByPeriodId(Long periodId) {
        return this.projectDao.getFocalPointByPeriodId(periodId);
    }

    public User getCurrentUser(HttpServletRequest request) {
        CustomPrincipal principal = (CustomPrincipal) request.getAttribute("userPrincipal");
        if (principal != null) {
            UserWeb userWeb = principal.getUser();
            User responsibleUser = userWeb != null ? userDao.findByUserName(userWeb.getUsername()) : null;
            return responsibleUser;
        } else {
            throw new IllegalStateException("No authenticated user found.");

        }
    }
}
