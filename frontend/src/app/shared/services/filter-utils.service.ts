import {Injectable} from '@angular/core';
import {UtilsService} from './utils.service';
import {
    CustomDissagregationAssignationToIndicator, CustomDissagregationOption,
    DissagregationAssignationToIndicator,
    Marker,
    Office,
    Organization, Statement
} from '../model/OsmosysModel';
import {EnumsService} from './enums.service';
import {EnumsType} from '../model/UtilsModel';
import {OfficeOrganizationPipe} from '../pipes/officeOrganization.pipe';

@Injectable({
    providedIn: 'root'
})
export class FilterUtilsService {

    constructor(
        private utilsService: UtilsService,
        private enumsService: EnumsService,
        private officeOrganizationPipe: OfficeOrganizationPipe
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
            if (value[field] === undefined || value[field] === null) {
                continue;
            } else {
                if (value[field].toLowerCase().includes(filter.toString().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
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
        let result = false;


        if (value.code.toLowerCase().includes(filter.toString().toLowerCase())
            || value.description.toLowerCase().includes(filter.toString().toLowerCase())
        ) {
            return result = true;
        } else {
            result = false;
        }
        return result;
    }

    markersFilter(value: Marker[], filter): boolean {

        if (filter === undefined || filter === null || filter.trim() === '') {
            return true;
        }

        if (value === undefined || value === null || value.length === 0) {
            return false;
        }
        let result = false;

        for (const da of value) {

            if (
                da.shortDescription.toLowerCase().includes(filter.toString().toLowerCase())
                || da.shortDescription.toLowerCase().includes(filter.toString().toLowerCase())
                || da.subType.toLowerCase().includes(filter.toString().toLowerCase())
                || da.subType.toLowerCase().includes(filter.toString().toLowerCase())
                || da.type.toLowerCase().includes(filter.toString().toLowerCase())
                || da.type.toLowerCase().includes(filter.toString().toLowerCase())


            ) {
                return result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    officeOrganizationAcronymDescriptionFilter(value: Office | Organization, filter): boolean {

        if (filter === undefined || filter === null || filter.trim() === '') {
            return true;
        }

        if (value === undefined || value === null) {
            return false;
        }
        let result = false;

        const valueS = this.officeOrganizationPipe.transform(value);
        if (valueS.toLowerCase().includes(filter.toString().toLowerCase())) {
            result = true;
        }
        return result;
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

    // todo sin uso ni prueba
    objectListFilterId(value: any[], filter: any[]): boolean {

        if (filter === undefined || filter === null || filter.length < 1) {
            return true;
        }

        if (value === undefined || value === null || filter.length < 1) {
            return false;
        }
        let result = false;
        const filterIds: number[] = filter.map(value1 => value1.id as number);
        const valuesIds: number[] = value.map(value1 => value1.id as number);
        valuesIds.forEach(value1 => {
            if (filterIds.includes(value1)) {
                result = true;
            }
        });

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


}
