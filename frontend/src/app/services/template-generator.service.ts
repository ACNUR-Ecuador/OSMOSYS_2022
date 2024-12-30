import { Injectable } from '@angular/core';
import * as FileSaver from 'file-saver';
import * as ExcelJS from 'exceljs';
import { UtilsService } from './utils.service';
import { Password } from 'primeng/password';
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
    
    const catalogueListold = dissagregationTypeList.map(item => {
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

  // Crear el catálogo para cada desagregación
const catalogueList = dissagregationTypeList.map(item => {
  const key = Object.keys(item)[0]; // Obtener la clave del objeto
  const concatenatedNames = item[key].options
      .filter(subItem => subItem.state === 'ACTIVO') // Filtrar solo los elementos con estado 'ACTIVO'
      .map(subItem => 
          subItem.name.replace(/--/g, '-')  // Reemplazar '--' por '-'
                      .replace(/\s*-\s*/g, '-') // Eliminar espacios alrededor del '-'
                      .replace(/,/g, ';') // Reemplazar comas por punto y coma
      );
  return { [key]: concatenatedNames }; 
});

  
  

  // Función para obtener el CATALOGO para una desagregacion
      function getCatalogue(key:string) {
        const result = catalogueList.find(item => item[key] !== undefined);
        return result ? result[key] : []; 
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

    //Inicio Código anterior
/*
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
                      `AND(ISNUMBER(${letter}${i}), ${letter}${i}>=0)`, // Verifica que sea número y mayor que 0
                  ],
                  showErrorMessage: true,
                  errorTitle: 'Entrada inválida',
                  error: 'Por favor, introduce un número positivo.',
              };
          }else{
            console.log(getCatalogue(item.key))
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
*/
  //Fin codigo anterior

  const catalogueSheet = workbook.addWorksheet('Catalogs');

// Recorrer todos los catálogos y agregarlos a la hoja 'Catalogs'
allColumns.forEach((item, index) => {
  const key = item.key;  // Usar la clave para identificar el catálogo
  const catalogValues = getCatalogue(key); // Obtener el catálogo de la desagregación
  const letter = String.fromCharCode(65 + index);  // Obtener la letra de la columna

   // Agregar encabezado para cada columna del catálogo
   if (index !== allColumns.length - 1){
      catalogueSheet.getCell(`${letter}1`).value = key; // El encabezado será el nombre del catálogo (key)
    // Aplicar estilo al encabezado (similar a la hoja principal)
      catalogueSheet.getCell(`${letter}1`).font = { bold: true, color: { argb: 'FFFFFF' } };
      catalogueSheet.getCell(`${letter}1`).fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '4F81BD' } }; // Color de fondo similar
      catalogueSheet.getCell(`${letter}1`).alignment = { horizontal: 'center', vertical: 'middle' };
    
    }
  
   

   if (catalogValues.length > 0) {
    // Escribir el catálogo en las filas de la hoja 'Catalogs'
    catalogValues.forEach((value, i) => {
      catalogueSheet.getCell(`${letter}${i + 2}`).value = value;  // Escribir cada valor en una fila de la columna correspondiente (empieza desde fila 2)
      
      // Aplicar estilo similar a las celdas de la hoja principal (estilo de celdas pares)
      if ((i + 2) % 2 === 0) {  // Solo aplica a las filas pares
        catalogueSheet.getCell(`${letter}${i + 2}`).fill = {
          type: 'pattern',
          pattern: 'solid',
          fgColor: { argb: 'D9E1F2' }, // Color similar a la hoja principal
        };
      }
    });
  }

   // Ajustar el ancho de la columna para que se ajuste al contenido más largo
   const maxLength = Math.max(...catalogValues.map(value => value.length), key.length);  // Obtenemos la longitud máxima (incluyendo el encabezado)
   catalogueSheet.getColumn(letter).width = maxLength + 2;  // Añadimos un margen de 2 caracteres para que no esté justo al borde


    // Aplicar validación de datos para cada columna
    for (let i = 2; i <= 100; i++) {  
      const letter = String.fromCharCode(65 + index);  // Obtener la letra de la columna
       //Aplicar a las primeras 100 filas
       
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
                  `AND(ISNUMBER(${letter}${i}), ${letter}${i}>=0)`, // Verifica que sea número y mayor que 0
              ],
              showErrorMessage: true,
              errorTitle: 'Entrada inválida',
              error: 'Por favor, introduce un número positivo.',
          };
      }else{
        //Validar datos
      worksheet.getCell(letter + i).dataValidation = {
        type: 'list',
        allowBlank: true,
        formulae: [`'Catalogs'!$${letter}$2:$${letter}$${catalogValues.length + 1}`],  // Usar la columna A en 'Catalogs' como referencia
        showErrorMessage: true,
        errorTitle: 'Entrada inválida',
        error: 'Por favor selecciona un valor de la lista.',
        
      };
      worksheet.getCell(letter + i).protection = {
        locked: true
      }
    }
  }
    
    // Protección de celdas de catálogo en la hoja 'Catalogs'
    catalogValues.forEach((value, i) => {
      const cell = catalogueSheet.getCell(`${letter}${i + 1}`);
      cell.protection = { locked: true }; // Bloquear la celda para evitar modificaciones
    });
  }
);

// Proteger la hoja 'Catalogs' para que no se puedan modificar las celdas

catalogueSheet.protect('@DM1N2025', {
  formatCells: true,
  formatColumns: true,
  formatRows: true,
  insertRows: true,
  insertColumns: false,
  insertHyperlinks: true,
  deleteRows: true,
  deleteColumns: false,
  sort: true,
  autoFilter: true
})

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
