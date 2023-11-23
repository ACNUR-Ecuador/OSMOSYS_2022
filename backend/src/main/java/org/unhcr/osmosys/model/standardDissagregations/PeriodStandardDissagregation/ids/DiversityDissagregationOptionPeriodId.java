package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DiversityDissagregationOptionPeriodId implements Serializable {

    public DiversityDissagregationOptionPeriodId() {
    }

    public DiversityDissagregationOptionPeriodId(Long periodId, Long diversityDissagregationOptionId) {
        this.periodId = periodId;
        this.diversityDissagregationOptionId = diversityDissagregationOptionId;

    }

    @Column(name = "period_id")
    Long periodId;

    @Column(name = "diversity_dissagregation_option_id")
    Long diversityDissagregationOptionId;

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public Long getDiversityDissagregationOptionId() {
        return diversityDissagregationOptionId;
    }

    public void setDiversityDissagregationOptionId(Long diversityDissagregationOptionId) {
        this.diversityDissagregationOptionId = diversityDissagregationOptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof DiversityDissagregationOptionPeriodId)) return false;

        DiversityDissagregationOptionPeriodId that = (DiversityDissagregationOptionPeriodId) o;

        return new EqualsBuilder().append(periodId, that.periodId).append(diversityDissagregationOptionId, that.diversityDissagregationOptionId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(periodId).append(diversityDissagregationOptionId).toHashCode();
    }
}