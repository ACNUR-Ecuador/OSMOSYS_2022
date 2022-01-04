import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {EMPTY, Observable} from 'rxjs';
import {EnumsType, MonthType, SelectItemWithOrder} from '../model/UtilsModel';
import {HttpClient} from '@angular/common/http';
import {catchError, shareReplay} from 'rxjs/operators';

const mainServiceUrl = environment.base_url + '/enums';


@Injectable({
    providedIn: 'root'
})
export class EnumsService {

    public cacheMap = new Map<EnumsType, SelectItemWithOrder<any>[]>();

    constructor(private http: HttpClient) {
    }

    public getByType(type: EnumsType): Observable<SelectItemWithOrder<any>[]> {
        if (this.cacheMap[type]) {
            console.log('Returning cached value!');
            return this.cacheMap[type];
        } else {

            console.log('Do the request again');
            return this.cacheMap[type] = this.getByTypeFromServer(type).pipe(
                shareReplay(1),
                catchError(err => {
                    delete this.cacheMap[type];
                    return EMPTY;
                }));
        }
    }

    public getByTypeFromServer(type: EnumsType): Observable<SelectItemWithOrder<any>[]> {
        return this.http.get<SelectItemWithOrder<any>[]>(`${mainServiceUrl}/${type}`);
    }

    public loadcache() {
        console.log('loading cache');
        Object.keys(EnumsType).map(key => {
            const enumname: EnumsType = EnumsType[key];
            this.getByTypeFromServer(enumname).subscribe(value => {
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

    monthTypeToNumber(month: MonthType): number {
        const monthV = MonthType[month] as MonthType;
        switch (monthV) {
            case MonthType.ENERO: {
                return 1;
            }
            case MonthType.FEBRERO: {
                return 2;
            }
            case MonthType.MARZO: {
                return 3;
            }
            case MonthType.ABRIL: {
                return 4;
            }
            case MonthType.MAYO: {
                return 5;
            }
            case MonthType.JUNIO: {
                return 6;
            }
            case MonthType.JULIO: {
                return 7;
            }
            case MonthType.AGOSTO: {
                return 8;
            }
            case MonthType.SEPTIEMBRE: {
                return 9;
            }
            case MonthType.OCTUBRE: {
                return 10;
            }
            case MonthType.NOVIEMBRE: {
                return 11;
            }
            case MonthType.DICIEMBRE: {
                return 12;
            }
        }
    }


}
