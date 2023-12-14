package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class StandardDissagregationOptionPeriodId implements Serializable {

    public StandardDissagregationOptionPeriodId() {
    }

    public StandardDissagregationOptionPeriodId(Long periodId, Long dissagregationOptionId) {
        this.periodId = periodId;
        this.dissagregationOptionId = dissagregationOptionId;
    }

    @Column(name = "period_id")
    Long periodId;

    @Column(name = "dissagregation_option_id")
    Long dissagregationOptionId;

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public Long getDissagregationOptionId() {
        return dissagregationOptionId;
    }

    public void setDissagregationOptionId(Long dissagregationOptionId) {
        this.dissagregationOptionId = dissagregationOptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof StandardDissagregationOptionPeriodId)) return false;

        StandardDissagregationOptionPeriodId that = (StandardDissagregationOptionPeriodId) o;

        return new EqualsBuilder().append(periodId, that.periodId).append(dissagregationOptionId, that.dissagregationOptionId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(periodId).append(dissagregationOptionId).toHashCode();
    }

    @Override
    public String toString() {
        return "StandardDissagregationOptionPeriodId{" +
                "periodId=" + periodId +
                ", dissagregationOptionId=" + dissagregationOptionId +
                '}';
    }
}