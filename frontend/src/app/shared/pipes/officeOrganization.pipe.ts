import {Pipe, PipeTransform} from '@angular/core';
import {Office, Organization} from '../model/OsmosysModel';

@Pipe({
    name: 'office'
})
export class OfficeOrganizationPipe implements PipeTransform {

    transform(value: Office | Organization): string {
        if (!value) {
            return null;
        }
        const result = value.acronym + '-' + value.description;
        return result;
    }

}
