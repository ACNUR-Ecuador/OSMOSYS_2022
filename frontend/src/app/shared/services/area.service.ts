import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Area} from '../model/OsmosysModel';

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
}
