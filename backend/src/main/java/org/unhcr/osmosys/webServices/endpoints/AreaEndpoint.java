package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.AreaService;
import org.unhcr.osmosys.webServices.model.AreaResumeWeb;
import org.unhcr.osmosys.webServices.model.AreaWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/areas")
@RequestScoped
public class AreaEndpoint {

    @Inject
    AreaService areaService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(AreaWeb areaWeb) throws GeneralAppException {
        return this.areaService.save(areaWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(AreaWeb areaWeb) throws GeneralAppException {
        return this.areaService.update(areaWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AreaWeb> getAll() {
        return this.areaService.getAll();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AreaWeb> getByState(@PathParam("state") State state) {
        return this.areaService.getByState(state);
    }

    @Path("/getDirectImplementationAreaResume")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AreaResumeWeb> getDirectImplementationAreaResume(
            @Context UriInfo uriInfo
    ) throws GeneralAppException {
        String userIdS = uriInfo.getQueryParameters().getFirst("userId");
        String periodIdS = uriInfo.getQueryParameters().getFirst("periodId");
        String officeIdS = uriInfo.getQueryParameters().getFirst("officeId");
        String supervisorS = uriInfo.getQueryParameters().getFirst("supervisor");
        String responsibleS = uriInfo.getQueryParameters().getFirst("responsible");
        String backupS = uriInfo.getQueryParameters().getFirst("backup");

        Long userId = null;
        Long periodId = null;
        Long officeId = null;
        boolean supervisor;
        boolean responsible;
        boolean backup;
        try {
            if (userIdS != null && !userIdS.equalsIgnoreCase("null")) {
                userId = Long.valueOf(userIdS);
            }
            if (officeIdS != null && !officeIdS.equalsIgnoreCase("null")) {
                officeId = Long.valueOf(officeIdS);
            }
            if (periodIdS != null && !periodIdS.equalsIgnoreCase("null")) {
                periodId = Long.valueOf(periodIdS);
            }
        } catch (NumberFormatException e) {
            throw new GeneralAppException("parametros incorrectos", Response.Status.BAD_REQUEST.getStatusCode());
        }
        supervisor = supervisorS != null && supervisorS.equalsIgnoreCase("true");
        responsible = supervisorS != null && responsibleS.equalsIgnoreCase("true");
        backup = backupS != null && backupS.equalsIgnoreCase("true");

        if (periodId == null) {
            throw new GeneralAppException("El periodo es obligatorio", Response.Status.BAD_REQUEST.getStatusCode());
        }
        if (officeId == null && userId == null) {
            throw new GeneralAppException("Es necesario seleccionar o una oficina o un usuario", Response.Status.BAD_REQUEST.getStatusCode());
        }
        if (userId != null && !responsible && !backup && !supervisor) {
            throw new GeneralAppException("Si selecciona un usuario, debe seleccionar al menos un rol", Response.Status.BAD_REQUEST.getStatusCode());
        }

        return this.areaService.getDirectImplementationAreaResume(userId, periodId, officeId, supervisor, responsible, backup);
    }

}
