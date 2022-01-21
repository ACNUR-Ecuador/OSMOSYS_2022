import {Pipe, PipeTransform} from '@angular/core';
import {Statement} from '../model/OsmosysModel';

@Pipe({
    name: 'codeDescriptionList'
})
export class CodeDescriptionListPipe implements PipeTransform {

    transform(value: Statement[]): string {
        const result =
            value.map(value1 => value1.code + '-' + value1.description);

        return result.join(' - ');
    }

}
