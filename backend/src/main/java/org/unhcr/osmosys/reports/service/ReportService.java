package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.exceptions.GeneralAppException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.reports.model.LateReportingDto;
import org.unhcr.osmosys.services.IndicatorExecutionService;

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
    private static final Logger LOGGER = Logger.getLogger(ReportService.class);

    @Inject
    ReportDataService reportDataService;

    public ByteArrayOutputStream generatePartnerLateReportByProjectId(Long projectId) throws GeneralAppException {
        String jrxmlFile = "ProjectLateReport.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        InputStream file = this.getReportFile(jrxmlFile);
        List<LateReportingDto> r = this.reportDataService.getLateIndicatorExecutionByProjectId(projectId);


        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(file);

            if (parameters == null) {
                parameters = new HashMap<>();
            }
            JRDataSource dataSource = new JRBeanCollectionDataSource(r);
            JasperPrint jasperprint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

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

        } catch (Exception e) {
            throw new GeneralAppException("Error al generar el reporte", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    public InputStream getReportFile(String fileName) {
        //URL fileUrl = this.getClass().getResource("reportsJR/GeneralReportsTotal.jrxml");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream resource = classLoader.getResourceAsStream("reports" + File.separator + fileName);


        return resource;
    }
}
