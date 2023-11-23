package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.DiversityDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.DiversityDissagregationOption;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DiversityDissagregationOptionService extends StandardDissagregationOptionService<DiversityDissagregationOptionDao, DiversityDissagregationOption>{

    @Inject
    DiversityDissagregationOptionDao diversityDissagregationOptionDao;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(DiversityDissagregationOptionService.class);


    @Override
    protected DiversityDissagregationOptionDao getDao() {
        return this.diversityDissagregationOptionDao;
    }

}
