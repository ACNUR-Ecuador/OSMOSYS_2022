package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.PeriodDao;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.*;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.*;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.PopulationTypeDissagregationOptionPeriodId;
import org.unhcr.osmosys.services.standardDissagregations.*;
import org.unhcr.osmosys.webServices.model.PeriodWeb;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.*;
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
    AgeDissagregationOptionService ageDissagregationOptionService;

    @Inject
    GenderDissagregationOptionService genderDissagregationOptionService;

    @Inject
    PopulationTypeDissagregationOptionService populationTypeDissagregationOptionService;

    @Inject
    DiversityDissagregationOptionService diversityDissagregationOptionService;

    @Inject
    CountryOfOriginDissagregationOptionService countryOfOriginDissagregationOptionService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(PeriodService.class);

    public Period find(Long id) {
        return this.periodDao.find(id);
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
            AgeDissagregationOption option = this.ageDissagregationOptionService.getById(optionWeb.getId());
            PeriodAgeDissagregationOption periodOption = new PeriodAgeDissagregationOption(period, option);
            period.addPeriodAgeDissagregationOption(periodOption);
        }

        for (StandardDissagregationOptionWeb optionWeb : periodWeb.getPeriodGenderDissagregationOptions()) {
            GenderDissagregationOption option = this.genderDissagregationOptionService.getById(optionWeb.getId());
            PeriodGenderDissagregationOption periodOption = new PeriodGenderDissagregationOption(period, option);
            period.addPeriodGenderDissagregationOption(periodOption);
        }

        for (StandardDissagregationOptionWeb optionWeb : periodWeb.getPeriodPopulationTypeDissagregationOptions()) {
            PopulationTypeDissagregationOption option = this.populationTypeDissagregationOptionService.getById(optionWeb.getId());
            PeriodPopulationTypeDissagregationOption periodOption = new PeriodPopulationTypeDissagregationOption(period, option);
            period.addPeriodPopulationTypeDissagregationOption(periodOption);
        }


        for (StandardDissagregationOptionWeb optionWeb : periodWeb.getPeriodDiversityDissagregationOptions()) {
            DiversityDissagregationOption option = this.diversityDissagregationOptionService.getById(optionWeb.getId());
            PeriodDiversityDissagregationOption periodOption = new PeriodDiversityDissagregationOption(period, option);
            period.addPeriodDiversityDissagregationOption(periodOption);
        }


        for (StandardDissagregationOptionWeb optionWeb : periodWeb.getPeriodCountryOfOriginDissagregationOptions()) {
            CountryOfOriginDissagregationOption option = this.countryOfOriginDissagregationOptionService.getById(optionWeb.getId());
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

    public List<PeriodWeb> getAll() {
        return this.modelWebTransformationService.periodsToPeriodsWeb(this.periodDao.findAll());
    }

    public List<PeriodWeb> getByState(State state) {
        return this.modelWebTransformationService.periodsToPeriodsWeb(this.periodDao.getByState(state));
    }

    public Long update(PeriodWeb periodWeb) throws GeneralAppException {
        if (periodWeb == null) {
            throw new GeneralAppException("No se puede actualizar un period null", Response.Status.BAD_REQUEST);
        }
        if (periodWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un period sin id", Response.Status.BAD_REQUEST);
        }

        Period period = this.periodDao.find(periodWeb.getId());

        this.update
        this.updateDissagregationOptions();

        for (PeriodAgeDissagregationOption periodOption : period.getPeriodAgeDissagregationOptions()) {
            Set<Long> optionsIdsWeb = periodWeb.getPeriodAgeDissagregationOptions().stream().map(optionWeb -> optionWeb.getId()).collect(Collectors.toSet());
            Set<Long> optionsIds = period.getPeriodAgeDissagregationOptions().stream().map(option -> option.getDissagregationOption().getId()).collect(Collectors.toSet());
            //los nuevos
            Collection<Long> newOptions = CollectionUtils.subtract(optionsIdsWeb, optionsIds);
            for (Long newOption : newOptions) {
                AgeDissagregationOption option = this.ageDissagregationOptionService.getById(newOption);
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

    private void updateDissagregationOptions(
            Set<PeriodStandardDissagregationOption> currentPeriodOptions,
            List<StandardDissagregationOptionWeb> newPeriodOptionsWeb

    ) {
        Set<StandardDissagregationOption> newStandardDissagregationOptions = new HashSet<>();
        for (PeriodStandardDissagregationOption periodOption : currentPeriodOptions) {
            Set<Long> optionsIdsWeb = newPeriodOptionsWeb.stream().map(optionWeb -> optionWeb.getId()).collect(Collectors.toSet());
            Set<Long> optionsIds = currentPeriodOptions.stream().map(option -> option.getDissagregationOption().getId()).collect(Collectors.toSet());
            //los nuevos
            Collection<Long> newOptions = CollectionUtils.subtract(optionsIdsWeb, optionsIds);
            for (Long newOption : newOptions) {
                AgeDissagregationOption option = this.ageDissagregationOptionService.getById(newOption);
                PeriodAgeDissagregationOption newPeriodAgeOption = new PeriodAgeDissagregationOption(period, option);
                newStandardDissagregationOptions.add(newPeriodAgeOption);
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
        }
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

    public PeriodWeb getByid(Long id) {
        return this.modelWebTransformationService.periodToPeriodWeb(this.periodDao.find(id));
    }

    public PeriodWeb getWebWithGeneralIndicatorById(Long id) {

        Period period = this.periodDao.getWithGeneralIndicatorById(id);
        PeriodWeb periodWeb = this.modelWebTransformationService.periodToPeriodWeb(period);

        if (period.getGeneralIndicator() != null) {
            periodWeb.setGeneralIndicator(this.modelWebTransformationService.generalIndicatorToGeneralIndicatorWeb(period.getGeneralIndicator()));
        }
        return periodWeb;
    }

    public Period getWithGeneralIndicatorById(Long id) {

        return this.periodDao.getWithGeneralIndicatorById(id);

    }


    public List<PeriodWeb> getWithGeneralIndicatorAll() {
        List<Period> periods = this.periodDao.getWithGeneralIndicatorAll();

        List<PeriodWeb> r = new ArrayList<>();

        for (Period period : periods) {
            PeriodWeb periodWeb = this.modelWebTransformationService.periodToPeriodWeb(period);
            if (period.getGeneralIndicator() != null) {
                periodWeb.setGeneralIndicator(this.modelWebTransformationService.generalIndicatorToGeneralIndicatorWeb(period.getGeneralIndicator()));
            }
            r.add(periodWeb);
        }
        return r;
    }

    public Period getWithDissagregationOptionsById(Long id) {
        return this.periodDao.getWithDissagregationOptionsById(id);
    }

    public Period getByYear(Integer year) {
        return this.periodDao.getByYear(year);
    }

    public Period getById(Long year) {
        return this.periodDao.find(year);
    }

}
