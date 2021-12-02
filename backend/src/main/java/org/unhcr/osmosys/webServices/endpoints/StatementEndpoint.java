package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.StatementService;
import org.unhcr.osmosys.webServices.model.StatementWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/statements")
@RequestScoped
public class StatementEndpoint {

    @Inject
    StatementService statementService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(StatementWeb statementWeb) throws GeneralAppException {
        return this.statementService.save(statementWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(StatementWeb statementWeb) throws GeneralAppException {
        return this.statementService.update(statementWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StatementWeb> getAll() {
        return this.statementService.getAll();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StatementWeb> getByState(@PathParam("state") State state) {
        return this.statementService.getByState(state);
    }
}
