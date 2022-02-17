import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {DissagregationType} from '../../../shared/model/UtilsModel';
import {IndicatorValue, Month} from '../../../shared/model/OsmosysModel';
import {EnumsService} from '../../../shared/services/enums.service';

@Component({
    selector: 'app-dissagregation-no-dissagregation-integer',
    templateUrl: './dissagregation-no-dissagregation-integer.component.html',
    styleUrls: ['./dissagregation-no-dissagregation-integer.component.scss']
})
export class DissagregationNoDissagregationIntegerComponent implements OnInit, OnChanges {
    @Input()
    dissagregationType: DissagregationType;
    @Input()
    values: IndicatorValue[];
    @Input()
    month: Month;

    rows = new Array<Array<IndicatorValue>>();

    constructor(
        public enumsService: EnumsService
    ) {
    }

    ngOnInit(): void {
        this.processDissagregationValues();

    }

    processDissagregationValues() {
        this.rows.push(this.values);

    }

    ngOnChanges() {
        this.processDissagregationValues();
    }
}
