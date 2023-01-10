package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.services.IndicatorService;
import org.unhcr.osmosys.services.dataImport.IndicatorsImportService;
import org.unhcr.osmosys.webServices.model.ImportFileWeb;
import org.unhcr.osmosys.webServices.model.IndicatorWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.List;

@Path("/indicators")
@RequestScoped
public class IndicatorEndpoint {

    private static final Logger LOGGER = Logger.getLogger(IndicatorEndpoint.class);

    @Inject
    IndicatorService indicatorService;

    @Inject
    IndicatorsImportService indicatorsImportService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(IndicatorWeb indicatorWeb) throws GeneralAppException {
        return this.indicatorService.save(indicatorWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(IndicatorWeb indicatorWeb) throws GeneralAppException {
        return this.indicatorService.update(indicatorWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorWeb> getAll() {
        return this.indicatorService.getAll();
    }

    @Path("/getByPeriodAssignmentAndState/{periodId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorWeb> getByPeriodAssignmentAndState(@PathParam("periodId") Long periodId) {
        return this.indicatorService.getByPeriodAssignmentAndState(periodId, State.ACTIVO);
    }


    @Path("/getImportTemplate/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getStatementImportTemplate(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getIndicatorsImportTemplate:" + principal.getName());
        String fileName = null;
        try {
            fileName = "implortador_indicadores_plantilla.xlsm";
            ByteArrayOutputStream template = this.indicatorsImportService.generateTemplate(periodId);
            return Response.ok(template.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + fileName + "\"").build();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new GeneralAppException("Error al obtener el template " + fileName, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("/importIndicatorsCatalog")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response importStatementsCatalog(ImportFileWeb importFileWeb) throws GeneralAppException {
        LOGGER.debug(importFileWeb);
        this.indicatorsImportService.indicatorsImport(importFileWeb);
        return null;
    }

}

