import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'booleanYesNo'
})
export class BooleanYesNoPipe implements PipeTransform {

    transform(value: boolean): string {
        if (value) {
            return 'Si';
        } else {
            return 'No';
        }
    }

}
