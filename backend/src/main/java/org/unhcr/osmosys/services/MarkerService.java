package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.MarkerDao;
import org.unhcr.osmosys.model.Marker;
import org.unhcr.osmosys.webServices.model.MarkerWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class MarkerService {

    @Inject
    MarkerDao markerDao;

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
        Marker marker = this.saveOrUpdate(this.markerWebToMarker(markerWeb));
        return marker.getId();
    }

    public List<MarkerWeb> getAll() {
        List<MarkerWeb> r = new ArrayList<>();
        return this.markersToMarkersWeb(this.markerDao.findAll());
    }

    public List<MarkerWeb> getByState(State state) {
        List<MarkerWeb> r = new ArrayList<>();
        return this.markersToMarkersWeb(this.markerDao.getByState(state));
    }

    public Long update(MarkerWeb markerWeb) throws GeneralAppException {
        if (markerWeb == null) {
            throw new GeneralAppException("No se puede actualizar un marker null", Response.Status.BAD_REQUEST);
        }
        if (markerWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un marker sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(markerWeb);
        Marker marker = this.saveOrUpdate(this.markerWebToMarker(markerWeb));
        return marker.getId();
    }

    public List<MarkerWeb> markersToMarkersWeb(List<Marker> markers) {
        List<MarkerWeb> r = new ArrayList<>();
        for (Marker marker : markers) {
            r.add(this.markerToMarkerWeb(marker));
        }
        return r;
    }

    public MarkerWeb markerToMarkerWeb(Marker marker) {
        if (marker == null) {
            return null;
        }
        MarkerWeb markerWeb = new MarkerWeb();
        markerWeb.setId(marker.getId());
        markerWeb.setSubType(marker.getSubType());
        markerWeb.setType(marker.getType());
        markerWeb.setDescription(marker.getDescription());
        markerWeb.setShortDescription(marker.getShortDescription());
        markerWeb.setState(marker.getState());
        return markerWeb;
    }

    public List<Marker> markersWebToMarkers(List<MarkerWeb> markersWebs) {
        List<Marker> r = new ArrayList<>();
        for (MarkerWeb markerWeb : markersWebs) {
            r.add(this.markerWebToMarker(markerWeb));
        }
        return r;
    }

    public Marker markerWebToMarker(MarkerWeb markerWeb) {
        if (markerWeb == null) {
            return null;
        }
        Marker marker = new Marker();
        marker.setId(markerWeb.getId());
        marker.setType(markerWeb.getType());
        marker.setSubType(markerWeb.getSubType());
        marker.setState(markerWeb.getState());
        marker.setDescription(markerWeb.getDescription());
        marker.setShortDescription(markerWeb.getShortDescription());
        return marker;
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
