import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {EnumsType} from '../../../shared/model/UtilsModel';
import {EnumWeb, IndicatorValue, StandardDissagregationOption} from '../../../shared/model/OsmosysModel';
import {EnumsService} from '../../../services/enums.service';
import {UtilsService} from '../../../services/utils.service';

@Component({
  selector: 'app-dissagregation-one-integer-dimensions',
  templateUrl: './dissagregation-one-integer-dimensions.component.html',
  styleUrls: ['./dissagregation-one-integer-dimensions.component.scss']
})
export class DissagregationOneIntegerDimensionsComponent implements OnInit, OnChanges {
    @Input()
    dissagregationType: EnumWeb;
    @Input()
    values: IndicatorValue[];
    @Input()
    editable: boolean;

    dissagregationOptionsRows: StandardDissagregationOption[];
    //enumTypeRows: EnumWeb;
    dissagregationTypeRows: EnumWeb;
    rows = new Array<Array<IndicatorValue>>();


    constructor(
        public enumsService: EnumsService,
        public utilsService: UtilsService,
    ) {
    }

    ngOnInit(): void {
        this.processDissagregationValues();

    }

    processDissagregationValues() {
        // ontengo los tipos de desagregación horizontal y vertical
        this.rows = [];
        const dissagregationsTypesRyCEnum: EnumWeb[] = this.dissagregationType.standardDissagregationTypes
            .map(value => {
                return this.enumsService.resolveEnumWeb(EnumsType.DissagregationType, value)
            });
        this.dissagregationTypeRows = dissagregationsTypesRyCEnum[0];

        // obtiene las opciones
        this.dissagregationOptionsRows = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationTypeRows);

        this.dissagregationOptionsRows.forEach(dissagregationOption => {
            const row = this.values.filter(value => {
                const valueOption =
                    (this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationTypeRows, value));
                return valueOption.id === dissagregationOption.id;

            });
            this.rows.push(row);
        });

    }

    ngOnChanges() {
        this.processDissagregationValues();
    }
}
