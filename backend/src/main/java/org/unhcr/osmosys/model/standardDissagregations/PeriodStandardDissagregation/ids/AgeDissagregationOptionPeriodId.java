package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AgeDissagregationOptionPeriodId implements Serializable {

    public AgeDissagregationOptionPeriodId() {
    }

    public AgeDissagregationOptionPeriodId(Long periodId, Long ageDissagregationOptionId) {
        this.periodId = periodId;
        this.ageDissagregationOptionId = ageDissagregationOptionId;
    }

    @Column(name = "period_id")
    Long periodId;

    @Column(name = "age_dissagregation_option_id")
    Long ageDissagregationOptionId;

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public Long getAgeDissagregationOptionId() {
        return ageDissagregationOptionId;
    }

    public void setAgeDissagregationOptionId(Long ageDissagregationOptionId) {
        this.ageDissagregationOptionId = ageDissagregationOptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AgeDissagregationOptionPeriodId)) return false;

        AgeDissagregationOptionPeriodId that = (AgeDissagregationOptionPeriodId) o;

        return new EqualsBuilder().append(periodId, that.periodId).append(ageDissagregationOptionId, that.ageDissagregationOptionId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(periodId).append(ageDissagregationOptionId).toHashCode();
    }
}