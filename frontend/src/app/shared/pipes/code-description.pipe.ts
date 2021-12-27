import { Pipe, PipeTransform } from '@angular/core';
import {Indicator} from '../model/OsmosysModel';

@Pipe({
  name: 'codeDescription'
})
export class CodeDescriptionPipe implements PipeTransform {

  transform(value: Indicator): string {
      if (!value) {
          return null;
      }
      const result = value.code + ' - ' + value.description;
      return result;
  }

}
