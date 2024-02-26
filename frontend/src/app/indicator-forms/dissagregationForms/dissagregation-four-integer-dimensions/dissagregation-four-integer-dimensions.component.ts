import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {EnumsType} from '../../../shared/model/UtilsModel';
import {EnumWeb, IndicatorValue, StandardDissagregationOption} from '../../../shared/model/OsmosysModel';
import {UtilsService} from '../../../services/utils.service';
import {EnumsService} from '../../../services/enums.service';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import * as XLSX from "xlsx";
import {WorkBook} from "xlsx";

@Component({
    selector: 'app-dissagregation-four-integer-dimensions',
    templateUrl: './dissagregation-four-integer-dimensions.component.html',
    styleUrls: ['./dissagregation-four-integer-dimensions.component.scss']
})
export class DissagregationFourIntegerDimensionsComponent implements OnInit, OnChanges {


    @Input() // todo quitar?
    implementationType: string;
    @Input()
    dissagregationType: EnumWeb;
    @Input()
    values: IndicatorValue[];
    @Input()
    editable: boolean;

    dissagregationGroupsL1Type: EnumWeb;
    dissagregationGroupsL2Type: EnumWeb;
    dissagregationRowsType: EnumWeb;
    dissagregationColumnsType: EnumWeb;


    dissagregationOptionsGroupsL1: StandardDissagregationOption[];
    dissagregationOptionsGroupsL2: StandardDissagregationOption[];
    dissagregationOptionsColumns: StandardDissagregationOption[];
    dissagregationOptionsRows: StandardDissagregationOption[];

    valuesGroupRowsMap: Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>;
    importForm: FormGroup;
    showImportDialog: boolean = false;
    showImportButton: boolean = false;
    sheetOptions: string[];
    importErroMessage: string[];
    showImportErroMessage: boolean;

    // private workbook: XLSX.WorkBook;


    constructor(
        public utilsService: UtilsService,
        public enumsService: EnumsService,
        private fb: FormBuilder
    ) {
    }

    ngOnInit(): void {
        this.processDissagregationValues();
        // todo 2024 importación de datos
        this.showImportButton = false;//this.dissagregationType === DissagregationType.TIPO_POBLACION_LUGAR_EDAD_Y_GENERO && this.implementationType === 'directImplementation';
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
        this.dissagregationRowsType = dissagregationsTypesRyCEnum[2];
        this.dissagregationColumnsType = dissagregationsTypesRyCEnum[3];

        // obtiene las opciones
        this.dissagregationOptionsGroupsL1 = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationGroupsL1Type);
        this.dissagregationOptionsGroupsL2 = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationGroupsL2Type);
        this.dissagregationOptionsRows = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationRowsType);
        this.dissagregationOptionsColumns = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationColumnsType);


        // hace un mapa
        this.valuesGroupRowsMap = new Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>();
        // para el nivel 1
        this.dissagregationOptionsGroupsL1.forEach(optionL1 => {
            // por cada nivel 1
            const groupL2Map: Map<StandardDissagregationOption, IndicatorValue[][]> = new Map<StandardDissagregationOption, IndicatorValue[][]>();

            this.dissagregationOptionsGroupsL2.forEach(optionL2 => {
                // // filtro para 2 2 primeros niveles
                let rows = this.getByL1AndL2Options(optionL1, this.dissagregationGroupsL1Type, optionL2, this.dissagregationGroupsL2Type, this.values);
                const rowsT = this.utilsService.getRowsAndColumnsFromValues(this.dissagregationColumnsType,
                    this.dissagregationRowsType,
                    this.dissagregationOptionsRows,
                    this.dissagregationOptionsColumns,
                    rows);


                groupL2Map.set(optionL2, rowsT);
            });
            this.valuesGroupRowsMap.set(optionL1, groupL2Map);
        });

    }

    getByL1AndL2Options(optionL1: StandardDissagregationOption,dissagregationGroupsL1Type: EnumWeb,  optionL2: StandardDissagregationOption, dissagregationGroupsL2Type: EnumWeb,values:IndicatorValue[]  ):IndicatorValue[]{
        return values.filter(value => {

            const valueOption1 = this.utilsService.getIndicatorValueByDissagregationType(dissagregationGroupsL1Type, value);
            const valueOption2 = this.utilsService.getIndicatorValueByDissagregationType(dissagregationGroupsL2Type, value);
            return valueOption1.id === optionL1.id && valueOption2.id === optionL2.id;
        });
    }


    getTotalIndicatorValuesMap(map: Map<StandardDissagregationOption, IndicatorValue[][]>) {
        let total = 0;
        map.forEach(value => {
            total += this.utilsService.getTotalIndicatorValuesArrayArray(value);
        });
        return total;
    }


    getTotalIndicator(map: Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>) {
        if (!map) {
            return null;
        }
        let total = 0;
        map.forEach(valueMap => {
            valueMap.forEach(value => {
                total += this.utilsService.getTotalIndicatorValuesArrayArray(value);
            });
        });
        return total;
    }


    fileUploader(event: any) {
        const file = event.files[0];
        this.importForm.get('fileName').setValue(file.name);
        this.importForm.get('fileName').markAsTouched();
        console.log(file);
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
            workbook = XLSX.read(bstr, {type: "binary"});
            this.importForm.get('workbook').patchValue(workbook);
            this.sheetOptions = [];
            workbook.SheetNames.forEach(value => {
                console.log(value);
                this.sheetOptions.push(value);
            });
            console.log(this.sheetOptions);
            this.importForm.get('sheet').patchValue(null);
            this.importForm.get('sheet').markAsTouched();
        };

    }


    showImportDialogF() {
        this.showImportDialog = true;
    }

    importFile() {
        let workbook = this.importForm.get('workbook').value as XLSX.WorkBook;
        workbook.SheetNames.forEach(value => {
            console.log(value);
        });
        let spreedsheetname = this.importForm.get('sheet').value;
        console.log(spreedsheetname);
        const ws: XLSX.WorkSheet = workbook.Sheets[spreedsheetname];
        const data = XLSX.utils.sheet_to_json(ws);

        this.importErroMessage = [];
        this.showImportErroMessage = false;


        console.log(data);
        data.forEach(value => {
            // const canton_codigo: string = value['canton_codigo'];
            const tipo_poblacion: string = value['tipo_poblacion'];
            const tipo_genero: string = value['tipo_genero'];
            const tipo_edad: string = value['tipo_edad'];
            const valor: number = value['valor'];

            // todo 2024
            const indicatorValues = [];
            /*const indicatorValues = valuesDissagregation.filter(value1 => {
                return value1.location.code === canton_codigo
                    && value1.populationType === tipo_poblacion
                    && value1.genderType === tipo_genero
                    && value1.ageType === tipo_edad
            });*/
            if (indicatorValues.length < 1) {

                this.importErroMessage.push('Error en la fila con los siguientes datos ' +
                    ' provincia: ' + value['provincia'] +
                    ' canton: ' + value['canton'] +
                    ' tipo de población: ' + tipo_poblacion +
                    ' tipo de género: ' + tipo_genero +
                    ' tipo de edad: ' + tipo_edad +
                    ' tipo de valor: ' + valor
                );
                this.showImportErroMessage = true;
            } else {
                if (valor) {
                    indicatorValues[0].value = valor;
                } else {
                    indicatorValues[0].value = 0;
                }

            }
        });
        if (this.showImportErroMessage) {
            console.log(this.importErroMessage);
            console.log(this.importErroMessage.length);
        } else {
            this.showImportDialog = false;
        }
    }


    cancelImportDialog() {
        this.showImportDialog = false;
        this.importForm.reset();
    }
}
