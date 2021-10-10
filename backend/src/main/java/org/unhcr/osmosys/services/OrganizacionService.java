package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.apache.commons.lang3.StringUtils;
import org.unhcr.osmosys.daos.OrganizactionDao;
import org.unhcr.osmosys.model.Organization;
import org.unhcr.osmosys.webServices.model.OrganizationWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Stateless
public class OrganizacionService {

    @Inject
    OrganizactionDao organizactionDao;

    public Organization getById(Long id) {
        return this.organizactionDao.find(id);
    }


    public Organization create(OrganizationWeb organizationWeb) throws GeneralAppException {
        this.validaOffice(organizationWeb);
        Organization organization = new Organization();
        organization.setCode(organizationWeb.getCode());
        organization.setAcronym(organizationWeb.getAcronym());
        organization.setState(organizationWeb.getState());
        organization.setDescription(organizationWeb.getDescription());
        this.organizactionDao.save(organization);
        return organization;
    }

    public void validaOffice(OrganizationWeb organizationWeb) throws GeneralAppException {
        if (organizationWeb == null) {
            throw new GeneralAppException("Organización es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(organizationWeb.getCode())) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(organizationWeb.getAcronym())) {
            throw new GeneralAppException("Acrónimo no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(organizationWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (organizationWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válida", Response.Status.BAD_REQUEST);
        }
    }

    public OrganizationWeb organizationToOrganizationWeb(Organization organization){
        if(organization==null){
            return null;
        }
        OrganizationWeb o = new OrganizationWeb();
        o.setId(organization.getId());
        o.setAcronym(organization.getAcronym());
        o.setCode(organization.getCode());
        o.setState(organization.getState());
        o.setDescription(organization.getDescription());
        return o;
    }

}
