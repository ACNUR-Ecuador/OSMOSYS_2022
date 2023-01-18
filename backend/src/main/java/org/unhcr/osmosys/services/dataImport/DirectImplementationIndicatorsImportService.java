package org.unhcr.osmosys.services.dataImport;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.utils.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.services.*;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class DirectImplementationIndicatorsImportService {


    @Inject
    IndicatorService indicatorService;

    @Inject
    CantonService cantonService;
    @Inject
    ModelWebTransformationService modelWebTransformationService;
    @Inject
    PeriodService periodService;
    @Inject
    OfficeService officeService;

    @Inject
    UserService userService;

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    FileUtils fileUtils;

    private final static Logger LOGGER = Logger.getLogger(DirectImplementationIndicatorsImportService.class);
    // private static final String FILE_NAME = "D:\\proyectos\\OSMOSYS_2023\\DataImport2023\\partnerisIndicators\\FUDELA SOIB.xlsx";

    private static final String PRODUCT_INDICATOR = "Indicadores de Producto";
    private static final String MAIN_ACTIVITIES = "Actividades clave de Producto";
    private static final String CANTONS = "Cantones de Ejecución";
    private static final String REPORTER = "Responsable de Reporte";
    private static final String SUPERVISOR = "Punto Focal de Unidad/FO";


    private static final int MAX = 50;

    public void directImplementationIndicatorsImport(Long periodId, Long officeId, ImportFileWeb importFileWeb) throws GeneralAppException {
        byte[] fileContent = this.fileUtils.decodeBase64ToBytes(importFileWeb.getFile());
        InputStream targetStream = new ByteArrayInputStream(fileContent);
        this.directImplementationIndicatorsImport(periodId,  officeId, targetStream);
    }

    public void directImplementationIndicatorsImport(Long periodId, Long officeId,
                                                     InputStream file
    ) throws GeneralAppException {
        LOGGER.info("test import");
        // FileInputStream file = null;
        try {
            Period period = this.periodService.getById(periodId);
            if (period == null) {
                throw new GeneralAppException("El periodo con id " + periodId + " no existe", Response.Status.BAD_REQUEST);

            }
            Office office = this.officeService.getById(officeId);
            if (office == null) {
                throw new GeneralAppException("La oficina con id " + periodId + " no existe", Response.Status.BAD_REQUEST);

            }
            // file = new FileInputStream(FILE_NAME);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheet("indicadores_oficina");

            Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheet);
            CellAddress CODE_CELL = titleAdresses.get(PRODUCT_INDICATOR);
            int rowInitial = CODE_CELL.getRow();
            // get INDICATORS


            int COL_PRODUCT_INDICATOR = titleAdresses.get(PRODUCT_INDICATOR).getColumn();
            int COL_MAIN_ACTIVITIES = titleAdresses.get(MAIN_ACTIVITIES).getColumn();
            int COL_CANTONS = titleAdresses.get(CANTONS).getColumn();
            int COL_REPORTER = titleAdresses.get(REPORTER).getColumn();
            int COL_SUPERVISOR = titleAdresses.get(SUPERVISOR).getColumn();


            Iterator<Row> rowIterator = sheet.iterator();
            // get indicators
            DataFormatter dataFormatter = new DataFormatter();


            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() <= rowInitial) {
                    continue;
                }


                // get produdctStatement
                if (
                        row.getCell(COL_PRODUCT_INDICATOR) == null
                                || row.getCell(COL_PRODUCT_INDICATOR).getCellType() == CellType.BLANK
                                || dataFormatter.formatCellValue(row.getCell(COL_PRODUCT_INDICATOR)) == null

                ) {
                    break;
                }
                // find by year and code
                IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb = new
                        IndicatorExecutionAssigmentWeb();

                String productIndicatorString = StringUtils.trimToNull(dataFormatter.formatCellValue(row.getCell(COL_PRODUCT_INDICATOR)));
                String indicatorCode = StringUtils.split(productIndicatorString, " ", 2)[0];
                Indicator indicator = this.indicatorService.getByPeriodAndCode(period.getId(), indicatorCode);
                indicatorExecutionAssigmentWeb.setIndicator(this.modelWebTransformationService.indicatorToIndicatorWeb(indicator, false, true, false));
                indicatorExecutionAssigmentWeb.setState(State.ACTIVO);
                indicatorExecutionAssigmentWeb.setKeepBudget(false);
                indicatorExecutionAssigmentWeb.setPeriod(this.modelWebTransformationService.periodToPeriodWeb(period));
                indicatorExecutionAssigmentWeb.setReportingOffice(this.modelWebTransformationService.officeToOfficeWeb(office, false));

                String reporterString = StringUtils.trimToNull(dataFormatter.formatCellValue(row.getCell(COL_REPORTER)));
                User reporter = this.userService.getUNHCRUsersByName(reporterString);
                if (reporter == null) {
                    throw new GeneralAppException("Usuario " + REPORTER + " no encontrado " + reporterString + " en el indicaodor " + productIndicatorString, Response.Status.BAD_REQUEST);
                }
                indicatorExecutionAssigmentWeb.setAssignedUser(this.modelWebTransformationService.userToUserWebSimple(reporter, false, false));


                String supervisorString = StringUtils.trimToNull(dataFormatter.formatCellValue(row.getCell(COL_SUPERVISOR)));
                User supervisor = this.userService.getUNHCRUsersByName(supervisorString);
                if (supervisor == null) {
                    throw new GeneralAppException("Usuario " + SUPERVISOR + " no encontrado " + supervisorString + " en el indicaodor " + productIndicatorString, Response.Status.BAD_REQUEST);
                }
                indicatorExecutionAssigmentWeb.setSupervisorUser(this.modelWebTransformationService.userToUserWebSimple(supervisor, false, false));

                // cantones
                // locations
                Set<Canton> indicatorLocations = new HashSet<>();
                String locationsTotal = StringUtils.trimToNull(dataFormatter.formatCellValue(row.getCell(COL_CANTONS)));
                if (StringUtils.isNotEmpty(locationsTotal)) {

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
                }
                // create ie
                String mainActivities = StringUtils.trimToNull(dataFormatter.formatCellValue(row.getCell(COL_MAIN_ACTIVITIES)));

                indicatorExecutionAssigmentWeb.setActivityDescription(mainActivities);
                indicatorExecutionAssigmentWeb.setLocations(new ArrayList<>(this.modelWebTransformationService.cantonsToCantonsWeb(new ArrayList<>(indicatorLocations))));
                this.indicatorExecutionService.assignPerformanceIndicatoDirectImplementation(indicatorExecutionAssigmentWeb);
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralAppException(ExceptionUtils.getMessage(e), Response.Status.BAD_REQUEST);


        }


    }

    private Map<String, CellAddress> getTitleAdresses(XSSFSheet sheet) throws GeneralAppException {
        Map<String, CellAddress> titleMaps = new HashMap<>();

        titleMaps.put(PRODUCT_INDICATOR, null);
        titleMaps.put(MAIN_ACTIVITIES, null);
        titleMaps.put(CANTONS, null);
        titleMaps.put(REPORTER, null);
        titleMaps.put(SUPERVISOR, null);

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



    public ByteArrayOutputStream generateTemplate(Long periodId, Long officeId) throws GeneralAppException {

        Period period = this.periodService.getById(periodId);
        Office office = this.officeService.getById(officeId);
        final String filename = "direct_implementation_indicators_template.xlsm";
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
                    case "indicators":
                        fillIndicators(sheetTemplate, sheetOptions, table, period);
                        break;
                    case "cantons":
                        fillCantons(sheetTemplate, sheetOptions, table);
                        break;
                    case "users":
                        fillUsers(sheetTemplate, sheetOptions, table, office.getId());
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

    private void fillUsers(XSSFSheet sheetTemplate, XSSFSheet sheetOptions, XSSFTable tableOptions, Long officeId) throws GeneralAppException {
        int firstRow = tableOptions.getArea().getFirstCell().getRow();
        int firstCol = tableOptions.getArea().getFirstCell().getCol();

        List<User> users = this.userService.getUNHCRUsersByOfficeIdAndState(State.ACTIVO, officeId);

        List<String> listaValues = users.stream().map(User::getName)
                .sorted().collect(Collectors.toList());

        for (String name : listaValues) {
            firstRow++;
            XSSFRow row = sheetOptions.getRow(firstRow);
            if (row == null) {
                row = sheetOptions.createRow(firstRow);
            }
            XSSFCell cell = row.getCell(firstCol);
            if (cell != null) {
                LOGGER.info("cell: " + cell.getStringCellValue());
                cell.setCellValue(name);
            } else {
                cell = row.createCell(firstCol);
                cell.setCellValue(name);
            }
        }

        tableOptions.setArea(new AreaReference(tableOptions.getArea().getFirstCell(), new CellReference(firstRow, firstCol), null));

        // validation
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetTemplate);
        String listFormula = "INDIRECT(\"" + tableOptions.getName() + "[" + tableOptions.getName() + "]\")";
        LOGGER.info(listFormula);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createFormulaListConstraint(listFormula);

        Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheetTemplate);
        CellAddress cell_units = titleAdresses.get(REPORTER);
        CellRangeAddressList addressList = new CellRangeAddressList(cell_units.getRow() + 1, cell_units.getRow() + MAX,
                cell_units.getColumn(), cell_units.getColumn());
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        sheetTemplate.addValidationData(validation);
        CellAddress cell_units2 = titleAdresses.get(SUPERVISOR);
        CellRangeAddressList addressList2 = new CellRangeAddressList(cell_units2.getRow() + 1, cell_units2.getRow() + MAX,
                cell_units2.getColumn(), cell_units2.getColumn());
        XSSFDataValidation validation2 = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList2);
        sheetTemplate.addValidationData(validation2);


    }

    private void fillIndicators(XSSFSheet sheetTemplate, XSSFSheet sheetOptions, XSSFTable tableOptions, Period period) throws GeneralAppException {
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

        Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheetTemplate);
        CellAddress cell_units = titleAdresses.get(PRODUCT_INDICATOR);
        CellRangeAddressList addressList = new CellRangeAddressList(cell_units.getRow() + 1, cell_units.getRow() + MAX,
                cell_units.getColumn(), cell_units.getColumn());
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        sheetTemplate.addValidationData(validation);


    }


    private void fillCantons(XSSFSheet sheetTemplate, XSSFSheet sheetOptions, XSSFTable tableOptions) throws GeneralAppException {
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

        Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheetTemplate);
        CellAddress cell_units = titleAdresses.get(CANTONS);
        CellRangeAddressList addressList = new CellRangeAddressList(cell_units.getRow() + 1,
                cell_units.getRow() + MAX,
                cell_units.getColumn(), cell_units.getColumn());
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        sheetTemplate.addValidationData(validation);


    }
}
