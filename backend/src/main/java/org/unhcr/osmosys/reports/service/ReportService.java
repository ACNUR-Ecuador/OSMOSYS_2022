package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.exceptions.GeneralAppException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ReportService {
    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(ReportService.class);

    @Inject
    ReportDataService reportDataService;

    public ByteArrayOutputStream indicatorExecutionsToLateProjectsReportsByPeriodYear(Long periodId) throws GeneralAppException {
        String jrxmlFile = "AllProjectStateReport.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        InputStream file = this.getReportFile(jrxmlFile);
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(file);
            List<Map<String, Object>> data = this.reportDataService.indicatorExecutionsProjectsReportsByPeriodId(periodId);
            JRMapArrayDataSource dataSource = new JRMapArrayDataSource(data.toArray());
            parameters.put("DataParameter", dataSource);

            JasperPrint jasperprint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
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
        } catch (JRException e) {
            throw new GeneralAppException("Error al generar el reporte", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    public InputStream getReportFile(String fileName) {
        //URL fileUrl = this.getClass().getResource("reportsJR/GeneralReportsTotal.jrxml");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream("reports" + File.separator + fileName);
    }
}
