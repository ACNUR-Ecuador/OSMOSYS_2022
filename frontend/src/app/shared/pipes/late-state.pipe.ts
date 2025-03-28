import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'lateState'
})
export class LateStatePipe implements PipeTransform {

  transform(value: string): string {
    if (value==='LATE') {
        return 'Si';
    } else {
        return 'No';
    }
}

}
