package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PeriodWeb extends BaseWebEntity implements Serializable {

    public PeriodWeb() {
        super();
    }

    private Integer year;
    private GeneralIndicatorWeb generalIndicator;

    private Set<StandardDissagregationOptionWeb> periodAgeDissagregationOptions = new HashSet<>();

    private Set<StandardDissagregationOptionWeb> periodGenderDissagregationOptions = new HashSet<>();

    private Set<StandardDissagregationOptionWeb> periodPopulationTypeDissagregationOptions = new HashSet<>();

    private Set<StandardDissagregationOptionWeb> periodDiversityDissagregationOptions = new HashSet<>();


    private Set<StandardDissagregationOptionWeb> periodCountryOfOriginDissagregationOptions = new HashSet<>();



    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }


    public GeneralIndicatorWeb getGeneralIndicator() {
        return generalIndicator;
    }

    public void setGeneralIndicator(GeneralIndicatorWeb generalIndicator) {
        this.generalIndicator = generalIndicator;
    }

    public Set<StandardDissagregationOptionWeb> getPeriodAgeDissagregationOptions() {
        return periodAgeDissagregationOptions;
    }

    public void setPeriodAgeDissagregationOptions(Set<StandardDissagregationOptionWeb> periodAgeDissagregationOptions) {
        this.periodAgeDissagregationOptions = periodAgeDissagregationOptions;
    }

    public Set<StandardDissagregationOptionWeb> getPeriodGenderDissagregationOptions() {
        return periodGenderDissagregationOptions;
    }

    public void setPeriodGenderDissagregationOptions(Set<StandardDissagregationOptionWeb> periodGenderDissagregationOptions) {
        this.periodGenderDissagregationOptions = periodGenderDissagregationOptions;
    }

    public Set<StandardDissagregationOptionWeb> getPeriodPopulationTypeDissagregationOptions() {
        return periodPopulationTypeDissagregationOptions;
    }

    public void setPeriodPopulationTypeDissagregationOptions(Set<StandardDissagregationOptionWeb> periodPopulationTypeDissagregationOptions) {
        this.periodPopulationTypeDissagregationOptions = periodPopulationTypeDissagregationOptions;
    }

    public Set<StandardDissagregationOptionWeb> getPeriodDiversityDissagregationOptions() {
        return periodDiversityDissagregationOptions;
    }

    public void setPeriodDiversityDissagregationOptions(Set<StandardDissagregationOptionWeb> periodDiversityDissagregationOptions) {
        this.periodDiversityDissagregationOptions = periodDiversityDissagregationOptions;
    }

    public Set<StandardDissagregationOptionWeb> getPeriodCountryOfOriginDissagregationOptions() {
        return periodCountryOfOriginDissagregationOptions;
    }

    public void setPeriodCountryOfOriginDissagregationOptions(Set<StandardDissagregationOptionWeb> periodCountryOfOriginDissagregationOptions) {
        this.periodCountryOfOriginDissagregationOptions = periodCountryOfOriginDissagregationOptions;
    }

    @Override
    public String toString() {
        return "PeriodWeb{" + "id=" + id + ", year=" + year + ", state=" + state + ", generalIndicator=" + generalIndicator + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeriodWeb)) return false;
        PeriodWeb periodWeb = (PeriodWeb) o;
        return year.equals(periodWeb.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year);
    }
}
