package org.unhcr.osmosys.services.standardDissagregations;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.daos.standardDissagregations.StandardDissagregationOptionDao;
import org.unhcr.osmosys.model.Canton;
import org.unhcr.osmosys.model.standardDissagregations.options.*;
import org.unhcr.osmosys.webServices.model.CantonWeb;
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
        return this.standardDissagregationOptionDao.find(id);
    }

    public <T extends StandardDissagregationOption> T saveOrUpdate(T dissagregationOption) {
        if (dissagregationOption.getId() == null) {
            this.standardDissagregationOptionDao.save(dissagregationOption);
        } else {
            this.standardDissagregationOptionDao.update(dissagregationOption);
        }
        return dissagregationOption;
    }

    public List<StandardDissagregationOption> getByState(State state) {
        return this.standardDissagregationOptionDao.getByState(state);
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

    public List<Canton> getCantonDissagregationOptionByState(State state) {
        return this.standardDissagregationOptionDao.getCantonOptionsByState(state);
    }

    public List<StandardDissagregationOption> getWebByState(State state) {
        return this.standardDissagregationOptionDao.getByState(state);
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

    public List<CantonWeb> getCantonWebOptionByState(State state) {
        List<Canton> r=this.standardDissagregationOptionDao.getCantonOptionsByState(state);
        return this.modelWebTransformationService.cantonsToCantonsWeb(r);
    }

    public List<CantonWeb> getAllCantonWebOption() {
        return this.modelWebTransformationService.cantonsToCantonsWeb(this.standardDissagregationOptionDao.getCantonOptions());
    }

    public List<Canton> getAllCantonsOptions() {
        return this.standardDissagregationOptionDao.getCantonOptions();
    }

    public List<Canton> getCantonByIds(List<Long> ids) {
        return this.standardDissagregationOptionDao.getCantonByIds(ids);
    }

    public List<StandardDissagregationOption> getDissagregationOptionsByIds(List<Long> ids) {
        return this.standardDissagregationOptionDao.getDissagregationOptionsByIds(ids);
    }

    public Canton getByCantonDescriptionAndProvinceDescription(String cantonDescription, String provinceDescription) throws GeneralAppException {
        return this.standardDissagregationOptionDao.getByCantonDescriptionAndProvinceDescription(cantonDescription, provinceDescription);
    }


    public Canton discoverCanton(String codeCanton, String descriptionCanton, String codeProvincia, String descriptionProvincia) throws GeneralAppException {
        return this.standardDissagregationOptionDao.discoverCanton(codeCanton, descriptionCanton, codeProvincia, descriptionProvincia);
    }
}
