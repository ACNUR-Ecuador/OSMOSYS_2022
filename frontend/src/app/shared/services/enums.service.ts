import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {EnumsType} from '../model/UtilsModel';
import {HttpClient} from '@angular/common/http';
import {SelectItem} from 'primeng/api';

const mainServiceUrl = environment.base_url + '/enums';


@Injectable({
    providedIn: 'root'
})
export class EnumsService {

    constructor(private http: HttpClient) {
    }

    public getByType(type: EnumsType): Observable<SelectItem[]> {
        return this.http.get<SelectItem[]>(`${mainServiceUrl}/${type}`);
    }
}
