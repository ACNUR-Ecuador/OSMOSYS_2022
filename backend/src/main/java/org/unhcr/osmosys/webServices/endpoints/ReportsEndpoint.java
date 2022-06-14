package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.reports.service.ReportService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Path("/reports")
@RequestScoped
public class ReportsEndpoint {
    private static final Logger LOGGER = Logger.getLogger(ReportsEndpoint.class);
    @Inject
    ReportService reportService;

    @Path("/getAllPartnertsStateReport/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getFile(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getAllPartnertsStateReport:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.indicatorExecutionsToLateProjectsReportsByPeriodYear(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Reporte_estado_proyectos_" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("" +
            "/indicatorsCatalogByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response indicatorsCatalogByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("indicatorsCatalogByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.indicatorsCatalogByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Catalogo_indicadores_" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/indicatorsCatalogWithImplementersSimple/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response indicatorsCatalogWithImplementersSimpleByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("indicatorsCatalogWithImplementersSimple:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.indicatorsCatalogWithImplementersSimpleByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Catalogo_indicadores_detalle_" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/indicatorsCatalogWithImplementersDetailed/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response indicatorsCatalogWithImplementersDetailedByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("indicatorsCatalogWithImplementersDetailed:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.indicatorsCatalogWithImplementersDetailedByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Catalogo_indicadores_detalle__" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllImplementationsAnnualByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsAnnualByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getAllImplementationsAnnualByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getAllImplementationsAnnualByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_total_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllImplementationsQuarterlyByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsQuarterlyByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getAllImplementationsQuarterlyByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getAllImplementationsQuarterlyByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_total_trimestral" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllImplementationsMonthlyByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsMonthlylyByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getAllImplementationsMonthlyByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getAllImplementationsMonthlyByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_total_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllImplementationsDetailedByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsDetailedByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getAllImplementationsDetailedByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getAllImplementationsDetailedByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_total_adetallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllImplementationsPerformanceIndicatorsAnnualByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsPerformanceIndicatorsAnnualByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getAllImplementationsPerformanceIndicatorsAnnualByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getAllImplementationsPerformanceIndicatorsAnnualByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_indicadores_producto_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }


    @Path("/getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_indicadores_producto_trimestral" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }


    @Path("/getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_indicadores_producto_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllImplementationsPerformanceIndicatorsDetailedByPeriodId/{periodId}")
    @GET
    //@Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllImplementationsPerformanceIndicatorsDetailedByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        //LOGGER.info("getAllImplementationsPerformanceIndicatorsDetailedByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getAllImplementationsPerformanceIndicatorsDetailedByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_indicadores_producto_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersAnnualByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersAnnualByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnersAnnualByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnersAnnualByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socios_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersQuarterlyByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersQuarterlyByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnersQuarterlyByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnersQuarterlyByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socios_trimestral" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersMonthlyByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersMonthlyByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnersMonthlyByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnersMonthlyByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socios_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }


    @Path("/getPartnersDetailedByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersDetailedByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnersDetailedByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnersDetailedByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socios_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }


    @Path("/getPartnersGeneralIndicatorsAnnualByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersGeneralIndicatorsAnnualByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnersGeneralIndicatorsAnnualByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnersGeneralIndicatorsAnnualByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socios_igeneral_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }


    @Path("/getPartnersGeneralIndicatorsMonthlyByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersGeneralIndicatorsMonthlyByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnersGeneralIndicatorsMonthlyByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnersGeneralIndicatorsMonthlyByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socios_igeneral_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersGeneralIndicatorsDetailedByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersGeneralIndicatorsDetailedByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnersGeneralIndicatorsDetailedByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnersGeneralIndicatorsDetailedByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socios_igeneral_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersPerformanceIndicatorsAnnualByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersPerformanceIndicatorsAnnualByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnersPerformanceIndicatorsAnnualByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnersPerformanceIndicatorsAnnualByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socios_iproducto_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersPerformanceIndicatorsQuarterlyByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersPerformanceIndicatorsQuarterlyByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnersPerformanceIndicatorsQuarterlyByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnersPerformanceIndicatorsQuarterlyByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socios_iproducto_trimestral" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersPerformanceIndicatorsMonthlyByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersPerformanceIndicatorsMonthlyByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnersPerformanceIndicatorsMonthlyByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnersPerformanceIndicatorsMonthlyByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socios_iproducto_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnersPerformanceIndicatorsDetailedByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnersPerformanceIndicatorsDetailedByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnersPerformanceIndicatorsDetailedByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnersPerformanceIndicatorsDetailedByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socios_iproducto_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnerAnnualByProjectId/{projectId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnerAnnualByProjectId(
            @Context SecurityContext securityContext,
            @PathParam("projectId") Long projectId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnerAnnualByProjectId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnerAnnualByProjectId(projectId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socio_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnerQuarterlyByProjectId/{projectId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnerQuarterlyByProjectId(
            @Context SecurityContext securityContext,
            @PathParam("projectId") Long projectId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnerQuarterlyByProjectId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnerQuarterlyByProjectId(projectId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socio_trimestral" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnerMonthlyByProjectId/{projectId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnerMonthlyByProjectId(
            @Context SecurityContext securityContext,
            @PathParam("projectId") Long projectId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnerMonthlyByProjectId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnerMonthlyByProjectId(projectId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socio_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getPartnerDetailedByProjectId/{projectId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getPartnerDetailedByProjectId(
            @Context SecurityContext securityContext,
            @PathParam("projectId") Long projectId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnerDetailedByProjectId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getPartnerDetailedByProjectId(projectId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socio_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getDirectImplementationPerformanceIndicatorsAnnualByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getDirectImplementationPerformanceIndicatorsAnnualByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getDirectImplementationPerformanceIndicatorsAnnualByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getDirectImplementationPerformanceIndicatorsAnnualByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_implementacion_directa_anual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_implementacion_directa_trimestral" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_implementacion_directa_mensual" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getDirectImplementationPerformanceIndicatorsDetailedByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(
            @Context SecurityContext securityContext,
            @PathParam("periodId") Long periodId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getDirectImplementationPerformanceIndicatorsDetailedByPeriodId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_implementacion_directa_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("/getAllIndicatorExecutionDetailedByPeriodIdAndOfficeIdAndOfficeId/{projectId}/{officeId}")
    @GET
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response getAllIndicatorExecutionDetailedByPeriodIdAndOfficeIdAndOfficeId(
            @Context SecurityContext securityContext,
            @PathParam("projectId") Long projectId,
            @PathParam("officeId") Long officeId
    ) throws GeneralAppException {
        Principal principal = securityContext.getUserPrincipal();
        LOGGER.info("getPartnerDetailedByProjectId:" + principal.getName());
        long lStartTime = System.nanoTime();
        ByteArrayOutputStream r = this.reportService.getAllIndicatorExecutionDetailedByPeriodIdAndOfficeIdAndOffice(projectId, officeId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds: " + (lEndTime - lStartTime) / 1000000000);
        String filename = "Exportacion_datos_socio_detallado" + "_" + LocalDateTime.now(ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy-HH_ss")) + " .xlsx";
        return Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

}
