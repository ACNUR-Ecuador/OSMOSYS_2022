package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.GenderDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.GenderDissagregationOption;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class GenderDissagregationOptionService extends StandardDissagregationOptionService<GenderDissagregationOptionDao, GenderDissagregationOption>{

    @Inject
    GenderDissagregationOptionDao genderDissagregationOptionDao;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(GenderDissagregationOptionService.class);


    @Override
    protected GenderDissagregationOptionDao getDao() {
        return this.genderDissagregationOptionDao;
    }

}
