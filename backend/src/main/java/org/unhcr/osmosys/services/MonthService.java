package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.MonthDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.model.enums.QuarterEnum;
import org.unhcr.osmosys.model.enums.TotalIndicatorCalculationType;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class MonthService {

    @Inject
    MonthDao monthDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    IndicatorValueService indicatorValueService;
    @Inject
    IndicatorValueCustomDissagregationService indicatorValueCustomDissagregationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(MonthService.class);

    public Month getById(Long id) {
        return this.monthDao.find(id);
    }

    public Month saveOrUpdate(Month month) {
        if (month.getId() == null) {
            this.monthDao.save(month);
        } else {
            this.monthDao.update(month);
        }
        return month;
    }

    public List<Month> createMonthsForQuarter(Quarter quarter, LocalDate startDate, LocalDate endDate,
                                              List<DissagregationType> dissagregationTypes, List<CustomDissagregation> customDissagregations,
                                              List<Canton> cantones) throws GeneralAppException {
        QuarterEnum quarterEnum = quarter.getQuarter();
        List<MonthEnum> monthsEnums = MonthEnum.getMonthsByQuarter(quarterEnum);
        // solo los meses q enten dentro del periodo


        List<Month> months = new ArrayList<>();
        for (MonthEnum monthEnum : monthsEnums) {
            LocalDate firstDay = LocalDate.of(quarter.getYear(), monthEnum.getOrder(), 1);
            LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
            if (
                    firstDay.isBefore(endDate.plusDays(1)) && lastDay.isAfter(startDate.minusDays(1))
            ) {


                Month month = this.createMonth(quarter.getYear(), monthEnum, dissagregationTypes, customDissagregations, cantones);
                months.add(month);
            }
        }
        return months;

    }

    public Month createMonth(Integer year, MonthEnum monthEnum,
                             List<DissagregationType> dissagregationTypes, List<CustomDissagregation> customDissagregations,
                             List<Canton> cantones) throws GeneralAppException {
        Month m = new Month();
        m.setState(State.ACTIVO);
        m.setYear(year);
        m.setMonth(monthEnum);

        Set<IndicatorValue> indicatorValues = new HashSet<>();
        for (DissagregationType dissagregationType : dissagregationTypes) {
            indicatorValues.addAll(this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(dissagregationType, cantones));
        }

        for (IndicatorValue indicatorValue : indicatorValues) {
            m.addIndicatorValue(indicatorValue);

        }
        Set<IndicatorValueCustomDissagregation> indicatorValuesCustomDissagregations = new HashSet<>();
        for (CustomDissagregation customDissagregation : customDissagregations) {
            indicatorValuesCustomDissagregations.addAll(this.indicatorValueCustomDissagregationService.createIndicatorValuesCustomDissagregationForMonth(customDissagregation));
        }
        for (IndicatorValueCustomDissagregation indicatorValuesCustomDissagregation : indicatorValuesCustomDissagregations) {
            m.addIndicatorValueCustomDissagregation(indicatorValuesCustomDissagregation);
        }
        return m;
    }

    public void updateMonthTotals(Month month, TotalIndicatorCalculationType totalIndicatorCalculationType) throws GeneralAppException {
        List<IndicatorValue> ivs = month.getIndicatorValues().stream().filter(indicatorValue -> {
            return indicatorValue.getState().equals(State.ACTIVO);
        }).collect(Collectors.toList());

        Set<DissagregationType> dissagregationsTypes = ivs.stream().map(indicatorValue -> {
            return indicatorValue.getDissagregationType();
        }).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(dissagregationsTypes)) {
            month.setTotalExecution(BigDecimal.ZERO);
            return;
        }
        // veo cualquiera menos diversidad
        DissagregationType dissagregationTypeToCalculate = null;
        Optional<DissagregationType> dissagregationTypeOptional =
                dissagregationsTypes.stream().filter(dissagregationType1 -> {
                    return !dissagregationType1.equals(DissagregationType.DIVERSIDAD);
                }).findFirst();
        if (dissagregationTypeOptional.isPresent()) {
            dissagregationTypeToCalculate = dissagregationTypeOptional.get();
        } else {
            dissagregationTypeToCalculate = dissagregationsTypes.iterator().next();
        }
        DissagregationType finalDissagregationTypeToCalculate = dissagregationTypeToCalculate;
        ivs = ivs.stream().filter(indicatorValue -> {
            return indicatorValue.getDissagregationType().equals(finalDissagregationTypeToCalculate);
        }).collect(Collectors.toList());

        List<BigDecimal> valuesList = ivs.stream().map(IndicatorValue::getValue).filter(Objects::nonNull).collect(Collectors.toList());
        List<BigDecimal> numeratorsList = ivs.stream().map(IndicatorValue::getNumeratorValue).filter(Objects::nonNull).collect(Collectors.toList());
        List<BigDecimal> denominatorsList = ivs.stream().map(IndicatorValue::getDenominatorValue).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(valuesList) && CollectionUtils.isEmpty(numeratorsList) && CollectionUtils.isEmpty(denominatorsList)) {
            BigDecimal totalExecution = null;
            switch (totalIndicatorCalculationType) {
                case SUMA:
                    totalExecution = valuesList.stream().reduce(BigDecimal::add).get();
                    break;
                case PROMEDIO:
                    BigDecimal total = valuesList.stream().reduce(BigDecimal::add).get();
                    totalExecution = total.divide(new BigDecimal(valuesList.size()), RoundingMode.HALF_UP);
                    break;
                case MAXIMO:
                    totalExecution = valuesList.stream().reduce(BigDecimal::max).get();
                    break;
                case MINIMO:
                    totalExecution = valuesList.stream().reduce(BigDecimal::min).get();
                    break;
                default:
                    throw new GeneralAppException("Tipo de calculo no soportado, por favor comuniquese con el administrador del sistema", Response.Status.INTERNAL_SERVER_ERROR);
            }
            month.setTotalExecution(totalExecution);
        } else if (CollectionUtils.isNotEmpty(numeratorsList) && CollectionUtils.isNotEmpty(denominatorsList)) {

            BigDecimal totalExecution = null;
            BigDecimal totalNumeratorExecution = null;
            BigDecimal totalDenominatorExecution = null;
            switch (totalIndicatorCalculationType) {
                case SUMA:
                    totalExecution = ivs.stream().filter(indicatorValue -> {
                        return indicatorValue.getDenominatorValue() != null
                                && indicatorValue.getDenominatorValue().compareTo(BigDecimal.ZERO) != 0
                                && indicatorValue.getNumeratorValue() != null;
                    }).map(indicatorValue -> {
                        return indicatorValue.getNumeratorValue().divide(indicatorValue.getDenominatorValue(), RoundingMode.HALF_UP);
                    }).reduce(BigDecimal::add).get();
                    break;
                case PROMEDIO:
                    totalNumeratorExecution = numeratorsList.stream().reduce(BigDecimal::add).get();
                    totalDenominatorExecution = numeratorsList.stream().reduce(BigDecimal::add).get();
                    if (totalDenominatorExecution.compareTo(BigDecimal.ZERO) != 0) {
                        totalExecution = null;
                    } else {
                        totalExecution = totalNumeratorExecution.divide(totalDenominatorExecution, RoundingMode.HALF_UP);
                    }
                    break;
                case MAXIMO:
                    totalExecution = ivs.stream().filter(indicatorValue -> {
                        return indicatorValue.getDenominatorValue() != null
                                && indicatorValue.getDenominatorValue().compareTo(BigDecimal.ZERO) != 0
                                && indicatorValue.getNumeratorValue() != null;
                    }).map(indicatorValue -> {
                        return indicatorValue.getNumeratorValue().divide(indicatorValue.getDenominatorValue(), RoundingMode.HALF_UP);
                    }).reduce(BigDecimal::max).get();
                    break;
                case MINIMO:
                    totalExecution = ivs.stream().filter(indicatorValue -> {
                        return indicatorValue.getDenominatorValue() != null
                                && indicatorValue.getDenominatorValue().compareTo(BigDecimal.ZERO) != 0
                                && indicatorValue.getNumeratorValue() != null;
                    }).map(indicatorValue -> {
                        return indicatorValue.getNumeratorValue().divide(indicatorValue.getDenominatorValue(), RoundingMode.HALF_UP);
                    }).reduce(BigDecimal::min).get();
                    break;
                default:
                    throw new GeneralAppException("Tipo de calculo no soportado, por favor comuniquese con el administrador del sistema", Response.Status.INTERNAL_SERVER_ERROR);
            }
            month.setTotalExecution(totalExecution);
        } else {
            month.setTotalExecution(null);
        }

    }

    public MonthValuesWeb getMonthValuesWeb(Long monthId) {
        // TODO Q PASA SI NO HAY VALUES

        List<IndicatorValue> indicatorValues = this.indicatorValueService.getIndicatorValuesByMonthId(monthId);
        List<IndicatorValueCustomDissagregation> indicatorValuesCustomDissagregation =
                this.indicatorValueCustomDissagregationService.getIndicatorValuesByMonthId(monthId);

        List<IndicatorValueWeb> indicatorValuesWeb = this.modelWebTransformationService.indicatorsToIndicatorValuesWeb(new HashSet<>(indicatorValues));
        // clasifico por tipo desagreggacions


        Map<DissagregationType, List<IndicatorValueWeb>> map = new HashMap<>();
        for (DissagregationType dissagregationType : DissagregationType.values()) {
            map.put(dissagregationType, new ArrayList<>());
            // los filtros
            List<IndicatorValueWeb> values = indicatorValuesWeb.stream().filter(indicatorValue -> {
                return indicatorValue.getDissagregationType().equals(dissagregationType);
            }).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(values)) {
                map.put(dissagregationType, null);
            } else {
                map.put(dissagregationType, values);
            }
        }

        MonthValuesWeb r = new MonthValuesWeb();
        r.setMonth(this.modelWebTransformationService.monthToMonthWeb(indicatorValues.get(0).getMonth()));
        r.setIndicatorValuesMap(map);
        // clasifico por desagregaciones
        if (CollectionUtils.isNotEmpty(indicatorValuesCustomDissagregation)) {
            r.setCustomDissagregationValues(new ArrayList<>());
            Map<CustomDissagregation, Set<IndicatorValueCustomDissagregation>> mapCustom = new HashMap<>();
            // saco un set de disegraciones
            Set<CustomDissagregation> setCustomDissagegations = indicatorValuesCustomDissagregation.stream()
                    .map(indicatorValueCustomDissagregation -> {
                        return indicatorValueCustomDissagregation.getCustomDissagregationOption().getCustomDissagregation();
                    }).collect(Collectors.toSet());
            setCustomDissagegations.stream().forEach(customDissagregation -> {
                Set<IndicatorValueCustomDissagregation> values = indicatorValuesCustomDissagregation.stream().filter(indicatorValueCustomDissagregation -> {
                    return indicatorValueCustomDissagregation.getCustomDissagregationOption().getCustomDissagregation().getId().equals(customDissagregation.getId());
                }).collect(Collectors.toSet());
                mapCustom.put(customDissagregation, values);
            });
            mapCustom.forEach((customDissagregation, indicatorValueCustomDissagregations) -> {
                CustomDissagregationValuesWeb customDissagregationValuesWeb = new CustomDissagregationValuesWeb();
                customDissagregationValuesWeb.setCustomDissagregation(this.modelWebTransformationService.customDissagregationWebToCustomDissagregation(customDissagregation));

                List<IndicatorValueCustomDissagregationWeb> values = this.modelWebTransformationService.indicatorValueCustomDissagregationsToIndicatorValueCustomDissagregationWebs(indicatorValueCustomDissagregations);
                customDissagregationValuesWeb.setIndicatorValuesCustomDissagregation(values);
                r.getCustomDissagregationValues().add(customDissagregationValuesWeb);
            });
        }

        return r;

    }

    public void updateMonthLocationsByAssignation(Month month, List<Canton> cantones, List<DissagregationType> locationDissagregationTypes) throws GeneralAppException {
        List<IndicatorValue> newValues = new ArrayList<>();
        for (DissagregationType locationDissagregationType : locationDissagregationTypes) {
            newValues.addAll(this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(locationDissagregationType, cantones));
        }
        newValues.forEach(indicatorValue -> {
            month.addIndicatorValue(indicatorValue);
        });
    }
}
