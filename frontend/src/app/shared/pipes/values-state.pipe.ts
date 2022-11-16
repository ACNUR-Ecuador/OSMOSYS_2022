import {Pipe, PipeTransform} from '@angular/core';
import {UtilsService} from '../../services/utils.service';

@Pipe({
    name: 'valuesState'
})
export class ValuesStatePipe implements PipeTransform {

    constructor(
        private utilsService: UtilsService
    ) {
    }

    transform(value: number, ...args: unknown[]): string {
        const timeStatus: string = args[1] as string;
        return this.utilsService.valueToBadgeValue(value, timeStatus);
    }
}
