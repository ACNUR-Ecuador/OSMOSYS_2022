package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.PopulationTypeDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.PopulationTypeDissagregationOption;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class PopulationTypeDissagregationOptionService extends StandardDissagregationOptionService<PopulationTypeDissagregationOptionDao, PopulationTypeDissagregationOption>{

    @Inject
    PopulationTypeDissagregationOptionDao populationTypeDissagregationOptionDao;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(PopulationTypeDissagregationOptionService.class);


    @Override
    protected PopulationTypeDissagregationOptionDao getDao() {
        return this.populationTypeDissagregationOptionDao;
    }

}
