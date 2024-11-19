import {Injectable} from '@angular/core';
import {UtilsService} from './utils.service';
import {
    CustomDissagregationAssignationToIndicator, CustomDissagregationOption,
    DissagregationAssignationToIndicator,
    Period, PeriodStatementAsignation,
    PeriodTagAsignation,
    Statement
} from '../shared/model/OsmosysModel';
import {EnumsService} from './enums.service';
import {EnumsState, EnumsType} from '../shared/model/UtilsModel';

@Injectable({
    providedIn: 'root'
})
export class FilterUtilsService {

    constructor(
        private utilsService: UtilsService,
        private enumsService: EnumsService
    ) {

    }

    customDissagregationsAssignationToIndicatorFilter(value: CustomDissagregationAssignationToIndicator[], filter): boolean {

        if (filter === undefined || filter === null || filter.trim() === '') {
            return true;
        }

        if (value === undefined || value === null || value.length === 0) {
            return false;
        }
        let result = false;
        for (const da of value) {
            if (da.customDissagregation.name.toLowerCase().includes(filter.toString().toLowerCase())
                || da.customDissagregation.name.toLowerCase().includes(filter.toString().toLowerCase())) {
                return result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    generalFilter(value: any, fields: string[], filter: string): boolean {
        if (filter === undefined || filter === null || filter.trim() === '') {
            return true;
        }

        if (value === undefined || value === null) {
            return false;
        }
        for (const field of fields) {
            if (value[field] !== undefined && value[field] !== null) {
                if ((value[field]).toString().toLowerCase().includes(filter.toString().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    generalListFilter(valueArray: any[], fields: string[], filter: string): boolean {
        if (filter === undefined || filter === null || filter.trim() === '') {
            return true;
        }

        if (valueArray === undefined || valueArray === null || valueArray.length === 0) {
            return false;
        }
        for (const value of valueArray) {
            for (const field of fields) {
                if (value[field] !== undefined || value[field] !== null) {
                    if ((value[field]).toString().toLowerCase().includes(filter.toString().toLowerCase())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    periodIndicatorFilter(value: DissagregationAssignationToIndicator[], filter): boolean {

        if (filter === undefined || filter === null) {
            return true;
        }

        if (value === undefined || value === null || value.length === 0) {
            return false;
        }
        let result = false;

        for (const da of value) {
            if (da.period.id === filter.id) {
                return result = true;
            } else {
                result = false;
            }
        }
        return result;
    }
    customFilterPeriod(value: any, filter: any): boolean {
        
        if (!filter || !value) return true; 
        
        // Buscamos si el año seleccionado está presente dentro de la cadena de años concatenados
        return String(value).includes(String(filter.year)); 
      }


    dissagregationsAssignationToIndicatorFilter(value: DissagregationAssignationToIndicator[], filter): boolean {

        if (filter === undefined || filter === null || filter.trim() === '') {
            return true;
        }

        if (value === undefined || value === null || value.length === 0) {
            return false;
        }
        let result = false;

        for (const da of value) {
            const label = this.enumsService.resolveLabel(EnumsType.DissagregationType, da.dissagregationType);
            if (da.dissagregationType.toLowerCase().includes(filter.toString().toLowerCase())
                || da.dissagregationType.toLowerCase().includes(filter.toString().toLowerCase())
                || label.toLowerCase().includes(filter.toString().toLowerCase())
                || label.toLowerCase().includes(filter.toString().toLowerCase())

            ) {
                return result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    statementFilter(value: Statement, filter): boolean {

        if (filter === undefined || filter === null || filter.trim() === '') {
            return true;
        }

        if (value === undefined || value === null) {
            return false;
        }
        return value.code.toLowerCase().includes(filter.toString().toLowerCase())
            || value.description.toLowerCase().includes(filter.toString().toLowerCase());
    }


    objectFilterId(value: any, filter): boolean {

        if (filter === undefined || filter === null) {
            return true;
        }

        if (value === undefined || value === null) {
            return false;
        }
        let result = false;

        const valueId = value.id;
        if (valueId === filter.id) {
            result = true;
        }
        return result;
    }

    customDissagregationName(value: CustomDissagregationOption[], filter: string): boolean {

        if (filter === undefined || filter === null || filter.length < 1) {
            return true;
        }

        if (value === undefined || value === null || value.length < 1) {
            return false;
        }
        let result = false;
        value.map(value1 => {
            return value1.name;
        }).forEach(value1 => {
            if (value1.toLowerCase().includes(filter)) {
                result = true;
            }
        });
        return result;
    }

    roleListFilterId(value: any[], filter: string[]): boolean {

        if (filter === undefined || filter === null || filter.length < 1) {
            return true;
        }

        if (value === undefined || value === null || filter.length < 1) {
            return false;
        }
        let result = false;
        const valuesNames: string[] = value.map(value1 => value1.name);
        valuesNames.forEach(value1 => {
            if (filter.includes(value1)) {
                result = true;
            }
        });

        return result;
    }

    periodStatementAsignationsFilter(value: PeriodStatementAsignation[], filter: Period[]): boolean {

        if (filter === undefined || filter === null || filter.length === 0) {
            return true;
        }

        if (value === undefined || value === null || value.length === 0) {
            return false;
        }
        let result = false;

        const periodsVal: Period[] = value
            .map(value1 => {
                return value1.period;
            })
            .filter(value1 => {
                return value1.state === EnumsState.ACTIVE;
            });
        for (let periodFilter of filter) {
            for (let periodVal of periodsVal) {
                if (periodVal.id === periodFilter.id) {
                    result = true;
                }
            }
        }
        return result;
    }

    periodTagAsignationsFilter(value: PeriodTagAsignation[], filter: Period[]): boolean {

        if (filter === undefined || filter === null || filter.length === 0) {
            return true;
        }

        if (value === undefined || value === null || value.length === 0) {
            return false;
        }
        let result = false;

        const periodsVal: Period[] = value
            .map(value1 => {
                return value1.period;
            })
            .filter(value1 => {
                return value1.state === EnumsState.ACTIVE;
            });
        for (let periodFilter of filter) {
            for (let periodVal of periodsVal) {
                if (periodVal.id === periodFilter.id) {
                    result = true;
                }
            }
        }
        return result;
    }


}
