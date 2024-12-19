import {Pipe, PipeTransform} from '@angular/core';
import {CoreIndicator, Indicator, Organization, Statement} from '../model/OsmosysModel';

@Pipe({
    name: 'codeDescription'
})
export class CodeDescriptionPipe implements PipeTransform {
    transform(value: Indicator | Organization | Statement | CoreIndicator): string {
        if (!value) {
            return null;
        }
        return value.code + ' - ' + value.description;
    }
}
