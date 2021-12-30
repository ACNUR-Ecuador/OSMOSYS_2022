import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {MonthValues, QuarterResumeWeb, StartEndDatesWeb} from '../model/OsmosysModel';
import {Observable} from 'rxjs';

const mainServiceUrl = environment.base_url + '/months';

@Injectable({
    providedIn: 'root'
})
export class MonthService {
    constructor(private http: HttpClient) {
    }

    public getMonthIndicatorValueByMonthId(monthId: number): Observable<MonthValues> {

        return this.http.get<MonthValues>(`${mainServiceUrl}/getMonthIndicatorValueByMonthId/${monthId}`);
    }
}
