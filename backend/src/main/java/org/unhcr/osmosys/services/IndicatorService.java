package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.webServices.model.IndicatorWeb;
import org.unhcr.osmosys.webServices.model.MarkerWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        // markers
        indicatorWeb.getDissagregationsAssignationToIndicator().forEach(dissagregationAssignationToIndicatorWeb -> {
            DissagregationAssignationToIndicator dia = this.dissagregationAssignationToIndicatorService.createDissagregationAssignationToIndicatorFromWeb(dissagregationAssignationToIndicatorWeb);
            indicator.addDissagregationAssignationToIndicator(dia);
        });
        indicatorWeb.getCustomDissagregationAssignationToIndicators().forEach(customDissagregationAssignationToIndicatorWeb -> {
            CustomDissagregationAssignationToIndicator cdia = this.customDissagregationAssignationToIndicatorService.createCustomDissagregationAssignationToIndicatorFromWeb(customDissagregationAssignationToIndicatorWeb);
            indicator.addCustomDissagregationAssignationToIndicator(cdia);
        });
        this.saveOrUpdate(indicator);
        return indicator.getId();
    }

    @SuppressWarnings("DuplicatedCode")
    private void indicatorWebToIndicator(IndicatorWeb indicatorWeb, Indicator indicator) {
        indicator.setCode(indicatorWeb.getCode());
        indicator.setDescription(indicatorWeb.getDescription());
        indicator.setCategory(indicatorWeb.getCategory());
        indicator.setInstructions(indicatorWeb.getInstructions());
        indicator.setQualitativeInstructions(indicatorWeb.getQualitativeInstructions());
        indicator.setState(indicatorWeb.getState());
        indicator.setIndicatorType(indicatorWeb.getIndicatorType());
        indicator.setMeasureType(indicatorWeb.getMeasureType());
        indicator.setFrecuency(indicatorWeb.getFrecuency());
        indicator.setAreaType(indicatorWeb.getAreaType());
        indicator.setMonitored(indicatorWeb.getMonitored());
        indicator.setCalculated(indicatorWeb.getCalculated());
        indicator.setTotalIndicatorCalculationType(indicatorWeb.getTotalIndicatorCalculationType());
        indicator.setCompassIndicator(indicatorWeb.getCompassIndicator());
        indicator.setUnit(indicatorWeb.getUnit());
        indicator.setStatement(this.statementService.find(indicatorWeb.getStatement().getId()));
        indicator.setBlockAfterUpdate(indicatorWeb.getBlockAfterUpdate());
    }

    public List<IndicatorWeb> getAll() {
        return this.modelWebTransformationService.indicatorsToIndicatorsWeb(this.indicatorDao.getAllWithData(), true, true, true);
    }

    public List<IndicatorWeb> getByState(State state) {
        return this.modelWebTransformationService.indicatorsToIndicatorsWeb(this.indicatorDao.getByState(state), true, true, true);
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
        indicator.setDescription(indicatorWeb.getDescription());
        indicator.setInstructions(indicatorWeb.getInstructions());
        indicator.setQualitativeInstructions(indicatorWeb.getQualitativeInstructions());
        indicator.setCategory(indicatorWeb.getCategory());
        indicator.setStatement(this.statementService.find(indicatorWeb.getStatement().getId()));
        indicator.setBlockAfterUpdate(indicatorWeb.getBlockAfterUpdate());
        indicator.setUnit(indicatorWeb.getUnit());
        indicator.setFrecuency(indicatorWeb.getFrecuency());
        // marcadores
        // veo los nuevos
        indicatorWeb.getMarkers().forEach(markerWeb -> {
            Optional<Marker> markerOp = indicator.getMarkers().stream().filter(marker -> marker.getId().equals(markerWeb.getId())).findFirst();
            if (!markerOp.isPresent()) {
                indicator.addMarker(this.modelWebTransformationService.markerWebToMarker(markerWeb));
            }
        });
        // nuevo borro ausentes
        indicator.getMarkers().forEach(marker -> {
            Optional<MarkerWeb> markerWebOp = indicatorWeb.getMarkers().stream().filter(markerWeb -> markerWeb.getId().equals(marker.getId())).findFirst();
            if (!markerWebOp.isPresent()) {
                indicator.removeMarker(marker);
            }
        });
        // dissagregationAssiment

        List<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsToEnable = new ArrayList<>();
        List<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsToDisable = new ArrayList<>();
        List<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsToCreate = new ArrayList<>();
        indicatorWeb.getDissagregationsAssignationToIndicator().forEach(dissagregationAssignationToIndicatorWeb -> {
            if (dissagregationAssignationToIndicatorWeb.getId() != null) {
                Optional<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorOp = indicator.getDissagregationsAssignationToIndicator().stream().filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicatorWeb.getId().equals(dissagregationAssignationToIndicator.getId())).findFirst();
                dissagregationAssignationToIndicatorOp.ifPresent(dissagregationAssignationToIndicator ->
                {
                    if (!dissagregationAssignationToIndicator.getState().equals(dissagregationAssignationToIndicatorWeb.getState())) {
                        if (dissagregationAssignationToIndicatorWeb.getState().equals(State.ACTIVO)) {
                            dissagregationAssignationToIndicatorsToEnable.add(dissagregationAssignationToIndicator);
                        } else {
                            dissagregationAssignationToIndicatorsToDisable.add(dissagregationAssignationToIndicator);
                        }
                    }
                    dissagregationAssignationToIndicator.setState(dissagregationAssignationToIndicatorWeb.getState());
                });
            } else {
                // es nuevo
                DissagregationAssignationToIndicator da = new DissagregationAssignationToIndicator();
                da.setState(State.ACTIVO);
                da.setPeriod(this.modelWebTransformationService.periodWebToPeriod(dissagregationAssignationToIndicatorWeb.getPeriod()));
                da.setDissagregationType(dissagregationAssignationToIndicatorWeb.getDissagregationType());
                indicator.addDissagregationAssignationToIndicator(da);
                dissagregationAssignationToIndicatorsToCreate.add(da);
            }
        });
        // customdissagregationAssiment

        List<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicatorsToEnable = new ArrayList<>();
        List<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicatorsToDisable = new ArrayList<>();
        List<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicatorsToCreate = new ArrayList<>();

        indicatorWeb.getCustomDissagregationAssignationToIndicators().forEach(dissagregationAssignationToIndicatorWeb -> {
            if (dissagregationAssignationToIndicatorWeb.getId() != null) {
                Optional<CustomDissagregationAssignationToIndicator> dissagregationAssignationToIndicatorOp = indicator.getCustomDissagregationAssignationToIndicators().stream().filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicatorWeb.getId().equals(dissagregationAssignationToIndicator.getId())).findFirst();
                dissagregationAssignationToIndicatorOp.ifPresent(customDissagregationAssignationToIndicator -> {
                    if (!customDissagregationAssignationToIndicator.getState().equals(dissagregationAssignationToIndicatorWeb.getState())) {
                        if (dissagregationAssignationToIndicatorWeb.getState().equals(State.ACTIVO)) {
                            customDissagregationAssignationToIndicatorsToEnable.add(customDissagregationAssignationToIndicator);
                        } else {
                            customDissagregationAssignationToIndicatorsToDisable.add(customDissagregationAssignationToIndicator);
                        }
                    }
                    customDissagregationAssignationToIndicator.setState(dissagregationAssignationToIndicatorWeb.getState());

                });
            } else {
                // es nuevo
                CustomDissagregationAssignationToIndicator da = new CustomDissagregationAssignationToIndicator();
                da.setState(State.ACTIVO);
                da.setPeriod(this.modelWebTransformationService.periodWebToPeriod(dissagregationAssignationToIndicatorWeb.getPeriod()));
                da.setCustomDissagregation(this.modelWebTransformationService.customDissagregationWebToCustomDissagregation(dissagregationAssignationToIndicatorWeb.getCustomDissagregation()));
                indicator.addCustomDissagregationAssignationToIndicator(da);
                customDissagregationAssignationToIndicatorsToCreate.add(da);
            }
        });


        this.saveOrUpdate(indicator);
        //update dissagregations in ie
        this.indicatorExecutionService
                .updateIndicatorExecutionsDissagregations(
                        dissagregationAssignationToIndicatorsToEnable,
                        dissagregationAssignationToIndicatorsToDisable,
                        dissagregationAssignationToIndicatorsToCreate);
        this.indicatorExecutionService
                .updateIndicatorExecutionsCustomDissagregations(
                        customDissagregationAssignationToIndicatorsToEnable,
                        customDissagregationAssignationToIndicatorsToDisable,
                        customDissagregationAssignationToIndicatorsToCreate);
        return indicator.getId();
    }

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
        return this.modelWebTransformationService.indicatorsToIndicatorsWeb(this.indicatorDao.getByPeriodAssignmentAndState(periodId, state), true, true, true);
    }

    public List<Indicator> getByCodeList(List<String> codeList) {
        return this.indicatorDao.getByCodeList(codeList);
    }

    public void addDissagregationToIndicator(Indicator indicator, Period period, DissagregationType dissagregationType) throws GeneralAppException {
        DissagregationAssignationToIndicator dissagregationAssignationToIndicator = new DissagregationAssignationToIndicator();
        dissagregationAssignationToIndicator.setState(State.ACTIVO);
        dissagregationAssignationToIndicator.setDissagregationType(dissagregationType);
        dissagregationAssignationToIndicator.setPeriod(period);
        indicator.addDissagregationAssignationToIndicator(dissagregationAssignationToIndicator);
        this.saveOrUpdate(indicator);
        List<DissagregationAssignationToIndicator> toDisable = new ArrayList<>();
        List<DissagregationAssignationToIndicator> toEnable = new ArrayList<>();
        List<DissagregationAssignationToIndicator> toCreate = new ArrayList<>();
        toCreate.add(dissagregationAssignationToIndicator);
        this.indicatorExecutionService.updateIndicatorExecutionsDissagregations(
                toEnable, toDisable, toCreate);

    }

    public void dissableDissagregationsToIndicator(Indicator indicator, Period period, List<DissagregationType> dissagregationTypes) throws GeneralAppException {
        List<DissagregationAssignationToIndicator> toDisable =
                indicator.getDissagregationsAssignationToIndicator()
                        .stream()
                        .filter(
                                dissagregationAssignationToIndicator -> dissagregationTypes.contains(dissagregationAssignationToIndicator.getDissagregationType()) && dissagregationAssignationToIndicator.getPeriod().getId().equals(period.getId())
                        )
                        .collect(Collectors.toList());
        this.saveOrUpdate(indicator);
        // List<DissagregationAssignationToIndicator> toDisable = new ArrayList<>();
        List<DissagregationAssignationToIndicator> toEnable = new ArrayList<>();
        List<DissagregationAssignationToIndicator> toCreate = new ArrayList<>();
        this.indicatorExecutionService.updateIndicatorExecutionsDissagregations(
                toEnable, toDisable, toCreate);

    }


    public List<Indicator> getByPeriodYearAssignmentAndState(int year) {
        return this.indicatorDao.getByPeriodYearAssignmentAndState(year, State.ACTIVO);
    }

    public Indicator getByPeriodAndCode(Long periodId, String code) throws GeneralAppException {
        return this.indicatorDao.getByPeriodAndCode(periodId, code);
    }

    public Indicator getByCodeAndDescription(String code, String description) throws GeneralAppException {
        LOGGER.info(code+"-"+description);
        return this.indicatorDao.getByCodeAndDescription(code, description);
    }
}
