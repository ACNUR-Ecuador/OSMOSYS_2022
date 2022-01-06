import {Pipe, PipeTransform} from '@angular/core';
import {Role} from '../model/User';
import {EnumsService} from '../services/enums.service';
import {EnumsType} from '../model/UtilsModel';

@Pipe({
    name: 'rolesList'
})
export class RolesListPipe implements PipeTransform {
    constructor(private enumsService: EnumsService) {
    }

    transform(value: Role[]): string {
        if (!value || value.length < 1) {
            return null;
        }
        const result = value.map(value1 => {
            return this.enumsService.resolveLabel(EnumsType.RoleType, value1.name)
                + '-' + this.enumsService.resolveLabel(EnumsType.State, value1.state);
        });

        return result.join(' * ');


    }

}
