import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Office} from '../model/OsmosysModel';
import {environment} from '../../../environments/environment';
import {EnumsState} from '../model/UtilsModel';

const mainServiceUrl = environment.base_url + '/offices';

@Injectable({
  providedIn: 'root'
})
export class OfficeService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<Office[]> {
        return this.http.get<Office[]>(`${mainServiceUrl}`);
    }

    public getTree(): Observable<Office[]> {
        return this.http.get<Office[]>(`${mainServiceUrl}/tree`);
    }

    public getActive(): Observable<Office[]> {
        return this.http.get<Office[]>(`${mainServiceUrl}/active`);
    }

    public save(office: Office): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, office);
    }

    public update(office: Office): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, office);
    }

    public getByState(state: EnumsState): Observable<Office[]> {
        return this.http.get<Office[]>(`${mainServiceUrl}/byState/${state}`);
    }
}
