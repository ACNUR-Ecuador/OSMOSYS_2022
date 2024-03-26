import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {EMPTY, Observable} from 'rxjs';
import {EnumsType, SelectItemWithOrder} from '../shared/model/UtilsModel';
import {HttpClient} from '@angular/common/http';
import {catchError, shareReplay} from 'rxjs/operators';
import {EnumWeb} from "../shared/model/OsmosysModel";

const mainServiceUrl = environment.base_url + '/enums';


@Injectable({
    providedIn: 'root'
})
export class EnumsService {

    cacheMap = new Map<EnumsType, SelectItemWithOrder<any>[]>();

    constructor(private http: HttpClient) {
    }

    public getByType(type: EnumsType): Observable<SelectItemWithOrder<any>[]> {
        // @ts-ignore
        if (this.cacheMap[type]) {
            // @ts-ignore
            return this.cacheMap[type];
        } else {
            // @ts-ignore
            return this.cacheMap[type] = this.getByTypeFromServer(type).pipe(
                shareReplay(1),
                catchError(() => {
                    // @ts-ignore
                    delete this.cacheMap[type];
                    return EMPTY;
                }));
        }
    }

    public getByTypeFromServer(type: EnumsType): Observable<SelectItemWithOrder<any>[]> {
        return this.http.get<SelectItemWithOrder<any>[]>(`${mainServiceUrl}/${type}`);
    }

    public loadcache() {
        Object.keys(EnumsType).map(key => {
            // @ts-ignore
            const enumname: EnumsType = EnumsType[key];
            this.getByTypeFromServer(enumname)
                .subscribe({next:value => {
                        this.cacheMap.set(enumname, value);
                    },error:error => {
                        console.log('Error cache: ' + error);
                    }});
        });
    }


    resolveLabel(enumName: EnumsType, value: string): string {
        const enumerador = this.cacheMap.get(enumName).filter(
            enume => {
                return enume.value === value;
            });
        if (enumerador && enumerador.length > 0) {
            return enumerador[0].label;
        } else {
            return value;
        }
    }
    resolveEnum(enumName: EnumsType, value: string): any {
        const enumerador = this.cacheMap.get(enumName).filter(
            enume => {
                return enume.value === value;
            });
        if (enumerador && enumerador.length > 0) {
            return enumerador[0]
        } else {
            return null;
        }
    }

    resolveEnumWeb(enumName: EnumsType, value: string): EnumWeb {
        const enumerador = this.cacheMap.get(enumName).filter(
            enume => {
                return enume.value === value;
            });
        if (enumerador && enumerador.length > 0) {
            return enumerador[0] as EnumWeb
        } else {
            return null;
        }
    }
}
