import {Pipe, PipeTransform} from '@angular/core';
import {User} from '../model/User';

@Pipe({
    name: 'user'
})
export class UserPipe implements PipeTransform {
    transform(value: User): string {
        if (!value) {
            return null;
        }
        if (value.organization.acronym === 'ACNUR'
            || value.organization.acronym === 'UNHCR') {
            return value.name + (value.office ? ' - ' + value.office.acronym : '');
        } else {
            return value.name + ' - ' + value.organization.acronym;
        }
    }
}
