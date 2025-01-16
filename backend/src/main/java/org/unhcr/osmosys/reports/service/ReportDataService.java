package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.webservice.webModel.UserWeb;
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
import org.apache.poi.xssf.usermodel.*;
import org.jboss.logging.Logger;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
import org.unhcr.osmosys.daos.ReportDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.cubeDTOs.OfficeDTO;
import org.unhcr.osmosys.model.enums.Frecuency;
import org.unhcr.osmosys.model.enums.TimeStateEnum;
import org.unhcr.osmosys.model.reportDTOs.IndicatorExecutionDetailedDTO;
import org.unhcr.osmosys.model.reportDTOs.IndicatorExecutionTagDTO;
import org.unhcr.osmosys.model.reportDTOs.LaterReportDTO;
import org.unhcr.osmosys.reports.model.IndicatorReportProgramsDTO;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.webServices.model.IndicatorExecutionWeb;
import org.unhcr.osmosys.webServices.model.MonthWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
@Stateless
public class ReportDataService {
    private static final Logger LOGGER = Logger.getLogger(ReportDataService.class);
    @Inject
    IndicatorExecutionService indicatorExecutionService;


    @Inject
    ReportDao reportDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    public List<Map<String, Object>> indicatorExecutionsProjectsReportsByPeriodId(Long periodId) throws GeneralAppException {
        List<IndicatorExecutionWeb> indicatorExecutions = this.indicatorExecutionService.getActiveProjectIndicatorExecutionsByPeriodId(periodId);
        return this.indicatorExecutionsProjectsReports(indicatorExecutions);
    }

    public List<Map<String, Object>> indicatorExecutionsProjectsReportsByProjectId(Long projectId) throws GeneralAppException {
        List<IndicatorExecutionWeb> indicatorExecutions = this.indicatorExecutionService.getActivePartnersIndicatorExecutionsByProjectId(projectId);
        return this.indicatorExecutionsProjectsToProjectReport(indicatorExecutions);

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
            map.put("late", ie.getLate().equals(TimeStateEnum.LATE) ? "Si" : "No");
            r.add(map);
        }

        return r;

    }

    public List<Map<String, Object>> indicatorExecutionsProjectsToProjectReport(List<IndicatorExecutionWeb> indicatorExecutions) {
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
            map.put("late", ie.getLate().equals(TimeStateEnum.LATE) ? "Si" : "No");

            map.put("ie_id", ie.getId());
            if (ie.getProject() != null) {
                map.put("implementation_type", "Socios");
                map.put("statement_project", ie.getProjectStatement() != null ?
                        ie.getProjectStatement().getCode() + " - " + ie.getProjectStatement().getDescription()
                        : null);
                map.put("implementers", ie.getProject().getOrganization().getAcronym());
            } else {
                map.put("implementation_type", "Directa");
                map.put("statement_project", null);
                map.put("implementers", ie.getReportingOffice().getAcronym());
            }
            map.put("statement", ie.getIndicator() != null && ie.getIndicator().getStatement() != null ? ie.getIndicator().getStatement().getCode() + " - " + ie.getIndicator().getStatement().getDescription() : null);
            map.put("indicator_type", ie.getIndicatorType().getLabel());
            map.put("indicator", ie.getIndicator() == null ? "General" : ie.getIndicator().getCode() + " - " + ie.getIndicator().getDescription());
            map.put("category", ie.getIndicator() != null ? ie.getIndicator().getCategory() : null);
            map.put("frecuency", ie.getIndicator() != null && ie.getIndicator().getFrecuency() != null ? ie.getIndicator().getFrecuency().getLabel() : Frecuency.MENSUAL.getLabel());
            map.put("dissagregations", ie.getIndicator().getDissagregationsAssignationToIndicator().stream().map(dissagregationAssignationToIndicatorWeb -> dissagregationAssignationToIndicatorWeb.getDissagregationType().getLabel()).collect(Collectors.joining(", ")));
            map.put("custom_dissagregations", ie.getIndicator().getCustomDissagregationAssignationToIndicators() != null ?
                    ie.getIndicator().getCustomDissagregationAssignationToIndicators()
                            .stream()
                            .map(customDissagregationAssignationToIndicatorWeb -> customDissagregationAssignationToIndicatorWeb.getCustomDissagregation().getName())
                            .collect(Collectors.joining(", "))
                    : null
            );
            map.put("total_execution", ie.getTotalExecution());
            map.put("target", ie.getTarget());
            map.put("execution_percentage", ie.getExecutionPercentage());
            String monthsLate = ie.getQuarters().stream().flatMap(quarterWeb -> quarterWeb.getMonths().stream())
                    .filter(monthWeb -> monthWeb.getLate().equals(TimeStateEnum.LATE))
                    .sorted(Comparator.comparingInt(o -> o.getMonth().getOrder()))
                    .map(monthWeb -> monthWeb.getMonth().getLabel())
                    .collect(Collectors.joining(", "));
            map.put("months_late", monthsLate);
            map.put("is_late", StringUtils.isNotBlank(monthsLate) ? "Retrasado" : "A tiempo");
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

    private SXSSFWorkbook getReportFromIndicatorExecutionTagdDTO(List<IndicatorExecutionTagDTO> resultData, Tags tag, Period period) {
        List<Integer> titlesWidth = Arrays.asList(3000,3000,5000,3000,3000,3000,3000);

        List<Indicator> tagIndicators = tag.getIndicatorTagAssignations().stream().filter(ie -> ie.getState().equals(State.ACTIVO)).map(IndicatorTagAssignation::getIndicator).collect(Collectors.toList());

        List<String> indicators = tagIndicators.stream().map(Indicator::getCode).collect(Collectors.toList());
        List<String> titles = new ArrayList<>(Arrays.asList("Periodo", "Trimestre", "Mes"));
        titles.addAll(indicators);
        if(!tag.getOperation().isEmpty())
            titles.add(tag.getOperation());

        List<List<String>> indicatorsList = new ArrayList<>();
        indicators.forEach(indicator -> {
            List<String> ind = new ArrayList<>();
            ind.add(indicator);
            for (int i = 1; i < 13; i++) {
                int finalI = i;
                Optional<IndicatorExecutionTagDTO> first = resultData.stream().filter(r -> monthtoNumber(r.getMonth()) == finalI && r.getIndicator().startsWith(indicator)).findFirst();
                if (first.isPresent()) {
                    String value = first.get().getTotalValue().toString();
                    System.out.println (i + " - " + indicator + " - " + value);
                    ind.add(value);
                }
                else {
                    ind.add("0");
                }
            }
            //resultData.stream().filter(r -> r.getMonthOrder() == finalI && r.getIndicator().startsWith(indicator));
            indicatorsList.add(ind);
        });
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
//            if(titlesWidth.size() < titles.size())
//                sheet.setColumnWidth(i, titlesWidth.get(i));
//            else
                sheet.setColumnWidth(i, 3000);
        }

        long lStartTime2 = System.nanoTime();
        for (int i = 1; i <= 12; i++) {
            SXSSFRow rowData = sheet.createRow(i);
            String mesEnEspanol = Month.of(i).getDisplayName(TextStyle.FULL, new Locale("es", "ES"));

            //Títulos de los indicadores
            for (int j = 0; j < indicatorsList.size(); j++) {
                List<String> indReport = indicatorsList.get(j);
                if(!indReport.get(0).isEmpty()) {
                    Cell cell = rowData.createCell(3 +j);
                    cell.setCellValue(indReport.get(i));
                }
            }
            //Totales
            int finalI = i;

            List<Integer> monthValues = indicatorsList.stream()
                    .filter(subArray -> subArray.size() > finalI)
                    .map(subArray -> Integer.parseInt(subArray.get(finalI))).collect(Collectors.toList());

            switch (tag.getOperation()) {
                case "Suma":
                    Cell cell = rowData.createCell(3 + indicatorsList.size());
                    cell.setCellValue(monthValues.stream().mapToInt(Integer::intValue).sum());
                    break;
                case "Máximo":
                    Cell cell1 = rowData.createCell(3 + indicatorsList.size());
                    OptionalInt maximo = monthValues.stream().mapToInt(Integer::intValue).max();
                    if(maximo.isPresent()) {
                        cell1.setCellValue(maximo.getAsInt());
                    } else {
                        cell1.setCellValue("No aplica");
                    }
                    break;
                case "Mínimo":
                    Cell cell3 = rowData.createCell(3 + indicatorsList.size());
                    OptionalInt minimo = monthValues.stream().mapToInt(Integer::intValue).min();
                    if(minimo.isPresent()) {
                        cell3.setCellValue(minimo.getAsInt());
                    } else {
                        cell3.setCellValue("No aplica");
                    }
                    break;
                case "Promedio":
                    Cell cell4 = rowData.createCell(3 + indicatorsList.size());
                    OptionalDouble average = monthValues.stream().mapToInt(Integer::intValue).average();
                    if(average.isPresent()) {
                        cell4.setCellValue(average.getAsDouble());
                    } else {
                        cell4.setCellValue("No aplica");
                    }
                    break;
                default:
                    break;
            }

            //Filas de periodos, trimestre, mes
            for (int j = 0; j <= titles.size(); j++) {
                if(j == 0){
                    Cell cell = rowData.createCell(j);
                    cell.setCellValue(period.getYear());

                }
                if(j == 1){
                    Cell cell = rowData.createCell(j);
                    int trimestres = 0;
                    if(i >= 1 && i <= 4 )
                        trimestres = 1;
                    if(i >= 4 && i <= 7 )
                        trimestres = 2;
                    if(i >= 7 && i <= 10 )
                        trimestres = 3;
                    if(i >= 10 && i <= 12 )
                        trimestres = 4;

                    cell.setCellValue(trimestres);
                }
                if(j == 2){
                    Cell cell = rowData.createCell(j);
                    cell.setCellValue(mesEnEspanol);
                }
            }
        }
        long lEndTime2 = System.nanoTime();
        LOGGER.info("Elapsed time in seconds(excel construction): " + (lEndTime2 - lStartTime2) / 1000000000);
        return wb;
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
                this.setDataFromIndicatorExecutionDetailedDTO(titles.get(t), cell, ie, cellStylePercentage);
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

    public SXSSFWorkbook getTagReport(Tags tag, Period period) {
        long lStartTime = System.nanoTime();
        List<IndicatorExecutionTagDTO> resultData = this.reportDao.getTagReport(tag, period);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds:(query))" + (lEndTime - lStartTime) / 1000000000);
        return getReportFromIndicatorExecutionTagdDTO(resultData, tag, period);
    }

    public SXSSFWorkbook getPartnerDetailedByProjectId(Long projectId) {
        long lStartTime = System.nanoTime();
        List<IndicatorExecutionDetailedDTO> resultData = this.reportDao.getPartnerDetailedByProjectId(projectId);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds:(query))" + (lEndTime - lStartTime) / 1000000000);

        List<Integer> columnsToRemove = new ArrayList<>(
                Arrays.asList(
                        0, 1, 2, 3, 4, 5, 27, 28, 22, 23
                )
        );

        return getReportFromIndicatorExecutionDetailedDTO(resultData, columnsToRemove);
    }

    private void setDataFromIndicatorExecutionDetailedDTO(String title, Cell cell, IndicatorExecutionDetailedDTO ie, CellStyle cellStylePercentage) {

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

    public XSSFWorkbook getPartnerLateReportByProjectId(Long projectId, Integer currentYear, Integer currentMonth) throws GeneralAppException {

        List<IndicatorExecution> data = this.reportDao.getPartnerLateReportByProjectId(projectId, currentYear, currentMonth);
        List<LaterReportDTO> dataDto;
        if (CollectionUtils.isEmpty(data)) {
            return null;
        } else {
            dataDto = this.indicatorExecutionsToLateReportDtos(data);
            if (CollectionUtils.isEmpty(dataDto)) {
                return null;
            }
            return this.getLateReport(dataDto, true, false);
        }
    }

    public XSSFWorkbook getPartnerLateReviewByProjectId(Long projectId, Integer currentYear, Integer currentMonth) {
        List<LaterReportDTO> data = this.reportDao.getPartnerLateReviewByProjectId(projectId, currentYear, currentMonth);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, true, true);
    }

    public XSSFWorkbook getPartnerLateReportByFocalPointId(Long focalPointId, Integer currentYear, Integer currentMonth) throws GeneralAppException {
        List<IndicatorExecution> data = this.reportDao.getPartnerLateReviewReportByFocalPointId(focalPointId, currentYear, currentMonth);
        List<LaterReportDTO> dataDto;
        if (CollectionUtils.isEmpty(data)) {
            return null;
        } else {
            dataDto = this.indicatorExecutionsToLateReportDtos(data);
            if (CollectionUtils.isEmpty(dataDto)) {
                return null;
            }
            return this.getLateReport(dataDto, false, false);
        }
    }


    public XSSFWorkbook getPartnerLateReviewReportByProjectId(Long projectId, Integer currentYear, Integer currentMonth) {
        List<LaterReportDTO> data = this.reportDao.getPartnerLateReviewByProjectId(projectId, currentYear, currentMonth);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, true, true);
    }

    public XSSFWorkbook getLateReport(List<LaterReportDTO> data, Boolean partner, Boolean review) {


        List<Integer> titlesWidth = this.getTitlesWidthLateReport(partner, review);


        List<String> titles = this.getTitlesLateReport(partner, review);
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        // Set which area the table should be placed in
        int NB_ROWS = data.size();
        int NB_COLS = titles.size() - 1;
        AreaReference reference = wb.getCreationHelper()
                .createAreaReference(
                        new CellReference(0, 0),
                        new CellReference(NB_ROWS, NB_COLS));
        // title rows
        // Create
        XSSFTable table = sheet.createTable(reference);
        table.setName("data_export");
        table.setDisplayName("data_export");
        // For now, create the initial style in a low-level way
        table.getCTTable().addNewTableStyleInfo();
        table.getCTTable().getTableStyleInfo().setName("TableStyleMedium2");
        table.getCTTable().setAutoFilter(CTAutoFilter.Factory.newInstance());
        // Style the table
        XSSFTableStyleInfo style = (XSSFTableStyleInfo) table.getStyle();
        style.setName("TableStyleMedium2");
        style.setShowColumnStripes(false);
        style.setShowRowStripes(true);
        style.setFirstColumn(false);
        style.setLastColumn(false);

        DataFormat format = wb.createDataFormat();
        CellStyle cellStylePercentage;
        cellStylePercentage = wb.createCellStyle();
        cellStylePercentage.setDataFormat(format.getFormat("0%"));


        Row rowTitle = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = rowTitle.createCell(i);
            cell.setCellValue(titles.get(i));
            sheet.setColumnWidth(i, titlesWidth.get(i));
        }
        for (int i = 0; i < data.size(); i++) {
            XSSFRow rowData = sheet.createRow(i + 1);
            LaterReportDTO dataRow = data.get(i);

            for (int t = 0; t < titles.size(); t++) {
                Cell cell = rowData.createCell(t);
                this.setDataFromLaterReportDTO( titles.get(t), cell, dataRow);
                CellStyle styleCell = cell.getCellStyle();
                styleCell.setWrapText(true);
            }
            rowData.setHeight((short) -1);

        }

        return wb;
    }

    private void setDataFromLaterReportDTO( String title, Cell cell, LaterReportDTO dataRow) {
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
                this.setDataFromIndicatorExecutionDetailedDTO(titles.get(t), cell, ie, cellStylePercentage);
            }

        }
        long lEndTime2 = System.nanoTime();
        LOGGER.info("Elapsed time in seconds(excel construction): " + (lEndTime2 - lStartTime2) / 1000000000);
        return wb;
    }

    public XSSFWorkbook getPartnerLateReportByPartnerId(Long partnerId, Integer currentYear, Integer currentMonthYearOrder) throws GeneralAppException {

        List<LaterReportDTO> data = this.reportDao.getPartnerLateReportByPartnerId(partnerId, currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            throw new GeneralAppException("El socio no tiene retrazos ", Response.Status.NOT_FOUND.getStatusCode());
        }
        return this.getLateReport(data, true, false);


    }

    public XSSFWorkbook getDirectImplementationLateReportByResponsableId(Long periodId, Long responsableId, Integer currentYear, Integer currentMonthYearOrder) throws GeneralAppException {

        List<IndicatorExecution> data = this.reportDao.getDirectImplementationLateReportByResponsableId(periodId, responsableId, currentYear, currentMonthYearOrder);
        List<LaterReportDTO> dataDto;
        if (CollectionUtils.isEmpty(data)) {
            return null;
        } else {
            dataDto = this.indicatorExecutionsToLateReportDtos(data);
            if (CollectionUtils.isEmpty(dataDto)) {
                return null;
            }
            return this.getLateReport(dataDto, false, false);
        }
    }

    public XSSFWorkbook getDirectImplementationLateReviewReportBySupervisorId(Long responsableId, Integer currentYear, Integer currentMonthYearOrder) {

        List<LaterReportDTO> data = this.reportDao.getDirectImplementationLateReviewReportBySupervisorId(responsableId, currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, false, true);
    }

    public XSSFWorkbook getDirectImplementationLateReportBySupervisorId(Long periodId, Long responsableId, Integer currentYear, Integer currentMonthYearOrder) throws GeneralAppException {

        List<IndicatorExecution> data = this.reportDao.getDirectImplementationLateReportBySupervisorId(periodId, responsableId, currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        } else {
            List<LaterReportDTO> dataDto = this.indicatorExecutionsToLateReportDtos(data);
            if (CollectionUtils.isEmpty(dataDto)) {
                return null;
            }
            return this.getLateReport(dataDto, false, false);
        }
    }

    public XSSFWorkbook getOfficeLateDirectImplementationReport(Long periodId, Long officeId, Integer currentYear, Integer currentMonthYearOrder) throws GeneralAppException {

        List<IndicatorExecution> data = this.reportDao.getOfficeLateDirectImplementationReport(periodId, officeId, currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        } else {
            List<LaterReportDTO> dataDto = this.indicatorExecutionsToLateReportDtos(data);
            if (CollectionUtils.isEmpty(dataDto)) {
                return null;
            }
            return this.getLateReport(dataDto, false, false);
        }
    }

    public XSSFWorkbook getAllLateReviewReportDirectImplementation(Integer currentYear, Integer currentMonthYearOrder) {

        List<LaterReportDTO> data = this.reportDao.getAllLateReviewReportDirectImplementation(currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, false, true);
    }

    public XSSFWorkbook getAllLateReportDirectImplementation(Long periodId, Integer currentYear, Integer currentMonthYearOrder) throws GeneralAppException {

        List<IndicatorExecution> data = this.reportDao.getAllLateReportDirectImplementation(periodId, currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        } else {
            List<LaterReportDTO> dataDto = this.indicatorExecutionsToLateReportDtos(data);
            dataDto = dataDto.stream()
                    .sorted(Comparator.comparing(LaterReportDTO::getIndicator_code))
                    .sorted(Comparator.comparing(LaterReportDTO::getImplementer))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dataDto)) {
                return null;
            }
            return this.getLateReport(dataDto, false, false);
        }
    }


    public XSSFWorkbook getAllLateReportPartners(Long periodId, int currentMonth, int currentYear) throws GeneralAppException {

        List<IndicatorExecution> data = this.reportDao.getAllLateReportPartners(periodId, currentMonth, currentYear);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        } else {
            List<LaterReportDTO> dataDto = this.indicatorExecutionsToLateReportDtos(data);
            if (CollectionUtils.isEmpty(dataDto)) {
                return null;
            }
            return this.getLateReport(dataDto, true, false);
        }
    }

    private List<LaterReportDTO> indicatorExecutionsToLateReportDtos(List<IndicatorExecution> indicatorExecutions) throws GeneralAppException {
        List<LaterReportDTO> r = new ArrayList<>();
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            LaterReportDTO lateDto = this.indicatorExecutionToLateReportDto(indicatorExecution);
            if (StringUtils.isNotBlank(lateDto.getLate_months())) {
                r.add(lateDto);
            }
        }
        return r;
    }

    private LaterReportDTO indicatorExecutionToLateReportDto(IndicatorExecution indicatorExecution) throws GeneralAppException {
        IndicatorExecutionWeb iew = this.modelWebTransformationService.indicatorExecutionToIndicatorExecutionWeb(indicatorExecution, true);
        String lateMonths = iew.getQuarters().stream()
                .flatMap(quarterWeb -> quarterWeb.getMonths().stream())
                .filter(monthWeb -> monthWeb.getLate().equals(TimeStateEnum.LATE) || monthWeb.getLate().equals(TimeStateEnum.SOON_REPORT))
                .sorted(Comparator.comparingInt(MonthWeb::getOrder))
                .map(monthWeb -> monthWeb.getMonth().getLabel()).collect(Collectors.joining(", "));
        if (iew.getProject() != null) {
            return new LaterReportDTO(
                    iew.getProject().getName(),
                    iew.getProject().getOrganization().getAcronym(),
                    iew.getIndicator().getCode(),
                    iew.getIndicator().getDescription(),
                    iew.getIndicator().getCategory(),
                    lateMonths,
                    iew.getProject().getFocalPoints().stream().map(UserWeb::getName).collect(Collectors.joining())
            );
        } else {
            return new LaterReportDTO(
                    iew.getReportingOffice().getAcronym(),
                    iew.getIndicator().getCode(),
                    iew.getIndicator().getDescription(),
                    iew.getIndicator().getCategory(),
                    lateMonths,
                    iew.getSupervisorUser().getName(),
                    iew.getAssignedUser().getName(),
                    null
            );
        }

    }

    public XSSFWorkbook getAllLateReviewPartners(Integer currentYear, Integer currentMonthYearOrder) {

        List<LaterReportDTO> data = this.reportDao.getAllLateReviewPartners(currentYear, currentMonthYearOrder);
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return this.getLateReport(data, true, true);
    }

    private int monthtoNumber(String month) {
        switch (month.toLowerCase()) {
            case "enero":
                return 1;
            case "febrero":
                return 2;
            case "marzo":
                return 3;
            case "abril":
                return 4;
            case "mayo":
                return 5;
            case "junio":
                return 6;
            case "julio":
                return 7;
            case "agosto":
                return 8;
            case "septiembre":
                return 9;
            case "octubre":
                return 10;
            case "noviembre":
                return 11;
            case "diciembre":
                return 12;
            default:
                return -1; // Mes no válido
        }
    }

// todo
/*
    public List<IndicatorReportProgramsDTO> getIndicatorReportProgramsByProjectId(Long projectId) {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionService.getIndicatorExecutionsByProjectId(projectId);
        indicatorExecutions = indicatorExecutions.stream().filter(
                indicatorExecution -> {
                    Optional<DissagregationType> r = indicatorExecution.getDissagregationsAssignationsToIndicatorExecutions()
                            .stream()
                            .filter(dissagregationAssignationToIndicatorExecution -> dissagregationAssignationToIndicatorExecution.getState().equals(State.ACTIVO))
                            .map(DissagregationAssignationToIndicatorExecution::getDissagregationType)
                            .filter(dissagregationType ->
                                    dissagregationType.equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_Y_GENERO)
                                            || dissagregationType.equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO)
                                            || dissagregationType.equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO)
                            )
                            .findFirst();
                    return r.isPresent();
                }).collect(Collectors.toList());

        return this.indicatorExecutionsToIndicatorReportPrograms(indicatorExecutions);
    }*/

    private List<IndicatorReportProgramsDTO> indicatorExecutionsToIndicatorReportPrograms(List<IndicatorExecution> indicatorExecutions) {
        List<IndicatorReportProgramsDTO> r = new ArrayList<>();
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            // r.add(this.indicatorExecutionsToIndicatorReportPrograms(indicatorExecution));
        }
        return r;
    }
/*
    private IndicatorReportProgramsDTO indicatorExecutionsToIndicatorReportPrograms(IndicatorExecution ie) {
        IndicatorReportProgramsDTO r = new IndicatorReportProgramsDTO();
        r.setIeId(ie.getId());
        if (ie.getProjectStatement() != null) {
            r.setStatement(ie.getProjectStatement().getCode() + "-" + ie.getProjectStatement().getDescription());
        } else if (ie.getIndicator() != null) {
            r.setStatement(ie.getIndicator().getStatement().getCode() + "-" + ie.getIndicator().getStatement().getDescription());
        }

        if (ie.getIndicator() != null) {
            r.setIndicator(ie.getIndicator().getCode() + "-" + ie.getIndicator().getDescription());
        } else {
            r.setIndicator(ie.getPeriod().getGeneralIndicator().getDescription());
        }

        r.setTotalExecution(ie.getTotalExecution() == null ? 0 : ie.getTotalExecution().toBigInteger().intValueExact());
        r.setTotalTarget(ie.getTarget() == null ? 0 : ie.getTarget().toBigInteger().intValueExact());
        List<IndicatorValue> indicatorValues = this.indicatorValueService.getByIndicatorExecutionId(r.getIeId());

        DissagregationType dissagregation = ie.getDissagregationsAssignationsToIndicatorExecutions()
                .stream()
                .filter(dissagregationAssignationToIndicatorExecution -> dissagregationAssignationToIndicatorExecution.getState().equals(State.ACTIVO))
                .map(DissagregationAssignationToIndicatorExecution::getDissagregationType)
                .filter(dissagregationType ->
                        dissagregationType.equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_Y_GENERO)
                                || dissagregationType.equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO)
                                || dissagregationType.equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO)
                )
                .findFirst().orElse(null);

        //noinspection ConstantConditions
        r.setRefugeesInfantMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.INFANTES, GenderType.MASCULINO));
        r.setRefugeesInfantFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.INFANTES, GenderType.FEMENINO));
        r.setRefugeesInfantOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.INFANTES, GenderType.OTRO));
        r.setRefugeesNNAMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.NINOS, GenderType.MASCULINO));
        r.setRefugeesNNAFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.NINOS, GenderType.FEMENINO));
        r.setRefugeesNNAOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.NINOS, GenderType.OTRO));
        r.setRefugeesAdultMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.ADULTOS, GenderType.MASCULINO));
        r.setRefugeesAdultFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.ADULTOS, GenderType.FEMENINO));
        r.setRefugeesAdultOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.ADULTOS, GenderType.OTRO));
        r.setRefugeesElderlyMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.ADULTOS_MAYORES, GenderType.MASCULINO));
        r.setRefugeesElderlyFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.ADULTOS_MAYORES, GenderType.FEMENINO));
        r.setRefugeesElderlyOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.REFUGIADOS, AgeType.ADULTOS_MAYORES, GenderType.OTRO));

        r.setAsylumSeekersInfantMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.INFANTES, GenderType.MASCULINO));
        r.setAsylumSeekersInfantFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.INFANTES, GenderType.FEMENINO));
        r.setAsylumSeekersInfantOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.INFANTES, GenderType.OTRO));
        r.setAsylumSeekersNNAMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.NINOS, GenderType.MASCULINO));
        r.setAsylumSeekersNNAFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.NINOS, GenderType.FEMENINO));
        r.setAsylumSeekersNNAOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.NINOS, GenderType.OTRO));
        r.setAsylumSeekersAdultMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.ADULTOS, GenderType.MASCULINO));
        r.setAsylumSeekersAdultFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.ADULTOS, GenderType.FEMENINO));
        r.setAsylumSeekersAdultOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.ADULTOS, GenderType.OTRO));
        r.setAsylumSeekersElderlyMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.ADULTOS_MAYORES, GenderType.MASCULINO));
        r.setAsylumSeekersElderlyFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.ADULTOS_MAYORES, GenderType.FEMENINO));
        r.setAsylumSeekersElderlyOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.SOLICITANTES_DE_ASILO, AgeType.ADULTOS_MAYORES, GenderType.OTRO));

        r.setVenezuelanAbroadInfantMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.INFANTES, GenderType.MASCULINO));
        r.setVenezuelanAbroadInfantFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.INFANTES, GenderType.FEMENINO));
        r.setVenezuelanAbroadInfantOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.INFANTES, GenderType.OTRO));
        r.setVenezuelanAbroadNNAMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.NINOS, GenderType.MASCULINO));
        r.setVenezuelanAbroadNNAFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.NINOS, GenderType.FEMENINO));
        r.setVenezuelanAbroadNNAOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.NINOS, GenderType.OTRO));
        r.setVenezuelanAbroadAdultMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.ADULTOS, GenderType.MASCULINO));
        r.setVenezuelanAbroadAdultFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.ADULTOS, GenderType.FEMENINO));
        r.setVenezuelanAbroadAdultOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.ADULTOS, GenderType.OTRO));
        r.setVenezuelanAbroadElderlyMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.ADULTOS_MAYORES, GenderType.MASCULINO));
        r.setVenezuelanAbroadElderlyFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.ADULTOS_MAYORES, GenderType.FEMENINO));
        r.setVenezuelanAbroadElderlyOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.VENEZOLANOS_DESPLAZADOS, AgeType.ADULTOS_MAYORES, GenderType.OTRO));

        r.setHostCommunityInfantMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.INFANTES, GenderType.MASCULINO));
        r.setHostCommunityInfantFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.INFANTES, GenderType.FEMENINO));
        r.setHostCommunityInfantOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.INFANTES, GenderType.OTRO));
        r.setHostCommunityNNAMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.NINOS, GenderType.MASCULINO));
        r.setHostCommunityNNAFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.NINOS, GenderType.FEMENINO));
        r.setHostCommunityNNAOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.NINOS, GenderType.OTRO));
        r.setHostCommunityAdultMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.ADULTOS, GenderType.MASCULINO));
        r.setHostCommunityAdultFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.ADULTOS, GenderType.FEMENINO));
        r.setHostCommunityAdultOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.ADULTOS, GenderType.OTRO));
        r.setHostCommunityElderlyMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.ADULTOS_MAYORES, GenderType.MASCULINO));
        r.setHostCommunityElderlyFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.ADULTOS_MAYORES, GenderType.FEMENINO));
        r.setHostCommunityElderlyOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.COMUNIDAD_DE_ACOGIDA, AgeType.ADULTOS_MAYORES, GenderType.OTRO));

        r.setOthersOfInterestInfantMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.INFANTES, GenderType.MASCULINO));
        r.setOthersOfInterestInfantFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.INFANTES, GenderType.FEMENINO));
        r.setOthersOfInterestInfantOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.INFANTES, GenderType.OTRO));
        r.setOthersOfInterestNNAMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.NINOS, GenderType.MASCULINO));
        r.setOthersOfInterestNNAFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.NINOS, GenderType.FEMENINO));
        r.setOthersOfInterestNNAOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.NINOS, GenderType.OTRO));
        r.setOthersOfInterestAdultMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.ADULTOS, GenderType.MASCULINO));
        r.setOthersOfInterestAdultFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.ADULTOS, GenderType.FEMENINO));
        r.setOthersOfInterestAdultOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.ADULTOS, GenderType.OTRO));
        r.setOthersOfInterestElderlyMale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.ADULTOS_MAYORES, GenderType.MASCULINO));
        r.setOthersOfInterestElderlyFemale(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.ADULTOS_MAYORES, GenderType.FEMENINO));
        r.setOthersOfInterestElderlyOther(this.getProgramValue(indicatorValues, dissagregation, PopulationType.OTROS_DE_INTERES, AgeType.ADULTOS_MAYORES, GenderType.OTRO));

        return r;

    }*/
/*

    private Integer getProgramValue(List<IndicatorValue> indicatorValues, DissagregationType dissagregationType, PopulationType populationType, AgeType ageType, GenderType genderType) {
        switch (dissagregationType) {
            case TIPO_POBLACION_LUGAR_EDAD_Y_GENERO:
                return indicatorValues.stream()
                        .filter(indicatorValue -> indicatorValue.getDissagregationType().equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_Y_GENERO))
                        .filter(indicatorValue -> indicatorValue.getPopulationType().equals(populationType)
                                && indicatorValue.getAgeType().equals(ageType)
                                && indicatorValue.getGenderType().equals(genderType))
                        .map(IndicatorValue::getValue)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).toBigInteger().intValueExact();
            case TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO:
                List<AgePrimaryEducationType> agesTypes = new ArrayList<>();
                switch (ageType) {
                    case INFANTES:
                        agesTypes.add(AgePrimaryEducationType.PREPRIMARIA);
                        break;
                    case NINOS:
                        agesTypes.add(AgePrimaryEducationType.PRIMARIA);
                        agesTypes.add(AgePrimaryEducationType.SECUNDARIA);
                        break;
                    case ADULTOS:
                        agesTypes.add(AgePrimaryEducationType.ADULTOS);
                        break;
                    case ADULTOS_MAYORES:
                        agesTypes.add(AgePrimaryEducationType.ADULTOS_MAYORES);
                        break;

                }
                return indicatorValues.stream()
                        .filter(indicatorValue -> indicatorValue.getDissagregationType().equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO))
                        .filter(indicatorValue -> indicatorValue.getPopulationType().equals(populationType)
                                && indicatorValue.getGenderType().equals(genderType)
                                && agesTypes.contains(indicatorValue.getAgePrimaryEducationType())
                        )
                        .map(IndicatorValue::getValue)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).toBigInteger().intValueExact();

            case TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO:
                List<AgeTertiaryEducationType> agesTypesT = new ArrayList<>();
                switch (ageType) {
                    case INFANTES:
                        agesTypesT.add(AgeTertiaryEducationType.INFANTES);
                        break;
                    case NINOS:
                        agesTypesT.add(AgeTertiaryEducationType.NINOS);
                        break;
                    case ADULTOS:
                        agesTypesT.add(AgeTertiaryEducationType.JOVENES_ADULTOS);
                        agesTypesT.add(AgeTertiaryEducationType.ADULTOS);
                        break;
                    case ADULTOS_MAYORES:
                        agesTypesT.add(AgeTertiaryEducationType.ADULTOS_MAYORES);
                        break;

                }
                return indicatorValues.stream()
                        .filter(indicatorValue -> indicatorValue.getDissagregationType().equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO))
                        .filter(indicatorValue -> indicatorValue.getPopulationType().equals(populationType)
                                && indicatorValue.getGenderType().equals(genderType)
                                && agesTypesT.contains(indicatorValue.getAgeTertiaryEducationType())
                        )
                        .map(IndicatorValue::getValue)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add).toBigInteger().intValueExact();
            default:
                return null;
        }
    }

*/

}
