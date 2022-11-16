import {Pipe, PipeTransform} from '@angular/core';
import {CustomDissagregationAssignationToIndicator} from '../model/OsmosysModel';
import {EnumsState} from '../model/UtilsModel';

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


}
