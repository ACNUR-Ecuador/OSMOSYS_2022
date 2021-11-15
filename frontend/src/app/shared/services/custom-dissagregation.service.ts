import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CustomDissagregation} from '../model/OsmosysModel';
import {EnumsState} from '../model/UtilsModel';
import {environment} from '../../../environments/environment';

const mainServiceUrl = environment.base_url + '/customDissagregations';

@Injectable({
    providedIn: 'root'
})
export class CustomDissagregationService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<CustomDissagregation[]> {
        return this.http.get<CustomDissagregation[]>(`${mainServiceUrl}`);
    }

    public save(marker: CustomDissagregation): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, marker);
    }

    public update(marker: CustomDissagregation): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, marker);
    }

    public getByState(state: EnumsState): Observable<CustomDissagregation[]> {
        return this.http.get<CustomDissagregation[]>(`${mainServiceUrl}/byState/${state}`);
    }
}
