import {Pipe, PipeTransform} from '@angular/core';
import {Month} from '../model/OsmosysModel';

@Pipe({
    name: 'month'
})
export class MonthPipe implements PipeTransform {

    transform(value: Month): unknown {
        if (value) {
            return value.month + '-' + value.year;
        }
        return null;
    }

}
