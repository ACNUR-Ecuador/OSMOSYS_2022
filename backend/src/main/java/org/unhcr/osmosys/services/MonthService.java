package org.unhcr.osmosys.services;

import
        com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.AuditAction;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.UserSecurityContext;
import com.sagatechs.generics.security.dao.UserDao;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorExecutionDao;
import org.unhcr.osmosys.daos.MonthDao;
import org.unhcr.osmosys.daos.ProjectDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.*;
import org.unhcr.osmosys.model.standardDissagregations.options.IndicatorValueOptionsDTO;
import org.unhcr.osmosys.model.standardDissagregations.options.StandardDissagregationOption;
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
    GeneralIndicatorService generalIndicatorService;

    @Inject
    DissagregationAssignationToIndicatorService dissagregationAssignationToIndicatorService;

    @Inject
    CustomDissagregationAssignationToIndicatorService customDissagregationAssignationToIndicatorService;

    @Inject
    UtilsService utilsService;

    @Inject
    AuditService auditService;

    @Inject
    UserDao userDao;

    @Inject
    private IndicatorExecutionDao indicatorExecutionDao;

    @Inject
    private ProjectDao projectDao;

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
                                              Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationMap,
                                              List<CustomDissagregation> customDissagregations
    ) throws GeneralAppException {
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


                Month month = this.createMonth(quarter.getYear(), monthEnum, dissagregationMap, customDissagregations);
                months.add(month);
            }
        }
        return months;

    }

    public Month createMonth(Integer year, MonthEnum monthEnum,
                             Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap,
                             List<CustomDissagregation> customDissagregations


    ) throws GeneralAppException {
        Month m = new Month();
        m.setState(State.ACTIVO);
        m.setYear(year);
        m.setMonth(monthEnum);
        m.setBlockUpdate(Boolean.FALSE);

        Set<IndicatorValue> indicatorValues = new HashSet<>();
        for (DissagregationType dissagregationType : dissagregationsMap.keySet()) {

            indicatorValues.addAll(this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(dissagregationType, dissagregationsMap.get(dissagregationType)));
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

    public void updateMonthDissagregations(Month month, Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> newDissagregationMap) throws GeneralAppException {

        // obtengo los datos actuales
        List<IndicatorValue> indicatorValues = this.indicatorValueService.getIndicatorValuesByMonthId(month.getId());
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> currentDissagregationMap = this.indicatorValueService.getDissagregationMapIndicatorValuesByMonthId(indicatorValues);

        // actualizar desagregaciones
        Set<DissagregationType> newsDissagregations = newDissagregationMap.keySet();
        Set<DissagregationType> currentDissagregations = currentDissagregationMap.keySet();
        // busco los nuevos
        Set<DissagregationType> dissagregationsToCreate = new HashSet<>(CollectionUtils.subtract(newsDissagregations, currentDissagregations));
        Set<DissagregationType> dissagregationsToDissable = new HashSet<>(CollectionUtils.subtract(currentDissagregations, newsDissagregations));
        Set<DissagregationType> dissagregationsToEnable = new HashSet<>(CollectionUtils.intersection(currentDissagregations, newsDissagregations));

        //  creo los nuevos
        for (DissagregationType dissagregationType : dissagregationsToCreate) {
            List<IndicatorValue> newIndicatorValues = this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(dissagregationType, newDissagregationMap.get(dissagregationType));
            for (IndicatorValue indicatorValue : newIndicatorValues) {
                month.addIndicatorValue(indicatorValue);
            }
            indicatorValues.addAll(newIndicatorValues);
        }
        // desactivo
        for (DissagregationType dissagregationType : dissagregationsToDissable) {
            indicatorValues.stream().filter(value -> value.getDissagregationType().equals(dissagregationType)).forEach(value -> value.setState(State.INACTIVO));
        }


        // activo
        for (DissagregationType dissagregationType : dissagregationsToEnable) {
            indicatorValues.stream().filter(value -> value.getDissagregationType().equals(dissagregationType)).forEach(value -> value.setState(State.ACTIVO));
        }


        // actualizar opciones
        for (DissagregationType dissagregationType : newDissagregationMap.keySet()) {
            if (dissagregationType.equals(DissagregationType.SIN_DESAGREGACION)) continue;
            List<IndicatorValue> dissagregationValues = indicatorValues.stream().filter(value -> value.getDissagregationType().equals(dissagregationType)).collect(Collectors.toList());
            updatedDissagregationOptions(month, dissagregationValues,
                    dissagregationType,
                    newDissagregationMap.get(dissagregationType), currentDissagregationMap.get(dissagregationType));

        }

    }

    /**
     * Actualiza las segregaciones de un mes en base a las asisgnaciones de segregaciones personalizadas asignadas a su indicador
     *
     * @param month
     * @param customDissagregationAssignationToIndicators
     */
    public void updateMonthCustomDissagregations(Month month, Set<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicators) {

        if(customDissagregationAssignationToIndicators == null) return;
        // obtengo todos los values
        Set<IndicatorValueCustomDissagregation> currentIndicatorValues = month.getIndicatorValuesIndicatorValueCustomDissagregations(); // this.indicatorValueCustomDissagregationService.getIndicatorValueCustomDissagregationsByMonthId(month.getId());

        Set<CustomDissagregation> currentDissagregations = currentIndicatorValues.stream().map(indicatorValueCustomDissagregation -> indicatorValueCustomDissagregation.getCustomDissagregationOption().getCustomDissagregation()).collect(Collectors.toSet());
        Set<CustomDissagregation> newDissagregations = customDissagregationAssignationToIndicators.stream().map(CustomDissagregationAssignationToIndicator::getCustomDissagregation).collect(Collectors.toSet());
        // busco las opciones q debo crear

        Set<CustomDissagregation> dissagregationsToCreate = new HashSet<>(CollectionUtils.subtract(newDissagregations, currentDissagregations));
        // creo
        Set<IndicatorValueCustomDissagregation> indicatorValuesCustomDissagregations = new HashSet<>();
        for (CustomDissagregation customDissagregation : dissagregationsToCreate) {
            indicatorValuesCustomDissagregations.addAll(this.indicatorValueCustomDissagregationService.createIndicatorValuesCustomDissagregationForMonth(customDissagregation));
            currentDissagregations.add(customDissagregation);
        }
        for (IndicatorValueCustomDissagregation indicatorValuesCustomDissagregation : indicatorValuesCustomDissagregations) {
            month.addIndicatorValueCustomDissagregation(indicatorValuesCustomDissagregation);
        }

        // ahora acualizo según los estados
        // activos todas para luego desactivar segun el caso
        month.getIndicatorValuesIndicatorValueCustomDissagregations().forEach(indicatorValueCustomDissagregation -> indicatorValueCustomDissagregation.setState(State.ACTIVO));
        // desactivo según el caso
        Set<IndicatorValueCustomDissagregation> valuesToDisable = new HashSet<>();
        for (CustomDissagregationAssignationToIndicator customDissagregationAssignationToIndicator : customDissagregationAssignationToIndicators) {
            // si la asignación esta inactiva desactivo todos los valores
            if (customDissagregationAssignationToIndicator.getState().equals(State.INACTIVO) || customDissagregationAssignationToIndicator.getCustomDissagregation().getState().equals(State.INACTIVO)) {
                Set<Long> optionsToDisableIds = customDissagregationAssignationToIndicator.getCustomDissagregation().getCustomDissagregationOptions().stream().map(CustomDissagregationOption::getId).collect(Collectors.toSet());
                Set<IndicatorValueCustomDissagregation> subValuesToDissable = month
                        .getIndicatorValuesIndicatorValueCustomDissagregations().stream()
                        .filter(indicatorValueCustomDissagregation -> optionsToDisableIds.contains(indicatorValueCustomDissagregation.getCustomDissagregationOption().getId()))
                        .collect(Collectors.toSet());
                valuesToDisable.addAll(subValuesToDissable);
            } else {
                // ahora por opción
                for (CustomDissagregationOption customDissagregationOption : customDissagregationAssignationToIndicator.getCustomDissagregation().getCustomDissagregationOptions()) {
                    if (customDissagregationOption.getState().equals(State.INACTIVO)) {
                        Set<IndicatorValueCustomDissagregation> subValuesToDissable = month.getIndicatorValuesIndicatorValueCustomDissagregations().stream().filter(indicatorValueCustomDissagregation -> indicatorValueCustomDissagregation.getCustomDissagregationOption().getId().equals(customDissagregationOption.getId())).collect(Collectors.toSet());
                        valuesToDisable.addAll(subValuesToDissable);
                    }
                }
            }
        }

        valuesToDisable.forEach(indicatorValueCustomDissagregation -> indicatorValueCustomDissagregation.setState(State.INACTIVO));


    }

    /**
     * @param month
     * @param indicatorValues                      solo los correspondientes a la desagregación q se está trabajando
     * @param dissagregationType
     * @param newSimpleDissagregationOptionMap
     * @param currentSimpleDissagregationOptionMap
     * @throws GeneralAppException
     */
    private void updatedDissagregationOptions(Month month, List<IndicatorValue> indicatorValues, DissagregationType dissagregationType,
                                              Map<DissagregationType, List<StandardDissagregationOption>> newSimpleDissagregationOptionMap,
                                              Map<DissagregationType, List<StandardDissagregationOption>> currentSimpleDissagregationOptionMap
    ) throws GeneralAppException {
        // si no hay current es porq es nuevo , no actgualizo se debe haber creado
        if (currentSimpleDissagregationOptionMap == null) return;

        // newSimpleDissagregationOptionMap
        List<DissagregationType> simpleDissagregation = dissagregationType.getSimpleDissagregations();
        for (DissagregationType simpleDissagregationType : simpleDissagregation) {
            List<StandardDissagregationOption> currentOptions = currentSimpleDissagregationOptionMap.get(simpleDissagregationType);
            List<StandardDissagregationOption> newOptions = newSimpleDissagregationOptionMap.get(simpleDissagregationType);
            if (newOptions == null) return;

            // to create
            List<StandardDissagregationOption> toCreate = new ArrayList<>(CollectionUtils.subtract(newOptions, currentOptions));
            Map<DissagregationType, List<StandardDissagregationOption>> simpleDissagregationOptionsMapTmp = new HashMap<>();
            for (DissagregationType dissagregationTypeTmp : simpleDissagregation) {
                if (dissagregationTypeTmp.equals(simpleDissagregationType)) {
                    simpleDissagregationOptionsMapTmp.put(dissagregationTypeTmp, toCreate);
                } else {
                    simpleDissagregationOptionsMapTmp.put(dissagregationTypeTmp, currentSimpleDissagregationOptionMap.get(dissagregationTypeTmp));
                }
            }
            List<IndicatorValue> newValues = this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(dissagregationType, simpleDissagregationOptionsMapTmp);
            newValues.forEach(month::addIndicatorValue);
            indicatorValues.addAll(newValues);
            currentSimpleDissagregationOptionMap.get(simpleDissagregationType).addAll(toCreate);


        }

        // una vez creados, activo y desactivo con los valores actuales
        // estos deberían estar activos
        // llamada 1********************


        if (dissagregationType.equals(DissagregationType.SIN_DESAGREGACION)) {
            throw new GeneralAppException("testing");
        } else {
            List<IndicatorValueOptionsDTO> indicatorValuesDTOs = this.indicatorValueService.getIndicatorValueOptionsDTOS(dissagregationType, newSimpleDissagregationOptionMap);
            // me aseguro q solo esten con la desagregación q trabajo
            for (IndicatorValue indicatorValue : indicatorValues) {
                if (!indicatorValue.getDissagregationType().equals(dissagregationType)) {
                    throw new GeneralAppException("Error, se está procesando una valor fuera de rango para la actualización de segregaciones y opciones");
                }
                boolean found = indicatorValuesDTOs.stream().anyMatch(indicatorValueOptionsDTO -> indicatorValueOptionsDTO.equals(indicatorValue.getDissagregationDTO()));
                if (found) {
                    indicatorValue.setState(State.ACTIVO);
                } else {
                    indicatorValue.setState(State.INACTIVO);
                }
            }
        }

    }


    /****************************************************************************************************************************************/

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


    public List<MonthWeb> getMonthsIndicatorExecutionId(Long indicatorExecutionId, State state) {
        List<Month> months = this.monthDao.getMonthsIndicatorExecutionId(indicatorExecutionId, state);
        return this.modelWebTransformationService.monthsToMonthsWeb(new HashSet<>(months));
    }



    public Long changeMonthBlockedState(Long monthId, Boolean blockinState) {
        Month month = this.monthDao.find(monthId);
        IndicatorExecution indicatorExecution=indicatorExecutionDao.find(month.getQuarter().getIndicatorExecution().getId());
        Object oldMonth;
        if(indicatorExecution.getProject()!=null){
            oldMonth=monthDao.findMonthRelatedProject(monthId);
        }else{
            oldMonth=monthDao.findMonthRelatedIndicator(monthId);
        }

        if (!blockinState && month.getBlockUpdate()) {
            month.setChecked(false);
            // todo send alert email
        }

        UserWeb principal = UserSecurityContext.getCurrentUser();

        User responsibleUser = principal != null ? userDao.findByUserName(principal.getUsername()) : null;


        month.setBlockUpdate(blockinState);
        Object newMonth;
        if(indicatorExecution.getProject()!=null){
            newMonth=monthDao.findMonthRelatedProject(monthId);
        }else{
            newMonth=monthDao.findMonthRelatedIndicator(monthId);
        }

        if (blockinState) {
            // Registrar auditoría con validación
            auditService.logAction(
                    "Bloqueo de Mes Indicador",
                    indicatorExecution.getProject() != null ? indicatorExecution.getProject().getCode() : null,
                    indicatorExecution.getIndicator().getCode(),
                    AuditAction.LOCK,
                    responsibleUser,
                    oldMonth,
                    newMonth,
                    State.ACTIVO
            );
        } else {
            // Registrar auditoría con validación
            auditService.logAction(
                    "Bloqueo de Mes Indicador",
                    indicatorExecution.getProject() != null ? indicatorExecution.getProject().getCode() : null,
                    indicatorExecution.getIndicator().getCode(),
                    AuditAction.UNLOCK,
                    responsibleUser,
                    oldMonth,
                    newMonth,
                    State.ACTIVO
            );
        }

        this.saveOrUpdate(month);
        return month.getId();
    }

    public void getActiveMonthsByProjectIdAndMonthAndYear(Long projectId, MonthEnum month, int year, Boolean blockUpdate) {
        // get all months active by project
        List<Month> months = this.monthDao.getActiveMonthsByProjectIdAndMonthAndYear(projectId, month, year);
        UserWeb principal = UserSecurityContext.getCurrentUser();
        User responsibleUser = principal != null ? userDao.findByUserName(principal.getUsername()) : null;
        Project project=projectDao.find(projectId);
        List<?> oldIndicatorsMonthState=monthDao.findIndicatorsRelatedProjectByMonth(projectId, month, year);
        for (Month monthE : months) {
            monthE.setBlockUpdate(blockUpdate);
            this.saveOrUpdate(monthE);
        }
        List<?> newIndicatorsMonthState=monthDao.findIndicatorsRelatedProjectByMonth(projectId, month, year);
        if(blockUpdate) {
            // Registrar auditoría
            auditService.logAction("Bloqueo de Mes Masivo", project.getCode(),null, AuditAction.LOCK, responsibleUser, oldIndicatorsMonthState, newIndicatorsMonthState, State.ACTIVO);
        }else {
            // Registrar auditoría
            auditService.logAction("Bloqueo de Mes Masivo", project.getCode(),null, AuditAction.UNLOCK, responsibleUser, oldIndicatorsMonthState, newIndicatorsMonthState, State.ACTIVO);

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


        List<Month> unlockedMonthsMonthly = this.monthDao.getActiveMonthsAndMonthAndYearAndBlockingStatusAndFrecuency(month, year, !block, Frecuency.MENSUAL);
        unlockedMonthsMonthly.forEach(month1 -> month1.setBlockUpdate(block));
        List<Month> monthsToUpdate = new ArrayList<>(unlockedMonthsMonthly);

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
        List<CustomDissagregationOption> optionsTotal = new ArrayList<>(customDissagregation.getCustomDissagregationOptions());
        List<CustomDissagregationOption> optionsToDissable = customDissagregation.getCustomDissagregationOptions().stream().filter(customDissagregationOption -> customDissagregationOption.getState().equals(State.INACTIVO)).collect(Collectors.toList());
        List<CustomDissagregationOption> optionsToEnable = customDissagregation.getCustomDissagregationOptions().stream().filter(customDissagregationOption -> customDissagregationOption.getState().equals(State.ACTIVO)).collect(Collectors.toList());
        for (Month month : months) {
            Set<IndicatorValueCustomDissagregation> indicatorValues = month.getIndicatorValuesIndicatorValueCustomDissagregations();
            // creo las nuevas
            Collection<CustomDissagregationOption> newOptions = CollectionUtils.removeAll(optionsTotal, indicatorValues.stream().map(IndicatorValueCustomDissagregation::getCustomDissagregationOption).collect(Collectors.toList()));
            for (CustomDissagregationOption newOption : newOptions) {
                IndicatorValueCustomDissagregation indicatorValueCustomDissagregation = new IndicatorValueCustomDissagregation();
                //noinspection OptionalGetWithoutIsPresent
                indicatorValueCustomDissagregation.setCustomDissagregationOption(customDissagregation.getCustomDissagregationOptions().stream()
                        .filter(customDissagregationOption -> customDissagregationOption.equals(newOption)).findFirst().get());
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


    /**************************************************/

    /**
     * crea el dto  MonthValuesWeb
     *
     * @param monthId id del mes
     * @param state   esado, siempre Activos
     * @return MonthValuesWeb
     */
    public MonthValuesWeb getMonthValuesWeb(Long monthId, State state) throws GeneralAppException {

        IndicatorExecution ie = this.monthDao.getIndicatorExecutionByMonthId(monthId);

        List<IndicatorValue> indicatorValues = this.indicatorValueService.getIndicatorValuesByMonthIdAndState(monthId, state);


        List<IndicatorValueCustomDissagregation> indicatorValuesCustomDissagregation =
                this.indicatorValueCustomDissagregationService.getIndicatorValuesByMonthId(monthId, state);

        List<IndicatorValueWeb> indicatorValuesWeb = this.modelWebTransformationService.indicatorsToIndicatorValuesWeb(new HashSet<>(indicatorValues));

        // verifico las tipo desagregaciones asignadas al indicador


        // Desagregaciones del IE
        List<DissagregationType> dissagregationTypeAssignated = ie.getIndicatorType().equals(IndicatorType.GENERAL) ? this.generalIndicatorService.getActiveGeneralIndicatorDissagregationTypeByPeriodId(ie.getPeriod().getId()) : this.dissagregationAssignationToIndicatorService.getActiveDissagregationsByIndicatorExecutionId(ie.getId(), ie.getPeriod().getId());

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
        //List<CustomDissagregationAssignationToIndicator> customDissagregationAsignations = this.customDissagregationAssignationToIndicatorService.getCustomDissagregationAssignationsByIndicatorExecutionId(ie.getId(), ie.getPeriod().getId());
        List<CustomDissagregation> customDissagregationTypeAssignated = this.customDissagregationAssignationToIndicatorService.getActiveCustomDissagregationsByIndicatorExecutionId(ie.getId(), ie.getPeriod().getId());
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


}
