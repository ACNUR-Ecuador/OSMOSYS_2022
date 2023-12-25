package org.unhcr.osmosys.model.standardDissagregations.periodOptions.ids;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DiversityDissagregationOptionPeriodId  extends StandardDissagregationOptionPeriodId implements Serializable {

    public DiversityDissagregationOptionPeriodId() {
    }

    public DiversityDissagregationOptionPeriodId(Long periodId, Long diversityDissagregationOptionId) {
       super(periodId,diversityDissagregationOptionId);

    }

}