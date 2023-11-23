package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.CountryOfOriginDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.CountryOfOriginDissagregationOption;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class CountryOfOriginDissagregationOptionService extends StandardDissagregationOptionService<CountryOfOriginDissagregationOptionDao, CountryOfOriginDissagregationOption>{

    @Inject
    CountryOfOriginDissagregationOptionDao countryOfOriginDissagregationOptionDao;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(CountryOfOriginDissagregationOptionService.class);


    @Override
    protected CountryOfOriginDissagregationOptionDao getDao() {
        return this.countryOfOriginDissagregationOptionDao;
    }

}
