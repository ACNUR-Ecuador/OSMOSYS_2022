import { Pipe, PipeTransform } from '@angular/core';
import { Project, ProjectResume } from '../model/OsmosysModel';

@Pipe({
  name: 'codeName'
})
export class CodeNamePipe implements PipeTransform {

   transform(value: Project | ProjectResume): string {
          if (!value) {
              return null;
          }
          return value.code + ' - ' + value.name;
      }

}
