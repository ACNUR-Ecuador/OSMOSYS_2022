package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.service.AsyncService;
import com.sagatechs.generics.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.threeten.extra.YearQuarter;
import org.unhcr.osmosys.daos.IndicatorDao;
import org.unhcr.osmosys.daos.IndicatorExecutionDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.*;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class IndicatorExecutionService {

    @Inject
    IndicatorExecutionDao indicatorExecutionDao;

    @Inject
    GeneralIndicatorService generalIndicatorService;

    @Inject
    QuarterService quarterService;

    @Inject
    MonthService monthService;

    @Inject
    IndicatorDao indicatorDao;

    @Inject
    ProjectService projectService;

    @Inject
    DateUtils dateUtils;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    StatementService statementService;

    @Inject
    PeriodService periodService;

    @Inject
    UserService userService;

    @Inject
    OfficeService officeService;

    @Inject
    CantonService cantonService;

    @Inject
    IndicatorValueService indicatorValueService;

    @Inject
    UtilsService utilsService;

    @EJB
    AsyncService asyncService;
    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(IndicatorExecutionService.class);

    public IndicatorExecution getById(Long id) {
        return this.indicatorExecutionDao.find(id);
    }

    public IndicatorExecution saveOrUpdate(IndicatorExecution indicatorExecution) {
        if (indicatorExecution.getId() == null) {
            this.indicatorExecutionDao.save(indicatorExecution);
        } else {
            this.indicatorExecutionDao.update(indicatorExecution);
        }
        return indicatorExecution;
    }


    public IndicatorExecution createGeneralIndicatorForProject(Project project) throws GeneralAppException {
        IndicatorExecution ie = new IndicatorExecution();
        ie.setCompassIndicator(false);
        //target
        ie.setProject(project);
        ie.setIndicatorType(IndicatorType.GENERAL);
        ie.setTarget(null);
        ie.setPeriod(project.getPeriod());
        ie.setState(State.ACTIVO);
        ie.setKeepBudget(false);


        List<Canton> cantones = project.getProjectLocationAssigments().stream().filter(projectLocationAssigment -> projectLocationAssigment.getState().equals(State.ACTIVO)).map(ProjectLocationAssigment::getLocation).collect(Collectors.toList());
        List<DissagregationType> dissagregationTypes;
        GeneralIndicator generalIndicator = this.generalIndicatorService.getByPeriodIdAndState(project.getPeriod().getId(), State.ACTIVO);

        if (generalIndicator != null) {
            Set<DissagregationAssignationToGeneralIndicator> dissagregations = generalIndicator.getDissagregationAssignationsToGeneralIndicator();
            if (CollectionUtils.isNotEmpty(dissagregations)) {
                dissagregationTypes = dissagregations.stream()
                        .filter(dissagregationAssignationToGeneralIndicator -> dissagregationAssignationToGeneralIndicator.getState().equals(State.ACTIVO)).map(DissagregationAssignationToGeneralIndicator::getDissagregationType)
                        .collect(Collectors.toList());
            } else {
                dissagregationTypes = new ArrayList<>();
            }

        } else {
            dissagregationTypes = new ArrayList<>();
        }

        // guardos las segregaciones asignadas
        dissagregationTypes.forEach(dissagregationType -> {
            DissagregationAssignationToIndicatorExecution da = new DissagregationAssignationToIndicatorExecution();
            da.setDissagregationType(dissagregationType);
            da.setState(State.ACTIVO);
            ie.addDissagregationAssignationToIndicatorExecution(da);
        });

        List<CustomDissagregation> customDissagregations = new ArrayList<>();
        createQuartersInIndicatorExecution(ie, project, cantones, dissagregationTypes, customDissagregations);
        return ie;

    }

    public IndicatorExecution assignPerformanceIndicatoToProject(IndicatorExecutionAssigmentWeb indicatorExecutionWeb) throws GeneralAppException {
        this.validatePerformanceIndicatorAssignationToProject(indicatorExecutionWeb);
        IndicatorExecution ie = new IndicatorExecution();
        Indicator indicator = this.indicatorDao.find(indicatorExecutionWeb.getIndicator().getId());
        if (indicator == null) {
            throw new GeneralAppException("Indicador no encontrado " + indicatorExecutionWeb.getIndicator().getId(), Response.Status.BAD_REQUEST);
        }
        ie.setIndicator(indicator);
        ie.setCompassIndicator(indicator.getCompassIndicator());
        ie.setIndicatorType(indicator.getIndicatorType());
        ie.setState(indicatorExecutionWeb.getState());
        ie.setActivityDescription(indicatorExecutionWeb.getActivityDescription());
        ie.setProjectStatement(this.statementService.getById(indicatorExecutionWeb.getProjectStatement().getId()));
        ie.setKeepBudget(indicatorExecutionWeb.getKeepBudget());
        ie.setAssignedBudget(indicatorExecutionWeb.getAssignedBudget());
        Project project = this.projectService.getById(indicatorExecutionWeb.getProject().getId());
        if (project == null) {
            throw new GeneralAppException("Proyecto no encontrado " + indicatorExecutionWeb.getProject().getId(), Response.Status.BAD_REQUEST);
        }
        ie.setPeriod(project.getPeriod());
        @SuppressWarnings("DuplicatedCode")
        List<DissagregationAssignationToIndicatorExecution> dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator().stream().filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getState().equals(State.ACTIVO) && dissagregationAssignationToIndicator.getPeriod().getId().equals(ie.getPeriod().getId())).map(dissagregationAssignationToIndicator -> {
            DissagregationAssignationToIndicatorExecution da = new DissagregationAssignationToIndicatorExecution();
            da.setState(State.ACTIVO);
            da.setDissagregationType(dissagregationAssignationToIndicator.getDissagregationType());
            return da;
        }).collect(Collectors.toList());
        //noinspection DuplicatedCode
        dissagregationAssignations.forEach(ie::addDissagregationAssignationToIndicatorExecution);
        List<CustomDissagregationAssignationToIndicatorExecution> customDissagregationsAssignations = indicator.getCustomDissagregationAssignationToIndicators().stream().filter(customDissagregationAssignationToIndicator -> customDissagregationAssignationToIndicator.getState().equals(State.ACTIVO) && customDissagregationAssignationToIndicator.getPeriod().getId().equals(ie.getPeriod().getId())).map(customDissagregationAssignationToIndicator -> {
            CustomDissagregationAssignationToIndicatorExecution da = new CustomDissagregationAssignationToIndicatorExecution();
            da.setCustomDissagregation(customDissagregationAssignationToIndicator.getCustomDissagregation());
            da.setState(State.ACTIVO);
            return da;
        }).collect(Collectors.toList());
        customDissagregationsAssignations.forEach(ie::addCustomDissagregationAssignationToIndicatorExecution);
        //TODO FILTERS

        ie.setProject(project);
        // TODO MARKERS
        List<Canton> cantones = new ArrayList<>();
        if (indicatorExecutionWeb.getLocations().size() > 0) {
            cantones = this.modelWebTransformationService.cantonsWebToCantons(indicatorExecutionWeb.getLocations());

        }
        List<DissagregationType> dissagregationTypes;

        dissagregationTypes = dissagregationAssignations.stream().map(DissagregationAssignationToIndicatorExecution::getDissagregationType).collect(Collectors.toList());

        // location assignations
        Set<ProjectLocationAssigment> projectLocationAsignations = project.getProjectLocationAssigments();
        for (ProjectLocationAssigment projectLocationAsignation : projectLocationAsignations) {
            IndicatorExecutionLocationAssigment iela = new IndicatorExecutionLocationAssigment();
            iela.setState(projectLocationAsignation.getState());
            iela.setLocation(projectLocationAsignation.getLocation());
            ie.addIndicatorExecutionLocationAssigment(iela);
        }

        List<CustomDissagregation> customDissagregations = customDissagregationsAssignations.stream().map(CustomDissagregationAssignationToIndicatorExecution::getCustomDissagregation).collect(Collectors.toList());
        createQuartersInIndicatorExecution(ie, project, cantones, dissagregationTypes, customDissagregations);


        this.saveOrUpdate(ie);
        return ie;

    }

    private void createQuartersInIndicatorExecution(IndicatorExecution ie, Project project, List<Canton> cantones, List<DissagregationType> dissagregationTypes, List<CustomDissagregation> customDissagregations) throws GeneralAppException {
        Set<Quarter> qs = this.quarterService.createQuarters(project.getStartDate(), project.getEndDate(), dissagregationTypes, customDissagregations, cantones);
        this.validateLocationsSegregationsAndCantons(dissagregationTypes, cantones);
        List<Quarter> qsl = setOrderInQuartersAndMonths(qs);
        for (Quarter quarter : qsl) {
            ie.addQuarter(quarter);
        }
    }

    private List<Quarter> setOrderInQuartersAndMonths(Set<Quarter> qs) {
        List<Quarter> qsl = qs.stream().sorted((o1, o2) -> {
            if (o1.getYear() < o2.getYear()) {
                return -1;
            } else if (o1.getYear() > o2.getYear()) {
                return 1;
            } else return Integer.compare(o1.getQuarter().getOrder(), o2.getQuarter().getOrder());
        }).collect(Collectors.toList());

        int orderQ = 1;
        for (Quarter quarter : qsl) {
            quarter.setOrder(orderQ);
            orderQ++;
        }
        List<Month> monthList =
                qs.stream()
                        .map(Quarter::getMonths)
                        .flatMap(Collection::stream).sorted((o1, o2) -> {
                            if (o1.getYear() < o2.getYear()) {
                                return -1;
                            } else if (o1.getYear() > o2.getYear()) {
                                return 1;
                            } else return Integer.compare(o1.getMonth().getOrder(), o2.getMonth().getOrder());
                        }).collect(Collectors.toList());
        int orderM = 1;
        for (Month month : monthList) {
            month.setOrder(orderM);
            orderM++;
        }
        return qsl;
    }


    public List<IndicatorExecutionWeb> getGeneralIndicatorExecutionsAdministrationByProjectId(Long projectId) throws GeneralAppException {
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getGeneralIndicatorExecutionsByProjectId(projectId);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(ies, true);
    }

    public List<IndicatorExecutionWeb> getGeneralIndicatorExecutionsByProjectIdAndState(Long projectId, State state) throws GeneralAppException {
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getGeneralIndicatorExecutionsByProjectIdAndState(projectId, state);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(ies, true);
    }

    public List<IndicatorExecutionWeb> getPerformanceIndicatorExecutionsByProjectId(Long projectId, State state) throws GeneralAppException {
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getPerformanceIndicatorExecutionsByProjectIdAndState(projectId, state);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(ies, true);
    }

    public void updateTargets(TargetUpdateDTOWeb targetUpdateDTOWeb) throws GeneralAppException {
        IndicatorExecution ie = this.indicatorExecutionDao.getByIdWithIndicatorValues(targetUpdateDTOWeb.getIndicatorExecutionId());
        if (ie.getIndicatorType().equals(IndicatorType.GENERAL)) {
            ie.setTarget(targetUpdateDTOWeb.getTotalTarget());
        } else {
            for (QuarterWeb quarterResumeWeb : targetUpdateDTOWeb.getQuarters()) {
                Optional<Quarter> quarterOptional = ie.getQuarters().stream().filter(quarter -> quarter.getId().equals(quarterResumeWeb.getId())).findFirst();
                if (!quarterOptional.isPresent()) {
                    throw new GeneralAppException("No se pudo encontrar el trimestre con id " + quarterResumeWeb.getId(), Response.Status.NOT_FOUND);
                } else {
                    quarterOptional.get().setTarget(quarterResumeWeb.getTarget());
                }
            }
        }


        this.updateIndicatorExecutionTotals(ie);

        this.saveOrUpdate(ie);
    }


    public void updateIndicatorExecutionTotals(IndicatorExecution indicatorExecution) throws GeneralAppException {
        TotalIndicatorCalculationType totalIndicatorCalculationType;
        if (indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
            totalIndicatorCalculationType = TotalIndicatorCalculationType.SUMA;
        } else if (indicatorExecution.getIndicator() != null && indicatorExecution.getIndicator().getTotalIndicatorCalculationType() != null) {
            totalIndicatorCalculationType = indicatorExecution.getIndicator().getTotalIndicatorCalculationType();
        } else {
            throw new GeneralAppException("Tipo de calculo inválido para el indicador ");
        }
        for (Quarter quarter : indicatorExecution.getQuarters()) {
            this.quarterService.updateQuarterTotals(quarter, totalIndicatorCalculationType);
        }
        List<Quarter> quarters = indicatorExecution.getQuarters().stream().filter(quarter -> quarter.getState().equals(State.ACTIVO)).collect(Collectors.toList());
        if (!indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
            // target total
            Optional<BigDecimal> totalTargetOpt;
            totalTargetOpt = quarters.stream().map(Quarter::getTarget).filter(Objects::nonNull).reduce(BigDecimal::add);
            if (totalTargetOpt.isPresent()) {
                indicatorExecution.setTarget(totalTargetOpt.get());
            } else {
                indicatorExecution.setTarget(null);
            }
        }

        // total execution and total percentage
        List<BigDecimal> totalQuarterValues = quarters.stream()
                .map(Quarter::getTotalExecution).filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(totalQuarterValues)) {
            indicatorExecution.setTotalExecution(null);
            indicatorExecution.setExecutionPercentage(null);
        } else {
            BigDecimal totalExecution = this.utilsService.calculetTotalExecution(totalIndicatorCalculationType, totalQuarterValues);
            indicatorExecution.setTotalExecution(totalExecution);

            //noinspection DuplicatedCode
            if (indicatorExecution.getTotalExecution() != null && indicatorExecution.getTarget() != null) {
                if (indicatorExecution.getTarget().compareTo(BigDecimal.ZERO) == 0) {
                    indicatorExecution.setTarget(BigDecimal.ZERO);
                } else {
                    indicatorExecution.setExecutionPercentage(indicatorExecution.getTotalExecution().divide(indicatorExecution.getTarget(), 4, RoundingMode.HALF_UP));
                }
            } else {
                indicatorExecution.setExecutionPercentage(null);
            }
        }
        this.updateIndicatorExecutionBudget(indicatorExecution);
    }

    private void updateIndicatorExecutionBudget(IndicatorExecution indicatorExecution) {
        if (indicatorExecution.getKeepBudget()) {
            Optional<BigDecimal> totalExecutedOpt = indicatorExecution
                    .getQuarters().stream().
                    flatMap(quarter -> quarter.getMonths().stream())
                    .filter(month -> month.getState().equals(State.ACTIVO) && month.getUsedBudget() != null)
                    .map(Month::getUsedBudget)
                    .reduce(BigDecimal::add);
            BigDecimal totalExecuted = totalExecutedOpt.orElse(BigDecimal.ZERO);
            BigDecimal assignedBudget = indicatorExecution.getAssignedBudget() != null ? indicatorExecution.getAssignedBudget() : BigDecimal.ZERO;
            BigDecimal result = assignedBudget.subtract(totalExecuted).setScale(2, RoundingMode.HALF_UP);
            indicatorExecution.setAvailableBudget(result);
            indicatorExecution.setTotalUsedBudget(totalExecuted);

        }
    }

    public List<IndicatorExecutionWeb> getPerformanceIndicatorExecutionsAdministrationByProjectId(Long projectId) throws GeneralAppException {
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getPerformanceIndicatorExecutionsByProjectId(projectId);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(ies, true);
    }

    public void validatePerformanceIndicatorAssignationToProject(IndicatorExecutionAssigmentWeb indicatorExecutionWeb) throws GeneralAppException {
        //noinspection DuplicatedCode
        validatePerformanceIndicatorAssignation(indicatorExecutionWeb);

        if (indicatorExecutionWeb.getProject() == null || indicatorExecutionWeb.getProject().getId() == null) {
            throw new GeneralAppException("El proyecto asignado es nulo.", Response.Status.BAD_REQUEST);
        }
    }

    public IndicatorExecutionWeb getResumeAdministrationPerformanceIndicatorById(Long id, boolean getProject) throws GeneralAppException {
        return this.modelWebTransformationService.
                indicatorExecutionToIndicatorExecutionWeb(this.indicatorExecutionDao.getPerformanceIndicatorExecutionById(id), getProject);
    }

    public Long updateMonthValues(Long indicatorExecutionId, MonthValuesWeb monthValuesWeb) throws GeneralAppException {
        // get indicator execution id
        IndicatorExecution indicatorExecution = this.indicatorExecutionDao.getByIdWithIndicatorValues(indicatorExecutionId);
        if (monthValuesWeb.getMonth() == null || monthValuesWeb.getMonth().getId() == null) {
            throw new GeneralAppException("Llamada mal estructurada (month es nulo)", Response.Status.BAD_REQUEST);
        }
        if (monthValuesWeb.getIndicatorValuesMap() == null || monthValuesWeb.getIndicatorValuesMap().size() < 1) {
            throw new GeneralAppException("Llamada mal estructurada (no valores )", Response.Status.BAD_REQUEST);
        }
        if (indicatorExecution == null) {
            throw new GeneralAppException("No se pudo encontrar el indicador (indicatorExecutionId:" + indicatorExecutionId + ")", Response.Status.BAD_REQUEST);
        }
        // update values
        Month monthToUpdate = null;
        for (Quarter quarter : indicatorExecution.getQuarters()) {
            Optional<Month> monthToUpdateOp = quarter.getMonths().stream()
                    .filter(month -> monthValuesWeb.getMonth().getId().equals(month.getId()))
                    .findFirst();
            if (monthToUpdateOp.isPresent()) {
                monthToUpdate = monthToUpdateOp.get();
                break;
            }
        }
        if (monthToUpdate == null) {
            throw new GeneralAppException("No se pudo encontrar el mes (monthId:" + monthValuesWeb.getMonth().getId() + ")", Response.Status.BAD_REQUEST);
        }
        monthToUpdate.setCommentary(monthValuesWeb.getMonth().getCommentary());
        // para afterupdate
        if (indicatorExecution.getIndicator() != null && indicatorExecution.getIndicator().getBlockAfterUpdate()) {
            monthToUpdate.setBlockUpdate(true);
        } else if (monthValuesWeb.getMonth().getChecked() != null && monthValuesWeb.getMonth().getChecked() != monthToUpdate.getChecked()) {
            if (!monthValuesWeb.getMonth().getChecked()) {
                monthToUpdate.setBlockUpdate(false);
                // send message laertinf parner /responsable
                this.sendReviewDataAlertEmailMessage(indicatorExecution, monthToUpdate);
            } else {
                monthToUpdate.setBlockUpdate(true);
            }

        }

        monthToUpdate.setChecked(monthValuesWeb.getMonth().getChecked());
        monthToUpdate.setSourceOther(monthValuesWeb.getMonth().getSourceOther());
        monthToUpdate.setSources(new HashSet<>());
        monthToUpdate.setUsedBudget(monthValuesWeb.month.getUsedBudget());

        if (CollectionUtils.isNotEmpty(monthValuesWeb.getMonth().getSources())) {

            for (SourceType source : monthValuesWeb.getMonth().getSources()) {
                monthToUpdate.addSource(source);
            }
        }

        List<IndicatorValueWeb> totalIndicatorValueWebs = new ArrayList<>();
        monthValuesWeb.getIndicatorValuesMap().forEach((dissagregationType, indicatorValueWebs) -> {
            if (indicatorValueWebs != null) {
                totalIndicatorValueWebs.addAll(indicatorValueWebs);
            }
        });
        for (IndicatorValueWeb indicatorValueWeb : totalIndicatorValueWebs) {
            Optional<IndicatorValue> valueToUpdateOp = monthToUpdate.getIndicatorValues().stream().filter(indicatorValue -> indicatorValue.getId().equals(indicatorValueWeb.getId())).findFirst();
            if (valueToUpdateOp.isPresent()) {
                IndicatorValue valueToUpdate = valueToUpdateOp.get();
                valueToUpdate.setValue(indicatorValueWeb.getValue());
                valueToUpdate.setNumeratorValue(indicatorValueWeb.getNumeratorValue());
                valueToUpdate.setDenominatorValue(indicatorValueWeb.getDenominatorValue());
            } else {
                throw new GeneralAppException("No se pudo encontrar el valor (valueId:" + indicatorValueWeb.getId() + ")", Response.Status.BAD_REQUEST);
            }
        }
        if (CollectionUtils.isNotEmpty(monthValuesWeb.getCustomDissagregationValues())) {
            List<IndicatorValueCustomDissagregationWeb> totalIndicatorValueCustomDissagregationWebs = new ArrayList<>();
            monthValuesWeb.getCustomDissagregationValues().forEach(customDissagregationValuesWeb -> totalIndicatorValueCustomDissagregationWebs.addAll(customDissagregationValuesWeb.getIndicatorValuesCustomDissagregation()));
            for (IndicatorValueCustomDissagregationWeb totalIndicatorValueCustomDissagregationWeb : totalIndicatorValueCustomDissagregationWebs) {
                Optional<IndicatorValueCustomDissagregation> indicatorValueCustomDissagregationOp = monthToUpdate.getIndicatorValuesIndicatorValueCustomDissagregations().stream().filter(indicatorValueCustomDissagregation -> totalIndicatorValueCustomDissagregationWeb.getId().equals(indicatorValueCustomDissagregation.getId())).findFirst();
                if (indicatorValueCustomDissagregationOp.isPresent()) {
                    IndicatorValueCustomDissagregation valueToUpdate = indicatorValueCustomDissagregationOp.get();
                    valueToUpdate.setValue(totalIndicatorValueCustomDissagregationWeb.getValue());
                    valueToUpdate.setNumeratorValue(totalIndicatorValueCustomDissagregationWeb.getNumeratorValue());
                    valueToUpdate.setDenominatorValue(totalIndicatorValueCustomDissagregationWeb.getDenominatorValue());
                } else {
                    throw new GeneralAppException("No se pudo encontrar el valor (valueId:" + totalIndicatorValueCustomDissagregationWeb.getId() + ")", Response.Status.BAD_REQUEST);
                }
            }
        }
        this.updateIndicatorExecutionTotals(indicatorExecution);
        this.saveOrUpdate(indicatorExecution);
        return indicatorExecution.getId();
    }

    private void sendReviewDataAlertEmailMessage(IndicatorExecution indicatorExecution, Month monthToUpdate) {
        String messageText;
        String destinationAdress;
        String destinationCopy;
        if (indicatorExecution.getProject() != null) {
            // is for partner
            // al users partners
            Long organizationId = indicatorExecution.getProject().getOrganization().getId();
            List<User> partnerUsers = this.userService.getActivePartnerUsers(organizationId);
            destinationAdress = partnerUsers.stream().map(User::getEmail).collect(Collectors.joining(", "));
            destinationCopy = indicatorExecution.getProject().getFocalPoint().getEmail();
            messageText = "<p>Estimado/a colega:</p> " +
                    "<p>El punto focal de su proyecto ha solicitado la revisi&oacute;n de los siguientes datos:</p> " +
                    "<p>Socio: " + indicatorExecution.getProject().getOrganization().getDescription() + "</p> " +
                    "<p>Proyecto: " + indicatorExecution.getProject().getName() + "</p> " +
                    "<p>Mes: " + monthToUpdate.getMonth() + "</p> " +
                    "<p>Indicador: " +
                    (indicatorExecution.getIndicator() != null ? (indicatorExecution.getIndicator().getCode() + "-" + indicatorExecution.getIndicator().getDescription()) : "Indicador General") + "</p> " +
                    "<p>Para mayor detalle por favor comun&iacute;quese con el punto focal de su proyecto: <a href=\"mailto:" + indicatorExecution.getProject().getFocalPoint().getEmail() + "\">" + indicatorExecution.getProject().getFocalPoint().getName() + "</a></p> " +
                    "<p>Nota: Este correo es generado automaticamente por el Sistema OSMOSYS, por favor no contestar a este remitente.</p>";

        } else {
            destinationAdress = indicatorExecution.getAssignedUser().getEmail();
            destinationCopy = indicatorExecution.getSupervisorUser().getEmail();
            messageText = "<p>Estimado/a colega:</p> " +
                    "<p>Se ha solicitado la revisi&oacute;n de los siguientes datos:</p> " +
                    "<p>Oficina: " + indicatorExecution.getReportingOffice().getDescription() + "</p> " +
                    "<p>Mes: " + monthToUpdate.getMonth() + "</p> " +
                    "<p>Indicador: " +
                    (indicatorExecution.getIndicator() != null ? (indicatorExecution.getIndicator().getCode() + "-" + indicatorExecution.getIndicator().getDescription()) : "Indicador General") + "</p> " +
                    "<p>Para mayor detalle por favor comun&iacute;quese con el verificador de este indicador: <a href=\"mailto:" + indicatorExecution.getSupervisorUser().getEmail() + "\">" + indicatorExecution.getSupervisorUser().getName() + "</a></p> " +
                    "<p>Nota: Este correo es generado automaticamente por el Sistema OSMOSYS, por favor no contestar a este remitente.</p>";

        }
        this.asyncService.sendEmail(destinationAdress, destinationCopy, "Solicitud de Revisiòn de Datos OSMOSYS", messageText);
    }


    public void updateIndicatorExecutionProjectDates(Project project, LocalDate newStartDate, LocalDate newEndDate) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getIndicatorExecutionsByProjectId(project.getId());
        List<Canton> cantones = project.getProjectLocationAssigments().stream().filter(projectLocationAssigment -> projectLocationAssigment.getState().equals(State.ACTIVO)).map(ProjectLocationAssigment::getLocation).collect(Collectors.toList());

        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            List<DissagregationType> dissagregationTypes;
            List<CustomDissagregation> customDissagregations;
            if (indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
                GeneralIndicator generalIndicator = this.generalIndicatorService.getByPeriodIdAndState(project.getPeriod().getId(), State.ACTIVO);
                Set<DissagregationAssignationToGeneralIndicator> dissagregations = generalIndicator.getDissagregationAssignationsToGeneralIndicator();
                if (CollectionUtils.isNotEmpty(dissagregations)) {
                    dissagregationTypes = dissagregations.stream()
                            .filter(dissagregationAssignationToGeneralIndicator -> dissagregationAssignationToGeneralIndicator.getState().equals(State.ACTIVO)).map(DissagregationAssignationToGeneralIndicator::getDissagregationType)
                            .collect(Collectors.toList());
                } else {
                    dissagregationTypes = new ArrayList<>();
                }
                customDissagregations = new ArrayList<>();
            } else {
                dissagregationTypes = indicatorExecution
                        .getDissagregationsAssignationsToIndicatorExecutions().stream()
                        .filter(dissagregationAssignationToIndicatorExecution -> dissagregationAssignationToIndicatorExecution.getState().equals(State.ACTIVO))
                        .map(DissagregationAssignationToIndicatorExecution::getDissagregationType).collect(Collectors.toList());
                customDissagregations = indicatorExecution
                        .getCustomDissagregationAssignationToIndicatorExecutions().stream()
                        .filter(customDissagregationAssignationToIndicatorExecution -> customDissagregationAssignationToIndicatorExecution.getState().equals(State.ACTIVO)).map(CustomDissagregationAssignationToIndicatorExecution::getCustomDissagregation).collect(Collectors.toList());
            }
            Set<Quarter> quartersOrg = indicatorExecution.getQuarters();
            List<YearQuarter> newQuarters = this.dateUtils.calculateQuarter(newStartDate, newEndDate);
            // veo los quarters nuevos
            for (YearQuarter newQuarter : newQuarters) {
                Optional<Quarter> foundQuarterOp = quartersOrg.stream().filter(quarter -> {
                    try {
                        return quarter.getYear().equals(newQuarter.getYear())
                                && quarter.getQuarter().equals(QuarterEnum.getByQuarterNumber(newQuarter.getQuarterValue()));
                    } catch (GeneralAppException e) {
                        return false;
                    }
                }).findFirst();
                if (foundQuarterOp.isPresent()) {
                    Quarter foundQuarter = foundQuarterOp.get();
                    foundQuarter.setState(State.ACTIVO);
                    List<MonthEnum> monthsEnums = MonthEnum.getMonthsByQuarter(foundQuarter.getQuarter());
                    monthsEnums = monthsEnums.stream().filter(monthEnum -> {
                        LocalDate firstDay = LocalDate.of(foundQuarter.getYear(), monthEnum.getOrder(), 1);
                        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
                        return
                                firstDay.isBefore(newEndDate.plusDays(1))
                                        && lastDay.isAfter(newStartDate.minusDays(1));

                    }).collect(Collectors.toList());
                    // meses presentes
                    for (Month month : foundQuarter.getMonths()) {
                        Optional<MonthEnum> presentMonth = monthsEnums.stream().filter(monthEnum -> monthEnum.equals(month.getMonth())).findFirst();
                        if (presentMonth.isPresent()) {
                            month.setState(State.ACTIVO);
                            for (IndicatorValue indicatorValue : month.getIndicatorValues()) {
                                indicatorValue.setState(State.ACTIVO);
                            }
                            for (IndicatorValueCustomDissagregation indicatorValuesIndicatorValueCustomDissagregation : month.getIndicatorValuesIndicatorValueCustomDissagregations()) {
                                indicatorValuesIndicatorValueCustomDissagregation.setState(State.ACTIVO);
                            }
                        } else {
                            month.setState(State.INACTIVO);
                            for (IndicatorValue indicatorValue : month.getIndicatorValues()) {
                                indicatorValue.setState(State.INACTIVO);
                            }
                            for (IndicatorValueCustomDissagregation indicatorValuesIndicatorValueCustomDissagregation : month.getIndicatorValuesIndicatorValueCustomDissagregations()) {
                                indicatorValuesIndicatorValueCustomDissagregation.setState(State.INACTIVO);
                            }
                        }
                    }
                    // meses ausentes
                    for (MonthEnum monthEnum : monthsEnums) {
                        Optional<Month> foundMonth = foundQuarter.getMonths().stream().filter(month -> month.getMonth().equals(monthEnum)).findFirst();
                        if (!foundMonth.isPresent()) {
                            // creo mes
                            Month newmonth = this.monthService
                                    .createMonth(foundQuarter.getYear(), monthEnum, dissagregationTypes, customDissagregations, cantones);
                            foundQuarter.addMonth(newmonth);
                        }
                    }


                } else {
                    this.validateLocationsSegregationsAndCantons(dissagregationTypes, cantones);
                    Quarter newCreatedQuarter = this.quarterService.createQuarter(newQuarter, newStartDate, newEndDate, dissagregationTypes, customDissagregations, cantones);
                    indicatorExecution.addQuarter(newCreatedQuarter);
                }
            }
            // veo lo que tengo que desactivar
            for (Quarter quarterOrg : quartersOrg) {
                Optional<YearQuarter> foundOrgQuarter = newQuarters.stream().filter(yearQuarter -> {
                    try {
                        return yearQuarter.getYear() == quarterOrg.getYear()
                                && QuarterEnum.getByQuarterNumber(yearQuarter.getQuarterValue()).equals(quarterOrg.getQuarter());
                    } catch (GeneralAppException e) {
                        return false;
                    }
                }).findFirst();
                if (!foundOrgQuarter.isPresent()) {
                    quarterOrg.setState(State.INACTIVO);
                    for (Month month : quarterOrg.getMonths()) {
                        month.setState(State.INACTIVO);
                        for (IndicatorValue indicatorValue : month.getIndicatorValues()) {
                            indicatorValue.setState(State.INACTIVO);
                        }
                        for (IndicatorValueCustomDissagregation indicatorValuesIndicatorValueCustomDissagregation : month.getIndicatorValuesIndicatorValueCustomDissagregations()) {
                            indicatorValuesIndicatorValueCustomDissagregation.setState(State.INACTIVO);
                        }
                    }
                }
            }
            //order recalculate
            this.setOrderInQuartersAndMonths(indicatorExecution.getQuarters());
            this.updateIndicatorExecutionTotals(indicatorExecution);
            this.saveOrUpdate(indicatorExecution);
        }


    }

    private void validateLocationsSegregationsAndCantons(List<DissagregationType> dissagregationTypes, List<Canton> cantones) throws GeneralAppException {
        for (DissagregationType dissagregationType : dissagregationTypes) {
            if (DissagregationType.getLocationDissagregationTypes().contains(dissagregationType)) {
                if (CollectionUtils.isEmpty(cantones)) {
                    throw new GeneralAppException("No se puede crear una segregaciones de lugar sin cantones ", Response.Status.BAD_REQUEST);
                }
            }
        }
    }

    public void updateIndicatorExecutionLocationsByAssignation(IndicatorExecution indicatorExecution, List<Canton> cantonesToCreate) throws GeneralAppException {
        List<DissagregationType> locationDissagregationTypes;
        if (indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
            locationDissagregationTypes = indicatorExecution
                    .getPeriod()
                    .getGeneralIndicator()
                    .getDissagregationAssignationsToGeneralIndicator()
                    .stream().map(DissagregationAssignationToGeneralIndicator::getDissagregationType).filter(dissagregationType -> DissagregationType.getLocationDissagregationTypes().contains(dissagregationType)).collect(Collectors.toList());
        } else {
            locationDissagregationTypes = indicatorExecution.getDissagregationsAssignationsToIndicatorExecutions()
                    .stream().map(DissagregationAssignationToIndicatorExecution::getDissagregationType)
                    .filter(dissagregationType -> DissagregationType.getLocationDissagregationTypes().contains(dissagregationType))
                    .collect(Collectors.toList());
        }
        for (Quarter quarter : indicatorExecution.getQuarters()) {
            this.quarterService.updateQuarterLocationsByAssignation(quarter, cantonesToCreate, locationDissagregationTypes);
        }
    }

    public Long updateAssignPerformanceIndicatoToProject(IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb) throws GeneralAppException {
        if (indicatorExecutionAssigmentWeb.getId() == null) {
            throw new GeneralAppException("No se pudo encontrar la asignación (Id:" + indicatorExecutionAssigmentWeb.getId() + ")", Response.Status.BAD_REQUEST);
        }
        IndicatorExecution indicatorExecution = this.indicatorExecutionDao.find(indicatorExecutionAssigmentWeb.getId());
        if (indicatorExecution == null) {
            throw new GeneralAppException("No se pudo encontrar la asignación (Id:" + indicatorExecutionAssigmentWeb.getId() + ")", Response.Status.BAD_REQUEST);
        }
        if (!indicatorExecution.getProject().getId().equals(indicatorExecutionAssigmentWeb.getProject().getId())) {
            throw new GeneralAppException("El indicador no corresponde al proyecto (Id:" + indicatorExecutionAssigmentWeb.getId() + " projectId" + indicatorExecutionAssigmentWeb.getId() + ")", Response.Status.BAD_REQUEST);
        }
        indicatorExecution.setState(indicatorExecutionAssigmentWeb.getState());

        indicatorExecution.setProjectStatement(this.statementService.getById(indicatorExecutionAssigmentWeb.getProjectStatement().getId()));
        indicatorExecution.setActivityDescription(indicatorExecutionAssigmentWeb.getActivityDescription());
        indicatorExecution.setKeepBudget(indicatorExecutionAssigmentWeb.getKeepBudget());
        indicatorExecution.setAssignedBudget(indicatorExecutionAssigmentWeb.getAssignedBudget());
        // actualizo locations
        if (CollectionUtils.isNotEmpty(
                indicatorExecution.getDissagregationsAssignationsToIndicatorExecutions().stream()
                        .filter(dissagregationAssignationToIndicatorExecution ->
                                DissagregationType.getLocationDissagregationTypes().contains(dissagregationAssignationToIndicatorExecution.getDissagregationType())
                        ).collect(Collectors.toSet())
        )) {
            // busco las que ya no existen

            List<IndicatorValue> currentIndicatorValuesCantons =
                    indicatorExecution.getQuarters().stream()
                            .flatMap(quarter -> quarter.getMonths().stream())
                            .filter(month -> month.getState().equals(State.ACTIVO))
                            .flatMap(month -> month.getIndicatorValues().stream())
                            .filter(indicatorValue ->
                                    DissagregationType.getLocationDissagregationTypes().contains(indicatorValue.getDissagregationType())
                            )
                            .collect(Collectors.toList());
            List<Canton> currentCantons = currentIndicatorValuesCantons.stream().map(IndicatorValue::getLocation).distinct()
                    .collect(Collectors.toList());
            for (Canton currentCanton : currentCantons) {
                Optional<CantonWeb> exitsCanton = indicatorExecutionAssigmentWeb
                        .getLocations().stream().filter(cantonWeb -> cantonWeb.getId().equals(currentCanton.getId())).findFirst();
                if (exitsCanton.isPresent()) {
                    currentIndicatorValuesCantons.stream().filter(indicatorValue -> indicatorValue.getLocation().getId().equals(currentCanton.getId())).forEach(indicatorValue -> indicatorValue.setState(State.ACTIVO));
                    indicatorExecution.getIndicatorExecutionLocationAssigments()
                            .stream().filter(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.getLocation().getId().equals(currentCanton.getId()))
                            .forEach(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.setState(State.ACTIVO));
                } else {
                    currentIndicatorValuesCantons.stream().filter(indicatorValue -> indicatorValue.getLocation().getId().equals(currentCanton.getId())).forEach(indicatorValue -> indicatorValue.setState(State.INACTIVO));
                    indicatorExecution.getIndicatorExecutionLocationAssigments()
                            .stream().filter(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.getLocation().getId().equals(currentCanton.getId()))
                            .forEach(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.setState(State.INACTIVO));
                }
            }


        }
        this.updateIndicatorExecutionTotals(indicatorExecution);
        this.saveOrUpdate(indicatorExecution);
        return indicatorExecution.getId();
    }


    public void createIndicatorExecForProjects(Long periodId) throws GeneralAppException {
        List<Project> projects = this.projectService.getByPeriodId(periodId);
        for (Project project : projects) {
            List<Canton> cantones = project.getProjectLocationAssigments().stream().map(ProjectLocationAssigment::getLocation).collect(Collectors.toList());
            List<IndicatorExecution> iepi = this.indicatorExecutionDao.getPerformanceIndicatorExecutionsByProjectIdAndState(project.getId(), State.ACTIVO);
            for (IndicatorExecution indicatorExecution : iepi) {
//noinspection DuplicatedCode
                Indicator indicator = indicatorExecution.getIndicator();

                List<DissagregationAssignationToIndicatorExecution> dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator().stream().filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getState().equals(State.ACTIVO) && dissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())).map(dissagregationAssignationToIndicator -> {
                    DissagregationAssignationToIndicatorExecution da = new DissagregationAssignationToIndicatorExecution();
                    da.setState(State.ACTIVO);
                    da.setDissagregationType(dissagregationAssignationToIndicator.getDissagregationType());
                    return da;
                }).collect(Collectors.toList());
                dissagregationAssignations.forEach(indicatorExecution::addDissagregationAssignationToIndicatorExecution);

                List<CustomDissagregationAssignationToIndicatorExecution> customDissagregationsAssignations = indicator.getCustomDissagregationAssignationToIndicators().stream().filter(customDissagregationAssignationToIndicator -> customDissagregationAssignationToIndicator.getState().equals(State.ACTIVO) && customDissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())).map(customDissagregationAssignationToIndicator -> {
                    CustomDissagregationAssignationToIndicatorExecution da = new CustomDissagregationAssignationToIndicatorExecution();
                    da.setCustomDissagregation(customDissagregationAssignationToIndicator.getCustomDissagregation());
                    da.setState(State.ACTIVO);
                    return da;
                }).collect(Collectors.toList());
                customDissagregationsAssignations.forEach(indicatorExecution::addCustomDissagregationAssignationToIndicatorExecution);

                List<DissagregationType> dissagregationTypes;

                dissagregationTypes = dissagregationAssignations.stream().map(DissagregationAssignationToIndicatorExecution::getDissagregationType).collect(Collectors.toList());


                List<CustomDissagregation> customDissagregations = customDissagregationsAssignations.stream().map(CustomDissagregationAssignationToIndicatorExecution::getCustomDissagregation).collect(Collectors.toList());
                this.validateLocationsSegregationsAndCantons(dissagregationTypes, cantones);
                Set<Quarter> qs = this.quarterService.createQuarters(project.getStartDate(), project.getEndDate(), dissagregationTypes, customDissagregations, cantones);
                List<Quarter> qsl = setOrderInQuartersAndMonths(qs);
                for (Quarter quarter : qsl) {
                    indicatorExecution.addQuarter(quarter);
                }

                this.saveOrUpdate(indicatorExecution);
            }
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void createIndicatorExecForID(Long periodId) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getAllIndicatorDirectImplementationNoValues(periodId);
        indicatorExecutions = indicatorExecutions
                .stream()
                .filter(indicatorExecution -> indicatorExecution.getQuarters().size() < 1)
                .collect(Collectors.toList());
        Period period = this.periodService.find(periodId);
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            Indicator indicator = indicatorExecution.getIndicator();

            List<DissagregationAssignationToIndicatorExecution> dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator().stream().filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getState().equals(State.ACTIVO) && dissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())).map(dissagregationAssignationToIndicator -> {
                DissagregationAssignationToIndicatorExecution da = new DissagregationAssignationToIndicatorExecution();
                da.setState(State.ACTIVO);
                da.setDissagregationType(dissagregationAssignationToIndicator.getDissagregationType());
                return da;
            }).collect(Collectors.toList());
            dissagregationAssignations
                    .forEach(indicatorExecution::addDissagregationAssignationToIndicatorExecution);

            List<CustomDissagregationAssignationToIndicatorExecution> customDissagregationsAssignations = indicator.getCustomDissagregationAssignationToIndicators().stream().filter(customDissagregationAssignationToIndicator -> customDissagregationAssignationToIndicator.getState().equals(State.ACTIVO) && customDissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())).map(customDissagregationAssignationToIndicator -> {
                CustomDissagregationAssignationToIndicatorExecution da = new CustomDissagregationAssignationToIndicatorExecution();
                da.setCustomDissagregation(customDissagregationAssignationToIndicator.getCustomDissagregation());
                da.setState(State.ACTIVO);
                return da;
            }).collect(Collectors.toList());
            customDissagregationsAssignations
                    .forEach(indicatorExecution::addCustomDissagregationAssignationToIndicatorExecution);

            List<DissagregationType> dissagregationTypes = dissagregationAssignations.stream()
                    .map(DissagregationAssignationToIndicatorExecution::getDissagregationType)
                    .collect(Collectors.toList());


            List<CustomDissagregation> customDissagregations = customDissagregationsAssignations
                    .stream()
                    .map(CustomDissagregationAssignationToIndicatorExecution::getCustomDissagregation)
                    .collect(Collectors.toList());

            LocalDate startDate = LocalDate.of(period.getYear(), 1, 1);
            LocalDate endDate = LocalDate.of(period.getYear(), 12, 31);

            Set<Quarter> qs = this.quarterService.createQuarters(startDate, endDate, dissagregationTypes, customDissagregations, new ArrayList<>());
            List<Quarter> qsl = setOrderInQuartersAndMonths(qs);
            for (Quarter quarter : qsl) {
                indicatorExecution.addQuarter(quarter);
            }

            this.saveOrUpdate(indicatorExecution);
        }
    }


    public List<IndicatorExecutionWeb> getAllDirectImplementationIndicatorByPeriodId(Long periodId) throws
            GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getDirectImplementationIndicatorByPeriodId(periodId);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(indicatorExecutions, false);
    }

    @SuppressWarnings("DuplicatedCode")
    public Long assignPerformanceIndicatoDirectImplementation(IndicatorExecutionAssigmentWeb
                                                                      indicatorExecutionAssigmentWeb) throws GeneralAppException {
        this.validatePerformanceIndicatorAssignationDirectImplementation(indicatorExecutionAssigmentWeb);

        IndicatorExecution indicatorExecution = new IndicatorExecution();
        Indicator indicator = this.indicatorDao.find(indicatorExecutionAssigmentWeb.getIndicator().getId());
        if (indicator == null) {
            throw new GeneralAppException("Indicador no encontrado " + indicatorExecutionAssigmentWeb.getIndicator().getId(), Response.Status.BAD_REQUEST);
        }
        indicatorExecution.setIndicator(indicator);
        Period period = this.periodService.find(indicatorExecutionAssigmentWeb.getPeriod().getId());
        if (period == null) {
            throw new GeneralAppException("Periodo no encontrado " + indicatorExecutionAssigmentWeb.getPeriod().getId(), Response.Status.BAD_REQUEST);
        }
        indicatorExecution.setCompassIndicator(indicator.getCompassIndicator());
        indicatorExecution.setIndicatorType(indicator.getIndicatorType());
        indicatorExecution.setState(indicatorExecutionAssigmentWeb.getState());
        indicatorExecution.setPeriod(period);
        indicatorExecution.setKeepBudget(indicatorExecutionAssigmentWeb.getKeepBudget());
        indicatorExecution.setAssignedBudget(indicatorExecutionAssigmentWeb.getAssignedBudget());
        Office office = this.officeService.getById(indicatorExecutionAssigmentWeb.getReportingOffice().getId());
        indicatorExecution.setReportingOffice(office);
        if (office == null) {
            throw new GeneralAppException("Oficina no encontrada " + indicatorExecutionAssigmentWeb.getReportingOffice().getId(), Response.Status.BAD_REQUEST);
        }
        this.assigUsersToIndicatorExecution(indicatorExecution, indicatorExecutionAssigmentWeb);
        List<DissagregationAssignationToIndicatorExecution> dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator()
                .stream()
                .filter(dissagregationAssignationToIndicator ->
                        dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                                && dissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())
                ).map(dissagregationAssignationToIndicator -> {
                    DissagregationAssignationToIndicatorExecution da = new DissagregationAssignationToIndicatorExecution();
                    da.setState(State.ACTIVO);
                    da.setDissagregationType(dissagregationAssignationToIndicator.getDissagregationType());
                    return da;
                }).collect(Collectors.toList());
        dissagregationAssignations.forEach(indicatorExecution::addDissagregationAssignationToIndicatorExecution);
        List<CustomDissagregationAssignationToIndicatorExecution> customDissagregationsAssignations =
                indicator.getCustomDissagregationAssignationToIndicators()
                        .stream()
                        .filter(customDissagregationAssignationToIndicator ->
                                customDissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                                        && customDissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())
                        ).map(customDissagregationAssignationToIndicator -> {
                            CustomDissagregationAssignationToIndicatorExecution da = new CustomDissagregationAssignationToIndicatorExecution();
                            da.setCustomDissagregation(customDissagregationAssignationToIndicator.getCustomDissagregation());
                            da.setState(State.ACTIVO);
                            return da;
                        }).collect(Collectors.toList());
        customDissagregationsAssignations.forEach(indicatorExecution::addCustomDissagregationAssignationToIndicatorExecution);
        //TODO FILTERS
        // TODO MARKERS
        List<DissagregationType> dissagregationTypes = dissagregationAssignations
                .stream()
                .map(DissagregationAssignationToIndicatorExecution::getDissagregationType)
                .collect(Collectors.toList());
        List<CustomDissagregation> customDissagregations =
                customDissagregationsAssignations
                        .stream()
                        .map(CustomDissagregationAssignationToIndicatorExecution::getCustomDissagregation)
                        .collect(Collectors.toList());
        LocalDate startDate = LocalDate.of(period.getYear(), 1, 1);
        LocalDate endDate = LocalDate.of(period.getYear(), 12, 31);
        Set<Quarter> qs = this.quarterService.createQuarters(startDate, endDate, dissagregationTypes, customDissagregations, new ArrayList<>());
        List<Quarter> qsl = setOrderInQuartersAndMonths(qs);
        for (Quarter quarter : qsl) {
            indicatorExecution.addQuarter(quarter);
        }
        this.saveOrUpdate(indicatorExecution);
        return indicatorExecution.getId();
    }


    public Long updateAssignPerformanceIndicatorDirectImplementation(IndicatorExecutionAssigmentWeb
                                                                             indicatorExecutionAssigmentWeb) throws GeneralAppException {
        if (indicatorExecutionAssigmentWeb.getId() == null) {
            throw new GeneralAppException("No se pudo encontrar la asignación (Id:" + indicatorExecutionAssigmentWeb.getId() + ")", Response.Status.BAD_REQUEST);
        }
        this.validatePerformanceIndicatorAssignationDirectImplementation(indicatorExecutionAssigmentWeb);
        IndicatorExecution indicatorExecution = this.indicatorExecutionDao.find(indicatorExecutionAssigmentWeb.getId());
        if (indicatorExecution == null) {
            throw new GeneralAppException("No se pudo encontrar la asignación (Id:" + indicatorExecutionAssigmentWeb.getId() + ")", Response.Status.BAD_REQUEST);
        }

        indicatorExecution.setState(indicatorExecutionAssigmentWeb.getState());

        this.assigUsersToIndicatorExecution(indicatorExecution, indicatorExecutionAssigmentWeb);
        /*  *************budget**********/
        indicatorExecution.setKeepBudget(indicatorExecutionAssigmentWeb.getKeepBudget());
        indicatorExecution.setAssignedBudget(indicatorExecutionAssigmentWeb.getAssignedBudget());

        this.saveOrUpdate(indicatorExecution);
        return indicatorExecution.getId();
    }

    public void assigUsersToIndicatorExecution(IndicatorExecution indicatorExecution, IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb) throws GeneralAppException {
        User assignedUser = this.userService.getById(indicatorExecutionAssigmentWeb.getAssignedUser().getId());
        if (assignedUser == null) {
            throw new GeneralAppException("Usuario responsable no encontrado " + indicatorExecutionAssigmentWeb.getAssignedUser().getId(), Response.Status.BAD_REQUEST);
        }
        indicatorExecution.setAssignedUser(assignedUser);
        if (indicatorExecutionAssigmentWeb.getAssignedUserBackup() != null) {
            User assignedUserBackup = this.userService.getById(indicatorExecutionAssigmentWeb.getAssignedUserBackup().getId());
            if (assignedUserBackup == null) {
                throw new GeneralAppException("Usuario responsable alterno no encontrado " + indicatorExecutionAssigmentWeb.getAssignedUserBackup().getId(), Response.Status.BAD_REQUEST);
            }
            indicatorExecution.setAssignedUserBackup(assignedUserBackup);
        }
        if (indicatorExecutionAssigmentWeb.getSupervisorUser() != null) {
            User supervisorUser = this.userService.getById(indicatorExecutionAssigmentWeb.getSupervisorUser().getId());
            if (supervisorUser == null) {
                throw new GeneralAppException("Usuario supervisor no encontrado " + indicatorExecutionAssigmentWeb.getSupervisorUser().getId(), Response.Status.BAD_REQUEST);
            }
            indicatorExecution.setSupervisorUser(supervisorUser);
        }
    }

    public void validatePerformanceIndicatorAssignationDirectImplementation(IndicatorExecutionAssigmentWeb
                                                                                    indicatorExecutionWeb) throws GeneralAppException {
        validatePerformanceIndicatorAssignation(indicatorExecutionWeb);

        if (indicatorExecutionWeb.getReportingOffice() == null || indicatorExecutionWeb.getReportingOffice().getId() == null) {
            throw new GeneralAppException("La oficina asignada es obligatorio.", Response.Status.BAD_REQUEST);
        }

        if (indicatorExecutionWeb.getPeriod() == null || indicatorExecutionWeb.getPeriod().getId() == null) {
            throw new GeneralAppException("El periodo asignado es obligatorio.", Response.Status.BAD_REQUEST);
        }
        if (indicatorExecutionWeb.getAssignedUser() == null || indicatorExecutionWeb.getAssignedUser().getId() == null) {
            throw new GeneralAppException("El usuario responsable es obligatorio.", Response.Status.BAD_REQUEST);
        }
        // existe indicador para esta officina para
        IndicatorExecution assimentFound = this.indicatorExecutionDao.getByIndicatorIdAndOfficeId(indicatorExecutionWeb.getIndicator().getId(), indicatorExecutionWeb.getReportingOffice().getId());
        if (assimentFound != null && !assimentFound.getId().equals(indicatorExecutionWeb.getId())) {
            throw new GeneralAppException("Este indicador ya se encuentra asignado para esta oficina.", Response.Status.BAD_REQUEST);
        }

    }

    private void validatePerformanceIndicatorAssignation(IndicatorExecutionAssigmentWeb indicatorExecutionWeb) throws GeneralAppException {
        if (indicatorExecutionWeb == null) {
            throw new GeneralAppException("La asignación es obligatorio", Response.Status.BAD_REQUEST);
        }

        if (indicatorExecutionWeb.getIndicator() == null || indicatorExecutionWeb.getIndicator().getId() == null) {
            throw new GeneralAppException("Indicador asignado es obligatorio.", Response.Status.BAD_REQUEST);
        }

        if (indicatorExecutionWeb.getState() == null) {
            throw new GeneralAppException("El estado es obligatorio.", Response.Status.BAD_REQUEST);
        }
    }

    public List<IndicatorExecutionWeb> getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId
            (
                    Long userId,
                    Long periodId,
                    Long officeId,
                    Boolean supervisor,
                    Boolean responsible,
                    Boolean backup) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId(
                userId, periodId, officeId, supervisor, responsible, backup
        );
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(indicatorExecutions, false);
    }

    public List<IndicatorExecutionWeb> getActiveProjectIndicatorExecutionsByPeriodId(Long periodId) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getActivePartnersIndicatorExecutionsByPeriodId(periodId);
        return this.modelWebTransformationService
                .indicatorExecutionsToIndicatorExecutionsWeb(indicatorExecutions, true);
    }

    public List<IndicatorExecutionWeb> getDirectImplementationIndicatorExecutionsByIds(List<Long> indicatorExecutionIds, State state) throws GeneralAppException {
        return this.modelWebTransformationService
                .indicatorExecutionsToIndicatorExecutionsWeb(
                        this.indicatorExecutionDao.getDirectImplementationIndicatorExecutionsByIdsAndState(indicatorExecutionIds, state), false);
    }

    public Long updateDirectImplementationIndicatorExecutionLocationAssigment(Long indicatorExecutionId, List<CantonWeb> cantonesWeb) throws GeneralAppException {
        if (indicatorExecutionId == null) {
            throw new GeneralAppException("indicador id es dato obligatorio", Response.Status.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(cantonesWeb)) {
            throw new GeneralAppException("Al menos debe haber un cantón", Response.Status.BAD_REQUEST);
        }
        // recupero el ie
        IndicatorExecution ie = this.indicatorExecutionDao.getDirectImplementationIndicatorExecutionsById(indicatorExecutionId);
        if (ie == null) {
            throw new GeneralAppException("No se pudo encontrar el indicador (indicatorExecutionId =" + indicatorExecutionId + ")", Response.Status.BAD_REQUEST);
        }
        // activo todos
        Set<Canton> cantonesToActivate = new HashSet<>(this.cantonService.getByIds(cantonesWeb.stream().map(CantonWeb::getId).collect(Collectors.toList())));
        //desactivo los q ya no existen
        Set<Canton> cantonesToDissable = new HashSet<>();
        // cantones que ya existent
        List<Canton> existingCantons = ie.getIndicatorExecutionLocationAssigments()
                .stream()
                .map(IndicatorExecutionLocationAssigment::getLocation).collect(Collectors.toList());
        for (Canton existingCanton : existingCantons) {
            Optional<CantonWeb> cantonWebFound = cantonesWeb.stream().filter(cantonWeb -> cantonWeb.getId().equals(existingCanton.getId()))
                    .findFirst();
            if (!cantonWebFound.isPresent()) {
                cantonesToDissable.add(existingCanton);
            }
        }
        this.updateIndicatorExecutionLocations(ie, cantonesToActivate, cantonesToDissable);

        this.saveOrUpdate(ie);
        return ie.getId();
    }


    public List<IndicatorExecutionWeb> getActiveProjectIndicatorExecutionsByPeriodYear(Integer year) throws GeneralAppException {
        Period period = this.periodService.getByYear(year);
        if (period == null) {
            throw new GeneralAppException("Periodo no encontrado", Response.Status.NOT_FOUND);
        }
        return this.getActiveProjectIndicatorExecutionsByPeriodId(period.getId());
    }

    public List<IndicatorExecutionWeb> getActiveDirectImplementationIndicatorExecutionsByPeriodYear(Integer year) throws GeneralAppException {

        Period period = this.periodService.getByYear(year);
        if (period == null) {
            throw new GeneralAppException("Periodo no encontrado", Response.Status.NOT_FOUND);
        }
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(
                this.indicatorExecutionDao.getDirectImplementationIndicatorByPeriodIdAndState(period.getId(), State.ACTIVO), false
        );

    }

    public void updateIndicatorExecutionsDissagregations(
            List<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsToEnable,
            List<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsToDisable,
            List<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsToCreate
    ) throws GeneralAppException {
        List<IndicatorExecution> iesToUpdateTotals = new ArrayList<>();
        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : dissagregationAssignationToIndicatorsToEnable) {
            List<IndicatorExecution> iesToEnable = this.indicatorExecutionDao.getByPeriodIdAndIndicatorId(dissagregationAssignationToIndicator.getPeriod().getId(), dissagregationAssignationToIndicator.getIndicator().getId());
            iesToUpdateTotals.addAll(iesToEnable);
            enableDissagregationAssignationIndIndicatorExecution(iesToEnable, dissagregationAssignationToIndicator.getDissagregationType());
        }
        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : dissagregationAssignationToIndicatorsToDisable) {
            List<IndicatorExecution> iesToDisable = this.indicatorExecutionDao.getByPeriodIdAndIndicatorId(dissagregationAssignationToIndicator.getPeriod().getId(), dissagregationAssignationToIndicator.getIndicator().getId());
            iesToUpdateTotals.addAll(iesToDisable);
            disableDissagregationAsignationInIndicatiorExecution(iesToDisable, dissagregationAssignationToIndicator.getDissagregationType());
        }

        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : dissagregationAssignationToIndicatorsToCreate) {
            List<IndicatorExecution> iesToCreate = this.indicatorExecutionDao.getByPeriodIdAndIndicatorId(dissagregationAssignationToIndicator.getPeriod().getId(), dissagregationAssignationToIndicator.getIndicator().getId());
            iesToUpdateTotals.addAll(iesToCreate);
            createDissagregationAsignationInIndicatiorExecution(iesToCreate, dissagregationAssignationToIndicator.getDissagregationType());
        }

        for (IndicatorExecution ieToUpdateTotal : iesToUpdateTotals) {
            this.updateIndicatorExecutionTotals(ieToUpdateTotal);
            this.saveOrUpdate(ieToUpdateTotal);
        }
    }

    private void disableDissagregationAsignationInIndicatiorExecution(List<IndicatorExecution> iesToDisable, DissagregationType dissagregationType
    ) {
        for (IndicatorExecution indicatorExecution : iesToDisable) {
            indicatorExecution.getDissagregationsAssignationsToIndicatorExecutions()
                    .stream()
                    .filter(dissagregationAssignationToIndicatorExecution ->
                            dissagregationAssignationToIndicatorExecution.getDissagregationType()
                                    .equals(dissagregationType))
                    .findFirst()
                    .ifPresent(dissagregationAssignationToIndicatorExecution -> {
                        dissagregationAssignationToIndicatorExecution.setState(State.INACTIVO);
                        indicatorExecution.getQuarters()
                                .stream().flatMap(quarter -> quarter.getMonths().stream())
                                .flatMap(month -> month.getIndicatorValues().stream())
                                .filter(indicatorValue -> indicatorValue.getDissagregationType().equals(dissagregationType))
                                .forEach(indicatorValue -> indicatorValue.setState(State.INACTIVO));
                    });
        }
    }

    private void enableDissagregationAssignationIndIndicatorExecution(List<IndicatorExecution> iesToEnable,
                                                                      DissagregationType dissagregationType
    ) {
        for (IndicatorExecution indicatorExecution : iesToEnable) {
            indicatorExecution.getDissagregationsAssignationsToIndicatorExecutions()
                    .stream()
                    .filter(dissagregationAssignationToIndicatorExecution ->
                            dissagregationAssignationToIndicatorExecution.getDissagregationType()
                                    .equals(dissagregationType))
                    .findFirst()
                    .ifPresent(dissagregationAssignationToIndicatorExecution -> {
                        dissagregationAssignationToIndicatorExecution.setState(State.ACTIVO);
                        indicatorExecution.getQuarters()
                                .stream().flatMap(quarter -> quarter.getMonths().stream())
                                .flatMap(month -> month.getIndicatorValues().stream())
                                .filter(indicatorValue -> indicatorValue.getDissagregationType().equals(dissagregationType))
                                .forEach(indicatorValue -> indicatorValue.setState(State.ACTIVO));
                    });
        }
    }

    public void updateIndicatorExecutionLocations(
            IndicatorExecution indicatorExecution,
            Set<Canton> locationsToActivate,
            Set<Canton> locationsToDissable
    ) throws GeneralAppException {//
        Set<Canton> locationsToActivateIe = new HashSet<>();
        Set<Canton> locationsToDissableIe = new HashSet<>();

        locationsToActivate.forEach(cantonToActivate -> {
            Optional<IndicatorExecutionLocationAssigment> indicatorExecutionLocationAssigmentToActivateOpt =
                    indicatorExecution.getIndicatorExecutionLocationAssigments()
                            .stream()
                            .filter(indicatorExecutionLocationAssigment ->
                                    cantonToActivate.getId().equals(indicatorExecutionLocationAssigment.getLocation().getId()))
                            .findFirst();
            if (indicatorExecutionLocationAssigmentToActivateOpt.isPresent()) {
                indicatorExecutionLocationAssigmentToActivateOpt.get().setState(State.ACTIVO);
                locationsToActivateIe.add(indicatorExecutionLocationAssigmentToActivateOpt.get().getLocation());
            } else {
                IndicatorExecutionLocationAssigment newIndicatorExecutionLocationAssigment = new IndicatorExecutionLocationAssigment();
                newIndicatorExecutionLocationAssigment.setLocation(cantonToActivate);
                newIndicatorExecutionLocationAssigment.setState(State.ACTIVO);
                indicatorExecution.addIndicatorExecutionLocationAssigment(newIndicatorExecutionLocationAssigment);
                locationsToActivateIe.add(cantonToActivate);

            }
        });

        locationsToDissable.forEach(cantonToDissable -> {
            Optional<IndicatorExecutionLocationAssigment> indicatorExecutionLocationAssigmentToDissableOpt =
                    indicatorExecution.getIndicatorExecutionLocationAssigments()
                            .stream()
                            .filter(indicatorExecutionLocationAssigment -> cantonToDissable.getId().equals(indicatorExecutionLocationAssigment.getLocation().getId()))
                            .findFirst();
            if (indicatorExecutionLocationAssigmentToDissableOpt.isPresent()) {
                indicatorExecutionLocationAssigmentToDissableOpt.get().setState(State.INACTIVO);
                locationsToDissableIe.add(indicatorExecutionLocationAssigmentToDissableOpt.get().getLocation());
            }
        });

        this.indicatorValueService.updateIndicatorValuesLocationsForIndicatorExecution(
                indicatorExecution, locationsToActivateIe, locationsToDissableIe
        );
        this.updateIndicatorExecutionTotals(indicatorExecution);
    }


    public void updateIndicatorExecutionsGeneralDissagregations(
            Long periodId,
            List<DissagregationAssignationToGeneralIndicator> dissagregationAssignationToIndicatorsToEnable,
            List<DissagregationAssignationToGeneralIndicator> dissagregationAssignationToIndicatorsToDisable,
            List<DissagregationAssignationToGeneralIndicator> dissagregationAssignationToIndicatorsToCreate
    ) throws GeneralAppException {
        List<IndicatorExecution> iesToUpdate = this.indicatorExecutionDao.getGeneralIndicatorExecutionsByPeriodId(periodId);
        List<IndicatorExecution> iesToUpdateTotals = new ArrayList<>(iesToUpdate);
        for (DissagregationAssignationToGeneralIndicator dissagregationAssignationToIndicator : dissagregationAssignationToIndicatorsToEnable) {
            enableDissagregationAssignationIndIndicatorExecution(iesToUpdate, dissagregationAssignationToIndicator.getDissagregationType());

        }
        for (DissagregationAssignationToGeneralIndicator dissagregationAssignationToIndicator : dissagregationAssignationToIndicatorsToDisable) {
            disableDissagregationAsignationInIndicatiorExecution(iesToUpdate, dissagregationAssignationToIndicator.getDissagregationType());
        }

        for (DissagregationAssignationToGeneralIndicator dissagregationAssignationToIndicator : dissagregationAssignationToIndicatorsToCreate) {
            createDissagregationAsignationInIndicatiorExecution(iesToUpdate, dissagregationAssignationToIndicator.getDissagregationType());
        }

        for (IndicatorExecution ieToUpdateTotal : iesToUpdateTotals) {
            this.updateIndicatorExecutionTotals(ieToUpdateTotal);
            this.saveOrUpdate(ieToUpdateTotal);
        }
    }

    private void createDissagregationAsignationInIndicatiorExecution(List<IndicatorExecution> iesToUpdate, DissagregationType dissagregationType) throws GeneralAppException {
        for (IndicatorExecution indicatorExecution : iesToUpdate) {
            DissagregationAssignationToIndicatorExecution dissagregationAssignationToIndicatorExecution = new DissagregationAssignationToIndicatorExecution();
            dissagregationAssignationToIndicatorExecution.setState(State.ACTIVO);
            dissagregationAssignationToIndicatorExecution.setDissagregationType(dissagregationType);
            indicatorExecution.addDissagregationAssignationToIndicatorExecution(dissagregationAssignationToIndicatorExecution);
            List<Month> months = indicatorExecution.getQuarters().stream().flatMap(quarter -> quarter.getMonths().stream()).collect(Collectors.toList());
            for (Month month : months) {
                List<Canton> cantones = indicatorExecution.getIndicatorExecutionLocationAssigments()
                        .stream()
                        .map(IndicatorExecutionLocationAssigment::getLocation)
                        .distinct()
                        .collect(Collectors.toList());
                List<IndicatorValue> ivs = this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(dissagregationType,
                        cantones);
                for (IndicatorValue iv : ivs) {
                    // veo q esten activos o inactivos los locations asigmentes
                    if (iv.getLocation() != null) {
                        indicatorExecution.getIndicatorExecutionLocationAssigments()
                                .stream()
                                .filter(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.getLocation().getId().equals(iv.getLocation().getId()))
                                .findFirst().ifPresent(indicatorExecutionLocationAssigment -> iv.setState(indicatorExecutionLocationAssigment.getState()));
                    }
                    month.addIndicatorValue(iv);
                }
            }
        }
    }

    public void updateAllPartnersTotals(Long periodId) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getActivePartnersIndicatorExecutionsByPeriodId(periodId);
        int conteo = 0;
        int total = indicatorExecutions.size();
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            conteo = conteo + 1;
            LOGGER.info(conteo + ":" + total);
            this.updateIndicatorExecutionTotals(indicatorExecution);
            this.saveOrUpdate(indicatorExecution);
        }
    }


    public void updateAllDirectImplementationTotals(Long periodId) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getDirectImplementationActiveByPeriodId(periodId);
        int conteo = 0;
        int total = indicatorExecutions.size();
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            conteo = conteo + 1;
            LOGGER.info(conteo + ":" + total);
            this.updateIndicatorExecutionTotals(indicatorExecution);
            this.saveOrUpdate(indicatorExecution);
        }
    }
}






