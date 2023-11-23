package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PopulationTypeDissagregationOptionPeriodId  extends StandardDissagregationOptionPeriodId  implements Serializable {

    public PopulationTypeDissagregationOptionPeriodId() {
    }

    public PopulationTypeDissagregationOptionPeriodId(Long periodId, Long populationTypeDissagregationOptionId) {
        super(periodId,populationTypeDissagregationOptionId);

    }
}