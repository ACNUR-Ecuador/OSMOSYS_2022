package org.unhcr.osmosys.services;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.ImportDTOs.CatalogImportDTO;

import javax.ejb.Stateless;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Stateless
public class ImportService {
    private final static Logger LOGGER = Logger.getLogger(ImportService.class);
    private static final String FILE_NAME = "C:\\TESTS\\catalogo.xlsx";

    private static final String AI = "Area de Impacto & Declaración de Impacto ";
    private static final String AO = "Área de Resultado (Outcome Area)";
    private static final String AOU = "Declaración de Resultado (Outcome Statement)";
    private static final String OUS = "Declaración de Producto (Output statement)";
    private static final String INDICATOR = "Indicador de Producto";
    private static final String INDICATOR_CODE = "Código Indicador";
    private static final String INDICATOR_CODE_AI = "Indicador ActivityInfo";
    private static final String DISSAGREGATION = "Desagregación Sugerida";

    public void catalogImport() {
        LOGGER.info("test import");
        try {


            FileInputStream file = new FileInputStream(FILE_NAME);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            Map<String, CellAddress> titleAdresses = this.getTitleAdresses(sheet);

            // get SOUAdresss
            CellAddress OUSAdresss = titleAdresses.get(OUS);
            int rowOUS = OUSAdresss.getRow();
            int colOUS = OUSAdresss.getColumn();
            int colINDICATOR = titleAdresses.get(INDICATOR).getColumn();
            int colINDICATOR_CODE = titleAdresses.get(INDICATOR_CODE).getColumn();
            int colINDICATOR_CODE_AI = titleAdresses.get(INDICATOR_CODE_AI).getColumn();
            int colDISSAGREGATION = titleAdresses.get(DISSAGREGATION).getColumn();

            Iterator<Row> rowIterator = sheet.iterator();

            List<CatalogImportDTO> indicatorsList= new ArrayList<>();
            List<CatalogImportDTO> indicatorsListErrors= new ArrayList<>();
            String currentOUS =null;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if(row.getRowNum()<=rowOUS){
                    continue;
                }

                CatalogImportDTO indicatorDto = new CatalogImportDTO();

                if(StringUtils.isNotBlank(row.getCell(colOUS).getStringCellValue())){
                    currentOUS=StringUtils.trimToNull(row.getCell(colOUS).getStringCellValue());
                }
                indicatorDto.setTotalStatement(currentOUS);
                indicatorDto.setTotalIndicator(StringUtils.trimToNull(row.getCell(colINDICATOR).getStringCellValue()));
                indicatorDto.setCodeIndicator(StringUtils.trimToNull(row.getCell(colINDICATOR_CODE).getStringCellValue()));
                indicatorDto.setTotalActivityInfoIndicator(StringUtils.trimToNull(row.getCell(colINDICATOR_CODE_AI).getStringCellValue()));
                indicatorDto.setTodalDissagregation(StringUtils.trimToNull(row.getCell(colDISSAGREGATION).getStringCellValue()));

                if(row.getCell(colINDICATOR).getCellComment()!=null){
                    indicatorDto.setIndicatorDescription(row.getCell(colINDICATOR).getCellComment().getString().getString());
                }
                if(indicatorDto.hasFullData()){
                    indicatorsList.add(indicatorDto);
                }else {
                    indicatorsListErrors.add(indicatorDto);
                }

                LOGGER.info("--------------------------------------");
                LOGGER.info(indicatorDto);

            }


            file.close();
        } catch (Exception e) {
            e.printStackTrace();


        }


    }

    private Map<String, CellAddress> getTitleAdresses(XSSFSheet sheet) {
        Map<String, CellAddress> titleMaps = new HashMap<>();
        titleMaps.put(AI, null);
        titleMaps.put(AO, null);
        titleMaps.put(AOU, null);
        titleMaps.put(OUS, null);
        titleMaps.put(INDICATOR, null);
        titleMaps.put(INDICATOR_CODE, null);
        titleMaps.put(INDICATOR_CODE_AI, null);
        titleMaps.put(DISSAGREGATION, null);
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
                if (cell.getStringCellValue() != null) {
                    for (String key : titleMaps.keySet()) {
                        if (key.equalsIgnoreCase(cell.getStringCellValue())) {
                            titleMaps.put(key, cell.getAddress());
                        }
                    }
                }
            }

            if (titleMaps.values().stream().filter(Objects::isNull).count() < 1) {
                break;
            }

        }

        titleMaps.forEach((s, cellAddress) -> {
            LOGGER.debug(s + ": " + cellAddress.formatAsString());
        });
        return titleMaps;


    }
}
