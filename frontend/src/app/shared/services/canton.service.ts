import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Canton} from '../model/OsmosysModel';
import {EnumsState} from '../model/UtilsModel';

const mainServiceUrl = environment.base_url + '/cantons';

@Injectable({
  providedIn: 'root'
})
export class CantonService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<Canton[]> {
        return this.http.get<Canton[]>(`${mainServiceUrl}`);
    }

    public save(canton: Canton): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, canton);
    }

    public update(canton: Canton): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, canton);
    }

    public getByState(state: EnumsState): Observable<Canton[]> {
        return this.http.get<Canton[]>(`${mainServiceUrl}/byState/${state}`);
    }
}
