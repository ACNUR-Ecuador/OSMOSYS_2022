import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {DissagregationType, EnumsType, SelectItemWithOrder} from '../../../shared/model/UtilsModel';
import {Canton, IndicatorValue} from '../../../shared/model/OsmosysModel';
import {EnumsService} from '../../../shared/services/enums.service';
import {MessageService} from 'primeng/api';
import {UtilsService} from '../../../shared/services/utils.service';
import {forkJoin} from 'rxjs';

@Component({
    selector: 'app-dissagregation-two-integer-dimentions',
    templateUrl: './dissagregation-two-integer-dimentions.component.html',
    styleUrls: ['./dissagregation-two-integer-dimentions.component.scss']
})
export class DissagregationTwoIntegerDimentionsComponent implements OnInit, OnChanges {
    @Input()
    dissagregationType: DissagregationType;
    @Input()
    values: IndicatorValue[];

    dissagregationOptionsColumns: SelectItemWithOrder<any>[];
    dissagregationOptionsRows: SelectItemWithOrder<any>[];
    enumTypeColumns: EnumsType;
    enumTypeRows: EnumsType;
    dissagregationTypeColumns: DissagregationType;
    dissagregationTypeRows: DissagregationType;
    rows = new Array<Array<IndicatorValue>>();


    constructor(
        public enumsService: EnumsService,
        private messageService: MessageService,
        public utilsService: UtilsService,
    ) {
    }

    ngOnInit(): void {
        this.processDissagregationValues();

    }

    processDissagregationValues() {
        // ontengo los tipos de desagregación horizontal y vertical
        const enumTypesRyC: EnumsType[] =
            this.utilsService.getEnymTypesByDissagregationTypes(this.dissagregationType);
        this.enumTypeRows = enumTypesRyC[0];
        this.enumTypeColumns = enumTypesRyC[1];
        const dissagregationsTypesRyC: DissagregationType[] =
            this.utilsService.getDissagregationTypesByDissagregationType(this.dissagregationType);
        this.dissagregationTypeRows = dissagregationsTypesRyC[0];
        this.dissagregationTypeColumns = dissagregationsTypesRyC[1];
        const dissagregationOptionsRowsObj = this.enumsService.getByType(this.enumTypeRows);
        const dissagregationOptionsColumnsObj = this.enumsService.getByType(this.enumTypeColumns);
        if (this.dissagregationTypeRows !== DissagregationType.LUGAR) {
            forkJoin([dissagregationOptionsRowsObj, dissagregationOptionsColumnsObj])
                .subscribe(results => {
                    this.dissagregationOptionsRows = results[0];
                    this.dissagregationOptionsColumns = results[1];
                    // ordeno los rows
                    this.dissagregationOptionsRows = this.dissagregationOptionsRows
                        .sort((a, b) => {
                            return a.order - b.order;
                        });
                    this.dissagregationOptionsColumns = this.dissagregationOptionsColumns
                        .sort((a, b) => {
                            return a.order - b.order;
                        });
                    this.createTwoDimentionsGrid();
                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las opciones',
                        detail: error.error.message,
                        life: 3000
                    });
                });
        } else {
            this.dissagregationOptionsRows = this.values
                .map(value => {
                    return value.location;
                }).map(value => {
                    const selectITem = new SelectItemWithOrder();
                    selectITem.label = value.provincia.description + ' - ' + value.description;
                    selectITem.value = value;
                    return selectITem;
                }).sort((a, b) => a.label.localeCompare(b.label));
            this.dissagregationOptionsRows = this.dissagregationOptionsRows.filter((thing, j, arr) => {
                return arr.indexOf(arr.find(t => t.value.id === thing.value.id)) === j;
            });
            this.dissagregationOptionsRows.forEach((value, index) => value.order = index);

            dissagregationOptionsColumnsObj.subscribe(value => {
                this.dissagregationOptionsColumns = value;
                this.createTwoDimentionsGrid();
            });
        }
    }

    createTwoDimentionsGrid() {
        // ordeno
        // clasifico por cada uno de las opciones

        this.rows = new Array<Array<IndicatorValue>>();
        if (this.dissagregationTypeRows !== DissagregationType.LUGAR) {
            this.dissagregationOptionsRows.forEach(dissagregationOption => {
                const row = this.values.filter(value => {
                    const valueOption = this.utilsService.getOptionValueByDissagregationType(this.dissagregationTypeRows, value);
                    return valueOption === dissagregationOption.value;

                });
                // ordeno las columnas
                row.sort((a, b) => {
                    const optionA = this.utilsService.getOptionValueByDissagregationType(this.dissagregationTypeColumns, a);
                    const optionB = this.utilsService.getOptionValueByDissagregationType(this.dissagregationTypeColumns, b);
                    if (typeof optionA === 'string' || optionA instanceof String) {
                        // @ts-ignore
                        const orderA = this.utilsService.getOrderByDissagregationOption(optionA, this.dissagregationOptionsColumns);
                        // @ts-ignore
                        const orderB = this.utilsService.getOrderByDissagregationOption(optionB, this.dissagregationOptionsColumns);
                        return orderA - orderB;
                    } else {
                        // todo canton
                        return 1;
                    }

                });
                this.rows.push(row);
            });
        } else {
            this.dissagregationOptionsRows.forEach(dissagregationOption => {
                const row = this.values.filter(value => {
                    const valueOption =
                        (this.utilsService.getOptionValueByDissagregationType(this.dissagregationTypeRows, value)) as Canton;
                    return valueOption.id === dissagregationOption.value.id;

                });
                this.rows.push(row);
            });
        }
    }

    ngOnChanges() {
        this.processDissagregationValues();
    }
}


