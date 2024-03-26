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
    cacheMap = new Map<string, string>();

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

    public loadcache() {
        this.getAll().subscribe({
            next: value => {
                value.forEach(value1 => {
                    this.cacheMap.set(value1.clave,value1.valor);
                });
            }, error: err => {
                console.log('Error cache appconf: ' + err);
            }
        });
    }

    public getValueByKeyFromCache(key: string): string {
        const value=this.cacheMap.get(key);
        if(value){
            return value;
        }else {
            this.loadcache();
            return 'false';
        }
    }

    public getCanPartnerEditLocations(): boolean {
        const value=this.cacheMap.get('CAN_PARTNER_EDIT_LOCATION');
        return value?(value.toLowerCase()==='true'):false;
    }
    public getCanDirectImplementacionEditLocations(): boolean {
        const value=this.cacheMap.get('CAN_UNHCR_EDIT_LOCATION');
        return value?(value.toLowerCase()==='true'):false;
    }
}
