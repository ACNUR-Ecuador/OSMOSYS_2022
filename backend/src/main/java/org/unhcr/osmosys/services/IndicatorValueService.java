package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorValueDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.*;
import org.unhcr.osmosys.model.standardDissagregations.options.*;
import org.unhcr.osmosys.model.standardDissagregations.periodOptions.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class IndicatorValueService {

    @Inject
    IndicatorValueDao indicatorValueDao;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(IndicatorValueService.class);

    public IndicatorValue getById(Long id) {
        return this.indicatorValueDao.find(id);
    }

    public IndicatorValue saveOrUpdate(IndicatorValue indicatorValue) {
        if (indicatorValue.getId() == null) {
            this.indicatorValueDao.save(indicatorValue);
        } else {
            this.indicatorValueDao.update(indicatorValue);
        }
        return indicatorValue;
    }

    public List<IndicatorValue> createIndicatorValueDissagregationStandardForMonth(
            DissagregationType dissagregationType,
            Map<DissagregationType, List<StandardDissagregationOption>> simpleDissagregationOptionsMap
    ) throws GeneralAppException {


        // NO dissagregation
        if (dissagregationType.equals(DissagregationType.SIN_DESAGREGACION)) {
            return this.createIndicatorValueDissagregationStandardForNoDissagregation();
        } else {
            Integer numberOfDissagregations = dissagregationType.getNumberOfDissagregationTypes();

            List<DissagregationType> simpleDissagregations = dissagregationType.getSimpleDissagregations();
            simpleDissagregations= simpleDissagregations.stream().sorted(Comparator.comparingInt(DissagregationType::getOrder)).collect(Collectors.toList());
            List<List<StandardDissagregationOption>> optionsLists = new ArrayList<>();

            for (DissagregationType simpleDissagregation : simpleDissagregations) {
                List<StandardDissagregationOption> optionsList = simpleDissagregationOptionsMap.get(simpleDissagregation).stream().sorted(Comparator.comparingInt(StandardDissagregationOption::getOrder)).collect(Collectors.toList());;
                optionsLists.add(optionsList);
            }

            switch (numberOfDissagregations) {
                case 1:
                    return this.createIndicatorValueDissagregationStandardForMonth1Dissagregations(optionsLists, simpleDissagregations);
                case 2:
                    return this.createIndicatorValueDissagregationStandardForMonth2Dissagregations(optionsLists, simpleDissagregations);
                case 3:
                    return this.createIndicatorValueDissagregationStandardForMonth3Dissagregations(optionsLists, simpleDissagregations);
                case 4:
                    return this.createIndicatorValueDissagregationStandardForMonth4Dissagregations(optionsLists, simpleDissagregations);
                case 5:
                    return this.createIndicatorValueDissagregationStandardForMonth5Dissagregations(optionsLists, simpleDissagregations);
                case 6:
                    return this.createIndicatorValueDissagregationStandardForMonth6Dissagregations(optionsLists, simpleDissagregations);
                default:
                    throw new GeneralAppException("No implementado para " + numberOfDissagregations + " desagregaciones");

            }
        }

    }

    private void setValueDissagregation(IndicatorValue value, DissagregationType dissagregationType, StandardDissagregationOption standardDissagregationOption) throws GeneralAppException {

        switch (dissagregationType) {
            case EDAD:
                value.setAgeType((AgeDissagregationOption) standardDissagregationOption);
                break;
            case GENERO:
                value.setGenderType((GenderDissagregationOption) standardDissagregationOption);
                break;
            case TIPO_POBLACION:
                value.setPopulationType((PopulationTypeDissagregationOption) standardDissagregationOption);
                break;
            case DIVERSIDAD:
                value.setDiversityType((DiversityDissagregationOption) standardDissagregationOption);
                break;
            case PAIS_ORIGEN:
                value.setCountryOfOrigin((CountryOfOriginDissagregationOption) standardDissagregationOption);
                break;
            case LUGAR:
                value.setLocation((Canton) standardDissagregationOption);
                break;

            default:
                throw new GeneralAppException("No se puede puede generar opciones simples de desagregación para " + dissagregationType + ".");

        }

    }


    /***************************************************************************************************************************************************************************************************/
    private List<IndicatorValue> createIndicatorValueDissagregationStandardForMonth1Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValue> r = new ArrayList<>();
        List<StandardDissagregationOption> listOptions0 = optionsLists.get(0);
        for (StandardDissagregationOption standardDissagregationOption0 : listOptions0) {
            IndicatorValue indicatorValue = new IndicatorValue();
            indicatorValue.setState(State.ACTIVO);
            this.setValueDissagregation(indicatorValue, simpleDissagregations.get(0), standardDissagregationOption0);
            r.add(indicatorValue);
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForMonth2Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValue> r = new ArrayList<>();
        List<StandardDissagregationOption> listOptions0 = optionsLists.get(0);
        List<StandardDissagregationOption> listOptions1 = optionsLists.get(1);
        for (StandardDissagregationOption standardDissagregationOption0 : listOptions0) {
            for (StandardDissagregationOption standardDissagregationOption1 : listOptions1) {
                IndicatorValue indicatorValue = new IndicatorValue();
                indicatorValue.setState(State.ACTIVO);
                this.setValueDissagregation(indicatorValue, simpleDissagregations.get(0), standardDissagregationOption0);
                this.setValueDissagregation(indicatorValue, simpleDissagregations.get(1), standardDissagregationOption1);
                r.add(indicatorValue);
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForMonth3Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValue> r = new ArrayList<>();
        List<StandardDissagregationOption> listOptions0 = optionsLists.get(0);
        List<StandardDissagregationOption> listOptions1 = optionsLists.get(1);
        List<StandardDissagregationOption> listOptions2 = optionsLists.get(2);
        for (StandardDissagregationOption standardDissagregationOption0 : listOptions0) {
            for (StandardDissagregationOption standardDissagregationOption1 : listOptions1) {
                for (StandardDissagregationOption standardDissagregationOption2 : listOptions2) {
                    IndicatorValue indicatorValue = new IndicatorValue();
                    indicatorValue.setState(State.ACTIVO);
                    this.setValueDissagregation(indicatorValue, simpleDissagregations.get(0), standardDissagregationOption0);
                    this.setValueDissagregation(indicatorValue, simpleDissagregations.get(1), standardDissagregationOption1);
                    this.setValueDissagregation(indicatorValue, simpleDissagregations.get(2), standardDissagregationOption2);
                    r.add(indicatorValue);
                }
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForMonth4Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValue> r = new ArrayList<>();
        List<StandardDissagregationOption> listOptions0 = optionsLists.get(0);
        List<StandardDissagregationOption> listOptions1 = optionsLists.get(1);
        List<StandardDissagregationOption> listOptions2 = optionsLists.get(2);
        List<StandardDissagregationOption> listOptions3 = optionsLists.get(3);
        for (StandardDissagregationOption standardDissagregationOption0 : listOptions0) {
            for (StandardDissagregationOption standardDissagregationOption1 : listOptions1) {
                for (StandardDissagregationOption standardDissagregationOption2 : listOptions2) {
                    for (StandardDissagregationOption standardDissagregationOption3 : listOptions3) {
                        IndicatorValue indicatorValue = new IndicatorValue();
                        indicatorValue.setState(State.ACTIVO);
                        this.setValueDissagregation(indicatorValue, simpleDissagregations.get(0), standardDissagregationOption0);
                        this.setValueDissagregation(indicatorValue, simpleDissagregations.get(1), standardDissagregationOption1);
                        this.setValueDissagregation(indicatorValue, simpleDissagregations.get(2), standardDissagregationOption2);
                        this.setValueDissagregation(indicatorValue, simpleDissagregations.get(3), standardDissagregationOption3);
                        r.add(indicatorValue);
                    }
                }
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForMonth5Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValue> r = new ArrayList<>();
        List<StandardDissagregationOption> listOptions0 = optionsLists.get(0);
        List<StandardDissagregationOption> listOptions1 = optionsLists.get(1);
        List<StandardDissagregationOption> listOptions2 = optionsLists.get(2);
        List<StandardDissagregationOption> listOptions3 = optionsLists.get(3);
        List<StandardDissagregationOption> listOptions4 = optionsLists.get(4);
        for (StandardDissagregationOption standardDissagregationOption0 : listOptions0) {
            for (StandardDissagregationOption standardDissagregationOption1 : listOptions1) {
                for (StandardDissagregationOption standardDissagregationOption2 : listOptions2) {
                    for (StandardDissagregationOption standardDissagregationOption3 : listOptions3) {
                        for (StandardDissagregationOption standardDissagregationOption4 : listOptions4) {
                            IndicatorValue indicatorValue = new IndicatorValue();
                            indicatorValue.setState(State.ACTIVO);
                            this.setValueDissagregation(indicatorValue, simpleDissagregations.get(0), standardDissagregationOption0);
                            this.setValueDissagregation(indicatorValue, simpleDissagregations.get(1), standardDissagregationOption1);
                            this.setValueDissagregation(indicatorValue, simpleDissagregations.get(2), standardDissagregationOption2);
                            this.setValueDissagregation(indicatorValue, simpleDissagregations.get(3), standardDissagregationOption3);
                            this.setValueDissagregation(indicatorValue, simpleDissagregations.get(4), standardDissagregationOption4);
                            r.add(indicatorValue);
                        }
                    }
                }
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForMonth6Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValue> r = new ArrayList<>();
        List<StandardDissagregationOption> listOptions0 = optionsLists.get(0);
        List<StandardDissagregationOption> listOptions1 = optionsLists.get(1);
        List<StandardDissagregationOption> listOptions2 = optionsLists.get(2);
        List<StandardDissagregationOption> listOptions3 = optionsLists.get(3);
        List<StandardDissagregationOption> listOptions4 = optionsLists.get(4);
        List<StandardDissagregationOption> listOptions5 = optionsLists.get(5);
        for (StandardDissagregationOption standardDissagregationOption0 : listOptions0) {
            for (StandardDissagregationOption standardDissagregationOption1 : listOptions1) {
                for (StandardDissagregationOption standardDissagregationOption2 : listOptions2) {
                    for (StandardDissagregationOption standardDissagregationOption3 : listOptions3) {
                        for (StandardDissagregationOption standardDissagregationOption4 : listOptions4) {
                            for (StandardDissagregationOption standardDissagregationOption5 : listOptions5) {
                                IndicatorValue indicatorValue = new IndicatorValue();
                                indicatorValue.setState(State.ACTIVO);
                                this.setValueDissagregation(indicatorValue, simpleDissagregations.get(0), standardDissagregationOption0);
                                this.setValueDissagregation(indicatorValue, simpleDissagregations.get(1), standardDissagregationOption1);
                                this.setValueDissagregation(indicatorValue, simpleDissagregations.get(2), standardDissagregationOption2);
                                this.setValueDissagregation(indicatorValue, simpleDissagregations.get(3), standardDissagregationOption3);
                                this.setValueDissagregation(indicatorValue, simpleDissagregations.get(4), standardDissagregationOption4);
                                this.setValueDissagregation(indicatorValue, simpleDissagregations.get(5), standardDissagregationOption5);
                                r.add(indicatorValue);
                            }
                        }
                    }
                }
            }
        }
        return r;
    }

    private List<StandardDissagregationOption> getPeriodStandardDissagregationOptionsFromPeriod(Period period, DissagregationType dissagregationType) throws GeneralAppException {

        switch (dissagregationType) {
            case EDAD:
                return period.getPeriodAgeDissagregationOptions()
                        .stream()
                        .filter(option -> option.getState().equals(State.ACTIVO))
                        .map(PeriodAgeDissagregationOption::getDissagregationOption)
                        .collect(Collectors.toList());
            case GENERO:
                return period.getPeriodGenderDissagregationOptions()
                        .stream()
                        .filter(option -> option.getState().equals(State.ACTIVO))
                        .map(PeriodGenderDissagregationOption::getDissagregationOption)
                        .collect(Collectors.toList());
            case TIPO_POBLACION:
                return period.getPeriodPopulationTypeDissagregationOptions()
                        .stream()
                        .filter(option -> option.getState().equals(State.ACTIVO))
                        .map(PeriodPopulationTypeDissagregationOption::getDissagregationOption)
                        .collect(Collectors.toList());
            case DIVERSIDAD:
                return period.getPeriodDiversityDissagregationOptions()
                        .stream()
                        .filter(option -> option.getState().equals(State.ACTIVO))
                        .map(PeriodDiversityDissagregationOption::getDissagregationOption)
                        .collect(Collectors.toList());
            case PAIS_ORIGEN:
                return period.getPeriodCountryOfOriginDissagregationOptions()
                        .stream()
                        .filter(option -> option.getState().equals(State.ACTIVO))
                        .map(PeriodCountryOfOriginDissagregationOption::getDissagregationOption)
                        .collect(Collectors.toList());
            case LUGAR:
                return null;

            default:
                throw new GeneralAppException("No se puede puede generar opciones simples de desagregación para " + dissagregationType + ".");

        }

    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForNoDissagregation() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.SIN_DESAGREGACION;
        IndicatorValue iv = new IndicatorValue();
        iv.setState(State.ACTIVO);
        iv.setDissagregationType(dt);
        iv.setShowValue(true);
        r.add(iv);
        return r;
    }


    public List<IndicatorValue> getIndicatorValuesByMonthId(Long monthId, State state) {
        return this.indicatorValueDao.getIndicatorValuesByMonthIdAndState(monthId, state);
    }

    public void updateStateByPeriodIdIndicatorIdAndDissagregationType(List<Long> indicatorExecutionIds, DissagregationType dissagregationType, State state) {
        this.indicatorValueDao.updateStateByPeriodIdIndicatorIdAndDissagregationType(indicatorExecutionIds, dissagregationType, state);
    }

    public void updateGeneralIndicatorStateByPeriodIdAndDissagregationType(Long periodId, DissagregationType dissagregationType, State state) {
        this.indicatorValueDao.updateGeneralIndicatorStateByPeriodIdAndDissagregationType(periodId, dissagregationType, state);
    }




    public void updateIndicatorValuesLocationsForIndicatorExecution(
            IndicatorExecution indicatorExecution,
            Set<Canton> locationsToActivateIe,
            Set<Canton> locationsToDissableIe
    ) throws GeneralAppException {
        Set<DissagregationAssignationToIndicatorExecution> dissagregationAssigments = indicatorExecution.getDissagregationsAssignationsToIndicatorExecutions();

        List<IndicatorValue> indicatorValues =
                indicatorExecution.getQuarters().stream()
                        .flatMap(quarter -> quarter.getMonths().stream())
                        .flatMap(month -> month.getIndicatorValues().stream())
                        .collect(Collectors.toList());
        // voy por cada dessagregacion
        List<IndicatorValue> unvalidatedCantos = new ArrayList<>();
        for (DissagregationAssignationToIndicatorExecution dissagregationAssignationToIndicatorExecution : dissagregationAssigments) {

            DissagregationType dissagregationType
                    = dissagregationAssignationToIndicatorExecution.getDissagregationType();
            State dissagregationState
                    = dissagregationAssignationToIndicatorExecution.getState();
            if (dissagregationType.isLocationsDissagregation()) {
                // solo los indicadores de localtion
                List<IndicatorValue> indicatorValuesDissagregation = indicatorValues
                        .stream().filter(indicatorValue ->
                                indicatorValue.getDissagregationType().equals(dissagregationType))
                        .collect(Collectors.toList());
                List<Canton> existingCantons = indicatorValuesDissagregation.stream()
                        .map(IndicatorValue::getLocation)
                        .distinct()
                        .collect(Collectors.toList());
                // creo o activo
                for (Canton cantonToActivate : locationsToActivateIe) {
                    List<IndicatorValue> indicatorValuesToActivateLocation = indicatorValuesDissagregation.stream()
                            .filter(indicatorValue ->
                                    cantonToActivate.getId().equals(indicatorValue.getLocation().getId()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(indicatorValuesToActivateLocation)) {
                        // debo activar
                        indicatorValuesToActivateLocation.forEach(indicatorValue -> indicatorValue.setState(State.ACTIVO));
                    } else {
                        // debo crear
                        List<Month> months = indicatorExecution.getQuarters()
                                .stream().
                                flatMap(quarter -> quarter.getMonths().stream())
                                .collect(Collectors.toList());
                        List<Canton> cantones = new ArrayList<>();
                        cantones.add(cantonToActivate);
                        for (Month month : months) {
                            List<IndicatorValue> newIndicatorValues =
                                    // todo se agrego null en 2024
                                    this.createIndicatorValueDissagregationStandardForMonth(dissagregationType, null);
                            newIndicatorValues.forEach(indicatorValue -> {
                                        month.addIndicatorValue(indicatorValue);
                                        indicatorValuesDissagregation.add(indicatorValue);
                                    }
                            );

                        }
                    }

                }
                // desactivo

                for (Canton cantonToDissable : locationsToDissableIe) {
                    indicatorValuesDissagregation.stream()
                            .filter(indicatorValue ->
                                    cantonToDissable.getId().equals(indicatorValue.getLocation().getId()))
                            .forEach(indicatorValue -> {
                                        if (indicatorValue.getValue() != null && indicatorValue.getValue().compareTo(BigDecimal.ZERO) > 0) {
                                            LOGGER.info(indicatorValue.getValue().compareTo(BigDecimal.ZERO));
                                            unvalidatedCantos.add(indicatorValue);
                                        }
                                        indicatorValue.setState(State.INACTIVO);

                                    }
                            );

                }
                // activo o desactivo segun estado de desagregacion
                /*indicatorValuesDissagregation
                        .forEach(indicatorValue -> indicatorValue.setState(dissagregationState));*/

                if (dissagregationState.equals(State.INACTIVO)) {
                    indicatorValuesDissagregation
                            .forEach(indicatorValue -> indicatorValue.setState(dissagregationState));
                }
            }

        }
        if (!unvalidatedCantos.isEmpty()) {
            String unvalidMonths = unvalidatedCantos.stream().map(IndicatorValue::getMonthEnum)
                    .distinct().sorted(Comparator.comparingInt(MonthEnum::getOrder))
                    .map(MonthEnum::getLabel).collect(Collectors.joining(", "));
            String unvalidCantonts = unvalidatedCantos.stream().map(IndicatorValue::getLocation).distinct()
                    .map(canton -> canton.getProvincia().getDescription() + "-" + canton.getName())
                    .sorted().collect(Collectors.joining(", "));
            String message = "No se puede eliminar los siguientes cantones porque tienen datos reportados : " +
                    unvalidCantonts + " en los siguientes meses: " + unvalidMonths + " Para eliminar el cantón, primero vaya a estos meses y ponga en 0 (cero) los valores de estos cantones. ";
            throw new GeneralAppException(message, Response.Status.BAD_REQUEST);
        }
    }

    public List<IndicatorValue> getByIndicatorExecutionId(Long indicatorExecutionId) {
        return this.indicatorValueDao.getByIndicatorExecutionId(indicatorExecutionId);
    }

    /////////////////////
}
