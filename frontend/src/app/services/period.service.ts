import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Period} from '../shared/model/OsmosysModel';
import {EnumsState} from '../shared/model/UtilsModel';

const mainServiceUrl = environment.base_url + '/periods';

@Injectable({
    providedIn: 'root'
})
export class PeriodService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<Period[]> {
        return this.http.get<Period[]>(`${mainServiceUrl}`);
    }

    public getwithGeneralIndicatorAll(): Observable<Period[]> {
        return this.http.get<Period[]>(`${mainServiceUrl}/withGeneralIndicatorAll`);
    }

    public save(period: Period): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, period);
    }

    public update(period: Period): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, period);
    }

    public getByState(state: EnumsState): Observable<Period[]> {
        return this.http.get<Period[]>(`${mainServiceUrl}/byState/${state}`);
    }

    public getById(id: number): Observable<Period> {
        return this.http.get<Period>(`${mainServiceUrl}/${id}`);
    }
}
