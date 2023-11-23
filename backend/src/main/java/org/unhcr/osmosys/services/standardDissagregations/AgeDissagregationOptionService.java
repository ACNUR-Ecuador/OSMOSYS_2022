package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.*;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.AgeDissagregationOption;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AgeDissagregationOptionService extends StandardDissagregationOptionService<AgeDissagregationOptionDao, AgeDissagregationOption>{

    @Inject
    AgeDissagregationOptionDao ageDissagregationOptionDao;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(AgeDissagregationOptionService.class);


    @Override
    protected AgeDissagregationOptionDao getDao() {
        return this.ageDissagregationOptionDao;
    }

}
