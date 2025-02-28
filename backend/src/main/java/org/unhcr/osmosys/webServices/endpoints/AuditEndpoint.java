package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.AuditService;
import org.unhcr.osmosys.webServices.model.AreaWeb;
import org.unhcr.osmosys.webServices.model.AuditWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/audits")
@RequestScoped
public class AuditEndpoint {
    @Inject
    AuditService auditService;

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(AuditWeb auditWeb) throws GeneralAppException {
        return this.auditService.save(auditWeb);
    }

    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(AuditWeb auditWeb) throws GeneralAppException {
        return this.auditService.update(auditWeb);
    }

    @Path("/")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuditWeb> getAll() {
        return this.auditService.getAll();
    }

    @Path("/byState/{state}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuditWeb> getByState(@PathParam("state") State state) {
        return this.auditService.getByState(state);
    }

    @Path("/getAuditsByTableName/{tableName}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuditWeb> getByTableName(@PathParam("tableName") String tableName) {
        return auditService.getAuditsByTableName(tableName);
    }

    @Path("/getAuditsByTableNameAndDate/{tableName}/{year}/{month}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuditWeb> getByTableNameAndDate(@PathParam("tableName") String tableName,
                                                @PathParam("year") int year,
                                                @PathParam("month") int month) {
        return auditService.getAuditsByTableNamePeriodAndMonth(tableName, year, month);
    }




}
