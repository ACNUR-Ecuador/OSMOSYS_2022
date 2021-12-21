package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorExecutionDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
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


        Set<Quarter> qs = this.quarterService.createQuarters(project.getStartDate(), project.getEndDate(), dissagregationTypes, cantones);
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


}
