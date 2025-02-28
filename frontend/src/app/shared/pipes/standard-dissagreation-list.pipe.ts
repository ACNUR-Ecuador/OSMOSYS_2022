import { Pipe, PipeTransform } from '@angular/core';
import {StandardDissagregationOption} from "../model/OsmosysModel";
import {EnumsState} from "../model/UtilsModel";

@Pipe({
  name: 'standardDissagreationList'
})
export class StandardDissagreationListPipe implements PipeTransform {

  transform(value: StandardDissagregationOption[]):string {
      const result =
          value.map(value1 => value1.name);

      return result.join(' - ');
  }

}
