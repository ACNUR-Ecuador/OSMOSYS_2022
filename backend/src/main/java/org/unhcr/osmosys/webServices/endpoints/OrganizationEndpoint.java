package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.model.Organization;
import org.unhcr.osmosys.services.OrganizacionService;
import org.unhcr.osmosys.webServices.model.OfficeWeb;
import org.unhcr.osmosys.webServices.model.OrganizationWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/organization")
@RequestScoped
public class OrganizationEndpoint {

    @Inject
    OrganizacionService organizacionService;

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(OrganizationWeb organizationWeb) throws GeneralAppException {
        Organization organization =this.organizacionService.create(organizationWeb);
        return organization.getId();
    }
}
