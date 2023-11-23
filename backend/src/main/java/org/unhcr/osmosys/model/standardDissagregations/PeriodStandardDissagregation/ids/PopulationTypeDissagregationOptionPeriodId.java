package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PopulationTypeDissagregationOptionPeriodId implements Serializable {

    public PopulationTypeDissagregationOptionPeriodId() {
    }

    public PopulationTypeDissagregationOptionPeriodId(Long periodId, Long populationTypeDissagregationOptionId) {
        this.periodId = periodId;
        this.populationTypeDissagregationOptionId = populationTypeDissagregationOptionId;

    }

    @Column(name = "period_id")
    Long periodId;

    @Column(name = "population_type_dissagregation_option_id")
    Long populationTypeDissagregationOptionId;

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public Long getPopulationTypeDissagregationOptionId() {
        return populationTypeDissagregationOptionId;
    }

    public void setPopulationTypeDissagregationOptionId(Long populationTypeDissagregationOptionId) {
        this.populationTypeDissagregationOptionId = populationTypeDissagregationOptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PopulationTypeDissagregationOptionPeriodId)) return false;

        PopulationTypeDissagregationOptionPeriodId that = (PopulationTypeDissagregationOptionPeriodId) o;

        return new EqualsBuilder().append(periodId, that.periodId).append(populationTypeDissagregationOptionId, that.populationTypeDissagregationOptionId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(periodId).append(populationTypeDissagregationOptionId).toHashCode();
    }
}