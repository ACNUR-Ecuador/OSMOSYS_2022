package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodAgeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.AgeDissagregationOptionPeriodId;

import javax.ejb.Stateless;

@SuppressWarnings("unchecked")
@Stateless
public class PeriodAgeDissagregationOptionDao extends PeriodStandardDissagregationOptionDao<PeriodAgeDissagregationOption, AgeDissagregationOptionPeriodId> {
    public PeriodAgeDissagregationOptionDao() {
        super(PeriodAgeDissagregationOption.class, AgeDissagregationOptionPeriodId.class);
    }

    {


    }
}
