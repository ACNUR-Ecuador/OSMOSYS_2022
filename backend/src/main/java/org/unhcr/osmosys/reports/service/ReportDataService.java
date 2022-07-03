package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.ReportDao;
import org.unhcr.osmosys.model.reportDTOs.IndicatorExecutionDetailedDTO;
import org.unhcr.osmosys.model.reportDTOs.LaterReportDTO;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.webServices.model.IndicatorExecutionWeb;
import org.unhcr.osmosys.webServices.model.LateType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
@Stateless
public class ReportDataService {
    private static final Logger LOGGER = Logger.getLogger(ReportDataService.class);
    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    ReportDao reportDao;

    public List<Map<String, Object>> indicatorExecutionsProjectsReportsByPeriodId(Long periodId) throws GeneralAppException {
        List<IndicatorExecutionWeb> indicatorExecutions = this.indicatorExecutionService.getActiveProjectIndicatorExecutionsByPeriodId(periodId);
        return this.indicatorExecutionsProjectsReports(indicatorExecutions);
    }


    public List<Map<String, Object>> indicatorExecutionsProjectsReports(List<IndicatorExecutionWeb> indicatorExecutions) {
        List<Map<String, Object>> r = new ArrayList();
        for (IndicatorExecutionWeb ie : indicatorExecutions) {
            Map<String, Object> map = new HashMap();
            map.put("partner", ie.getProject().getOrganization().getAcronym() + '-' + ie.getProject().getOrganization().getDescription());
            map.put("project", ie.getProject().getCode() + '-' + ie.getProject().getName());
            map.put("indicatorType", ie.getIndicatorType().getLabel());
            map.put("outcomeStatement", ie.getProjectStatement() != null ? ie.getProjectStatement().getCode() + '-' + ie.getProjectStatement().getDescription() : null);
            map.put("indicator", ie.getIndicator().getCode() + "-" + ie.getIndicator().getDescription()
                    + (StringUtils.isNotEmpty(ie.getIndicator().getCategory()) ? " (Categoría: " + ie.getIndicator().getCategory() + " )" : ""));
            map.put("target", ie.getTarget());
            map.put("totalExecution", ie.getTotalExecution());
            map.put("executionPercentage", ie.getExecutionPercentage());
            map.put("late", ie.getLate().equals(LateType.LATE) ? "Si" : "No");
            r.add(map);
        }

        return r;

    }


    public SXSSFWorkbook getAllImplementationsDetailedByPeriodId(Long projectId) {

        long lStartTime = System.nanoTime();
        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getAllIndicatorExecutionDetailed(projectId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds:(query))" + (lEndTime - lStartTime) / 1000000000);

        List<Integer> columnsToRemove = new ArrayList<>(
                Arrays.asList(
                        0, 1, 2, 3, 4, 5, 27, 28
                )
        );

        return getReportFromIndicatorExecutionDetailedDTO(resultData, columnsToRemove);

    }

    public SXSSFWorkbook getAllImplementationsPerformanceIndicatorsDetailedByPeriodId(Long projectId) {


        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getAllPerformanceIndicatorsIndicatorExecutionDetailed(projectId);
        List<Integer> columnsToRemove = new ArrayList<>(
                Arrays.asList(
                        0, 1, 2, 3, 4, 5, 27, 28
                )
        );


        return getReportFromIndicatorExecutionDetailedDTO(resultData, columnsToRemove);
    }

    private SXSSFWorkbook getReportFromIndicatorExecutionDetailedDTO(List<IndicatorExecutionDetailedDTO> resultData, List<Integer> columnsToRemove) {
        List<Integer> titlesWidth = this.getTitlesWidthIndicatorExecutionDetailedDTO(columnsToRemove);


        List<String> titles = this.getTitlesIndicatorExecutionDetailedDTO(columnsToRemove);

        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = resultData.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        // title rows
        sheet.setAutoFilter(CellRangeAddress.valueOf(reference.formatAsString()));
        // Create
        DataFormat format = wb.createDataFormat();
        CellStyle cellStylePercentage;
        cellStylePercentage = wb.createCellStyle();
        cellStylePercentage.setDataFormat(format.getFormat("0%"));
        // Set the values for the table

        Row rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = rowTitle.createCell(i);
            cell.setCellValue(titles.get(i));
            sheet.setColumnWidth(i, titlesWidth.get(i));
        }

        long lStartTime2 = System.nanoTime();
        for (int i = 0; i < resultData.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            IndicatorExecutionDetailedDTO ie = resultData.get(i);
            for (int t = 0; t < titles.size(); t++) {
                Cell cell = rowData.createCell(t);
                this.setDataFromIndicatorExecutionDetailedDTO(wb, titles.get(t), cell, ie, cellStylePercentage);
            }

        }
        long lEndTime2 = System.nanoTime();
        LOGGER.info("Elapsed time in seconds(excel construction): " + (lEndTime2 - lStartTime2) / 1000000000);
        return wb;
    }

    public SXSSFWorkbook getPartnersIndicatorsExecutionsDetailedByPeriodId(Long projectId) {


        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getPartnersIndicatorsExecutionsDetailedByPeriodId(projectId);
        List<Integer> columnsToRemove = new ArrayList<>(
                Arrays.asList(
                        0, 1, 2, 3, 4, 5, 27, 28
                )
        );


        return getReportFromIndicatorExecutionDetailedDTO(resultData, columnsToRemove);

    }

    public SXSSFWorkbook getPartnersGeneralIndicatorsDetailedByPeriodId(Long projectId) {

        long lStartTime = System.nanoTime();
        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getPartnersGeneralIndicatorsDetailedByPeriodId(projectId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds:(query))" + (lEndTime - lStartTime) / 1000000000);

        List<Integer> columnsToRemove = new ArrayList<>(
                Arrays.asList(
                        0, 1, 2, 3, 4, 5, 27, 28
                )
        );

        return getReportFromIndicatorExecutionDetailedDTO(resultData, columnsToRemove);

    }

    public SXSSFWorkbook getPartnersPerformanceIndicatorsDetailedByPeriodId(Long projectId) {

        long lStartTime = System.nanoTime();
        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getPartnersPerformanceIndicatorsDetailedByPeriodId(projectId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds:(query))" + (lEndTime - lStartTime) / 1000000000);

        List<Integer> columnsToRemove = new ArrayList<>(
                Arrays.asList(
                        0, 1, 2, 3, 4, 5, 27, 28
                )
        );

        return getReportFromIndicatorExecutionDetailedDTO(resultData, columnsToRemove);


    }

    public SXSSFWorkbook getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(Long periodId) {


        long lStartTime = System.nanoTime();
        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(periodId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds:(query))" + (lEndTime - lStartTime) / 1000000000);

        List<Integer> columnsToRemove = new ArrayList<>(
                Arrays.asList(
                        0, 1, 2, 3, 4, 5, 27, 28
                )
        );

        return getReportFromIndicatorExecutionDetailedDTO(resultData, columnsToRemove);

    }

    public SXSSFWorkbook getPartnerDetailedByProjectId(Long projectId) {
        long lStartTime = System.nanoTime();
        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getPartnerDetailedByProjectId(projectId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds:(query))" + (lEndTime - lStartTime) / 1000000000);

        List<Integer> columnsToRemove = new ArrayList<>(
                Arrays.asList(
                        0, 1, 2, 3, 4, 5, 27, 28
                )
        );

        return getReportFromIndicatorExecutionDetailedDTO(resultData, columnsToRemove);
    }

    private void setDataFromIndicatorExecutionDetailedDTO(SXSSFWorkbook wb, String title, Cell cell, IndicatorExecutionDetailedDTO ie, CellStyle cellStylePercentage) {

        switch (title) {
            case "Asignacion de Indicador Id":
                cell.setCellValue(ie.getIe_id());
                break;
            case "Periodo Id":
                cell.setCellValue(ie.getPeriod_id());
            case "Proyecto Id":
                if (ie.getProject_id() != null) {
                    cell.setCellValue(ie.getProject_id());
                }
                break;
            case "Oficina Id":
                if (ie.getReporting_office_id() != null) {
                    cell.setCellValue(ie.getReporting_office_id());
                }
                break;
            case "Organización Id":
                if (ie.getOrganization_id() != null) {
                    cell.setCellValue(ie.getOrganization_id());
                }
                break;
            case "Tipo de Implementación":
                cell.setCellValue(ie.getImplementation_type());
                break;
            case "Área":
                cell.setCellValue(ie.getArea());
                break;
            case "Declaración":
                cell.setCellValue(ie.getStatement());
                break;
            case "Declaración de Proyecto":
                cell.setCellValue(ie.getStatement_project());
                break;
            case "Tipo de Indicador":
                cell.setCellValue(ie.getIndicator_type());
                break;
            case "Indicador":
                cell.setCellValue(ie.getIndicator());
                break;
            case "Categoría":
                cell.setCellValue(ie.getCategory());
                break;
            case "Frecuencia":
                cell.setCellValue(ie.getFrecuency());
                break;
            case "Proyecto":
                cell.setCellValue(ie.getProject());
                break;
            case "Implementador":
                cell.setCellValue(ie.getImplementers());
                break;
            case "Ejecución Total":
                if (ie.getTotal_execution() != null) {
                    cell.setCellValue(ie.getTotal_execution().intValue());
                }
                break;
            case "Meta Total":
                if (ie.getTarget() != null) {
                    cell.setCellValue(ie.getTarget().intValue());
                }
                break;
            case "% de Ejecución Total":
                if (ie.getExecution_percentage() != null) {
                    cell.setCellValue(ie.getExecution_percentage().intValue());
                    cell.setCellStyle(cellStylePercentage);
                }

                break;
            case "Trimestre Orden":
                cell.setCellValue(ie.getQuarter_order());
                break;
            case "Trimestre":
                cell.setCellValue(ie.getQuarter());
                break;
            case "Ejecucion Trimestral":
                if (ie.getQuarter_execution() != null) {
                    cell.setCellValue(ie.getQuarter_execution().intValue());
                }
                break;
            case "Meta Trimestral":
                if (ie.getQuarter_target() != null) {
                    cell.setCellValue(ie.getQuarter_target().intValue());
                }
                break;
            case "% de Ejecución Trimestral":
                if (ie.getQuarter_percentage() != null) {
                    cell.setCellValue(ie.getQuarter_percentage().intValue());
                    cell.setCellStyle(cellStylePercentage);
                }
                break;
            case "Mes Orden":
                cell.setCellValue(ie.getMonth_order());
                break;
            case "Mes":
                cell.setCellValue(ie.getMonth());
                break;
            case "Ejecucion Mensual":
                if (ie.getMonth_execution() != null) {
                    cell.setCellValue(ie.getMonth_execution().intValue());
                }
                break;
            case "Valor Id":
                if (ie.getIv_id() != null) {
                    cell.setCellValue(ie.getIv_id());
                }
                break;
            case "Valor Personalizado Id":
                if (ie.getIvc_id() != null) {
                    cell.setCellValue(ie.getIvc_id());
                }
                break;
            case "Tipo de Desagregacion":
                cell.setCellValue(ie.getDissagregation_type());
                break;
            case "Cantón":

                cell.setCellValue(ie.getLugar_canton());
                break;
            case "Provincia":
                cell.setCellValue(ie.getLugar_provincia());
                break;
            case "Tipo de Población":
                cell.setCellValue(ie.getPopulation_type());
                break;
            case "Género":
                cell.setCellValue(ie.getGender_type());
                break;
            case "Edad":
                cell.setCellValue(ie.getAge_type());
                break;
            case "País de Origen":
                cell.setCellValue(ie.getCountry_of_origin());
                break;
            case "Diversidad":
                cell.setCellValue(ie.getDiversity_type());
                break;
            case "Edad - Educación Primaria":
                cell.setCellValue(ie.getAge_primary_education_type());
                break;
            case "Edad - Educación Terciaria":
                cell.setCellValue(ie.getAge_tertiary_education_type());
                break;
            case "Desagregación Personalizada":
                cell.setCellValue(ie.getCustom_dissagregacion());
                break;
            case "Valor Reportado":
                if (ie.getValue() != null) {
                    cell.setCellValue(ie.getValue().intValue());
                }
                break;


        }
    }

    public List<String> getTitlesIndicatorExecutionDetailedDTO(List<Integer> columnsToRemove) {

        Map<Integer, String> titlesMap = new HashMap<>();
        titlesMap.put(0, "Asignacion de Indicador Id");
        titlesMap.put(1, "Periodo Id");
        titlesMap.put(2, "Proyecto Id");
        titlesMap.put(3, "Oficina Id");
        titlesMap.put(4, "Organización Id");
        titlesMap.put(5, "Indicador de Producto Id");
        titlesMap.put(6, "Tipo de Implementación");
        titlesMap.put(7, "Área");
        titlesMap.put(8, "Declaración");
        titlesMap.put(9, "Declaración de Proyecto");
        titlesMap.put(10, "Tipo de Indicador");
        titlesMap.put(11, "Indicador");
        titlesMap.put(12, "Categoría");
        titlesMap.put(13, "Frecuencia");
        titlesMap.put(14, "Proyecto");
        titlesMap.put(15, "Implementador");
        titlesMap.put(16, "Ejecución Total");
        titlesMap.put(17, "Meta Total");
        titlesMap.put(18, "% de Ejecución Total");
        titlesMap.put(19, "Trimestre Orden");
        titlesMap.put(20, "Trimestre");
        titlesMap.put(21, "Ejecucion Trimestral");
        titlesMap.put(22, "Meta Trimestral");
        titlesMap.put(23, "% de Ejecución Trimestral");
        titlesMap.put(24, "Mes Orden");
        titlesMap.put(25, "Mes");
        titlesMap.put(26, "Ejecucion Mensual");
        titlesMap.put(27, "Valor Id");
        titlesMap.put(28, "Valor Personalizado Id");
        titlesMap.put(29, "Tipo de Desagregacion");
        titlesMap.put(30, "Cantón");
        titlesMap.put(31, "Provincia");
        titlesMap.put(32, "Tipo de Población");
        titlesMap.put(33, "Género");
        titlesMap.put(34, "Edad");
        titlesMap.put(35, "País de Origen");
        titlesMap.put(36, "Diversidad");
        titlesMap.put(37, "Edad - Educación Primaria");
        titlesMap.put(38, "Edad - Educación Terciaria");
        titlesMap.put(39, "Desagregación Personalizada");
        titlesMap.put(40, "Valor Reportado");


        List<String> titles = new ArrayList<>();
        titlesMap.forEach((order, title) -> {
            if (!columnsToRemove.contains(order)) {
                titles.add(title);
            }
        });
        return titles;
    }

    public List<Integer> getTitlesWidthIndicatorExecutionDetailedDTO(List<Integer> columnsToRemove) {

        Map<Integer, Integer> titlesMap = new HashMap<>();
        titlesMap.put(0, 3000);// "Asignacion de Indicador Id");
        titlesMap.put(1, 3000);// "Periodo Id");
        titlesMap.put(2, 3000);// "Proyecto Id");
        titlesMap.put(3, 3000);// "Oficina Id");
        titlesMap.put(4, 3000);// "Indicador de Producto Id");
        titlesMap.put(5, 3000);// "Organización Id");
        titlesMap.put(6, 6000);// "Tipo de Implementación");
        titlesMap.put(7, 4500);// "Área");
        titlesMap.put(8, 6000);// "Declaración");
        titlesMap.put(9, 6000);// "Declaración de Proyecto");
        titlesMap.put(10, 4000);// "Tipo de Indicador");
        titlesMap.put(11, 7000);// "Indicador");
        titlesMap.put(12, 6500);// "Categoría");
        titlesMap.put(13, 5000);// "Frecuencia");
        titlesMap.put(14, 7000);// "Proyecto");
        titlesMap.put(15, 5000);// "Implementador");
        titlesMap.put(16, 5000);// "Ejecución Total");
        titlesMap.put(17, 5000);// "Meta Total");
        titlesMap.put(18, 5000);// "% de Ejecución Total");
        titlesMap.put(19, 5000);// "Trimestre Orden");
        titlesMap.put(20, 5000);// "Trimestre");
        titlesMap.put(21, 5000);// "Ejecucion Trimestral");
        titlesMap.put(22, 5000);// "Meta Trimestral");
        titlesMap.put(23, 5000);// "% de Ejecución Trimestral");
        titlesMap.put(24, 5000);// "Mes Orden");
        titlesMap.put(25, 5000);// "Mes");
        titlesMap.put(26, 5000);// "Ejecucion Mensual");
        titlesMap.put(27, 5000);// "Valor Id");
        titlesMap.put(28, 5000);// "Valor Personalizado Id");
        titlesMap.put(29, 7000);// "Tipo de Desagregacion");
        titlesMap.put(30, 5000);// "Cantón");
        titlesMap.put(31, 5000);// "Provincia");
        titlesMap.put(32, 6000);// "Tipo de Población");
        titlesMap.put(33, 5000);// "Género");
        titlesMap.put(34, 5000);// "Edad");
        titlesMap.put(35, 5000);// "País de Origen");
        titlesMap.put(36, 5000);// "Diversidad");
        titlesMap.put(37, 5000);// "Edad - Educación Primaria");
        titlesMap.put(38, 5000);// "Edad - Educación Terciaria");
        titlesMap.put(39, 5000);// "Desagregación Personalizada");
        titlesMap.put(40, 5000);// "Valor Reportado");


        List<Integer> widths = new ArrayList<>();
        titlesMap.forEach((order, title) -> {
            if (!columnsToRemove.contains(order)) {
                widths.add(title);
            }
        });
        return widths;
    }

    public SXSSFWorkbook getAllIndicatorExecutionDetailedByPeriodIdAndOfficeIdAndOffice(Long projectId, Long officeId) {

        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getAllIndicatorExecutionDetailedByPeriodIdAndOfficeId(projectId, officeId);

        List<Integer> columnsToRemove = new ArrayList<>(
                Arrays.asList(
                        0, 1, 2, 3, 4, 5, 27, 28
                )
        );

        return getReportFromIndicatorExecutionDetailedDTO(resultData, columnsToRemove);

    }

    public SXSSFWorkbook getPartnerLateReportByProjectId(Long projectId, Integer currentYear, Integer currentMonth) {
        List<LaterReportDTO> data = this.reportDao.getPartnerLateReportByProjectId(projectId, currentYear, currentMonth);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, true, false);
    }
    public SXSSFWorkbook getPartnerLateReviewByProjectId(Long projectId, Integer currentYear, Integer currentMonth) {
        List<LaterReportDTO> data = this.reportDao.getPartnerLateReviewByProjectId(projectId, currentYear, currentMonth);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, true, true);
    }

    public SXSSFWorkbook getPartnerLateReportByFocalPointId(Long focalPointId, Integer currentYear, Integer currentMonth) {
        List<LaterReportDTO> data = this.reportDao.getPartnerLateReportByProjectId(focalPointId, currentYear, currentMonth);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, true, false);
    }

    public SXSSFWorkbook getPartnerLateReviewReportByFocalPointId(Long focalPointId, Integer currentYear, Integer currentMonth) {
        List<LaterReportDTO> data = this.reportDao.getPartnerLateReviewReportByFocalPointId(focalPointId, currentYear, currentMonth);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, true, true);
    }

    public SXSSFWorkbook getPartnerLateReviewReportByProjectId(Long projectId, Integer currentYear, Integer currentMonth) {
        List<LaterReportDTO> data = this.reportDao.getPartnerLateReviewByProjectId(projectId, currentYear, currentMonth);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, true, true);
    }

    public SXSSFWorkbook getLateReport(List<LaterReportDTO> data, Boolean partner, Boolean review) {


        List<Integer> titlesWidth = this.getTitlesWidthLateReport(partner, review);


        List<String> titles = this.getTitlesLateReport(partner, review);
        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = data.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        // title rows
        sheet.setAutoFilter(CellRangeAddress.valueOf(reference.formatAsString()));

        Row rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = rowTitle.createCell(i);
            cell.setCellValue(titles.get(i));
            sheet.setColumnWidth(i, titlesWidth.get(i));
        }
        for (int i = 0; i < data.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            LaterReportDTO dataRow = data.get(i);

            for (int t = 0; t < titles.size(); t++) {
                Cell cell = rowData.createCell(t);
                this.setDataFromLaterReportDTO(wb, titles.get(t), cell, dataRow);
            }

        }
        return wb;
    }

    private void setDataFromLaterReportDTO(SXSSFWorkbook wb, String title, Cell cell, LaterReportDTO dataRow) {
        switch (title) {
            case "Proyecto":
                cell.setCellValue(dataRow.getProject());
                break;
            case "Oficina":
                cell.setCellValue(dataRow.getImplementer());
                break;
            case "Socio":
                cell.setCellValue(dataRow.getImplementer());
                break;
            case "Indicador Código":
                cell.setCellValue(dataRow.getIndicator_code());
                break;
            case "Indicador":
                cell.setCellValue(dataRow.getIndicator());
                break;
            case "Indicador Categoría":
                cell.setCellValue(dataRow.getIndicator_category());
                break;
            case "Meses Sin Verificación":
                cell.setCellValue(dataRow.getLate_months());
                break;
            case "Meses con Retraso":
                cell.setCellValue(dataRow.getLate_months());
                break;
            case "Punto Focal":
                cell.setCellValue(dataRow.getFocal_point());
                break;
            case "Verificador de datos":
                cell.setCellValue(dataRow.getSupervisor());
                break;
            case "Responsable reporte":
                cell.setCellValue(dataRow.getResponsible());
                break;

        }
    }

    public List<String> getTitlesLateReport(Boolean parters, Boolean review) {
        List<String> titles = new ArrayList<>();
        if (parters) {
            titles.add("Proyecto");
            titles.add("Socio");
            titles.add("Indicador Código");
            titles.add("Indicador");
            titles.add("Indicador Categoría");
            if (review) {
                titles.add("Meses Sin Verificación");
            } else {
                titles.add("Meses con Retraso");
            }
            titles.add("Punto Focal");
        } else {
            titles.add("Oficina");
            titles.add("Indicador Código");
            titles.add("Indicador");
            titles.add("Indicador Categoría");
            if (review) {
                titles.add("Meses Sin Verificación");
            } else {
                titles.add("Meses con Retraso");
            }
            titles.add("Verificador de datos");
            titles.add("Responsable reporte");
        }

        return titles;
    }

    public List<Integer> getTitlesWidthLateReport(Boolean parters, Boolean review) {

        List<Integer> titles = new ArrayList<>();
        if (parters) {
            titles.add(7000);
            titles.add(2000);
            titles.add(2000);
            titles.add(7000);
            titles.add(3000);
            if (review) {
                titles.add(7000);
            } else {
                titles.add(7000);
            }
            titles.add(5000);
        } else {
            titles.add(4000);
            titles.add(2000);
            titles.add(7000);
            titles.add(3000);
            if (review) {
                titles.add(7000);
            } else {
                titles.add(7000);
            }
            titles.add(5000);
            titles.add(5000);
        }

        return titles;
    }

    private SXSSFWorkbook getLAteReportExcel(List<IndicatorExecutionDetailedDTO> resultData, List<Integer> columnsToRemove) {
        List<Integer> titlesWidth = this.getTitlesWidthIndicatorExecutionDetailedDTO(columnsToRemove);


        List<String> titles = this.getTitlesIndicatorExecutionDetailedDTO(columnsToRemove);

        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = resultData.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        // title rows
        sheet.setAutoFilter(CellRangeAddress.valueOf(reference.formatAsString()));
        // Create
        DataFormat format = wb.createDataFormat();
        CellStyle cellStylePercentage;
        cellStylePercentage = wb.createCellStyle();
        cellStylePercentage.setDataFormat(format.getFormat("0%"));
        // Set the values for the table

        Row rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = rowTitle.createCell(i);
            cell.setCellValue(titles.get(i));
            sheet.setColumnWidth(i, titlesWidth.get(i));
        }

        long lStartTime2 = System.nanoTime();
        for (int i = 0; i < resultData.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            IndicatorExecutionDetailedDTO ie = resultData.get(i);
            for (int t = 0; t < titles.size(); t++) {
                Cell cell = rowData.createCell(t);
                this.setDataFromIndicatorExecutionDetailedDTO(wb, titles.get(t), cell, ie, cellStylePercentage);
            }

        }
        long lEndTime2 = System.nanoTime();
        LOGGER.info("Elapsed time in seconds(excel construction): " + (lEndTime2 - lStartTime2) / 1000000000);
        return wb;
    }

    public SXSSFWorkbook getPartnerLateReportByPartnerId(Long partnerId, Integer currentYear, Integer currentMonthYearOrder) throws GeneralAppException {

        List<LaterReportDTO> data = this.reportDao.getPartnerLateReportByPartnerId(partnerId, currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            throw new GeneralAppException("El socio no tiene retrazos ", Response.Status.NOT_FOUND.getStatusCode());
        }
        return this.getLateReport(data, true, false);


    }

    public SXSSFWorkbook getDirectImplementationLateReportByResponsableId(Long responsableId, Integer currentYear, Integer currentMonthYearOrder) {

        List<LaterReportDTO> data = this.reportDao.getDirectImplementationLateReportByResponsableId(responsableId, currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, false, false);
    }

    public SXSSFWorkbook getDirectImplementationLateReviewReportBySupervisorId(Long responsableId, Integer currentYear, Integer currentMonthYearOrder) {

        List<LaterReportDTO> data = this.reportDao.getDirectImplementationLateReviewReportBySupervisorId(responsableId, currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, false, true);
    }

    public SXSSFWorkbook getAllLateReviewReportDirectImplementation(Integer currentYear, Integer currentMonthYearOrder) {

        List<LaterReportDTO> data = this.reportDao.getAllLateReviewReportDirectImplementation( currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, false, true);
    }
    public SXSSFWorkbook getAllLateReportDirectImplementation(Integer currentYear, Integer currentMonthYearOrder) {

        List<LaterReportDTO> data = this.reportDao.getAllLateReportDirectImplementation( currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, false, false);
    }
    public SXSSFWorkbook getAllLateReportPartners(Integer currentYear, Integer currentMonthYearOrder) {

        List<LaterReportDTO> data = this.reportDao.getAllLateReportPartners( currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, true, false);
    }
    public SXSSFWorkbook getAllLateReviewPartners(Integer currentYear, Integer currentMonthYearOrder) {

        List<LaterReportDTO> data = this.reportDao.getAllLateReviewPartners( currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, true, true);
    }
}
