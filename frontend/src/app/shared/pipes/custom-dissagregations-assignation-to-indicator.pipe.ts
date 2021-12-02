import { Pipe, PipeTransform } from '@angular/core';
import {CustomDissagregationAssignationToIndicator, DissagregationAssignationToIndicator} from '../model/OsmosysModel';
import {EnumsState, EnumsType} from '../model/UtilsModel';
import {EnumsService} from '../services/enums.service';

@Pipe({
  name: 'customDissagregationsAssignationToIndicator'
})
export class CustomDissagregationsAssignationToIndicatorPipe implements PipeTransform {

    transform(value: CustomDissagregationAssignationToIndicator[]): string {
        const result = value.filter(da => {
            return da.state === EnumsState.ACTIVE;
        }).map(dis => {
            return dis.customDissagregation.name;
        });
        return result.join(' - ');
    }

    constructor(private enumService: EnumsService) {
    }
}
