package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorValueDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.*;

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
            case DIVERSIDAD_EDAD_EDUCACION_PRIMARIA_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForDiversityAgePrimaryEducationAndGender();
            case DIVERSIDAD_EDAD_EDUCACION_TERCIARIA_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForDiversityAgeTerciaryEducationAndGender();
            case TIPO_POBLACION_LUGAR_EDAD_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForPopulationTypeLocationAgeAndGender(cantones);
            case TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForPopulationTypeLocationAgePrimaryEducationAndGender(cantones);
            case TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForPopulationTypeLocationAgeTertiaryEducationAndGender(cantones);
            case LUGAR_DIVERSIDAD_EDAD_EDUCACION_PRIMARIA_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForLocationDiversityAgePrimaryEducationAndGender(cantones);
            case SIN_DESAGREGACION:
                return this.createIndicatorValueDissagregationStandardForNoDissagregation();
            case LUGAR_Y_DIVERSIDAD:
                return this.createIndicatorValueDissagregationStandardForLocationAndDiversity(cantones);
            case LUGAR_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForLocationAndGender(cantones);
            case LUGAR_PAIS_ORIGEN_EDAD_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForLocationCountryOfOriginAgeAndGender(cantones);
            case PAIS_ORIGEN_EDAD_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForCountryOfOriginAgeAndGender();
            case LUGAR_PAIS_ORIGEN_EDAD_EDUCACION_PRIMARIA_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForLocationCountryOfOriginAgePrimaryEducationAndGender(cantones);
            case PAIS_ORIGEN_EDAD_EDUCACION_PRIMARIA_Y_GENERO:
                return this.createIndicatorValueDissagregationStandardForCountryOfOriginAgePrimaryEducationAndGender();
            default: {
                throw new GeneralAppException(" Desagregación no implementada " + dissagregationType, Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForLocationDiversityAgePrimaryEducationAndGender(List<Canton> cantones) {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.LUGAR_DIVERSIDAD_EDAD_EDUCACION_PRIMARIA_Y_GENERO;
        for (Canton canton : cantones) {
            for (DiversityType diversityType : DiversityType.values()) {
                for (GenderType genderType : GenderType.values()) {
                    for (AgePrimaryEducationType ageType : AgePrimaryEducationType.values()) {
                        IndicatorValue iv = new IndicatorValue();
                        iv.setState(State.ACTIVO);
                        iv.setDissagregationType(dt);
                        iv.setGenderType(genderType);
                        iv.setAgePrimaryEducationType(ageType);
                        iv.setDiversityType(diversityType);
                        iv.setLocation(canton);
                        iv.setShowValue(true);
                        r.add(iv);
                    }
                }
            }
        }
        return r;
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

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForLocationAndDiversity(List<Canton> cantones) {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.LUGAR_Y_DIVERSIDAD;
        for (Canton canton : cantones) {
            for (DiversityType diversityType : DiversityType.values()) {
                IndicatorValue iv = new IndicatorValue();
                iv.setState(State.ACTIVO);
                iv.setDissagregationType(dt);
                iv.setLocation(canton);
                iv.setDiversityType(diversityType);
                iv.setShowValue(true);
                r.add(iv);
            }

        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForLocationAndGender(List<Canton> cantones) {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.LUGAR_Y_DIVERSIDAD;
        for (Canton canton : cantones) {
            for (GenderType genderType : GenderType.values()) {
                IndicatorValue iv = new IndicatorValue();
                iv.setState(State.ACTIVO);
                iv.setDissagregationType(dt);
                iv.setLocation(canton);
                iv.setGenderType(genderType);
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

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForDiversityAgePrimaryEducationAndGender() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.DIVERSIDAD_EDAD_EDUCACION_PRIMARIA_Y_GENERO;
        for (DiversityType diversityType : DiversityType.values()) {
            for (GenderType genderType : GenderType.values()) {
                for (AgePrimaryEducationType ageType : AgePrimaryEducationType.values()) {
                    IndicatorValue iv = new IndicatorValue();
                    iv.setState(State.ACTIVO);
                    iv.setDissagregationType(dt);
                    iv.setGenderType(genderType);
                    iv.setAgePrimaryEducationType(ageType);
                    iv.setDiversityType(diversityType);
                    iv.setShowValue(true);
                    r.add(iv);
                }
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForDiversityAgeTerciaryEducationAndGender() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.DIVERSIDAD_EDAD_EDUCACION_TERCIARIA_Y_GENERO;
        for (DiversityType diversityType : DiversityType.values()) {
            for (GenderType genderType : GenderType.values()) {
                for (AgeTertiaryEducationType ageType : AgeTertiaryEducationType.values()) {
                    IndicatorValue iv = new IndicatorValue();
                    iv.setState(State.ACTIVO);
                    iv.setDissagregationType(dt);
                    iv.setGenderType(genderType);
                    iv.setAgeTertiaryEducationType(ageType);
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
        for (Canton canton : cantones) {
            for (PopulationType populationType : PopulationType.values()) {
                for (GenderType genderType : GenderType.values()) {
                    for (AgeType ageType : AgeType.values()) {
                        IndicatorValue iv = new IndicatorValue();
                        iv.setState(State.ACTIVO);
                        iv.setDissagregationType(dt);
                        iv.setLocation(canton);
                        iv.setPopulationType(populationType);
                        iv.setGenderType(genderType);
                        iv.setAgeType(ageType);
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

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForCountryOfOriginAgeAndGender() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.PAIS_ORIGEN_EDAD_Y_GENERO;
        for (CountryOfOrigin countryOfOrigin : CountryOfOrigin.values()) {
            for (GenderType genderType : GenderType.values()) {
                for (AgeType ageType : AgeType.values()) {
                    IndicatorValue iv = new IndicatorValue();
                    iv.setState(State.ACTIVO);
                    iv.setDissagregationType(dt);
                    iv.setGenderType(genderType);
                    iv.setAgeType(ageType);
                    iv.setCountryOfOrigin(countryOfOrigin);
                    iv.setShowValue(true);
                    r.add(iv);
                }
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForCountryOfOriginAgePrimaryEducationAndGender() {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.PAIS_ORIGEN_EDAD_EDUCACION_PRIMARIA_Y_GENERO;
        for (CountryOfOrigin countryOfOrigin : CountryOfOrigin.values()) {
            for (GenderType genderType : GenderType.values()) {
                for (AgePrimaryEducationType agePrimaryEducationType : AgePrimaryEducationType.values()) {
                    IndicatorValue iv = new IndicatorValue();
                    iv.setState(State.ACTIVO);
                    iv.setDissagregationType(dt);
                    iv.setGenderType(genderType);
                    iv.setAgePrimaryEducationType(agePrimaryEducationType);
                    iv.setCountryOfOrigin(countryOfOrigin);
                    iv.setShowValue(true);
                    r.add(iv);
                }
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForLocationCountryOfOriginAgeAndGender(List<Canton> cantones) {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.LUGAR_PAIS_ORIGEN_EDAD_Y_GENERO;
        for (Canton canton : cantones) {
            for (CountryOfOrigin countryOfOrigin : CountryOfOrigin.values()) {
                for (GenderType genderType : GenderType.values()) {
                    for (AgeType ageType : AgeType.values()) {
                        IndicatorValue iv = new IndicatorValue();
                        iv.setState(State.ACTIVO);
                        iv.setDissagregationType(dt);
                        iv.setGenderType(genderType);
                        iv.setAgeType(ageType);
                        iv.setLocation(canton);
                        iv.setCountryOfOrigin(countryOfOrigin);
                        iv.setShowValue(true);
                        r.add(iv);
                    }
                }
            }
        }
        return r;
    }

    private List<IndicatorValue> createIndicatorValueDissagregationStandardForLocationCountryOfOriginAgePrimaryEducationAndGender(List<Canton> cantones) {
        List<IndicatorValue> r = new ArrayList<>();
        DissagregationType dt = DissagregationType.LUGAR_PAIS_ORIGEN_EDAD_EDUCACION_PRIMARIA_Y_GENERO;
        for (Canton canton : cantones) {
            for (CountryOfOrigin countryOfOrigin : CountryOfOrigin.values()) {
                for (GenderType genderType : GenderType.values()) {
                    for (AgePrimaryEducationType agePrimaryEducationType : AgePrimaryEducationType.values()) {
                        IndicatorValue iv = new IndicatorValue();
                        iv.setState(State.ACTIVO);
                        iv.setDissagregationType(dt);
                        iv.setGenderType(genderType);
                        iv.setAgePrimaryEducationType(agePrimaryEducationType);
                        iv.setLocation(canton);
                        iv.setCountryOfOrigin(countryOfOrigin);
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
            if (DissagregationType.getLocationDissagregationTypes().contains(dissagregationType)) {
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
                                    this.createIndicatorValueDissagregationStandardForMonth(dissagregationType, cantones);
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
        if (unvalidatedCantos.size() > 1) {
            String unvalidMonths = unvalidatedCantos.stream().map(indicatorValue -> indicatorValue.getMonthEnum())
                    .distinct().sorted(Comparator.comparingInt(MonthEnum::getOrder))
                    .map(MonthEnum::getLabel).collect(Collectors.joining(", "));
            String unvalidCantonts =unvalidatedCantos.stream().map(IndicatorValue::getLocation).distinct()
                    .map(canton -> canton.getProvincia().getDescription()+"-"+canton.getDescription())
                    .sorted().collect(Collectors.joining(", "));
            String message= "No se puede eliminar los siguientes cantones porque tienen datos reportados : " +
                    unvalidCantonts +" porque tienen datos reportados en los siguientes meses: "+unvalidMonths +" Para eliminar el caanton, primero vaya a estos meses y ponga en 0 (cero) los valores de estos cantos en los meses señalados. ";
            throw new GeneralAppException(message, Response.Status.BAD_REQUEST);
        }
    }

    public List<IndicatorValue> getByIndicatorExecutionId(Long indicatorExecutionId) {
        return this.indicatorValueDao.getByIndicatorExecutionId(indicatorExecutionId);
    }
}
