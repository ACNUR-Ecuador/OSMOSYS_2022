package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodDiversityDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.DiversityDissagregationOptionPeriodId;

import javax.ejb.Stateless;



@Stateless
public class PeriodDiversityDissagregationOptionDao extends PeriodStandardDissagregationOptionDao<PeriodDiversityDissagregationOption, DiversityDissagregationOptionPeriodId> {
    public PeriodDiversityDissagregationOptionDao() {
        super(PeriodDiversityDissagregationOption.class, DiversityDissagregationOptionPeriodId.class);
    }

    {


    }
}
