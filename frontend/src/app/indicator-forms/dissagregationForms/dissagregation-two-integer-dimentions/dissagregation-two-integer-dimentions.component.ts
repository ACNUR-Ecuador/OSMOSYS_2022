import {Component, Input, OnInit} from '@angular/core';
import {DissagregationType, EnumsType, SelectItemWithOrder} from '../../../shared/model/UtilsModel';
import {IndicatorValue} from '../../../shared/model/OsmosysModel';
import {EnumsService} from '../../../shared/services/enums.service';
import {MessageService} from 'primeng/api';
import {UtilsService} from '../../../shared/services/utils.service';
import {forkJoin} from 'rxjs';

@Component({
    selector: 'app-dissagregation-two-integer-dimentions',
    templateUrl: './dissagregation-two-integer-dimentions.component.html',
    styleUrls: ['./dissagregation-two-integer-dimentions.component.scss']
})
export class DissagregationTwoIntegerDimentionsComponent implements OnInit {
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
    valuesRows: IndicatorValue[];

    constructor(
        public enumsService: EnumsService,
        private messageService: MessageService,
        private utilsService: UtilsService,
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

    }

    createTwoDimentionsGrid() {
        // ordeno
        // clasifico por cada uno de las opciones

        this.rows = new Array<Array<IndicatorValue>>();
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
        this.valuesRows = [];

        // ordeno por
        console.log('this.rows');
        console.log(this.rows);
        // tslint:disable-next-line:prefer-for-of
        for (let i = 0; i < this.rows.length; i++) {
            console.log(i);
            console.log(this.rows[i]);
            let div = '';
            this.rows[i].forEach(value => {
                div = div + '-' + value.diversityType;
            });
            console.log(div);
        }

    }

}
