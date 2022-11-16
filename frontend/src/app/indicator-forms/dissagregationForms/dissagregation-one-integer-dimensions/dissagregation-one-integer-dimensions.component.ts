import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {DissagregationType, EnumsType, SelectItemWithOrder} from '../../../shared/model/UtilsModel';
import {Canton, IndicatorValue} from '../../../shared/model/OsmosysModel';
import {EnumsService} from '../../../services/enums.service';
import {MessageService} from 'primeng/api';
import {UtilsService} from '../../../services/utils.service';

@Component({
  selector: 'app-dissagregation-one-integer-dimensions',
  templateUrl: './dissagregation-one-integer-dimensions.component.html',
  styleUrls: ['./dissagregation-one-integer-dimensions.component.scss']
})
export class DissagregationOneIntegerDimensionsComponent implements OnInit, OnChanges {
    @Input()
    dissagregationType: DissagregationType;
    @Input()
    values: IndicatorValue[];
    @Input()
    editable: boolean;

    dissagregationOptionsRows: SelectItemWithOrder<any>[];
    enumTypeRows: EnumsType;
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
        this.rows = [];
        const enumTypesRyC: EnumsType[] =
            this.utilsService.getEnymTypesByDissagregationTypes(this.dissagregationType);
        this.enumTypeRows = enumTypesRyC[0];
        const dissagregationsTypesRyC: DissagregationType[] =
            this.utilsService.getDissagregationTypesByDissagregationType(this.dissagregationType);
        this.dissagregationTypeRows = dissagregationsTypesRyC[0];
        if (this.dissagregationTypeRows !== DissagregationType.LUGAR) {
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
            this.dissagregationOptionsRows.forEach((value, index) => value.order = index);
            this.createTwoDimentionsGrid();
        }
    }

    createTwoDimentionsGrid() {
        // ordeno
        // clasifico por cada uno de las opciones

        this.rows = new Array<Array<IndicatorValue>>();
        if (this.dissagregationTypeRows !== DissagregationType.LUGAR) {
            this.dissagregationOptionsRows.forEach(dissagregationOption => {
                const row = this.values.filter(value => {
                    const valueOption = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationTypeRows, value);
                    return valueOption === dissagregationOption.value;

                });
                this.rows.push(row);
            });
        } else {
            this.dissagregationOptionsRows.forEach(dissagregationOption => {
                const row = this.values.filter(value => {
                    const valueOption =
                        (this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationTypeRows, value)) as Canton;
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
