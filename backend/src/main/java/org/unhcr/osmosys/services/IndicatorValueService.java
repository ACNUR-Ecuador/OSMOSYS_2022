package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorValueDao;
import org.unhcr.osmosys.model.Canton;
import org.unhcr.osmosys.model.IndicatorExecution;
import org.unhcr.osmosys.model.IndicatorValue;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.standardDissagregations.options.*;
import org.unhcr.osmosys.model.standardDissagregations.periodOptions.*;
import org.unhcr.osmosys.services.standardDissagregations.StandardDissagregationOptionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
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
        List<IndicatorValueOptionsDTO> valuesOptionsDTOs;
        if (dissagregationType.equals(DissagregationType.SIN_DESAGREGACION)) {
            List<IndicatorValue> indicatorValues = this.createIndicatorValueDissagregationStandardForNoDissagregation();
            indicatorValues.forEach(value -> value.setDissagregationType(dissagregationType));
            return indicatorValues;
        } else {
            valuesOptionsDTOs = getIndicatorValueOptionsDTOS(dissagregationType, simpleDissagregationOptionsMap);
            List<IndicatorValue> indicatorValues= new ArrayList<>();
            for (IndicatorValueOptionsDTO valuesOptionsDTO : valuesOptionsDTOs) {
                IndicatorValue indicatorValue = new IndicatorValue();
                indicatorValue.setState(State.ACTIVO);
                indicatorValue.setBytDTO(valuesOptionsDTO);
                indicatorValue.setDissagregationType(dissagregationType);
                indicatorValues.add(indicatorValue);
            }
            return indicatorValues;

        }



    }

    public List<IndicatorValueOptionsDTO> getIndicatorValueOptionsDTOS(DissagregationType dissagregationType, Map<DissagregationType, List<StandardDissagregationOption>> simpleDissagregationOptionsMap) throws GeneralAppException {
        List<IndicatorValueOptionsDTO> valuesOptionsDTOs;
        //# de desagregaciones
        Integer numberOfDissagregations = dissagregationType.getNumberOfDissagregationTypes();

        // desagregaciones simple o base ordenadas
        List<DissagregationType> simpleDissagregations = dissagregationType.getSimpleDissagregations();
        simpleDissagregations = simpleDissagregations.stream().sorted(Comparator.comparingInt(DissagregationType::getOrder)).collect(Collectors.toList());

        // opciones ordenadas
        List<List<StandardDissagregationOption>> optionsLists = new ArrayList<>();
        for (DissagregationType simpleDissagregation : simpleDissagregations) {
            List<StandardDissagregationOption> optionsList = simpleDissagregationOptionsMap
                    .get(simpleDissagregation)
                    .stream()
                    .sorted(
                            Comparator.comparingInt(StandardDissagregationOption::getOrder)).collect(Collectors.toList());

            optionsLists.add(optionsList);
        }


        switch (numberOfDissagregations) {
            case 0:
                throw new GeneralAppException("Error programación: No implementado para " + numberOfDissagregations + " desagregaciones");

            case 1:
                valuesOptionsDTOs = this.createIndicatorValueDissagregationStandardForMonth1Dissagregations(optionsLists, simpleDissagregations);
                break;
            case 2:
                valuesOptionsDTOs = this.createIndicatorValueDissagregationStandardForMonth2Dissagregations(optionsLists, simpleDissagregations);
                break;
            case 3:
                valuesOptionsDTOs = this.createIndicatorValueDissagregationStandardForMonth3Dissagregations(optionsLists, simpleDissagregations);
                break;
            case 4:
                valuesOptionsDTOs = this.createIndicatorValueDissagregationStandardForMonth4Dissagregations(optionsLists, simpleDissagregations);
                break;
            case 5:
                valuesOptionsDTOs = this.createIndicatorValueDissagregationStandardForMonth5Dissagregations(optionsLists, simpleDissagregations);
                break;
            case 6:
                valuesOptionsDTOs = this.createIndicatorValueDissagregationStandardForMonth6Dissagregations(optionsLists, simpleDissagregations);
                break;
            default:
                throw new GeneralAppException("No implementado para " + numberOfDissagregations + " desagregaciones");

        }
        return valuesOptionsDTOs;
    }

    private void setIndicatorValueOptionDTO(IndicatorValueOptionsDTO value, DissagregationType dissagregationType, StandardDissagregationOption standardDissagregationOption) throws GeneralAppException {

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

    public List<IndicatorValueOptionsDTO> createIndicatorValueDissagregationStandardForMonthV2(
            List<List<StandardDissagregationOption>> optionsLists,
            List<DissagregationType> simpleDissagregations) throws GeneralAppException {List<IndicatorValueOptionsDTO> r = new ArrayList<>();

        // if any has no options, return empty arrays
        for (List<StandardDissagregationOption> optionsList : optionsLists) {
            if(CollectionUtils.isEmpty(optionsList)) return new ArrayList<>();
        }
        // Get the number of dissagregations
        int numDissagregations = optionsLists.size();

        // Create an array to hold the current option index for each dissagregation
        int[] currentIndexes = new int[numDissagregations];

        // Initialize the current indexes
        Arrays.fill(currentIndexes, 0);

        // Iterate through all combinations of dissagregation options
        while (true) {
            IndicatorValueOptionsDTO indicatorValueOptionDTO = new IndicatorValueOptionsDTO();

            // Set dissagregation options for the current combination
            for (int i = 0; i < numDissagregations; i++) {
                this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(i), optionsLists.get(i).get(currentIndexes[i]));
            }

            // Add the current combination to the result list
            r.add(indicatorValueOptionDTO);

            // Move to the next combination
            int index = numDissagregations - 1;
            while (index >= 0 && currentIndexes[index] == optionsLists.get(index).size() - 1) {
                currentIndexes[index] = 0;
                index--;
            }

            if (index < 0) {
                break; // All combinations have been processed
            }

            // Move to the next option for the current dissagregation type
            currentIndexes[index]++;
        }
        r.forEach(LOGGER::info);

        return r;
    }


    private List<IndicatorValueOptionsDTO> createIndicatorValueDissagregationStandardForMonth1Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValueOptionsDTO> r = new ArrayList<>();
        List<StandardDissagregationOption> listOptions0 = optionsLists.get(0);
        for (StandardDissagregationOption standardDissagregationOption0 : listOptions0) {
            IndicatorValueOptionsDTO indicatorValueOptionDTO= new IndicatorValueOptionsDTO();
            this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(0), standardDissagregationOption0);
            r.add(indicatorValueOptionDTO);
        }
        return r;
    }

    private List<IndicatorValueOptionsDTO> createIndicatorValueDissagregationStandardForMonth2Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValueOptionsDTO> r = new ArrayList<>();
        List<StandardDissagregationOption> listOptions0 = optionsLists.get(0);
        List<StandardDissagregationOption> listOptions1 = optionsLists.get(1);
        for (StandardDissagregationOption standardDissagregationOption0 : listOptions0) {
            for (StandardDissagregationOption standardDissagregationOption1 : listOptions1) {
                IndicatorValueOptionsDTO indicatorValueOptionDTO= new IndicatorValueOptionsDTO();
                this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(0), standardDissagregationOption0);
                this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(1), standardDissagregationOption1);
                r.add(indicatorValueOptionDTO);
            }
        }
        return r;
    }

    private List<IndicatorValueOptionsDTO> createIndicatorValueDissagregationStandardForMonth3Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValueOptionsDTO> r = new ArrayList<>();
        List<StandardDissagregationOption> listOptions0 = optionsLists.get(0);
        List<StandardDissagregationOption> listOptions1 = optionsLists.get(1);
        List<StandardDissagregationOption> listOptions2 = optionsLists.get(2);
        for (StandardDissagregationOption standardDissagregationOption0 : listOptions0) {
            for (StandardDissagregationOption standardDissagregationOption1 : listOptions1) {
                for (StandardDissagregationOption standardDissagregationOption2 : listOptions2) {
                    IndicatorValueOptionsDTO indicatorValueOptionDTO= new IndicatorValueOptionsDTO();
                    this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(0), standardDissagregationOption0);
                    this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(1), standardDissagregationOption1);
                    this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(2), standardDissagregationOption2);
                    r.add(indicatorValueOptionDTO);
                }
            }
        }
        return r;
    }

    private List<IndicatorValueOptionsDTO> createIndicatorValueDissagregationStandardForMonth4Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValueOptionsDTO> r = new ArrayList<>();
        List<StandardDissagregationOption> listOptions0 = optionsLists.get(0);
        List<StandardDissagregationOption> listOptions1 = optionsLists.get(1);
        List<StandardDissagregationOption> listOptions2 = optionsLists.get(2);
        List<StandardDissagregationOption> listOptions3 = optionsLists.get(3);
        for (StandardDissagregationOption standardDissagregationOption0 : listOptions0) {
            for (StandardDissagregationOption standardDissagregationOption1 : listOptions1) {
                for (StandardDissagregationOption standardDissagregationOption2 : listOptions2) {
                    for (StandardDissagregationOption standardDissagregationOption3 : listOptions3) {
                        IndicatorValueOptionsDTO indicatorValueOptionDTO= new IndicatorValueOptionsDTO();
                        this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(0), standardDissagregationOption0);
                        this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(1), standardDissagregationOption1);
                        this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(2), standardDissagregationOption2);
                        this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(3), standardDissagregationOption3);
                        r.add(indicatorValueOptionDTO);
                    }
                }
            }
        }
        return r;
    }

    private List<IndicatorValueOptionsDTO> createIndicatorValueDissagregationStandardForMonth5Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValueOptionsDTO> r = new ArrayList<>();
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
                            IndicatorValueOptionsDTO indicatorValueOptionDTO= new IndicatorValueOptionsDTO();
                            this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(0), standardDissagregationOption0);
                            this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(1), standardDissagregationOption1);
                            this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(2), standardDissagregationOption2);
                            this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(3), standardDissagregationOption3);
                            this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(4), standardDissagregationOption4);
                            r.add(indicatorValueOptionDTO);
                        }
                    }
                }
            }
        }
        return r;
    }

    private List<IndicatorValueOptionsDTO> createIndicatorValueDissagregationStandardForMonth6Dissagregations(List<List<StandardDissagregationOption>> optionsLists, List<DissagregationType> simpleDissagregations) throws GeneralAppException {
        List<IndicatorValueOptionsDTO> r = new ArrayList<>();
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
                                IndicatorValueOptionsDTO indicatorValueOptionDTO= new IndicatorValueOptionsDTO();
                                this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(0), standardDissagregationOption0);
                                this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(1), standardDissagregationOption1);
                                this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(2), standardDissagregationOption2);
                                this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(3), standardDissagregationOption3);
                                this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(4), standardDissagregationOption4);
                                this.setIndicatorValueOptionDTO(indicatorValueOptionDTO, simpleDissagregations.get(5), standardDissagregationOption5);
                                r.add(indicatorValueOptionDTO);
                            }
                        }
                    }
                }
            }
        }
        return r;
    }

    public List<IndicatorValue> getIndicatorValuesByMonthId(Long monthId) {
        return this.indicatorValueDao.getIndicatorValuesByMonthId(monthId);
    }


    public Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> getDissagregationMapIndicatorValuesByMonthId(List<IndicatorValue> indicatorValues) {
        // extraigo todos los tipos de desagregación
        List<DissagregationType> dissagregationTypes = indicatorValues.stream().map(IndicatorValue::getDissagregationType).distinct().collect(Collectors.toList());
        // esta estructura será el resultado
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationMap = new HashMap<>();
        for (DissagregationType dissagregationType : dissagregationTypes) {
            Map<DissagregationType, List<StandardDissagregationOption>> simpleDissagregationMap = getSimpleDissagregationMapByDissagregationType(indicatorValues, dissagregationType);
            dissagregationMap.put(dissagregationType, simpleDissagregationMap);
        }

        return dissagregationMap;


    }

    private Map<DissagregationType, List<StandardDissagregationOption>> getSimpleDissagregationMapByDissagregationType(List<IndicatorValue> indicatorValues, DissagregationType dissagregationType) {
        List<DissagregationType> simpleDissagregations = dissagregationType.getSimpleDissagregations();
        List<IndicatorValue> dissagregationValues = indicatorValues.stream()
                .filter(value -> value.getDissagregationType().equals(dissagregationType)).collect(Collectors.toList());
        Map<DissagregationType, List<StandardDissagregationOption>> simpleDissagregationMap = new HashMap<>();
        for (DissagregationType simpleDissagregation : simpleDissagregations) {
            List<StandardDissagregationOption> options = dissagregationValues.stream()
                    .map(value -> this.getValueDissagregationFromIndicatorValue(value, simpleDissagregation))
                    .distinct()
                    .collect(Collectors.toList());
            simpleDissagregationMap.put(simpleDissagregation, options);
        }
        return simpleDissagregationMap;
    }

    private StandardDissagregationOption getValueDissagregationFromIndicatorValue(IndicatorValue value, DissagregationType dissagregationType) {

        switch (dissagregationType) {
            case EDAD:
                return value.getAgeType();
            case GENERO:
                return value.getGenderType();
            case TIPO_POBLACION:
                return value.getPopulationType();
            case DIVERSIDAD:
                return value.getDiversityType();
            case PAIS_ORIGEN:
                return value.getCountryOfOrigin();
            case LUGAR:
                return value.getLocation();
        }

        return null;
    }


    /***************************************************************************************************************************************************************************************************/

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
        r.add(iv);
        return r;
    }


    public List<IndicatorValue> getIndicatorValuesByMonthIdAndState(Long monthId, State state) {
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
        /*Set<DissagregationAssignationToIndicatorExecution> dissagregationAssigments = indicatorExecution.getDissagregationsAssignationsToIndicatorExecutions();

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
                *//*indicatorValuesDissagregation
                        .forEach(indicatorValue -> indicatorValue.setState(dissagregationState));*//*

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
        */
    }

    public List<IndicatorValue> getByIndicatorExecutionId(Long indicatorExecutionId) {
        return this.indicatorValueDao.getByIndicatorExecutionId(indicatorExecutionId);
    }


    @Inject
    PeriodService periodService;
    @Inject
    StandardDissagregationOptionService standardDissagregationOptionService;
    public void testerDTOS() throws GeneralAppException {
        List<List<StandardDissagregationOption>> optionsLists = new ArrayList<>();
        List<DissagregationType> simpleDissagregations = new ArrayList<>();
        // lugar
        List<Long> cantonsIds = new ArrayList<>();
        cantonsIds.add(110L);
        cantonsIds.add(111L);
        List<Canton> cantons = this.standardDissagregationOptionService.getCantonByIds(cantonsIds);
        //simpleDissagregations.add(DissagregationType.LUGAR);
        //optionsLists.add(new ArrayList<>(cantons));
        //optionsLists.add(new ArrayList<>());

        Period period = this.periodService.getWithAllDataById(3L);
        // age
        List<AgeDissagregationOption> ages = period.getPeriodAgeDissagregationOptions()
                .stream().filter(option -> option.getState().equals(State.ACTIVO) && option.getPeriod().getId().equals(period.getId()))
                .map(PeriodAgeDissagregationOption::getDissagregationOption)
                .collect(Collectors.toList());
        //simpleDissagregations.add(DissagregationType.EDAD);
        //optionsLists.add(new ArrayList<>(ages));

        // gender
        List<GenderDissagregationOption> genders = period.getPeriodGenderDissagregationOptions()
                .stream().filter(option -> option.getState().equals(State.ACTIVO) && option.getPeriod().getId().equals(period.getId()))
                .map(PeriodGenderDissagregationOption::getDissagregationOption)
                .collect(Collectors.toList());
        //simpleDissagregations.add(DissagregationType.GENERO);
        //optionsLists.add(new ArrayList<>(genders));


        // diversity
        List<DiversityDissagregationOption> diversities = period.getPeriodDiversityDissagregationOptions()
                .stream().filter(option -> option.getState().equals(State.ACTIVO) && option.getPeriod().getId().equals(period.getId()))
                .map(PeriodDiversityDissagregationOption::getDissagregationOption)
                .collect(Collectors.toList());
        //simpleDissagregations.add(DissagregationType.DIVERSIDAD);
        //optionsLists.add(new ArrayList<>(diversities));


        // tipo de poblacion
        List<PopulationTypeDissagregationOption> populations = period.getPeriodPopulationTypeDissagregationOptions()
                .stream().filter(option -> option.getState().equals(State.ACTIVO) && option.getPeriod().getId().equals(period.getId()))
                .map(PeriodPopulationTypeDissagregationOption::getDissagregationOption)
                .collect(Collectors.toList());
        simpleDissagregations.add(DissagregationType.TIPO_POBLACION);
        optionsLists.add(new ArrayList<>(populations));

        // country of origin
        List<CountryOfOriginDissagregationOption> countries = period.getPeriodCountryOfOriginDissagregationOptions()
                .stream().filter(option -> option.getState().equals(State.ACTIVO) && option.getPeriod().getId().equals(period.getId()))
                .map(PeriodCountryOfOriginDissagregationOption::getDissagregationOption)
                .collect(Collectors.toList());
        simpleDissagregations.add(DissagregationType.PAIS_ORIGEN);
        optionsLists.add(new ArrayList<>(countries));


        List<IndicatorValueOptionsDTO> valuesDtos = this.createIndicatorValueDissagregationStandardForMonthV2(optionsLists, simpleDissagregations);

        valuesDtos.forEach(LOGGER::info);
    }

    /////////////////////
}
