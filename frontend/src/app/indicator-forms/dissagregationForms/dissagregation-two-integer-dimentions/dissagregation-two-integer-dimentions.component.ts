import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {EnumsType} from '../../../shared/model/UtilsModel';
import {EnumWeb, IndicatorValue, StandardDissagregationOption} from '../../../shared/model/OsmosysModel';
import {EnumsService} from '../../../services/enums.service';
import {UtilsService} from '../../../services/utils.service';

@Component({
    selector: 'app-dissagregation-two-integer-dimentions',
    templateUrl: './dissagregation-two-integer-dimentions.component.html',
    styleUrls: ['./dissagregation-two-integer-dimentions.component.scss']
})
export class DissagregationTwoIntegerDimentionsComponent implements OnInit, OnChanges {
    @Input()
    dissagregationType: EnumWeb;
    @Input()
    values: IndicatorValue[];
    @Input()
    editable: boolean;

    dissagregationOptionsColumns: StandardDissagregationOption[];
    dissagregationOptionsRows: StandardDissagregationOption[];

    /*enumTypeColumns: EnumWeb;
    enumTypeRows: EnumWeb;*/

    dissagregationColumnsType: EnumWeb;
    dissagregationRowsType: EnumWeb;

    rows = new Array<Array<IndicatorValue>>();


    constructor(
        public enumsService: EnumsService,
        //private messageService: MessageService,
        public utilsService: UtilsService,
    ) {
    }

    ngOnInit(): void {
        this.processDissagregationValues();

    }

    processDissagregationValues() {

        // ontengo los tipos de desagregación horizontal y vertical
        const dissagregationsTypesRyCEnum: EnumWeb[] =
            this.dissagregationType.standardDissagregationTypes
                .map(value => {
                    return this.enumsService.resolveEnumWeb(EnumsType.DissagregationType, value)
                });

        this.dissagregationRowsType = dissagregationsTypesRyCEnum[0];
        this.dissagregationColumnsType = dissagregationsTypesRyCEnum[1];

        // obtiene las opciones
        this.dissagregationOptionsRows = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationRowsType);
        this.dissagregationOptionsColumns = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationColumnsType);
        this.rows = this.utilsService.getRowsAndColumnsFromValues(this.dissagregationColumnsType,
            this.dissagregationRowsType,
            this.dissagregationOptionsRows,
            this.dissagregationOptionsColumns,
            this.values);

    }


    ngOnChanges() {
        this.processDissagregationValues();
    }
}


