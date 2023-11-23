package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.PopulationTypeDissagregationOption;

import javax.ejb.Stateless;

@Stateless
public class PopulationTypeDissagregationOptionDao extends StandardDissagregationOptionDao<PopulationTypeDissagregationOption>  {
    public PopulationTypeDissagregationOptionDao() {
        super(PopulationTypeDissagregationOption.class);
    }

}
