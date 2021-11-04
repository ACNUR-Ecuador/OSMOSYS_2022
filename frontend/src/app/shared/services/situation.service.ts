import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Situation} from '../model/OsmosysModel';
import {EnumsState} from '../model/UtilsModel';


const mainServiceUrl = environment.base_url + '/situations';


@Injectable({
  providedIn: 'root'
})
export class SituationService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<Situation[]> {
        return this.http.get<Situation[]>(`${mainServiceUrl}`);
    }

    public save(situation: Situation): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, situation);
    }

    public update(situation: Situation): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, situation);
    }

    public getByState(state: EnumsState): Observable<Situation[]> {
        return this.http.get<Situation[]>(`${mainServiceUrl}/byState/${state}`);
    }
}
