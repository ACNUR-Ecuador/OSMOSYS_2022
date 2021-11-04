import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Statement} from '../model/OsmosysModel';
import {EnumsState} from '../model/UtilsModel';

const mainServiceUrl = environment.base_url + '/statements';

@Injectable({
  providedIn: 'root'
})
export class StatementService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<Statement[]> {
        return this.http.get<Statement[]>(`${mainServiceUrl}`);
    }

    public save(statement: Statement): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, statement);
    }

    public update(statement: Statement): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, statement);
    }

    public getByState(state: EnumsState): Observable<Statement[]> {
        return this.http.get<Statement[]>(`${mainServiceUrl}/byState/${state}`);
    }
}
