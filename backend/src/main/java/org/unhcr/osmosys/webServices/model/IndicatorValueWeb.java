package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.*;

import java.io.Serializable;
import java.math.BigDecimal;


public class IndicatorValueWeb implements Serializable {


    private Long id;
    private State state;
    private MonthEnum monthEnum;
    private DissagregationType dissagregationType;
    private PopulationType populationType;
    private CountryOfOrigin countryOfOrigin;
    private GenderType genderType;
    private AgeType ageType;
    private AgePrimaryEducationType agePrimaryEducationType;
    private AgeTertiaryEducationType ageTertiaryEducationType;
    private DiversityType diversityType;
    private CantonWeb location;
    private Boolean showValue;
    private BigDecimal value;
    private BigDecimal denominatorValue;
    private BigDecimal numeratorValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public MonthEnum getMonthEnum() {
        return monthEnum;
    }

    public void setMonthEnum(MonthEnum monthEnum) {
        this.monthEnum = monthEnum;
    }

    public DissagregationType getDissagregationType() {
        return dissagregationType;
    }

    public void setDissagregationType(DissagregationType dissagregationType) {
        this.dissagregationType = dissagregationType;
    }

    public PopulationType getPopulationType() {
        return populationType;
    }

    public void setPopulationType(PopulationType populationType) {
        this.populationType = populationType;
    }

    public CountryOfOrigin getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(CountryOfOrigin countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public GenderType getGenderType() {
        return genderType;
    }

    public void setGenderType(GenderType genderType) {
        this.genderType = genderType;
    }

    public AgeType getAgeType() {
        return ageType;
    }

    public void setAgeType(AgeType ageType) {
        this.ageType = ageType;
    }

    public DiversityType getDiversityType() {
        return diversityType;
    }

    public void setDiversityType(DiversityType diversityType) {
        this.diversityType = diversityType;
    }

    public CantonWeb getLocation() {
        return location;
    }

    public void setLocation(CantonWeb location) {
        this.location = location;
    }

    public Boolean getShowValue() {
        return showValue;
    }

    public void setShowValue(Boolean showValue) {
        this.showValue = showValue;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getDenominatorValue() {
        return denominatorValue;
    }

    public void setDenominatorValue(BigDecimal denominatorValue) {
        this.denominatorValue = denominatorValue;
    }

    public BigDecimal getNumeratorValue() {
        return numeratorValue;
    }

    public void setNumeratorValue(BigDecimal numeratorValue) {
        this.numeratorValue = numeratorValue;
    }

    public AgePrimaryEducationType getAgePrimaryEducationType() {
        return agePrimaryEducationType;
    }

    public void setAgePrimaryEducationType(AgePrimaryEducationType agePrimaryEducationType) {
        this.agePrimaryEducationType = agePrimaryEducationType;
    }

    public AgeTertiaryEducationType getAgeTertiaryEducationType() {
        return ageTertiaryEducationType;
    }

    public void setAgeTertiaryEducationType(AgeTertiaryEducationType ageTertiaryEducationType) {
        this.ageTertiaryEducationType = ageTertiaryEducationType;
    }
}
