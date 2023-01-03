package org.unhcr.osmosys.services.dataImport;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.*;
import org.unhcr.osmosys.services.*;
import org.unhcr.osmosys.webServices.model.PeriodWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.util.*;

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

    private final static Logger LOGGER = Logger.getLogger(IndicatorsImportService.class);
    private static final String FILE_NAME = "C:\\test\\indicator_import.xlsx";

    private static final String IMPACT_STATEMENT_CODE = "DECLARACION DE PRODUCTO CODIGO";
    private static final String IMPACT_STATEMENT = "DECLARACION DE PRODUCTO";
    private static final String INDICATOR_CODE = "INDICADOR CODIGO";
    private static final String INDICATOR = "INDICADOR";
    private static final String CATEGORY = "CATEGORIA";
    private static final String FRECUENCY = "FRECUENCIA";
    private static final String INSTRUCTIONS = "INSTRUCCIONES";
    private static final String DISSAGREGATION = "DESAGREGACION";
    private static final String CUSTOM_DISSAGREGATION = "DESAGREGACION PERSONALIZADA";
    private static final String UNIT_TYPE = "UNIDAD";


    public void indicatorsImport(PeriodWeb periodWeb
                                 // , InputStream file
    ) throws GeneralAppException {
        LOGGER.info("test import");
        try {
            Period period = this.periodService.getByYear(periodWeb.getYear());
            if (period == null) {
                throw new GeneralAppException("El periodo " + periodWeb.getYear() + " no existe", Response.Status.BAD_REQUEST);

            }

            FileInputStream file = new FileInputStream(FILE_NAME);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheet("indicator_catalog");

            Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheet);
            LOGGER.error(titleAdresses.get(IMPACT_STATEMENT_CODE).getColumn());
            LOGGER.error(titleAdresses.get(IMPACT_STATEMENT_CODE).getRow());
            CellAddress IMPACT_AREA_CODE_CELL = titleAdresses.get(IMPACT_STATEMENT_CODE);
            int rowInitial = IMPACT_AREA_CODE_CELL.getRow();
            // get IMPACT STATEMENTS


            int COL_IMPACT_STATEMENT_CODE = titleAdresses.get(IMPACT_STATEMENT_CODE).getColumn();
            // int COL_IMPACT_STATEMENT = titleAdresses.get(IMPACT_STATEMENT).getColumn();
            int COL_INDICATOR_CODE = titleAdresses.get(INDICATOR_CODE).getColumn();
            int COL_INDICATOR = titleAdresses.get(INDICATOR).getColumn();
            int COL_CATEGORY = titleAdresses.get(CATEGORY).getColumn();
            int COL_FRECUENCY = titleAdresses.get(FRECUENCY).getColumn();
            int COL_INSTRUCTIONS = titleAdresses.get(INSTRUCTIONS).getColumn();
            int COL_DISSAGREGATION = titleAdresses.get(DISSAGREGATION).getColumn();
            int COL_CUSTOM_DISSAGREGATION = titleAdresses.get(CUSTOM_DISSAGREGATION).getColumn();
            int COL_UNIT_TYPE = titleAdresses.get(UNIT_TYPE).getColumn();


            Iterator<Row> rowIterator = sheet.iterator();
            // get indicators
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() <= rowInitial) {
                    continue;
                }

                // get Indicator

                // find by year and code
                String indicatorCode = StringUtils.trimToNull(row.getCell(COL_INDICATOR_CODE).getStringCellValue());
                String indicatorDescription = StringUtils.trimToNull(row.getCell(COL_INDICATOR).getStringCellValue());
                Indicator indicator;
                indicator = this.indicatorService.getByPeriodAndCode(period.getId(), indicatorCode);

                if (indicator != null) {
                    throw new GeneralAppException("Ya existe un indicador con código: " + indicatorCode + " Para el periodo " + period.getYear() + "." +
                            " Por favor modifíquelo manualmente.", Response.Status.BAD_REQUEST);

                }
                indicator = this.indicatorService.getByCodeAndDescription(indicatorCode, indicatorDescription);
                if (indicator != null) {
                    throw new GeneralAppException("Ya existe un indicador con código: " + indicatorCode + " y descripción: " +
                            indicatorDescription + " Por favor modifíquelo manualmente.", Response.Status.BAD_REQUEST);

                }

                indicator = new Indicator();
                indicator.setCode(indicatorCode);
                indicator.setDescription(StringUtils.trimToNull(row.getCell(COL_INDICATOR).getStringCellValue()));
                indicator.setCategory(StringUtils.trimToNull(row.getCell(COL_CATEGORY).getStringCellValue()));
                indicator.setInstructions(StringUtils.trimToNull(row.getCell(COL_INSTRUCTIONS).getStringCellValue()));
                indicator.setState(State.ACTIVO);
                String statementCode = StringUtils.trimToNull(row.getCell(COL_IMPACT_STATEMENT_CODE).getStringCellValue());
                indicator.setIndicatorType(IndicatorType.OPERACION);
                indicator.setMeasureType(MeasureType.NUMERO);
                indicator.setAreaType(AreaType.PRODUCTO);
                indicator.setCalculated(false);
                indicator.setCompassIndicator(false);
                indicator.setBlockAfterUpdate(false);
                indicator.setMonitored(false);
                indicator.setTotalIndicatorCalculationType(TotalIndicatorCalculationType.SUMA);

                // freceuncy
                String frecuencyString = StringUtils.trimToNull(row.getCell(COL_FRECUENCY).getStringCellValue());
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
                        throw new GeneralAppException("La segregación '" + dissagregationString + "' no existe. Indicador " + indicator.getCode() + ".", Response.Status.BAD_REQUEST);
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
                    String customDissagregationStringTmp = StringUtils.upperCase(StringUtils.trimToNull(unitString));
                    try {
                        if (StringUtils.isNotEmpty(customDissagregationStringTmp)) {
                            CustomDissagregation customDissagregation = this.customDissagregationService.getByName(customDissagregationStringTmp);
                            if (customDissagregation != null) {
                                customDissagregationTypes.add(customDissagregation);
                            }

                        }

                    } catch (IllegalArgumentException e) {
                        throw new GeneralAppException("La segregación " + customDissagregationString + " no existe. Indicador " + indicator.getCode() + ".", Response.Status.BAD_REQUEST);
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
/*

    public void statementImport(List<PeriodWeb> periodWebs) throws GeneralAppException {
        LOGGER.info("test import");
        try {


            FileInputStream file = new FileInputStream(FILE_NAME);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheet("catalogo_statements");

            Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheet);
            LOGGER.error(titleAdresses.get(IMPACT_AREA_CODE).getColumn());
            LOGGER.error(titleAdresses.get(IMPACT_AREA_CODE).getRow());
            CellAddress IMPACT_AREA_CODE_CELL = titleAdresses.get(IMPACT_AREA_CODE);
            int rowInitial = IMPACT_AREA_CODE_CELL.getRow();
            Iterator<Row> rowIterator0 = sheet.iterator();
            // get IMPACT STATEMENTS

            int COL_IMPACT_AREA_CODE = titleAdresses.get(IMPACT_AREA_CODE).getColumn();
            int COL_IMPACT_AREA = titleAdresses.get(IMPACT_AREA).getColumn();
            int COL_IMPACT_STATEMENT_CODE = titleAdresses.get(IMPACT_STATEMENT_CODE).getColumn();
            int COL_IMPACT_STATEMENT = titleAdresses.get(IMPACT_STATEMENT).getColumn();
            int COL_OUTCOME_AREA_CODE = titleAdresses.get(OUTCOME_AREA_CODE).getColumn();
            int COL_OUTCOME_AREA = titleAdresses.get(OUTCOME_AREA).getColumn();
            int COL_OUTCOME_STATEMENT_CODE = titleAdresses.get(OUTCOME_STATEMENT_CODE).getColumn();
            int COL_OUTCOME_STATEMENT = titleAdresses.get(OUTCOME_STATEMENT).getColumn();
            int COL_OUTPUT_STATEMENT_CODE = titleAdresses.get(OUTPUT_STATEMENT_CODE).getColumn();
            int COL_OUTPUT_STATEMENT = titleAdresses.get(OUTPUT_STATEMENT).getColumn();
            int COL_PILLAR_CODE = titleAdresses.get(PILLAR_CODE).getColumn();
            int COL_PILLAR = titleAdresses.get(PILLAR).getColumn();
            int COL_SITUATION_CODE = titleAdresses.get(SITUATION_CODE).getColumn();
            int COL_SITUATION = titleAdresses.get(SITUATION).getColumn();

            Iterator<Row> rowIterator = sheet.iterator();
            // get IMPACT STATEMENTS

            Set<StatementWeb> impactStatementsWebs = new HashSet<>();
            Set<StatementWeb> outcomeStatementsWebs = new HashSet<>();
            Set<StatementWeb> outputStatementsWebs = new HashSet<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() <= rowInitial) {
                    continue;
                }
                // get IMPACT STATEMENTS
                StatementWeb impactStatementWeb = new StatementWeb();
                impactStatementWeb.setState(State.ACTIVO);
                impactStatementWeb.setAreaType(AreaType.IMPACTO);
                impactStatementWeb.setCode(StringUtils.trimToNull(row.getCell(COL_IMPACT_STATEMENT_CODE).getStringCellValue()));
                impactStatementWeb.setDescription(StringUtils.trimToNull(row.getCell(COL_IMPACT_STATEMENT).getStringCellValue()));
                impactStatementWeb.setParentStatement(null);
                // area
                String areaCode = StringUtils.trimToNull(row.getCell(COL_IMPACT_AREA_CODE).getStringCellValue());
                Area impactArea = areaService.getByCode(areaCode);
                AreaWeb impactAreaWeb = this.modelWebTransformationService.areaToAreaWeb(impactArea);
                impactStatementWeb.setArea(impactAreaWeb);
                // PILLAR
                String pillarCode = StringUtils.trimToNull(row.getCell(COL_PILLAR_CODE).getStringCellValue());
                Pillar pillar = this.pillarService.getByCode(pillarCode);
                PillarWeb pillarWeb = this.modelWebTransformationService.pillarToPillarWeb(pillar);
                impactStatementWeb.setPillar(pillarWeb);
                // SITUATION
                String situationCode = StringUtils.trimToNull(row.getCell(COL_SITUATION_CODE).getStringCellValue());
                Situation situation = this.situationService.getByCode(situationCode);
                SituationWeb situationWeb = this.modelWebTransformationService.situationToSituationWeb(situation);
                impactStatementWeb.setSituation(situationWeb);

                impactStatementWeb.setPeriodStatementAsignations(new ArrayList<>());
                for (PeriodWeb periodWeb : periodWebs) {
                    PeriodStatementAsignationWeb periodStatementAsignationWeb = new PeriodStatementAsignationWeb();
                    periodStatementAsignationWeb.setPeriod(periodWeb);
                    periodStatementAsignationWeb.setState(State.ACTIVO);
                    periodStatementAsignationWeb.setPopulationCoverage(null);
                    impactStatementWeb.getPeriodStatementAsignations().add(periodStatementAsignationWeb);
                }

                impactStatementsWebs.add(impactStatementWeb);
                // get OUTCOME STATEMENTS

                StatementWeb outcomeStatementWeb = new StatementWeb();
                outcomeStatementWeb.setState(State.ACTIVO);
                outcomeStatementWeb.setAreaType(AreaType.RESULTADO);
                outcomeStatementWeb.setCode(StringUtils.trimToNull(row.getCell(COL_OUTCOME_STATEMENT_CODE).getStringCellValue()));
                outcomeStatementWeb.setDescription(StringUtils.trimToNull(row.getCell(COL_OUTCOME_STATEMENT).getStringCellValue()));
                outcomeStatementWeb.setParentStatement(impactStatementsWebs.stream().filter(statementWeb -> {
                    return statementWeb.getCode().equalsIgnoreCase(StringUtils.trimToNull(row.getCell(COL_IMPACT_STATEMENT_CODE).getStringCellValue()));
                }).findFirst().get());
                // area
                String areaCodeOutcome = StringUtils.trimToNull(row.getCell(COL_OUTCOME_AREA_CODE).getStringCellValue());
                Area outcomeArea = areaService.getByCode(areaCodeOutcome);
                AreaWeb outcomeAreaWeb = this.modelWebTransformationService.areaToAreaWeb(outcomeArea);
                outcomeStatementWeb.setArea(outcomeAreaWeb);
                // PILLAR
                outcomeStatementWeb.setPillar(pillarWeb);
                // SITUATION
                outcomeStatementWeb.setSituation(situationWeb);
                outcomeStatementWeb.setPeriodStatementAsignations(new ArrayList<>());
                for (PeriodWeb periodWeb : periodWebs) {
                    PeriodStatementAsignationWeb periodStatementAsignationWeb = new PeriodStatementAsignationWeb();
                    periodStatementAsignationWeb.setPeriod(periodWeb);
                    periodStatementAsignationWeb.setState(State.ACTIVO);
                    periodStatementAsignationWeb.setPopulationCoverage(null);
                    outcomeStatementWeb.getPeriodStatementAsignations().add(periodStatementAsignationWeb);
                }

                outcomeStatementsWebs.add(outcomeStatementWeb);

                // get OUTPUT STATEMENTS

                StatementWeb outputStatementWeb = new StatementWeb();
                outputStatementWeb.setState(State.ACTIVO);
                outputStatementWeb.setAreaType(AreaType.PRODUCTO);
                outputStatementWeb.setCode(StringUtils.trimToNull(row.getCell(COL_OUTPUT_STATEMENT_CODE).getStringCellValue()));
                outputStatementWeb.setDescription(StringUtils.trimToNull(row.getCell(COL_OUTPUT_STATEMENT).getStringCellValue()));
                outputStatementWeb.setParentStatement(outcomeStatementsWebs.stream().filter(statementWeb -> {
                    return statementWeb.getCode().equalsIgnoreCase(StringUtils.trimToNull(row.getCell(COL_OUTCOME_STATEMENT_CODE).getStringCellValue()));
                }).findFirst().get());
                // area
                String areaCodeOutput = StringUtils.trimToNull(row.getCell(COL_OUTCOME_AREA_CODE).getStringCellValue());
                Area outputArea = areaService.getByCode(areaCodeOutput);
                AreaWeb outputAreaWeb = this.modelWebTransformationService.areaToAreaWeb(outputArea);
                outputStatementWeb.setArea(outputAreaWeb);
                // PILLAR
                outputStatementWeb.setPillar(pillarWeb);
                // SITUATION
                outputStatementWeb.setSituation(situationWeb);
                outputStatementWeb.setPeriodStatementAsignations(new ArrayList<>());
                for (PeriodWeb periodWeb : periodWebs) {
                    PeriodStatementAsignationWeb periodStatementAsignationWeb = new PeriodStatementAsignationWeb();
                    periodStatementAsignationWeb.setPeriod(periodWeb);
                    periodStatementAsignationWeb.setState(State.ACTIVO);
                    periodStatementAsignationWeb.setPopulationCoverage(null);
                    outputStatementWeb.getPeriodStatementAsignations().add(periodStatementAsignationWeb);
                }

                outputStatementsWebs.add(outputStatementWeb);


            }
            LOGGER.info(impactStatementsWebs);
            LOGGER.info(impactStatementsWebs.size());
            for (StatementWeb impactStatementsWeb : impactStatementsWebs) {
                this.statementService.validate(impactStatementsWeb);
                this.statementService.save(impactStatementsWeb);
            }

            LOGGER.info(outcomeStatementsWebs);
            LOGGER.info(outcomeStatementsWebs.size());
            for (StatementWeb outcomeStatementsWeb : outcomeStatementsWebs) {
                this.statementService.validate(outcomeStatementsWeb);
                this.statementService.save(outcomeStatementsWeb);
            }
            LOGGER.info(outputStatementsWebs);
            LOGGER.info(outputStatementsWebs.size());

            for (StatementWeb outputStatementsWeb : outputStatementsWebs) {
                this.statementService.validate(outputStatementsWeb);
                this.statementService.save(outputStatementsWeb);
            }

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralAppException(ExceptionUtils.getMessage(e), Response.Status.BAD_REQUEST);


        }


    }
*/

    private Map<String, CellAddress> getTitleAdresses(XSSFSheet sheet) {
        Map<String, CellAddress> titleMaps = new HashMap<>();

        titleMaps.put(IMPACT_STATEMENT_CODE, null);
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
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
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
}
