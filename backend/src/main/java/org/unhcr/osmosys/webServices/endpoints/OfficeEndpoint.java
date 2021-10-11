package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.services.OfficeService;
import org.unhcr.osmosys.webServices.model.OfficeWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/office")
@RequestScoped
public class OfficeEndpoint {

    @Inject
    OfficeService officeService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(OfficeWeb officeWeb) throws GeneralAppException {
        Office office =this.officeService.create(officeWeb);
        return office.getId();
    }
}
