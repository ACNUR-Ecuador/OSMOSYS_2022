import {Pipe, PipeTransform} from '@angular/core';
import {MonthPipe} from './month.pipe';
import {Month} from '../model/OsmosysModel';

@Pipe({
    name: 'monthList'
})
export class MonthListPipe implements PipeTransform {

    constructor(
        private monthPipe: MonthPipe
    ) {
    }

    transform(value: Month[]): string {
        if (!value || value.length < 1) {
            return null;
        }
        const result = value.map(value1 =>
            this.monthPipe.transform(value1)
        );
        return result.join(' * ');
    }
}
