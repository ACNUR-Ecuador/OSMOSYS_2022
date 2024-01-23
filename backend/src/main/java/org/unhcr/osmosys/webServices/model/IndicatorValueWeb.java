package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import java.io.Serializable;
import java.math.BigDecimal;


public class IndicatorValueWeb extends BaseWebEntity implements Serializable {
    public IndicatorValueWeb() {
        super();
    }

    private MonthEnum monthEnum;
    private DissagregationType dissagregationType;
    private StandardDissagregationOptionWeb populationType;
    private StandardDissagregationOptionWeb countryOfOrigin;
    private StandardDissagregationOptionWeb genderType;
    private StandardDissagregationOptionWeb ageType;
    private StandardDissagregationOptionWeb diversityType;
    private CantonWeb location;

    private BigDecimal value;
    private BigDecimal denominatorValue;
    private BigDecimal numeratorValue;

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

    public StandardDissagregationOptionWeb getPopulationType() {
        return populationType;
    }

    public void setPopulationType(StandardDissagregationOptionWeb populationType) {
        this.populationType = populationType;
    }

    public StandardDissagregationOptionWeb getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(StandardDissagregationOptionWeb countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public StandardDissagregationOptionWeb getGenderType() {
        return genderType;
    }

    public void setGenderType(StandardDissagregationOptionWeb genderType) {
        this.genderType = genderType;
    }

    public StandardDissagregationOptionWeb getAgeType() {
        return ageType;
    }

    public void setAgeType(StandardDissagregationOptionWeb ageType) {
        this.ageType = ageType;
    }

    public StandardDissagregationOptionWeb getDiversityType() {
        return diversityType;
    }

    public void setDiversityType(StandardDissagregationOptionWeb diversityType) {
        this.diversityType = diversityType;
    }

    public CantonWeb getLocation() {
        return location;
    }

    public void setLocation(CantonWeb location) {
        this.location = location;
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


}
