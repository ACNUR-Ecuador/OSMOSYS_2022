import {Component, Input, OnInit, SimpleChanges} from '@angular/core';
import {EnumWeb, IndicatorValue, StandardDissagregationOption} from "../../../shared/model/OsmosysModel";
import {UtilsService} from "../../../services/utils.service";
import {EnumsService} from "../../../services/enums.service";
import {EnumsType} from "../../../shared/model/UtilsModel";

@Component({
    selector: 'app-dissagregation-six-integer-dimensions',
    templateUrl: './dissagregation-six-integer-dimensions.component.html',
    styleUrls: ['./dissagregation-six-integer-dimensions.component.scss']
})
export class DissagregationSixIntegerDimensionsComponent implements OnInit {

    @Input()
    dissagregationType: EnumWeb;
    @Input()
    values: IndicatorValue[];
    @Input()
    editable: boolean;

    dissagregationGroupsL1Type: EnumWeb;
    dissagregationGroupsL2Type: EnumWeb;
    dissagregationGroupsL3Type: EnumWeb;
    dissagregationGroupsL4Type: EnumWeb;
    dissagregationRowsType: EnumWeb;
    dissagregationColumnsType: EnumWeb;


    dissagregationOptionsGroupsL1: StandardDissagregationOption[];
    dissagregationOptionsGroupsL2: StandardDissagregationOption[];
    dissagregationOptionsGroupsL3: StandardDissagregationOption[];
    dissagregationOptionsGroupsL4: StandardDissagregationOption[];
    dissagregationOptionsColumns: StandardDissagregationOption[];
    dissagregationOptionsRows: StandardDissagregationOption[];

    valuesGroupRowsMap: Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>>>;

    constructor(
        public utilsService: UtilsService,
        public enumsService: EnumsService
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

        this.dissagregationGroupsL1Type = dissagregationsTypesRyCEnum[0];
        this.dissagregationGroupsL2Type = dissagregationsTypesRyCEnum[1];
        this.dissagregationGroupsL3Type = dissagregationsTypesRyCEnum[2];
        this.dissagregationGroupsL4Type = dissagregationsTypesRyCEnum[3];
        this.dissagregationRowsType = dissagregationsTypesRyCEnum[4];
        this.dissagregationColumnsType = dissagregationsTypesRyCEnum[5];

        // obtiene las opciones
        this.dissagregationOptionsGroupsL1 = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationGroupsL1Type);
        this.dissagregationOptionsGroupsL2 = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationGroupsL2Type);
        this.dissagregationOptionsGroupsL3 = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationGroupsL3Type);
        this.dissagregationOptionsGroupsL4 = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationGroupsL4Type);
        this.dissagregationOptionsRows = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationRowsType);
        this.dissagregationOptionsColumns = this.utilsService.getOptionsFromValuesByDissagregationType(this.values, this.dissagregationColumnsType);


        // hace un mapa
        this.valuesGroupRowsMap = new Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>>>();
        // para el nivel 1
        this.dissagregationOptionsGroupsL1.forEach(optionL1 => {
            // por cada nivel 1
            const groupL2Map: Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>> = new Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>>();

            this.dissagregationOptionsGroupsL2.forEach(optionL2 => {
                const groupL3Map: Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>> = new Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>();

                this.dissagregationOptionsGroupsL3.forEach(optionL3 => {
                    const groupL4Map: Map<StandardDissagregationOption, IndicatorValue[][]> = new Map<StandardDissagregationOption, IndicatorValue[][]>();
                    this.dissagregationOptionsGroupsL4.forEach(optionL4 => {
                        // // filtro para 2 2 primeros niveles
                        let rows = this.getByL1AndL2AndL3AndL4Options(
                            optionL1, this.dissagregationGroupsL1Type,
                            optionL2, this.dissagregationGroupsL2Type,
                            optionL3, this.dissagregationGroupsL3Type,
                            optionL4, this.dissagregationGroupsL4Type,
                            this.values);
                        const rowsT = this.utilsService.getRowsAndColumnsFromValues(this.dissagregationColumnsType,
                            this.dissagregationRowsType,
                            this.dissagregationOptionsRows,
                            this.dissagregationOptionsColumns,
                            rows);


                        groupL4Map.set(optionL4, rowsT);
                    });
                    groupL3Map.set(optionL3, groupL4Map);
                });

                groupL2Map.set(optionL2, groupL3Map);
            });
            this.valuesGroupRowsMap.set(optionL1, groupL2Map);
        });

    }

    getByL1AndL2AndL3AndL4Options(
        optionL1: StandardDissagregationOption, dissagregationGroupsL1Type: EnumWeb,
        optionL2: StandardDissagregationOption, dissagregationGroupsL2Type: EnumWeb,
        optionL3: StandardDissagregationOption, dissagregationGroupsL3Type: EnumWeb,
        optionL4: StandardDissagregationOption, dissagregationGroupsL4Type: EnumWeb,
        values: IndicatorValue[]): IndicatorValue[] {
        return values.filter(value => {

            const valueOption1 = this.utilsService.getIndicatorValueByDissagregationType(dissagregationGroupsL1Type, value);
            const valueOption2 = this.utilsService.getIndicatorValueByDissagregationType(dissagregationGroupsL2Type, value);
            const valueOption3 = this.utilsService.getIndicatorValueByDissagregationType(dissagregationGroupsL3Type, value);
            const valueOption4 = this.utilsService.getIndicatorValueByDissagregationType(dissagregationGroupsL4Type, value);
            return valueOption1.id === optionL1.id && valueOption2.id === optionL2.id
                && valueOption3.id === optionL3.id
                && valueOption4.id === optionL4.id
                ;
        });
    }


    getTotalIndicatorValuesMapL1(map: Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>>) {
        let total = 0;
        map.forEach(valueMap => {
            valueMap.forEach(valueMap1 => {
                valueMap1.forEach(value => {
                    total += this.utilsService.getTotalIndicatorValuesArrayArray(value);
                });
            });
        });
        return total;
    }

    getTotalIndicatorValuesMapL2(map: Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>) {
        let total = 0;
        map.forEach(valueMap => {
            valueMap.forEach(value => {
                total += this.utilsService.getTotalIndicatorValuesArrayArray(value);

            });

        });
        return total;
    }

    getTotalIndicatorValuesMapL3(map: Map<StandardDissagregationOption, IndicatorValue[][]>) {
        let total = 0;
        map.forEach(value => {
            total += this.utilsService.getTotalIndicatorValuesArrayArray(value);

        });
        return total;
    }


    getTotalIndicator(map: Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, Map<StandardDissagregationOption, IndicatorValue[][]>>>>) {
        if (!map) {
            return null;
        }
        let total = 0;
        map.forEach(valueMap => {
            valueMap.forEach(valueMap2 => {
                valueMap2.forEach(valueMap3 => {
                    valueMap3.forEach(value => {
                        total += this.utilsService.getTotalIndicatorValuesArrayArray(value);
                    });
                });
            });
        });
        return total;
    }


}
