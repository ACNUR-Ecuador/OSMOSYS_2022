import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ImportFile, Indicator} from '../shared/model/OsmosysModel';
import {EnumsState} from '../shared/model/UtilsModel';
import {environment} from '../../environments/environment';

const mainServiceUrl = environment.base_url + '/indicators';

@Injectable({
    providedIn: 'root'
})
export class IndicatorService {
    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<Indicator[]> {
        return this.http.get<Indicator[]>(`${mainServiceUrl}`);
    }

    public save(indicator: Indicator): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, indicator);
    }

    public update(indicator: Indicator): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, indicator);
    }

    public getByState(state: EnumsState): Observable<Indicator[]> {
        return this.http.get<Indicator[]>(`${mainServiceUrl}/byState/${state}`);
    }

    public getByPeriodAssignment(periodId: number): Observable<Indicator[]> {
        return this.http.get<Indicator[]>(`${mainServiceUrl}/getByPeriodAssignmentAndState/${periodId}`);
    }

    getImportTemplate(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getImportTemplate/${periodId}`,{
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public importCatalog(file: ImportFile) {
        return this.http.post(`${mainServiceUrl}/importIndicatorsCatalog`, file);
    }
}
