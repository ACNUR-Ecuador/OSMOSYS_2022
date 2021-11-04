package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.CustomDissagregationDao;
import org.unhcr.osmosys.model.CustomDissagregation;
import org.unhcr.osmosys.model.CustomDissagregationOption;
import org.unhcr.osmosys.webServices.model.CustomDissagregationOptionWeb;
import org.unhcr.osmosys.webServices.model.CustomDissagregationWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Stateless
public class CustomDissagregationService {

    @Inject
    CustomDissagregationDao customDissagregationDao;

    @Inject
    MarkerService markerService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(CustomDissagregationService.class);

    public CustomDissagregation getById(Long id) {
        return this.customDissagregationDao.find(id);
    }

    public CustomDissagregation saveOrUpdate(CustomDissagregation customDissagregation) {
        if (customDissagregation.getId() == null) {
            this.customDissagregationDao.save(customDissagregation);
        } else {
            this.customDissagregationDao.update(customDissagregation);
        }
        return customDissagregation;
    }

    public Long save(CustomDissagregationWeb customDissagregationWeb) throws GeneralAppException {
        if (customDissagregationWeb == null) {
            throw new GeneralAppException("No se puede guardar un customDissagregation null", Response.Status.BAD_REQUEST);
        }
        if (customDissagregationWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un customDissagregation con id", Response.Status.BAD_REQUEST);
        }
        this.validate(customDissagregationWeb);
        CustomDissagregation customDissagregation = this.saveOrUpdate(this.customDissagregationToCustomDissagregationWeb(customDissagregationWeb));
        return customDissagregation.getId();
    }

    public List<CustomDissagregationWeb> getAll() {
        List<CustomDissagregationWeb> r = new ArrayList<>();
        return this.customDissagregationsToCustomDissagregationsWeb(this.customDissagregationDao.findAll());
    }

    public List<CustomDissagregationWeb> getByState(State state) {
        List<CustomDissagregationWeb> r = new ArrayList<>();
        return this.customDissagregationsToCustomDissagregationsWeb(this.customDissagregationDao.getByState(state));
    }

    public Long update(CustomDissagregationWeb customDissagregationWeb) throws GeneralAppException {
        if (customDissagregationWeb == null) {
            throw new GeneralAppException("No se puede actualizar un customDissagregation null", Response.Status.BAD_REQUEST);
        }
        if (customDissagregationWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un customDissagregation sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(customDissagregationWeb);
        CustomDissagregation customDissagregation = this.saveOrUpdate(this.customDissagregationToCustomDissagregationWeb(customDissagregationWeb));
        return customDissagregation.getId();
    }

    public List<CustomDissagregationWeb> customDissagregationsToCustomDissagregationsWeb(List<CustomDissagregation> customDissagregations) {
        List<CustomDissagregationWeb> r = new ArrayList<>();
        for (CustomDissagregation customDissagregation : customDissagregations) {
            r.add(this.customDissagregationToCustomDissagregationWeb(customDissagregation));
        }
        return r;
    }

    public CustomDissagregationWeb customDissagregationToCustomDissagregationWeb(CustomDissagregation customDissagregation) {
        if (customDissagregation == null) {
            return null;
        }
        CustomDissagregationWeb customDissagregationWeb = new CustomDissagregationWeb();
        customDissagregationWeb.setId(customDissagregation.getId());
        customDissagregationWeb.setControlTotalValue(customDissagregation.getControlTotalValue());
        customDissagregationWeb.setDescription(customDissagregation.getDescription());
        customDissagregationWeb.setName(customDissagregation.getName());
        customDissagregationWeb.setState(customDissagregation.getState());
        return customDissagregationWeb;
    }

    public List<CustomDissagregationOptionWeb> customDissagregationOptionsToCustomDissagregationOptionsWeb(List<CustomDissagregationOption> customDissagregationOptions) {
        List<CustomDissagregationOptionWeb> r = new ArrayList<>();
        for (CustomDissagregationOption customDissagregationOption : customDissagregationOptions) {
            r.add(this.customDissagregationOptionToCustomDissagregationOptionWeb(customDissagregationOption));
        }
        return r;
    }


    public List<CustomDissagregation> customDissagregationsWebToCustomDissagregations(List<CustomDissagregationWeb> customDissagregationsWebs) {
        List<CustomDissagregation> r = new ArrayList<>();
        for (CustomDissagregationWeb customDissagregationWeb : customDissagregationsWebs) {
            r.add(this.customDissagregationToCustomDissagregationWeb(customDissagregationWeb));
        }
        return r;
    }

    public CustomDissagregation customDissagregationToCustomDissagregationWeb(CustomDissagregationWeb customDissagregationWeb) {
        if (customDissagregationWeb == null) {
            return null;
        }
        CustomDissagregation customDissagregation = new CustomDissagregation();
        customDissagregation.setId(customDissagregationWeb.getId());
        customDissagregation.setName(customDissagregationWeb.getName());
        customDissagregation.setState(customDissagregationWeb.getState());
        customDissagregation.setDescription(customDissagregationWeb.getDescription());
        customDissagregation.setControlTotalValue(customDissagregationWeb.getControlTotalValue());

        return customDissagregation;
    }

    public List<CustomDissagregationOption> customDissagregationOptionsWebToCustomDissagregationOptions(List<CustomDissagregationOptionWeb> customDissagregationOptionsWeb) {
        List<CustomDissagregationOption> r = new ArrayList<>();
        for (CustomDissagregationOptionWeb customDissagregationOptionWeb : customDissagregationOptionsWeb) {
            r.add(this.customDissagregationOptionWebToCustomDissagregationOption(customDissagregationOptionWeb));
        }
        return r;
    }
    public CustomDissagregationOptionWeb customDissagregationOptionToCustomDissagregationOptionWeb(CustomDissagregationOption customDissagregationOption) {
        if (customDissagregationOption == null) {
            return null;
        }
        CustomDissagregationOptionWeb customDissagregationOptionWeb = new CustomDissagregationOptionWeb();
        customDissagregationOptionWeb.setId(customDissagregationOption.getId());
        customDissagregationOptionWeb.setName(customDissagregationOption.getName());
        customDissagregationOptionWeb.setState(customDissagregationOption.getState());
        customDissagregationOptionWeb.setDescription(customDissagregationOption.getDescription());
        customDissagregationOptionWeb.setMarkers(this.markerService.markersToMarkersWeb(new ArrayList<>(customDissagregationOption.getMarkers())));
        return customDissagregationOptionWeb;
    }

    public CustomDissagregationOption customDissagregationOptionWebToCustomDissagregationOption(CustomDissagregationOptionWeb customDissagregationOptionWeb) {
        if (customDissagregationOptionWeb == null) {
            return null;
        }
        CustomDissagregationOption customDissagregationOption = new CustomDissagregationOption();
        customDissagregationOption.setId(customDissagregationOptionWeb.getId());
        customDissagregationOption.setName(customDissagregationOptionWeb.getName());
        customDissagregationOption.setState(customDissagregationOptionWeb.getState());
        customDissagregationOption.setDescription(customDissagregationOptionWeb.getDescription());
        customDissagregationOption.setMarkers(new HashSet<>(this.markerService.markersWebToMarkers(new ArrayList<>(customDissagregationOptionWeb.getMarkers()))));
        return customDissagregationOption;
    }

    public void validate(CustomDissagregationWeb customDissagregationWeb) throws GeneralAppException {
        if (customDissagregationWeb == null) {
            throw new GeneralAppException("Desagregación es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(customDissagregationWeb.getName())) {
            throw new GeneralAppException("Nombre no válido", Response.Status.BAD_REQUEST);
        }
        if (customDissagregationWeb.getControlTotalValue()==null) {
            throw new GeneralAppException("Control de valor no válido", Response.Status.BAD_REQUEST);
        }

        if (customDissagregationWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }
        if (customDissagregationWeb.getCustomDissagregationOptions() == null || CollectionUtils.isEmpty(customDissagregationWeb.getCustomDissagregationOptions())) {
            throw new GeneralAppException("Debe al menos tener una opción", Response.Status.BAD_REQUEST);
        }

        CustomDissagregation itemRecovered = this.customDissagregationDao.getByName(customDissagregationWeb.getName());
        if (itemRecovered != null) {
            if (customDissagregationWeb.getId() == null || !customDissagregationWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe una desagregación con este nombre", Response.Status.BAD_REQUEST);
            }
        }

        itemRecovered = this.customDissagregationDao.getByName(customDissagregationWeb.getName());
        if (itemRecovered != null) {
            if (customDissagregationWeb.getId() == null || !customDissagregationWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un área con esta descripción corta", Response.Status.BAD_REQUEST);
            }
        }

    }
}
