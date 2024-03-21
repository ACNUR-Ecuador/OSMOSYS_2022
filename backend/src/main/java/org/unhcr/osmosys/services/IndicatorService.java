package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorDao;
import org.unhcr.osmosys.model.CustomDissagregationAssignationToIndicator;
import org.unhcr.osmosys.model.DissagregationAssignationToIndicator;
import org.unhcr.osmosys.model.Indicator;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.DissagregationAssignationToIndicatorPeriodCustomization;
import org.unhcr.osmosys.model.standardDissagregations.options.AgeDissagregationOption;
import org.unhcr.osmosys.services.standardDissagregations.StandardDissagregationOptionService;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class IndicatorService {

    @Inject
    StatementService statementService;

    @Inject
    IndicatorDao indicatorDao;

    @Inject
    DissagregationAssignationToIndicatorService dissagregationAssignationToIndicatorService;

    @Inject
    CustomDissagregationAssignationToIndicatorService customDissagregationAssignationToIndicatorService;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

   @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    StandardDissagregationOptionService standardDissagregationOptionService;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(IndicatorService.class);

    public Indicator getById(Long id) {
        return this.indicatorDao.find(id);
    }

    public Indicator saveOrUpdate(Indicator indicator) {
        if (indicator.getId() == null) {
            this.indicatorDao.save(indicator);
        } else {
            this.indicatorDao.update(indicator);
        }
        return indicator;
    }

    public Long save(IndicatorWeb indicatorWeb) throws GeneralAppException {
        if (indicatorWeb == null) {
            throw new GeneralAppException("No se puede guardar un indicator null", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un indicator con id", Response.Status.BAD_REQUEST);
        }
        this.validate(indicatorWeb);
        Indicator indicator = new Indicator();
        indicatorWebToIndicator(indicatorWeb, indicator);


        // dissagregation assignations
        indicatorWeb.getDissagregationsAssignationToIndicator().forEach(dissagregationAssignationToIndicatorWeb -> {
            DissagregationAssignationToIndicator dia = this.dissagregationAssignationToIndicatorService.createDissagregationAssignationToIndicatorFromWeb(dissagregationAssignationToIndicatorWeb);
            indicator.addDissagregationAssignationToIndicator(dia);
        });
        indicatorWeb.getCustomDissagregationAssignationToIndicators().forEach(customDissagregationAssignationToIndicatorWeb -> {
            CustomDissagregationAssignationToIndicator cdia = this.customDissagregationAssignationToIndicatorService.createCustomDissagregationAssignationToIndicatorFromWeb(customDissagregationAssignationToIndicatorWeb);
            indicator.addCustomDissagregationAssignationToIndicator(cdia);
        });
        this.saveOrUpdate(indicator);
        this.updatePeriodStatementAssigment(indicator);
        return indicator.getId();
    }


    private void indicatorWebToIndicator(IndicatorWeb indicatorWeb, Indicator indicator) {
        indicator.setCode(indicatorWeb.getCode()); //
        indicator.setDescription(indicatorWeb.getDescription());
        indicator.setCategory(indicatorWeb.getCategory());
        indicator.setInstructions(indicatorWeb.getInstructions());
        indicator.setQualitativeInstructions(indicatorWeb.getQualitativeInstructions());
        indicator.setStatement(this.statementService.find(indicatorWeb.getStatement().getId()));
        indicator.setBlockAfterUpdate(indicatorWeb.getBlockAfterUpdate());
        indicator.setUnit(indicatorWeb.getUnit());
        indicator.setFrecuency(indicatorWeb.getFrecuency());
        indicator.setCompassIndicator(indicatorWeb.getCompassIndicator());
        indicator.setState(indicatorWeb.getState());
        /// no inicialmente actualizacion
        indicator.setIndicatorType(indicatorWeb.getIndicatorType());
        indicator.setMeasureType(indicatorWeb.getMeasureType());
        indicator.setAreaType(indicatorWeb.getAreaType());
        indicator.setMonitored(indicatorWeb.getMonitored());
        indicator.setCalculated(indicatorWeb.getCalculated());
        indicator.setTotalIndicatorCalculationType(indicatorWeb.getTotalIndicatorCalculationType());
    }

    public List<IndicatorWeb> getAll() {
        return this.modelWebTransformationService.indicatorsToIndicatorsWeb(this.indicatorDao.getAllWithData(), true, true);
    }

    public List<IndicatorWeb> getByState(State state) {
        return this.modelWebTransformationService.indicatorsToIndicatorsWeb(this.indicatorDao.getByState(state), true, true);
    }

    public Long update(IndicatorWeb indicatorWeb) throws GeneralAppException {
        if (indicatorWeb == null) {
            throw new GeneralAppException("No se puede actualizar un indicator null", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un indicator sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(indicatorWeb);
        Indicator indicator = this.indicatorDao.findWithData(indicatorWeb.getId());
        this.indicatorWebToIndicator(indicatorWeb, indicator);
        // dissagregationAssiment
        List<DissagregationAssignationToIndicatorWeb> dissagregationAssignationToIndicatorsNews = indicatorWeb.getDissagregationsAssignationToIndicator();

        List<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsOriginals = new ArrayList<>(indicator.getDissagregationsAssignationToIndicator());


        // to create
        List<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsToCreate = this.dissagregationAssignationToIndicatorService.getToCreate(dissagregationAssignationToIndicatorsNews, dissagregationAssignationToIndicatorsOriginals);
        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : dissagregationAssignationToIndicatorsToCreate) {

            if (dissagregationAssignationToIndicator.getUseCustomAgeDissagregations()) {
                Optional<DissagregationAssignationToIndicatorWeb> webOptional = dissagregationAssignationToIndicatorsNews.stream()
                        .filter(dissagregationAssignationToIndicatorWeb -> this.dissagregationAssignationToIndicatorService.equalsWebToEntity(dissagregationAssignationToIndicatorWeb, dissagregationAssignationToIndicator))
                        .findFirst();
                if (webOptional.isPresent()) {
                    // agrefo todos si es el caso
                    DissagregationAssignationToIndicatorWeb web = webOptional.get();
                    for (StandardDissagregationOptionWeb customIndicatorOptionWeb : web.getCustomIndicatorOptions()) {
                        AgeDissagregationOption ageOption = (AgeDissagregationOption) this.standardDissagregationOptionService.getById(customIndicatorOptionWeb.getId());
                        dissagregationAssignationToIndicator.addAgeDissagregationCustomizations(ageOption);
                    }

                } else {
                    throw new GeneralAppException("Error en el manejo de segregaciones personalizadas para el indicador.", Response.Status.INTERNAL_SERVER_ERROR);
                }
            }

            indicator.addDissagregationAssignationToIndicator(dissagregationAssignationToIndicator);
        }

        // to dissable
        List<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsToDisable = this.dissagregationAssignationToIndicatorService.getToDisableTotal(dissagregationAssignationToIndicatorsNews, dissagregationAssignationToIndicatorsOriginals);
        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : dissagregationAssignationToIndicatorsToDisable) {
            dissagregationAssignationToIndicator.setState(State.INACTIVO);
            // no update customs
        }
        // to enable
        List<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsToEnable = this.dissagregationAssignationToIndicatorService.getToEnable(dissagregationAssignationToIndicatorsNews, dissagregationAssignationToIndicatorsOriginals);
        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : dissagregationAssignationToIndicatorsToEnable) {
            dissagregationAssignationToIndicator.setState(State.ACTIVO);


        }
        updateCustomOptions(dissagregationAssignationToIndicatorsToDisable, dissagregationAssignationToIndicatorsNews);

        List<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsToKeep = this.dissagregationAssignationToIndicatorService.getToKeep(dissagregationAssignationToIndicatorsNews, dissagregationAssignationToIndicatorsOriginals);
        updateCustomOptions(dissagregationAssignationToIndicatorsToKeep, dissagregationAssignationToIndicatorsNews);

        ///customs dissagregations

        List<CustomDissagregationAssignationToIndicatorWeb> customDissagregationAssignationToIndicatorsNews = indicatorWeb.getCustomDissagregationAssignationToIndicators();

        List<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicatorsOriginals = new ArrayList<>(indicator.getCustomDissagregationAssignationToIndicators());

        // to create
        List<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicatorsToCreate = this.customDissagregationAssignationToIndicatorService.getToCreate(customDissagregationAssignationToIndicatorsNews, customDissagregationAssignationToIndicatorsOriginals);
        for (CustomDissagregationAssignationToIndicator customDissagregationAssignationToIndicator : customDissagregationAssignationToIndicatorsToCreate) {
            indicator.addCustomDissagregationAssignationToIndicator(customDissagregationAssignationToIndicator);
        }
        // to dissable
        List<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicatorsToDisable = this.customDissagregationAssignationToIndicatorService.getToDisableTotal(customDissagregationAssignationToIndicatorsNews, customDissagregationAssignationToIndicatorsOriginals);
        for (CustomDissagregationAssignationToIndicator customDissagregationAssignationToIndicator : customDissagregationAssignationToIndicatorsToDisable) {
            customDissagregationAssignationToIndicator.setState(State.INACTIVO);
        }
        // to enable
        List<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicatorsToEnable = this.customDissagregationAssignationToIndicatorService.getToEnable(customDissagregationAssignationToIndicatorsNews, customDissagregationAssignationToIndicatorsOriginals);
        for (CustomDissagregationAssignationToIndicator customDissagregationAssignationToIndicator : customDissagregationAssignationToIndicatorsToEnable) {
            customDissagregationAssignationToIndicator.setState(State.ACTIVO);
        }


        this.saveOrUpdate(indicator);
        updatePeriodStatementAssigment(indicator);
        this.updateIndicatorExecutionsDissagregationsByIndicator(
                indicator
        );
        this.indicatorExecutionService
                .updateIndicatorExecutionsCustomDissagregations(
                        customDissagregationAssignationToIndicatorsToEnable,
                        customDissagregationAssignationToIndicatorsToDisable,
                        customDissagregationAssignationToIndicatorsToCreate);
        return indicator.getId();
    }

    /**
     * updates dissagregations for all ie that uses this indicator
     * @param indicator
     * @throws GeneralAppException
     */
    private void updateIndicatorExecutionsDissagregationsByIndicator(Indicator indicator) throws GeneralAppException {

        List<Period> periods = indicator.getDissagregationsAssignationToIndicator().stream().map(DissagregationAssignationToIndicator::getPeriod).distinct().collect(Collectors.toList());
        for (Period period : periods) {

            this.indicatorExecutionService.updatePerformanceIndicatorExecutionsDissagregations(period,indicator);
        }

    }




    private void updatePeriodStatementAssigment(Indicator indicator) throws GeneralAppException {
        List<Period> periodsDissagregations = indicator.getDissagregationsAssignationToIndicator().stream().map(DissagregationAssignationToIndicator::getPeriod).distinct().collect(Collectors.toList());
        List<Period> periodsCustomsDissagregations = indicator.getCustomDissagregationAssignationToIndicators().stream().map(CustomDissagregationAssignationToIndicator::getPeriod).distinct().collect(Collectors.toList());
        List<Period> periodsTotals = new ArrayList<>();
        if (!periodsDissagregations.isEmpty()) periodsTotals.addAll(periodsDissagregations);
        if (!periodsCustomsDissagregations.isEmpty()) periodsTotals.addAll(periodsCustomsDissagregations);
        periodsTotals = periodsTotals.stream().distinct().collect(Collectors.toList());
        if (periodsTotals.isEmpty()) return;

        StatementWeb statementWeb = this.modelWebTransformationService.statementToStatementWeb(this.statementService.find(indicator.getStatement().getId()), true, true, true, true, true);
        List<PeriodStatementAsignationWeb> periodStatementAsignations = statementWeb.getPeriodStatementAsignations();
        for (Period period : periodsTotals) {
            Optional<PeriodStatementAsignationWeb> pasOptional = periodStatementAsignations.stream()
                    .filter(periodStatementAsignationWeb -> periodStatementAsignationWeb.getPeriod().getId().equals(period.getId()))
                    .findFirst();
            if (pasOptional.isPresent()) {
                PeriodStatementAsignationWeb pas = pasOptional.get();
                pas.setState(State.ACTIVO);
            } else {
                PeriodStatementAsignationWeb pas = new PeriodStatementAsignationWeb();
                pas.setState(State.ACTIVO);
                pas.setPeriod(this.modelWebTransformationService.periodToPeriodWeb(period, true));

                statementWeb.getPeriodStatementAsignations().add(pas);
            }
        }
        this.statementService.update(statementWeb);
    }

    /*************************************************************************/


    private void updateCustomOptions(List<DissagregationAssignationToIndicator> originals, List<DissagregationAssignationToIndicatorWeb> news) throws GeneralAppException {
        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : originals) {
            Optional<DissagregationAssignationToIndicatorWeb> webOptional = news.stream()
                    .filter(dissagregationAssignationToIndicatorWeb ->
                            this.dissagregationAssignationToIndicatorService.equalsWebToEntity(dissagregationAssignationToIndicatorWeb, dissagregationAssignationToIndicator))
                    .findFirst();

            if (webOptional.isEmpty()) {
                // no existe en los nuevos, // no actualizo
                break;
                //throw new GeneralAppException("Error en el manejo de segregaciones personalizadas para el indicador.", Response.Status.INTERNAL_SERVER_ERROR);
            }
            dissagregationAssignationToIndicator.setUseCustomAgeDissagregations(webOptional.get().getUseCustomAgeDissagregations());
            if (dissagregationAssignationToIndicator.getUseCustomAgeDissagregations()!=null && dissagregationAssignationToIndicator.getUseCustomAgeDissagregations()) {
                dissagregationAssignationToIndicator.getDissagregationAssignationToIndicatorPeriodCustomizations().size();

                Set<StandardDissagregationOptionWeb> webs = webOptional.get().getCustomIndicatorOptions();
                if (webs.isEmpty()) {
                    throw new GeneralAppException("Para el periodo " + dissagregationAssignationToIndicator.getPeriod().getYear() +
                            " para la desagregación " + dissagregationAssignationToIndicator.getDissagregationType().getLabel() +
                            " se ha activado la personalización de segregaciones pero no se han seleccionado opciones"
                            , Response.Status.BAD_REQUEST);
                }

                //add all, in add is logic to create or activate
                for (StandardDissagregationOptionWeb web : webs) {
                    AgeDissagregationOption option = (AgeDissagregationOption) this.standardDissagregationOptionService.getById(web.getId());
                    dissagregationAssignationToIndicator.addAgeDissagregationCustomizations(option);
                }
                // to Remove
                List<AgeDissagregationOption> toRemove = dissagregationAssignationToIndicator.getDissagregationAssignationToIndicatorPeriodCustomizations()
                        .stream()
                        .map(DissagregationAssignationToIndicatorPeriodCustomization::getAgeDissagregationOption)
                        .filter(ageDissagregationOption ->
                                webs.stream().noneMatch(
                                        standardDissagregationOptionWeb -> standardDissagregationOptionWeb.getId().equals(ageDissagregationOption.getId())
                                )
                        )
                        .collect(Collectors.toList());

                for (AgeDissagregationOption ageDissagregationOption : toRemove) {
                    dissagregationAssignationToIndicator.removeAgeDissagregationCustomizations(ageDissagregationOption);
                }


            }
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void validate(IndicatorWeb indicatorWeb) throws GeneralAppException {
        if (indicatorWeb == null) {
            throw new GeneralAppException("Indicador es nulo", Response.Status.BAD_REQUEST);
        }


        if (StringUtils.isBlank(indicatorWeb.getCode())) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(indicatorWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }

        if (indicatorWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getIndicatorType() == null) {
            throw new GeneralAppException("Tipo no válido", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getMeasureType() == null) {
            throw new GeneralAppException("Tipo de medida no válida", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getFrecuency() == null) {
            throw new GeneralAppException("Frecuencia no válida", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getAreaType() == null) {
            throw new GeneralAppException("Área no válida", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getMonitored() == null) {
            throw new GeneralAppException("Monitoreo no válido", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getCalculated() == null) {
            throw new GeneralAppException("Calculado no válido", Response.Status.BAD_REQUEST);
        }

        if (indicatorWeb.getCompassIndicator() == null) {
            throw new GeneralAppException("Indicador compass no válidos", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getTotalIndicatorCalculationType() == null) {
            throw new GeneralAppException("Tipo de cálculo total no válido", Response.Status.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(indicatorWeb.getDissagregationsAssignationToIndicator())) {
            throw new GeneralAppException("No se ha asignado ninguna desagregación", Response.Status.BAD_REQUEST);
        }
        Indicator itemRecovered = this.indicatorDao.getByCode(indicatorWeb.getCode());
        if (itemRecovered != null) {
            if (indicatorWeb.getId() == null || !indicatorWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un indicador con este código", Response.Status.BAD_REQUEST);
            }
        }
    }

    public List<IndicatorWeb> getByPeriodAssignmentAndState(Long periodId, State state) {
        return this.modelWebTransformationService.indicatorsToIndicatorsWeb(this.indicatorDao.getByPeriodAssignmentAndState(periodId, state), true, true);
    }

    public List<Indicator> getByCodeList(List<String> codeList) {
        return this.indicatorDao.getByCodeList(codeList);
    }



    public List<Indicator> getByPeriodYearAssignmentAndState(int year) {
        return this.indicatorDao.getByPeriodYearAssignmentAndState(year, State.ACTIVO);
    }

    public Indicator getByPeriodAndCode(Long periodId, String code) throws GeneralAppException {
        return this.indicatorDao.getByPeriodAndCode(periodId, code);
    }

    public Indicator getByCodeAndDescription(String code, String description) throws GeneralAppException {
        LOGGER.info(code + "-" + description);
        return this.indicatorDao.getByCodeAndDescription(code, description);
    }

    public Indicator getByCode(String code) throws GeneralAppException {
        return this.indicatorDao.getByCode(code);
    }
}
