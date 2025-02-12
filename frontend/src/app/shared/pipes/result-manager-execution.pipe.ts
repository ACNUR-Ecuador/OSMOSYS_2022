import { Pipe, PipeTransform } from '@angular/core';
import { ResultManagerIndicator } from '../model/OsmosysModel';

@Pipe({
  name: 'resultManagerExecution'
})
export class ResultManagerExecutionPipe implements PipeTransform {

  transform(value: ResultManagerIndicator): string {
    if (!value.anualTarget || value.anualTarget === 0 || value.anualExecution === 0) {
      return '0%'; // Evitar división por cero
    }
    const percentage = (value.anualExecution / value.anualTarget) * 100;
    return percentage.toFixed(2) + '%'; // Redondear a 2 decimales
  }

}
