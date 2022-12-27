package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.services.StatementService;
import org.unhcr.osmosys.webServices.model.ImportFileWeb;
import org.unhcr.osmosys.webServices.model.StatementWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

@Path("/statements")
@RequestScoped
public class StatementEndpoint {
    private static final Logger LOGGER = Logger.getLogger(StatementEndpoint.class);
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

    @Path("/getStatementImportTemplate")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getStatementImportTemplate(
            @Context SecurityContext securityContext
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getStatementImportTemplate:" + principal.getName());

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final String filename = "statementImportTemplate.xlsx";
        InputStream template = classLoader.getResourceAsStream("templates" + File.separator + filename);


        try {
            return Response.ok(IOUtils.toByteArray(template)).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new GeneralAppException("Error al obtener el template " + filename, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/importStatementsCatalog")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response importStatementsCatalog(ImportFileWeb importFileWeb) throws GeneralAppException {
        LOGGER.debug(importFileWeb);
        this.statementService.importCatalog(importFileWeb);
        return null;
    }
}
