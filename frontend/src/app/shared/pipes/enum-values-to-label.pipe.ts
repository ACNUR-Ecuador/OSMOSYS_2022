import { Pipe, PipeTransform } from '@angular/core';
import {EnumsType} from '../model/UtilsModel';
import {SelectItem} from 'primeng/api';
import {EnumsService} from '../../services/enums.service';

@Pipe({
  name: 'enumValuesToLabel'
})
export class EnumValuesToLabelPipe implements PipeTransform {
    cacheMap = new Map<EnumsType, SelectItem[]>();

    constructor(private enumsService: EnumsService) {
    }

    transform(value: string, ...args: unknown[]): string {
        const enumName = (args[0] as EnumsType);
        return this.enumsService.resolveLabel(enumName, value);
    }

}
