package org.unhcr.osmosys.model.standardDissagregations.periodOptions.ids;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CountryOfOriginDissagregationOptionPeriodId extends StandardDissagregationOptionPeriodId  implements Serializable {

    public CountryOfOriginDissagregationOptionPeriodId() {
    }

    public CountryOfOriginDissagregationOptionPeriodId(Long periodId, Long countryOfOriginDissagregationOptionId) {
        super(periodId,countryOfOriginDissagregationOptionId);

    }

}