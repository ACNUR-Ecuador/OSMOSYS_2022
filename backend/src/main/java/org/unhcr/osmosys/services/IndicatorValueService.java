package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorValueDao;
import org.unhcr.osmosys.model.Canton;
import org.unhcr.osmosys.model.IndicatorValue;
import org.unhcr.osmosys.model.enums.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

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
            List<Canton> cantones
    ) throws GeneralAppException {

        switch (dissagregationType) {
            case EDAD:
                return this.createIndicatorValueDissagregationStandardAge();
            case EDAD_EDUCACION_PRIMARIA:
                return this.createIndicatorValueDissagregationStandardAgePrimaryEducation();
            case EDAD_EDUCACION_TERCIARIA:
                return this.createIndicatorValueDissagregationStandardAgeTertiaryEducation();

            case DIVERSIDAD:
                return this.createIndicatorValueDissagregationStandardForMonthDiversity();

            case GENERO:
                return this.createIndicatorValueDissagregationStandardForMonthGender();

            case LUGAR:
                return this.createIndicatorValueDissagregationStandardForMonthLocation(cantones);
            case PAIS_ORIGEN:
                return this.createIndicatorValueDissagregationStandardForMonthCountryOfOrigin();
            case TIPO_POBLACION:
                return this.createIndicatorValueDissagregationStandardForMonthPopulationType();
            case GENERO_Y_EDAD:
                return this.createIndicatorValueDissagregationStandardForGenderAndAge();
            case GENERO_Y_DIVERSIDAD:
                return this.createIndicatorValueDissagregationStandardForGenderAndDiversity();
            case TIPO_POBLACION_Y_DIVERSIDAD:
                return this.createIndicatorValueDissagregationStandardForPopulationTypeAndDiversity();
            case TIPO_POBLACION_Y_EDAD:
                return this.createIndicatorValueDissagregationStandardForPopulationTypeAndAge();
            case TIPO_POBLACION_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForPopulationTypeAndGender();
            case TIPO_POBLACION_Y_PAIS_ORIGEN:
                return this.createIndicatorValueDissagregationStandardForPopulationTypeAndCountryOfOrigin();
            case TIPO_POBLACION_Y_LUGAR:
                return this.createIndicatorValueDissagregationStandardForPopulationTypeAndLocation(cantones);
            case LUGAR_EDAD_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForLocationAgeAndGender(cantones);
            case DIVERSIDAD_EDAD_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForDiversityAgeAndGender();
            case TIPO_POBLACION_LUGAR_EDAD_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForPopulationTypeLocationAgeAndGender(cantones);
            case TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForPopulationTypeLocationAgePrimaryEducationAndGender(cantones);
            case TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForPopulationTypeLocationAgeTertiaryEducationAndGender(cantones);
            case SIN_DESAGREGACION:
                return this.createIndicatorValueDissagregationStandardForNoDissagregation();
            default: {
                throw new GeneralAppException(" Desagregación no implementada " + dissagregationType, Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardAge() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.EDAD;
        for (AgeType ageType : AgeType.values()) {
            IndicatorValue iv = new IndicatorValue();
            iv.setState(State.ACTIVO);
            iv.setDissagregationType(dt);
            iv.setAgeType(ageType);
            iv.setShowValue(true);
            r.add(iv);
        }
        return r;
    }


    private List<IndicatorValue> createIndicatorValueDissagregationStandardAgePrimaryEducation() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.EDAD;
        for (AgePrimaryEducationType agePrimaryEducationType : AgePrimaryEducationType.values()) {
            IndicatorValue iv = new IndicatorValue();
            iv.setState(State.ACTIVO);
            iv.setDissagregationType(dt);
            iv.setAgePrimaryEducationType(agePrimaryEducationType);
            iv.setShowValue(true);
            r.add(iv);
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardAgeTertiaryEducation() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.EDAD;
        for (AgeTertiaryEducationType ageageTertiaryEducationType : AgeTertiaryEducationType.values()) {
            IndicatorValue iv = new IndicatorValue();
            iv.setState(State.ACTIVO);
            iv.setDissagregationType(dt);
            iv.setAgeTertiaryEducationType(ageageTertiaryEducationType);
            iv.setShowValue(true);
            r.add(iv);
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForMonthDiversity() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.DIVERSIDAD;
        for (DiversityType diversityType : DiversityType.values()) {
            IndicatorValue iv = new IndicatorValue();
            iv.setState(State.ACTIVO);
            iv.setDissagregationType(dt);
            iv.setDiversityType(diversityType);
            iv.setShowValue(true);
            r.add(iv);
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForMonthGender() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.GENERO;
        for (GenderType genderType : GenderType.values()) {
            IndicatorValue iv = new IndicatorValue();
            iv.setState(State.ACTIVO);
            iv.setDissagregationType(dt);
            iv.setGenderType(genderType);
            iv.setShowValue(true);
            r.add(iv);
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForMonthCountryOfOrigin() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.PAIS_ORIGEN;
        for (CountryOfOrigin countryOfOrigin : CountryOfOrigin.values()) {
            IndicatorValue iv = new IndicatorValue();
            iv.setState(State.ACTIVO);
            iv.setDissagregationType(dt);
            iv.setCountryOfOrigin(countryOfOrigin);
            iv.setShowValue(true);
            r.add(iv);
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForMonthPopulationType() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.TIPO_POBLACION;
        for (PopulationType populationType : PopulationType.values()) {
            IndicatorValue iv = new IndicatorValue();
            iv.setState(State.ACTIVO);
            iv.setDissagregationType(dt);
            iv.setPopulationType(populationType);
            iv.setShowValue(true);
            r.add(iv);
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForMonthLocation(List<Canton> cantones) {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.LUGAR;
        for (Canton canton : cantones) {
            IndicatorValue iv = new IndicatorValue();
            iv.setState(State.ACTIVO);
            iv.setDissagregationType(dt);
            iv.setLocation(canton);
            iv.setShowValue(true);
            r.add(iv);
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForGenderAndAge() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.GENERO_Y_EDAD;
        for (GenderType genderType : GenderType.values()) {
            for (AgeType ageType : AgeType.values()) {
                IndicatorValue iv = new IndicatorValue();
                iv.setState(State.ACTIVO);
                iv.setDissagregationType(dt);
                iv.setGenderType(genderType);
                iv.setAgeType(ageType);
                iv.setShowValue(true);
                r.add(iv);
            }

        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForGenderAndDiversity() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.GENERO_Y_DIVERSIDAD;
        for (GenderType genderType : GenderType.values()) {
            for (DiversityType diversityType : DiversityType.values()) {
                IndicatorValue iv = new IndicatorValue();
                iv.setState(State.ACTIVO);
                iv.setDissagregationType(dt);
                iv.setGenderType(genderType);
                iv.setDiversityType(diversityType);
                iv.setShowValue(true);
                r.add(iv);
            }

        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForPopulationTypeAndDiversity() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.TIPO_POBLACION_Y_DIVERSIDAD;
        for (PopulationType populationType : PopulationType.values()) {
            for (DiversityType diversityType : DiversityType.values()) {
                IndicatorValue iv = new IndicatorValue();
                iv.setState(State.ACTIVO);
                iv.setDissagregationType(dt);
                iv.setPopulationType(populationType);
                iv.setDiversityType(diversityType);
                iv.setShowValue(true);
                r.add(iv);
            }

        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForPopulationTypeAndCountryOfOrigin() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.TIPO_POBLACION_Y_PAIS_ORIGEN;
        for (PopulationType populationType : PopulationType.values()) {
            for (CountryOfOrigin countryOfOrigin : CountryOfOrigin.values()) {
                IndicatorValue iv = new IndicatorValue();
                iv.setState(State.ACTIVO);
                iv.setDissagregationType(dt);
                iv.setPopulationType(populationType);
                iv.setCountryOfOrigin(countryOfOrigin);
                iv.setShowValue(true);
                r.add(iv);
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForPopulationTypeAndLocation(List<Canton> cantones) {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.TIPO_POBLACION_Y_LUGAR;
        for (PopulationType populationType : PopulationType.values()) {
            for (Canton canton : cantones) {
                IndicatorValue iv = new IndicatorValue();
                iv.setState(State.ACTIVO);
                iv.setDissagregationType(dt);
                iv.setPopulationType(populationType);
                iv.setLocation(canton);
                iv.setShowValue(true);
                r.add(iv);
            }
        }
        return r;
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

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForPopulationTypeAndAge() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.TIPO_POBLACION_Y_EDAD;
        for (PopulationType populationType : PopulationType.values()) {
            for (AgeType ageType : AgeType.values()) {
                IndicatorValue iv = new IndicatorValue();
                iv.setState(State.ACTIVO);
                iv.setDissagregationType(dt);
                iv.setPopulationType(populationType);
                iv.setAgeType(ageType);
                iv.setShowValue(true);
                r.add(iv);
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForPopulationTypeAndGender() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.TIPO_POBLACION_Y_GENERO;
        for (PopulationType populationType : PopulationType.values()) {
            for (GenderType genderType : GenderType.values()) {
                IndicatorValue iv = new IndicatorValue();
                iv.setState(State.ACTIVO);
                iv.setDissagregationType(dt);
                iv.setPopulationType(populationType);
                iv.setGenderType(genderType);
                iv.setShowValue(true);
                r.add(iv);
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForLocationAgeAndGender(List<Canton> cantones) {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.LUGAR_EDAD_Y_GENERO;
        for (Canton canton : cantones) {
            for (GenderType genderType : GenderType.values()) {
                for (AgeType ageType : AgeType.values()) {
                    IndicatorValue iv = new IndicatorValue();
                    iv.setState(State.ACTIVO);
                    iv.setDissagregationType(dt);
                    iv.setGenderType(genderType);
                    iv.setAgeType(ageType);
                    iv.setLocation(canton);
                    iv.setShowValue(true);
                    r.add(iv);
                }
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForDiversityAgeAndGender() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.DIVERSIDAD_EDAD_Y_GENERO;
        for (DiversityType diversityType : DiversityType.values()) {
            for (GenderType genderType : GenderType.values()) {
                for (AgeType ageType : AgeType.values()) {
                    IndicatorValue iv = new IndicatorValue();
                    iv.setState(State.ACTIVO);
                    iv.setDissagregationType(dt);
                    iv.setGenderType(genderType);
                    iv.setAgeType(ageType);
                    iv.setDiversityType(diversityType);
                    iv.setShowValue(true);
                    r.add(iv);
                }
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForPopulationTypeLocationAgeAndGender(List<Canton> cantones) {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.TIPO_POBLACION_LUGAR_EDAD_Y_GENERO;
        for (PopulationType populationType : PopulationType.values()) {
            for (Canton canton : cantones) {
                for (GenderType genderType : GenderType.values()) {
                    for (AgeType ageType : AgeType.values()) {
                        IndicatorValue iv = new IndicatorValue();
                        iv.setState(State.ACTIVO);
                        iv.setDissagregationType(dt);
                        iv.setGenderType(genderType);
                        iv.setAgeType(ageType);
                        iv.setLocation(canton);
                        iv.setPopulationType(populationType);
                        iv.setShowValue(true);
                        r.add(iv);
                    }
                }
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForPopulationTypeLocationAgePrimaryEducationAndGender(List<Canton> cantones) {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO;
        for (PopulationType populationType : PopulationType.values()) {
            for (Canton canton : cantones) {
                for (GenderType genderType : GenderType.values()) {
                    for (AgePrimaryEducationType ageType : AgePrimaryEducationType.values()) {
                        IndicatorValue iv = new IndicatorValue();
                        iv.setState(State.ACTIVO);
                        iv.setDissagregationType(dt);
                        iv.setGenderType(genderType);
                        iv.setAgePrimaryEducationType(ageType);
                        iv.setLocation(canton);
                        iv.setPopulationType(populationType);
                        iv.setShowValue(true);
                        r.add(iv);
                    }
                }
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForPopulationTypeLocationAgeTertiaryEducationAndGender(List<Canton> cantones) {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO;
        for (PopulationType populationType : PopulationType.values()) {
            for (Canton canton : cantones) {
                for (GenderType genderType : GenderType.values()) {
                    for (AgeTertiaryEducationType ageType : AgeTertiaryEducationType.values()) {
                        IndicatorValue iv = new IndicatorValue();
                        iv.setState(State.ACTIVO);
                        iv.setDissagregationType(dt);
                        iv.setGenderType(genderType);
                        iv.setAgeTertiaryEducationType(ageType);
                        iv.setLocation(canton);
                        iv.setPopulationType(populationType);
                        iv.setShowValue(true);
                        r.add(iv);
                    }
                }
            }
        }
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
}
