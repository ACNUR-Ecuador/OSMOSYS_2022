import { Pipe, PipeTransform } from '@angular/core';
import { Canton } from '../model/OsmosysModel';

@Pipe({
  name: 'cantons'
})
export class CantonsPipe implements PipeTransform {

  transform(value: Canton[]): string {
         if (value) {
             return value.map(canton => canton.provincia.description+"-"+canton.name).join(', ');;
         }
         return null;
     }

}
