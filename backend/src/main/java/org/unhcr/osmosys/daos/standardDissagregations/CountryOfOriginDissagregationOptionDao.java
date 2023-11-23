package org.unhcr.osmosys.daos.standardDissagregations;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.CountryOfOriginDissagregationOption;

import javax.ejb.Stateless;

@SuppressWarnings("unchecked")
@Stateless
public class CountryOfOriginDissagregationOptionDao extends StandardDissagregationOptionDao<CountryOfOriginDissagregationOption> {
    public CountryOfOriginDissagregationOptionDao() {
        super(CountryOfOriginDissagregationOption.class);
    }
}
