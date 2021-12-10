package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.GeneralIndicatorDao;
import org.unhcr.osmosys.model.DissagregationAssignationToGeneralIndicator;
import org.unhcr.osmosys.model.GeneralIndicator;
import org.unhcr.osmosys.webServices.model.DissagregationAssignationToGeneralIndicatorWeb;
import org.unhcr.osmosys.webServices.model.GeneralIndicatorWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class GeneralIndicatorService {

    @Inject
    GeneralIndicatorDao generalIndicatorDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(GeneralIndicatorService.class);

    public GeneralIndicator getById(Long id) {
        return this.generalIndicatorDao.find(id);
    }

    public GeneralIndicator saveOrUpdate(GeneralIndicator generalIndicator) {
        if (generalIndicator.getId() == null) {
            this.generalIndicatorDao.save(generalIndicator);
        } else {
            this.generalIndicatorDao.update(generalIndicator);
        }
        return generalIndicator;
    }

    public Long save(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        if (generalIndicatorWeb == null) {
            throw new GeneralAppException("No se puede guardar un generalIndicator null", Response.Status.BAD_REQUEST);
        }
        if (generalIndicatorWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un generalIndicator con id", Response.Status.BAD_REQUEST);
        }
        this.validate(generalIndicatorWeb);
        GeneralIndicator generalIndicator = this.saveOrUpdate(this.modelWebTransformationService.generalIndicatorWebToGeneralIndicator(generalIndicatorWeb));
        return generalIndicator.getId();
    }

    public List<GeneralIndicatorWeb> getAll() {
        List<GeneralIndicatorWeb> r = new ArrayList<>();
        return this.modelWebTransformationService.generalIndicatorsToGeneralIndicatorsWeb(this.generalIndicatorDao.findAll());
    }

    public List<GeneralIndicatorWeb> getByState(State state) {
        List<GeneralIndicatorWeb> r = new ArrayList<>();
        return this.modelWebTransformationService.generalIndicatorsToGeneralIndicatorsWeb(this.generalIndicatorDao.getByState(state));
    }


    public GeneralIndicatorWeb getByPeriodId(Long periodId) throws GeneralAppException {
        return this.modelWebTransformationService.generalIndicatorToGeneralIndicatorWeb(this.generalIndicatorDao.getByPeriodId( periodId));
    }

    public Long update(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        if (generalIndicatorWeb == null) {
            throw new GeneralAppException("No se puede actualizar un generalIndicator null", Response.Status.BAD_REQUEST);
        }
        if (generalIndicatorWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un generalIndicator sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(generalIndicatorWeb);
        GeneralIndicator generalfound = this.generalIndicatorDao.getByPeriodId(generalIndicatorWeb.getPeriod().getId());
        if(generalfound.getId().equals(generalIndicatorWeb.getId())){
            generalfound.setDescription(generalIndicatorWeb.getDescription());
            generalfound.setState( generalIndicatorWeb.getState());
            generalfound.setMeasureType(generalIndicatorWeb.getMeasureType());
            for (DissagregationAssignationToGeneralIndicatorWeb dissagregationAssignationToGeneralIndicatorWeb : generalIndicatorWeb.getDissagregationAssignationsToGeneralIndicator()) {
                if(dissagregationAssignationToGeneralIndicatorWeb.getState().equals(State.ACTIVO)){
                    Optional<DissagregationAssignationToGeneralIndicator> dissaFound = generalfound.getDissagregationAssignationsToGeneralIndicator().stream().filter(dissagregationAssignationToGeneralIndicator -> {
                        return dissagregationAssignationToGeneralIndicator.getDissagregationType().equals(dissagregationAssignationToGeneralIndicatorWeb.getDissagregationType());
                    }).findFirst();
                    if(dissaFound.isPresent()){
                        dissaFound.get().setState(State.ACTIVO);
                    }else {
                        DissagregationAssignationToGeneralIndicator dissanew = new DissagregationAssignationToGeneralIndicator();
                        dissanew.setState(State.ACTIVO);
                        dissanew.setDissagregationType(dissagregationAssignationToGeneralIndicatorWeb.getDissagregationType());
                        generalfound.addDissagregationAssignationsToGeneralIndicator(dissanew);
                    }
                }else{
                    Optional<DissagregationAssignationToGeneralIndicator> dissaFound = generalfound.getDissagregationAssignationsToGeneralIndicator().stream().filter(dissagregationAssignationToGeneralIndicator -> {
                        return dissagregationAssignationToGeneralIndicator.getDissagregationType().equals(dissagregationAssignationToGeneralIndicatorWeb.getDissagregationType());
                    }).findFirst();
                    if(dissaFound.isPresent()){
                        dissaFound.get().setState(State.INACTIVO);
                    }
                }
            }

            generalfound.getDissagregationAssignationsToGeneralIndicator().forEach(dissagregationAssignationToGeneralIndicator -> {
                Optional<DissagregationAssignationToGeneralIndicatorWeb> dissafound = generalIndicatorWeb.getDissagregationAssignationsToGeneralIndicator().stream().filter(dissagregationAssignationToGeneralIndicatorWeb -> {
                    return dissagregationAssignationToGeneralIndicator.getDissagregationType().equals(dissagregationAssignationToGeneralIndicatorWeb.getDissagregationType());
                }).findFirst();
                if(!dissafound.isPresent()){
                    dissafound.get().setState(State.INACTIVO);
                }
            });
        }
        this.saveOrUpdate(generalfound);
        return generalfound.getId();
    }


    public void validate(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        if (generalIndicatorWeb == null) {
            throw new GeneralAppException("Indicador es nulo", Response.Status.BAD_REQUEST);
        }


        if (StringUtils.isBlank(generalIndicatorWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (generalIndicatorWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }
        if (generalIndicatorWeb.getMeasureType() == null) {
            throw new GeneralAppException("Tipo medida no válida", Response.Status.BAD_REQUEST);
        }

        if (generalIndicatorWeb.getPeriod() == null) {
            throw new GeneralAppException("Periodo no válido", Response.Status.BAD_REQUEST);
        }

    }
}
