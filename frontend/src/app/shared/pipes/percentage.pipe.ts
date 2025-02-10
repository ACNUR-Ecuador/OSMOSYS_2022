import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'percentage'
})
export class PercentagePipe implements PipeTransform {

  transform(execution: number, target: number): string {
    if (!target || target === 0) {
      return '0%'; // Evitar división por cero
    }
    const percentage = (execution / target) * 100;
    return percentage.toFixed(2) + '%'; // Redondear a 2 decimales
  }

}