package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.apache.commons.lang3.StringUtils;
import org.unhcr.osmosys.daos.OfficeDao;
import org.unhcr.osmosys.daos.OrganizactionDao;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.webServices.model.OfficeWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Stateless
public class OfficeService {

    @Inject
    OfficeDao officeDao;

    public Office getById(Long id) {
        return this.officeDao.find(id);
    }

    public Office create(OfficeWeb officeWeb) throws GeneralAppException {
        this.valida(officeWeb);
        Office office = new Office();
        office.setAcronym(officeWeb.getAcronym());
        office.setState(officeWeb.getState());
        office.setDescription(officeWeb.getDescription());
        office.setType(officeWeb.getType());
        Office parent = null;
        if (officeWeb.getParentOffice() != null) {
            parent = this.getById(officeWeb.getParentOffice().getId());
            parent.getChildOffices().add(office);
            office.setParentOffice(parent);
        }

        this.officeDao.save(office);
        if (parent != null) {
            this.officeDao.save(parent);
        }
        return office;
    }

    public void valida(OfficeWeb officeWeb) throws GeneralAppException {
        if (officeWeb == null) {
            throw new GeneralAppException("Oficina es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(officeWeb.getAcronym())) {
            throw new GeneralAppException("Acrónimo no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(officeWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válida", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getType() == null) {
            throw new GeneralAppException("Tipo no válida", Response.Status.BAD_REQUEST);
        }
    }

}
