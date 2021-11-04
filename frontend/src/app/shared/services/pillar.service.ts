import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Pillar} from '../model/OsmosysModel';
import {EnumsState} from '../model/UtilsModel';


const mainServiceUrl = environment.base_url + '/pillars';

@Injectable({
    providedIn: 'root'
})
export class PillarService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<Pillar[]> {
        return this.http.get<Pillar[]>(`${mainServiceUrl}`);
    }

    public save(pillar: Pillar): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, pillar);
    }

    public update(pillar: Pillar): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, pillar);
    }

    public getByState(state: EnumsState): Observable<Pillar[]> {
        return this.http.get<Pillar[]>(`${mainServiceUrl}/byState/${state}`);
    }
}
