package org.unhcr.osmosys.model.standardDissagregations.periodOptions.ids;

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