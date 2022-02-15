import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';


const mainServiceUrl = environment.base_url + '/reports';

@Injectable({
    providedIn: 'root'
})
export class ReportsService {

    constructor(private http: HttpClient) {
    }

    public getAllPartnertsStateReport(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllPartnertsStateReport/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }
}
