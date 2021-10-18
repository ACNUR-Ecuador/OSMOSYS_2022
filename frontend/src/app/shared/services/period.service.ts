import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Area, Period} from '../model/OsmosysModel';
import {environment} from '../../../environments/environment';


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
}
