package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.exceptions.GeneralAppException;
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
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.webServices.model.IndicatorExecutionWeb;
import org.unhcr.osmosys.webServices.model.LateType;

import javax.ejb.Stateless;
import javax.inject.Inject;
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
        List<String> titles = new ArrayList<>(
                Arrays.asList("Tipo de Implementacion", "Area", "Declaracion", "Declaracion de Proyecto", "Tipo de Indicador",
                        "Indicador", "Frecuencia", "Implementador", "Ejecucion Total", "Meta Total",
                        "% de Ejecucion Total", "Trimestre", "Ejecucion Trimestral", "Meta Trimestral", "% de Ejecucion Trimestral",
                        "Mes", "Ejecucion Mensual", "Tipo de Desagregacion", "Desagregacion Nivel 1", "Desagregacion Nivel 2",
                        "Valor Reportado"));

        List<Integer> titlesWidth = new ArrayList<>(
                Arrays.asList(
                        5000, 5000, 10000, 10000, 5000,
                        10000, 3000, 3000, 2000, 2000,
                        2000, 2000, 2000, 2000, 2000,
                        2000, 2000, 5000, 5000, 5000,
                        2000));

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

        Cell cell;
        long lStartTime2 = System.nanoTime();
        for (int i = 0; i < resultData.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            IndicatorExecutionDetailedDTO ie = resultData.get(i);

            cell = rowData.createCell(0);
            cell.setCellValue(ie.getImplementation_type());
            cell = rowData.createCell(1);
            cell.setCellValue(ie.getArea());
            cell = rowData.createCell(2);
            cell.setCellValue(ie.getStatement());
            cell = rowData.createCell(3);
            cell.setCellValue(ie.getStatement_project());
            cell = rowData.createCell(4);
            cell.setCellValue(ie.getIndicator_type());
            cell = rowData.createCell(5);
            cell.setCellValue(ie.getIndicator());
            cell = rowData.createCell(6);
            cell.setCellValue(ie.getFrecuency());
            cell = rowData.createCell(7);
            cell.setCellValue(ie.getImplementers());
            cell = rowData.createCell(8);
            if (ie.getTotal_execution() != null) {
                cell.setCellValue(ie.getTotal_execution().intValue());
            }
            cell = rowData.createCell(9);
            if (ie.getTarget() != null) {
                cell.setCellValue(ie.getTarget().intValue());
            }
            cell = rowData.createCell(10);
            if (ie.getExecution_percentage() != null) {
                cell.setCellValue(ie.getExecution_percentage().intValue());
                cell.setCellStyle(cellStylePercentage);
            }
            cell = rowData.createCell(11);
            cell.setCellValue(ie.getQuarter());
            cell = rowData.createCell(12);
            if (ie.getQuarter_execution() != null) {
                cell.setCellValue(ie.getQuarter_execution().intValue());
            }
            cell = rowData.createCell(13);
            if (ie.getQuarter_target() != null) {
                cell.setCellValue(ie.getQuarter_target().intValue());
            }
            cell = rowData.createCell(14);
            if (ie.getQuarter_percentage() != null) {
                cell.setCellValue(ie.getQuarter_percentage().floatValue());
                cell.setCellStyle(cellStylePercentage);
            }
            cell = rowData.createCell(15);
            cell.setCellValue(ie.getMonth());
            cell = rowData.createCell(16);
            if (ie.getMonth_execution() != null) {
                cell.setCellValue(ie.getMonth_execution().intValue());
            }
            cell = rowData.createCell(17);
            cell.setCellValue(ie.getDissagregation_type());
            cell = rowData.createCell(18);
            cell.setCellValue(ie.getDissagregation_level1());
            cell = rowData.createCell(19);
            cell.setCellValue(ie.getDissagregation_level2());
            cell = rowData.createCell(20);
            if (ie.getValue() != null) {
                cell.setCellValue(ie.getValue().intValue());
            }
        }
        long lEndTime2 = System.nanoTime();
        LOGGER.info("Elapsed time in seconds(excel construction): " + (lEndTime2 - lStartTime2) / 1000000000);
        return wb;

    }



    public SXSSFWorkbook getPartnersIndicatorsExecutionsDetailedByPeriodId(Long projectId) {


        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getPartnersIndicatorsExecutionsDetailedByPeriodId(projectId);
        List<String> titles = new ArrayList<>(
                Arrays.asList("Tipo de Implementacion", "Area", "Declaracion", "Declaracion de Proyecto", "Tipo de Indicador",
                        "Indicador", "Frecuencia", "Implementador", "Ejecucion Total", "Meta Total",
                        "% de Ejecucion Total", "Trimestre", "Ejecucion Trimestral", "Meta Trimestral", "% de Ejecucion Trimestral",
                        "Mes", "Ejecucion Mensual", "Tipo de Desagregacion", "Desagregacion Nivel 1", "Desagregacion Nivel 2",
                        "Valor Reportado"));

        List<Integer> titlesWidth = new ArrayList<>(
                Arrays.asList(
                        5000, 5000, 10000, 10000, 5000,
                        10000, 3000, 3000, 2000, 2000,
                        2000, 2000, 2000, 2000, 2000,
                        2000, 2000, 5000, 5000, 5000,
                        2000));

        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = resultData.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        sheet.setAutoFilter(CellRangeAddress.valueOf(reference.formatAsString()));
        // title rows

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

        Cell cell;
        for (int i = 0; i < resultData.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            IndicatorExecutionDetailedDTO ie = resultData.get(i);

            cell = rowData.createCell(0);
            cell.setCellValue(ie.getImplementation_type());
            cell = rowData.createCell(1);
            cell.setCellValue(ie.getArea());
            cell = rowData.createCell(2);
            cell.setCellValue(ie.getStatement());
            cell = rowData.createCell(3);
            cell.setCellValue(ie.getStatement_project());
            cell = rowData.createCell(4);
            cell.setCellValue(ie.getIndicator_type());
            cell = rowData.createCell(5);
            cell.setCellValue(ie.getIndicator());
            cell = rowData.createCell(6);
            cell.setCellValue(ie.getFrecuency());
            cell = rowData.createCell(7);
            cell.setCellValue(ie.getImplementers());
            cell = rowData.createCell(8);
            if (ie.getTotal_execution() != null) {
                cell.setCellValue(ie.getTotal_execution().intValue());
            }
            cell = rowData.createCell(9);
            if (ie.getTarget() != null) {
                cell.setCellValue(ie.getTarget().intValue());
            }
            cell = rowData.createCell(10);
            if (ie.getExecution_percentage() != null) {
                cell.setCellValue(ie.getExecution_percentage().intValue());
                cell.setCellStyle(cellStylePercentage);
            }
            cell = rowData.createCell(11);
            cell.setCellValue(ie.getQuarter());
            cell = rowData.createCell(12);
            if (ie.getQuarter_execution() != null) {
                cell.setCellValue(ie.getQuarter_execution().intValue());
            }
            cell = rowData.createCell(13);
            if (ie.getQuarter_target() != null) {
                cell.setCellValue(ie.getQuarter_target().intValue());
            }
            cell = rowData.createCell(14);
            if (ie.getQuarter_percentage() != null) {
                cell.setCellValue(ie.getQuarter_percentage().floatValue());
                cell.setCellStyle(cellStylePercentage);
            }
            cell = rowData.createCell(15);
            cell.setCellValue(ie.getMonth());
            cell = rowData.createCell(16);
            if (ie.getMonth_execution() != null) {
                cell.setCellValue(ie.getMonth_execution().intValue());
            }
            cell = rowData.createCell(17);
            cell.setCellValue(ie.getDissagregation_type());
            cell = rowData.createCell(18);
            cell.setCellValue(ie.getDissagregation_level1());
            cell = rowData.createCell(19);
            cell.setCellValue(ie.getDissagregation_level2());
            cell = rowData.createCell(20);
            if (ie.getValue() != null) {
                cell.setCellValue(ie.getValue().intValue());
            }
        }

        return wb;

    }

    public SXSSFWorkbook getAllImplementationsPerformanceIndicatorsDetailedByPeriodId(Long projectId) {


        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getAllPerformanceIndicatorsIndicatorExecutionDetailed(projectId);
        List<String> titles = new ArrayList<>(
                Arrays.asList("Tipo de Implementacion", "Area", "Declaracion", "Declaracion de Proyecto", "Tipo de Indicador",
                        "Indicador", "Frecuencia", "Implementador", "Ejecucion Total", "Meta Total",
                        "% de Ejecucion Total", "Trimestre", "Ejecucion Trimestral", "Meta Trimestral", "% de Ejecucion Trimestral",
                        "Mes", "Ejecucion Mensual", "Tipo de Desagregacion", "Desagregacion Nivel 1", "Desagregacion Nivel 2",
                        "Valor Reportado"));

        List<Integer> titlesWidth = new ArrayList<>(
                Arrays.asList(
                        5000, 5000, 10000, 10000, 5000,
                        10000, 3000, 3000, 2000, 2000,
                        2000, 2000, 2000, 2000, 2000,
                        2000, 2000, 5000, 5000, 5000,
                        2000));

        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = resultData.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        sheet.setAutoFilter(CellRangeAddress.valueOf(reference.formatAsString()));
        // title rows

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

        Cell cell;
        for (int i = 0; i < resultData.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            IndicatorExecutionDetailedDTO ie = resultData.get(i);

            cell = rowData.createCell(0);
            cell.setCellValue(ie.getImplementation_type());
            cell = rowData.createCell(1);
            cell.setCellValue(ie.getArea());
            cell = rowData.createCell(2);
            cell.setCellValue(ie.getStatement());
            cell = rowData.createCell(3);
            cell.setCellValue(ie.getStatement_project());
            cell = rowData.createCell(4);
            cell.setCellValue(ie.getIndicator_type());
            cell = rowData.createCell(5);
            cell.setCellValue(ie.getIndicator());
            cell = rowData.createCell(6);
            cell.setCellValue(ie.getFrecuency());
            cell = rowData.createCell(7);
            cell.setCellValue(ie.getImplementers());
            cell = rowData.createCell(8);
            if (ie.getTotal_execution() != null) {
                cell.setCellValue(ie.getTotal_execution().intValue());
            }
            cell = rowData.createCell(9);
            if (ie.getTarget() != null) {
                cell.setCellValue(ie.getTarget().intValue());
            }
            cell = rowData.createCell(10);
            if (ie.getExecution_percentage() != null) {
                cell.setCellValue(ie.getExecution_percentage().intValue());
                cell.setCellStyle(cellStylePercentage);
            }
            cell = rowData.createCell(11);
            cell.setCellValue(ie.getQuarter());
            cell = rowData.createCell(12);
            if (ie.getQuarter_execution() != null) {
                cell.setCellValue(ie.getQuarter_execution().intValue());
            }
            cell = rowData.createCell(13);
            if (ie.getQuarter_target() != null) {
                cell.setCellValue(ie.getQuarter_target().intValue());
            }
            cell = rowData.createCell(14);
            if (ie.getQuarter_percentage() != null) {
                cell.setCellValue(ie.getQuarter_percentage().floatValue());
                cell.setCellStyle(cellStylePercentage);
            }
            cell = rowData.createCell(15);
            cell.setCellValue(ie.getMonth());
            cell = rowData.createCell(16);
            if (ie.getMonth_execution() != null) {
                cell.setCellValue(ie.getMonth_execution().intValue());
            }
            cell = rowData.createCell(17);
            cell.setCellValue(ie.getDissagregation_type());
            cell = rowData.createCell(18);
            cell.setCellValue(ie.getDissagregation_level1());
            cell = rowData.createCell(19);
            cell.setCellValue(ie.getDissagregation_level2());
            cell = rowData.createCell(20);
            if (ie.getValue() != null) {
                cell.setCellValue(ie.getValue().intValue());
            }
        }

        return wb;

    }

    public SXSSFWorkbook getPartnersGeneralIndicatorsDetailedByPeriodId(Long projectId) {


        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getPartnersGeneralIndicatorsDetailedByPeriodId(projectId);
        List<String> titles = new ArrayList<>(
                Arrays.asList("Tipo de Implementacion", /*"Area", "Declaracion", "Declaracion de Proyecto",*/ "Tipo de Indicador",
                        "Indicador", /*"Frecuencia", */"Implementador", "Ejecucion Total", "Meta Total",
                        "% de Ejecucion Total", "Trimestre", "Ejecucion Trimestral", "Meta Trimestral", "% de Ejecucion Trimestral",
                        "Mes", "Ejecucion Mensual", "Tipo de Desagregacion", "Desagregacion Nivel 1", "Desagregacion Nivel 2",
                        "Valor Reportado"));

        List<Integer> titlesWidth = new ArrayList<>(
                Arrays.asList(
                        5000, /*5000, 10000, 10000,*/ 5000,
                        10000,/* 3000, */3000, 2000, 2000,
                        2000, 2000, 2000, 2000, 2000,
                        2000, 2000, 5000, 5000, 5000,
                        2000));

        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = resultData.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        sheet.setAutoFilter(CellRangeAddress.valueOf(reference.formatAsString()));
        // title rows

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

        Cell cell;
        for (int i = 0; i < resultData.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            IndicatorExecutionDetailedDTO ie = resultData.get(i);

            cell = rowData.createCell(0);
            cell.setCellValue(ie.getImplementation_type());
            cell = rowData.createCell(1);
            cell.setCellValue(ie.getIndicator_type());
            cell = rowData.createCell(2);
            cell.setCellValue(ie.getIndicator());
            cell = rowData.createCell(3);
            cell.setCellValue(ie.getImplementers());
            cell = rowData.createCell(4);
            if (ie.getTotal_execution() != null) {
                cell.setCellValue(ie.getTotal_execution().intValue());
            }
            cell = rowData.createCell(5);
            if (ie.getTarget() != null) {
                cell.setCellValue(ie.getTarget().intValue());
            }
            cell = rowData.createCell(6);
            if (ie.getExecution_percentage() != null) {
                cell.setCellValue(ie.getExecution_percentage().intValue());
                cell.setCellStyle(cellStylePercentage);
            }
            cell = rowData.createCell(7);
            cell.setCellValue(ie.getQuarter());
            cell = rowData.createCell(8);
            if (ie.getQuarter_execution() != null) {
                cell.setCellValue(ie.getQuarter_execution().intValue());
            }
            cell = rowData.createCell(9);
            if (ie.getQuarter_target() != null) {
                cell.setCellValue(ie.getQuarter_target().intValue());
            }
            cell = rowData.createCell(10);
            if (ie.getQuarter_percentage() != null) {
                cell.setCellValue(ie.getQuarter_percentage().floatValue());
                cell.setCellStyle(cellStylePercentage);
            }
            cell = rowData.createCell(11);
            cell.setCellValue(ie.getMonth());
            cell = rowData.createCell(12);
            if (ie.getMonth_execution() != null) {
                cell.setCellValue(ie.getMonth_execution().intValue());
            }
            cell = rowData.createCell(13);
            cell.setCellValue(ie.getDissagregation_type());
            cell = rowData.createCell(14);
            cell.setCellValue(ie.getDissagregation_level1());
            cell = rowData.createCell(15);
            cell.setCellValue(ie.getDissagregation_level2());
            cell = rowData.createCell(16);
            if (ie.getValue() != null) {
                cell.setCellValue(ie.getValue().intValue());
            }
        }

        return wb;

    }

    public SXSSFWorkbook getPartnersPerformanceIndicatorsDetailedByPeriodId(Long projectId) {


        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getPartnersPerformanceIndicatorsDetailedByPeriodId(projectId);
        List<String> titles = new ArrayList<>(
                Arrays.asList("Tipo de Implementacion", "Area", "Declaracion", "Declaracion de Proyecto", "Tipo de Indicador",
                        "Indicador", "Frecuencia", "Implementador", "Ejecucion Total", "Meta Total",
                        "% de Ejecucion Total", "Trimestre", "Ejecucion Trimestral", "Meta Trimestral", "% de Ejecucion Trimestral",
                        "Mes", "Ejecucion Mensual", "Tipo de Desagregacion", "Desagregacion Nivel 1", "Desagregacion Nivel 2",
                        "Valor Reportado"));

        List<Integer> titlesWidth = new ArrayList<>(
                Arrays.asList(
                        5000, 5000, 10000, 10000, 5000,
                        10000, 3000, 3000, 2000, 2000,
                        2000, 2000, 2000, 2000, 2000,
                        2000, 2000, 5000, 5000, 5000,
                        2000));

        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = resultData.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        sheet.setAutoFilter(CellRangeAddress.valueOf(reference.formatAsString()));
        // title rows

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

        Cell cell;
        for (int i = 0; i < resultData.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            IndicatorExecutionDetailedDTO ie = resultData.get(i);

            cell = rowData.createCell(0);
            cell.setCellValue(ie.getImplementation_type());
            cell = rowData.createCell(1);
            cell.setCellValue(ie.getArea());
            cell = rowData.createCell(2);
            cell.setCellValue(ie.getStatement());
            cell = rowData.createCell(3);
            cell.setCellValue(ie.getStatement_project());
            cell = rowData.createCell(4);
            cell.setCellValue(ie.getIndicator_type());
            cell = rowData.createCell(5);
            cell.setCellValue(ie.getIndicator());
            cell = rowData.createCell(6);
            cell.setCellValue(ie.getFrecuency());
            cell = rowData.createCell(7);
            cell.setCellValue(ie.getImplementers());
            cell = rowData.createCell(8);
            if (ie.getTotal_execution() != null) {
                cell.setCellValue(ie.getTotal_execution().intValue());
            }
            cell = rowData.createCell(9);
            if (ie.getTarget() != null) {
                cell.setCellValue(ie.getTarget().intValue());
            }
            cell = rowData.createCell(10);
            if (ie.getExecution_percentage() != null) {
                cell.setCellValue(ie.getExecution_percentage().intValue());
                cell.setCellStyle(cellStylePercentage);
            }
            cell = rowData.createCell(11);
            cell.setCellValue(ie.getQuarter());
            cell = rowData.createCell(12);
            if (ie.getQuarter_execution() != null) {
                cell.setCellValue(ie.getQuarter_execution().intValue());
            }
            cell = rowData.createCell(13);
            if (ie.getQuarter_target() != null) {
                cell.setCellValue(ie.getQuarter_target().intValue());
            }
            cell = rowData.createCell(14);
            if (ie.getQuarter_percentage() != null) {
                cell.setCellValue(ie.getQuarter_percentage().floatValue());
                cell.setCellStyle(cellStylePercentage);
            }
            cell = rowData.createCell(15);
            cell.setCellValue(ie.getMonth());
            cell = rowData.createCell(16);
            if (ie.getMonth_execution() != null) {
                cell.setCellValue(ie.getMonth_execution().intValue());
            }
            cell = rowData.createCell(17);
            cell.setCellValue(ie.getDissagregation_type());
            cell = rowData.createCell(18);
            cell.setCellValue(ie.getDissagregation_level1());
            cell = rowData.createCell(19);
            cell.setCellValue(ie.getDissagregation_level2());
            cell = rowData.createCell(20);
            if (ie.getValue() != null) {
                cell.setCellValue(ie.getValue().intValue());
            }
        }

        return wb;

    }

    public SXSSFWorkbook getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(Long projectId) {


        long lStartTime = System.nanoTime();
        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(projectId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds (query): " + (lEndTime - lStartTime) / 1000000000);
        List<String> titles = new ArrayList<>(
                Arrays.asList("Tipo de Implementacion", "Area", "Declaracion", "Declaracion de Proyecto", "Tipo de Indicador",
                        "Indicador", "Frecuencia", "Implementador", "Ejecucion Total", //"Meta Total",
                        /*"% de Ejecucion Total",*/ "Trimestre", "Ejecucion Trimestral", /*"Meta Trimestral", "% de Ejecucion Trimestral",*/
                        "Mes", "Ejecucion Mensual", "Tipo de Desagregacion", "Desagregacion Nivel 1", "Desagregacion Nivel 2",
                        "Valor Reportado"));

        List<Integer> titlesWidth = new ArrayList<>(
                Arrays.asList(
                        5000, 5000, 10000, 10000, 5000,
                        10000, 3000, 3000, 2000, //2000,
                        /*2000,*/ 2000, 2000, // 2000, 2000,
                        2000, 2000, 5000, 5000, 5000,
                        2000));

        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = resultData.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        sheet.setAutoFilter(CellRangeAddress.valueOf(reference.formatAsString()));
        // title rows
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

        Cell cell;
        for (int i = 0; i < resultData.size(); i++) {
            SXSSFRow rowData = sheet.createRow(i + 1);
            IndicatorExecutionDetailedDTO ie = resultData.get(i);

            cell = rowData.createCell(0);
            cell.setCellValue(ie.getImplementation_type());
            cell = rowData.createCell(1);
            cell.setCellValue(ie.getArea());
            cell = rowData.createCell(2);
            cell.setCellValue(ie.getStatement());
            cell = rowData.createCell(3);
            cell.setCellValue(ie.getStatement_project());
            cell = rowData.createCell(4);
            cell.setCellValue(ie.getIndicator_type());
            cell = rowData.createCell(5);
            cell.setCellValue(ie.getIndicator());
            cell = rowData.createCell(6);
            cell.setCellValue(ie.getFrecuency());
            cell = rowData.createCell(7);
            cell.setCellValue(ie.getImplementers());
            cell = rowData.createCell(8);
            if (ie.getTotal_execution() != null) {
                cell.setCellValue(ie.getTotal_execution().intValue());
            }

            cell = rowData.createCell(9);
            cell.setCellValue(ie.getQuarter());
            cell = rowData.createCell(10);
            if (ie.getQuarter_execution() != null) {
                cell.setCellValue(ie.getQuarter_execution().intValue());
            }
            cell = rowData.createCell(11);
            cell.setCellValue(ie.getMonth());
            cell = rowData.createCell(12);
            if (ie.getMonth_execution() != null) {
                cell.setCellValue(ie.getMonth_execution().intValue());
            }
            cell = rowData.createCell(13);
            cell.setCellValue(ie.getDissagregation_type());
            cell = rowData.createCell(14);
            cell.setCellValue(ie.getDissagregation_level1());
            cell = rowData.createCell(15);
            cell.setCellValue(ie.getDissagregation_level2());
            cell = rowData.createCell(16);
            if (ie.getValue() != null) {
                cell.setCellValue(ie.getValue().intValue());
            }
        }

        return wb;

    }
}
