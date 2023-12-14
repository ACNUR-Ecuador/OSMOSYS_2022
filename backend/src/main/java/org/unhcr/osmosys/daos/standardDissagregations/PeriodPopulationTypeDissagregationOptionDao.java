package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodPopulationTypeDissagregationOption;

import javax.ejb.Stateless;

@SuppressWarnings("unchecked")
@Stateless
public class PeriodPopulationTypeDissagregationOptionDao extends PeriodStandardDissagregationOptionDao<PeriodPopulationTypeDissagregationOption> {


    public PeriodPopulationTypeDissagregationOptionDao() {
        super(PeriodPopulationTypeDissagregationOption.class);
    }
}
