package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.AgeDissagregationOption;

import javax.ejb.Stateless;

@SuppressWarnings("unchecked")
@Stateless
public class AgeDissagregationOptionDao extends StandardDissagregationOptionDao<AgeDissagregationOption> {
    public AgeDissagregationOptionDao() {
        super(AgeDissagregationOption.class);
    }

}
