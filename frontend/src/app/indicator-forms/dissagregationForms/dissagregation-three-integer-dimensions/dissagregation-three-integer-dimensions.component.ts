import {Component, Input, OnInit} from '@angular/core';
import {DissagregationType, SelectItemWithOrder} from '../../../shared/model/UtilsModel';
import {Canton, IndicatorValue} from '../../../shared/model/OsmosysModel';
import {UtilsService} from '../../../shared/services/utils.service';
import {EnumsService} from '../../../shared/services/enums.service';
import {forkJoin, Observable, of} from 'rxjs';

@Component({
    selector: 'app-dissagregation-three-integer-dimensions',
    templateUrl: './dissagregation-three-integer-dimensions.component.html',
    styleUrls: ['./dissagregation-three-integer-dimensions.component.scss']
})
export class DissagregationThreeIntegerDimensionsComponent implements OnInit {

    @Input()
    dissagregationType: DissagregationType;
    @Input()
    values: IndicatorValue[];
    @Input()
    editable: boolean;

    dissagregationGroupsType: DissagregationType;
    dissagregationRowsType: DissagregationType;
    dissagregationColumnsType: DissagregationType;


    dissagregationOptionsGroups: SelectItemWithOrder<string | Canton>[];
    dissagregationOptionsColumns: SelectItemWithOrder<string | Canton>[];
    dissagregationOptionsRows: SelectItemWithOrder<string | Canton>[];

    valuesGroupRowsMap: Map<SelectItemWithOrder<string | Canton>, IndicatorValue[][]>;

    constructor(
        public utilsService: UtilsService,
        public enumsService: EnumsService,
    ) {
    }

    ngOnInit(): void {
        this.processDissagregationValues();
    }

    processDissagregationValues() {
        const dissagregationsTypesRyC: DissagregationType[] =
            this.utilsService.getDissagregationTypesByDissagregationType(this.dissagregationType);
        this.dissagregationGroupsType = dissagregationsTypesRyC[0];
        this.dissagregationRowsType = dissagregationsTypesRyC[1];
        this.dissagregationColumnsType = dissagregationsTypesRyC[2];

        const dissagregationOptionsGroupsObj = this.getOptions(this.dissagregationGroupsType);
        const dissagregationOptionsRowsObj = this.getOptions(this.dissagregationRowsType);
        const dissagregationOptionsColumnsObj = this.getOptions(this.dissagregationColumnsType);
        forkJoin([dissagregationOptionsGroupsObj,
            dissagregationOptionsRowsObj, dissagregationOptionsColumnsObj])
            .subscribe(results => {
                this.dissagregationOptionsGroups = results[0] as SelectItemWithOrder<any>[];
                this.dissagregationOptionsRows = results[1] as SelectItemWithOrder<any>[];
                this.dissagregationOptionsColumns = results[2] as SelectItemWithOrder<any>[];
                this.valuesGroupRowsMap = new Map<SelectItemWithOrder<any>, IndicatorValue[][]>();
                this.dissagregationOptionsGroups.forEach(itemL1 => {
                    const rows = this.getRowsByGroups(itemL1);
                    this.valuesGroupRowsMap.set(itemL1, rows);
                });
            });
    }

    getOptions(dissagregationType: DissagregationType): Observable<SelectItemWithOrder<any>[]> {
        if (dissagregationType !== DissagregationType.LUGAR) {
            return this.enumsService.getByDissagregationType(dissagregationType);
        } else {
            let locations: SelectItemWithOrder<any>[] = this.values
                .map(value => {
                    return value.location;
                }).map(value => {
                    const selectITem = new SelectItemWithOrder();
                    selectITem.label = value.provincia.description + ' - ' + value.description;
                    selectITem.value = value;
                    return selectITem;
                }).sort((a, b) => a.label.localeCompare(b.label));
            locations = locations.filter((thing, j, arr) => {
                return arr.indexOf(arr.find(t => t.value.id === thing.value.id)) === j;
            });
            locations.forEach((value, index) => value.order = index);
            return of(locations);

        }
    }

    getRowsByGroups(itemL1: SelectItemWithOrder<any>): Array<Array<IndicatorValue>> {
        let indicatorValues: IndicatorValue[];
        indicatorValues = this.getValuesByDissagregationValues(this.values, this.dissagregationGroupsType, itemL1.value);
        // ordeno y clasifico
        return this.getRowsByValues(this.dissagregationOptionsRows, this.dissagregationOptionsColumns, indicatorValues);
    }

    getValuesByDissagregationValues(values: IndicatorValue[], dissagregationType: DissagregationType, value: string | Canton) {
        return values.filter(indicatorValue => {
            if (dissagregationType === DissagregationType.LUGAR) {
                value = value as Canton;
                const valueOption = this.utilsService.getIndicatorValueByDissagregationType(dissagregationType, indicatorValue) as Canton;
                return valueOption.id === value.id;
            } else {
                const valueOption = this.utilsService.getIndicatorValueByDissagregationType(dissagregationType, indicatorValue) as string;
                return valueOption === value;
            }
        });
    }

    getRowsByValues(dissagregationOptionsRows: SelectItemWithOrder<any>[],
                    dissagregationOptionsColumns: SelectItemWithOrder<any>[],
                    indicatorValues: IndicatorValue[]): IndicatorValue[][] {
        const rows = new Array<Array<IndicatorValue>>();
        if (this.dissagregationRowsType !== DissagregationType.LUGAR) {
            dissagregationOptionsRows.forEach(option => {
                const row: IndicatorValue[] = indicatorValues.filter(indicatorValue => {
                    const value = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationRowsType, indicatorValue);
                    return value === option.value;
                });
                // ordeno las columnas
                this.sortRow(row);
                rows.push(row);
            });
        } else {
            this.dissagregationOptionsRows.forEach(option => {
                const row = indicatorValues.filter(indicatorValue => {
                    const value = this.utilsService
                        .getIndicatorValueByDissagregationType(this.dissagregationRowsType, indicatorValue) as Canton;
                    return value.id === (option.value as Canton).id;
                });
                this.sortRow(row);
                rows.push(row);
            });
        }
        return rows;
    }

    sortRow(row: IndicatorValue[]) {
        row.sort((a, b) => {
            const valueA = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationColumnsType, a);
            const valueB = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationColumnsType, b);
            if (typeof valueA === 'string' || typeof valueB === 'string') {
                const orderA = this.utilsService.getOrderByDissagregationOption(valueA as string,
                    this.dissagregationOptionsColumns);
                const orderB = this.utilsService.getOrderByDissagregationOption(valueB as string,
                    this.dissagregationOptionsColumns);
                return orderA - orderB;
            } else {
                const orderA = (valueA as Canton).code;
                const orderB = (valueB as Canton).code;
                return orderA > orderB ? -1 : 1;
            }
        });
    }

    getTotalIndicatorValuesMap(map: Map<SelectItemWithOrder<string | Canton>, IndicatorValue[][]>) {
        let total = 0;
        map.forEach(value => {
            total += this.utilsService.getTotalIndicatorValuesArrayArray(value);
        });
        return total;
    }

}
