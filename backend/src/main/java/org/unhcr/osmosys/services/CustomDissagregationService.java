package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.CustomDissagregationDao;
import org.unhcr.osmosys.model.CustomDissagregation;
import org.unhcr.osmosys.webServices.model.CustomDissagregationOptionWeb;
import org.unhcr.osmosys.webServices.model.CustomDissagregationWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class CustomDissagregationService {

    @Inject
    CustomDissagregationDao customDissagregationDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(CustomDissagregationService.class);

    public CustomDissagregation find(Long id) {
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
        CustomDissagregation customDissagregation = this.saveOrUpdate(this.modelWebTransformationService.customDissagregationWebToCustomDissagregation(customDissagregationWeb));
        return customDissagregation.getId();
    }

    public List<CustomDissagregationWeb> getAll() {
        return this.modelWebTransformationService.customDissagregationsToCustomDissagregationsWeb(this.customDissagregationDao.findAll());
    }

    public List<CustomDissagregationWeb> getByState(State state) {
        return this.modelWebTransformationService.customDissagregationsToCustomDissagregationsWeb(this.customDissagregationDao.getByState(state));
    }

    public Long update(CustomDissagregationWeb customDissagregationWeb) throws GeneralAppException {
        if (customDissagregationWeb == null) {
            throw new GeneralAppException("No se puede actualizar un customDissagregation null", Response.Status.BAD_REQUEST);
        }
        if (customDissagregationWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un customDissagregation sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(customDissagregationWeb);
        CustomDissagregation customDissagregation = this.saveOrUpdate(this.modelWebTransformationService.customDissagregationWebToCustomDissagregation(customDissagregationWeb));
        return customDissagregation.getId();
    }











    public void validate(CustomDissagregationWeb customDissagregationWeb) throws GeneralAppException {
        if (customDissagregationWeb == null) {
            throw new GeneralAppException("Desagregación es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(customDissagregationWeb.getName())) {
            throw new GeneralAppException("Nombre no válido", Response.Status.BAD_REQUEST);
        }
        if (customDissagregationWeb.getControlTotalValue() == null) {
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

        this.validateOptions(customDissagregationWeb.getCustomDissagregationOptions());

    }

    private void validateOptions(List<CustomDissagregationOptionWeb> customDissagregationOptions) throws GeneralAppException {
        if (CollectionUtils.isEmpty(customDissagregationOptions)) {
            throw new GeneralAppException("No tiene opciones", Response.Status.BAD_REQUEST);
        }
        Map<String, Long> counting = customDissagregationOptions.stream().collect(
                Collectors.groupingBy(CustomDissagregationOptionWeb::getName,
                        Collectors.counting()));
        for(String optionName:counting.keySet()){
            if(counting.get(optionName)>1) {
                throw new GeneralAppException("La opción " + optionName + " está repetida", Response.Status.BAD_REQUEST);
            }
        }

        for (CustomDissagregationOptionWeb customDissagregationOption : customDissagregationOptions) {
            if (StringUtils.isBlank(customDissagregationOption.getName())) {
                throw new GeneralAppException("El nombre de la opción es obligatorio", Response.Status.BAD_REQUEST);
            }
            if (customDissagregationOption.getState() == null) {
                throw new GeneralAppException("El estado de la opción es obligatorio", Response.Status.BAD_REQUEST);
            }



        }


    }

    public CustomDissagregation getByName(String name) {
        return this.customDissagregationDao.getByName(name);
    }
}
