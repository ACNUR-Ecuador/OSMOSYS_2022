package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodGenderDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.GenderDissagregationOptionPeriodId;

import javax.ejb.Stateless;

@SuppressWarnings("unchecked")
@Stateless
public class PeriodGenderDissagregationOptionDao extends PeriodStandardDissagregationOptionDao<PeriodGenderDissagregationOption, GenderDissagregationOptionPeriodId> {
    public PeriodGenderDissagregationOptionDao() {
        super(PeriodGenderDissagregationOption.class, GenderDissagregationOptionPeriodId.class);
    }

    {


    }
}
