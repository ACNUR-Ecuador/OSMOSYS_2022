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
        return this.http.get(`${mainServiceUrl}/getAllImplementationsPerformanceIndicatorsAnnualByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllImplementationsPerformanceIndicatorsDetailedByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/getAllImplementationsPerformanceIndicatorsDetailedByPeriodId/${periodId}`, {
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

    public getPartnerAnnualByProjectId(projectId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnerAnnualByProjectId/${projectId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnerQuarterlyByProjectId(projectId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnerQuarterlyByProjectId/${projectId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnerMonthlyByProjectId(projectId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnerMonthlyByProjectId/${projectId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnerDetailedByProjectId(projectId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnerDetailedByProjectId/${projectId}`, {
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


    /****************indicators catalog***********/
    public getIndicatorsCatalogByPeriodId(periodId: number) {
        return this.http.get(`${mainServiceUrl}/indicatorsCatalogByPeriodId/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getIndicatorsCatalogWithImplementersSimple(periodId: number) {
        return this.http.get(`${mainServiceUrl}/indicatorsCatalogWithImplementersSimple/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getIndicatorsCatalogWithImplementersDetailed(periodId: number) {
        return this.http.get(`${mainServiceUrl}/indicatorsCatalogWithImplementersDetailed/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }


    /********************LATE REPORTS******************/
    public getFocalPointLateReviewReport(focalPointId: number) {
        return this.http.get(`${mainServiceUrl}/getFocalPointLateReviewReport/${focalPointId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getPartnerLateReportByProjectId(projectId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnerLateReportByProjectId/${projectId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }
    public getPartnerLateReviewByProjectId(projectId: number) {
        return this.http.get(`${mainServiceUrl}/getPartnerLateReviewByProjectId/${projectId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getFocalPointLateReport(focalPointId: number) {
        return this.http.get(`${mainServiceUrl}/getFocalPointLateReport/${focalPointId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getResponsableLateReport(responsableId: number) {
        return this.http.get(`${mainServiceUrl}/getResponsableLateReport/${responsableId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getSupervisorLateReviewReport(supervisorId: number) {
        return this.http.get(`${mainServiceUrl}/getSupervisorLateReviewReport/${supervisorId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllLateReviewReportDirectImplementation() {
        return this.http.get(`${mainServiceUrl}/getAllLateReviewReportDirectImplementation`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllLateReportDirectImplementation() {
        return this.http.get(`${mainServiceUrl}/getAllLateReportDirectImplementation`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }

    public getAllLateReportPartners() {
        return this.http.get(`${mainServiceUrl}/getAllLateReportPartners`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }
    public getAllLateReviewPartners() {
        return this.http.get(`${mainServiceUrl}/getAllLateReviewPartners`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }
}
