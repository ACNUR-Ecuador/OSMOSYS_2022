package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodGenderDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.StandardDissagregationOptionPeriodId;

import javax.ejb.Stateless;

@SuppressWarnings("unchecked")
@Stateless
public class PeriodGenderDissagregationOptionDao extends PeriodStandardDissagregationOptionDao<PeriodGenderDissagregationOption> {
    public PeriodGenderDissagregationOptionDao() {
        super(PeriodGenderDissagregationOption.class);
    }


}
