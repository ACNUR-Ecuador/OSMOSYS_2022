package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.model.Organization;
import org.unhcr.osmosys.services.OrganizacionService;
import org.unhcr.osmosys.webServices.model.AreaWeb;
import org.unhcr.osmosys.webServices.model.OfficeWeb;
import org.unhcr.osmosys.webServices.model.OrganizationWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/organizations")
@RequestScoped
public class OrganizationEndpoint {

    @Inject
    OrganizacionService organizacionService;

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(OrganizationWeb organizationWeb) throws GeneralAppException {
        return this.organizacionService.save(organizationWeb);
    }


    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(OrganizationWeb organizationWeb) throws GeneralAppException {
        return this.organizacionService.update(organizationWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganizationWeb> getAll() {
        return this.organizacionService.getAll();
    }
}
