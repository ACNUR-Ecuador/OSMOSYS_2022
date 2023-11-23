package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.PopulationTypeDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.PopulationTypeDissagregationOption;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class PopulationTypeDissagregationOptionService {

    @Inject
    PopulationTypeDissagregationOptionDao populationTypeDissagregationOptionDao;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(PopulationTypeDissagregationOptionService.class);

    public PopulationTypeDissagregationOption getById(Long id) {
        return this.populationTypeDissagregationOptionDao.find(id);
    }

    public PopulationTypeDissagregationOption saveOrUpdate(PopulationTypeDissagregationOption populationTypeDissagregationOption) {
        if (populationTypeDissagregationOption.getId() == null) {
            this.populationTypeDissagregationOptionDao.save(populationTypeDissagregationOption);
        } else {
            this.populationTypeDissagregationOptionDao.update(populationTypeDissagregationOption);
        }
        return populationTypeDissagregationOption;
    }


}
