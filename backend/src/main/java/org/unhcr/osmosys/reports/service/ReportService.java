package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.exceptions.GeneralAppException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.services.UtilsService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ReportService {
    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(ReportService.class);

    @Inject
    ReportDataService reportDataService;

    @Inject
    UtilsService utilsService;


    final static Boolean dissableJasperReport = Boolean.FALSE;

    public ByteArrayOutputStream indicatorExecutionsToLateProjectsReportsByPeriodYear(Long periodId) throws GeneralAppException {
        if (ReportService.dissableJasperReport) {
            throw new GeneralAppException("Reporte en mantenimiento", Response.Status.BAD_REQUEST);
        }
        String jrxmlFile = "AllProjectStateReport.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        InputStream file = this.getReportFile(jrxmlFile);
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(file);
            List<Map<String, Object>> data = this.reportDataService.indicatorExecutionsProjectsReportsByPeriodId(periodId);
            JRMapArrayDataSource dataSource = new JRMapArrayDataSource(data.toArray());
            parameters.put("DataParameter", dataSource);

            JasperPrint jasperprint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            return getByteArrayOutputStreamFromJasperPrint(jasperprint);
        } catch (JRException e) {
            throw new GeneralAppException("Error al generar el reporte", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    private ByteArrayOutputStream getByteArrayOutputStreamFromJasperPrint(JasperPrint jasperprint) throws JRException {
        JRXlsxExporter xlsxExporter = new JRXlsxExporter();
        xlsxExporter.setExporterInput(new SimpleExporterInput(jasperprint));
        ByteArrayOutputStream finalReport = new ByteArrayOutputStream();
        OutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(finalReport);
        xlsxExporter.setExporterOutput(output);
        SimpleXlsxReportConfiguration xlsReportConfiguration = new SimpleXlsxReportConfiguration();
        xlsReportConfiguration.setOnePagePerSheet(true);
        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(false);
        xlsReportConfiguration.setDetectCellType(true);
        xlsReportConfiguration.setWhitePageBackground(false);
        xlsxExporter.setConfiguration(xlsReportConfiguration);
        xlsxExporter.exportReport();
        return finalReport;
    }

    public ByteArrayOutputStream indicatorsCatalogByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "indicatorCatalog.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream indicatorsCatalogWithImplementersSimpleByPeriodId(Long periodId) throws GeneralAppException {

        String jrxmlFile = "indicatorsCatalogWithImplementersSimple.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream indicatorsCatalogWithImplementersDetailedByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "indicatorsCatalogWithImplementersDetailed.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getAllImplementationsAnnualByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "all_implementations_anual_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getAllImplementationsQuarterlyByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "all_implementations_quarterly_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getAllImplementationsMonthlyByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "all_implementations_monthly_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }


    public ByteArrayOutputStream getAllImplementationsDetailedByPeriodId(Long periodId) throws GeneralAppException {

        SXSSFWorkbook workbook = this.reportDataService.getAllImplementationsDetailedByPeriodId(periodId);

        return getByteArrayOutputStreamFromWorkbook(workbook);

    }

    /***********total pi****************/
    public ByteArrayOutputStream getAllImplementationsPerformanceIndicatorsAnnualByPeriodId(Long periodId) throws GeneralAppException {
        if (ReportService.dissableJasperReport) {
            throw new GeneralAppException("Reporte en mantenimiento", Response.Status.BAD_REQUEST);
        }
        String jrxmlFile = "all_implementations_pi_anual_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId(Long periodId) throws GeneralAppException {
        if (ReportService.dissableJasperReport) {
            throw new GeneralAppException("Reporte en mantenimiento", Response.Status.BAD_REQUEST);
        }
        String jrxmlFile = "all_implementations_pi_quarterly_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId(Long periodId) throws GeneralAppException {
        if (ReportService.dissableJasperReport) {
            throw new GeneralAppException("Reporte en mantenimiento", Response.Status.BAD_REQUEST);
        }
        String jrxmlFile = "all_implementations_pi_monthly_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getAllImplementationsPerformanceIndicatorsDetailedByPeriodId(Long periodId) throws GeneralAppException {

        SXSSFWorkbook workbook = this.reportDataService.getAllImplementationsPerformanceIndicatorsDetailedByPeriodId(periodId);

        return getByteArrayOutputStreamFromWorkbook(workbook);

    }

    private ByteArrayOutputStream getByteArrayOutputStreamFromWorkbook(SXSSFWorkbook workbook) throws GeneralAppException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);

            return bos;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralAppException("Error al Generar el reporte", Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*******************partners*****************/
    public ByteArrayOutputStream getPartnersAnnualByPeriodId(Long periodId) throws GeneralAppException {
        if (ReportService.dissableJasperReport) {
            throw new GeneralAppException("Reporte en mantenimiento", Response.Status.BAD_REQUEST);
        }
        String jrxmlFile = "partners_anual_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getPartnersQuarterlyByPeriodId(Long periodId) throws GeneralAppException {
        if (ReportService.dissableJasperReport) {
            throw new GeneralAppException("Reporte en mantenimiento", Response.Status.BAD_REQUEST);
        }
        String jrxmlFile = "partners_quarterly_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getPartnersMonthlyByPeriodId(Long periodId) throws GeneralAppException {
        if (ReportService.dissableJasperReport) {
            throw new GeneralAppException("Reporte en mantenimiento", Response.Status.BAD_REQUEST);
        }
        String jrxmlFile = "partners_monthly_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getPartnersDetailedByPeriodId(Long periodId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getPartnersIndicatorsExecutionsDetailedByPeriodId(periodId);

        return getByteArrayOutputStreamFromWorkbook(workbook);
    }

    public ByteArrayOutputStream getPartnersGeneralIndicatorsAnnualByPeriodId(Long periodId) throws GeneralAppException {

        String jrxmlFile = "partners_general_anual_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getPartnersGeneralIndicatorsMonthlyByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "partners_general_monthly_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getPartnersGeneralIndicatorsDetailedByPeriodId(Long periodId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getPartnersGeneralIndicatorsDetailedByPeriodId(periodId);

        return getByteArrayOutputStreamFromWorkbook(workbook);
    }

    public ByteArrayOutputStream getPartnersPerformanceIndicatorsAnnualByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "partners_pi_anual_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }


    public ByteArrayOutputStream getPartnersPerformanceIndicatorsQuarterlyByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "partners_pi_quarterly_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }


    public ByteArrayOutputStream getPartnersPerformanceIndicatorsMonthlyByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "partners_pi_monthly_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getPartnersPerformanceIndicatorsDetailedByPeriodId(Long periodId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getPartnersPerformanceIndicatorsDetailedByPeriodId(periodId);

        return getByteArrayOutputStreamFromWorkbook(workbook);
    }

    public ByteArrayOutputStream getPartnerAnnualByProjectId(Long projectId) throws GeneralAppException {
        if (ReportService.dissableJasperReport) {
            throw new GeneralAppException("Reporte en mantenimiento", Response.Status.BAD_REQUEST);
        }
        String jrxmlFile = "partner_anual_by_project_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("projectId", projectId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getPartnerQuarterlyByProjectId(Long projectId) throws GeneralAppException {
        if (ReportService.dissableJasperReport) {
            throw new GeneralAppException("Reporte en mantenimiento", Response.Status.BAD_REQUEST);
        }
        String jrxmlFile = "partner_quarterly_by_project_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("projectId", projectId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getPartnerMonthlyByProjectId(Long projectId) throws GeneralAppException {
        if (ReportService.dissableJasperReport) {
            throw new GeneralAppException("Reporte en mantenimiento", Response.Status.BAD_REQUEST);
        }
        String jrxmlFile = "partner_monthly_by_project_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("projectId", projectId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getPartnerDetailedByProjectId(Long projectId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getPartnerDetailedByProjectId(projectId);

        return getByteArrayOutputStreamFromWorkbook(workbook);
    }


    /*************direct implementation ************/

    public ByteArrayOutputStream getDirectImplementationPerformanceIndicatorsAnnualByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "direct_implementation_anual_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "direct_implementation_quarterly_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId(Long periodId) throws GeneralAppException {
        String jrxmlFile = "direct_implementation_monthly_by_period_id.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("periodId", periodId);
        return this.generateReporWithJdbcConnecion(jrxmlFile, parameters);
    }

    public ByteArrayOutputStream getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(Long periodId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(periodId);

        return getByteArrayOutputStreamFromWorkbook(workbook);
    }


    public InputStream getReportFile(String fileName) {
        //URL fileUrl = this.getClass().getResource("reportsJR/GeneralReportsTotal.jrxml");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream("reports" + File.separator + fileName);
    }


    private Connection getConexion() throws GeneralAppException {
        Connection connection;
        try {
            InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup("java:jboss/datasources/osmosys");
            connection = dataSource.getConnection();
            return connection;
        } catch (NamingException | SQLException e) {
            LOGGER.error("error al crear conexión");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new GeneralAppException("Error al conectarse a la base de datos, comunícate con el administrador", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private ByteArrayOutputStream generateReporWithJdbcConnecion(String jrxmlFile, Map<String, Object> parameters) throws GeneralAppException {
        try {
            InputStream file = this.getReportFile(jrxmlFile);
            JasperReport jasperReport = JasperCompileManager.compileReport(file);
            Connection con = this.getConexion();
            JasperPrint jasperprint = JasperFillManager.fillReport(jasperReport, parameters, con);
            return getByteArrayOutputStreamFromJasperPrint(jasperprint);
        } catch (JRException e) {
            throw new GeneralAppException("Error al generar el reporte", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    public ByteArrayOutputStream getAllIndicatorExecutionDetailedByPeriodIdAndOfficeIdAndOffice(Long periodId, Long officeId) throws GeneralAppException {

        SXSSFWorkbook workbook = this.reportDataService.getAllIndicatorExecutionDetailedByPeriodIdAndOfficeIdAndOffice(periodId, officeId);

        return getByteArrayOutputStreamFromWorkbook(workbook);

    }

    public ByteArrayOutputStream getPartnerLateReportByProjectId(Long projectId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getPartnerLateReportByProjectId(projectId, this.utilsService.getCurrentYear(), this.utilsService.getCurrentMonthYearOrder());
        if (workbook != null) {
            return getByteArrayOutputStreamFromWorkbook(workbook);
        } else {
            return null;
        }
    }

    public ByteArrayOutputStream getPartnerLateReviewByProjectId(Long projectId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getPartnerLateReviewByProjectId(projectId, this.utilsService.getCurrentYear(), this.utilsService.getCurrentMonthYearOrder());
        if (workbook != null) {
            return getByteArrayOutputStreamFromWorkbook(workbook);
        } else {
            return null;
        }
    }

    public ByteArrayOutputStream getPartnerLateReportByFocalPointId(Long focalPointId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getPartnerLateReportByFocalPointId(focalPointId, this.utilsService.getCurrentYear(), this.utilsService.getCurrentMonthYearOrder());
        if (workbook != null) {
            return getByteArrayOutputStreamFromWorkbook(workbook);
        } else {
            return null;
        }
    }

    public ByteArrayOutputStream getPartnerLateReviewReportByFocalPointId(Long focalPointId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getPartnerLateReviewReportByFocalPointId(focalPointId, this.utilsService.getCurrentYear(), this.utilsService.getCurrentMonthYearOrder());
        if (workbook != null) {
            return getByteArrayOutputStreamFromWorkbook(workbook);
        } else {
            return null;
        }
    }

    public ByteArrayOutputStream getDirectImplementationLateReportByResponsableId(Long responsableId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getDirectImplementationLateReportByResponsableId(responsableId, this.utilsService.getCurrentYear(), this.utilsService.getCurrentMonthYearOrder());
        if (workbook != null) {
            return getByteArrayOutputStreamFromWorkbook(workbook);
        } else {
            return null;
        }
    }

    public ByteArrayOutputStream getPartnerLateReviewReportByProjectId(Long projectId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getPartnerLateReviewReportByProjectId(projectId, this.utilsService.getCurrentYear(), this.utilsService.getCurrentMonthYearOrder());
        if (workbook != null) {
            return getByteArrayOutputStreamFromWorkbook(workbook);
        } else {
            return null;
        }
    }

    public ByteArrayOutputStream getDirectImplementationLateReviewReportBySupervisorId(Long supervisorId) throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getDirectImplementationLateReviewReportBySupervisorId(supervisorId, this.utilsService.getCurrentYear(), this.utilsService.getCurrentMonthYearOrder());
        if (workbook != null) {
            return getByteArrayOutputStreamFromWorkbook(workbook);
        } else {
            return null;
        }
    }

    public ByteArrayOutputStream getAllLateReviewReportDirectImplementation() throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getAllLateReviewReportDirectImplementation(this.utilsService.getCurrentYear(), this.utilsService.getCurrentMonthYearOrder());
        if (workbook != null) {
            return getByteArrayOutputStreamFromWorkbook(workbook);
        } else {
            return null;
        }
    }

    public ByteArrayOutputStream getAllLateReportDirectImplementation() throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getAllLateReportDirectImplementation(this.utilsService.getCurrentYear(), this.utilsService.getCurrentMonthYearOrder());
        if (workbook != null) {
            return getByteArrayOutputStreamFromWorkbook(workbook);
        } else {
            return null;
        }
    }

    public ByteArrayOutputStream getAllLateReportPartners() throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getAllLateReportPartners(this.utilsService.getCurrentYear(), this.utilsService.getCurrentMonthYearOrder());
        if (workbook != null) {
            return getByteArrayOutputStreamFromWorkbook(workbook);
        } else {
            return null;
        }
    }

    public ByteArrayOutputStream getAllLateReviewPartners() throws GeneralAppException {
        SXSSFWorkbook workbook = this.reportDataService.getAllLateReviewPartners(this.utilsService.getCurrentYear(), this.utilsService.getCurrentMonthYearOrder());
        if (workbook != null) {
            return getByteArrayOutputStreamFromWorkbook(workbook);
        } else {
            return null;
        }
    }


}
