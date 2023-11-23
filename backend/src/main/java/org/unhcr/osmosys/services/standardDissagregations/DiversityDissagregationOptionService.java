package org.unhcr.osmosys.services.standardDissagregations;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.standardDissagregations.DiversityDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.DiversityDissagregationOption;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DiversityDissagregationOptionService {

    @Inject
    DiversityDissagregationOptionDao diversityDissagregationOptionDao;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(DiversityDissagregationOptionService.class);

    public DiversityDissagregationOption getById(Long id) {
        return this.diversityDissagregationOptionDao.find(id);
    }

    public DiversityDissagregationOption saveOrUpdate(DiversityDissagregationOption diversityDissagregationOption) {
        if (diversityDissagregationOption.getId() == null) {
            this.diversityDissagregationOptionDao.save(diversityDissagregationOption);
        } else {
            this.diversityDissagregationOptionDao.update(diversityDissagregationOption);
        }
        return diversityDissagregationOption;
    }


}
