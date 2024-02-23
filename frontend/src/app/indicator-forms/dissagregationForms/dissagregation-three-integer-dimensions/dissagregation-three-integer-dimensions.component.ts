import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {EnumsType, SelectItemWithOrder} from '../../../shared/model/UtilsModel';
import {Canton, EnumWeb, IndicatorValue, StandardDissagregationOption} from '../../../shared/model/OsmosysModel';
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
        this.dissagregationOptionsGroups.forEach(itemL1 => {
            // por cada nivel 1

            const rows = this.getRowsByGroups(itemL1);


            this.valuesGroupRowsMap.set(itemL1, rows);
        });

    }

    getRowsByGroups(itemL1: StandardDissagregationOption): Array<Array<IndicatorValue>> {
        let indicatorValues: IndicatorValue[];

        // level 1
        indicatorValues = this.getValuesByDissagregationValues(this.values, this.dissagregationGroupsType, itemL1);

        return this.getRowsByValues( indicatorValues);
    }

    getValuesByDissagregationValues(values: IndicatorValue[], dissagregationType: EnumWeb, value: StandardDissagregationOption | Canton) {
        return values.filter(indicatorValue => {
            const valueOption = this.utilsService.getIndicatorValueByDissagregationType(dissagregationType, indicatorValue);
            return valueOption.id === value.id;

        });
    }

    getRowsByValues(indicatorValues: IndicatorValue[]): IndicatorValue[][] {
        const rows = new Array<Array<IndicatorValue>>();

        this.dissagregationOptionsRows.forEach(option => {
            const row = indicatorValues.filter(indicatorValue => {
                const value = this.utilsService
                    .getIndicatorValueByDissagregationType(this.dissagregationRowsType, indicatorValue);
                return value.id === option.id;
            });
            this.sortRow(row);
            rows.push(row);
        });

        return rows;
    }

    sortRow(row: IndicatorValue[]) {
        row.sort((a, b) => {
            const valueA = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationColumnsType, a);
            const valueB = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationColumnsType, b);
            const orderA = valueA.order;
            const orderB = valueB.order;
            return orderA > orderB ? -1 : 1;
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
