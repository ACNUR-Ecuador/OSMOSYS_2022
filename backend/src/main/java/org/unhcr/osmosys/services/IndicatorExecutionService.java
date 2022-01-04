package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorExecutionDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.model.enums.TotalIndicatorCalculationType;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    IndicatorService indicatorService;

    @Inject
    ProjectService projectService;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

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


        List<Canton> cantones = project.getProjectLocationAssigments().stream().filter(projectLocationAssigment -> {
            return projectLocationAssigment.getState().equals(State.ACTIVO);
        }).map(ProjectLocationAssigment::getLocation).collect(Collectors.toList());
        List<DissagregationType> dissagregationTypes;
        GeneralIndicator generalIndicator = this.generalIndicatorService.getByPeriodIdAndState(project.getPeriod().getId(), State.ACTIVO);

        if (generalIndicator != null) {
            Set<DissagregationAssignationToGeneralIndicator> dissagregations = generalIndicator.getDissagregationAssignationsToGeneralIndicator();
            LOGGER.error(dissagregations.size());
            if (CollectionUtils.isNotEmpty(dissagregations)) {
                dissagregationTypes = dissagregations.stream()
                        .filter(dissagregationAssignationToGeneralIndicator -> {
                            return dissagregationAssignationToGeneralIndicator.getState().equals(State.ACTIVO);
                        }).map(DissagregationAssignationToGeneralIndicator::getDissagregationType)
                        .collect(Collectors.toList());
            } else {
                dissagregationTypes = new ArrayList<>();
            }

        } else {
            dissagregationTypes = new ArrayList<>();
        }


        List<CustomDissagregation> customDissagregations = new ArrayList<>();
        Set<Quarter> qs = this.quarterService.createQuarters(project.getStartDate(), project.getEndDate(), dissagregationTypes, customDissagregations, cantones);
        List<Quarter> qsl = setOrderInQuartersAndMonths(qs);
        for (Quarter quarter : qsl) {
            ie.addQuarter(quarter);
        }


        for (Quarter q : qsl) {
            LOGGER.error("---->" + q.toString());
            for (Month month : q.getMonths().stream().sorted(Comparator.comparingInt(Month::getOrder)).collect(Collectors.toList())) {
                LOGGER.error(month.toString());
                LOGGER.error("indicatorValues " + month.getIndicatorValues().size());
                List<IndicatorValue> iv = month.getIndicatorValues().stream().sorted(Comparator.comparing(IndicatorValue::getDissagregationType)).collect(Collectors.toList());
                for (IndicatorValue indicatorValue : iv) {
                    LOGGER.error(indicatorValue.getDissagregationType() + "-"
                            + indicatorValue.getDiversityType() + "-"
                            + indicatorValue.getLocation() + "-"
                            + indicatorValue.getAgeType() + "-"
                            + indicatorValue.getCountryOfOrigin() + "-"
                            + indicatorValue.getGenderType() + "-"
                            + indicatorValue.getPopulationType() + "-"
                    );
                }
            }
        }

        return ie;

    }

    public IndicatorExecution assignPerformanceIndicatoToProject(IndicatorExecutionAssigmentWeb indicatorExecutionWeb) throws GeneralAppException {
        this.validatePerformanceIndicatorAssignationToProject(indicatorExecutionWeb);
        IndicatorExecution ie = new IndicatorExecution();
        Indicator indicator = this.indicatorService.getById(indicatorExecutionWeb.getIndicator().getId());
        if (indicator == null) {
            throw new GeneralAppException("Indicador no encontrado " + indicatorExecutionWeb.getIndicator().getId(), Response.Status.BAD_REQUEST);
        }
        ie.setIndicator(indicator);
        ie.setCompassIndicator(indicator.getCompassIndicator());
        ie.setIndicatorType(indicator.getIndicatorType());
        ie.setState(indicatorExecutionWeb.getState());
        Project project = this.projectService.getById(indicatorExecutionWeb.getProject().getId());
        if (project == null) {
            throw new GeneralAppException("Proyecto no encontrado " + indicatorExecutionWeb.getProject().getId(), Response.Status.BAD_REQUEST);
        }
        ie.setPeriod(project.getPeriod());
        List<DissagregationAssignationToIndicatorExecution> dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator().stream().filter(dissagregationAssignationToIndicator -> {
            return dissagregationAssignationToIndicator.getState().equals(State.ACTIVO);
        }).map(dissagregationAssignationToIndicator -> {
            DissagregationAssignationToIndicatorExecution da = new DissagregationAssignationToIndicatorExecution();
            da.setState(State.ACTIVO);
            da.setDissagregationType(dissagregationAssignationToIndicator.getDissagregationType());
            return da;
        }).collect(Collectors.toList());
        dissagregationAssignations.forEach(dissagregationAssignationToIndicatorExecution -> {
            ie.addDissagregationAssignationToIndicatorExecution(dissagregationAssignationToIndicatorExecution);
        });

        List<CustomDissagregationAssignationToIndicatorExecution> customDissagregationsAssignations = indicator.getCustomDissagregationAssignationToIndicators().stream().filter(customDissagregationAssignationToIndicator -> {
            return customDissagregationAssignationToIndicator.getState().equals(State.ACTIVO);
        }).map(customDissagregationAssignationToIndicator -> {
            CustomDissagregationAssignationToIndicatorExecution da = new CustomDissagregationAssignationToIndicatorExecution();
            da.setCustomDissagregation(customDissagregationAssignationToIndicator.getCustomDissagregation());
            da.setState(State.ACTIVO);
            return da;
        }).collect(Collectors.toList());
        customDissagregationsAssignations.forEach(customDissagregationAssignationToIndicatorExecution -> {
            ie.addCustomDissagregationAssignationToIndicatorExecution(customDissagregationAssignationToIndicatorExecution);
        });
        //TODO FILTERS

        ie.setProject(project);
        // TODO MARKERS
        List<Canton> cantones = new ArrayList<>();
        if (indicatorExecutionWeb.getLocations().size() > 0) {
            cantones = this.modelWebTransformationService.cantonsWebToCantons(indicatorExecutionWeb.getLocations());

        }
        List<DissagregationType> dissagregationTypes;

        dissagregationTypes = dissagregationAssignations.stream().map(dissagregationAssignationToIndicatorExecution -> {
            return dissagregationAssignationToIndicatorExecution.getDissagregationType();
        }).collect(Collectors.toList());


        List<CustomDissagregation> customDissagregations = customDissagregationsAssignations.stream().map(customDissagregationAssignationToIndicatorExecution -> {
            return customDissagregationAssignationToIndicatorExecution.getCustomDissagregation();
        }).collect(Collectors.toList());
        Set<Quarter> qs = this.quarterService.createQuarters(project.getStartDate(), project.getEndDate(), dissagregationTypes, customDissagregations, cantones);
        List<Quarter> qsl = setOrderInQuartersAndMonths(qs);
        for (Quarter quarter : qsl) {
            ie.addQuarter(quarter);
        }


        this.saveOrUpdate(ie);
        for (Quarter q : qsl) {
            LOGGER.error("---->" + q.toString());
            for (Month month : q.getMonths().stream().sorted(Comparator.comparingInt(Month::getOrder)).collect(Collectors.toList())) {
                LOGGER.error(month.toString());
                LOGGER.error("indicatorValues " + month.getIndicatorValues().size());
                List<IndicatorValue> iv = month.getIndicatorValues().stream().sorted(Comparator.comparing(IndicatorValue::getDissagregationType)).collect(Collectors.toList());
                for (IndicatorValue indicatorValue : iv) {
                    LOGGER.error(indicatorValue.getDissagregationType() + "-"
                            + indicatorValue.getDiversityType() + "-"
                            + indicatorValue.getLocation() + "-"
                            + indicatorValue.getAgeType() + "-"
                            + indicatorValue.getCountryOfOrigin() + "-"
                            + indicatorValue.getGenderType() + "-"
                            + indicatorValue.getPopulationType() + "-"
                    );
                }
            }
        }

        return ie;

    }

    private List<Quarter> setOrderInQuartersAndMonths(Set<Quarter> qs) {
        List<Quarter> qsl = qs.stream().sorted((o1, o2) -> {
            if (o1.getYear() < o2.getYear()) {
                return -1;
            } else if (o1.getYear() > o2.getYear()) {
                return 1;
            } else if (o1.getQuarter().getOrder() < o2.getQuarter().getOrder()) {
                return -1;
            } else if (o1.getQuarter().getOrder() > o2.getQuarter().getOrder()) {
                return 1;
            } else {
                return 0;
            }
        }).collect(Collectors.toList());

        int orderQ = 1;
        for (Quarter quarter : qsl) {
            quarter.setOrder(orderQ);
            orderQ++;
        }
        List<Month> monthList =
                qs.stream()
                        .map(quarter -> {
                            List<Month> monthsList = new ArrayList<>(quarter.getMonths());
                            return monthsList;
                        }).flatMap(Collection::stream).sorted((o1, o2) -> {
                            if (o1.getYear() < o2.getYear()) {
                                return -1;
                            } else if (o1.getYear() > o2.getYear()) {
                                return 1;
                            } else if (o1.getMonth().getOrder() < o2.getMonth().getOrder()) {
                                return -1;
                            } else if (o1.getMonth().getOrder() > o2.getMonth().getOrder()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }).collect(Collectors.toList());
        int orderM = 1;
        for (Month month : monthList) {
            month.setOrder(orderM);
            orderM++;
        }
        return qsl;
    }


    public List<IndicatorExecutionGeneralIndicatorAdministrationResumeWeb> getGeneralIndicatorExecutionsAdministrationByProjectId(Long projectId) {
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getGeneralIndicatorExecutionsByProjectId(projectId);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionGeneralIndicatorAdministrationResumesWeb(ies);
    }

    public List<IndicatorExecutionGeneralIndicatorResumeWeb> getGeneralIndicatorExecutionsByProjectId(Long projectId, State state) {
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getGeneralIndicatorExecutionsByProjectIdAndState(projectId, state);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionGeneralIndicatorResumesWeb(ies);
    }

    public void updateTargets(TargetUpdateDTOWeb targetUpdateDTOWeb) throws GeneralAppException {
        IndicatorExecution ie = this.indicatorExecutionDao.getByIdWithValues(targetUpdateDTOWeb.getIndicatorExecutionId());
        for (QuarterResumeWeb quarterResumeWeb : targetUpdateDTOWeb.getQuarters()) {
            Optional<Quarter> quarterOptional = ie.getQuarters().stream().filter(quarter -> {
                return quarter.getId().equals(quarterResumeWeb.getId());
            }).findFirst();
            if (!quarterOptional.isPresent()) {
                throw new GeneralAppException("No se pudo encontrar el trimestre con id " + quarterResumeWeb.getId(), Response.Status.NOT_FOUND);
            } else {
                quarterOptional.get().setTarget(quarterResumeWeb.getTarget());
            }
        }


        this.updateIndicatorExecutionTotals(ie);

        this.saveOrUpdate(ie);
    }

    public void updateIndicatorExecutionTotals(IndicatorExecution indicatorExecution) throws GeneralAppException {
        TotalIndicatorCalculationType totalIndicatorCalculationType = null;
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

        List<Quarter> quarters = indicatorExecution.getQuarters().stream().filter(quarter -> {
            return quarter.getState().equals(State.ACTIVO);
        }).collect(Collectors.toList());
// target total
        Optional<BigDecimal> totalTarget = quarters.stream().map(Quarter::getTarget).filter(Objects::nonNull).reduce(BigDecimal::add);
        if (totalTarget.isPresent()) {
            indicatorExecution.setTarget(totalTarget.get());
        } else {
            indicatorExecution.setTarget(null);
        }
        // total execution and total percentage
        List<BigDecimal> totalQuarterValues = quarters.stream()
                .map(Quarter::getTotalExecution).filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(totalQuarterValues)) {
            indicatorExecution.setTotalExecution(null);
            indicatorExecution.setExecutionPercentage(null);
        } else {
            BigDecimal totalExecution = null;
            switch (totalIndicatorCalculationType) {
                case SUMA:
                    totalExecution = totalQuarterValues.stream().reduce(BigDecimal::add).get();
                    break;
                case PROMEDIO:
                    BigDecimal total = totalQuarterValues.stream().reduce(BigDecimal::add).get();
                    totalExecution = total.divide(new BigDecimal(totalQuarterValues.size()), RoundingMode.HALF_UP);
                    break;
                case MAXIMO:
                    totalExecution = totalQuarterValues.stream().reduce(BigDecimal::max).get();
                    break;
                case MINIMO:
                    totalExecution = totalQuarterValues.stream().reduce(BigDecimal::min).get();
                    break;
                default:
                    throw new GeneralAppException("Tipo de calculo no soportado, por favor comuniquese con el administrador del sistema", Response.Status.INTERNAL_SERVER_ERROR);
            }
            indicatorExecution.setTotalExecution(totalExecution);

            if (indicatorExecution.getTotalExecution() != null && indicatorExecution.getTarget() != null) {
                if (indicatorExecution.getTarget().equals(BigDecimal.ZERO)) {
                    indicatorExecution.setTarget(BigDecimal.ZERO);
                } else {
                    indicatorExecution.setExecutionPercentage(indicatorExecution.getTotalExecution().divide(totalExecution, RoundingMode.HALF_UP));
                }
            } else {
                indicatorExecution.setExecutionPercentage(null);
            }
        }
    }

    public List<IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb> getPerformanceIndicatorExecutionsAdministrationByProjectId(Long projectId) {
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getPerformanceIndicatorExecutionsByProjectId(projectId);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionPerformanceIndicatorAdministrationResumesWeb(ies);
    }

    public void validatePerformanceIndicatorAssignationToProject(IndicatorExecutionAssigmentWeb indicatorExecutionWeb) throws GeneralAppException {
        if (indicatorExecutionWeb == null) {
            throw new GeneralAppException("La asignación es obligatorio", Response.Status.BAD_REQUEST);
        }

        if (indicatorExecutionWeb.getIndicator() == null || indicatorExecutionWeb.getIndicator().getId() == null) {
            throw new GeneralAppException("Indicador asignado es obligatorio.", Response.Status.BAD_REQUEST);
        }

        if (indicatorExecutionWeb.getState() == null) {
            throw new GeneralAppException("El estado es obligatorio.", Response.Status.BAD_REQUEST);
        }

        if (indicatorExecutionWeb.getProject() == null || indicatorExecutionWeb.getProject().getId() == null) {
            throw new GeneralAppException("El proyecto asignado es nulo.", Response.Status.BAD_REQUEST);
        }
    }

    public IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb getResumeAdministrationPerformanceIndicatorById(Long id) {
        return this.modelWebTransformationService.
                indicatorExecutionToIndicatorExecutionPerformanceIndicatorAdministrationResumeWeb(this.indicatorExecutionDao.getPerformanceIndicatorExecutionById(id));
    }

    public Long updateMonthValues(Long indicatorExecutionId, MonthValuesWeb monthValuesWeb) throws GeneralAppException {
        // get indicator execution id
        IndicatorExecution indicatorExecution = this.indicatorExecutionDao.getByIdWithValues(indicatorExecutionId);
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
        Quarter quarterToUpdate = null;
        for (Quarter quarter : indicatorExecution.getQuarters()) {
            Optional<Month> monthToUpdateOp = quarter.getMonths().stream()
                    .filter(month -> monthValuesWeb.getMonth().getId().equals(month.getId()))
                    .findFirst();
            if (monthToUpdateOp.isPresent()) {
                monthToUpdate = monthToUpdateOp.get();
                quarterToUpdate = quarter;
                break;
            }
        }
        if (monthToUpdate == null) {
            throw new GeneralAppException("No se pudo encontrar el mes (monthId:" + monthValuesWeb.getMonth().getId() + ")", Response.Status.BAD_REQUEST);
        }
        List<IndicatorValueWeb> totalIndicatorValueWebs = new ArrayList<>();
        monthValuesWeb.getIndicatorValuesMap().forEach((dissagregationType, indicatorValueWebs) -> {
            totalIndicatorValueWebs.addAll(indicatorValueWebs);
        });
        for (IndicatorValueWeb indicatorValueWeb : totalIndicatorValueWebs) {
            Optional<IndicatorValue> valueToUpdateOp = monthToUpdate.getIndicatorValues().stream().filter(indicatorValue -> {
                return indicatorValue.getId().equals(indicatorValueWeb.getId());
            }).findFirst();
            if (valueToUpdateOp.isPresent()) {
                IndicatorValue valueToUpdate = valueToUpdateOp.get();
                valueToUpdate.setValue(indicatorValueWeb.getValue());
                valueToUpdate.setNumeratorValue(indicatorValueWeb.getNumeratorValue());
                valueToUpdate.setDenominatorValue(indicatorValueWeb.getDenominatorValue());
            } else {
                throw new GeneralAppException("No se pudo encontrar el valor (valueId:" + indicatorValueWeb.getId() + ")", Response.Status.BAD_REQUEST);
            }
        }
        this.updateIndicatorExecutionTotals(indicatorExecution);
        this.saveOrUpdate(indicatorExecution);
        return indicatorExecution.getId();
    }
}






