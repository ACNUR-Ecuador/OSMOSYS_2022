import { Pipe, PipeTransform } from '@angular/core';
import {PeriodStatementAsignation} from "../model/OsmosysModel";
import {EnumsState} from "../model/UtilsModel";

@Pipe({
  name: 'statementPeriodStatementAsignationsList'
})
export class StatementPeriodStatementAsignationsListPipe implements PipeTransform {

  transform(value: PeriodStatementAsignation[]): unknown {
    const  result =
        value.filter(value1 => {
            return value1.state === EnumsState.ACTIVE;
        }).map(value1 => {
            return value1.period;
        }).map(value1 => {
            return value1.year
        });
      return result.join(' - ');
  }

}
