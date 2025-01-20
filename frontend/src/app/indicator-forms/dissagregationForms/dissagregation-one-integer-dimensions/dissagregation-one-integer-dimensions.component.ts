import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { EnumsType } from '../../../shared/model/UtilsModel';
import { EnumWeb, IndicatorExecution, IndicatorValue, StandardDissagregationOption } from '../../../shared/model/OsmosysModel';
import { EnumsService } from '../../../services/enums.service';
import { UtilsService } from '../../../services/utils.service';
import { TemplateGeneratorService } from 'src/app/services/template-generator.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import * as XLSX from "xlsx";
import { WorkBook } from "xlsx";

@Component({
    selector: 'app-dissagregation-one-integer-dimensions',
    templateUrl: './dissagregation-one-integer-dimensions.component.html',
    styleUrls: ['./dissagregation-one-integer-dimensions.component.scss']
})
export class DissagregationOneIntegerDimensionsComponent implements OnInit, OnChanges {
    @Input()
    dissagregationType: EnumWeb;
    @Input()
    values: IndicatorValue[];
    @Input()
    editable: boolean;
    @Input()
    indicatorExecution: IndicatorExecution;

    dissagregationOptionsRows: StandardDissagregationOption[];
    //enumTypeRows: EnumWeb;
    dissagregationTypeRows: EnumWeb;
    rows = new Array<Array<IndicatorValue>>();
    importForm: FormGroup;
    showImportDialog: boolean = false;
    sheetOptions: string[];
    importErroMessage: string[];
    showImportErrorLogs: boolean;
    currentTimestamp: string;
    

    constructor(
        public enumsService: EnumsService,
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
        
    }

    processDissagregationValues() {
        // ontengo los tipos de desagregación horizontal y vertical
        this.rows = [];
        const dissagregationsTypesRyCEnum: EnumWeb[] = this.dissagregationType.standardDissagregationTypes
            .map(value => {
                return this.enumsService.resolveEnumWeb(EnumsType.DissagregationType, value)
            });
        this.dissagregationTypeRows = dissagregationsTypesRyCEnum[0];

        // obtiene las opciones
        this.dissagregationOptionsRows = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationTypeRows);

        this.dissagregationOptionsRows.forEach(dissagregationOption => {
            const row = this.values.filter(value => {
                const valueOption =
                    (this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationTypeRows, value));
                return valueOption.id === dissagregationOption.id;

            });
            this.rows.push(row);
        });

    }

    ngOnChanges() {
        this.processDissagregationValues();
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

        // Verificar que el arreglo no esté vacío
        if (data.length === 0) {
            this.importErroMessage.push('Error: El archivo se encuentra vacío')
            this.showImportErrorLogs = true;
            return
        }
        //creo el arrays de keys para la desagregacion especifica
        const dissagregationKeys = this.dissagregationType.standardDissagregationTypes.map(value => {
            return this.utilsService.getDissagregationKey(value)
        })

        let dissagregationLabels = dissagregationKeys.map(key => {
            return this.utilsService.getDissagregationlabelByKey(key);
        })
        dissagregationLabels = [
            ...dissagregationLabels,
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
                let cellValue = value[this.utilsService.getDissagregationlabelByKey(key)];
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
        const validationMessages = this.utilsService.validateDataImportValues(indicatorValues, this.createdisagregationCatalogue())
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
                            if (key === 'location') {
                                const [provincia, canton] = item['location'].split('-')
                                acc['provincia'] = provincia
                                acc['canton'] = canton

                            } else {
                                acc[key] = item[key];
                            }
                        }
                        return acc;
                    }, {});

                    // Comparo cada valor de las claves en comparisionValues con cada objeto de valuesRowsMap
                    const isMatch = Object.keys(comparisonValues).every(key => {
                        if (key === 'provincia') {
                            return comparisonValues[key] === Col.location['provincia'].description
                        } else if (key === 'canton') {
                            return comparisonValues[key] === Col.location.name
                        } else {
                            return comparisonValues[key] === Col[key].name
                        }
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






    createdisagregationCatalogue() {
        const dissagregationTypes = [
            this.dissagregationTypeRows,
        ]
        const dissagregationOptions = [
            this.dissagregationOptionsRows,
        ];

        const dissagregationCatalogue = dissagregationTypes.map((diss, index) => ({
            [diss.value]: {
                label: diss.label,
                options: dissagregationOptions[index]
            },
        }));
        return dissagregationCatalogue
    }

    generarExcel() {
        let templateName:string
        if(this.indicatorExecution.indicatorType==="GENERAL"){
            templateName="Plantilla_import_Ind_General - "+this.indicatorExecution.period.year
        }else{
            templateName="Plantilla_import_Indicador - "+this.indicatorExecution.indicator.code
        }
        this.templateService.generateExcel(this.createdisagregationCatalogue(),templateName)
            .then(() => console.log('Archivo Excel generado exitosamente.'))
            .catch(error => console.error('Error al generar el archivo:', error));
    }
}
