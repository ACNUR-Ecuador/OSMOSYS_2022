package org.unhcr.osmosys.services.dataImport;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.AreaType;
import org.unhcr.osmosys.services.*;
import org.unhcr.osmosys.webServices.model.PeriodWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.*;

@Stateless
public class StatementImportService {

    @Inject
    AreaService areaService;

    @Inject
    PillarService pillarService;

    @Inject
    SituationService situationService;

    @Inject
    StatementService statementService;

    @Inject
    PeriodService periodService;

    private final static Logger LOGGER = Logger.getLogger(StatementImportService.class);
    // private static final String FILE_NAME = "C:\\test\\statementsv3.xlsx";

    private static final String IMPACT_AREA_CODE = "AREA DE IMPACTO CODIGO";
    private static final String IMPACT_AREA = "AREA DE IMPACTO";
    private static final String IMPACT_STATEMENT_CODE = "DECLARACION DE IMPACTO CODIGO";
    private static final String IMPACT_STATEMENT = "DECLARACION DE IMPACTO";
    private static final String OUTCOME_AREA_CODE = "AREA DE EFECTO CODIGO";
    private static final String OUTCOME_AREA = "AREA DE EFECTO";
    private static final String OUTCOME_STATEMENT_CODE = "DECLARACION DE EFECTO CODIGO";
    private static final String OUTCOME_STATEMENT = "DECLARACION DE EFECTO";
    private static final String OUTPUT_STATEMENT_CODE = "DECLARACION DE PRODUCTO CODIGO";
    private static final String OUTPUT_STATEMENT = "DECLARACION DE PRODUCTO";
    private static final String PILLAR_CODE = "PILAR CODIGO";
    private static final String PILLAR = "PILAR";
    private static final String SITUATION_CODE = "SITUACION CODIGO";
    private static final String SITUATION = "SITUACION";


    public void statementImportV2(PeriodWeb periodWeb, InputStream file) throws GeneralAppException {
        LOGGER.info("test import");
        try {
            DataFormatter formatter = new DataFormatter();
            Period period = this.periodService.getByYear(periodWeb.getYear());
            if (period == null) {
                throw new GeneralAppException("El periodo " + periodWeb.getYear() + " no existe", Response.Status.BAD_REQUEST);

            }

            //FileInputStream file = new FileInputStream(FILE_NAME);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheet("catalogo_declaraciones");

            Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheet);
            LOGGER.error(titleAdresses.get(IMPACT_AREA_CODE).getColumn());
            LOGGER.error(titleAdresses.get(IMPACT_AREA_CODE).getRow());
            CellAddress IMPACT_AREA_CODE_CELL = titleAdresses.get(IMPACT_AREA_CODE);
            int rowInitial = IMPACT_AREA_CODE_CELL.getRow();
            // get IMPACT STATEMENTS

            int COL_IMPACT_AREA_CODE = titleAdresses.get(IMPACT_AREA_CODE).getColumn();
            // int COL_IMPACT_AREA = titleAdresses.get(IMPACT_AREA).getColumn();
            int COL_IMPACT_STATEMENT_CODE = titleAdresses.get(IMPACT_STATEMENT_CODE).getColumn();
            int COL_IMPACT_STATEMENT = titleAdresses.get(IMPACT_STATEMENT).getColumn();
            int COL_OUTCOME_AREA_CODE = titleAdresses.get(OUTCOME_AREA_CODE).getColumn();
            // int COL_OUTCOME_AREA = titleAdresses.get(OUTCOME_AREA).getColumn();
            int COL_OUTCOME_STATEMENT_CODE = titleAdresses.get(OUTCOME_STATEMENT_CODE).getColumn();
            int COL_OUTCOME_STATEMENT = titleAdresses.get(OUTCOME_STATEMENT).getColumn();
            int COL_OUTPUT_STATEMENT_CODE = titleAdresses.get(OUTPUT_STATEMENT_CODE).getColumn();
            int COL_OUTPUT_STATEMENT = titleAdresses.get(OUTPUT_STATEMENT).getColumn();
            int COL_PILLAR_CODE = titleAdresses.get(PILLAR_CODE).getColumn();
            // int COL_PILLAR = titleAdresses.get(PILLAR).getColumn();
            int COL_SITUATION_CODE = titleAdresses.get(SITUATION_CODE).getColumn();
            // int COL_SITUATION = titleAdresses.get(SITUATION).getColumn();

            Iterator<Row> rowIterator = sheet.iterator();
            // get IMPACT STATEMENTS

            Set<Statement> impactStatements = new HashSet<>();
            Set<Statement> outcomeStatements = new HashSet<>();
            Set<Statement> outputStatements = new HashSet<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() <= rowInitial) {
                    continue;
                }




                Statement impactStatement = this.statementService.getByCodeAndPeriodYearAndAreaType(StringUtils.trimToNull(
                        formatter.formatCellValue(row.getCell(COL_IMPACT_STATEMENT_CODE))),
                        AreaType.IMPACTO,
                        periodWeb.getYear());
                if (impactStatement != null) {
                    impactStatement.setState(State.ACTIVO);
                } else {
                    impactStatement = statementService.getByCodeAndDescriptionAndAreaType(
                            StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_IMPACT_STATEMENT_CODE))),
                            StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_IMPACT_STATEMENT))),
                            AreaType.IMPACTO
                    );
                    if (impactStatement != null) {
                        impactStatement.setState(State.ACTIVO);
                        PeriodStatementAsignation periodStatementAsignation = new PeriodStatementAsignation();
                        periodStatementAsignation.setState(State.ACTIVO);
                        periodStatementAsignation.setPeriod(period);
                        impactStatement.addPeriodStatementAsignation(periodStatementAsignation);

                    } else {
                        impactStatement = new Statement();
                        impactStatement.setState(State.ACTIVO);
                        impactStatement.setAreaType(AreaType.IMPACTO);
                        impactStatement.setCode(StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_IMPACT_STATEMENT_CODE))));
                        impactStatement.setDescription(StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_IMPACT_STATEMENT))));
                        String areaCode = StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_IMPACT_AREA_CODE)));
                        Area area = this.areaService.getByCode(areaCode);
                        if (area == null) {
                            throw new GeneralAppException("El área con código " + areaCode + " no existe", Response.Status.BAD_REQUEST);
                        }
                        impactStatement.setArea(area);
                        String pilarCode = StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_PILLAR_CODE)));
                        Pillar pillar = this.pillarService.getByCode(pilarCode);
                        if (pillar == null) {
                            throw new GeneralAppException("El pilar con código " + pilarCode + " no existe", Response.Status.BAD_REQUEST);
                        }
                        impactStatement.setPillar(pillar);

                        String situacionCode = StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_SITUATION_CODE)));
                        Situation situation = this.situationService.getByCode(situacionCode);
                        if (situation == null) {
                            throw new GeneralAppException("La situación con código " + situacionCode + " no existe", Response.Status.BAD_REQUEST);
                        }
                        impactStatement.setSituation(situation);
                        PeriodStatementAsignation periodStatementAsignation = new PeriodStatementAsignation();
                        periodStatementAsignation.setState(State.ACTIVO);
                        periodStatementAsignation.setPeriod(period);
                        impactStatement.addPeriodStatementAsignation(periodStatementAsignation);
                    }
                }

                impactStatements.add(impactStatement);

                // get OUTCOME STATEMENTS
                Statement outcomeStatement = this.statementService.getByCodeAndPeriodYearAndAreaType(StringUtils.trimToNull(
                        formatter.formatCellValue(row.getCell(COL_OUTCOME_STATEMENT_CODE))),
                        AreaType.RESULTADO,
                        periodWeb.getYear());
                if (outcomeStatement != null) {
                    outcomeStatement.setState(State.ACTIVO);
                } else {
                    outcomeStatement = statementService.getByCodeAndDescriptionAndAreaType(
                            StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTCOME_STATEMENT_CODE))),
                            StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTCOME_STATEMENT))),
                            AreaType.RESULTADO
                    );
                    if (outcomeStatement != null) {
                        outcomeStatement.setState(State.ACTIVO);
                        PeriodStatementAsignation periodStatementAsignation = new PeriodStatementAsignation();
                        periodStatementAsignation.setState(State.ACTIVO);
                        periodStatementAsignation.setPeriod(period);
                        outcomeStatement.addPeriodStatementAsignation(periodStatementAsignation);

                    } else {
                        outcomeStatement = new Statement();
                        outcomeStatement.setState(State.ACTIVO);
                        outcomeStatement.setAreaType(AreaType.RESULTADO);
                        outcomeStatement.setCode(StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTCOME_STATEMENT_CODE))));
                        outcomeStatement.setDescription(StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTCOME_STATEMENT))));
                        String areaCode = StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTCOME_AREA_CODE)));
                        Area area = this.areaService.getByCode(areaCode);
                        if (area == null) {
                            throw new GeneralAppException("El área con código " + areaCode + " no existe", Response.Status.BAD_REQUEST);
                        }
                        outcomeStatement.setArea(area);
                        // parent statement
                        String outcomeParentStatementCode=StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_IMPACT_STATEMENT_CODE)));
                        Optional<Statement> outcomeParentStatement = impactStatements.stream().filter(statement -> statement.getCode().equalsIgnoreCase(outcomeParentStatementCode)).findFirst();
                        if (!outcomeParentStatement.isPresent()) {
                            throw new GeneralAppException("La declaración con código " + outcomeStatement.getCode() + " no tiene una declaración padre con código "+ outcomeParentStatementCode, Response.Status.BAD_REQUEST);
                        }else {
                            outcomeStatement.setParentStatement(outcomeParentStatement.get());
                        }


                        String pilarCode = StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_PILLAR_CODE)));
                        Pillar pillar = this.pillarService.getByCode(pilarCode);
                        if (pillar == null) {
                            throw new GeneralAppException("El pilar con código " + pilarCode + " no existe", Response.Status.BAD_REQUEST);
                        }
                        outcomeStatement.setPillar(pillar);

                        String situacionCode = StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_SITUATION_CODE)));
                        Situation situation = this.situationService.getByCode(situacionCode);
                        if (situation == null) {
                            throw new GeneralAppException("La situación con código " + situacionCode + " no existe", Response.Status.BAD_REQUEST);
                        }
                        outcomeStatement.setSituation(situation);
                        PeriodStatementAsignation periodStatementAsignation = new PeriodStatementAsignation();
                        periodStatementAsignation.setState(State.ACTIVO);
                        periodStatementAsignation.setPeriod(period);
                        outcomeStatement.addPeriodStatementAsignation(periodStatementAsignation);
                    }
                }

                outcomeStatements.add(outcomeStatement);

                // get OUTPUT STATEMENTS
                Statement outputStatement = this.statementService.getByCodeAndPeriodYearAndAreaType(
                        StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTPUT_STATEMENT_CODE))),
                        AreaType.PRODUCTO,
                        periodWeb.getYear());
                if (outputStatement != null) {
                    outputStatement.setState(State.ACTIVO);
                } else {
                    outputStatement = statementService.getByCodeAndDescriptionAndAreaType(
                            StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTPUT_STATEMENT_CODE))),
                            StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTPUT_STATEMENT))),
                            AreaType.PRODUCTO
                    );
                    if (outputStatement != null) {
                        outputStatement.setState(State.ACTIVO);
                        PeriodStatementAsignation periodStatementAsignation = new PeriodStatementAsignation();
                        periodStatementAsignation.setState(State.ACTIVO);
                        periodStatementAsignation.setPeriod(period);
                        outputStatement.addPeriodStatementAsignation(periodStatementAsignation);

                    } else {
                        outputStatement = new Statement();
                        outputStatement.setState(State.ACTIVO);
                        outputStatement.setAreaType(AreaType.PRODUCTO);
                        outputStatement.setCode(StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTPUT_STATEMENT_CODE))));
                        outputStatement.setDescription(StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTPUT_STATEMENT))));
                        // parent statement+
                        String outcomeParentStatementCode=StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTCOME_STATEMENT_CODE)));
                        Optional<Statement> outcomeParentStatement = outcomeStatements.stream().filter(statement -> statement.getCode().equalsIgnoreCase(outcomeParentStatementCode)).findFirst();
                        if (!outcomeParentStatement.isPresent()) {
                            throw new GeneralAppException("La declaración con código " + outputStatement.getCode() + " no tiene una declaración padre con código "+ outcomeParentStatementCode, Response.Status.BAD_REQUEST);
                        }else {
                            outputStatement.setParentStatement(outcomeParentStatement.get());
                        }
                        String areaCode = StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_OUTCOME_AREA_CODE)));
                        Area area = this.areaService.getByCode(areaCode);
                        if (area == null) {
                            throw new GeneralAppException("El área con código " + areaCode + " no existe", Response.Status.BAD_REQUEST);
                        }
                        outputStatement.setArea(area);
                        String pilarCode = StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_PILLAR_CODE)));
                        Pillar pillar = this.pillarService.getByCode(pilarCode);
                        if (pillar == null) {
                            throw new GeneralAppException("El pilar con código " + pilarCode + " no existe", Response.Status.BAD_REQUEST);
                        }
                        outputStatement.setPillar(pillar);

                        String situacionCode = StringUtils.trimToNull(formatter.formatCellValue(row.getCell(COL_SITUATION_CODE)));
                        Situation situation = this.situationService.getByCode(situacionCode);
                        if (situation == null) {
                            throw new GeneralAppException("La situación con código " + situacionCode + " no existe", Response.Status.BAD_REQUEST);
                        }
                        outputStatement.setSituation(situation);
                        PeriodStatementAsignation periodStatementAsignation = new PeriodStatementAsignation();
                        periodStatementAsignation.setState(State.ACTIVO);
                        periodStatementAsignation.setPeriod(period);
                        outputStatement.addPeriodStatementAsignation(periodStatementAsignation);
                    }
                }

                outputStatements.add(outputStatement);

            }

            // validate unique codes
            List<Statement> totalStatements= new ArrayList<>();
            totalStatements.addAll(impactStatements);
            totalStatements.addAll(outcomeStatements);
            totalStatements.addAll(outputStatements);
            Map<String, Integer> codeValidation = new HashMap<>();
            for (Statement statement : totalStatements) {
                Integer count = codeValidation.get(statement.getCode());
                codeValidation.put(statement.getCode(), (count==null)?1:++count);
            }
            List<String> repeatedCodes= new ArrayList<>();
            codeValidation.forEach((code, count) ->{
                if(count>1){
                    repeatedCodes.add(code);
                }
            } );
            if(CollectionUtils.isNotEmpty(repeatedCodes)){
                throw new GeneralAppException(
                        "Los siguientes códigos están repetidos, revise sus descripciones "
                        + StringUtils.join(repeatedCodes,'-')

                        , Response.Status.BAD_REQUEST);
            }

            LOGGER.info(impactStatements);
            LOGGER.info(impactStatements.size());
            for (Statement impactStatement : impactStatements) {
                this.statementService.saveOrUpdate(impactStatement);
            }

            LOGGER.info(outcomeStatements);
            LOGGER.info(outcomeStatements.size());
            for (Statement outcomeStatement : outcomeStatements) {
                this.statementService.saveOrUpdate(outcomeStatement);
            }
            LOGGER.info(outputStatements);
            LOGGER.info(outputStatements.size());

            for (Statement outputStatement : outputStatements) {
                this.statementService.saveOrUpdate(outputStatement);
            }

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralAppException(ExceptionUtils.getMessage(e), Response.Status.BAD_REQUEST);
        }
    }




    private Map<String, CellAddress> getTitleAdresses(XSSFSheet sheet) {
        Map<String, CellAddress> titleMaps = new HashMap<>();

        titleMaps.put(IMPACT_AREA_CODE, null);
        titleMaps.put(IMPACT_AREA, null);
        titleMaps.put(IMPACT_STATEMENT_CODE, null);
        titleMaps.put(IMPACT_STATEMENT, null);
        titleMaps.put(OUTCOME_AREA_CODE, null);
        titleMaps.put(OUTCOME_AREA, null);
        titleMaps.put(OUTCOME_STATEMENT_CODE, null);
        titleMaps.put(OUTCOME_STATEMENT, null);
        titleMaps.put(OUTPUT_STATEMENT_CODE, null);
        titleMaps.put(OUTPUT_STATEMENT, null);
        titleMaps.put(PILLAR_CODE, null);
        titleMaps.put(PILLAR, null);
        titleMaps.put(SITUATION_CODE, null);
        titleMaps.put(SITUATION, null);
        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
               /* Cell cellA = row.getCell(0);
                if (cellA != null) {
                    LOGGER.info(cellA.getStringCellValue());
                    LOGGER.info(cellA.getAddress().formatAsString());
                }*/
            //For each row, iterate through all the columns
            Iterator<Cell> cellIterator = row.cellIterator();


            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                //Check the cell type and format accordingly
                    /*switch (cell.getCellType()) {
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "t");
                            break;
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "t");
                            break;
                    }*/
                DataFormatter formatter = new DataFormatter();
                if (StringUtils.trimToNull(formatter.formatCellValue(cell)) != null) {
                    for (String key : titleMaps.keySet()) {
                        if (key.equalsIgnoreCase(formatter.formatCellValue(cell))) {
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
