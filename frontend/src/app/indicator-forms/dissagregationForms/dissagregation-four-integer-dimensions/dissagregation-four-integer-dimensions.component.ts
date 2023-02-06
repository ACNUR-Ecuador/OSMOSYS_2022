import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {DissagregationType, SelectItemWithOrder} from '../../../shared/model/UtilsModel';
import {Canton, IndicatorValue} from '../../../shared/model/OsmosysModel';
import {forkJoin, Observable, of} from 'rxjs';
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

    @Input()
    implementationType: string;
    @Input()
    dissagregationType: DissagregationType;
    @Input()
    values: IndicatorValue[];
    @Input()
    editable: boolean;

    dissagregationGroupsL1Type: DissagregationType;
    dissagregationGroupsL2Type: DissagregationType;
    dissagregationRowsType: DissagregationType;
    dissagregationColumnsType: DissagregationType;


    dissagregationOptionsGroupsL1: SelectItemWithOrder<string | Canton>[];
    dissagregationOptionsGroupsL2: SelectItemWithOrder<string | Canton>[];
    dissagregationOptionsColumns: SelectItemWithOrder<string | Canton>[];
    dissagregationOptionsRows: SelectItemWithOrder<string | Canton>[];

    valuesGroupRowsMap: Map<SelectItemWithOrder<string | Canton>, Map<SelectItemWithOrder<string | Canton>, IndicatorValue[][]>>;
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
        this.showImportButton = this.dissagregationType === DissagregationType.TIPO_POBLACION_LUGAR_EDAD_Y_GENERO && this.implementationType === 'directImplementation';
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
        const dissagregationsTypesRyC: DissagregationType[] =
            this.utilsService.getDissagregationTypesByDissagregationType(this.dissagregationType);
        this.dissagregationGroupsL1Type = dissagregationsTypesRyC[0];
        this.dissagregationGroupsL2Type = dissagregationsTypesRyC[1];
        this.dissagregationRowsType = dissagregationsTypesRyC[2];
        this.dissagregationColumnsType = dissagregationsTypesRyC[3];

        const dissagregationOptionsGroupsL1Obj = this.getOptions(this.dissagregationGroupsL1Type);
        const dissagregationOptionsGroupsL2Obj = this.getOptions(this.dissagregationGroupsL2Type);
        const dissagregationOptionsRowsObj = this.getOptions(this.dissagregationRowsType);
        const dissagregationOptionsColumnsObj = this.getOptions(this.dissagregationColumnsType);
        forkJoin([dissagregationOptionsGroupsL1Obj, dissagregationOptionsGroupsL2Obj,
            dissagregationOptionsRowsObj, dissagregationOptionsColumnsObj])
            .subscribe(results => {
                this.dissagregationOptionsGroupsL1 = results[0] as SelectItemWithOrder<any>[];
                this.dissagregationOptionsGroupsL2 = results[1] as SelectItemWithOrder<any>[];
                this.dissagregationOptionsRows = results[2] as SelectItemWithOrder<any>[];
                this.dissagregationOptionsColumns = results[3] as SelectItemWithOrder<any>[];
                this.valuesGroupRowsMap = new Map<SelectItemWithOrder<any>, Map<SelectItemWithOrder<any>, IndicatorValue[][]>>();
                this.dissagregationOptionsGroupsL1.forEach(itemL1 => {
                    const groupL2Map: Map<SelectItemWithOrder<any>, IndicatorValue[][]> =
                        new Map<SelectItemWithOrder<any>, IndicatorValue[][]>();
                    this.dissagregationOptionsGroupsL2.forEach(itemL2 => {
                        const rows = this.getRowsByGroups(itemL1, itemL2);
                        groupL2Map.set(itemL2, rows);
                    });
                    this.valuesGroupRowsMap.set(itemL1, groupL2Map);
                });
            });
    }

    getOptions(dissagregationType: DissagregationType): Observable<SelectItemWithOrder<any>[]> {
        if (dissagregationType !== DissagregationType.LUGAR) {
            return this.enumsService.getByDissagregationType(dissagregationType);
        } else {
            let locations: SelectItemWithOrder<any>[] = this.values
                .map(value => {
                    return value.location;
                }).map(value => {
                    const selectITem = new SelectItemWithOrder();
                    selectITem.label = value.provincia.description + ' - ' + value.description;
                    selectITem.value = value;
                    return selectITem;
                }).sort((a, b) => a.label.localeCompare(b.label));
            locations = locations.filter((thing, j, arr) => {
                return arr.indexOf(arr.find(t => t.value.id === thing.value.id)) === j;
            });
            locations.forEach((value, index) => value.order = index);
            return of(locations);

        }
    }

    getRowsByGroups(itemL1: SelectItemWithOrder<any>, itemL2: SelectItemWithOrder<any>): Array<Array<IndicatorValue>> {
        let indicatorValues: IndicatorValue[];
        // level 1
        indicatorValues = this.getValuesByDissagregationValues(this.values, this.dissagregationGroupsL1Type, itemL1.value);
        // level 2
        indicatorValues = this.getValuesByDissagregationValues(indicatorValues, this.dissagregationGroupsL2Type, itemL2.value);
        // ordeno y clasifico
        return this.getRowsByValues(this.dissagregationOptionsRows, this.dissagregationOptionsColumns, indicatorValues);
    }

    getValuesByDissagregationValues(values: IndicatorValue[], dissagregationType: DissagregationType, value: string | Canton) {
        return values.filter(indicatorValue => {
            if (dissagregationType === DissagregationType.LUGAR) {
                value = value as Canton;
                const valueOption = this.utilsService.getIndicatorValueByDissagregationType(dissagregationType, indicatorValue) as Canton;
                return valueOption.id === value.id;
            } else {
                const valueOption = this.utilsService.getIndicatorValueByDissagregationType(dissagregationType, indicatorValue) as string;
                return valueOption === value;
            }
        });
    }

    getRowsByValues(dissagregationOptionsRows: SelectItemWithOrder<any>[],
                    dissagregationOptionsColumns: SelectItemWithOrder<any>[],
                    indicatorValues: IndicatorValue[]): IndicatorValue[][] {
        const rows = new Array<Array<IndicatorValue>>();
        if (this.dissagregationRowsType !== DissagregationType.LUGAR) {
            dissagregationOptionsRows.forEach(option => {
                const row: IndicatorValue[] = indicatorValues.filter(indicatorValue => {
                    const value = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationRowsType, indicatorValue);
                    return value === option.value;
                });
                // ordeno las columnas
                this.sortRow(row);
                rows.push(row);
            });
        } else {
            this.dissagregationOptionsRows.forEach(option => {
                const row = indicatorValues.filter(indicatorValue => {
                    const value = this.utilsService
                        .getIndicatorValueByDissagregationType(this.dissagregationRowsType, indicatorValue) as Canton;
                    return value.id === (option.value as Canton).id;
                });
                this.sortRow(row);
                rows.push(row);
            });
        }
        return rows;
    }

    sortRow(row: IndicatorValue[]) {
        row.sort((a, b) => {
            const valueA = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationColumnsType, a);
            const valueB = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationColumnsType, b);
            if (typeof valueA === 'string' || typeof valueB === 'string') {
                const orderA = this.utilsService.getOrderByDissagregationOption(valueA as string,
                    this.dissagregationOptionsColumns);
                const orderB = this.utilsService.getOrderByDissagregationOption(valueB as string,
                    this.dissagregationOptionsColumns);
                return orderA - orderB;
            } else {
                const orderA = (valueA as Canton).code;
                const orderB = (valueB as Canton).code;
                return orderA > orderB ? -1 : 1;
            }
        });
    }

    getTotalIndicatorValuesMap(map: Map<SelectItemWithOrder<string | Canton>, IndicatorValue[][]>) {
        let total = 0;
        map.forEach(value => {
            total += this.utilsService.getTotalIndicatorValuesArrayArray(value);
        });
        return total;
    }

    getTotalIndicator(map: Map<SelectItemWithOrder<string | Canton>, Map<SelectItemWithOrder<string | Canton>, IndicatorValue[][]>>) {
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

        const valuesDissagregation = this.values.filter(value => {
            console.log(value.dissagregationType);
            console.log(this.dissagregationType);
            console.log(value.dissagregationType === this.dissagregationType);
            return value.dissagregationType === this.dissagregationType;

        });
        this.importErroMessage = [];
        this.showImportErroMessage = false;


        console.log(data);
        data.forEach(value => {
            const canton_codigo: string = value['canton_codigo'];
            const tipo_poblacion: string = value['tipo_poblacion'];
            const tipo_genero: string = value['tipo_genero'];
            const tipo_edad: string = value['tipo_edad'];
            const valor: number = value['valor'];

            const indicatorValues = valuesDissagregation.filter(value1 => {
                return value1.location.code === canton_codigo
                    && value1.populationType === tipo_poblacion
                    && value1.genderType === tipo_genero
                    && value1.ageType === tipo_edad
            });
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
                indicatorValues[0].value = valor;
            }
        });
        if (this.showImportErroMessage) {
            console.log(this.importErroMessage);
            console.log(this.importErroMessage.length );
        } else {
            this.showImportDialog = false;
        }
    }

    cancelImportDialog() {
        this.showImportDialog = false;
        this.importForm.reset();
    }
}
