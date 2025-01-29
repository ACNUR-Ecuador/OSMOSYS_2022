import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { EnumWeb, IndicatorExecution, IndicatorValue, StandardDissagregationOption } from "../../../shared/model/OsmosysModel";
import { UtilsService } from "../../../services/utils.service";
import { EnumsService } from "../../../services/enums.service";
import { EnumsType } from "../../../shared/model/UtilsModel";
import { TemplateGeneratorService } from 'src/app/services/template-generator.service';
import * as XLSX from "xlsx";
import { WorkBook } from "xlsx";
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-dissagregation-five-integer-dimensions',
    templateUrl: './dissagregation-five-integer-dimensions.component.html',
    styleUrls: ['./dissagregation-five-integer-dimensions.component.scss']
})
export class DissagregationFiveIntegerDimensionsComponent implements OnInit {

    @Input()
    dissagregationType: EnumWeb;
    @Input()
    values: IndicatorValue[];
    @Input()
    editable: boolean;
    @Input()
    indicatorExecution: IndicatorExecution;

    dissagregationGroupsL1Type: EnumWeb;
    dissagregationGroupsL2Type: EnumWeb;
    dissagregationGroupsL3Type: EnumWeb;
    dissagregationRowsType: EnumWeb;
    dissagregationColumnsType: EnumWeb;


    dissagregationOptionsGroupsL1: StandardDissagregationOption[];
    dissagregationOptionsGroupsL2: StandardDissagregationOption[];
    dissagregationOptionsGroupsL3: StandardDissagregationOption[];
    dissagregationOptionsColumns: StandardDissagregationOption[];
    dissagregationOptionsRows: StandardDissagregationOption[];

    valuesGroupRowsMap: Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>>;
    importForm: FormGroup;
    showImportDialog: boolean = false;
    sheetOptions: string[];
    importErroMessage: string[];
    showImportErrorLogs: boolean;
    currentTimestamp: string;
    validateTotalControl: boolean=true

    constructor(
        public utilsService: UtilsService,
        public enumsService: EnumsService,
        public templateService: TemplateGeneratorService,
        private fb: FormBuilder
    ) {
    }

    ngOnInit(): void {
        if(this.dissagregationType.value.lastIndexOf("DIVERSIDAD")>=0){
            this.validateTotalControl=false;
        }
        this.processDissagregationValues();
        this.importForm = this.fb.group({
            fileName: new FormControl('', [Validators.required]),
            file: new FormControl(''),
            sheet: new FormControl('', [Validators.required]),
            workbook: new FormControl('', [Validators.required]),
        });

    }

    ngOnChanges(changes: SimpleChanges): void {
        this.processDissagregationValues();
    }

    processDissagregationValues() {
        const dissagregationsTypesRyCEnum: EnumWeb[] = this.dissagregationType.standardDissagregationTypes
            .map(value => {
                return this.enumsService.resolveEnumWeb(EnumsType.DissagregationType, value)
            });

        this.dissagregationGroupsL1Type = dissagregationsTypesRyCEnum[0];
        this.dissagregationGroupsL2Type = dissagregationsTypesRyCEnum[1];
        this.dissagregationGroupsL3Type = dissagregationsTypesRyCEnum[2];
        this.dissagregationRowsType = dissagregationsTypesRyCEnum[3];
        this.dissagregationColumnsType = dissagregationsTypesRyCEnum[4];

        // obtiene las opciones
        this.dissagregationOptionsGroupsL1 = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationGroupsL1Type);
        this.dissagregationOptionsGroupsL2 = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationGroupsL2Type);
        this.dissagregationOptionsGroupsL3 = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationGroupsL3Type);
        this.dissagregationOptionsRows = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationRowsType);
        this.dissagregationOptionsColumns = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationColumnsType);


        // hace un mapa
        this.valuesGroupRowsMap = new Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>>();
        // para el nivel 1
        this.dissagregationOptionsGroupsL1.forEach(optionL1 => {
            // por cada nivel 1
            const groupL2Map: Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>> = new Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>();

            this.dissagregationOptionsGroupsL2.forEach(optionL2 => {
                const groupL3Map: Map<StandardDissagregationOption, IndicatorValue[][]> = new Map<StandardDissagregationOption, IndicatorValue[][]>();

                this.dissagregationOptionsGroupsL3.forEach(optionL3 => {
                    // // filtro para 2 2 primeros niveles
                    let rows = this.getByL1AndL2AndL3Options(
                        optionL1, this.dissagregationGroupsL1Type,
                        optionL2, this.dissagregationGroupsL2Type,
                        optionL3, this.dissagregationGroupsL3Type,
                        this.values);
                    const rowsT = this.utilsService.getRowsAndColumnsFromValues(this.dissagregationColumnsType,
                        this.dissagregationRowsType,
                        this.dissagregationOptionsRows,
                        this.dissagregationOptionsColumns,
                        rows);


                    groupL3Map.set(optionL3, rowsT);
                });

                groupL2Map.set(optionL2, groupL3Map);
            });
            this.valuesGroupRowsMap.set(optionL1, groupL2Map);
        });

    }

    getByL1AndL2AndL3Options(
        optionL1: StandardDissagregationOption, dissagregationGroupsL1Type: EnumWeb,
        optionL2: StandardDissagregationOption, dissagregationGroupsL2Type: EnumWeb,
        optionL3: StandardDissagregationOption, dissagregationGroupsL3Type: EnumWeb,
        values: IndicatorValue[]): IndicatorValue[] {
        return values.filter(value => {

            const valueOption1 = this.utilsService.getIndicatorValueByDissagregationType(dissagregationGroupsL1Type, value);
            const valueOption2 = this.utilsService.getIndicatorValueByDissagregationType(dissagregationGroupsL2Type, value);
            const valueOption3 = this.utilsService.getIndicatorValueByDissagregationType(dissagregationGroupsL3Type, value);
            return valueOption1.id === optionL1.id && valueOption2.id === optionL2.id && valueOption3.id === optionL3.id;
        });
    }

    getTotalIndicatorValuesMapL1(map: Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>) {
        let total = 0;
        map.forEach(valueMap => {
            valueMap.forEach(value => {
                total += this.utilsService.getTotalIndicatorValuesArrayArray(value);
            });
        });
        return total;
    }

    getTotalIndicatorValuesMapL2(map: Map<StandardDissagregationOption, IndicatorValue[][]>) {
        let total = 0;
        map.forEach(value => {
            total += this.utilsService.getTotalIndicatorValuesArrayArray(value);

        });
        return total;
    }


    getTotalIndicator(map: Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>>) {
        if (!map) {
            return null;
        }
        let total = 0;
        map.forEach(valueMap => {
            valueMap.forEach(valueMap2 => {
                valueMap2.forEach(value => {
                    total += this.utilsService.getTotalIndicatorValuesArrayArray(value);
                });
            });
        });
        return total;
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
        this.valuesGroupRowsMap.forEach(L1 => {
            L1.forEach(L2 => {
                L2.forEach(L3 => {
                    L3.forEach(Row => {
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
            this.dissagregationGroupsL1Type,
            this.dissagregationGroupsL2Type,
            this.dissagregationGroupsL3Type,
            this.dissagregationRowsType,
            this.dissagregationColumnsType,
        ]
        const dissagregationOptions = [
            this.dissagregationOptionsGroupsL1,
            this.dissagregationOptionsGroupsL2,
            this.dissagregationOptionsGroupsL3,
            this.dissagregationOptionsRows,
            this.dissagregationOptionsColumns,
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
