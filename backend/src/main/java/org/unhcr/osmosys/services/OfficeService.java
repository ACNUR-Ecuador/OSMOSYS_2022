package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.OfficeDao;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.webServices.model.OfficeWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OfficeService {

    @Inject
    OfficeDao officeDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(OfficeService.class);

    public Office getById(Long id) {
        return this.officeDao.find(id);
    }

    public Office saveOrUpdate(Office office) {
        if (office.getId() == null) {
            this.officeDao.save(office);
        } else {
            this.officeDao.update(office);
        }
        return office;
    }

    public Long save(OfficeWeb officeWeb) throws GeneralAppException {
        if (officeWeb == null) {
            throw new GeneralAppException("No se puede guardar un pilar null", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un pilar con id", Response.Status.BAD_REQUEST);
        }
        this.validate(officeWeb);
        Office office = this.saveOrUpdate(this.modelWebTransformationService.officeWebToOffice(officeWeb));
        return office.getId();
    }

    public List<OfficeWeb> getAll(boolean returnChilds) {
        return this.modelWebTransformationService.officesToOfficesWeb(this.officeDao.findAll(), returnChilds);
    }

    public List<OfficeWeb> getByState(State state, boolean returnChilds) {
        return this.modelWebTransformationService.officesToOfficesWeb(this.officeDao.getByState(state), returnChilds);
    }

    public Long update(OfficeWeb officeWeb) throws GeneralAppException {
        if (officeWeb == null) {
            throw new GeneralAppException("No se puede actualizar un office null", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un office sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(officeWeb);
        Office office = this.saveOrUpdate(this.modelWebTransformationService.officeWebToOffice(officeWeb));
        return office.getId();
    }




    public void validate(OfficeWeb officeWeb) throws GeneralAppException {
        if (officeWeb == null) {
            throw new GeneralAppException("Pilar es nulo", Response.Status.BAD_REQUEST);
        }

        if (officeWeb.getDescription() == null) {
            throw new GeneralAppException("Descripción no válido", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getAcronym() == null) {
            throw new GeneralAppException("Acrónimo no válido", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getType() == null) {
            throw new GeneralAppException("Tipo no válido", Response.Status.BAD_REQUEST);
        }

        Office itemRecovered = this.officeDao.getByAcronym(officeWeb.getAcronym());
        if (itemRecovered != null) {
            if (officeWeb.getId() == null || !officeWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con este acrónimo", Response.Status.BAD_REQUEST);
            }
        }

        itemRecovered = this.officeDao.getByDescription(officeWeb.getDescription());
        if (itemRecovered != null) {
            if (officeWeb.getId() == null || !officeWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con esta descripción", Response.Status.BAD_REQUEST);
            }
        }
    }

    public List<OfficeWeb> getOfficeTree() {
        List<Office> offices = this.officeDao.getByNoParent();
        return this.modelWebTransformationService.officesToOfficesWeb(offices, true);
    }


    public List<OfficeWeb> getReportingOfficeByPeriodId(Long periodId) {
        List<Office> offices = this.officeDao.getReportingOfficeByPeriodId(periodId);
        return this.modelWebTransformationService.officesToOfficesWeb(offices, false);
    }
}
