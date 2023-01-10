package org.unhcr.osmosys.services.dataImport;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.servicio.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.io.FileInputStream;
import java.io.IOException;
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

    private final static Logger LOGGER = Logger.getLogger(ProjectIndicatorsImportService.class);
    private static final String FILE_NAME = "D:\\proyectos\\OSMOSYS_2023\\DataImport2023\\partnerisIndicators\\FUDELA SOIB.xlsx";

    private static final String NO = "No.";
    private static final String PRODUCT_STATEMENT = "Declaración de Producto";
    private static final String MAIN_ACTIVITIES = "Actividades clave de Producto";
    private static final String PRODUCT_INDICATOR = "Indicadores de Producto";
    private static final String TARGET_T1 = "T1";
    private static final String TARGET_T2 = "T2";
    private static final String TARGET_T3 = "T3";
    private static final String TARGET_T4 = "T4";
    private static final String TOTAL_TARGET = "Meta Total";
    private static final String CANTONS = "Cantones de Ejecución";

    public void projectIndicatorsImport(ProjectWeb projectWeb
                                        // , InputStream file
    ) throws GeneralAppException, IOException {
        LOGGER.info("test import");
        FileInputStream file = null;
        try {
            Project project = this.projectService.getById(projectWeb.getId());
            if (project == null) {
                throw new GeneralAppException("El projecto con id " + projectWeb.getId() + " no existe", Response.Status.BAD_REQUEST);

            }
            Integer year = project.getPeriod().getYear();
            file = new FileInputStream(FILE_NAME);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheet("indicators");

            Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheet);
            CellAddress CODE_CELL = titleAdresses.get(NO);
            int rowInitial = CODE_CELL.getRow();
            // get INDICATORS


            int COL_NO = titleAdresses.get(NO).getColumn();
            int COL_PRODUCT_STATEMENT = titleAdresses.get(PRODUCT_STATEMENT).getColumn();
            int COL_MAIN_ACTIVITIES = titleAdresses.get(MAIN_ACTIVITIES).getColumn();
            int COL_PRODUCT_INDICATOR = titleAdresses.get(PRODUCT_INDICATOR).getColumn();
            int COL_TARGET_T1 = titleAdresses.get(TARGET_T1).getColumn();
            int COL_TARGET_T2 = titleAdresses.get(TARGET_T2).getColumn();
            int COL_TARGET_T3 = titleAdresses.get(TARGET_T3).getColumn();
            int COL_TARGET_T4 = titleAdresses.get(TARGET_T4).getColumn();
            // int COL_TOTAL_TARGET = titleAdresses.get(TOTAL_TARGET).getColumn();
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

                String noString =
                        row.getCell(COL_NO).getCellType() == CellType.NUMERIC ?
                                StringUtils.trimToNull(Integer.toString((int) (row.getCell(COL_NO).getNumericCellValue()))) :
                                StringUtils.trimToNull(row.getCell(COL_NO).getStringCellValue());
                LOGGER.debug(noString);
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

                int T1I = (int) row.getCell(COL_TARGET_T1).getNumericCellValue();
                int T2I = (int) row.getCell(COL_TARGET_T2).getNumericCellValue();
                int T3I = (int) row.getCell(COL_TARGET_T3).getNumericCellValue();
                int T4I = (int) row.getCell(COL_TARGET_T4).getNumericCellValue();
                // cantones
                // locations
                Set<Canton> indicatorLocations = new HashSet<>();
                String locationsTotal = StringUtils.trimToNull(row.getCell(COL_CANTONS).getStringCellValue());
                if (StringUtils.isEmpty(locationsTotal)) {
                    throw new GeneralAppException("No se encontraron lugares de ejecución para el indicador "
                            + productIndicatorString + ". '" + NO + "'.", Response.Status.BAD_REQUEST);
                }
                List<String> locationStringList = Arrays.asList(StringUtils.split(locationsTotal, ","));
                if (CollectionUtils.isEmpty(locationStringList)) {
                    throw new GeneralAppException("No se encontraron lugares de ejecución para el indicador "
                            + productIndicatorString + ". '" + NO + "'.", Response.Status.BAD_REQUEST);
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
                totalLocations.addAll(indicatorLocations);
                LOGGER.info("cantones indicador: " + indicatorLocations.size());
                indicatorExecutionAssigmentWebs.add(indicatorExecutionAssigmentWeb);

                // targets
                List<QuarterWeb> quarterWebs = new ArrayList<>();
                QuarterWeb q1 = new QuarterWeb();
                q1.setQuarter(QuarterEnum.I);
                q1.setTarget(new BigDecimal(T1I));
                quarterWebs.add(q1);
                QuarterWeb q2 = new QuarterWeb();
                q2.setQuarter(QuarterEnum.II);
                q2.setTarget(new BigDecimal(T2I));
                quarterWebs.add(q2);
                QuarterWeb q3 = new QuarterWeb();
                q3.setQuarter(QuarterEnum.III);
                q3.setTarget(new BigDecimal(T3I));
                quarterWebs.add(q3);
                QuarterWeb q4 = new QuarterWeb();
                q4.setQuarter(QuarterEnum.IV);
                q4.setTarget(new BigDecimal(T4I));
                quarterWebs.add(q4);
                targetsMap.put(indicatorExecutionAssigmentWeb.hashCode(), quarterWebs);
                LOGGER.debug(T1I + "-" + T2I + "-" + T3I + "-" + T4I);
            }

            // end indicators
            LOGGER.info("cantones total: " + totalLocations.size());
            LOGGER.info("target total: " + this.getGeneralTarget(sheet));

            ProjectWeb projectWebOrg = this.modelWebTransformationService.projectToProjectWeb(project);
            List<CantonWeb> totalLocaltionWeb = this.modelWebTransformationService.cantonsToCantonsWeb(new ArrayList<>(totalLocations));

            projectWeb.getLocations().addAll(totalLocaltionWeb);
            this.projectService.updateProjectLocations(projectWebOrg, project, false);
            for (IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb : indicatorExecutionAssigmentWebs) {
                IndicatorExecution ie = this.indicatorExecutionService.assignPerformanceIndicatoToProject(indicatorExecutionAssigmentWeb);

                List<QuarterWeb> targets = targetsMap.get(indicatorExecutionAssigmentWeb.hashCode());
                TargetUpdateDTOWeb targetUpdateDTOWeb = new TargetUpdateDTOWeb();
                targetUpdateDTOWeb.setQuarters(new ArrayList<>());
                for (Quarter quarter : ie.getQuarters()) {
                    Optional<QuarterWeb> targetTmpOpt = targets.stream().filter(quarterWeb -> quarterWeb.getQuarter().equals(quarter.getQuarter())).findFirst();
                    QuarterWeb q = this.modelWebTransformationService.quarterToQuarterWeb(quarter);
                    if (targetTmpOpt.isPresent()) {


                        q.setTarget(targetTmpOpt.get().getTarget());
                    }
                    targetUpdateDTOWeb.getQuarters().add(q);
                }
                targetUpdateDTOWeb.setIndicatorType(ie.getIndicatorType());
                targetUpdateDTOWeb.setIndicatorExecutionId(ie.getId());


                this.indicatorExecutionService.updateTargets(targetUpdateDTOWeb);
            }

            // general target
            Integer generalTarget = this.getGeneralTarget(sheet);
            if (generalTarget!=null){
                List<IndicatorExecutionWeb> generalIes = this.indicatorExecutionService.getGeneralIndicatorExecutionsByProjectIdAndState(project.getId(), State.ACTIVO);

                if (CollectionUtils.isNotEmpty(generalIes)) {
                    IndicatorExecutionWeb generalIe = generalIes.get(0);
                    generalIe.setTarget(new BigDecimal(generalTarget));
                    TargetUpdateDTOWeb targetUpdateDTOWebGeneral= new TargetUpdateDTOWeb();
                    targetUpdateDTOWebGeneral.setTotalTarget(new BigDecimal(generalTarget));
                    targetUpdateDTOWebGeneral.setIndicatorType(IndicatorType.GENERAL);
                    targetUpdateDTOWebGeneral.setIndicatorExecutionId(generalIe.getId());
                    this.indicatorExecutionService.updateTargets(targetUpdateDTOWebGeneral);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralAppException(ExceptionUtils.getMessage(e), Response.Status.BAD_REQUEST);


        } finally {
            if (file != null) {
                file.close();
            }
        }


    }

    private Map<String, CellAddress> getTitleAdresses(XSSFSheet sheet) throws GeneralAppException {
        Map<String, CellAddress> titleMaps = new HashMap<>();

        titleMaps.put(NO, null);
        titleMaps.put(PRODUCT_STATEMENT, null);
        titleMaps.put(MAIN_ACTIVITIES, null);
        titleMaps.put(PRODUCT_INDICATOR, null);
        titleMaps.put(TARGET_T1, null);
        titleMaps.put(TARGET_T2, null);
        titleMaps.put(TARGET_T3, null);
        titleMaps.put(TARGET_T4, null);
        titleMaps.put(TOTAL_TARGET, null);
        titleMaps.put(CANTONS, null);

        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
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
                if (titleMaps.values().stream().filter(cellAddress -> cellAddress != null).count() < 2) {
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

    private Integer getGeneralTarget(XSSFSheet sheet) throws GeneralAppException {
        String label = "General - No. total de beneficiarios";
        CellAddress lableAddress = null;
        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
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
}
