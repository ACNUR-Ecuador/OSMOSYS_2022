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
import org.unhcr.osmosys.model.standardDissagregations.DissagregationAssignationToIndicatorPeriodCustomization;
import org.unhcr.osmosys.model.standardDissagregations.options.*;
import org.unhcr.osmosys.model.standardDissagregations.periodOptions.*;
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
    IndicatorValueCustomDissagregationService indicatorValueCustomDissagregationService;

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

        Period period = this.periodService.getWithAllDataById(project.getPeriod().getId());
        ie.setPeriod(period);

        // locations for ie // todo 2024 separar por un actualizador
        List<Canton> cantones = new ArrayList<>();
        if (!indicatorExecutionWeb.getLocations().isEmpty()) {
            cantones = this.cantonService.getByIds(indicatorExecutionWeb.getLocations().stream().map(BaseWebEntity::getId).collect(Collectors.toList()));
            for (Canton canton : cantones) {
                IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigment = new IndicatorExecutionLocationAssigment(canton);
                ie.addIndicatorExecutionLocationAssigment(indicatorExecutionLocationAssigment);
            }
        }


        // todo 2024 separar a actualizador
        @SuppressWarnings("DuplicatedCode")
        List<DissagregationAssignationToIndicator> dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator()
                .stream()
                .filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                        && dissagregationAssignationToIndicator.getPeriod().getId().equals(period.getId()))
                .collect(Collectors.toList());
        //noinspection DuplicatedCode


        List<CustomDissagregationAssignationToIndicatorExecution> customDissagregationsAssignations = indicator.getCustomDissagregationAssignationToIndicators().stream().filter(customDissagregationAssignationToIndicator -> customDissagregationAssignationToIndicator.getState().equals(State.ACTIVO) && customDissagregationAssignationToIndicator.getPeriod().getId().equals(ie.getPeriod().getId())).map(customDissagregationAssignationToIndicator -> {
            CustomDissagregationAssignationToIndicatorExecution da = new CustomDissagregationAssignationToIndicatorExecution();
            da.setCustomDissagregation(customDissagregationAssignationToIndicator.getCustomDissagregation());
            da.setState(State.ACTIVO);
            return da;
        }).collect(Collectors.toList());
        customDissagregationsAssignations.forEach(ie::addCustomDissagregationAssignationToIndicatorExecution);


        ie.setProject(project);


        // manejo de desagregaciones
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = this.getDissagregationTypeOptionsMap(new ArrayList<>(dissagregationAssignations), cantones, indicator, period, ie);

        List<CustomDissagregation> customDissagregations = customDissagregationsAssignations.stream().map(CustomDissagregationAssignationToIndicatorExecution::getCustomDissagregation).collect(Collectors.toList());


        this.createQuartersInIndicatorExecution(ie, project, dissagregationsMap, customDissagregations);


        this.saveOrUpdate(ie);
        return ie;

    }

    private Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> getDissagregationTypeOptionsMap(List<DissagregationAssignationToIndicatorInterface> dissagregationAssignations, List<Canton> cantones, Indicator indicator, Period period, IndicatorExecution ie) throws GeneralAppException {
        List<DissagregationType> dissagregationTypes = dissagregationAssignations.stream().map(DissagregationAssignationToIndicatorInterface::getDissagregationType).collect(Collectors.toList());
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = new HashMap<>();
        for (DissagregationType dissagregationType : dissagregationTypes) {
            Map<DissagregationType, List<StandardDissagregationOption>> simpleDissagregationsMap = new HashMap<>();
            for (DissagregationType simpleDissagregation : dissagregationType.getSimpleDissagregations()) {
                switch (simpleDissagregation) {
                    case LUGAR:
                        simpleDissagregationsMap.put(DissagregationType.LUGAR, new ArrayList<>(cantones));
                        break;
                    case EDAD:
                        boolean useCustomAgeDissagregation = false;
                        Optional<DissagregationAssignationToIndicator> dissagregationAssignationOptional = indicator.getDissagregationsAssignationToIndicator().stream()
                                .filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getPeriod().getId().equals(period.getId())
                                        && dissagregationAssignationToIndicator.getDissagregationType().equals(dissagregationType))
                                .findFirst();
                        if (dissagregationAssignationOptional.isPresent()) {
                            useCustomAgeDissagregation = dissagregationAssignationOptional.get().getUseCustomAgeDissagregations();
                        }
                        List<AgeDissagregationOption> ageOptions;
                        if (ie.getIndicatorType().equals(IndicatorType.GENERAL) || !useCustomAgeDissagregation) {
                            ageOptions = period.getPeriodAgeDissagregationOptions()
                                    .stream()
                                    .filter(option -> option.getState().equals(State.ACTIVO))
                                    .map(PeriodAgeDissagregationOption::getDissagregationOption)
                                    .collect(Collectors.toList());
                        } else {
                            ageOptions = dissagregationAssignationOptional.get().getDissagregationAssignationToIndicatorPeriodCustomizations()
                                    .stream()
                                    .filter(dissagregationAssignationToIndicatorPeriodCustomization -> dissagregationAssignationToIndicatorPeriodCustomization.getState().equals(State.ACTIVO))
                                    .map(DissagregationAssignationToIndicatorPeriodCustomization::getAgeDissagregationOption).collect(Collectors.toList());
                        }
                        simpleDissagregationsMap.put(DissagregationType.EDAD, new ArrayList<>(ageOptions));
                        break;
                    case GENERO:
                        List<GenderDissagregationOption> genderOptions = period.getPeriodGenderDissagregationOptions()
                                .stream()
                                .filter(option -> option.getState().equals(State.ACTIVO))
                                .map(PeriodGenderDissagregationOption::getDissagregationOption)
                                .collect(Collectors.toList());
                        simpleDissagregationsMap.put(DissagregationType.GENERO, new ArrayList<>(genderOptions));
                        break;
                    case DIVERSIDAD:
                        List<DiversityDissagregationOption> diversityOptions = period.getPeriodDiversityDissagregationOptions()
                                .stream()
                                .filter(option -> option.getState().equals(State.ACTIVO))
                                .map(PeriodDiversityDissagregationOption::getDissagregationOption)
                                .collect(Collectors.toList());
                        simpleDissagregationsMap.put(DissagregationType.DIVERSIDAD, new ArrayList<>(diversityOptions));
                        break;
                    case PAIS_ORIGEN:
                        List<CountryOfOriginDissagregationOption> countryOfOriginOptions = period.getPeriodCountryOfOriginDissagregationOptions()
                                .stream()
                                .filter(option -> option.getState().equals(State.ACTIVO))
                                .map(PeriodCountryOfOriginDissagregationOption::getDissagregationOption)
                                .collect(Collectors.toList());
                        simpleDissagregationsMap.put(DissagregationType.PAIS_ORIGEN, new ArrayList<>(countryOfOriginOptions));
                        break;
                    case TIPO_POBLACION:
                        List<PopulationTypeDissagregationOption> populationTypesOptions = period.getPeriodPopulationTypeDissagregationOptions()
                                .stream()
                                .filter(option -> option.getState().equals(State.ACTIVO))
                                .map(PeriodPopulationTypeDissagregationOption::getDissagregationOption)
                                .collect(Collectors.toList());
                        simpleDissagregationsMap.put(DissagregationType.TIPO_POBLACION, new ArrayList<>(populationTypesOptions));
                        break;

                    default:
                        throw new GeneralAppException("Error al definir opciones para el tipo de segregación simple " + simpleDissagregation, Response.Status.INTERNAL_SERVER_ERROR);
                }
            }
            dissagregationsMap.put(dissagregationType, simpleDissagregationsMap);
        }
        return dissagregationsMap;
    }

    private void createQuartersInIndicatorExecution(
            IndicatorExecution ie,
            Project project,
            Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap,
            List<CustomDissagregation> customDissagregations
    ) throws GeneralAppException {
        Set<Quarter> qs = this.quarterService.createQuarters(project.getStartDate(), project.getEndDate(), dissagregationsMap, customDissagregations);

        this.validateLocationsLocationsInLocationsDissagregations(dissagregationsMap);
        List<Quarter> qsl = setOrderInQuartersAndMonths(qs);
        for (Quarter quarter : qsl) {
            ie.addQuarter(quarter);
        }
    }

    private void validateLocationsLocationsInLocationsDissagregations(Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap) throws GeneralAppException {
        for (DissagregationType dissagregationType : dissagregationsMap.keySet()) {
            if (dissagregationType.isLocationsDissagregation()) {
                if (CollectionUtils.isEmpty(dissagregationsMap.get(dissagregationType).get(DissagregationType.LUGAR))) {
                    throw new GeneralAppException("No se puede crear una desagregación de lugar sin Lugares ", Response.Status.BAD_REQUEST);
                }
            }
        }
    }


    /***************************************************************************************************************/


    public IndicatorExecution createGeneralIndicatorForProject(Project project) throws GeneralAppException {
        IndicatorExecution ie = new IndicatorExecution();
        ie.setCompassIndicator(false);
        //target
        ie.setProject(project);
        ie.setIndicatorType(IndicatorType.GENERAL);
        ie.setTarget(null);
        Period period = this.periodService.getWithAllDataById(project.getPeriod().getId());
        ie.setPeriod(period);
        ie.setState(State.ACTIVO);
        ie.setKeepBudget(false);


        List<Canton> cantones = project.getProjectLocationAssigments().stream().filter(projectLocationAssigment -> projectLocationAssigment.getState().equals(State.ACTIVO)).map(ProjectLocationAssigment::getLocation).collect(Collectors.toList());
        List<DissagregationType> dissagregationTypes;
        GeneralIndicator generalIndicator = this.generalIndicatorService.getByPeriodIdAndState(project.getPeriod().getId(), State.ACTIVO);

        if (generalIndicator != null) {
            Set<DissagregationAssignationToGeneralIndicator> dissagregationAssignations = generalIndicator
                    .getDissagregationAssignationsToGeneralIndicator()
                    .stream().filter(dissagregationAssignationToGeneralIndicator -> dissagregationAssignationToGeneralIndicator.getState().equals(State.ACTIVO))
                    .collect(Collectors.toSet());
            List<CustomDissagregation> customDissagregations = new ArrayList<>();

            // manejo de desagregaciones
            Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = this.getDissagregationTypeOptionsMap(new ArrayList<>(dissagregationAssignations), cantones, null, period, ie);

            createQuartersInIndicatorExecution(ie, project, dissagregationsMap, customDissagregations);

        }


        return ie;

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
        ie.setTarget(targetUpdateDTOWeb.getTotalTarget());
        /*
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

*/
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
        /*if (!indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
            // target total
            Optional<BigDecimal> totalTargetOpt;
            totalTargetOpt = quarters.stream().map(Quarter::getTarget).filter(Objects::nonNull).reduce(BigDecimal::add);
            if (totalTargetOpt.isPresent()) {
                indicatorExecution.setTarget(totalTargetOpt.get());
            } else {
                indicatorExecution.setTarget(null);
            }
        }*/

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
                    indicatorExecution.setExecutionPercentage(BigDecimal.ZERO);
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
        if (monthValuesWeb.getIndicatorValuesMap() == null || monthValuesWeb.getIndicatorValuesMap().isEmpty()) {
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
            Period period = indicatorExecution.getPeriod();
            Indicator indicator;
            List<DissagregationAssignationToIndicatorInterface> dissagregationAssignations;
            List<CustomDissagregation> customDissagregations;
            if (indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
                indicator = null;
                GeneralIndicator generalIndicator = this.generalIndicatorService.getByPeriodIdAndState(project.getPeriod().getId(), State.ACTIVO);
                dissagregationAssignations = generalIndicator.getDissagregationAssignationsToGeneralIndicator()
                        .stream().filter(dissagregationAssignationToGeneralIndicator -> dissagregationAssignationToGeneralIndicator.getState().equals(State.ACTIVO)).collect(Collectors.toList());
                customDissagregations = new ArrayList<>();
            } else {
                // asignacion al indicador
                indicator = indicatorExecution.getIndicator();
                dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator()
                        .stream()
                        .filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                                && dissagregationAssignationToIndicator.getPeriod().getId().equals(period.getId()))
                        .collect(Collectors.toList());

                customDissagregations = indicatorExecution
                        .getCustomDissagregationAssignationToIndicatorExecutions().stream()
                        .filter(customDissagregationAssignationToIndicatorExecution -> customDissagregationAssignationToIndicatorExecution.getState().equals(State.ACTIVO)).map(CustomDissagregationAssignationToIndicatorExecution::getCustomDissagregation).collect(Collectors.toList());
            }

            // manejo de desagregaciones
            Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = this.getDissagregationTypeOptionsMap(new ArrayList<>(dissagregationAssignations), cantones, indicator, period, indicatorExecution);


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
                        if (foundMonth.isEmpty()) {
                            // creo mes
                            Month newmonth = this.monthService
                                    .createMonth(foundQuarter.getYear(), monthEnum, dissagregationsMap, customDissagregations);
                            foundQuarter.addMonth(newmonth);
                        }
                    }


                } else {

                    this.validateLocationsLocationsInLocationsDissagregations(dissagregationsMap);
                    Quarter newCreatedQuarter = this.quarterService.createQuarter(newQuarter, newStartDate, newEndDate, dissagregationsMap, customDissagregations);
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
                if (foundOrgQuarter.isEmpty()) {
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


    // todo 2024 revisar a detalle
    public void updateIndicatorExecutionLocationsByAssignation(IndicatorExecution indicatorExecution,
                                                               List<Canton> cantonesToCreate
    ) throws GeneralAppException {
        List<DissagregationAssignationToIndicatorInterface> locationDissagregationAssignations;
        Indicator indicator;
        if (indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
            indicator = null;
            locationDissagregationAssignations = indicatorExecution
                    .getPeriod()
                    .getGeneralIndicator()
                    .getDissagregationAssignationsToGeneralIndicator()
                    .stream()
                    .filter(dissagregationAssignationToGeneralIndicator -> dissagregationAssignationToGeneralIndicator.getState().equals(State.ACTIVO) && dissagregationAssignationToGeneralIndicator.getDissagregationType().isLocationsDissagregation())
                    .collect(Collectors.toList());
        } else {
            indicator = indicatorExecution.getIndicator();
            locationDissagregationAssignations = indicatorExecution.getIndicator().getDissagregationsAssignationToIndicator()
                    .stream()
                    .filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                            && dissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())
                            && dissagregationAssignationToIndicator.getDissagregationType().isLocationsDissagregation()
                    )

                    .collect(Collectors.toList());
        }

        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = this.getDissagregationTypeOptionsMap(new ArrayList<>(locationDissagregationAssignations), cantonesToCreate, indicator, indicatorExecution.getPeriod(), indicatorExecution);


        for (Quarter quarter : indicatorExecution.getQuarters()) {
            this.quarterService.updateQuarterLocationsByAssignation(quarter, dissagregationsMap);
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
                                dissagregationAssignationToIndicatorExecution.getDissagregationType().isLocationsDissagregation()
                        ).collect(Collectors.toSet())
        )) {
            // busco las que ya no existen
            // activo todos

            ProjectService.LocationToActivateDesativate result = this.getLocationToActivateDessactivate(indicatorExecution, indicatorExecutionAssigmentWeb.getLocations());
            this.updateIndicatorExecutionLocations(indicatorExecution, result.locationsToActivate, result.locationsToDissable);


        }
        this.updateIndicatorExecutionTotals(indicatorExecution);
        this.saveOrUpdate(indicatorExecution);
        return indicatorExecution.getId();
    }

    public ProjectService.LocationToActivateDesativate getLocationToActivateDessactivate(IndicatorExecution indicatorExecution, List<CantonWeb> cantonesWeb) {

        Set<Canton> cantonesToActivate = new HashSet<>(this.cantonService.getByIds(cantonesWeb.stream().map(CantonWeb::getId).collect(Collectors.toList())));
        //desactivo los q ya no existen
        Set<Canton> cantonesToDissable = new HashSet<>();
        // cantones que ya existent
        List<Canton> existingCantons = indicatorExecution.getIndicatorExecutionLocationAssigments()
                .stream()
                .map(IndicatorExecutionLocationAssigment::getLocation).collect(Collectors.toList());
        for (Canton existingCanton : existingCantons) {
            Optional<CantonWeb> cantonWebFound = cantonesWeb.stream().filter(cantonWeb -> cantonWeb.getId().equals(existingCanton.getId()))
                    .findFirst();
            if (cantonWebFound.isEmpty()) {
                cantonesToDissable.add(existingCanton);
            }
        }
        return new ProjectService.LocationToActivateDesativate(cantonesToActivate, cantonesToDissable);
    }

    private static class Result {
        public final Set<Canton> cantonesToActivate;
        public final Set<Canton> cantonesToDissable;

        public Result(Set<Canton> cantonesToActivate, Set<Canton> cantonesToDissable) {
            this.cantonesToActivate = cantonesToActivate;
            this.cantonesToDissable = cantonesToDissable;
        }
    }


    public void createIndicatorExecForProjects(Long periodId) throws GeneralAppException {
        List<Project> projects = this.projectService.getByPeriodId(periodId);
        Period period = this.periodService.getById(periodId);
        for (Project project : projects) {
            List<Canton> cantones = project.getProjectLocationAssigments().stream().map(ProjectLocationAssigment::getLocation).collect(Collectors.toList());
            List<IndicatorExecution> iepi = this.indicatorExecutionDao.getPerformanceIndicatorExecutionsByProjectIdAndState(project.getId(), State.ACTIVO);
            for (IndicatorExecution indicatorExecution : iepi) {
                Indicator indicator = indicatorExecution.getIndicator();

                List<DissagregationAssignationToIndicator> dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator()
                        .stream().filter(dissagregationAssignationToIndicator ->
                                dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                                        && dissagregationAssignationToIndicator.getPeriod().getId().equals(period.getId()))
                        .collect(Collectors.toList());

                // manejo de desagregaciones
                Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = this.getDissagregationTypeOptionsMap(new ArrayList<>(dissagregationAssignations), cantones, indicator, period, indicatorExecution);

                List<CustomDissagregationAssignationToIndicatorExecution> customDissagregationsAssignations = indicator.getCustomDissagregationAssignationToIndicators().stream().filter(customDissagregationAssignationToIndicator -> customDissagregationAssignationToIndicator.getState().equals(State.ACTIVO) && customDissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())).map(customDissagregationAssignationToIndicator -> {
                    CustomDissagregationAssignationToIndicatorExecution da = new CustomDissagregationAssignationToIndicatorExecution();
                    da.setCustomDissagregation(customDissagregationAssignationToIndicator.getCustomDissagregation());
                    da.setState(State.ACTIVO);
                    return da;
                }).collect(Collectors.toList());
                customDissagregationsAssignations.forEach(indicatorExecution::addCustomDissagregationAssignationToIndicatorExecution);


                List<CustomDissagregation> customDissagregations = customDissagregationsAssignations.stream().map(CustomDissagregationAssignationToIndicatorExecution::getCustomDissagregation).collect(Collectors.toList());



                this.validateLocationsLocationsInLocationsDissagregations(dissagregationsMap);
                Set<Quarter> qs = this.quarterService.createQuarters(project.getStartDate(), project.getEndDate(), dissagregationsMap, customDissagregations);
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
        Period period = this.periodService.getWithAllDataById(periodId);
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            Indicator indicator = indicatorExecution.getIndicator();

            List<DissagregationAssignationToIndicator> dissagregationAssignations = indicator
                    .getDissagregationsAssignationToIndicator().stream()
                    .filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                            && dissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId()))
                    .collect(Collectors.toList());

            // manejo de desagregaciones
            // todo 2024 revisar cantones
            List<Canton> cantones = indicatorExecution.getIndicatorExecutionLocationAssigments().stream().filter(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.getState().equals(State.ACTIVO))
                    .map(IndicatorExecutionLocationAssigment::getLocation).collect(Collectors.toList());
            Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = this.getDissagregationTypeOptionsMap(new ArrayList<>(dissagregationAssignations), cantones, indicator, period, indicatorExecution);

            List<CustomDissagregationAssignationToIndicatorExecution> customDissagregationsAssignations = indicator.getCustomDissagregationAssignationToIndicators().stream().filter(customDissagregationAssignationToIndicator -> customDissagregationAssignationToIndicator.getState().equals(State.ACTIVO) && customDissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())).map(customDissagregationAssignationToIndicator -> {
                CustomDissagregationAssignationToIndicatorExecution da = new CustomDissagregationAssignationToIndicatorExecution();
                da.setCustomDissagregation(customDissagregationAssignationToIndicator.getCustomDissagregation());
                da.setState(State.ACTIVO);
                return da;
            }).collect(Collectors.toList());
            customDissagregationsAssignations
                    .forEach(indicatorExecution::addCustomDissagregationAssignationToIndicatorExecution);


            List<CustomDissagregation> customDissagregations = customDissagregationsAssignations
                    .stream()
                    .map(CustomDissagregationAssignationToIndicatorExecution::getCustomDissagregation)
                    .collect(Collectors.toList());

            LocalDate startDate = LocalDate.of(period.getYear(), 1, 1);
            LocalDate endDate = LocalDate.of(period.getYear(), 12, 31);

            Set<Quarter> qs = this.quarterService.createQuarters(startDate, endDate, dissagregationsMap, customDissagregations);
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
        Period period = this.periodService.getWithAllDataById(indicatorExecutionAssigmentWeb.getPeriod().getId());
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
        indicatorExecution.setTarget(indicatorExecutionAssigmentWeb.getTarget() != null ? new BigDecimal(indicatorExecutionAssigmentWeb.getTarget()) : null);
        indicatorExecution.setReportingOffice(office);
        if (office == null) {
            throw new GeneralAppException("Oficina no encontrada " + indicatorExecutionAssigmentWeb.getReportingOffice().getId(), Response.Status.BAD_REQUEST);
        }
        this.assigUsersToIndicatorExecution(indicatorExecution, indicatorExecutionAssigmentWeb);
        List<DissagregationAssignationToIndicator> dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator()
                .stream()
                .filter(dissagregationAssignationToIndicator ->
                        dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                                && dissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())
                ).collect(Collectors.toList());
        // manejo de desagregaciones
        List<Canton> cantones = indicatorExecution.getIndicatorExecutionLocationAssigments().stream().filter(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.getState().equals(State.ACTIVO))
                .map(IndicatorExecutionLocationAssigment::getLocation).collect(Collectors.toList());
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = this.getDissagregationTypeOptionsMap(new ArrayList<>(dissagregationAssignations), cantones, indicator, period, indicatorExecution);


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


        List<CustomDissagregation> customDissagregations =
                customDissagregationsAssignations
                        .stream()
                        .map(CustomDissagregationAssignationToIndicatorExecution::getCustomDissagregation)
                        .collect(Collectors.toList());
        LocalDate startDate = LocalDate.of(period.getYear(), 1, 1);
        LocalDate endDate = LocalDate.of(period.getYear(), 12, 31);
        Set<Quarter> qs = this.quarterService.createQuarters(startDate, endDate, dissagregationsMap, customDissagregations);
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
        indicatorExecution.setTarget(indicatorExecutionAssigmentWeb.getTarget() != null ? new BigDecimal(indicatorExecutionAssigmentWeb.getTarget()) : null);

        this.assigUsersToIndicatorExecution(indicatorExecution, indicatorExecutionAssigmentWeb);
        /*  *************budget**********/
        indicatorExecution.setKeepBudget(indicatorExecutionAssigmentWeb.getKeepBudget());
        indicatorExecution.setAssignedBudget(indicatorExecutionAssigmentWeb.getAssignedBudget());

        this.saveOrUpdate(indicatorExecution);
        this.updateIndicatorExecutionTotals(indicatorExecution);
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
        } else {
            indicatorExecution.setAssignedUserBackup(null);
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
        IndicatorExecution assimentFound = this.indicatorExecutionDao.getByIndicatorIdAndOfficeIdAndPeriodId(indicatorExecutionWeb.getIndicator().getId(), indicatorExecutionWeb.getReportingOffice().getId(), indicatorExecutionWeb.getPeriod().getId());
        if (assimentFound != null && !assimentFound.getId().equals(indicatorExecutionWeb.getId())) {
            throw new GeneralAppException("Este indicador ya se encuentra asignado para esta oficina. " + indicatorExecutionWeb.getIndicator().getCode(), Response.Status.BAD_REQUEST);
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

    public List<IndicatorExecutionWeb> getActivePartnersIndicatorExecutionsByProjectId(Long projectId) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getActivePartnersIndicatorExecutionsByProjectId(projectId);
        return this.modelWebTransformationService
                .indicatorExecutionsToIndicatorExecutionsWeb(indicatorExecutions, true);
    }

    public List<IndicatorExecution> getIndicatorExecutionsByProjectId(Long projectId) {
        return this.indicatorExecutionDao.getActivePartnersIndicatorExecutionsByProjectId(projectId);

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

    public Long updatePartnerIndicatorExecutionLocationAssigment(Long indicatorExecutionId, List<CantonWeb> cantonesWeb) throws GeneralAppException {
        if (indicatorExecutionId == null) {
            throw new GeneralAppException("indicador execution id es dato obligatorio", Response.Status.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(cantonesWeb)) {
            throw new GeneralAppException("Al menos debe haber un cantón", Response.Status.BAD_REQUEST);
        }
        // recupero el ie
        IndicatorExecution ie = this.indicatorExecutionDao.getPartnerIndicatorExecutionById(indicatorExecutionId);
        if (ie == null) {
            throw new GeneralAppException("No se pudo encontrar el indicador (indicatorExecutionId =" + indicatorExecutionId + ")", Response.Status.BAD_REQUEST);
        }
        if (ie.getProject() == null) {
            throw new GeneralAppException("Este indicador no es ejecutado por un Socio (indicatorExecutionId =" + indicatorExecutionId + ")", Response.Status.BAD_REQUEST);
        }

        if (ie.getIndicatorType().equals(IndicatorType.GENERAL)) {
            ProjectService.LocationToActivateDesativate locatoinActiveNoActive = this.projectService.getLocationToActivateDesativate(ie.getProject(), cantonesWeb);
            Project project = ie.getProject();
            for (IndicatorExecution indicatorExecution : project.getIndicatorExecutions()) {
                this.updateIndicatorExecutionLocations(indicatorExecution, locatoinActiveNoActive.locationsToActivate, locatoinActiveNoActive.locationsToDissable);
            }
        } else {
            ProjectService.LocationToActivateDesativate locationToActivateDessactivate = this.getLocationToActivateDessactivate(ie, cantonesWeb);
            this.updateIndicatorExecutionLocations(ie, locationToActivateDessactivate.locationsToActivate, locationToActivateDessactivate.locationsToDissable);
// todo definir q hacer
        }

        return ie.getId();
    }

    public Long updateDirectImplementationIndicatorExecutionLocationAssigment(Long indicatorExecutionId, List<CantonWeb> cantonesWeb) throws GeneralAppException {
        if (indicatorExecutionId == null) {
            throw new GeneralAppException("indicador execution id es dato obligatorio", Response.Status.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(cantonesWeb)) {
            throw new GeneralAppException("Al menos debe haber un cantón", Response.Status.BAD_REQUEST);
        }
        // recupero el ie
        IndicatorExecution ie = this.indicatorExecutionDao.getDirectImplementationIndicatorExecutionsById(indicatorExecutionId);
        if (ie == null) {
            throw new GeneralAppException("No se pudo encontrar el indicador (indicatorExecutionId =" + indicatorExecutionId + ")", Response.Status.BAD_REQUEST);
        }
        ProjectService.LocationToActivateDesativate locationToActivateDessactivate = this.getLocationToActivateDessactivate(ie, cantonesWeb);

        this.updateIndicatorExecutionLocations(ie, locationToActivateDessactivate.locationsToActivate, locationToActivateDessactivate.locationsToDissable);


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

    public void updateIndicatorExecutionsCustomDissagregations(List<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicatorsToEnable,
                                                               List<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicatorsToDisable,
                                                               List<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicatorsToCreate) throws GeneralAppException {
        List<IndicatorExecution> iesToUpdateTotals = new ArrayList<>();
        for (CustomDissagregationAssignationToIndicator dissagregationAssignationToIndicator : customDissagregationAssignationToIndicatorsToEnable) {
            List<IndicatorExecution> iesToEnable = this.indicatorExecutionDao.getByPeriodIdAndIndicatorId(dissagregationAssignationToIndicator.getPeriod().getId(), dissagregationAssignationToIndicator.getIndicator().getId());
            iesToUpdateTotals.addAll(iesToEnable);
            enableCustomDissagregationAssignationIndIndicatorExecution(iesToEnable, dissagregationAssignationToIndicator.getCustomDissagregation());
        }
        for (CustomDissagregationAssignationToIndicator dissagregationAssignationToIndicator : customDissagregationAssignationToIndicatorsToDisable) {
            List<IndicatorExecution> iesToDisable = this.indicatorExecutionDao.getByPeriodIdAndIndicatorId(dissagregationAssignationToIndicator.getPeriod().getId(), dissagregationAssignationToIndicator.getIndicator().getId());
            iesToUpdateTotals.addAll(iesToDisable);
            disableCustomDissagregationAsignationInIndicatiorExecution(iesToDisable, dissagregationAssignationToIndicator.getCustomDissagregation());
        }

        for (CustomDissagregationAssignationToIndicator dissagregationAssignationToIndicator : customDissagregationAssignationToIndicatorsToCreate) {
            List<IndicatorExecution> iesToCreate = this.indicatorExecutionDao.getByPeriodIdAndIndicatorId(dissagregationAssignationToIndicator.getPeriod().getId(), dissagregationAssignationToIndicator.getIndicator().getId());
            iesToUpdateTotals.addAll(iesToCreate);
            createCustomDissagregationAsignationInIndicatiorExecution(iesToCreate, dissagregationAssignationToIndicator.getCustomDissagregation());
        }

        for (IndicatorExecution ieToUpdateTotal : iesToUpdateTotals) {
            this.updateIndicatorExecutionTotals(ieToUpdateTotal);
            this.saveOrUpdate(ieToUpdateTotal);
        }
    }

    private void createCustomDissagregationAsignationInIndicatiorExecution(List<IndicatorExecution> iesToCreate, CustomDissagregation customDissagregation) {
        for (IndicatorExecution indicatorExecution : iesToCreate) {
            CustomDissagregationAssignationToIndicatorExecution dissagregationAssignationToIndicatorExecution = new CustomDissagregationAssignationToIndicatorExecution();
            dissagregationAssignationToIndicatorExecution.setState(State.ACTIVO);
            dissagregationAssignationToIndicatorExecution.setCustomDissagregation(customDissagregation);
            indicatorExecution.addCustomDissagregationAssignationToIndicatorExecution(dissagregationAssignationToIndicatorExecution);
            List<Month> months = indicatorExecution.getQuarters().stream().flatMap(quarter -> quarter.getMonths().stream()).collect(Collectors.toList());
            for (Month month : months) {
                List<IndicatorValueCustomDissagregation> ivs = this.indicatorValueCustomDissagregationService.createIndicatorValuesCustomDissagregationForMonth(customDissagregation);
                for (IndicatorValueCustomDissagregation iv : ivs) {
                    month.addIndicatorValueCustomDissagregation(iv);
                }
            }
        }
    }

    private void disableCustomDissagregationAsignationInIndicatiorExecution(List<IndicatorExecution> iesToDisable, CustomDissagregation customDissagregation) {
        for (IndicatorExecution indicatorExecution : iesToDisable) {
            indicatorExecution.getCustomDissagregationAssignationToIndicatorExecutions()
                    .stream()
                    .filter(dissagregationAssignationToIndicatorExecution ->
                            dissagregationAssignationToIndicatorExecution.getCustomDissagregation().getId()
                                    .equals(customDissagregation.getId()))
                    .findFirst()
                    .ifPresent(dissagregationAssignationToIndicatorExecution -> {
                        dissagregationAssignationToIndicatorExecution.setState(State.INACTIVO);
                        indicatorExecution.getQuarters()
                                .stream().flatMap(quarter -> quarter.getMonths().stream())
                                .flatMap(month -> month.getIndicatorValuesIndicatorValueCustomDissagregations().stream())
                                .filter(indicatorValue -> indicatorValue.getCustomDissagregationOption().getCustomDissagregation().getId().equals(customDissagregation.getId()))
                                .forEach(indicatorValue -> indicatorValue.setState(State.INACTIVO));
                    });
        }
    }

    private void enableCustomDissagregationAssignationIndIndicatorExecution(List<IndicatorExecution> iesToEnable, CustomDissagregation customDissagregation) {
        for (IndicatorExecution indicatorExecution : iesToEnable) {
            indicatorExecution.getCustomDissagregationAssignationToIndicatorExecutions()
                    .stream()
                    .filter(customDissagregationAssignationToIndicatorExecution ->
                            customDissagregationAssignationToIndicatorExecution.getCustomDissagregation().getId()
                                    .equals(customDissagregation.getId()))
                    .findFirst()
                    .ifPresent(customDissagregationAssignationToIndicatorExecution -> {
                        customDissagregationAssignationToIndicatorExecution.setState(State.ACTIVO);
                        indicatorExecution.getQuarters()
                                .stream().flatMap(quarter -> quarter.getMonths().stream())
                                .flatMap(month -> month.getIndicatorValuesIndicatorValueCustomDissagregations().stream())
                                .filter(indicatorValue -> indicatorValue.getCustomDissagregationOption().getCustomDissagregation().getId().equals(customDissagregation.getId()))
                                .forEach(indicatorValue -> indicatorValue.setState(State.ACTIVO));
                    });
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
                IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigmentToDissable = indicatorExecutionLocationAssigmentToDissableOpt.get();
                locationsToDissableIe.add(indicatorExecutionLocationAssigmentToDissable.getLocation());
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

    // todo 2024
    private void createDissagregationAsignationInIndicatiorExecution(List<IndicatorExecution> iesToUpdate, DissagregationType dissagregationType) throws GeneralAppException {
        /*for (IndicatorExecution indicatorExecution : iesToUpdate) {
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

                List<IndicatorValue> ivs = this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(dissagregationType, cantones, indicatorExecution.getPeriod());
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
        }*/
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

    public void quartersTargetUpdate(List<QuarterWeb> quarterWebs) throws GeneralAppException {
        List<Long> quartersIds = quarterWebs.stream().map(QuarterWeb::getId).collect(Collectors.toList());
        List<Long> indicatorsExecutionsIdsToUpdate = this.indicatorExecutionDao.getByQuartersIds(quartersIds);
        List<IndicatorExecution> indicatorsExecutionsToUpdate = this.indicatorExecutionDao.getByIdsWithIndicatorValues(indicatorsExecutionsIdsToUpdate);
        List<Quarter> quartersList = indicatorsExecutionsToUpdate.stream()
                .map(IndicatorExecution::getQuarters)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        for (QuarterWeb quarterWeb : quarterWebs) {
            Optional<Quarter> quarterOpt = quartersList.stream().filter(quarter -> quarterWeb.getId().equals(quarter.getId())).findFirst();

            if (quarterOpt.isPresent()) {
                quarterOpt.get().setTarget(quarterWeb.getTarget());
            } else {
                throw new GeneralAppException("No se pudo encontrar el trimestre con id " + quarterWeb.getId(), Response.Status.NOT_FOUND);
            }
        }

        for (IndicatorExecution indicatorExecution : indicatorsExecutionsToUpdate) {
            this.updateIndicatorExecutionTotals(indicatorExecution);
            this.saveOrUpdate(indicatorExecution);
        }
    }

    public List<User> getSupervisorsByPeriodId(Long periodId) {
        return this.indicatorExecutionDao.getSupervisorstByPeriodId(periodId);
    }

    public List<IndicatorExecutionWeb> getDirectImplementationsIndicatorExecutionsBySupervisorId(Long periodId, Long supervisorId) throws GeneralAppException {
        List<IndicatorExecution> r = this.indicatorExecutionDao.getDirectImplementationsIndicatorExecutionsBySupervisorId(periodId, supervisorId);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(r, false);
    }



}






