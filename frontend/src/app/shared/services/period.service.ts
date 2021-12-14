import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Period} from '../model/OsmosysModel';
import {environment} from '../../../environments/environment';
import {EnumsState} from '../model/UtilsModel';


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
