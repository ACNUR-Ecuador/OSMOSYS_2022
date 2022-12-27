import {Pipe, PipeTransform} from '@angular/core';
import {Indicator} from "../model/OsmosysModel";

@Pipe({
    name: 'periodsFromIndicator'
})
export class PeriodsFromIndicatorPipe implements PipeTransform {

    transform(value: Indicator): string {

        let years: number[] = value.dissagregationsAssignationToIndicator.map(value1 => {
            return value1.period.year
        });
        years= [...new Set(years)];
        if(years && years.length<1){
            return null;
        }else {
            return years.sort((a, b) => b-a).join(' - ');
        }
    }

}
