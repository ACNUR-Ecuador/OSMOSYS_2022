import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {EMPTY, Observable, of} from 'rxjs';
import {EnumsType} from '../model/UtilsModel';
import {HttpClient} from '@angular/common/http';
import {SelectItem} from 'primeng/api';
import {catchError, shareReplay} from 'rxjs/operators';

const mainServiceUrl = environment.base_url + '/enums';


@Injectable({
    providedIn: 'root'
})
export class EnumsService {

    public cacheMap = new Map<EnumsType, SelectItem[]>();

    constructor(private http: HttpClient) {
    }

    public getByType(type: EnumsType): Observable<SelectItem[]> {
        return this.http.get<SelectItem[]>(`${mainServiceUrl}/${type}`);
    }

    public loadcache() {
        console.log('loading cache');
        Object.keys(EnumsType).map(key => {
            const enumname: EnumsType = EnumsType[key];
            this.getByType(enumname).subscribe(value => {
                this.cacheMap.set(enumname, value);
            });
        });
        console.log('finish loading cache');
    }


    resolveLabel(enumName, value): string {
        const enumerador = this.cacheMap.get(enumName).filter(enume => {
            return enume.value === value;
        });
        if (enumerador && enumerador.length > 0) {
            return enumerador[0].label;
        } else {
            return value;
        }
    }


}
