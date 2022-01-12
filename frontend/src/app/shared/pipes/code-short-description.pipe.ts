import {Pipe, PipeTransform} from '@angular/core';
import {Area, Pillar, Situation, Statement} from '../model/OsmosysModel';

@Pipe({
    name: 'codeShortDescription'
})
export class CodeShortDescriptionPipe implements PipeTransform {

    transform(value: Area | Pillar | Situation): string {
        if (!value) {
            return null;
        }
        const result = value.code + ' - ' + value.shortDescription;
        return result;
    }

}
