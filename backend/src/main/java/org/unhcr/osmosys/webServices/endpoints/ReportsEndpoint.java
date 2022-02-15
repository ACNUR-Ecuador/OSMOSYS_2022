package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.unhcr.osmosys.reports.service.ReportService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Path("/reports")
@RequestScoped
public class ReportsEndpoint {

    @Inject
    ReportService reportService;

    @Path("/getAllPartnertsStateReport/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getFile(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.indicatorExecutionsToLateProjectsReportsByPeriodYear(periodId);

        String filename = "Reporte_estado_proyectos_" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }
}
