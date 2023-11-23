package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.PeriodAgeDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.PeriodAgeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.AgeDissagregationOptionPeriodId;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class PeriodAgeDissagregationOptionService {

    @Inject
    PeriodAgeDissagregationOptionDao periodAgeDissagregationOptionDao;




    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(PeriodAgeDissagregationOptionService.class);

    public PeriodAgeDissagregationOption getById(AgeDissagregationOptionPeriodId id) {
        return this.periodAgeDissagregationOptionDao.find(id);
    }

    public PeriodAgeDissagregationOption saveOrUpdate(PeriodAgeDissagregationOption periodAgeDissagregationOption) {
        if (periodAgeDissagregationOption.getId() == null) {
            this.periodAgeDissagregationOptionDao.save(periodAgeDissagregationOption);
        } else {
            this.periodAgeDissagregationOptionDao.update(periodAgeDissagregationOption);
        }
        return periodAgeDissagregationOption;
    }


}
