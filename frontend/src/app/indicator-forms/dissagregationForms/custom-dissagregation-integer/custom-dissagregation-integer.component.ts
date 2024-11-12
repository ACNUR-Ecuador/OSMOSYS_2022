import {Component, Input, OnInit} from '@angular/core';
import {SelectItemWithOrder} from '../../../shared/model/UtilsModel';
import {
    CustomDissagregation, IndicatorValueCustomDissagregationWeb
} from '../../../shared/model/OsmosysModel';
import {UtilsService} from '../../../services/utils.service';
import { TemplateGeneratorService } from 'src/app/services/template-generator.service';
import * as XLSX from "xlsx";
import { WorkBook } from "xlsx";
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-custom-dissagregation-integer',
  templateUrl: './custom-dissagregation-integer.component.html',
  styleUrls: ['./custom-dissagregation-integer.component.scss']
})
export class CustomDissagregationIntegerComponent implements OnInit {
    @Input()
    customDissagregation: CustomDissagregation;
    @Input()
    values: IndicatorValueCustomDissagregationWeb[];
    @Input()
    editable: boolean;

    dissagregationOptionsRows: SelectItemWithOrder<any>[];
    rows = new Array<Array<IndicatorValueCustomDissagregationWeb>>();
    importForm: FormGroup;
    showImportDialog: boolean = false;
    sheetOptions: string[];
    importErroMessage: string[];
    showImportErrorLogs: boolean;
    currentTimestamp: string;

    constructor(
        public utilsService: UtilsService,
        public templateService: TemplateGeneratorService,
        private fb: FormBuilder
    ) {
    }

    ngOnInit(): void {
        this.processDissagregationValues();
        this.importForm = this.fb.group({
            fileName: new FormControl('', [Validators.required]),
            file: new FormControl(''),
            sheet: new FormControl('', [Validators.required]),
            workbook: new FormControl('', [Validators.required]),
        });
        console.log(this.customDissagregation)
        console.log(this.rows);
       
    }

    processDissagregationValues() {
        // ontengo los tipos de desagregación horizontal y vertical

        this.dissagregationOptionsRows = this.customDissagregation
            .customDissagregationOptions.map(value => {
                const selectITem = new SelectItemWithOrder();
                selectITem.label = value.name;
                selectITem.value = value;
                return selectITem;
            }).sort((a, b) => a.label.localeCompare(b.label));

        this.dissagregationOptionsRows.forEach((value, index) => value.order = index);
        this.createTwoDimentionsGrid();

    }

    createTwoDimentionsGrid() {
        // ordeno
        // clasifico por cada uno de las opciones
        this.rows = [];
        this.rows = new Array<Array<IndicatorValueCustomDissagregationWeb>>();
        this.dissagregationOptionsRows.forEach(value => {
            const row = this.values.filter(value1 => {
                return value1.customDissagregationOption.id === value.value.id;
            });
            this.rows.push(row);
        });
    }

    fileUploader(event: any) {
        const file = event.files[0];
        this.importForm.get('fileName').setValue(file.name);
        this.importForm.get('fileName').markAsTouched();
        const fileReader = new FileReader();

        fileReader.readAsArrayBuffer(file);
        // tslint:disable-next-line:only-arrow-functions
        fileReader.onload = () => {
            const arrayBuffer: any = fileReader.result;
            let data = new Uint8Array(arrayBuffer);
            let arr = [];
            for (var i = 0; i != data.length; ++i) arr[i] = String.fromCharCode(data[i]);
            let bstr = arr.join("");
            let workbook: WorkBook;
            workbook = XLSX.read(bstr, { type: "binary" });
            this.importForm.get('workbook').patchValue(workbook);
            this.sheetOptions = [];
            workbook.SheetNames.forEach(value => {
                this.sheetOptions.push(value);
            });
            this.importForm.get('sheet').patchValue(null);
            this.importForm.get('sheet').markAsTouched();
        };

    }


    showImportDialogF() {
        this.showImportDialog = true;
        this.sheetOptions = [];
        this.showImportErrorLogs = false;
    }

    importFile() {
        this.showImportErrorLogs = false;
        let workbook = this.importForm.get('workbook').value as XLSX.WorkBook;
        let spreedsheetname = this.importForm.get('sheet').value;
        const ws: XLSX.WorkSheet = workbook.Sheets[spreedsheetname];
        const data = XLSX.utils.sheet_to_json(ws);
        this.importErroMessage = [];
        this.showImportErrorLogs = false;
        console.log(data)
        // Verificar que el arreglo no esté vacío
        if (data.length === 0) {
            this.importErroMessage.push('Error: El archivo se encuentra vacío')
            this.showImportErrorLogs = true;
            return
        }
        //creo el arrays de keys para la desagregacion especifica
        const dissagregationKeys = [
            this.customDissagregation.name
        ]

        const dissagregationLabels = [
            ...dissagregationKeys,
            'Valor'
        ]

        //transformo la data en un array de desagregaciones
        const indicatorValues = data.map(value => {
            //@ts-ignore
            const allExist = Object.keys(value).every(item => dissagregationLabels.includes(item))
            if (!allExist) {
                const allowedColumnsString = dissagregationLabels.join(', ');
                this.importErroMessage.push('Error: Uno de los encabezados en el archivo importado se encuentra mal escrito o no corresponde al tipo de desagregación, los encabezados validos son: ' +
                    allowedColumnsString)
                return this.showImportErrorLogs = true

            }
            const obj = {};
            dissagregationKeys.forEach(key => {
                let cellValue = value[key];
                if (cellValue !== undefined) {
                    cellValue = cellValue.replace(/;/g, ',')
                }
                obj[key] = cellValue

            });
            obj['value'] = value['Valor'];
            return obj;

        });

        if (this.importErroMessage.length > 0) {
            this.showImportErrorLogs = true;
            return
        }

        //validar los valores en cada fila
        const validationMessages = this.utilsService.validateCustomDataImportValues(indicatorValues, this.createdisagregationCatalogue())
        if (validationMessages.length > 0) {
            this.importErroMessage = validationMessages
            this.showImportErrorLogs = true;
            return
        }

        //Asignar los valores del array de excel creado a los valores en la tabla
        this.rows.forEach(Row => {
            Row.forEach(Col => {
                // Para cada objeto en valuesRowsMap, buscamos coincidencias en indicatorValues
                indicatorValues.forEach(item => {
                    // Extraigo el valor de cada objeto de IndicatorValues
                    const value2 = item['value'];

                    // creo un arreglo sin value para comparar los keys
                    const comparisonValues = Object.keys(item).reduce((acc, key) => {
                        if (key !== 'value') {
                            acc[key] = item[key];
                        }
                        return acc;
                    }, {});

                    // Comparo cada valor de las claves en comparisionValues con cada objeto de valuesRowsMap
                    const isMatch = Object.keys(comparisonValues).every(key => {
                            return comparisonValues[key] === Col.customDissagregationOption.name
                    });
                    if (isMatch) {
                        Col.value = value2;
                    }
                });
            });

        });

        this.showImportDialog = false;

    }


    cancelImportDialog() {
        this.showImportDialog = false;
        this.importForm.reset();
    }

    updateTimestamp() {
        const now = new Date();
        this.currentTimestamp = `Error al Importar - ${now.toLocaleString()}`;
    }

    onDialogShow() {
        this.updateTimestamp(); 
    }

 
    createdisagregationCatalogue(){
        const dissagregationTypes = [
            this.customDissagregation,
        ]
       

        const dissagregationCatalogue = dissagregationTypes.map((diss, index) => ({
        [diss.name]: {
            label: diss.name,
            options: diss.customDissagregationOptions},
        }));
       return dissagregationCatalogue
    }
    generarExcel(){
        this.templateService.generateExcel(this.createdisagregationCatalogue())
      .then(() => console.log('Archivo Excel generado exitosamente.'))
      .catch(error => console.error('Error al generar el archivo:', error));
    }

}
