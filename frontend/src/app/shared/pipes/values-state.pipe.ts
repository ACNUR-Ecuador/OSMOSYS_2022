import {Pipe, PipeTransform} from '@angular/core';
import {UtilsService} from '../services/utils.service';
import {MonthType} from '../model/UtilsModel';

@Pipe({
    name: 'valuesState'
})
export class ValuesStatePipe implements PipeTransform {

    constructor(
        private utilsService: UtilsService
    ) {
    }

    transform(value: number, ...args: unknown[]): string {
        const monthS: string = args[0] as string;
        const month: MonthType = MonthType[monthS];
        const year: number = args[1] as number;
        return this.utilsService.valueToBadgeValue(value, month, year);
    }

}
