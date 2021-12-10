import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GeneralIndicator} from '../model/OsmosysModel';
import {EnumsState} from '../model/UtilsModel';


const mainServiceUrl = environment.base_url + '/generalIndicators';


@Injectable({
  providedIn: 'root'
})
export class GeneralIndicatorService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<GeneralIndicator[]> {
        return this.http.get<GeneralIndicator[]>(`${mainServiceUrl}`);
    }

    public save(generalIndicator: GeneralIndicator): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, generalIndicator);
    }

    public update(generalIndicator: GeneralIndicator): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, generalIndicator);
    }

    public getByState(state: EnumsState): Observable<GeneralIndicator[]> {
        return this.http.get<GeneralIndicator[]>(`${mainServiceUrl}/byState/${state}`);
    }

    public getByPeriodId(periodId: number): Observable<GeneralIndicator> {
        return this.http.get<GeneralIndicator>(`${mainServiceUrl}/byPeriodId/${periodId}`);
    }
}
