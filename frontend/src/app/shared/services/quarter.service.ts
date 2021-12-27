import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {Project, QuarterResumeWeb, StartEndDatesWeb} from '../model/OsmosysModel';
import {HttpClient, HttpParams} from '@angular/common/http';


const mainServiceUrl = environment.base_url + '/quarters';

@Injectable({
    providedIn: 'root'
})
export class QuarterService {

    constructor(private http: HttpClient) {
    }

    public getEmptyQuarters(startEndDatesWeb: StartEndDatesWeb): Observable<QuarterResumeWeb[]> {

        return this.http.post<QuarterResumeWeb[]>(`${mainServiceUrl}/getEmptyQuarters`,startEndDatesWeb);
    }
}
