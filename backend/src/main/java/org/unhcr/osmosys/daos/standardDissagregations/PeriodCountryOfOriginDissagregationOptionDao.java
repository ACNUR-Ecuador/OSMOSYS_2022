package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodCountryOfOriginDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.StandardDissagregationOptionPeriodId;

import javax.ejb.Stateless;

@SuppressWarnings("unchecked")
@Stateless
public class PeriodCountryOfOriginDissagregationOptionDao extends PeriodStandardDissagregationOptionDao<PeriodCountryOfOriginDissagregationOption> {
    public PeriodCountryOfOriginDissagregationOptionDao() {
        super(PeriodCountryOfOriginDissagregationOption.class);
    }

}
