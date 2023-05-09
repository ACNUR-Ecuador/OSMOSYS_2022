package org.unhcr.osmosys.services.dataImport;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.*;
import org.unhcr.osmosys.services.CustomDissagregationService;
import org.unhcr.osmosys.services.IndicatorService;
import org.unhcr.osmosys.services.PeriodService;
import org.unhcr.osmosys.services.StatementService;
import org.unhcr.osmosys.webServices.model.CustomDissagregationWeb;
import org.unhcr.osmosys.webServices.model.ImportFileWeb;
import org.unhcr.osmosys.webServices.model.PeriodWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class IndicatorsImportService {


    @Inject
    StatementService statementService;

    @Inject
    PeriodService periodService;

    @Inject
    IndicatorService indicatorService;

    @Inject
    CustomDissagregationService customDissagregationService;

    @Inject
    com.sagatechs.generics.utils.FileUtils fileUtils;


    private final static Logger LOGGER = Logger.getLogger(IndicatorsImportService.class);
    // private static final String FILE_NAME = "C:\\test\\indicator_import.xlsx";
    private static final String IMPACT_STATEMENT = "DECLARACION DE PRODUCTO";
    private static final String INDICATOR_CODE = "INDICADOR CODIGO";
    private static final String INDICATOR = "INDICADOR";
    private static final String CATEGORY = "CATEGORIA";
    private static final String FRECUENCY = "FRECUENCIA";
    private static final String INSTRUCTIONS = "INSTRUCCIONES";
    private static final String DISSAGREGATION = "DESAGREGACION";
    private static final String CUSTOM_DISSAGREGATION = "DESAGREGACION PERSONALIZADA";
    private static final String UNIT_TYPE = "UNIDAD";
    private static final int MAX = 200;

    public void indicatorsImport(ImportFileWeb importFileWeb
    ) throws GeneralAppException {
        byte[] fileContent = this.fileUtils.decodeBase64ToBytes(importFileWeb.getFile());
        InputStream targetStream = new ByteArrayInputStream(fileContent);
        this.indicatorsImport(importFileWeb.getPeriod(), targetStream);
    }

    public void indicatorsImport(PeriodWeb periodWeb
            , InputStream file
    ) throws GeneralAppException {
        LOGGER.info("test import");
        try {

            Period period = this.periodService.getByYear(periodWeb.getYear());
            if (period == null) {
                throw new GeneralAppException("El periodo " + periodWeb.getYear() + " no existe", Response.Status.BAD_REQUEST);

            }

            // FileInputStream file = new FileInputStream(FILE_NAME);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheet("indicator_catalog");

            Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheet);
            LOGGER.error(titleAdresses.get(IMPACT_STATEMENT).getColumn());
            LOGGER.error(titleAdresses.get(IMPACT_STATEMENT).getRow());
            CellAddress IMPACT_AREA_CODE_CELL = titleAdresses.get(IMPACT_STATEMENT);
            int rowInitial = IMPACT_AREA_CODE_CELL.getRow();
            // get IMPACT STATEMENTS


            int COL_IMPACT_STATEMENT = titleAdresses.get(IMPACT_STATEMENT).getColumn();
            int COL_INDICATOR_CODE = titleAdresses.get(INDICATOR_CODE).getColumn();
            int COL_INDICATOR = titleAdresses.get(INDICATOR).getColumn();
            int COL_CATEGORY = titleAdresses.get(CATEGORY).getColumn();
            int COL_FRECUENCY = titleAdresses.get(FRECUENCY).getColumn();
            int COL_INSTRUCTIONS = titleAdresses.get(INSTRUCTIONS).getColumn();
            int COL_DISSAGREGATION = titleAdresses.get(DISSAGREGATION).getColumn();
            int COL_CUSTOM_DISSAGREGATION = titleAdresses.get(CUSTOM_DISSAGREGATION).getColumn();
            int COL_UNIT_TYPE = titleAdresses.get(UNIT_TYPE).getColumn();


            // get indicators
            for (Row row : sheet) {
                if (row.getRowNum() <= rowInitial) {
                    continue;
                }

                // get Indicator

                // find by year and code
                DataFormatter objDefaultFormat = new DataFormatter();

                FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator( workbook);
                objFormulaEvaluator.evaluate(row.getCell(COL_INDICATOR_CODE));
                String indicatorCode = StringUtils.trimToNull(objDefaultFormat.formatCellValue(row.getCell(COL_INDICATOR_CODE),objFormulaEvaluator));
                String indicatorDescription = StringUtils.trimToNull(row.getCell(COL_INDICATOR).getStringCellValue());


                if (indicatorCode == null && indicatorDescription == null) {
                    break;
                } else if (indicatorCode == null || indicatorDescription == null)  {
                    throw new GeneralAppException("Datos incorrectos del indicador  " + indicatorCode + "-" + indicatorDescription + " Para el periodo " + period.getYear() + "." +
                            " Por favor modifíquelo manualmente.", Response.Status.BAD_REQUEST);
                }

                Indicator indicator;
                indicator = this.indicatorService.getByCode(indicatorCode);

                /*if (indicator != null) {
                    throw new GeneralAppException("Ya existe un indicador con código: " + indicatorCode + " Para el periodo " + period.getYear() + "." +
                            " Por favor modifíquelo manualmente.", Response.Status.BAD_REQUEST);

                }
                indicator = this.indicatorService.getByCodeAndDescription(indicatorCode, indicatorDescription);
                if (indicator != null) {
                    throw new GeneralAppException("Ya existe un indicador con código: " + indicatorCode + " y descripción: " +
                            indicatorDescription + " Por favor modifíquelo manualmente.", Response.Status.BAD_REQUEST);

                }*/
                if (indicator == null) {
                    indicator = new Indicator();
                }else {
                    Indicator indicatorVal = this.indicatorService.getByPeriodAndCode(period.getId(), indicatorCode);
                    if (indicatorVal != null) {
                        throw new GeneralAppException("Ya existe un indicador con código: " + indicatorCode + " y descripción: " +
                                indicatorDescription + " para el periodo "+period.getYear()+ " Por favor modifíquelo manualmente.", Response.Status.BAD_REQUEST);

                    }
                }

                indicator.setCode(indicatorCode);
                indicator.setDescription(StringUtils.trimToNull(row.getCell(COL_INDICATOR).getStringCellValue()));
                indicator.setCategory(StringUtils.trimToNull(row.getCell(COL_CATEGORY).getStringCellValue()));
                indicator.setInstructions(StringUtils.trimToNull(row.getCell(COL_INSTRUCTIONS).getStringCellValue()));
                indicator.setState(State.ACTIVO);
                String statementTotal = StringUtils.trimToNull(row.getCell(COL_IMPACT_STATEMENT).getStringCellValue());
                String statementCode = StringUtils.trimToNull(StringUtils.split(statementTotal, " ", 2)[0]);
                indicator.setIndicatorType(IndicatorType.OPERACION);
                indicator.setMeasureType(MeasureType.NUMERO);
                indicator.setAreaType(AreaType.PRODUCTO);
                indicator.setCalculated(false);
                indicator.setCompassIndicator(false);
                indicator.setBlockAfterUpdate(false);
                indicator.setMonitored(false);
                indicator.setTotalIndicatorCalculationType(TotalIndicatorCalculationType.SUMA);
                indicator.setQualitativeInstructions("Proporcione un resumen de las actividades desarrolladas para el cumplimiento de las metas de este indicador. Adicionalmente, en este espacio se deben registrar las razones por las cuales no se reporta avance del indicador o se supera el valor inicial de la meta. La información que se incluye en este campo podrá ser considerada para la elaboración de los reportes narrativos.");


                // freceuncy
                String frecuencyString = StringUtils.trimToNull(row.getCell(COL_FRECUENCY).getStringCellValue());
                if(frecuencyString==null){
                    throw new GeneralAppException("La frecuencia en la fila "+(row.getRowNum()+1)+" no existe. Indicador " + indicator.getCode() + ".", Response.Status.BAD_REQUEST);
                }
                Frecuency frecuency;
                try {
                    frecuency = Frecuency.valueOf(StringUtils.upperCase(frecuencyString));
                    indicator.setFrecuency(frecuency);
                } catch (IllegalArgumentException e) {
                    throw new GeneralAppException("La frecuencia " + frecuencyString + " no existe. Indicador " + indicator.getCode() + ".", Response.Status.BAD_REQUEST);
                }
                // unit
                String unitString = StringUtils.trimToNull(row.getCell(COL_UNIT_TYPE).getStringCellValue());
                UnitType unit;
                if(unitString==null){
                    throw new GeneralAppException("La unidad en la fila "+(row.getRowNum()+1)+" no existe. Indicador " + indicator.getCode() + ".", Response.Status.BAD_REQUEST);
                }
                try {
                    unit = UnitType.valueOf(StringUtils.upperCase(unitString));
                    indicator.setUnit(unit);
                } catch (IllegalArgumentException e) {
                    throw new GeneralAppException("La frecuencia " + frecuencyString + " no existe. Indicador " + indicator.getCode() + ".", Response.Status.BAD_REQUEST);
                }
                // dissagregations

                String dissagregationsTotal = StringUtils.trimToNull(row.getCell(COL_DISSAGREGATION).getStringCellValue());
                Set<DissagregationType> dissagregationTypes = new HashSet<>();

                String[] dissagregationsTotalList = StringUtils.split(dissagregationsTotal, ",");
                for (String dissagregationString : dissagregationsTotalList) {
                    String dissagregationStringTmp = StringUtils.upperCase(StringUtils.trimToNull(dissagregationString));
                    try {
                        DissagregationType dissagregation = DissagregationType.valueOf(dissagregationStringTmp);
                        dissagregationTypes.add(dissagregation);
                    } catch (IllegalArgumentException e) {
                        throw new GeneralAppException("La desagregación '" + dissagregationString + "' no existe. Indicador " + indicator.getCode() + ".", Response.Status.BAD_REQUEST);
                    }
                }

                if (CollectionUtils.isEmpty(dissagregationTypes)) {
                    throw new GeneralAppException("El indicador " + indicatorCode + " no tiene desagregaciones.", Response.Status.BAD_REQUEST);
                }
                for (DissagregationType dissagregationType : dissagregationTypes) {
                    DissagregationAssignationToIndicator dissagregationAssignationToIndicator = new DissagregationAssignationToIndicator();
                    dissagregationAssignationToIndicator.setPeriod(period);
                    dissagregationAssignationToIndicator.setIndicator(indicator);
                    dissagregationAssignationToIndicator.setState(State.ACTIVO);
                    dissagregationAssignationToIndicator.setDissagregationType(dissagregationType);
                    indicator.addDissagregationAssignationToIndicator(dissagregationAssignationToIndicator);
                }

                // custom dissagregations

                String customDissagregationsTotal = StringUtils.trimToNull(row.getCell(COL_CUSTOM_DISSAGREGATION).getStringCellValue());
                Set<CustomDissagregation> customDissagregationTypes = new HashSet<>();
                String[] customDissagregationsTotalList;
                if (StringUtils.isNotEmpty(customDissagregationsTotal)) {
                    customDissagregationsTotalList = StringUtils.split(customDissagregationsTotal, ",");
                } else {
                    customDissagregationsTotalList = new String[0];
                }
                for (String customDissagregationString : customDissagregationsTotalList) {
                    String customDissagregationStringTmp = StringUtils.upperCase(StringUtils.trimToNull(customDissagregationString));
                    try {
                        if (StringUtils.isNotEmpty(customDissagregationStringTmp)) {
                            CustomDissagregation customDissagregation = this.customDissagregationService.getByName(customDissagregationStringTmp);
                            if (customDissagregation != null) {
                                customDissagregationTypes.add(customDissagregation);
                            }else {
                                throw new GeneralAppException("La desagregación " + customDissagregationStringTmp + " no existe. Indicador " + indicator.getCode() + ".", Response.Status.BAD_REQUEST);
                            }

                        }

                    } catch (IllegalArgumentException e) {
                        throw new GeneralAppException("La desagregación " + customDissagregationString + " no existe. Indicador " + indicator.getCode() + ".", Response.Status.BAD_REQUEST);
                    }
                }
                for (CustomDissagregation customDissagregationType : customDissagregationTypes) {
                    CustomDissagregationAssignationToIndicator dissagregationAssignationToIndicator = new CustomDissagregationAssignationToIndicator();
                    dissagregationAssignationToIndicator.setIndicator(indicator);
                    dissagregationAssignationToIndicator.setPeriod(period);
                    dissagregationAssignationToIndicator.setState(State.ACTIVO);
                    dissagregationAssignationToIndicator.setCustomDissagregation(customDissagregationType);
                    indicator.addCustomDissagregationAssignationToIndicator(dissagregationAssignationToIndicator);
                }
                // statement


                Statement statement = this.statementService.getByCodeAndPeriodYearAndAreaType(statementCode, AreaType.PRODUCTO, period.getYear());
                if (statement == null) {
                    throw new GeneralAppException("El statement " + statementCode + " no existe", Response.Status.BAD_REQUEST);
                }

                indicator.setStatement(statement);

                this.indicatorService.saveOrUpdate(indicator);

            }


            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralAppException(ExceptionUtils.getMessage(e), Response.Status.BAD_REQUEST);


        }


    }


    private Map<String, CellAddress> getTitleAdresses(XSSFSheet sheet) {
        Map<String, CellAddress> titleMaps = new HashMap<>();

        titleMaps.put(IMPACT_STATEMENT, null);
        titleMaps.put(INDICATOR_CODE, null);
        titleMaps.put(INDICATOR, null);
        titleMaps.put(CATEGORY, null);
        titleMaps.put(FRECUENCY, null);
        titleMaps.put(INSTRUCTIONS, null);
        titleMaps.put(DISSAGREGATION, null);
        titleMaps.put(CUSTOM_DISSAGREGATION, null);
        titleMaps.put(UNIT_TYPE, null);
        //Iterate through each rows one by one
        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();


            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getStringCellValue() != null) {
                    for (String key : titleMaps.keySet()) {
                        if (key.equalsIgnoreCase(cell.getStringCellValue())) {
                            titleMaps.put(key, cell.getAddress());
                        }
                    }
                }
            }

            if (titleMaps.values().stream().noneMatch(Objects::isNull)) {
                break;
            }

        }

        titleMaps.forEach((s, cellAddress) -> LOGGER.debug(s + ": " + cellAddress.formatAsString()));
        return titleMaps;


    }


    public ByteArrayOutputStream generateTemplate(Long periodId) throws GeneralAppException {

        Period period = this.periodService.getById(periodId);
        final String filename = "indicatorImportTemplateV2.xlsm";
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
                        fillStatements(sheetTemplate, sheetOptions, table, period);
                        break;
                    case "frecuency":
                        fillFrecuency(sheetTemplate, sheetOptions, table);
                        break;
                    case "dissagregations":
                        fillDissagregations(sheetTemplate, sheetOptions, table);
                        break;
                    case "custom_dissagregations":
                        fillCustomDissagregations(sheetTemplate, sheetOptions, table);
                        break;
                    case "units":
                        fillUnits(sheetTemplate, sheetOptions, table);
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

    private void fillUnits(XSSFSheet sheetTemplate, XSSFSheet sheetOptions, XSSFTable tableUnits) {
        int firstRow = tableUnits.getArea().getFirstCell().getRow();
        int firstCol = tableUnits.getArea().getFirstCell().getCol();
        List<UnitType> unitValues = Arrays.stream(UnitType.values()).sorted().collect(Collectors.toList());

        for (UnitType value : unitValues) {
            firstRow++;
            XSSFRow row = sheetOptions.getRow(firstRow);
            if (row == null) {
                row = sheetOptions.createRow(firstRow);
            }
            XSSFCell cell = row.getCell(firstCol);
            if (cell != null) {
                cell.setCellValue(value.getStringValue());
            } else {
                cell = row.createCell(firstCol);
                cell.setCellValue(value.getStringValue());
            }


        }
        tableUnits.setArea(new AreaReference(tableUnits.getArea().getFirstCell(), new CellReference(firstRow, firstCol), null));

        // validation
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetTemplate);
        String listFormula = "INDIRECT(\"" + tableUnits.getName() + "[" + "units" + "]\")";
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createFormulaListConstraint(listFormula);

        Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheetTemplate);
        CellAddress cell_units = titleAdresses.get(UNIT_TYPE);
        CellRangeAddressList addressList = new CellRangeAddressList(cell_units.getRow() + 1, cell_units.getRow() + MAX,
                cell_units.getColumn(), cell_units.getColumn());
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        sheetTemplate.addValidationData(validation);
    }

    private void fillDissagregations(XSSFSheet sheetTemplate, XSSFSheet sheetOptions, XSSFTable tableUnits) {
        int firstRow = tableUnits.getArea().getFirstCell().getRow();
        int firstCol = tableUnits.getArea().getFirstCell().getCol();
        List<DissagregationType> unitValues = Arrays.stream(DissagregationType.values()).sorted(
                Comparator.comparingInt(DissagregationType::getOrder)
        ).collect(Collectors.toList());

        for (DissagregationType value : unitValues) {
            firstRow++;
            XSSFRow row = sheetOptions.getRow(firstRow);
            if (row == null) {
                row = sheetOptions.createRow(firstRow);
            }
            XSSFCell cell = row.getCell(firstCol);
            if (cell != null) {
                cell.setCellValue(value.getStringValue());
            } else {
                cell = row.createCell(firstCol);
                cell.setCellValue(value.getStringValue());
            }


        }
        tableUnits.setArea(new AreaReference(tableUnits.getArea().getFirstCell(), new CellReference(firstRow, firstCol), null));

        // validation
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetTemplate);
        String listFormula = "INDIRECT(\"" + tableUnits.getName() + "[" + "dissagregations" + "]\")";
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createFormulaListConstraint(listFormula);

        Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheetTemplate);
        CellAddress cell_units = titleAdresses.get(DISSAGREGATION);
        CellRangeAddressList addressList = new CellRangeAddressList(cell_units.getRow() + 1, cell_units.getRow() + MAX,
                cell_units.getColumn(), cell_units.getColumn());
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        sheetTemplate.addValidationData(validation);
    }

    private void fillFrecuency(XSSFSheet sheetTemplate, XSSFSheet sheetOptions, XSSFTable tableUnits) {
        int firstRow = tableUnits.getArea().getFirstCell().getRow();
        int firstCol = tableUnits.getArea().getFirstCell().getCol();
        List<Frecuency> unitValues = Arrays.stream(Frecuency.values()).sorted(
                Comparator.comparingInt(Frecuency::getOrder)
        ).collect(Collectors.toList());

        for (Frecuency value : unitValues) {
            firstRow++;
            XSSFRow row = sheetOptions.getRow(firstRow);
            if (row == null) {
                row = sheetOptions.createRow(firstRow);
            }
            XSSFCell cell = row.getCell(firstCol);
            if (cell != null) {
                cell.setCellValue(value.getStringValue());
            } else {
                cell = row.createCell(firstCol);
                cell.setCellValue(value.getStringValue());
            }


        }
        tableUnits.setArea(new AreaReference(tableUnits.getArea().getFirstCell(), new CellReference(firstRow, firstCol), null));

        // validation
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetTemplate);
        String listFormula = "INDIRECT(\"" + tableUnits.getName() + "[" + "frecuency" + "]\")";
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createFormulaListConstraint(listFormula);

        Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheetTemplate);
        CellAddress cell_units = titleAdresses.get(FRECUENCY);
        CellRangeAddressList addressList = new CellRangeAddressList(cell_units.getRow() + 1, cell_units.getRow() + MAX,
                cell_units.getColumn(), cell_units.getColumn());
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        sheetTemplate.addValidationData(validation);
    }

    private void fillCustomDissagregations(XSSFSheet sheetTemplate, XSSFSheet sheetOptions, XSSFTable tableOptions) {
        int firstRow = tableOptions.getArea().getFirstCell().getRow();
        int firstCol = tableOptions.getArea().getFirstCell().getCol();

        List<CustomDissagregationWeb> cutomsDissagregations = this.customDissagregationService.getByState(State.ACTIVO);

        List<CustomDissagregationWeb> listaValues = cutomsDissagregations.stream().sorted(Comparator.comparing(CustomDissagregationWeb::getName)).collect(Collectors.toList());

        for (CustomDissagregationWeb value : listaValues) {
            firstRow++;
            XSSFRow row = sheetOptions.getRow(firstRow);
            if (row == null) {
                row = sheetOptions.createRow(firstRow);
            }
            XSSFCell cell = row.getCell(firstCol);
            if (cell != null) {
                LOGGER.info("cell: " + cell.getStringCellValue());
                cell.setCellValue(value.getName());
            } else {
                cell = row.createCell(firstCol);
                cell.setCellValue(value.getName());
            }


        }

        tableOptions.setArea(new AreaReference(tableOptions.getArea().getFirstCell(), new CellReference(firstRow, firstCol), null));

        // validation
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetTemplate);
        String listFormula = "INDIRECT(\"" + tableOptions.getName() + "[" + "custom_dissagregations" + "]\")";
        LOGGER.info(listFormula);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createFormulaListConstraint(listFormula);

        Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheetTemplate);
        CellAddress cell_units = titleAdresses.get(CUSTOM_DISSAGREGATION);
        CellRangeAddressList addressList = new CellRangeAddressList(cell_units.getRow() + 1, cell_units.getRow() + MAX,
                cell_units.getColumn(), cell_units.getColumn());
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        sheetTemplate.addValidationData(validation);


    }

    private void fillStatements(XSSFSheet sheetTemplate, XSSFSheet sheetOptions, XSSFTable tableOptions, Period period) {
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

        Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheetTemplate);
        CellAddress cell_units = titleAdresses.get(IMPACT_STATEMENT);
        CellRangeAddressList addressList = new CellRangeAddressList(cell_units.getRow() + 1, cell_units.getRow() + MAX,
                cell_units.getColumn(), cell_units.getColumn());
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        sheetTemplate.addValidationData(validation);


    }


}
