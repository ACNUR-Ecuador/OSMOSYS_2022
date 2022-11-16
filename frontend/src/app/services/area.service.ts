import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Area, AreaResume} from '../shared/model/OsmosysModel';
import {EnumsState} from '../shared/model/UtilsModel';

const mainServiceUrl = environment.base_url + '/areas';

@Injectable({
    providedIn: 'root'
})
export class AreaService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<Area[]> {
        return this.http.get<Area[]>(`${mainServiceUrl}`);
    }

    public save(area: Area): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, area);
    }

    public update(area: Area): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, area);
    }

    public getByState(state: EnumsState): Observable<Area[]> {
        return this.http.get<Area[]>(`${mainServiceUrl}/byState/${state}`);
    }

    public getDirectImplementationAreaResume(
        userId: number,
        periodId: number,
        officeId: number,
        supervisor: boolean,
        responsible: boolean,
        backup: boolean,
    ): Observable<AreaResume[]> {
        // Initialize Params Object
        let params = new HttpParams();
        // Begin assigning parameters
        params = params.append('userId', String(userId));
        params = params.append('periodId', String(periodId));
        params = params.append('officeId', String(officeId));
        params = params.append('supervisor', String(supervisor));
        params = params.append('responsible', String(responsible));
        params = params.append('backup', String(backup));
        return this.http.get<AreaResume[]>(`${mainServiceUrl}/getDirectImplementationAreaResume`, {params});
    }
}
