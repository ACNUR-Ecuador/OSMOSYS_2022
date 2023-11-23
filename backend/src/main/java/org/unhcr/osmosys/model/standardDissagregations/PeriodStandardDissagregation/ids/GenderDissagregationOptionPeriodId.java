package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class GenderDissagregationOptionPeriodId implements Serializable {

    public GenderDissagregationOptionPeriodId() {
    }

    public GenderDissagregationOptionPeriodId(Long periodId, Long genderDissagregationOptionId) {
        this.periodId = periodId;
        this.genderDissagregationOptionId = genderDissagregationOptionId;
    }

    @Column(name = "period_id")
    Long periodId;

    @Column(name = "gender_dissagregation_option_id")
    Long genderDissagregationOptionId;

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public Long getGenderDissagregationOptionId() {
        return genderDissagregationOptionId;
    }

    public void setGenderDissagregationOptionId(Long genderDissagregationOptionId) {
        this.genderDissagregationOptionId = genderDissagregationOptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof GenderDissagregationOptionPeriodId)) return false;

        GenderDissagregationOptionPeriodId that = (GenderDissagregationOptionPeriodId) o;

        return new EqualsBuilder().append(periodId, that.periodId).append(genderDissagregationOptionId, that.genderDissagregationOptionId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(periodId).append(genderDissagregationOptionId).toHashCode();
    }
}