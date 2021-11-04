import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Organization} from '../model/OsmosysModel';
import {environment} from '../../../environments/environment';
import {EnumsState} from '../model/UtilsModel';

const mainServiceUrl = environment.base_url + '/organizations';

@Injectable({
  providedIn: 'root'
})
export class OrganizationService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<Organization[]> {
        return this.http.get<Organization[]>(`${mainServiceUrl}`);
    }

    public save(organization: Organization): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, organization);
    }

    public update(organization: Organization): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, organization);
    }

    public getByState(state: EnumsState): Observable<Organization[]> {
        return this.http.get<Organization[]>(`${mainServiceUrl}/byState/${state}`);
    }
}
