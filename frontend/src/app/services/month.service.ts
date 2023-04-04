import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Month, MonthValues, YearMonth} from '../shared/model/OsmosysModel';
import {environment} from '../../environments/environment';

const mainServiceUrl = environment.base_url + '/months';

@Injectable({
  providedIn: 'root'
})
export class MonthService {

    constructor(private http: HttpClient) {
    }

    public getMonthIndicatorValueByMonthId(monthId: number): Observable<MonthValues> {

        return this.http.get<MonthValues>(`${mainServiceUrl}/getMonthIndicatorValueByMonthId/${monthId}`)
            .pipe(map(value => {
                const monthValue = new MonthValues();
                monthValue.month = value.month;
                const valuesmap = value.indicatorValuesMap;
                monthValue.indicatorValuesMap = new Map(Object.entries(valuesmap));
                monthValue.customDissagregationValues = value.customDissagregationValues;
                return monthValue;
            }));
    }

    public getMonthsByIndicatorExecutionId(indicatorExecutinId: number): Observable<Month[]> {
        return this.http.get<Month[]>(`${mainServiceUrl}/getMonthsByIndicatorExecutionId/${indicatorExecutinId}`);
    }

    public changeBlockedState(monthId: number, blockingState: boolean): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}/changeBlockedState/${monthId}/${blockingState}`, null);
    }

    public getYearMonthByPeriodId(periodId: number): Observable<YearMonth[]> {
        return this.http.get<YearMonth[]>(`${mainServiceUrl}/getYearMonthByPeriodId/${periodId}`);
    }

    public massiveBlock(yearMonth: YearMonth): Observable<any> {
        return this.http.post(`${mainServiceUrl}/massiveBlock`, yearMonth);
    }

    public massiveUnblock(yearMonth: YearMonth): Observable<any> {
        return this.http.post(`${mainServiceUrl}/massiveUnblock`, yearMonth);
    }
}
