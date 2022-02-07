import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';


const mainServiceUrl = environment.base_url + '/quarters';

@Injectable({
    providedIn: 'root'
})
export class QuarterService {

    constructor(private http: HttpClient) {
    }

}
