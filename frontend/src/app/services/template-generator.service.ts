import { Injectable } from '@angular/core';
import * as FileSaver from 'file-saver';
import * as ExcelJS from 'exceljs';
import { UtilsService } from './utils.service';
@Injectable({
  providedIn: 'root'
})
export class TemplateGeneratorService {

  constructor(public utilsService: UtilsService) { }

  async generateExcel(dissagregationTypeList:any[]) {
    // Crear un nuevo workbook
    const workbook = new ExcelJS.Workbook();
     // Crear una nueva hoja de cálculo
     const worksheet = workbook.addWorksheet('Mi Hoja');
     

    //CREAR CATALOGO PARA CADA DESAGREGACION
    const catalogueList = dissagregationTypeList.map(item => {
      const key = Object.keys(item)[0]; // Obtener la clave del objeto
      const concatenatedNames = item[key].options
          .filter(subItem => subItem.state === 'ACTIVO') // Filtrar solo los elementos con estado 'ACTIVE'
          .map(subItem => 
              subItem.name.replace(/--/g, '-')  // Reemplazar '--' por '-'
                          .replace(/\s*-\s*/g, '-') // Eliminar espacios alrededor del '-'
                          .replace(/,/g, ';') // Reemplazar comas por punto y coma
          )
          .join(','); // Concatenar los nombres con coma
      return { [key]: concatenatedNames }; 
  });
  
  

  // Función para obtener el CATALOGO para una desagregacion
      function getCatalogue(key:string) {
        const result = catalogueList.find(item => item[key] !== undefined);
        return result ? result[key] : ""; 
      }

    const fixedColumns = [
      { header: 'Valor', key: 'value', width: 50 } // Columna fija después del array
  ];

  // Crear el arreglo de columnas dinámicamente
  const dynamicColumns = dissagregationTypeList.map(item => ({
    
    header: item[Object.keys(item)[0]].label,
    key: Object.keys(item)[0],
    width: 50 
}));


// Concatenar las columnas fijas con las dinámicas
const allColumns = [
  ...dynamicColumns, // Columnas dinámicas
  fixedColumns[0] // Columna fija al final
];

  // Asignar las columnas al worksheet
  worksheet.columns = allColumns;

   

    // Estilo solo para los encabezados
    const headerRow = worksheet.getRow(1);
    headerRow.eachCell((cell, colNumber) => {
      cell.font = {
        bold: true,
        color: { argb: 'FFFFFF' }, // Color de texto blanco
      };
      cell.fill = {
        // Color de fondo azul
        type: 'pattern',
        pattern: 'solid',
        fgColor: { argb: '4472C4' },
      };
      cell.border = {
        bottom: { style: 'thin' },
        left: { style: 'thin' },
        right: { style: 'thin' },
        top: { style: 'thin' },
      };
      // Centrando el texto
      cell.alignment = {
        horizontal: 'center',
        vertical: 'middle',
      };
    });

    //Realizar validaciones y colocar estilos 
    allColumns.forEach((item, index) => {
      let letter = String.fromCharCode(65 + index);

      //Aplicar a las primeras 100 filas
      for (let i = 2; i <= 100; i++) {  
          // Aplicar estilo solo a celdas pares
          if (i % 2 === 0) {
              worksheet.getCell(letter + i).fill = {
                  type: 'pattern',
                  pattern: 'solid',
                  fgColor: { argb: 'D9E1F2' },
              };
          }
  
          // Aplicar validación de datos a la columna Valor
          if (index === allColumns.length - 1) { 
              worksheet.getCell(letter + i).dataValidation = {
                  type: 'custom', 
                  formulae: [
                      `AND(ISNUMBER(${letter}${i}), ${letter}${i}>0)`, // Verifica que sea número y mayor que 0
                  ],
                  showErrorMessage: true,
                  errorTitle: 'Entrada inválida',
                  error: 'Por favor, introduce un número positivo.',
              };
          }else{
            //Aplica validacion y asigna opciones a cada celda 
              worksheet.getCell(letter+i).dataValidation = {
                type: 'list',
                allowBlank: true,
                formulae: [`"${getCatalogue(item.key)}"`],
                showErrorMessage: true,
                errorTitle: 'Entrada inválida',
                error: 'Por favor selecciona un valor de la lista.',
            };
            }
          
          
      }
  });
  

    const finalLetter = String.fromCharCode(65 + dissagregationTypeList.length);
  
     // Aplicar filtro from A1 to final Letter
     worksheet.autoFilter = {
      from: 'A1',
      to: finalLetter+1,
  }
    

  
    
    // Guardar el archivo en el sistema
     workbook.xlsx.writeBuffer().then(excelData => {
        const blob = new Blob([excelData], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
        const EXCEL_EXTENSION = '.xlsx';
        FileSaver.saveAs(blob, "Plantilla_de_Importación_"+ new Date().getTime() + EXCEL_EXTENSION);

    });


}

    

}
