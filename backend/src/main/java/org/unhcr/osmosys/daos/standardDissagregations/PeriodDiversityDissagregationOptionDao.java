package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodDiversityDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.StandardDissagregationOptionPeriodId;

import javax.ejb.Stateless;

@SuppressWarnings("unchecked")
@Stateless
public class PeriodDiversityDissagregationOptionDao extends PeriodStandardDissagregationOptionDao<PeriodDiversityDissagregationOption> {
    public PeriodDiversityDissagregationOptionDao() {
        super(PeriodDiversityDissagregationOption.class);
    }


}
