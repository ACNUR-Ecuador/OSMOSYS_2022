package org.unhcr.osmosys.services.dataImport;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.services.*;
import org.unhcr.osmosys.webServices.model.PeriodWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class ProjectsImportService {


    @Inject
    PeriodService periodService;

    @Inject
    OrganizacionService organizacionService;

    @Inject
    UserService userService;

    @Inject
    ProjectService projectService;

    @Inject
    CantonService cantonService;

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    private final static Logger LOGGER = Logger.getLogger(ProjectsImportService.class);
    private static final String FILE_NAME = "C:\\test\\projects_importV6.xlsx";

    private static final String CODE = "CODIGO";
    private static final String TITLE = "TITULO";
    private static final String PARTNER = "SOCIO";
    private static final String INITIAL_DATE = "FECHA_INICIO";
    private static final String END_DATE = "FECHA_FIN";
    private static final String FOCAL_POINT = "CORREO_PUNTO_FOCAL";
    private static final String GENERAL_TARGET = "META_INDICADOR_GENERAL";
    private static final String LOCATIONS = "LUGARES_DE_EJECUCION";

    public void projectsImport(PeriodWeb periodWeb
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
            XSSFSheet sheet = workbook.getSheet("projects");

            Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheet);
            LOGGER.error(titleAdresses.get(CODE).getColumn());
            LOGGER.error(titleAdresses.get(CODE).getRow());
            CellAddress CODE_CELL = titleAdresses.get(CODE);
            int rowInitial = CODE_CELL.getRow();
            // get INDICATORS


            int COL_CODE = titleAdresses.get(CODE).getColumn();
            int COL_TITLE = titleAdresses.get(TITLE).getColumn();
            int COL_PARTNER = titleAdresses.get(PARTNER).getColumn();
            int COL_INITIAL_DATE = titleAdresses.get(INITIAL_DATE).getColumn();
            int COL_END_DATE = titleAdresses.get(END_DATE).getColumn();
            int COL_FOCAL_POINT = titleAdresses.get(FOCAL_POINT).getColumn();
            int COL_GENERAL_TARGET = titleAdresses.get(GENERAL_TARGET).getColumn();
            int COL_LOCATIONS = titleAdresses.get(LOCATIONS).getColumn();


            Iterator<Row> rowIterator = sheet.iterator();
            // get indicators
            List<Project> projects= new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() <= rowInitial) {
                    continue;
                }

                // get project
                if (
                        row.getCell(COL_CODE) == null || row.getCell(COL_CODE).getCellType() == CellType.BLANK
                                || row.getCell(COL_TITLE) == null || row.getCell(COL_TITLE).getCellType() == CellType.BLANK
                ) {
                    continue;
                }
                // find by year and code
                String code = StringUtils.trimToNull(row.getCell(COL_CODE).getStringCellValue());

                String title = StringUtils.trimToNull(row.getCell(COL_TITLE).getStringCellValue());
                Project project;

                project = this.projectService.getByCode(code);
                if (project != null) {
                    if (!Objects.equals(project.getPeriod().getId(), period.getId())) {
                        throw new GeneralAppException("Ya existe un proyecto con código: " + code + " para el periodo " + period.getYear() + "." +
                                " Por favor modifíquelo manualmente.", Response.Status.BAD_REQUEST);
                    }
                } else {
                    project = new Project();
                }

                project.setCode(code);
                project.setName(title);
                project.setState(State.ACTIVO);
                try {
                    Date initialDate = row.getCell(COL_INITIAL_DATE).getDateCellValue();
                    LocalDate initialLocalDate = Instant.ofEpochMilli(initialDate.getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    project.setStartDate(initialLocalDate);
                } catch (IllegalStateException e) {
                    throw new GeneralAppException("La fecha inicial del proyecto: " + code + " no puede ser leido como fecha " +
                            " '" + row.getCell(COL_CODE).getStringCellValue() + "'.", Response.Status.BAD_REQUEST);
                }
                try {
                    Date endDate = row.getCell(COL_END_DATE).getDateCellValue();
                    LocalDate endLocalDate = Instant.ofEpochMilli(endDate.getTime())

                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    project.setEndDate(endLocalDate);
                } catch (IllegalStateException e) {
                    throw new GeneralAppException("La fecha final del proyecto: " + code + " no puede ser leido como fecha " +
                            " '" + row.getCell(COL_CODE).getStringCellValue() + "'.", Response.Status.BAD_REQUEST);
                }
                String partnerAcronym = StringUtils.trimToNull(row.getCell(COL_PARTNER).getStringCellValue());
                Organization partner = this.organizacionService.getByAcronym(partnerAcronym);
                if (partner == null) {
                    throw new GeneralAppException("El socio con acrónimo: " + partnerAcronym + " no puede pudo ser encontrado." +
                            " Por favor corregir el dato o registrar el nuevo socio", Response.Status.BAD_REQUEST);
                }
                project.setOrganization(partner);

                String focalPointEmail = StringUtils.trimToNull(row.getCell(COL_FOCAL_POINT).getStringCellValue());
                User focalPoint = this.userService.getByEmail(focalPointEmail);
                if (focalPoint == null) {
                    throw new GeneralAppException("No se pudo encontrar un usuario con el correo : " + focalPointEmail +
                            " para el proyecto " + code + ". Cree el usuario o asigne otro punto focal." + row.getCell(COL_CODE).getStringCellValue() + "'.", Response.Status.BAD_REQUEST);
                } else if (!focalPoint.getOrganization().getAcronym().equalsIgnoreCase("ACNUR")) {
                    throw new GeneralAppException("el punto focal debe ser un colega de ACNUR para el proyecto"
                            + code + "." + row.getCell(COL_CODE).getStringCellValue() + "'.", Response.Status.BAD_REQUEST);
                }

                project.setFocalPoint(focalPoint);
                project.setPeriod(period);
                // locations
                String locationsTotal = StringUtils.trimToNull(row.getCell(COL_LOCATIONS).getStringCellValue());
                if (StringUtils.isEmpty(locationsTotal)) {
                    throw new GeneralAppException("No se encontraron lugares de ejecución para el proyecto "
                            + code + "." + row.getCell(COL_LOCATIONS).getStringCellValue() + "'.", Response.Status.BAD_REQUEST);
                }
                List<String> locationStringList = Arrays.asList(StringUtils.split(locationsTotal, ","));
                if (CollectionUtils.isEmpty(locationStringList)) {
                    throw new GeneralAppException("No se encontraron lugares de ejecución para el proyecto "
                            + code + "." + row.getCell(COL_LOCATIONS).getStringCellValue() + "'..", Response.Status.BAD_REQUEST);
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
                        ProjectLocationAssigment projectLocationAssigment = new ProjectLocationAssigment();
                        projectLocationAssigment.setLocation(canton);
                        projectLocationAssigment.setState(State.ACTIVO);
                        project.addProjectLocationAssigment(projectLocationAssigment);
                    }
                }

                // veo si hay q crear general indicator
                BigDecimal target;
                if (row.getCell(COL_GENERAL_TARGET).getCellType() == CellType.NUMERIC) {
                    double targetD = row.getCell(COL_GENERAL_TARGET).getNumericCellValue();
                    target = new BigDecimal(targetD);
                } else {
                    target = null;
                }
                IndicatorExecution generalIndicatorIE = this.indicatorExecutionService.createGeneralIndicatorForProject(project);
                if (generalIndicatorIE != null) {
                    generalIndicatorIE.setTarget(target);
                    project.addIndicatorExecution(generalIndicatorIE);
                }


                LOGGER.info(project);
                projects.add(project);



            }

            LOGGER.info("Proyectos a ingresar: " +projects.size());
            int counter=0;
            for (Project project : projects) {
                counter++;
                LOGGER.info("Proyecto :  "+counter +"/" +projects.size());
                this.projectService.saveOrUpdate(project);
            }
            file.close();


        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralAppException(ExceptionUtils.getMessage(e), Response.Status.BAD_REQUEST);


        }


    }

    private Map<String, CellAddress> getTitleAdresses(XSSFSheet sheet) throws GeneralAppException {
        Map<String, CellAddress> titleMaps = new HashMap<>();

        titleMaps.put(CODE, null);
        titleMaps.put(TITLE, null);
        titleMaps.put(PARTNER, null);
        titleMaps.put(INITIAL_DATE, null);
        titleMaps.put(END_DATE, null);
        titleMaps.put(FOCAL_POINT, null);
        titleMaps.put(GENERAL_TARGET, null);
        titleMaps.put(LOCATIONS, null);

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


            if (titleMaps.values().stream().anyMatch(Objects::isNull) && titleMaps.values().stream().anyMatch(Objects::nonNull)) {
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
}
