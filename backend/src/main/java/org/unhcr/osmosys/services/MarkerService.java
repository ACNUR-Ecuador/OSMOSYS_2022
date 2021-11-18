package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.MarkerDao;
import org.unhcr.osmosys.model.Marker;
import org.unhcr.osmosys.webServices.model.MarkerWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

@Stateless
public class MarkerService {

    @Inject
    MarkerDao markerDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(MarkerService.class);

    public Marker getById(Long id) {
        return this.markerDao.find(id);
    }

    public Marker saveOrUpdate(Marker marker) {
        if (marker.getId() == null) {
            this.markerDao.save(marker);
        } else {
            this.markerDao.update(marker);
        }
        return marker;
    }

    public Long save(MarkerWeb markerWeb) throws GeneralAppException {
        if (markerWeb == null) {
            throw new GeneralAppException("No se puede guardar un marker null", Response.Status.BAD_REQUEST);
        }
        if (markerWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un marker con id", Response.Status.BAD_REQUEST);
        }
        this.validate(markerWeb);
        Marker marker = this.saveOrUpdate(this.modelWebTransformationService.markerWebToMarker(markerWeb));
        return marker.getId();
    }

    public List<MarkerWeb> getAll() {
        return this.modelWebTransformationService.markersToMarkersWeb(new HashSet<>());
    }

    public List<MarkerWeb> getByState(State state) {
        return this.modelWebTransformationService.markersToMarkersWeb(new HashSet<>(this.markerDao.getByState(state)));
    }

    public Long update(MarkerWeb markerWeb) throws GeneralAppException {
        if (markerWeb == null) {
            throw new GeneralAppException("No se puede actualizar un marker null", Response.Status.BAD_REQUEST);
        }
        if (markerWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un marker sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(markerWeb);
        Marker marker = this.saveOrUpdate(this.modelWebTransformationService.markerWebToMarker(markerWeb));
        return marker.getId();
    }

    public void validate(MarkerWeb markerWeb) throws GeneralAppException {
        if (markerWeb == null) {
            throw new GeneralAppException("Marcador es nulo", Response.Status.BAD_REQUEST);
        }

        if (markerWeb.getType() == null) {
            throw new GeneralAppException("Tipo no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(markerWeb.getSubType())) {
            throw new GeneralAppException("Subtipo no válida", Response.Status.BAD_REQUEST);
        }

        if (markerWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }

        Marker found = this.markerDao.getByTypeAndSubTypeAndShortDescription(markerWeb.getType(), markerWeb.getSubType(), markerWeb.getShortDescription());
        if(found!=null && !found.getId().equals(markerWeb.getId())){
            throw new GeneralAppException("Ya existe este marcador ", Response.Status.BAD_REQUEST);
        }

    }
}
