package org.unhcr.osmosys.services.standardDissagregations;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.daos.standardDissagregations.StandardDissagregationOptionDao;
import org.unhcr.osmosys.model.standardDissagregations.options.*;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;


@Stateless
public class StandardDissagregationOptionService {

    @Inject
    private ModelWebTransformationService modelWebTransformationService;

    @Inject
    private StandardDissagregationOptionDao standardDissagregationOptionDao;


    public StandardDissagregationOption getById(Long id) {
        return  this.standardDissagregationOptionDao.find(id);
    }

    public <T extends StandardDissagregationOption> T saveOrUpdate(T dissagregationOption) {
        if (dissagregationOption.getId() == null) {
            this.standardDissagregationOptionDao.save(dissagregationOption);
        } else {
            this.standardDissagregationOptionDao.update(dissagregationOption);
        }
        return dissagregationOption;
    }

    public  List<StandardDissagregationOption> getByState(State state) {
        return  this.standardDissagregationOptionDao.getByState(state);
    }

    public List<AgeDissagregationOption> getAgeDissagregationOptionByState(State state) {
        return this.standardDissagregationOptionDao.getAgeOptionsByState(state);
    }
    public List<PopulationTypeDissagregationOption> getPopulationTypeOptionsByState(State state) {
        return this.standardDissagregationOptionDao.getPopulationTypeOptionsByState(state);
    }
    public List<GenderDissagregationOption> getGenderDissagregationOptionByState(State state) {
        return this.standardDissagregationOptionDao.getGenderOptionsByState(state);
    }
    public List<DiversityDissagregationOption> getDiversityeOptionsByState(State state) {
        return this.standardDissagregationOptionDao.getDiversityOptionsByState(state);
    }
    public List<CountryOfOriginDissagregationOption> getCountryOfOriginDissagregationOptionByState(State state) {
        return this.standardDissagregationOptionDao.getCountryOfOriginOptionsByState(state);
    }

    public  List<StandardDissagregationOption> getWebByState(State state) {
        return  this.standardDissagregationOptionDao.getByState(state);
    }

    public List<StandardDissagregationOptionWeb> getAgeDissagregationOptionWebByState(State state) {
        return this.modelWebTransformationService.standardDissagregationOptionsToStandardDissagregationOptionWebs(this.standardDissagregationOptionDao.getAgeOptionsByState(state));
    }
    public List<StandardDissagregationOptionWeb> getPopulationTypeOptionsWebByState(State state) {
        return this.modelWebTransformationService.standardDissagregationOptionsToStandardDissagregationOptionWebs(this.standardDissagregationOptionDao.getPopulationTypeOptionsByState(state));
    }
    public List<StandardDissagregationOptionWeb> getGenderDissagregationOptionWebByState(State state) {
        return this.modelWebTransformationService.standardDissagregationOptionsToStandardDissagregationOptionWebs(this.standardDissagregationOptionDao.getGenderOptionsByState(state));
    }
    public List<StandardDissagregationOptionWeb> getDiversityeOptionsWebByState(State state) {
        return this.modelWebTransformationService.standardDissagregationOptionsToStandardDissagregationOptionWebs(this.standardDissagregationOptionDao.getDiversityOptionsByState(state));
    }
    public List<StandardDissagregationOptionWeb> getCountryOfOriginDissagregationWebOptionByState(State state) {
        return this.modelWebTransformationService.standardDissagregationOptionsToStandardDissagregationOptionWebs(this.standardDissagregationOptionDao.getCountryOfOriginOptionsByState(state));
    }




}
