import {Component, Input, OnInit} from '@angular/core';
import {DissagregationType, EnumsType, SelectItemWithOrder} from '../../../shared/model/UtilsModel';
import {IndicatorValue} from '../../../shared/model/OsmosysModel';
import {EnumsService} from '../../../shared/services/enums.service';
import {MessageService} from 'primeng/api';
import {UtilsService} from '../../../shared/services/utils.service';

@Component({
    selector: 'app-dissagregation-one-integer-dimentions',
    templateUrl: './dissagregation-one-integer-dimentions.component.html',
    styleUrls: ['./dissagregation-one-integer-dimentions.component.scss']
})
export class DissagregationOneIntegerDimentionsComponent implements OnInit {
    @Input()
    dissagregationType: DissagregationType;
    @Input()
    values: IndicatorValue[];

    dissagregationOptionsRows: SelectItemWithOrder<any>[];
    enumTypeRows: EnumsType;
    dissagregationTypeRows: DissagregationType;
    rows = new Array<Array<IndicatorValue>>();

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
        const dissagregationsTypesRyC: DissagregationType[] =
            this.utilsService.getDissagregationTypesByDissagregationType(this.dissagregationType);
        this.dissagregationTypeRows = dissagregationsTypesRyC[0];
        this.enumsService.getByType(this.enumTypeRows)
            .subscribe(value => {
                this.dissagregationOptionsRows = value;
                // ordeno los rows
                this.dissagregationOptionsRows = this.dissagregationOptionsRows
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
            this.rows.push(row);
        });
    }

}
