import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {EMPTY, Observable, of} from 'rxjs';
import {EnumsType, MonthType} from '../model/UtilsModel';
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
