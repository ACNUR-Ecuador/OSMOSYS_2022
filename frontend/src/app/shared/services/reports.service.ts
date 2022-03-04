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

    public getAllImplementationsAnnualByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllImplementationsAnnualByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllImplementationsQuarterlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllImplementationsQuarterlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllImplementationsMonthlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllImplementationsMonthlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllImplementationsDetailedByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllImplementationsDetailedByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllImplementationsPerformanceIndicatorsAnnualByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllImplementationsAnnualByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllImplementationsQuarterlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllImplementationsMonthlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllImplementationsPerformanceIndicatorsDetailedByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllImplementationsDetailedByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnersAnnualByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnersAnnualByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnersQuarterlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnersQuarterlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnersMonthlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnersMonthlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnersDetailedByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnersDetailedByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnersGeneralIndicatorsAnnualByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnersGeneralIndicatorsAnnualByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnersGeneralIndicatorsMonthlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnersGeneralIndicatorsMonthlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnersGeneralIndicatorsDetailedByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnersGeneralIndicatorsDetailedByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }
    public getPartnersPerformanceIndicatorsAnnualByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnersPerformanceIndicatorsAnnualByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnersPerformanceIndicatorsQuarterlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnersPerformanceIndicatorsQuarterlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnersPerformanceIndicatorsMonthlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnersPerformanceIndicatorsMonthlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }
    public getPartnersPerformanceIndicatorsDetailedByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnersPerformanceIndicatorsDetailedByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getDirectImplementationPerformanceIndicatorsAnnualByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getDirectImplementationPerformanceIndicatorsAnnualByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }
    public getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getDirectImplementationPerformanceIndicatorsDetailedByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }


}
