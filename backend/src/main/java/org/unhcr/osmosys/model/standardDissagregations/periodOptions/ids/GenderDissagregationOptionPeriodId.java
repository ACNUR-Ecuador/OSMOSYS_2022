package org.unhcr.osmosys.model.standardDissagregations.periodOptions.ids;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class GenderDissagregationOptionPeriodId extends StandardDissagregationOptionPeriodId implements Serializable {

    public GenderDissagregationOptionPeriodId() {
    }

    public GenderDissagregationOptionPeriodId(Long periodId, Long genderDissagregationOptionId) {
        super(periodId,genderDissagregationOptionId);
    }

}