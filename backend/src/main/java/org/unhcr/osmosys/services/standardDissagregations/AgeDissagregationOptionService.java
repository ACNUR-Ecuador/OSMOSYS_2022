package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.*;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.AgeDissagregationOption;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AgeDissagregationOptionService {

    @Inject
    AgeDissagregationOptionDao ageDissagregationOptionDao;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(AgeDissagregationOptionService.class);

    public AgeDissagregationOption getById(Long id) {
        return this.ageDissagregationOptionDao.find(id);
    }

    public AgeDissagregationOption saveOrUpdate(AgeDissagregationOption ageDissagregationOption) {
        if (ageDissagregationOption.getId() == null) {
            this.ageDissagregationOptionDao.save(ageDissagregationOption);
        } else {
            this.ageDissagregationOptionDao.update(ageDissagregationOption);
        }
        return ageDissagregationOption;
    }


}
