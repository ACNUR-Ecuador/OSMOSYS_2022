import { Pipe, PipeTransform } from '@angular/core';
import {DissagregationAssignationToIndicator} from '../model/OsmosysModel';
import {EnumsState, EnumsType} from '../model/UtilsModel';
import {EnumsService} from '../../services/enums.service';

@Pipe({
  name: 'dissagregationsAssignationToIndicator'
})
export class DissagregationsAssignationToIndicatorPipe implements PipeTransform {
    transform(value: DissagregationAssignationToIndicator[]): string {
        const result = value.filter(da => {
            return da.state === EnumsState.ACTIVE;
        }).map(dis => {
            return this.enumService.resolveLabel(EnumsType.DissagregationType, dis.dissagregationType);
        });
        return result.join(' - ');
    }

    constructor(private enumService: EnumsService) {
    }
}
