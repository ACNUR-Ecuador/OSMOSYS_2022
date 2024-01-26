package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.PeriodDao;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.options.*;
import org.unhcr.osmosys.model.standardDissagregations.periodOptions.*;
import org.unhcr.osmosys.services.standardDissagregations.StandardDissagregationOptionService;
import org.unhcr.osmosys.webServices.model.PeriodWeb;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class PeriodService {

    @Inject
    PeriodDao periodDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    GeneralIndicatorService generalIndicatorService;


    @Inject
    StandardDissagregationOptionService standardDissagregationOptionService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(PeriodService.class);

    public Period find(Long id) {
        return this.periodDao.getWithDissagregationOptionsById(id);
    }

    public Period saveOrUpdate(Period period) {
        if (period.getId() == null) {
            this.periodDao.save(period);
        } else {
            this.periodDao.update(period);
        }
        return period;
    }

    public Long save(PeriodWeb periodWeb) throws GeneralAppException {
        if (periodWeb == null) {
            throw new GeneralAppException("No se puede guardar un period null", Response.Status.BAD_REQUEST);
        }
        if (periodWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un period con id", Response.Status.BAD_REQUEST);
        }
        this.validate(periodWeb);

        Period period = this.modelWebTransformationService.periodWebToPeriod(periodWeb);

        // standard dissagregations

        for (StandardDissagregationOptionWeb optionWeb : periodWeb.getPeriodAgeDissagregationOptions()) {
            AgeDissagregationOption option = (AgeDissagregationOption) this.standardDissagregationOptionService.getById(optionWeb.getId());
            PeriodAgeDissagregationOption periodOption = new PeriodAgeDissagregationOption(period, option);
            period.addPeriodAgeDissagregationOption(periodOption);
        }

        for (StandardDissagregationOptionWeb optionWeb : periodWeb.getPeriodGenderDissagregationOptions()) {
            GenderDissagregationOption option = (GenderDissagregationOption) this.standardDissagregationOptionService.getById(optionWeb.getId());
            PeriodGenderDissagregationOption periodOption = new PeriodGenderDissagregationOption(period, option);
            period.addPeriodGenderDissagregationOption(periodOption);
        }

        for (StandardDissagregationOptionWeb optionWeb : periodWeb.getPeriodPopulationTypeDissagregationOptions()) {
            PopulationTypeDissagregationOption option = (PopulationTypeDissagregationOption) this.standardDissagregationOptionService.getById(optionWeb.getId());
            PeriodPopulationTypeDissagregationOption periodOption = new PeriodPopulationTypeDissagregationOption(period, option);
            period.addPeriodPopulationTypeDissagregationOption(periodOption);
        }


        for (StandardDissagregationOptionWeb optionWeb : periodWeb.getPeriodDiversityDissagregationOptions()) {
            DiversityDissagregationOption option = (DiversityDissagregationOption) this.standardDissagregationOptionService.getById(optionWeb.getId());
            PeriodDiversityDissagregationOption periodOption = new PeriodDiversityDissagregationOption(period, option);
            period.addPeriodDiversityDissagregationOption(periodOption);
        }


        for (StandardDissagregationOptionWeb optionWeb : periodWeb.getPeriodCountryOfOriginDissagregationOptions()) {
            CountryOfOriginDissagregationOption option = (CountryOfOriginDissagregationOption) this.standardDissagregationOptionService.getById(optionWeb.getId());
            PeriodCountryOfOriginDissagregationOption periodOption = new PeriodCountryOfOriginDissagregationOption(period, option);
            period.addPeriodCountryOfOriginDissagregationOption(periodOption);
        }


        this.saveOrUpdate(period);
        if (periodWeb.getGeneralIndicator() != null) {
            this.generalIndicatorService.validate(periodWeb.getGeneralIndicator());
            period.setGeneralIndicator(this.modelWebTransformationService.generalIndicatorWebToGeneralIndicator(periodWeb.getGeneralIndicator()));
            period.getGeneralIndicator().setPeriod(period);
            this.generalIndicatorService.saveOrUpdate(period.getGeneralIndicator());
        }


        return period.getId();
    }


    public List<PeriodWeb> getByState(State state) {
        return this.modelWebTransformationService.periodsToPeriodsWeb(this.periodDao.getByState(state), false);
    }

    public Long update(PeriodWeb periodWeb) throws GeneralAppException {
        if (periodWeb == null) {
            throw new GeneralAppException("No se puede actualizar un period null", Response.Status.BAD_REQUEST);
        }
        if (periodWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un period sin id", Response.Status.BAD_REQUEST);
        }

        Period period = this.periodDao.getWithDissagregationOptionsById(periodWeb.getId());


        /********************************************age***************/
        Set<Long> optionsIdsWeb = periodWeb.getPeriodAgeDissagregationOptions().stream().map(StandardDissagregationOptionWeb::getId).collect(Collectors.toSet());
        Set<Long> optionsIds = period.getPeriodAgeDissagregationOptions().stream().map(option -> option.getDissagregationOption().getId()).collect(Collectors.toSet());
        //los nuevos
        Collection<Long> newOptions = CollectionUtils.subtract(optionsIdsWeb, optionsIds);
        for (Long newOption : newOptions) {
            AgeDissagregationOption option = (AgeDissagregationOption) this.standardDissagregationOptionService.getById(newOption);
            PeriodAgeDissagregationOption newPeriodAgeOption = new PeriodAgeDissagregationOption(period, option);
            period.addPeriodAgeDissagregationOption(newPeriodAgeOption);
        }

        //los eliminados
        Collection<Long> removeOptions = CollectionUtils.subtract(optionsIds, optionsIdsWeb);
        for (Long removeOption : removeOptions) {
            for (PeriodAgeDissagregationOption option : period.getPeriodAgeDissagregationOptions()) {
                if (option.getDissagregationOption().getId().equals(removeOption)) {
                    option.setState(State.INACTIVO);
                }
            }
        }

        //los existentes
        Collection<Long> toUpdateOptions = CollectionUtils.intersection(optionsIds, optionsIdsWeb);
        for (Long toUpdateOption : toUpdateOptions) {
            for (PeriodAgeDissagregationOption option : period.getPeriodAgeDissagregationOptions()) {
                if (option.getDissagregationOption().getId().equals(toUpdateOption)) {
                    option.setState(State.ACTIVO);
                }
            }
        }

        /********************************************Gender***************/
        optionsIdsWeb = periodWeb.getPeriodGenderDissagregationOptions().stream().map(StandardDissagregationOptionWeb::getId).collect(Collectors.toSet());
        optionsIds = period.getPeriodGenderDissagregationOptions().stream().map(option -> option.getDissagregationOption().getId()).collect(Collectors.toSet());
        //los nuevos
        newOptions = CollectionUtils.subtract(optionsIdsWeb, optionsIds);
        for (Long newOption : newOptions) {
            GenderDissagregationOption option = (GenderDissagregationOption) this.standardDissagregationOptionService.getById(newOption);
            PeriodGenderDissagregationOption newPeriodGenderOption = new PeriodGenderDissagregationOption(period, option);
            period.addPeriodGenderDissagregationOption(newPeriodGenderOption);
        }

        //los eliminados
        removeOptions = CollectionUtils.subtract(optionsIds, optionsIdsWeb);
        for (Long removeOption : removeOptions) {
            for (PeriodGenderDissagregationOption option : period.getPeriodGenderDissagregationOptions()) {
                if (option.getDissagregationOption().getId().equals(removeOption)) {
                    option.setState(State.INACTIVO);
                }
            }
        }

        //los existentes
        toUpdateOptions = CollectionUtils.intersection(optionsIds, optionsIdsWeb);
        for (Long toUpdateOption : toUpdateOptions) {
            for (PeriodGenderDissagregationOption option : period.getPeriodGenderDissagregationOptions()) {
                if (option.getDissagregationOption().getId().equals(toUpdateOption)) {
                    option.setState(State.ACTIVO);
                }
            }
        }

        /********************************************Population type***************/
        optionsIdsWeb = periodWeb.getPeriodPopulationTypeDissagregationOptions().stream().map(StandardDissagregationOptionWeb::getId).collect(Collectors.toSet());
        optionsIds = period.getPeriodPopulationTypeDissagregationOptions().stream().map(option -> option.getDissagregationOption().getId()).collect(Collectors.toSet());
        //los nuevos
        newOptions = CollectionUtils.subtract(optionsIdsWeb, optionsIds);
        for (Long newOption : newOptions) {
            PopulationTypeDissagregationOption option = (PopulationTypeDissagregationOption) this.standardDissagregationOptionService.getById(newOption);
            PeriodPopulationTypeDissagregationOption newPeriodPopulationTypeOption = new PeriodPopulationTypeDissagregationOption(period, option);
            period.addPeriodPopulationTypeDissagregationOption(newPeriodPopulationTypeOption);
        }

        //los eliminados
        removeOptions = CollectionUtils.subtract(optionsIds, optionsIdsWeb);
        for (Long removeOption : removeOptions) {
            for (PeriodPopulationTypeDissagregationOption option : period.getPeriodPopulationTypeDissagregationOptions()) {
                if (option.getDissagregationOption().getId().equals(removeOption)) {
                    option.setState(State.INACTIVO);
                }
            }
        }

        //los existentes
        toUpdateOptions = CollectionUtils.intersection(optionsIds, optionsIdsWeb);
        for (Long toUpdateOption : toUpdateOptions) {
            for (PeriodPopulationTypeDissagregationOption option : period.getPeriodPopulationTypeDissagregationOptions()) {
                if (option.getDissagregationOption().getId().equals(toUpdateOption)) {
                    option.setState(State.ACTIVO);
                }
            }
        }

        /********************************************Diversity***************/

        optionsIdsWeb = periodWeb.getPeriodDiversityDissagregationOptions().stream().map(StandardDissagregationOptionWeb::getId).collect(Collectors.toSet());
        optionsIds = period.getPeriodDiversityDissagregationOptions().stream().map(option -> option.getDissagregationOption().getId()).collect(Collectors.toSet());
        //los nuevos
        newOptions = CollectionUtils.subtract(optionsIdsWeb, optionsIds);
        for (Long newOption : newOptions) {
            DiversityDissagregationOption option = (DiversityDissagregationOption) this.standardDissagregationOptionService.getById(newOption);
            PeriodDiversityDissagregationOption newPeriodDiversityOption = new PeriodDiversityDissagregationOption(period, option);
            period.addPeriodDiversityDissagregationOption(newPeriodDiversityOption);
        }

        //los eliminados
        removeOptions = CollectionUtils.subtract(optionsIds, optionsIdsWeb);
        for (Long removeOption : removeOptions) {
            for (PeriodDiversityDissagregationOption option : period.getPeriodDiversityDissagregationOptions()) {
                if (option.getDissagregationOption().getId().equals(removeOption)) {
                    option.setState(State.INACTIVO);
                }
            }
        }

        //los existentes
        toUpdateOptions = CollectionUtils.intersection(optionsIds, optionsIdsWeb);
        for (Long toUpdateOption : toUpdateOptions) {
            for (PeriodDiversityDissagregationOption option : period.getPeriodDiversityDissagregationOptions()) {
                if (option.getDissagregationOption().getId().equals(toUpdateOption)) {
                    option.setState(State.ACTIVO);
                }
            }
        }

        /********************************************Country of Origin***************/
        optionsIdsWeb = periodWeb.getPeriodCountryOfOriginDissagregationOptions().stream().map(StandardDissagregationOptionWeb::getId).collect(Collectors.toSet());
        optionsIds = period.getPeriodCountryOfOriginDissagregationOptions().stream().map(option -> option.getDissagregationOption().getId()).collect(Collectors.toSet());
        //los nuevos
        newOptions = CollectionUtils.subtract(optionsIdsWeb, optionsIds);
        for (Long newOption : newOptions) {
            CountryOfOriginDissagregationOption option = (CountryOfOriginDissagregationOption) this.standardDissagregationOptionService.getById(newOption);
            PeriodCountryOfOriginDissagregationOption newPeriodCountryOfOriginOption = new PeriodCountryOfOriginDissagregationOption(period, option);
            period.addPeriodCountryOfOriginDissagregationOption(newPeriodCountryOfOriginOption);
        }

        //los eliminados
        removeOptions = CollectionUtils.subtract(optionsIds, optionsIdsWeb);
        for (Long removeOption : removeOptions) {
            for (PeriodCountryOfOriginDissagregationOption option : period.getPeriodCountryOfOriginDissagregationOptions()) {
                if (option.getDissagregationOption().getId().equals(removeOption)) {
                    option.setState(State.INACTIVO);
                }
            }
        }

        //los existentes
        toUpdateOptions = CollectionUtils.intersection(optionsIds, optionsIdsWeb);
        for (Long toUpdateOption : toUpdateOptions) {
            for (PeriodCountryOfOriginDissagregationOption option : period.getPeriodCountryOfOriginDissagregationOptions()) {
                if (option.getDissagregationOption().getId().equals(toUpdateOption)) {
                    option.setState(State.ACTIVO);
                }
            }
        }


        this.validate(periodWeb);
        this.saveOrUpdate(period);
        if (periodWeb.getGeneralIndicator() != null) {
            this.generalIndicatorService.validate(periodWeb.getGeneralIndicator());
            if (periodWeb.getGeneralIndicator().getId() != null) {
                periodWeb.getGeneralIndicator().setPeriod(new PeriodWeb());
                periodWeb.getGeneralIndicator().getPeriod().setId(period.getId());
                this.generalIndicatorService.update(periodWeb.getGeneralIndicator());
            } else {
                period.setGeneralIndicator(this.modelWebTransformationService.generalIndicatorWebToGeneralIndicator(periodWeb.getGeneralIndicator()));
                period.getGeneralIndicator().setPeriod(period);
                periodWeb.getGeneralIndicator().setPeriod(new PeriodWeb());
                periodWeb.getGeneralIndicator().getPeriod().setId(period.getId());
                this.generalIndicatorService.saveOrUpdate(this.modelWebTransformationService.generalIndicatorWebToGeneralIndicator(periodWeb.getGeneralIndicator()));
            }


        }


        return period.getId();
    }


    public void validate(PeriodWeb periodWeb) throws GeneralAppException {
        if (periodWeb == null) {
            throw new GeneralAppException("Periodo es nulo", Response.Status.BAD_REQUEST);
        }

        if (periodWeb.getYear() == null) {
            throw new GeneralAppException("Año no válido", Response.Status.BAD_REQUEST);
        }
        if (periodWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }

        Period itemRecovered = this.periodDao.getByYear(periodWeb.getYear());
        if (itemRecovered != null) {
            if (periodWeb.getId() == null || !periodWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con este año", Response.Status.BAD_REQUEST);
            }
        }
    }

    public PeriodWeb getWebById(Long id) {
        return this.modelWebTransformationService.periodToPeriodWeb(this.periodDao.getWithDissagregationOptionsById(id), false);
    }

    public Period getById(Long year) {
        return this.periodDao.find(year);
    }
    public PeriodWeb getWebWithAllDataById(Long id) {

        Period period = this.periodDao.getWithGeneralIndicatorById(id);
        PeriodWeb periodWeb = this.modelWebTransformationService.periodToPeriodWeb(period, false);

        if (period.getGeneralIndicator() != null) {
            periodWeb.setGeneralIndicator(this.modelWebTransformationService.generalIndicatorToGeneralIndicatorWeb(period.getGeneralIndicator()));
        }
        return periodWeb;
    }
    public Period getWithAllDataById(Long id) {
        return this.periodDao.getWithDissagregationOptionsById(id);
    }




    public List<PeriodWeb> getWithGeneralIndicatorAll() {
        List<Period> periods = this.periodDao.getAllWithDissagregationOptions();

        return this.modelWebTransformationService.periodsToPeriodsWeb(periods,false);

    }



    public Period getByYear(Integer year) {
        return this.periodDao.getByYear(year);
    }



}
