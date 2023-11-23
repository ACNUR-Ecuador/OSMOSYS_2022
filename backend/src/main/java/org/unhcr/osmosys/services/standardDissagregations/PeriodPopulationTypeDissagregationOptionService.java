package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.PeriodPopulationTypeDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodPopulationTypeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.AgeDissagregationOptionPeriodId;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.PopulationTypeDissagregationOptionPeriodId;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class PeriodPopulationTypeDissagregationOptionService {

    @Inject
    PeriodPopulationTypeDissagregationOptionDao periodPopulationTypeDissagregationOptionDao;




    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(PeriodPopulationTypeDissagregationOptionService.class);

    public PeriodPopulationTypeDissagregationOption getById(PopulationTypeDissagregationOptionPeriodId id) {
        return this.periodPopulationTypeDissagregationOptionDao.find(id);
    }

    public PeriodPopulationTypeDissagregationOption saveOrUpdate(PeriodPopulationTypeDissagregationOption periodPopulationTypeDissagregationOption) {
        if (periodPopulationTypeDissagregationOption.getId() == null) {
            this.periodPopulationTypeDissagregationOptionDao.save(periodPopulationTypeDissagregationOption);
        } else {
            this.periodPopulationTypeDissagregationOptionDao.update(periodPopulationTypeDissagregationOption);
        }
        return periodPopulationTypeDissagregationOption;
    }


}
