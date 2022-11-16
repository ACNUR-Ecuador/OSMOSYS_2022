import {Pipe, PipeTransform} from '@angular/core';
import {Indicator} from '../model/OsmosysModel';

@Pipe({
    name: 'indicator'
})
export class IndicatorPipe implements PipeTransform {

    transform(value: Indicator): string {
        if (!value) {
            return null;
        }
        let result = value.code + ' - ' + value.description;
        if (value.category) {
            result += ' (Categoría: ' + value.category + ')';
        }
        return result;
    }
}
