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

    @Path("/indicatorsCatalogByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response indicatorsCatalogByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.indicatorsCatalogByPeriodId(periodId);

        String filename = "Catalogo_indicadores_" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/indicatorsCatalogWithImplementersSimple/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response indicatorsCatalogWithImplementersSimpleByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.indicatorsCatalogWithImplementersSimpleByPeriodId(periodId);

        String filename = "Catalogo_indicadores_" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/indicatorsCatalogWithImplementersDetailed/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response indicatorsCatalogWithImplementersDetailedByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.indicatorsCatalogWithImplementersDetailedByPeriodId(periodId);

        String filename = "Catalogo_indicadores_" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllImplementationsAnnualByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsAnnualByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getAllImplementationsAnnualByPeriodId(periodId);

        String filename = "Exportacion_datos_total_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllImplementationsQuarterlyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsQuarterlyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getAllImplementationsQuarterlyByPeriodId(periodId);

        String filename = "Exportacion_datos_total_trimestral" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllImplementationsMonthlylyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsMonthlylyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getAllImplementationsMonthlylyByPeriodId(periodId);

        String filename = "Exportacion_datos_total_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllImplementationsDetailedlyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsDetailedlyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getAllImplementationsDetailedlyByPeriodId(periodId);

        String filename = "Exportacion_datos_total_adetallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllImplementationsPerformanceIndicatorsAnualByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsPerformanceIndicatorsAnualByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getAllImplementationsPerformanceIndicatorsAnualByPeriodId(periodId);

        String filename = "Exportacion_datos_indicadores_producto_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }


    @Path("/getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId(periodId);

        String filename = "Exportacion_datos_indicadores_producto_trimestral" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }


    @Path("/getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId(periodId);

        String filename = "Exportacion_datos_indicadores_producto_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }


    @Path("/getAllImplementationsPerformanceIndicatorsDetailedByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsPerformanceIndicatorsDetailedByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getAllImplementationsPerformanceIndicatorsDetailedByPeriodId(periodId);

        String filename = "Exportacion_datos_indicadores_producto_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersAnualByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersAnualByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnersAnualByPeriodId(periodId);

        String filename = "Exportacion_datos_socios_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }
    @Path("/getPartnersQuarterlyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersQuarterlyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnersQuarterlyByPeriodId(periodId);

        String filename = "Exportacion_datos_socios_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }
    @Path("/getPartnersMonthlyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersMonthlyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnersMonthlyByPeriodId(periodId);

        String filename = "Exportacion_datos_socios_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersDetailedByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersDetailedByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnersDetailedByPeriodId(periodId);

        String filename = "Exportacion_datos_socios_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersGeneralIndicatorsAnualByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersGeneralIndicatorsAnualByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnersGeneralIndicatorsAnualByPeriodId(periodId);

        String filename = "Exportacion_datos_socios_igeneral_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }


    @Path("/getPartnersGeneralIndicatorsMonthlyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersGeneralIndicatorsMonthlyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnersGeneralIndicatorsMonthlyByPeriodId(periodId);

        String filename = "Exportacion_datos_socios_igeneral_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }


    @Path("/getPartnersGeneralIndicatorsDetailedByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersGeneralIndicatorsDetailedByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnersGeneralIndicatorsDetailedByPeriodId(periodId);

        String filename = "Exportacion_datos_socios_igeneral_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersPerformanceIndicatorsAnualByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersPerformanceIndicatorsAnualByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnersPerformanceIndicatorsAnualByPeriodId(periodId);

        String filename = "Exportacion_datos_socios_iproducto_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }
    @Path("/getPartnersPerformanceIndicatorsQuarterlyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersPerformanceIndicatorsQuarterlyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnersPerformanceIndicatorsQuarterlyByPeriodId(periodId);

        String filename = "Exportacion_datos_socios_iproducto_trimestral" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }
    @Path("/getPartnersPerformanceIndicatorsMonthlyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersPerformanceIndicatorsMonthlyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnersPerformanceIndicatorsMonthlyByPeriodId(periodId);

        String filename = "Exportacion_datos_socios_iproducto_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }
    @Path("/getPartnersPerformanceIndicatorsDetailedByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersPerformanceIndicatorsDetailedByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnersPerformanceIndicatorsDetailedByPeriodId(periodId);

        String filename = "Exportacion_datos_socios_iproducto_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnerAnualByProjectId/{projectId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnerAnualByProjectId(
            @PathParam("projectId") Long projectId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnerAnualByProjectId(projectId);

        String filename = "Exportacion_datos_socio_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnerQuarterlyByProjectId/{projectId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnerQuarterlyByProjectId(
            @PathParam("projectId") Long projectId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnerQuarterlyByProjectId(projectId);

        String filename = "Exportacion_datos_socio_trimestral" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnerMonthlyByProjectId/{projectId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnerMonthlyByProjectId(
            @PathParam("projectId") Long projectId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnerMonthlyByProjectId(projectId);

        String filename = "Exportacion_datos_socio_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnerDetailedByProjectId/{projectId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnerDetailedByProjectId(
            @PathParam("projectId") Long projectId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getPartnerDetailedByProjectId(projectId);

        String filename = "Exportacion_datos_socio_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getDirectImplementationPerformanceIndicatorsAnualByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getDirectImplementationPerformanceIndicatorsAnualByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getDirectImplementationPerformanceIndicatorsAnualByPeriodId(periodId);

        String filename = "Exportacion_datos_implementacion_directa_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId(periodId);

        String filename = "Exportacion_datos_implementacion_directa_trimestral" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId(periodId);

        String filename = "Exportacion_datos_implementacion_directa_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getDirectImplementationPerformanceIndicatorsDetailedByPeriodId/{periodId}")
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {

        ByteArrayOutputStream r = this.reportService.getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(periodId);

        String filename = "Exportacion_datos_implementacion_directa_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }


}
