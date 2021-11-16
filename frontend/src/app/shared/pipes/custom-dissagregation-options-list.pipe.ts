import {Pipe, PipeTransform} from '@angular/core';
import {CustomDissagregationOption, Marker} from '../model/OsmosysModel';
import {EnumsState} from '../model/UtilsModel';

@Pipe({
    name: 'customDissagregationOptionsList'
})
export class CustomDissagregationOptionsListPipe implements PipeTransform {

    transform(value: CustomDissagregationOption[]): string {
        const result =
            value.filter(value1 => {
                return value1.state === EnumsState.ACTIVE;
            }).map(value1 => value1.name);

        return result.join(' - ');
    }
}
