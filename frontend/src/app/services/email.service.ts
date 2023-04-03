import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

const mainServiceUrl = environment.base_url + '/email';

@Injectable({
    providedIn: 'root'
})
export class EmailService {
    constructor(private http: HttpClient) {
    }

    public recordatoryPartners() {
        return this.http.get(`${mainServiceUrl}/recordatoryPartners`);
    }

    public recordatoryId() {
        return this.http.get(`${mainServiceUrl}/recordatoryId`);
    }

    public alertPartners() {
        return this.http.get(`${mainServiceUrl}/alertPartners`);
    }

    public alertsId() {
        return this.http.get(`${mainServiceUrl}/alertsId`);
    }
}
