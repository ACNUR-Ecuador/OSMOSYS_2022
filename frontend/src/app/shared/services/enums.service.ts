import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {EMPTY, Observable} from 'rxjs';
import {DissagregationType, EnumsType, MonthType, SelectItemWithOrder} from '../model/UtilsModel';
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
            return this.cacheMap[type];
        } else {
            return this.cacheMap[type] = this.getByTypeFromServer(type).pipe(
                shareReplay(1),
                catchError(() => {
                    delete this.cacheMap[type];
                    return EMPTY;
                }));
        }
    }

    public getByDissagregationType(dissagregationType: DissagregationType): Observable<SelectItemWithOrder<any>[]> {
        if (!dissagregationType) {
            return null;
        }
        switch (dissagregationType) {
            case DissagregationType.EDAD:
                return this.getByType(EnumsType.AgeType);
            case DissagregationType.EDAD_EDUCACION_PRIMARIA:
                return this.getByType(EnumsType.AgePrimaryEducationType);
            case DissagregationType.EDAD_EDUCACION_TERCIARIA:
                return this.getByType(EnumsType.AgeTertiaryEducationType);
            case DissagregationType.GENERO:
                return this.getByType(EnumsType.GenderType);
            case DissagregationType.DIVERSIDAD:
                return this.getByType(EnumsType.DiversityType);
            case DissagregationType.TIPO_POBLACION:
                return this.getByType(EnumsType.PopulationType);
            case DissagregationType.PAIS_ORIGEN:
                return this.getByType(EnumsType.CountryOfOrigin);
            default:
                return null;
        }
    }

    public getByTypeFromServer(type: EnumsType): Observable<SelectItemWithOrder<any>[]> {
        return this.http.get<SelectItemWithOrder<any>[]>(`${mainServiceUrl}/${type}`);
    }

    public loadcache() {
        Object.keys(EnumsType).map(key => {
            const enumname: EnumsType = EnumsType[key];
            this.getByTypeFromServer(enumname).subscribe(value => {
                this.cacheMap.set(enumname, value);
            });
        });
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

    numberToMonthType(monthNumber: number): MonthType {
        switch (monthNumber) {
            case 1: {
                return MonthType.ENERO;
            }
            case 2: {
                return MonthType.FEBRERO;
            }
            case 3: {
                return MonthType.MARZO;
            }
            case 4: {
                return MonthType.ABRIL;
            }
            case 5: {
                return MonthType.MAYO;
            }
            case 6: {
                return MonthType.JUNIO;
            }
            case 7: {
                return MonthType.JULIO;
            }
            case 8: {
                return MonthType.AGOSTO;
            }
            case 9: {
                return MonthType.SEPTIEMBRE;
            }
            case 10: {
                return MonthType.OCTUBRE;
            }
            case 11: {
                return MonthType.NOVIEMBRE;
            }
            case 12: {
                return MonthType.DICIEMBRE;
            }
            default:
                return null;
        }
    }

}
