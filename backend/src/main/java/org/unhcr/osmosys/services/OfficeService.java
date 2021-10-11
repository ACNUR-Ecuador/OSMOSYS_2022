package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.CustomPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.OfficeDao;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.webServices.model.OfficeWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public Office create(OfficeWeb officeWeb) throws GeneralAppException {
        LOGGER.error(this.securityContext.getCallerPrincipal());
        LOGGER.error(this.securityContext.getCallerPrincipal().getName());
        if(this.securityContext.getCallerPrincipal() instanceof  CustomPrincipal){
            CustomPrincipal principal = (CustomPrincipal) this.securityContext.getCallerPrincipal();
            LOGGER.info(principal);
            LOGGER.info(principal.getUser());
        }





        this.validate(officeWeb);
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

    public void validate(OfficeWeb officeWeb) throws GeneralAppException {
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

    public OfficeWeb officeToOfficeWeb(Office office, boolean returnChilds) {
        if (office == null) {
            return null;
        }
        OfficeWeb o = new OfficeWeb();
        o.setId(office.getId());
        o.setAcronym(office.getAcronym());
        o.setType(office.getType());
        o.setState(office.getState());
        o.setDescription(office.getDescription());
        o.setParentOffice(this.officeToOfficeWeb(office.getParentOffice(), false));
        if (returnChilds) {
            o.setChildOffices(this.officesToOfficesWeb(office.getChildOffices(), false));
        }
        return o;
    }

    public List<OfficeWeb> officesToOfficesWeb(Set<Office> offices, boolean returnChilds) {
        List<OfficeWeb> r = new ArrayList<>();
        for (Office o : offices) {
            this.officeToOfficeWeb(o, returnChilds);
        }
        return r;

    }
}
