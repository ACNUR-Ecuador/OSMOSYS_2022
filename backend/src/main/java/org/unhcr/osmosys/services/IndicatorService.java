package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorDao;
import org.unhcr.osmosys.model.Indicator;
import org.unhcr.osmosys.webServices.model.IndicatorWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class IndicatorService {

    @Inject
    IndicatorDao indicatorDao;

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
        //this.validate(indicatorWeb);
       // Indicator indicator = this.saveOrUpdate(this.indicatorToIndicatorWeb(indicatorWeb));
        return null;// indicator.getId();
    }

    public List<IndicatorWeb> getAll() {
        List<IndicatorWeb> r = new ArrayList<>();
        return this.indicatorsToIndicatorsWeb(this.indicatorDao.findAll());
    }

    public List<IndicatorWeb> getByState(State state) {
        List<IndicatorWeb> r = new ArrayList<>();
        return this.indicatorsToIndicatorsWeb(this.indicatorDao.getByState(state));
    }

    public Long update(IndicatorWeb indicatorWeb) throws GeneralAppException {
        if (indicatorWeb == null) {
            throw new GeneralAppException("No se puede actualizar un indicator null", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un indicator sin id", Response.Status.BAD_REQUEST);
        }
        //this.validate(indicatorWeb);
        //Indicator indicator = this.saveOrUpdate(this.indicatorToIndicatorWeb(indicatorWeb));
        return null;// indicator.getId();
    }

    public List<IndicatorWeb> indicatorsToIndicatorsWeb(List<Indicator> indicators) {
        List<IndicatorWeb> r = new ArrayList<>();
        for (Indicator indicator : indicators) {
            //r.add(this.indicatorToIndicatorWeb(indicator));
        }
        return r;
    }
/*
    public IndicatorWeb indicatorToIndicatorWeb(Indicator indicator) {
        if (indicator == null) {
            return null;
        }
        IndicatorWeb indicatorWeb = new IndicatorWeb();
        indicatorWeb.setId(indicator.getId());
        indicatorWeb.setIndicatorType(indicator.getIndicatorType());
        indicatorWeb.setCode(indicator.getCode());
        indicatorWeb.setDefinition(indicator.getDefinition());
        indicatorWeb.setDescription(indicator.getDescription());
        indicatorWeb.setShortDescription(indicator.getShortDescription());
        indicatorWeb.setState(indicator.getState());

        return indicatorWeb;
    }

    public List<Indicator> indicatorsWebToIndicators(List<IndicatorWeb> indicatorsWebs) {
        List<Indicator> r = new ArrayList<>();
        for (IndicatorWeb indicatorWeb : indicatorsWebs) {
            r.add(this.indicatorToIndicatorWeb(indicatorWeb));
        }
        return r;
    }

    public Indicator indicatorToIndicatorWeb(IndicatorWeb indicatorWeb) {
        if (indicatorWeb == null) {
            return null;
        }
        Indicator indicator = new Indicator();
        indicator.setId(indicatorWeb.getId());
        indicator.setIndicatorType(indicatorWeb.getIndicatorType());
        indicator.setState(indicatorWeb.getState());
        indicator.setDescription(indicatorWeb.getDescription());
        indicator.setCode(indicatorWeb.getCode());
        indicator.setDefinition(indicatorWeb.getDefinition());
        indicator.setShortDescription(indicatorWeb.getShortDescription());
        return indicator;
    }

    public void validate(IndicatorWeb indicatorWeb) throws GeneralAppException {
        if (indicatorWeb == null) {
            throw new GeneralAppException("Oficina es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(indicatorWeb.getCode())) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(indicatorWeb.getShortDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(indicatorWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }
        if (indicatorWeb.getIndicatorType() == null) {
            throw new GeneralAppException("Tipo no válida", Response.Status.BAD_REQUEST);
        }

        Indicator itemRecovered = this.indicatorDao.getByCode(indicatorWeb.getCode());
        if (itemRecovered != null) {
            if (indicatorWeb.getId() == null || !indicatorWeb.getId().equals(itemRecovered.getId())){
                throw new GeneralAppException("Ya existe un área con este código", Response.Status.BAD_REQUEST);
            }
        }

        itemRecovered = this.indicatorDao.getByShortDescription(indicatorWeb.getShortDescription());
        if (itemRecovered != null) {
            if (indicatorWeb.getId() == null || !indicatorWeb.getId().equals(itemRecovered.getId())){
                throw new GeneralAppException("Ya existe un área con esta descripción corta", Response.Status.BAD_REQUEST);
            }
        }

    }*/
}
