import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Marker} from '../model/OsmosysModel';
import {EnumsState} from '../model/UtilsModel';
import {environment} from '../../../environments/environment';


const mainServiceUrl = environment.base_url + '/markers';

@Injectable({
  providedIn: 'root'
})
export class MarkerService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<Marker[]> {
        return this.http.get<Marker[]>(`${mainServiceUrl}`);
    }

    public save(marker: Marker): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, marker);
    }

    public update(marker: Marker): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, marker);
    }

    public getByState(state: EnumsState): Observable<Marker[]> {
        return this.http.get<Marker[]>(`${mainServiceUrl}/byState/${state}`);
    }
}
