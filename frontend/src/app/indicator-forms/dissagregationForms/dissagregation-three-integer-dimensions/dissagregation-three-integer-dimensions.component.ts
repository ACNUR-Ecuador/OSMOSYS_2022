import {Component, Input, OnInit} from '@angular/core';
import {DissagregationType, EnumsType, SelectItemWithOrder} from '../../../shared/model/UtilsModel';
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
    enumTypedissagregationGroups: EnumsType;
    enumTypedissagregationRows: EnumsType;
    enumTypedissagregationColumns: EnumsType;
    dissagregationOptionsGroups: SelectItemWithOrder<any>[];
    dissagregationOptionsColumns: SelectItemWithOrder<any>[];
    dissagregationOptionsRows: SelectItemWithOrder<any>[];

    valuesGroupRowsMap: Map<SelectItemWithOrder<any>, IndicatorValue[][]>;

    constructor(
        public utilsService: UtilsService,
        public enumsService: EnumsService,
    ) {
    }

    ngOnInit(): void {
        this.processDissagregationValues();

    }

    processDissagregationValues() {
        // ontengo los tipos de desagregación horizontal y vertical
        const enumTypesRyC: EnumsType[] =
            this.utilsService.getEnymTypesByDissagregationTypes(this.dissagregationType);
        this.enumTypedissagregationGroups = enumTypesRyC[0];
        this.enumTypedissagregationRows = enumTypesRyC[1];
        this.enumTypedissagregationColumns = enumTypesRyC[2];
        const dissagregationsTypesRyC: DissagregationType[] =
            this.utilsService.getDissagregationTypesByDissagregationType(this.dissagregationType);
        this.dissagregationGroupsType = dissagregationsTypesRyC[0];
        this.dissagregationRowsType = dissagregationsTypesRyC[1];
        this.dissagregationColumnsType = dissagregationsTypesRyC[2];

        const dissagregationOptionsGroupsObj = this.getOptions(this.dissagregationGroupsType);
        const dissagregationOptionsRowsObj = this.getOptions(this.dissagregationRowsType);
        const dissagregationOptionsColumnsObj = this.getOptions(this.dissagregationColumnsType);
        // veo los grupos
        forkJoin([dissagregationOptionsGroupsObj, dissagregationOptionsRowsObj, dissagregationOptionsColumnsObj])
            .subscribe(results => {
                this.dissagregationOptionsGroups = results[0];
                this.dissagregationOptionsRows = results[1];
                this.dissagregationOptionsColumns = results[2];
                console.log(this.dissagregationOptionsGroups);
                console.log(this.dissagregationOptionsRows);
                console.log(this.dissagregationOptionsColumns);
                this.valuesGroupRowsMap = new Map<SelectItemWithOrder<any>, IndicatorValue[][]>();
                this.dissagregationOptionsGroups.forEach(value => {
                    const rows = this.getRowsByGroup(value);
                    this.valuesGroupRowsMap.set(value, rows);

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

    getRowsByGroup(item: SelectItemWithOrder<any>): Array<Array<IndicatorValue>> {
        const rows = new Array<Array<IndicatorValue>>();
        const groupValues: IndicatorValue[] = this.values.filter(value => {
            if (this.dissagregationGroupsType !== DissagregationType.LUGAR) {
                const valueOption = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationGroupsType, value);
                return valueOption === item.value;
            } else {
                const valueOption = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationGroupsType, value) as Canton;
                return valueOption.id === item.value.id;
            }
        });
        if (this.dissagregationRowsType !== DissagregationType.LUGAR) {
            this.dissagregationOptionsRows.forEach(dissagregationOption => {
                const row = this.values.filter(value => {
                    const valueOption = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationRowsType, value);
                    return valueOption === dissagregationOption.value;
                });
                // ordeno las columnas
                row.sort((a, b) => {
                    const optionA = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationColumnsType, a);
                    const optionB = this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationColumnsType, b);
                    if (typeof optionA === 'string' || optionA instanceof String) {
                        // @ts-ignore
                        const orderA = this.utilsService.getOrderByDissagregationOption(optionA, this.dissagregationOptionsColumns);
                        // @ts-ignore
                        const orderB = this.utilsService.getOrderByDissagregationOption(optionB, this.dissagregationOptionsColumns);
                        return orderA - orderB;
                    } else {
                        // todo canton
                        return 1;
                    }

                });
                rows.push(row);
            });
        } else {
            this.dissagregationOptionsRows.forEach(dissagregationOption => {
                const row = this.values.filter(value => {
                    const valueOption =
                        (this.utilsService.getIndicatorValueByDissagregationType(this.dissagregationRowsType, value)) as Canton;
                    return valueOption.id === dissagregationOption.value.id;

                });
                rows.push(row);
            });
        }
        console.log('item');
        console.log(item);
        console.log(groupValues);
        return rows;

    }


}
