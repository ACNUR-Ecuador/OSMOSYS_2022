package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodPopulationTypeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.PopulationTypeDissagregationOptionPeriodId;

import javax.ejb.Stateless;

@SuppressWarnings("unchecked")
@Stateless
public class PeriodPopulationTypeDissagregationOptionDao extends PeriodStandardDissagregationOptionDao<PeriodPopulationTypeDissagregationOption, PopulationTypeDissagregationOptionPeriodId> {


    public PeriodPopulationTypeDissagregationOptionDao() {
        super(PeriodPopulationTypeDissagregationOption.class, PopulationTypeDissagregationOptionPeriodId.class);
    }
}
