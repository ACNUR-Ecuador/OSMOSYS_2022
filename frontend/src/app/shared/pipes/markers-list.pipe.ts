import { Pipe, PipeTransform } from '@angular/core';
import {Marker} from '../model/OsmosysModel';

@Pipe({
  name: 'markersList'
})
export class MarkersListPipe implements PipeTransform {

    transform(value: Marker[]): string {
        const result =
            value.map(value1 => value1.shortDescription);

        return result.join(' - ');
    }

}
