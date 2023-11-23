package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.CountryOfOriginDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.CountryOfOriginDissagregationOption;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class CountryOfOriginDissagregationOptionService {

    @Inject
    CountryOfOriginDissagregationOptionDao countryOfOriginDissagregationOptionDao;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(CountryOfOriginDissagregationOptionService.class);

    public CountryOfOriginDissagregationOption getById(Long id) {
        return this.countryOfOriginDissagregationOptionDao.find(id);
    }

    public CountryOfOriginDissagregationOption saveOrUpdate(CountryOfOriginDissagregationOption countryOfOriginDissagregationOption) {
        if (countryOfOriginDissagregationOption.getId() == null) {
            this.countryOfOriginDissagregationOptionDao.save(countryOfOriginDissagregationOption);
        } else {
            this.countryOfOriginDissagregationOptionDao.update(countryOfOriginDissagregationOption);
        }
        return countryOfOriginDissagregationOption;
    }


}
