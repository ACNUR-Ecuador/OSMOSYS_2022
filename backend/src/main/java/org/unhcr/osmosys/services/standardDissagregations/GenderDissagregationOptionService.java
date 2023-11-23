package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.GenderDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.GenderDissagregationOption;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class GenderDissagregationOptionService {

    @Inject
    GenderDissagregationOptionDao genderDissagregationOptionDao;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(GenderDissagregationOptionService.class);

    public GenderDissagregationOption getById(Long id) {
        return this.genderDissagregationOptionDao.find(id);
    }

    public GenderDissagregationOption saveOrUpdate(GenderDissagregationOption genderDissagregationOption) {
        if (genderDissagregationOption.getId() == null) {
            this.genderDissagregationOptionDao.save(genderDissagregationOption);
        } else {
            this.genderDissagregationOptionDao.update(genderDissagregationOption);
        }
        return genderDissagregationOption;
    }


}
