import {Pipe, PipeTransform} from '@angular/core';
import {Office, Organization} from '../model/OsmosysModel';

@Pipe({
  name: 'officeOrganization'
})
export class OfficeOrganizationPipe implements PipeTransform {
    transform(value: Office | Organization): string {
        if (!value) {
            return null;
        }
        return value.acronym + '-' + value.description;
    }

}
