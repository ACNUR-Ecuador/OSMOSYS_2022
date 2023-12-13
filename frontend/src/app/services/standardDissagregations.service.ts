import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Period, StandardDissagregationOption} from '../shared/model/OsmosysModel';
import {EnumsState} from '../shared/model/UtilsModel';

const mainServiceUrl = environment.base_url + '/standardDissagregations';

@Injectable({
    providedIn: 'root'
})
export class StandardDissagregationsService {

    constructor(private http: HttpClient) {
    }


    public getActiveAgeOptions(): Observable<StandardDissagregationOption[]> {
        return this.http.get<StandardDissagregationOption[]>(`${mainServiceUrl}/options/active/age`);
    }

    public getActiveGenderOptions(): Observable<StandardDissagregationOption[]> {
        return this.http.get<StandardDissagregationOption[]>(`${mainServiceUrl}/options/active/gender`);
    }

    public getActivePopulationTypeOptions(): Observable<StandardDissagregationOption[]> {
        return this.http.get<StandardDissagregationOption[]>(`${mainServiceUrl}/options/active/populationType`);
    }


    public getActiveCountryOfOriginOptions(): Observable<StandardDissagregationOption[]> {
        return this.http.get<StandardDissagregationOption[]>(`${mainServiceUrl}/options/active/countryOfOrigin`);
    }

    public getActiveDiversityOptions(): Observable<StandardDissagregationOption[]> {
        return this.http.get<StandardDissagregationOption[]>(`${mainServiceUrl}/options/active/diversity`);
    }

}
