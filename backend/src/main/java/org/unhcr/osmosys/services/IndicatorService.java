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
import org.unhcr.osmosys.model.Marker;
import org.unhcr.osmosys.webServices.model.IndicatorWeb;
import org.unhcr.osmosys.webServices.model.MarkerWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

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

    private void indicatorWebToIndicator(IndicatorWeb indicatorWeb, Indicator indicator) {
        indicator.setCode(indicatorWeb.getCode());
        indicator.setDescription(indicatorWeb.getDescription());
        indicator.setCategory(indicatorWeb.getCategory());
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
        // statements
        indicator.setStatement(this.statementService.find(indicatorWeb.getId()));
        // todo actualizacion en valores y demás
        // dissagregationAssiment
        //
        indicatorWeb.getDissagregationsAssignationToIndicator().forEach(dissagregationAssignationToIndicatorWeb -> {
            if (dissagregationAssignationToIndicatorWeb.getId() != null) {
                Optional<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorOp = indicator.getDissagregationsAssignationToIndicator().stream().filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicatorWeb.getId().equals(dissagregationAssignationToIndicator.getId())).findFirst();
                dissagregationAssignationToIndicatorOp.ifPresent(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.setState(dissagregationAssignationToIndicatorWeb.getState()));
            } else {
                // es nuevo
                DissagregationAssignationToIndicator da = new DissagregationAssignationToIndicator();
                da.setState(State.ACTIVO);
                da.setPeriod(this.modelWebTransformationService.periodWebToPeriod(dissagregationAssignationToIndicatorWeb.getPeriod()));
                da.setDissagregationType(dissagregationAssignationToIndicatorWeb.getDissagregationType());
                indicator.addDissagregationAssignationToIndicator(da);
            }
        });
        // customdissagregationAssiment
        //
        indicatorWeb.getCustomDissagregationAssignationToIndicators().forEach(dissagregationAssignationToIndicatorWeb -> {
            if (dissagregationAssignationToIndicatorWeb.getId() != null) {
                Optional<CustomDissagregationAssignationToIndicator> dissagregationAssignationToIndicatorOp = indicator.getCustomDissagregationAssignationToIndicators().stream().filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicatorWeb.getId().equals(dissagregationAssignationToIndicator.getId())).findFirst();
                dissagregationAssignationToIndicatorOp.ifPresent(customDissagregationAssignationToIndicator -> customDissagregationAssignationToIndicator.setState(dissagregationAssignationToIndicatorWeb.getState()));
            } else {
                // es nuevo
                CustomDissagregationAssignationToIndicator da = new CustomDissagregationAssignationToIndicator();
                da.setState(State.ACTIVO);
                da.setPeriod(this.modelWebTransformationService.periodWebToPeriod(dissagregationAssignationToIndicatorWeb.getPeriod()));
                da.setCustomDissagregation(this.modelWebTransformationService.customDissagregationWebToCustomDissagregation(dissagregationAssignationToIndicatorWeb.getCustomDissagregation()));
                indicator.addCustomDissagregationAssignationToIndicator(da);
            }
        });

        indicator.setQualitativeInstructions(indicatorWeb.getQualitativeInstructions());
        this.saveOrUpdate(indicator);
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
}
