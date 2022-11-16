import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppConfiguration} from '../shared/model/OsmosysModel';

const mainServiceUrl = environment.base_url + '/appconfiguration';

@Injectable({
    providedIn: 'root'
})
export class AppConfigurationService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<AppConfiguration[]> {
        return this.http.get<AppConfiguration[]>(`${mainServiceUrl}`);
    }

    public update(appConfiguration: AppConfiguration): Observable<any> {
        return this.http.put<any>(`${mainServiceUrl}`, appConfiguration);
    }

    public getValueByKey(key: string): Observable<string> {
        return this.http.get<string>(`${mainServiceUrl}/value/${key}`);
    }
}
