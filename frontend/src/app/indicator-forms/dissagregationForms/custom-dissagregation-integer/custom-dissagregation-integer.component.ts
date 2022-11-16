import {Component, Input, OnInit} from '@angular/core';
import {SelectItemWithOrder} from '../../../shared/model/UtilsModel';
import {
    CustomDissagregation, IndicatorValueCustomDissagregationWeb
} from '../../../shared/model/OsmosysModel';
import {UtilsService} from '../../../services/utils.service';

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

    constructor(
        public utilsService: UtilsService) {
    }

    ngOnInit(): void {
        this.processDissagregationValues();
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

}
