package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CountryOfOriginDissagregationOptionPeriodId implements Serializable {

    public CountryOfOriginDissagregationOptionPeriodId() {
    }

    public CountryOfOriginDissagregationOptionPeriodId(Long periodId, Long countryOfOriginDissagregationOptionId) {
        this.periodId = periodId;
        this.countryOfOriginDissagregationOptionId = countryOfOriginDissagregationOptionId;

    }

    @Column(name = "period_id")
    Long periodId;

    @Column(name = "country_of_origin_dissagregation_option_id")
    Long countryOfOriginDissagregationOptionId;

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public Long getCountryOfOriginDissagregationOptionId() {
        return countryOfOriginDissagregationOptionId;
    }

    public void setCountryOfOriginDissagregationOptionId(Long countryOfOriginDissagregationOptionId) {
        this.countryOfOriginDissagregationOptionId = countryOfOriginDissagregationOptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CountryOfOriginDissagregationOptionPeriodId)) return false;

        CountryOfOriginDissagregationOptionPeriodId that = (CountryOfOriginDissagregationOptionPeriodId) o;

        return new EqualsBuilder().append(periodId, that.periodId).append(countryOfOriginDissagregationOptionId, that.countryOfOriginDissagregationOptionId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(periodId).append(countryOfOriginDissagregationOptionId).toHashCode();
    }
}