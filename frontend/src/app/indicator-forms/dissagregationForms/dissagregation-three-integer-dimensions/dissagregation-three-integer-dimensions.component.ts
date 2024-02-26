import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {EnumsType} from '../../../shared/model/UtilsModel';
import {EnumWeb, IndicatorValue, StandardDissagregationOption} from '../../../shared/model/OsmosysModel';
import {UtilsService} from '../../../services/utils.service';
import {EnumsService} from '../../../services/enums.service';

@Component({
    selector: 'app-dissagregation-three-integer-dimensions',
    templateUrl: './dissagregation-three-integer-dimensions.component.html',
    styleUrls: ['./dissagregation-three-integer-dimensions.component.scss']
})
export class DissagregationThreeIntegerDimensionsComponent implements OnInit, OnChanges {

    @Input()
    dissagregationType: EnumWeb;
    @Input()
    values: IndicatorValue[];
    @Input()
    editable: boolean;

    dissagregationGroupsType: EnumWeb;
    dissagregationRowsType: EnumWeb;
    dissagregationColumnsType: EnumWeb;


    dissagregationOptionsGroups: StandardDissagregationOption[];
    dissagregationOptionsColumns: StandardDissagregationOption[];
    dissagregationOptionsRows: StandardDissagregationOption[];

    valuesGroupRowsMap: Map<StandardDissagregationOption, IndicatorValue[][]>;

    constructor(
        public utilsService: UtilsService,
        public enumsService: EnumsService,
    ) {
    }

    ngOnInit(): void {
        this.processDissagregationValues();
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.processDissagregationValues();
    }
    processDissagregationValues() {
        const dissagregationsTypesRyCEnum: EnumWeb[] = this.dissagregationType.standardDissagregationTypes
            .map(value => {
                return this.enumsService.resolveEnumWeb(EnumsType.DissagregationType, value)
            });
        this.dissagregationGroupsType = dissagregationsTypesRyCEnum[0];
        this.dissagregationRowsType = dissagregationsTypesRyCEnum[1];
        this.dissagregationColumnsType = dissagregationsTypesRyCEnum[2];


        // obtiene las opciones

        this.dissagregationOptionsGroups = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationGroupsType);
        this.dissagregationOptionsRows = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationRowsType);
        this.dissagregationOptionsColumns = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationColumnsType);

        // hace un mapa
        this.valuesGroupRowsMap = new Map<StandardDissagregationOption, IndicatorValue[][]>();

        // para el nivel 1
        this.dissagregationOptionsGroups.forEach(optionL1 => {
            // por cada nivel 1

            let rows = this.getByL1Options(optionL1, this.dissagregationGroupsType, this.values);
            const rowsT = this.utilsService.getRowsAndColumnsFromValues(
                this.dissagregationColumnsType,
                this.dissagregationRowsType,
                this.dissagregationOptionsRows,
                this.dissagregationOptionsColumns,
                rows);

            this.valuesGroupRowsMap.set(optionL1, rowsT);
        });

    }

    getByL1Options(optionL1: StandardDissagregationOption,dissagregationGroupsL1Type: EnumWeb, values:IndicatorValue[]  ):IndicatorValue[]{
        return values.filter(value => {

            const valueOption1 = this.utilsService.getIndicatorValueByDissagregationType(dissagregationGroupsL1Type, value);
            return valueOption1.id === optionL1.id;
        });
    }


    getTotalIndicator(map: Map<StandardDissagregationOption, IndicatorValue[][]>) {
        if (!map) {
            return null;
        }
        let total = 0;
        map.forEach(value => {
            total += this.utilsService.getTotalIndicatorValuesArrayArray(value);
        });
        return total;
    }

}
