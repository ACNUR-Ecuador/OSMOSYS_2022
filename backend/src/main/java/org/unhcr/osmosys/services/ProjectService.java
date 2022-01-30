package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.CantonDao;
import org.unhcr.osmosys.daos.ProjectDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.webServices.model.CantonWeb;
import org.unhcr.osmosys.webServices.model.ProjectResumeWeb;
import org.unhcr.osmosys.webServices.model.ProjectWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class ProjectService {

    @Inject
    ProjectDao projectDao;
    @Inject
    CantonDao cantonDao;

    @Inject
    PeriodService periodService;

    @Inject
    ProjectLocationAssigmentService projectLocationAssigmentService;

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    UserService userService;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    DateUtils dateUtils;

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


        Project project = new Project();
        project.setName(projectWeb.getName());
        project.setState(projectWeb.getState());
        project.setCode(projectWeb.getCode());
        Period period = this.periodService.getWithGeneralIndicatorById(projectWeb.getPeriod().getId());
        project.setPeriod(period);
        project.setOrganization(this.modelWebTransformationService.organizationWebToOrganization(projectWeb.getOrganization()));
        project.setStartDate(projectWeb.getStartDate());
        project.setEndDate(projectWeb.getEndDate());
        User focalPoint = null;
        if (projectWeb.getFocalPoint() != null) {
            focalPoint = this.userService.getById(projectWeb.getFocalPoint().getId());
        }

        project.setFocalPoint(focalPoint);
        List<Long> idsCanton = projectWeb.getLocations().stream().map(cantonWeb -> cantonWeb.getId()).collect(Collectors.toList());
        List<Canton> cantones = this.cantonDao.getByIds(idsCanton);
        for (Canton canton : cantones) {
            ProjectLocationAssigment projectLocationAssigment = new ProjectLocationAssigment();
            projectLocationAssigment.setLocation(canton);
            projectLocationAssigment.setState(State.ACTIVO);
            project.addProjectLocationAssigment(projectLocationAssigment);
        }

        // veo si hay q crear general indicator
        IndicatorExecution generalIndicatorIE = this.indicatorExecutionService.createGeneralIndicatorForProject(project);
        project.addIndicatorExecution(generalIndicatorIE);
        this.saveOrUpdate(project);
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
        if (project.getStartDate().compareTo(projectWeb.getStartDate()) != 0 || project.getEndDate().compareTo(projectWeb.getEndDate()) != 0) {
            projectDatesChanged = Boolean.TRUE;
            project.setStartDate(projectWeb.getStartDate());
            project.setEndDate(projectWeb.getEndDate());
        }


        // las localidades
        updateProjectLocations(projectWeb, project, projectWeb.getUpdateAllLocationsIndicators());

        this.saveOrUpdate(project);
        if (projectDatesChanged) {
            this.indicatorExecutionService.updateIndicatorExecutionProjectDates(project, project.getStartDate(), project.getEndDate());
        }
        return project.getId();
    }


    private void updateProjectLocations(ProjectWeb projectWeb, Project project, Boolean updateAllLocationsIndicators) throws GeneralAppException {
        // veo las q ya no están y desactivo
        List<Long> locationsToActive = new ArrayList<>();
        List<Long> locationsToDissable = new ArrayList<>();

        project.getProjectLocationAssigments().forEach(projectLocationAssigment -> {
            Optional<CantonWeb> cantonFound = projectWeb.getLocations().stream().filter(cantonWeb -> {
                return projectLocationAssigment.getLocation().getId().equals(cantonWeb.getId());
            }).findFirst();
            if (!cantonFound.isPresent()) {
                projectLocationAssigment.setState(State.INACTIVO);
                locationsToDissable.add(projectLocationAssigment.getLocation().getId());
            }
        });
        // veo las nuevas y creo hago update si es el caso
        List<Canton> cantonesToCreate = new ArrayList<>();
        projectWeb.getLocations().forEach(cantonWeb -> {
            Optional<ProjectLocationAssigment> assignmentFound = project.getProjectLocationAssigments().stream().filter(projectLocationAssigment -> projectLocationAssigment.getLocation().getId().equals(cantonWeb.getId())).findFirst();
            if (assignmentFound.isPresent()) {
                assignmentFound.get().setState(State.ACTIVO);
                locationsToActive.add(assignmentFound.get().getLocation().getId());
            } else {
                Canton canton = this.cantonDao.find(cantonWeb.getId());
                ProjectLocationAssigment projectLocationAssigment = new ProjectLocationAssigment();
                projectLocationAssigment.setLocation(canton);
                projectLocationAssigment.setState(State.ACTIVO);
                project.addProjectLocationAssigment(projectLocationAssigment);
                cantonesToCreate.add(canton);
                locationsToActive.add(canton.getId());
            }
        });


        for (IndicatorExecution indicatorExecution : project.getIndicatorExecutions()) {

            this.indicatorExecutionService.updateIndicatorExecutionLocationsByAssignation(indicatorExecution, cantonesToCreate);
            // tengo q activar y desactivar
            // si es general siempre activo y desactivo
            if (indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
                indicatorExecution.getQuarters()
                        .forEach(quarter -> {
                            quarter.getMonths().forEach(month -> {
                                month.getIndicatorValues().forEach(indicatorValue -> {
                                    if (DissagregationType.getLocationDissagregationTypes().contains(indicatorValue.getDissagregationType())) {
                                        // busco para activar
                                        if (locationsToActive.contains(indicatorValue.getLocation().getId())) {
                                            indicatorValue.setState(State.ACTIVO);
                                        } else if (locationsToDissable.contains(indicatorValue.getLocation().getId())) {
                                            indicatorValue.setState(State.INACTIVO);
                                        }
                                    }
                                });
                            });
                        });
            } else {
                // si es rendimiento siempre desactivo desactivo y activo solo si updateAllLocationsIndicators
                indicatorExecution.getQuarters()
                        .forEach(quarter -> {
                            quarter.getMonths().forEach(month -> {
                                month.getIndicatorValues().forEach(indicatorValue -> {
                                    if (DissagregationType.getLocationDissagregationTypes().contains(indicatorValue.getDissagregationType())) {
                                        // busco para activar
                                        if (locationsToActive.contains(indicatorValue.getLocation().getId()) && updateAllLocationsIndicators) {
                                            indicatorValue.setState(State.ACTIVO);
                                        } else if (locationsToActive.contains(indicatorValue.getLocation().getId()) && !updateAllLocationsIndicators) {
                                            indicatorValue.setState(State.INACTIVO);
                                        } else if (locationsToDissable.contains(indicatorValue.getLocation().getId())) {
                                            indicatorValue.setState(State.INACTIVO);
                                        }
                                    }
                                });
                            });
                        });
            }
        }
    }


    public List<ProjectWeb> getAll() {
        List<ProjectWeb> r = new ArrayList<>();
        return this.modelWebTransformationService.projectsToProjectsWeb(this.projectDao.findAll());
    }


    public List<ProjectWeb> getByState(State state) {
        List<ProjectWeb> r = new ArrayList<>();
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
            throw new GeneralAppException("No tiene asignado lugares de trabajo", Response.Status.BAD_REQUEST);
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
        if (projectWeb.getState() == null || projectWeb.getEndDate() == null) {
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

        itemRecovered = this.projectDao.getByName(projectWeb.getName());
        if (itemRecovered != null) {
            if (projectWeb.getId() == null || !projectWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un proyecto con esta descripción corta", Response.Status.BAD_REQUEST);
            }
        }

    }


    public List<ProjectResumeWeb> getProjectResumenWebByPeriodId(Long periodId) throws GeneralAppException {
        return this.projectDao.getProjectResumenWebByPeriodId(periodId);
    }

    public ProjectWeb getWebById(Long id) {
        return this.modelWebTransformationService.projectToProjectWeb(this.projectDao.find(id));
    }

    public List<ProjectWeb> getWebByIds(List<Long> ids) {
        return this.modelWebTransformationService.projectsToProjectsWeb(this.projectDao.getByIds(ids));
    }

    public List<Project> getByPeriodId(Long periodId){
        return this.projectDao.getByPeriodId(periodId);
    }

    public void createProjectGeneralStatements(Long periodId) throws GeneralAppException {
        List<Project> projects = this.getByPeriodId(periodId);
        for (Project project : projects) {
            IndicatorExecution ieg = this.indicatorExecutionService.createGeneralIndicatorForProject(project);
            this.indicatorExecutionService.saveOrUpdate(ieg);
        }
    }
}
