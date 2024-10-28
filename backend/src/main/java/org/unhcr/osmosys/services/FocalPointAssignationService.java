package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.FocalPointAssignationDao;
import org.unhcr.osmosys.model.FocalPointAssignation;
import org.unhcr.osmosys.webServices.model.FocalPointAssignationWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
public class FocalPointAssignationService {

    @Inject
    FocalPointAssignationDao focalPointAssignationDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(FocalPointAssignationService.class);



    public FocalPointAssignation saveOrUpdate(FocalPointAssignation focalPointAssignati) {
        if (focalPointAssignati.getId() == null) {
            this.focalPointAssignationDao.save(focalPointAssignati);
        } else {
            this.focalPointAssignationDao.update(focalPointAssignati);
        }
        return focalPointAssignati;
    }

    public Long save(FocalPointAssignationWeb focalPointAssignatiWeb) throws GeneralAppException {
        if (focalPointAssignatiWeb == null) {
            throw new GeneralAppException("No se puede guardar un pilar null", Response.Status.BAD_REQUEST);
        }
        if (focalPointAssignatiWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un pilar con id", Response.Status.BAD_REQUEST);
        }
        this.validate(focalPointAssignatiWeb);
        FocalPointAssignation focalPointAssignati = this.saveOrUpdate(this.modelWebTransformationService.focalPointerAssignationWebToFocalPointerAssignation(focalPointAssignatiWeb));
        return focalPointAssignati.getId();
    }

    public List<FocalPointAssignationWeb> getAll() {
        return this.modelWebTransformationService.focalPointerAssignationsToFocalPointerAssignationWeb(this.focalPointAssignationDao.findAll());
    }

    public List<FocalPointAssignationWeb> getWebByState(State state) {
        return this.modelWebTransformationService.focalPointerAssignationsToFocalPointerAssignationWeb(this.focalPointAssignationDao.getByState(state));
    }

    public List<FocalPointAssignation> getByState(State state) {
        return this.focalPointAssignationDao.getByState(state);
    }

    public Long update(FocalPointAssignationWeb focalPointAssignatiWeb) throws GeneralAppException {
        if (focalPointAssignatiWeb == null) {
            throw new GeneralAppException("No se puede actualizar un focalPointAssignati null", Response.Status.BAD_REQUEST);
        }
        if (focalPointAssignatiWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un focalPointAssignati sin id", Response.Status.BAD_REQUEST);
        }

        return 1L;
    }




    public void validate(FocalPointAssignationWeb focalPointAssignatiWeb) throws GeneralAppException {
        if (focalPointAssignatiWeb == null) {
            throw new GeneralAppException("Pilar es nulo", Response.Status.BAD_REQUEST);
        }

        if (focalPointAssignatiWeb.getFocalPointer() == null) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (focalPointAssignatiWeb.getMainFocalPointer() == null) {
            throw new GeneralAppException("Descripción corta no válida", Response.Status.BAD_REQUEST);
        }
        if (focalPointAssignatiWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }

    }
}
