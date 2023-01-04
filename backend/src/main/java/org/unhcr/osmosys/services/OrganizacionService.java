package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.StringUtils;
import org.unhcr.osmosys.daos.OrganizationDao;
import org.unhcr.osmosys.model.Organization;
import org.unhcr.osmosys.webServices.model.OrganizationWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
public class OrganizacionService {

    @Inject
    OrganizationDao organizationDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    public Organization getById(Long id) {
        return this.organizationDao.find(id);
    }

    public Organization saveOrUpdate(Organization organization) {
        if (organization.getId() == null) {
            this.organizationDao.save(organization);
        } else {
            this.organizationDao.update(organization);
        }
        return organization;
    }

    public Long save(OrganizationWeb organizationWeb) throws GeneralAppException {
        if (organizationWeb == null) {
            throw new GeneralAppException("No se puede guardar un organization null", Response.Status.BAD_REQUEST);
        }
        if (organizationWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un organization con id", Response.Status.BAD_REQUEST);
        }
        this.validate(organizationWeb);
        Organization organization = this.saveOrUpdate(this.modelWebTransformationService.organizationWebToOrganization(organizationWeb));
        return organization.getId();
    }

    public Long update(OrganizationWeb organizationWeb) throws GeneralAppException {
        if (organizationWeb == null) {
            throw new GeneralAppException("No se puede actualizar una organización null", Response.Status.BAD_REQUEST);
        }
        if (organizationWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear una organización sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(organizationWeb);
        Organization organization = this.saveOrUpdate(this.modelWebTransformationService.organizationWebToOrganization(organizationWeb));
        return organization.getId();
    }

    public List<OrganizationWeb> getAll() {
        return this.modelWebTransformationService.organizationsToOrganizationsWeb(this.organizationDao.findAll());
    }


    public void validate(OrganizationWeb organizationWeb) throws GeneralAppException {
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

        Organization itemRecovered = this.organizationDao.getByCode(organizationWeb.getCode());
        if (itemRecovered != null) {
            if (organizationWeb.getId() == null || !organizationWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con este código", Response.Status.BAD_REQUEST);
            }
        }

        itemRecovered = this.organizationDao.getByAcronym(organizationWeb.getAcronym());
        if (itemRecovered != null) {
            if (organizationWeb.getId() == null || !organizationWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con esta descripción corta", Response.Status.BAD_REQUEST);
            }
        }
        itemRecovered = this.organizationDao.getByDescription(organizationWeb.getDescription());
        if (itemRecovered != null) {
            if (organizationWeb.getId() == null || !organizationWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con esta descripción", Response.Status.BAD_REQUEST);
            }
        }
    }


    public List<OrganizationWeb> getByState(State state) {
        return this.modelWebTransformationService.organizationsToOrganizationsWeb(this.organizationDao.getByState(state));
    }

    public Organization getByAcronym(String acronym) throws GeneralAppException {
        return this.organizationDao.getByAcronym(acronym);
    }
}
