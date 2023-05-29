package org.unhcr.osmosys.services.dataImport;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.utils.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.AreaType;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.model.enums.QuarterEnum;
import org.unhcr.osmosys.services.*;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class ProjectIndicatorsImportService {


    @Inject
    ProjectService projectService;

    @Inject
    StatementService statementService;

    @Inject
    IndicatorService indicatorService;

    @Inject
    IndicatorExecutionService indicatorExecutionService;


    @Inject
    CantonService cantonService;
    @Inject
    ModelWebTransformationService modelWebTransformationService;
    @Inject
    PeriodService periodService;

    @Inject
    FileUtils fileUtils;

    private final static Logger LOGGER = Logger.getLogger(ProjectIndicatorsImportService.class);
    // private static final String FILE_NAME = "D:\\proyectos\\OSMOSYS_2023\\DataImport2023\\partnerisIndicators\\FUDELA SOIB.xlsx";

    private static final String PRODUCT_STATEMENT = "Declaración de Producto";
    private static final String MAIN_ACTIVITIES = "Actividades clave de Producto";
    private static final String PRODUCT_INDICATOR = "Indicadores de Producto";
    private static final String TARGET_T1 = "T1";
    private static final String TARGET_T2 = "T2";
    private static final String TARGET_T3 = "T3";
    private static final String TARGET_T4 = "T4";
    private static final String TOTAL_TARGET = "Meta Total";
    private static final String CANTONS = "Cantones de Ejecución";

    private static final int MAX = 50;

    public void projectIndicatorsImport(Long projectId, ImportFileWeb importFileWeb, boolean quarterlyTarget) throws GeneralAppException {
        byte[] fileContent = this.fileUtils.decodeBase64ToBytes(importFileWeb.getFile());
        InputStream targetStream = new ByteArrayInputStream(fileContent);
        this.projectIndicatorsImport(projectId, targetStream, quarterlyTarget);
    }

    public void projectIndicatorsImport(Long projectId,
                                        InputStream file,
                                        boolean quarterlyTarget
    ) throws GeneralAppException {
        // FileInputStream file = null;
        try {
            Project project = this.projectService.getById(projectId);
            if (project == null) {
                throw new GeneralAppException("El projecto con id " + projectId + " no existe", Response.Status.BAD_REQUEST);

            }
            Integer year = project.getPeriod().getYear();
            // file = new FileInputStream(FILE_NAME);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheet("indicadores_proyecto");
            if (sheet == null) {
                throw new GeneralAppException("No se encontró la hoja indicadores_proyecto en el archivo."
                        , Response.Status.BAD_REQUEST);
            }

            Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheet, quarterlyTarget);
            CellAddress CODE_CELL = titleAdresses.get(PRODUCT_STATEMENT);
            int rowInitial = CODE_CELL.getRow();
            // get INDICATORS
            Integer COL_TARGET_T1 = null;
            Integer COL_TARGET_T2 = null;
            Integer COL_TARGET_T3 = null;
            Integer COL_TARGET_T4 = null;
            Integer COL_TOTAL_TARGET = null;

            int COL_PRODUCT_STATEMENT = titleAdresses.get(PRODUCT_STATEMENT).getColumn();
            int COL_MAIN_ACTIVITIES = titleAdresses.get(MAIN_ACTIVITIES).getColumn();
            int COL_PRODUCT_INDICATOR = titleAdresses.get(PRODUCT_INDICATOR).getColumn();
            if (quarterlyTarget) {
                COL_TARGET_T1 = titleAdresses.get(TARGET_T1).getColumn();
                COL_TARGET_T2 = titleAdresses.get(TARGET_T2).getColumn();
                COL_TARGET_T3 = titleAdresses.get(TARGET_T3).getColumn();
                COL_TARGET_T4 = titleAdresses.get(TARGET_T4).getColumn();
            } else {
                COL_TOTAL_TARGET = titleAdresses.get(TOTAL_TARGET).getColumn();
            }
            int COL_CANTONS = titleAdresses.get(CANTONS).getColumn();


            Iterator<Row> rowIterator = sheet.iterator();
            // get indicators
            Set<Canton> totalLocations = new HashSet<>();
            List<IndicatorExecutionAssigmentWeb> indicatorExecutionAssigmentWebs = new ArrayList<>();
            Map<Integer, List<QuarterWeb>> targetsMap = new HashMap<>();


            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() <= rowInitial) {
                    continue;
                }

                // get produdctStatement
                if (
                        row.getCell(COL_PRODUCT_STATEMENT) == null || row.getCell(COL_PRODUCT_STATEMENT).getCellType() == CellType.BLANK
                                || row.getCell(COL_PRODUCT_STATEMENT) == null || row.getCell(COL_PRODUCT_STATEMENT).getCellType() == CellType.BLANK
                ) {
                    continue;
                }
                // find by year and code
                String productStatmentString = StringUtils.trimToNull(row.getCell(COL_PRODUCT_STATEMENT).getStringCellValue());

                String productStatementCode = StringUtils.split(productStatmentString, " ", 2)[0];

                Statement statement = this.statementService.getByCodeAndPeriodYearAndAreaType(productStatementCode, AreaType.PRODUCTO, year);

                String mainActivities = StringUtils.trimToNull(row.getCell(COL_MAIN_ACTIVITIES).getStringCellValue());

                String productIndicatorString = StringUtils.trimToNull(row.getCell(COL_PRODUCT_INDICATOR).getStringCellValue());
                String indicatorCode = StringUtils.split(productIndicatorString, " ", 2)[0];
                Indicator indicator = this.indicatorService.getByPeriodAndCode(project.getPeriod().getId(), indicatorCode);
                if (indicator == null) {
                    throw new GeneralAppException("No se encontraro el indicador "
                            + indicatorCode + "para el periodo " + project.getPeriod().getYear() + ".", Response.Status.BAD_REQUEST);
                }
                Integer T1I = null;
                Integer T2I = null;
                Integer T3I = null;
                Integer T4I = null;
                Integer Total_TARGET_value = null;
                if (quarterlyTarget) {
                    T1I = (int) row.getCell(COL_TARGET_T1).getNumericCellValue();
                    T2I = (int) row.getCell(COL_TARGET_T2).getNumericCellValue();
                    T3I = (int) row.getCell(COL_TARGET_T3).getNumericCellValue();
                    T4I = (int) row.getCell(COL_TARGET_T4).getNumericCellValue();
                } else {
                    DataFormatter dataFormatter = new DataFormatter();
                    String svalue = StringUtils.trimToNull(dataFormatter.formatCellValue(row.getCell(COL_TOTAL_TARGET)));
                    if (svalue == null) {
                        Total_TARGET_value = null;
                    } else {
                        Total_TARGET_value = new Integer(svalue);

                    }
                }
                // cantones
                // locations
                Set<Canton> indicatorLocations = new HashSet<>();
                String locationsTotal = StringUtils.trimToNull(row.getCell(COL_CANTONS).getStringCellValue());
                if (StringUtils.isEmpty(locationsTotal)) {
                    throw new GeneralAppException("No se encontraron lugares de ejecución para el indicador "
                            + productIndicatorString + ". '" + "'.", Response.Status.BAD_REQUEST);
                }
                List<String> locationStringList = Arrays.asList(StringUtils.split(locationsTotal, ","));
                if (CollectionUtils.isEmpty(locationStringList)) {
                    throw new GeneralAppException("No se encontraron lugares de ejecución para el indicador "
                            + productIndicatorString + "'.", Response.Status.BAD_REQUEST);
                }
                locationStringList = locationStringList.stream().map(StringUtils::trimToNull).filter(StringUtils::isNotEmpty).distinct()
                        .collect(Collectors.toList());
                for (String cantonString : locationStringList) {
                    String[] cantonSeparated = cantonString.split("-");
                    if (cantonSeparated.length != 2) {
                        throw new GeneralAppException("Error al buscar el cantón:  "
                                + cantonString + ".", Response.Status.BAD_REQUEST);
                    }
                    Canton canton = this.cantonService.getByCantonDescriptionAndProvinceDescription(cantonSeparated[1], cantonSeparated[0]);
                    if (canton == null) {
                        throw new GeneralAppException("Error al buscar el cantón:  provincia->" + cantonSeparated[0]
                                + " cantón-> " + cantonSeparated[1] + "-->"
                                + cantonString + ".", Response.Status.BAD_REQUEST);
                    } else {
                        indicatorLocations.add(canton);
                    }
                }

                // create ie
                IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb = new IndicatorExecutionAssigmentWeb();
                indicatorExecutionAssigmentWeb.setIndicator(this.modelWebTransformationService.indicatorToIndicatorWeb(indicator, false, false, false));
                indicatorExecutionAssigmentWeb.setState(State.ACTIVO);
                indicatorExecutionAssigmentWeb.setPeriod(this.modelWebTransformationService.periodToPeriodWeb(project.getPeriod()));
                indicatorExecutionAssigmentWeb.setProject(this.modelWebTransformationService.projectToProjectWeb(project));
                indicatorExecutionAssigmentWeb.setProjectStatement(this.modelWebTransformationService.statementToStatementWeb(statement, false, false, false, false, false));
                indicatorExecutionAssigmentWeb.setActivityDescription(mainActivities);
                indicatorExecutionAssigmentWeb.setLocations(new ArrayList<>(this.modelWebTransformationService.cantonsToCantonsWeb(new ArrayList<>(indicatorLocations))));
                indicatorExecutionAssigmentWeb.setKeepBudget(false);
                if (!quarterlyTarget) indicatorExecutionAssigmentWeb.setTarget(Total_TARGET_value);
                totalLocations.addAll(indicatorLocations);
                LOGGER.info("cantones indicador: " + indicatorLocations.size());
                indicatorExecutionAssigmentWebs.add(indicatorExecutionAssigmentWeb);

                // targets
                List<QuarterWeb> quarterWebs = new ArrayList<>();
                QuarterWeb q1 = new QuarterWeb();
                q1.setQuarter(QuarterEnum.I);
                if (quarterlyTarget) q1.setTarget(new BigDecimal(T1I));
                quarterWebs.add(q1);
                QuarterWeb q2 = new QuarterWeb();
                q2.setQuarter(QuarterEnum.II);
                if (quarterlyTarget) q2.setTarget(new BigDecimal(T2I));
                quarterWebs.add(q2);
                QuarterWeb q3 = new QuarterWeb();
                q3.setQuarter(QuarterEnum.III);
                if (quarterlyTarget) q3.setTarget(new BigDecimal(T3I));
                quarterWebs.add(q3);
                QuarterWeb q4 = new QuarterWeb();
                q4.setQuarter(QuarterEnum.IV);
                if (quarterlyTarget) q4.setTarget(new BigDecimal(T4I));
                quarterWebs.add(q4);
                targetsMap.put(indicatorExecutionAssigmentWeb.hashCode(), quarterWebs);
                LOGGER.debug(T1I + "-" + T2I + "-" + T3I + "-" + T4I);
            }

            // end indicators
            LOGGER.info("cantones total: " + totalLocations.size());
            LOGGER.info("target total: " + this.getGeneralTarget(sheet));

            ProjectWeb projectWebOrg = this.modelWebTransformationService.projectToProjectWeb(project);
            List<CantonWeb> totalLocaltionWeb = this.modelWebTransformationService.cantonsToCantonsWeb(new ArrayList<>(totalLocations));
            projectWebOrg.setLocations(new HashSet<>(totalLocaltionWeb));

            this.projectService.updateProjectLocations(projectWebOrg.getLocations(), project, false);

            for (IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb : indicatorExecutionAssigmentWebs) {
                IndicatorExecution ie = this.indicatorExecutionService.assignPerformanceIndicatoToProject(indicatorExecutionAssigmentWeb);
                if (quarterlyTarget) {
                    List<QuarterWeb> targets = targetsMap.get(indicatorExecutionAssigmentWeb.hashCode());
                    TargetUpdateDTOWeb targetUpdateDTOWeb = new TargetUpdateDTOWeb();
                    targetUpdateDTOWeb.setQuarters(new ArrayList<>());
                    for (Quarter quarter : ie.getQuarters()) {
                        Optional<QuarterWeb> targetTmpOpt = targets.stream().filter(quarterWeb -> quarterWeb.getQuarter().equals(quarter.getQuarter())).findFirst();
                        QuarterWeb q = this.modelWebTransformationService.quarterToQuarterWeb(quarter);
                        targetTmpOpt.ifPresent(quarterWeb -> q.setTarget(quarterWeb.getTarget()));
                        targetUpdateDTOWeb.getQuarters().add(q);
                    }
                    targetUpdateDTOWeb.setIndicatorType(ie.getIndicatorType());
                    targetUpdateDTOWeb.setIndicatorExecutionId(ie.getId());


                    this.indicatorExecutionService.updateTargets(targetUpdateDTOWeb);
                } else {

                    TargetUpdateDTOWeb targetUpdateDTOWeb = new TargetUpdateDTOWeb();
                    targetUpdateDTOWeb.setTotalTarget(indicatorExecutionAssigmentWeb.getTarget() != null ? new BigDecimal(indicatorExecutionAssigmentWeb.getTarget()) : null);
                    targetUpdateDTOWeb.setIndicatorType(ie.getIndicatorType());
                    targetUpdateDTOWeb.setIndicatorExecutionId(ie.getId());
                    this.indicatorExecutionService.updateTargets(targetUpdateDTOWeb);
                }
            }

            // general target
            Integer generalTarget = this.getGeneralTarget(sheet);
            if (generalTarget != null) {
                List<IndicatorExecutionWeb> generalIes = this.indicatorExecutionService.getGeneralIndicatorExecutionsByProjectIdAndState(project.getId(), State.ACTIVO);

                if (CollectionUtils.isNotEmpty(generalIes)) {
                    IndicatorExecutionWeb generalIe = generalIes.get(0);
                    generalIe.setTarget(new BigDecimal(generalTarget));
                    TargetUpdateDTOWeb targetUpdateDTOWebGeneral = new TargetUpdateDTOWeb();
                    targetUpdateDTOWebGeneral.setTotalTarget(new BigDecimal(generalTarget));
                    targetUpdateDTOWebGeneral.setIndicatorType(IndicatorType.GENERAL);
                    targetUpdateDTOWebGeneral.setIndicatorExecutionId(generalIe.getId());
                    this.indicatorExecutionService.updateTargets(targetUpdateDTOWebGeneral);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralAppException(ExceptionUtils.getMessage(e), Response.Status.BAD_REQUEST);


        }


    }

    private Map<String, CellAddress> getTitleAdresses(XSSFSheet sheet, boolean quaterlyTarget) throws GeneralAppException {
        Map<String, CellAddress> titleMaps = new HashMap<>();

        titleMaps.put(PRODUCT_STATEMENT, null);
        titleMaps.put(MAIN_ACTIVITIES, null);
        titleMaps.put(PRODUCT_INDICATOR, null);
        if (quaterlyTarget) {
            titleMaps.put(TARGET_T1, null);
            titleMaps.put(TARGET_T2, null);
            titleMaps.put(TARGET_T3, null);
            titleMaps.put(TARGET_T4, null);
        }
        titleMaps.put(TOTAL_TARGET, null);
        titleMaps.put(CANTONS, null);

        //Iterate through each rows one by one
        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();


            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue() != null) {
                        LOGGER.info(row.getRowNum());
                        LOGGER.info(cell.getStringCellValue());
                        for (String key : titleMaps.keySet()) {
                            if (key.equalsIgnoreCase(StringUtils.trimToEmpty(StringUtils.normalizeSpace(cell.getStringCellValue())))) {
                                titleMaps.put(key, cell.getAddress());
                            }
                        }
                    }
                }
            }

            if (titleMaps.values().stream().anyMatch(Objects::isNull) && titleMaps.values().stream().anyMatch(Objects::nonNull)) {
                if (titleMaps.values().stream().filter(Objects::nonNull).count() < 2) {
                    continue;
                }
                List<String> missedColumns = new ArrayList<>();
                for (Map.Entry<String, CellAddress> stringCellAddressEntry : titleMaps.entrySet()) {
                    if (stringCellAddressEntry.getValue() == null) {
                        missedColumns.add(stringCellAddressEntry.getKey());
                    }
                }
                throw new GeneralAppException("No se pudieron encontrar las siguientes columnas: " + StringUtils.join(missedColumns, ", ") +
                        " . Por favor corrija la plantilla."
                        , Response.Status.BAD_REQUEST);
            }

            if (titleMaps.values().stream().noneMatch(Objects::isNull)) {
                break;
            }

        }
        titleMaps.forEach((s, cellAddress) -> LOGGER.debug(s + ": " + cellAddress.formatAsString()));
        return titleMaps;
    }

    private Integer getGeneralTarget(XSSFSheet sheet) {
        String label = "General - No. total de beneficiarios";
        CellAddress lableAddress = null;
        //Iterate through each rows one by one
        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();


            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue() != null) {

                        if (label.equalsIgnoreCase(StringUtils.trimToEmpty(StringUtils.normalizeSpace(cell.getStringCellValue())))) {
                            lableAddress = cell.getAddress();
                        }

                    }
                }
            }
        }
        if (lableAddress == null) {
            return null;
        } else {
            XSSFRow row = sheet.getRow(lableAddress.getRow());
            XSSFCell cell = row.getCell(lableAddress.getColumn() + 1);
            if (cell.getCellType() != CellType.NUMERIC) {
                return null;
            } else {
                return (int) cell.getNumericCellValue();
            }
        }

    }


    public ByteArrayOutputStream generateProjectIndicators(Long periodId, boolean quarterlyTarget) throws GeneralAppException {

        Period period = this.periodService.getById(periodId);
        final String filename;
        if (quarterlyTarget) {
            filename = "projectIndicatorsTemplate.xlsm";
        } else {
            filename = "projectIndicatorsTemplate_anual_target.xlsm";

        }

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            InputStream template = classLoader.getResourceAsStream("templates" + File.separator + filename);

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(Objects.requireNonNull(template));

            //Get first/desired sheet from the workbook
            XSSFSheet sheetTemplate = workbook.getSheetAt(0);
            LOGGER.info(sheetTemplate.getSheetName());
            // options
            XSSFSheet sheetOptions = workbook.getSheetAt(1);
            LOGGER.info(sheetOptions.getSheetName());
            // tables options
            List<XSSFTable> tables = sheetOptions.getTables();
            for (XSSFTable table : tables) {
                switch (table.getName()) {
                    case "statements":
                        fillStatements(sheetTemplate, sheetOptions, table, period, quarterlyTarget);
                        break;
                    case "indicators":
                        fillIndicators(sheetTemplate, sheetOptions, table, period, quarterlyTarget);
                        break;
                    case "cantons":
                        fillCantons(sheetTemplate, sheetOptions, table, quarterlyTarget);
                        break;
                }
            }


            // template.close();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            workbook.write(stream);
            workbook.close();
            return stream;
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new GeneralAppException("Error al obtener el template " + filename, Response.Status.INTERNAL_SERVER_ERROR);
        }

    }


    private void fillStatements(XSSFSheet sheetTemplate, XSSFSheet sheetOptions, XSSFTable tableOptions, Period period, boolean quarterlyTarget) throws GeneralAppException {
        int firstRow = tableOptions.getArea().getFirstCell().getRow();
        int firstCol = tableOptions.getArea().getFirstCell().getCol();

        List<Statement> statements = this.statementService.getByPeriodYearAndAreaType(AreaType.PRODUCTO, period.getYear());

        List<Statement> listaValues = statements.stream().sorted(Comparator.comparing(Statement::getCode)).collect(Collectors.toList());

        for (Statement value : listaValues) {
            firstRow++;
            XSSFRow row = sheetOptions.getRow(firstRow);
            if (row == null) {
                row = sheetOptions.createRow(firstRow);
            }
            XSSFCell cell = row.getCell(firstCol);
            if (cell != null) {
                LOGGER.info("cell: " + cell.getStringCellValue());
                cell.setCellValue(value.getCode() + " " + value.getDescription());
            } else {
                cell = row.createCell(firstCol);
                cell.setCellValue(value.getCode() + " " + value.getDescription());
            }


        }

        tableOptions.setArea(new AreaReference(tableOptions.getArea().getFirstCell(), new CellReference(firstRow, firstCol), null));

        // validation
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetTemplate);
        String listFormula = "INDIRECT(\"" + tableOptions.getName() + "[" + "statements" + "]\")";
        LOGGER.info(listFormula);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createFormulaListConstraint(listFormula);

        Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheetTemplate, quarterlyTarget);
        CellAddress cell_units = titleAdresses.get(PRODUCT_STATEMENT);
        CellRangeAddressList addressList = new CellRangeAddressList(cell_units.getRow() + 1, cell_units.getRow() + MAX,
                cell_units.getColumn(), cell_units.getColumn());
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        sheetTemplate.addValidationData(validation);


    }

    private void fillIndicators(XSSFSheet sheetTemplate, XSSFSheet sheetOptions, XSSFTable tableOptions, Period period, boolean quarterlyTarget) throws GeneralAppException {
        int firstRow = tableOptions.getArea().getFirstCell().getRow();
        int firstCol = tableOptions.getArea().getFirstCell().getCol();

        List<Indicator> indicators = this.indicatorService.getByPeriodYearAssignmentAndState(period.getYear());

        List<Indicator> listaValues = indicators.stream().sorted(Comparator.comparing(Indicator::getCode)).collect(Collectors.toList());

        for (Indicator value : listaValues) {
            firstRow++;
            XSSFRow row = sheetOptions.getRow(firstRow);
            if (row == null) {
                row = sheetOptions.createRow(firstRow);
            }
            XSSFCell cell = row.getCell(firstCol);
            if (cell != null) {
                LOGGER.info("cell: " + cell.getStringCellValue());
                cell.setCellValue(value.getCode() + " " + value.getDescription());
            } else {
                cell = row.createCell(firstCol);
                cell.setCellValue(value.getCode() + " " + value.getDescription());
            }
        }

        tableOptions.setArea(new AreaReference(tableOptions.getArea().getFirstCell(), new CellReference(firstRow, firstCol), null));

        // validation
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetTemplate);
        String listFormula = "INDIRECT(\"" + tableOptions.getName() + "[" + "indicators" + "]\")";
        LOGGER.info(listFormula);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createFormulaListConstraint(listFormula);

        Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheetTemplate, quarterlyTarget);
        CellAddress cell_units = titleAdresses.get(PRODUCT_INDICATOR);
        CellRangeAddressList addressList = new CellRangeAddressList(cell_units.getRow() + 1, cell_units.getRow() + MAX,
                cell_units.getColumn(), cell_units.getColumn());
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        sheetTemplate.addValidationData(validation);


    }


    private void fillCantons(XSSFSheet sheetTemplate, XSSFSheet sheetOptions, XSSFTable tableOptions, boolean quarterlyTarget) throws GeneralAppException {
        int firstRow = tableOptions.getArea().getFirstCell().getRow();
        int firstCol = tableOptions.getArea().getFirstCell().getCol();

        List<CantonWeb> cantons = this.cantonService.getByState(State.ACTIVO);
        List<String> values = cantons.stream().map(cantonWeb -> cantonWeb.getProvincia().getDescription() + "-" + cantonWeb.getDescription())
                .sorted().collect(Collectors.toList());


        for (String value : values) {
            firstRow++;
            XSSFRow row = sheetOptions.getRow(firstRow);
            if (row == null) {
                row = sheetOptions.createRow(firstRow);
            }
            XSSFCell cell = row.getCell(firstCol);
            if (cell != null) {
                LOGGER.info("cell: " + cell.getStringCellValue());
                cell.setCellValue(value);
            } else {
                cell = row.createCell(firstCol);
                cell.setCellValue(value);
            }
        }

        tableOptions.setArea(new AreaReference(tableOptions.getArea().getFirstCell(), new CellReference(firstRow, firstCol), null));

        // validation
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetTemplate);
        String listFormula = "INDIRECT(\"" + tableOptions.getName() + "[" + tableOptions.getName() + "]\")";
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createFormulaListConstraint(listFormula);

        Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheetTemplate, quarterlyTarget);
        CellAddress cell_units = titleAdresses.get(CANTONS);
        CellRangeAddressList addressList = new CellRangeAddressList(cell_units.getRow() + 1,
                cell_units.getRow() + MAX,
                cell_units.getColumn(), cell_units.getColumn());
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        sheetTemplate.addValidationData(validation);


    }
}
