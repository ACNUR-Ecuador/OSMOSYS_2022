package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AgeDissagregationOptionPeriodId extends StandardDissagregationOptionPeriodId implements Serializable {

    public AgeDissagregationOptionPeriodId() {
    }

    public AgeDissagregationOptionPeriodId(Long periodId, Long ageDissagregationOptionId) {
        super(periodId,ageDissagregationOptionId);
    }



}