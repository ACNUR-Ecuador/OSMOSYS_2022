import {Component, Input, OnInit} from '@angular/core';
import {DissagregationType, EnumsType, SelectItemWithOrder} from '../../../shared/model/UtilsModel';
import {Canton, IndicatorValue, Month} from '../../../shared/model/OsmosysModel';
import {EnumsService} from '../../../shared/services/enums.service';
import {MessageService} from 'primeng/api';
import {UtilsService} from '../../../shared/services/utils.service';

@Component({
    selector: 'app-dissagregation-no-dissagregation-integer',
    templateUrl: './dissagregation-no-dissagregation-integer.component.html',
    styleUrls: ['./dissagregation-no-dissagregation-integer.component.scss']
})
export class DissagregationNoDissagregationIntegerComponent implements OnInit {
    @Input()
    dissagregationType: DissagregationType;
    @Input()
    values: IndicatorValue[];
    @Input()
    month: Month;

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
        this.rows.push(this.values);

    }
}
