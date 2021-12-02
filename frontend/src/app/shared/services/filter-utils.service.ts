import {Injectable} from '@angular/core';
import {UtilsService} from './utils.service';
import {CustomDissagregationAssignationToIndicator, DissagregationAssignationToIndicator, Marker} from '../model/OsmosysModel';
import {EnumsService} from './enums.service';
import {EnumsType} from '../model/UtilsModel';

@Injectable({
    providedIn: 'root'
})
export class FilterUtilsService {

    constructor(private utilsService: UtilsService, private enumsService: EnumsService) {

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
}
