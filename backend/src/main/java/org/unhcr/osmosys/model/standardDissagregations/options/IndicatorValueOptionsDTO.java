package org.unhcr.osmosys.model.standardDissagregations.options;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.Canton;

public class IndicatorValueOptionsDTO {

    public IndicatorValueOptionsDTO() {
    }

    public IndicatorValueOptionsDTO(PopulationTypeDissagregationOption populationType, CountryOfOriginDissagregationOption countryOfOrigin, GenderDissagregationOption genderType, AgeDissagregationOption ageType, DiversityDissagregationOption diversityType, Canton location) {
        this.populationType = populationType;
        this.countryOfOrigin = countryOfOrigin;
        this.genderType = genderType;
        this.ageType = ageType;
        this.diversityType = diversityType;
        this.location = location;
    }

    private PopulationTypeDissagregationOption populationType;

    private CountryOfOriginDissagregationOption countryOfOrigin;

    private GenderDissagregationOption genderType;

    private AgeDissagregationOption ageType;


    private DiversityDissagregationOption diversityType;

    private Canton location;


    public PopulationTypeDissagregationOption getPopulationType() {
        return populationType;
    }

    public void setPopulationType(PopulationTypeDissagregationOption populationType) {
        this.populationType = populationType;
    }

    public CountryOfOriginDissagregationOption getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(CountryOfOriginDissagregationOption countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public GenderDissagregationOption getGenderType() {
        return genderType;
    }

    public void setGenderType(GenderDissagregationOption genderType) {
        this.genderType = genderType;
    }

    public AgeDissagregationOption getAgeType() {
        return ageType;
    }

    public void setAgeType(AgeDissagregationOption ageType) {
        this.ageType = ageType;
    }

    public DiversityDissagregationOption getDiversityType() {
        return diversityType;
    }

    public void setDiversityType(DiversityDissagregationOption diversityType) {
        this.diversityType = diversityType;
    }

    public Canton getLocation() {
        return location;
    }

    public void setLocation(Canton location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof IndicatorValueOptionsDTO)) return false;

        IndicatorValueOptionsDTO that = (IndicatorValueOptionsDTO) o;

        return new EqualsBuilder().append(populationType, that.populationType).append(countryOfOrigin, that.countryOfOrigin).append(genderType, that.genderType).append(ageType, that.ageType).append(diversityType, that.diversityType).append(location, that.location).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(populationType).append(countryOfOrigin).append(genderType).append(ageType).append(diversityType).append(location).toHashCode();
    }
}
