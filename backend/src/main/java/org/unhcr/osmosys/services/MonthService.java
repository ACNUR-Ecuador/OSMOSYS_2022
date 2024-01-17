package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.MonthDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.*;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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

    @Inject
    UtilsService utilsService;
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
                                              //List<DissagregationType> dissagregationTypes, List<CustomDissagregation> customDissagregations,
                                              Indicator indicator,
                                              List<Canton> cantones, Period period) throws GeneralAppException {
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


                Month month = this.createMonth(quarter.getYear(), monthEnum, indicator, cantones, period);
                months.add(month);
            }
        }
        return months;

    }

    public Month createMonth(Integer year, MonthEnum monthEnum,
                             // List<DissagregationType> dissagregationTypes, List<CustomDissagregation> customDissagregations,
                             Indicator indicator,
                             List<Canton> cantones, Period period) throws GeneralAppException {
        Month m = new Month();
        m.setState(State.ACTIVO);
        m.setYear(year);
        m.setMonth(monthEnum);
        m.setBlockUpdate(Boolean.FALSE);

        Set<IndicatorValue> indicatorValues = new HashSet<>();

        List<DissagregationAssignationToIndicator> dissagregationAssignationsToIndicator =
                indicator.getDissagregationsAssignationToIndicator().stream()
                        .filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getPeriod().getId().equals(period.getId()))
                        .collect(Collectors.toList());

        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : dissagregationAssignationsToIndicator) {
            indicatorValues.addAll(this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(dissagregationAssignationToIndicator, cantones, period));
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

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void updateMonthTotals(Month month, TotalIndicatorCalculationType totalIndicatorCalculationType) throws GeneralAppException {
        List<IndicatorValue> ivst = month.getIndicatorValues().stream()
                .filter(indicatorValue -> indicatorValue.getState().equals(State.ACTIVO))
                .collect(Collectors.toList());

        List<DissagregationType> dissagregationsTypes = ivst.stream()
                // no diversidad
                .filter(indicatorValue -> !indicatorValue.getDissagregationType().getStringValue().contains("DIVERSIDAD"))
                // solo valores
                .filter(indicatorValue -> indicatorValue.getValue() != null || indicatorValue.getNumeratorValue() != null || indicatorValue.getDenominatorValue() != null)
                .map(IndicatorValue::getDissagregationType)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dissagregationsTypes)) {
            month.setTotalExecution(null);
            return;
        }
        DissagregationType dissagregationTypeToCalculate;
        /*// veo cualquiera menos diversidad

        Optional<DissagregationType> dissagregationTypeOptional =
                dissagregationsTypes.stream()
                        .filter(dissagregationType1 ->
                                {
                                    return !dissagregationType1.getStringValue().contains("DIVERSIDAD");
                                }
                                *//*
                                !dissagregationType1.equals(DissagregationType.DIVERSIDAD)
                                        && !dissagregationType1.equals(DissagregationType.TIPO_POBLACION_Y_DIVERSIDAD)
                                        && !dissagregationType1.equals(DissagregationType.DIVERSIDAD_EDAD_Y_GENERO)
                                        && !dissagregationType1.equals(DissagregationType.DIVERSIDAD_EDAD_EDUCACION_PRIMARIA_Y_GENERO)
                                        && !dissagregationType1.equals(DissagregationType.DIVERSIDAD_EDAD_EDUCACION_TERCIARIA_Y_GENERO)
                                        && !dissagregationType1.equals(DissagregationType.GENERO_Y_DIVERSIDAD)
                                        && !dissagregationType1.equals(DissagregationType.LUGAR_DIVERSIDAD_EDAD_EDUCACION_PRIMARIA_Y_GENERO)*//*
                        )
                        .findFirst();*/
        dissagregationTypeToCalculate = dissagregationsTypes.iterator().next();
        DissagregationType finalDissagregationTypeToCalculate = dissagregationTypeToCalculate;
        List<IndicatorValue> ivsd = ivst.stream()
                .filter(indicatorValue -> indicatorValue.getDissagregationType().equals(finalDissagregationTypeToCalculate))
                .collect(Collectors.toList());

        List<BigDecimal> valuesList = ivsd.stream().map(IndicatorValue::getValue).filter(Objects::nonNull).collect(Collectors.toList());
        List<BigDecimal> numeratorsList = ivsd.stream().map(IndicatorValue::getNumeratorValue).filter(Objects::nonNull).collect(Collectors.toList());
        List<BigDecimal> denominatorsList = ivsd.stream().map(IndicatorValue::getDenominatorValue).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(valuesList) && CollectionUtils.isEmpty(numeratorsList) && CollectionUtils.isEmpty(denominatorsList)) {
            BigDecimal totalExecution = this.utilsService.calculetTotalExecution(totalIndicatorCalculationType, valuesList);
            if (totalExecution != null) {
                // fill with zero
                ivst.stream().filter(indicatorValue -> indicatorValue.getState().equals(State.ACTIVO))
                        .filter(indicatorValue -> indicatorValue.getValue() == null)
                        .forEach(indicatorValue -> indicatorValue.setValue(BigDecimal.ZERO));

            }
            month.setTotalExecution(totalExecution);
        } else if (CollectionUtils.isNotEmpty(numeratorsList) && CollectionUtils.isNotEmpty(denominatorsList)) {

            BigDecimal totalExecution;
            BigDecimal totalNumeratorExecution;
            BigDecimal totalDenominatorExecution;
            switch (totalIndicatorCalculationType) {
                case SUMA:
                    totalExecution = ivsd.stream().filter(indicatorValue -> indicatorValue.getDenominatorValue() != null
                            && indicatorValue.getDenominatorValue().compareTo(BigDecimal.ZERO) != 0
                            && indicatorValue.getNumeratorValue() != null).map(indicatorValue -> indicatorValue.getNumeratorValue().divide(indicatorValue.getDenominatorValue(), RoundingMode.HALF_UP)).reduce(BigDecimal::add).get();
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
                    totalExecution = ivsd.stream().filter(indicatorValue -> indicatorValue.getDenominatorValue() != null
                            && indicatorValue.getDenominatorValue().compareTo(BigDecimal.ZERO) != 0
                            && indicatorValue.getNumeratorValue() != null).map(indicatorValue -> indicatorValue.getNumeratorValue().divide(indicatorValue.getDenominatorValue(), RoundingMode.HALF_UP)).reduce(BigDecimal::max).get();
                    break;
                case MINIMO:
                    totalExecution = ivsd.stream().filter(indicatorValue -> indicatorValue.getDenominatorValue() != null
                            && indicatorValue.getDenominatorValue().compareTo(BigDecimal.ZERO) != 0
                            && indicatorValue.getNumeratorValue() != null).map(indicatorValue -> indicatorValue.getNumeratorValue().divide(indicatorValue.getDenominatorValue(), RoundingMode.HALF_UP)).reduce(BigDecimal::min).get();
                    break;
                default:
                    throw new GeneralAppException("Tipo de calculo no soportado, por favor comuniquese con el administrador del sistema", Response.Status.INTERNAL_SERVER_ERROR);
            }
            month.setTotalExecution(totalExecution);
        } else {
            month.setTotalExecution(null);
        }

    }

    public MonthValuesWeb getMonthValuesWeb(Long monthId, State state) {

        List<IndicatorValue> indicatorValues = this.indicatorValueService.getIndicatorValuesByMonthId(monthId, state);


        List<IndicatorValueCustomDissagregation> indicatorValuesCustomDissagregation =
                this.indicatorValueCustomDissagregationService.getIndicatorValuesByMonthId(monthId, state);

        List<IndicatorValueWeb> indicatorValuesWeb = this.modelWebTransformationService.indicatorsToIndicatorValuesWeb(new HashSet<>(indicatorValues));
        // Desagregaciones del IE
        List<DissagregationAssignationToIndicatorExecution> dissagregationAsignations = this.monthDao.getDissagregationsByMonthId(monthId);
        List<DissagregationType> dissagregationTypeAssignated = dissagregationAsignations.stream().map(DissagregationAssignationToIndicatorExecution::getDissagregationType).collect(Collectors.toList());
        // clasifico por tipo desagreggacions
        Map<DissagregationType, List<IndicatorValueWeb>> map = new HashMap<>();
        for (DissagregationType dissagregationType : DissagregationType.values()) {
            map.put(dissagregationType, new ArrayList<>());
            // los filtros
            List<IndicatorValueWeb> values = indicatorValuesWeb.stream().filter(indicatorValue -> indicatorValue.getDissagregationType().equals(dissagregationType)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(values)) {
                map.put(dissagregationType, null);
            } else {
                map.put(dissagregationType, values);
            }
        }
        // valido segregaciones, si no hay mando con array vacio
        for (DissagregationType dissagregationType : dissagregationTypeAssignated) {
            map.computeIfAbsent(dissagregationType, k -> new ArrayList<>());
        }

        MonthValuesWeb r = new MonthValuesWeb();
        if (CollectionUtils.isNotEmpty(indicatorValues)) {
            r.setMonth(this.modelWebTransformationService.monthToMonthWeb(indicatorValues.get(0).getMonth()));
        } else if (CollectionUtils.isNotEmpty(indicatorValuesCustomDissagregation)) {
            r.setMonth(this.modelWebTransformationService.monthToMonthWeb(indicatorValuesCustomDissagregation.get(0).getMonth()));
        } else {
            r.setMonth(this.modelWebTransformationService.monthToMonthWeb(this.monthDao.find(monthId)));
        }

        r.setIndicatorValuesMap(map);
        // clasifico por desagregaciones
        List<CustomDissagregationAssignationToIndicatorExecution> customDissagregationAsignations = this.monthDao.getCustomDissagregationsByMonthId(monthId);
        List<CustomDissagregation> customDissagregationTypeAssignated = customDissagregationAsignations
                .stream()
                .map(CustomDissagregationAssignationToIndicatorExecution::getCustomDissagregation)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(indicatorValuesCustomDissagregation)) {
            r.setCustomDissagregationValues(new ArrayList<>());
            Map<CustomDissagregation, Set<IndicatorValueCustomDissagregation>> mapCustom = new HashMap<>();
            // saco un set de disegraciones
            Set<CustomDissagregation> setCustomDissagegations = indicatorValuesCustomDissagregation.stream()
                    .map(indicatorValueCustomDissagregation -> indicatorValueCustomDissagregation.getCustomDissagregationOption().getCustomDissagregation()).collect(Collectors.toSet());
            setCustomDissagegations.forEach(customDissagregation -> {
                Set<IndicatorValueCustomDissagregation> values = indicatorValuesCustomDissagregation.stream().filter(indicatorValueCustomDissagregation -> indicatorValueCustomDissagregation.getCustomDissagregationOption().getCustomDissagregation().getId().equals(customDissagregation.getId())).collect(Collectors.toSet());
                mapCustom.put(customDissagregation, values);
            });
            mapCustom.forEach((customDissagregation, indicatorValueCustomDissagregations) -> {
                CustomDissagregationValuesWeb customDissagregationValuesWeb = new CustomDissagregationValuesWeb();
                customDissagregationValuesWeb.setCustomDissagregation(this.modelWebTransformationService.customDissagregationWebToCustomDissagregation(customDissagregation));

                List<IndicatorValueCustomDissagregationWeb> values = this.modelWebTransformationService.indicatorValueCustomDissagregationsToIndicatorValueCustomDissagregationWebs(indicatorValueCustomDissagregations);
                customDissagregationValuesWeb.setIndicatorValuesCustomDissagregation(values);
                r.getCustomDissagregationValues().add(customDissagregationValuesWeb);
            });
            // valido segregaciones, si no hay mando con array vacio
            for (CustomDissagregation customDissagregation : customDissagregationTypeAssignated) {
                mapCustom.computeIfAbsent(customDissagregation, k -> new HashSet<>());
            }
        }
        return r;

    }

    public void updateMonthLocationsByAssignation(Month month, List<Canton> cantones, List<DissagregationType> locationDissagregationTypes,
                                                  Period period

    ) throws GeneralAppException {
        List<IndicatorValue> newValues = new ArrayList<>();
        for (DissagregationType locationDissagregationType : locationDissagregationTypes) {

            newValues.addAll(this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(locationDissagregationType, cantones, period));
        }
        newValues.forEach(month::addIndicatorValue);
    }

    public List<MonthWeb> getMonthsIndicatorExecutionId(Long indicatorExecutionId, State state) {
        List<Month> months = this.monthDao.getMonthsIndicatorExecutionId(indicatorExecutionId, state);
        return this.modelWebTransformationService.monthsToMonthsWeb(new HashSet<>(months));
    }

    public List<Month> getMonthsIndicatorExecutionId(Long indicatorExecutionId) {
        return this.monthDao.getMonthsIndicatorExecutionId(indicatorExecutionId);
    }

    public Long changeMonthBlockedState(Long monthId, Boolean blockinState) {
        Month month = this.monthDao.find(monthId);
        if (!blockinState && month.getBlockUpdate()) {
            month.setChecked(false);
            // todo send alert email
        }

        month.setBlockUpdate(blockinState);
        this.saveOrUpdate(month);
        return month.getId();
    }

    public void getActiveMonthsByProjectIdAndMonthAndYear(Long projectId, MonthEnum month, int year, Boolean blockUpdate) {
        // get all months active by project
        List<Month> months = this.monthDao.getActiveMonthsByProjectIdAndMonthAndYear(projectId, month, year);
        for (Month monthE : months) {
            monthE.setBlockUpdate(blockUpdate);
            this.saveOrUpdate(monthE);
        }
    }

    public void blockMonthsAutomaticaly() throws GeneralAppException {
        int currentYear = this.utilsService.getCurrentYear();
        // mes pasado
        int currentMonth = this.utilsService.getCurrentMonthYearOrder() - 1;
        // si es enero, regreso a diciembre
        if (currentMonth < 1) {
            currentYear = currentYear - 1;
            currentMonth = 1;
        }
        this.blockMonthsByYearAndoMonth(currentYear, MonthEnum.getMonthByNumber(currentMonth), true);

    }

    public void blockMonthsByYearAndoMonth(int year, MonthEnum month, boolean block) throws GeneralAppException {

        // mes pasado
        int currentMonth = month.getOrder();
        // si es enero, regreso a diciembre


        List<Month> monthsToUpdate = new ArrayList<>();

        List<Month> unlockedMonthsMonthly = this.monthDao.getActiveMonthsAndMonthAndYearAndBlockingStatusAndFrecuency(month, year, !block, Frecuency.MENSUAL);
        unlockedMonthsMonthly.forEach(month1 -> month1.setBlockUpdate(block));
        monthsToUpdate.addAll(unlockedMonthsMonthly);

        // general indicators
        List<Month> unlockedMonthsMonthlyGeneral = this.monthDao.getActiveMonthsAndMonthAndYearAndBlockingStatusGeneralIndicators(month, year, !block);
        unlockedMonthsMonthlyGeneral.forEach(month1 -> month1.setBlockUpdate(block));
        monthsToUpdate.addAll(unlockedMonthsMonthly);
        // quarterly
        if (currentMonth == 3 || currentMonth == 6 || currentMonth == 9 || currentMonth == 12) {
            // this is last month of quarter
            QuarterEnum quarter = MonthEnum.getQuarterByMonthNumber(currentMonth);
            List<MonthEnum> monthsOfQuarter = Arrays.stream(MonthEnum.values()).filter(monthEnum -> monthEnum.getQuarterEnum().equals(quarter)).sorted((o1, o2) -> o2.getOrder() - o1.getOrder()).collect(Collectors.toList());
            for (MonthEnum monthEnum : monthsOfQuarter) {
                List<Month> unlockedMonthsQuarterM = this.monthDao.getActiveMonthsAndMonthAndYearAndBlockingStatusAndFrecuency(monthEnum, year, false, Frecuency.TRIMESTRAL);
                unlockedMonthsQuarterM.forEach(month1 -> month1.setBlockUpdate(block));
                List<Month> unlockedMonthsQuarterMGI = this.monthDao.getActiveGeneralIndicatorMonthsAndMonthAndYearAndBlockingStatusAndFrecuency(monthEnum, year, false);
                unlockedMonthsQuarterMGI.forEach(month1 -> month1.setBlockUpdate(block));
                monthsToUpdate.addAll(unlockedMonthsQuarterMGI);
            }
        }

        // hlaf year
        if (currentMonth == 6 || currentMonth == 12) {
            // this is last month of quarter
            List<MonthEnum> monthsOfSemester;
            if (currentMonth == 6) {
                monthsOfSemester = new ArrayList<>(Arrays.asList(MonthEnum.ENERO, MonthEnum.FEBRERO, MonthEnum.MARZO, MonthEnum.ABRIL, MonthEnum.MAYO, MonthEnum.JUNIO));
            } else {
                monthsOfSemester = new ArrayList<>(Arrays.asList(MonthEnum.JULIO, MonthEnum.AGOSTO, MonthEnum.SEPTIEMBRE, MonthEnum.OCTUBRE, MonthEnum.NOVIEMBRE, MonthEnum.DICIEMBRE));
            }
            for (MonthEnum monthEnum : monthsOfSemester) {
                List<Month> unlockedMonthsSemesterM = this.monthDao.getActiveMonthsAndMonthAndYearAndBlockingStatusAndFrecuency(monthEnum, year, false, Frecuency.SEMESTRAL);
                unlockedMonthsSemesterM.forEach(month1 -> month1.setBlockUpdate(block));
                monthsToUpdate.addAll(unlockedMonthsSemesterM);
            }
        }

        // annual
        if (currentMonth == 12) {
            // this is last month of year
            for (MonthEnum monthEnum : MonthEnum.values()) {
                List<Month> unlockedMonthsAnnual = this.monthDao.getActiveMonthsAndMonthAndYearAndBlockingStatusAndFrecuency(monthEnum, year, false, Frecuency.ANUAL);
                unlockedMonthsAnnual.forEach(month1 -> month1.setBlockUpdate(true));
                monthsToUpdate.addAll(unlockedMonthsAnnual);
            }
        }

        for (Month month1 : monthsToUpdate) {
            this.saveOrUpdate(month1);
        }

    }

    public List<YearMonthDTO> getYearMonthDTOSByPeriodId(Long periodId) {
        return this.monthDao.getYearMonthDTOSByPeriodId(periodId);
    }

    public void updateCustomDissagregationsOptions(CustomDissagregation customDissagregation) {
        List<Month> months = this.monthDao.getbyCustomDissagregationId(customDissagregation.getId());
        List<CustomDissagregationOption> optionsTotal = customDissagregation.getCustomDissagregationOptions().stream().collect(Collectors.toList());
        List<CustomDissagregationOption> optionsToDissable = customDissagregation.getCustomDissagregationOptions().stream().filter(customDissagregationOption -> customDissagregationOption.getState().equals(State.INACTIVO)).collect(Collectors.toList());
        List<CustomDissagregationOption> optionsToEnable = customDissagregation.getCustomDissagregationOptions().stream().filter(customDissagregationOption -> customDissagregationOption.getState().equals(State.ACTIVO)).collect(Collectors.toList());
        for (Month month : months) {
            Set<IndicatorValueCustomDissagregation> indicatorValues = month.getIndicatorValuesIndicatorValueCustomDissagregations();
            // creo las nuevas
            Collection<CustomDissagregationOption> newOptions = CollectionUtils.removeAll(optionsTotal, indicatorValues.stream().map(IndicatorValueCustomDissagregation::getCustomDissagregationOption).collect(Collectors.toList()));
            for (CustomDissagregationOption newOption : newOptions) {
                IndicatorValueCustomDissagregation indicatorValueCustomDissagregation = new IndicatorValueCustomDissagregation();
                indicatorValueCustomDissagregation.setCustomDissagregationOption(customDissagregation.getCustomDissagregationOptions().stream().filter(customDissagregationOption -> customDissagregationOption.equals(newOption)).findFirst().get());
                indicatorValueCustomDissagregation.setMonthEnum(month.getMonth());
                indicatorValueCustomDissagregation.setState(State.ACTIVO);
                indicatorValueCustomDissagregation.setMonthYearOrder(month.getMonthYearOrder());
                month.addIndicatorValueCustomDissagregation(indicatorValueCustomDissagregation);
            }
            // activo y desactivo
            for (IndicatorValueCustomDissagregation indicatorValue : indicatorValues) {
                if (optionsToDissable.contains(indicatorValue.getCustomDissagregationOption())) {
                    indicatorValue.setState(State.INACTIVO);
                } else if (optionsToEnable.contains(indicatorValue.getCustomDissagregationOption())) {
                    indicatorValue.setState(State.ACTIVO);
                }
            }
            this.saveOrUpdate(month);
        }
    }
}
