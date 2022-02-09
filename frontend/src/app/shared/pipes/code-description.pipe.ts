import { Pipe, PipeTransform } from '@angular/core';
import {Indicator, Organization, Statement} from '../model/OsmosysModel';

@Pipe({
  name: 'codeDescription'
})
export class CodeDescriptionPipe implements PipeTransform {

  transform(value: Indicator| Organization| Statement): string {
      if (!value) {
          return null;
      }
      return  value.code + ' - ' + value.description;
  }

}
