package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorDao;
import org.unhcr.osmosys.model.Indicator;
import org.unhcr.osmosys.webServices.model.IndicatorWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class IndicatorService {

    @Inject
    IndicatorDao indicatorDao;

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
        Indicator indicator = this.saveOrUpdate(this.modelWebTransformationService.indicatorWebToIndicator(indicatorWeb));
        return indicator.getId();
    }

    public List<IndicatorWeb> getAll() {
        List<IndicatorWeb> r = new ArrayList<>();
        return this.modelWebTransformationService.indicatorsToIndicatorsWeb(this.indicatorDao.findAll());
    }

    public List<IndicatorWeb> getByState(State state) {
        List<IndicatorWeb> r = new ArrayList<>();
        return this.modelWebTransformationService.indicatorsToIndicatorsWeb(this.indicatorDao.getByState(state));
    }

    public Long update(IndicatorWeb indicatorWeb) throws GeneralAppException {
        if (indicatorWeb == null) {
            throw new GeneralAppException("No se puede actualizar un indicator null", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un indicator sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(indicatorWeb);
        Indicator indicator = this.saveOrUpdate(this.modelWebTransformationService.indicatorWebToIndicator(indicatorWeb));
        return indicator.getId();
    }

    public void validate(IndicatorWeb indicatorWeb) throws GeneralAppException {
        if (indicatorWeb == null) {
            throw new GeneralAppException("Oficina es nulo", Response.Status.BAD_REQUEST);
        }


        if (StringUtils.isBlank(indicatorWeb.getCode())) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(indicatorWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(indicatorWeb.getStatements())) {
            throw new GeneralAppException("No se ha asignado a ningún statement", Response.Status.BAD_REQUEST);
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
        if (indicatorWeb.getTotalIndicatorCalculationType() == null) {
            throw new GeneralAppException("Tipo de cálculo total no válido", Response.Status.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(indicatorWeb.getDissagregationsAssignationToIndicator())) {
            throw new GeneralAppException("No se ha asignado a ninguna desagregación", Response.Status.BAD_REQUEST);
        }
        Indicator itemRecovered = this.indicatorDao.getByCode(indicatorWeb.getCode());
        if (itemRecovered != null) {
            if (indicatorWeb.getId() == null || !indicatorWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un indicador con este código", Response.Status.BAD_REQUEST);
            }
        }

        itemRecovered = this.indicatorDao.getByDescription(indicatorWeb.getDescription());
        if (itemRecovered != null) {
            if (indicatorWeb.getId() == null || !indicatorWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un área con esta descripción", Response.Status.BAD_REQUEST);
            }
        }


    }
}
