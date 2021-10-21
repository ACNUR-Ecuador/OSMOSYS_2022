package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.OfficeDao;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.webServices.model.OfficeWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OfficeService {

    @Inject
    OfficeDao officeDao;

    @Inject
    SecurityContext securityContext;

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
        Office office = this.saveOrUpdate(this.officeWebToOffice(officeWeb));
        return office.getId();
    }

    public List<OfficeWeb> getAll(boolean returnChilds) {
        List<OfficeWeb> r = new ArrayList<>();
        return this.officesToOfficesWeb(this.officeDao.findAll(), returnChilds);
    }

    public List<OfficeWeb> getByState(State state, boolean returnChilds) {
        List<OfficeWeb> r = new ArrayList<>();
        return this.officesToOfficesWeb(this.officeDao.getByState(state), returnChilds);
    }

    public Long update(OfficeWeb officeWeb) throws GeneralAppException {
        if (officeWeb == null) {
            throw new GeneralAppException("No se puede actualizar un office null", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un office sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(officeWeb);
        Office office = this.saveOrUpdate(this.officeWebToOffice(officeWeb));
        return office.getId();
    }

    public List<OfficeWeb> officesToOfficesWeb(List<Office> offices, boolean returnChilds) {
        List<OfficeWeb> r = new ArrayList<>();
        for (Office office : offices) {
            r.add(this.officeToOfficeWeb(office, returnChilds));
        }
        return r;
    }

    public OfficeWeb officeToOfficeWeb(Office office, boolean returnChilds) {
        if (office == null) {
            return null;
        }
        OfficeWeb officeWeb = new OfficeWeb();
        officeWeb.setId(office.getId());
        officeWeb.setDescription(office.getDescription());
        officeWeb.setAcronym(office.getAcronym());
        officeWeb.setType(office.getType());
        officeWeb.setState(office.getState());
        officeWeb.setParentOffice(this.officeToOfficeWeb(office.getParentOffice(), false));
        if (returnChilds) {
            officeWeb.setChildOffices(this.officesToOfficesWeb(new ArrayList<>(office.getChildOffices()), returnChilds));
        }

        return officeWeb;
    }

    public List<Office> officesWebToOffices(List<OfficeWeb> officesWebs) {
        List<Office> r = new ArrayList<>();
        for (OfficeWeb officeWeb : officesWebs) {
            r.add(this.officeWebToOffice(officeWeb));
        }
        return r;
    }

    public Office officeWebToOffice(OfficeWeb officeWeb) {
        if (officeWeb == null) {
            return null;
        }
        Office office = new Office();
        office.setId(officeWeb.getId());
        office.setDescription(officeWeb.getDescription());
        office.setAcronym(officeWeb.getAcronym());
        office.setType(officeWeb.getType());
        office.setState(officeWeb.getState());
        office.setParentOffice(this.officeWebToOffice(officeWeb.getParentOffice()));
        // office.setChildOffices(this.officesToOfficesWeb(office.getChildOffices()));
        return office;
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

        return this.officesToOfficesWeb(offices, true);
    }


}
