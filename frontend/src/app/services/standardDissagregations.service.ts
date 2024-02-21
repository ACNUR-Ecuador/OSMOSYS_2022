import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {EMPTY, Observable} from 'rxjs';
import {StandardDissagregationOption} from '../shared/model/OsmosysModel';
import {catchError, map, shareReplay} from "rxjs/operators";
import {EnumsType} from "../shared/model/UtilsModel";

const mainServiceUrl = environment.base_url + '/standardDissagregations';

@Injectable({
    providedIn: 'root'
})
export class StandardDissagregationsService {

    cacheMap = new Map<SimpleDissagregationEnum, StandardDissagregationOption[]>();


    constructor(private http: HttpClient) {
    }


    public getActiveAgeOptions(): Observable<StandardDissagregationOption[]> {
        return this.http.get<StandardDissagregationOption[]>(`${mainServiceUrl}/options/active/age`)
            .pipe(map(value => {
                value.forEach(value1 => value1.type = 'age')
                return value;
            }));
    }

    public getActiveGenderOptions(): Observable<StandardDissagregationOption[]> {
        return this.http.get<StandardDissagregationOption[]>(`${mainServiceUrl}/options/active/gender`)
            .pipe(map(value => {
                value.forEach(value1 => value1.type = 'gender')
                return value;
            }));
    }

    public getActivePopulationTypeOptions(): Observable<StandardDissagregationOption[]> {
        return this.http.get<StandardDissagregationOption[]>(`${mainServiceUrl}/options/active/populationType`)
            .pipe(map(value => {
                value.forEach(value1 => value1.type = 'population_type')
                return value;
            }));
    }


    public getActiveCountryOfOriginOptions(): Observable<StandardDissagregationOption[]> {
        return this.http.get<StandardDissagregationOption[]>(`${mainServiceUrl}/options/active/countryOfOrigin`)
            .pipe(map(value => {
                value.forEach(value1 => value1.type = 'country_of_origin')
                return value;
            }));
    }

    public getActiveDiversityOptions(): Observable<StandardDissagregationOption[]> {
        return this.http.get<StandardDissagregationOption[]>(`${mainServiceUrl}/options/active/diversity`)
            .pipe(map(value => {
                value.forEach(value1 => value1.type = 'diversity')
                return value;
            }));
    }

    public getByType(type: SimpleDissagregationEnum): Observable<StandardDissagregationOption[]> {
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

    public loadcache() {

        const enumValues = Object.values(SimpleDissagregationEnum);
        for (const key of enumValues) {
            this.getByTypeFromServer(key).subscribe({
                next: value => {
                    this.cacheMap.set(key, value);
                }, error: error => {
                    console.log('Error cache options: ' + error);
                }
            });
        }
    }

    private getByTypeFromServer(enumname: SimpleDissagregationEnum) {
        switch (enumname) {
            case SimpleDissagregationEnum.age:
                return this.getActiveAgeOptions();
            case SimpleDissagregationEnum.gender:
                return this.getActiveGenderOptions();
            case SimpleDissagregationEnum.country_of_origin:
                return this.getActiveCountryOfOriginOptions();
            case SimpleDissagregationEnum.population_type:
                return this.getActivePopulationTypeOptions();
            case SimpleDissagregationEnum.diversity:
                return this.getActiveDiversityOptions();
        }

    }

    resolveByDissagregationTypeName(simpleDissagregation: string): StandardDissagregationOption[] {
        switch (simpleDissagregation) {
            case "TIPO_POBLACION":
                return this.cacheMap.get(SimpleDissagregationEnum.population_type);
            case  "PAIS_ORIGEN":
                return this.cacheMap.get(SimpleDissagregationEnum.country_of_origin);
            case "DIVERSIDAD":
                return this.cacheMap.get(SimpleDissagregationEnum.diversity);
            case "EDAD":
                return this.cacheMap.get(SimpleDissagregationEnum.age);
            case "GENERO":
                return this.cacheMap.get(SimpleDissagregationEnum.gender);
            default:
                return undefined;

        }


    }

    resolveByDissagregationType(enumName: SimpleDissagregationEnum): StandardDissagregationOption[] {
        return this.cacheMap.get(enumName);
    }

    getByDissagregationId(id: number): StandardDissagregationOption {
        for (const options of this.cacheMap.values()) {
            const foundOption = options.find(option => option.id === id);
            if (foundOption) {
                return foundOption;
            }
        }
        return undefined; // Return undefined if not found

    }


}

export enum SimpleDissagregationEnum {
    age = 'age',
    gender = 'gender',
    population_type = 'population_type',
    diversity = 'diversity',
    country_of_origin = 'country_of_origin',

}
