import { Pipe, PipeTransform } from '@angular/core';
import { PeriodTagAsignation } from '../model/OsmosysModel';
import { EnumsState } from '../model/UtilsModel';

@Pipe({
  name: 'tagPeriodTagAsignationsList'
})
export class TagPeriodTagAsignationsListPipe implements PipeTransform {

  transform(value: PeriodTagAsignation[]): unknown {
    const  result =
        value.filter(value1 => {
            return value1.state === EnumsState.ACTIVE;
        }).map(value1 => {
            return value1.period;
        }).map(value1 => {
            return value1.year
        });
      return result.join(' - ');
  }

}
