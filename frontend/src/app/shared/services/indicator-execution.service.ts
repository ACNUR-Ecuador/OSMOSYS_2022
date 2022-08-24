import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {
    Canton,
    IndicatorExecution, IndicatorExecutionAssigment, IndicatorValue, MonthValues, Quarter,
    TargetUpdateDTOWeb
} from '../model/OsmosysModel';
import {HttpClient, HttpParams} from '@angular/common/http';

const mainServiceUrl = environment.base_url + '/indicatorExecutions';

@Injectable({
    providedIn: 'root'
})
export class IndicatorExecutionService {

    constructor(private http: HttpClient) {
    }

    public getGeneralIndicatorAdministrationResume(projectId: number):
        Observable<IndicatorExecution[]> {
        return this.http.get<IndicatorExecution[]>(`${mainServiceUrl}/generalAdmin/${projectId}`);
    }

    public getGeneralIndicatorResume(projectId: number):
        Observable<IndicatorExecution[]> {
        return this.http.get<IndicatorExecution[]>(`${mainServiceUrl}/generalByProjectId/${projectId}`);
    }

    public getPerformanceIndicatorAdministrationResume(projectId: number):
        Observable<IndicatorExecution[]> {
        return this.http.get<IndicatorExecution[]>(`${mainServiceUrl}/performanceAdminByProject/${projectId}`);
    }

    public getPerformanceIndicatorResume(projectId: number):
        Observable<IndicatorExecution[]> {
        return this.http.get<IndicatorExecution[]>(`${mainServiceUrl}/performanceByProjectId/${projectId}`);
    }

    public updateTargets(targetUpdateDTOWeb: TargetUpdateDTOWeb): Observable<void> {
        return this.http.put<void>(`${mainServiceUrl}/targetsUpdate`, targetUpdateDTOWeb);
    }

    public quartersTargetUpdate(quarters: Quarter[]): Observable<void> {
        return this.http.put<void>(`${mainServiceUrl}/quartersTargetUpdate`, quarters);
    }

    public assignPerformanceIndicatorDirectImplementation(indicatorExecutionAssigment: IndicatorExecutionAssigment): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}/assignPerformanceIndicatorDirectImplementation`, indicatorExecutionAssigment);
    }

    // tslint:disable-next-line:max-line-length
    public updateAssignPerformanceIndicatorDirectImplementation(indicatorExecutionAssigment: IndicatorExecutionAssigment): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}/updateAssignPerformanceIndicatorDirectImplementation`, indicatorExecutionAssigment);
    }

    public assignPerformanceIndicatoToProject(indicatorExecutionAssigment: IndicatorExecutionAssigment): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}/assignPerformanceIndicatoToProject`, indicatorExecutionAssigment);
    }

    public updateAssignPerformanceIndicatoToProject(indicatorExecutionAssigment: IndicatorExecutionAssigment): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}/updateAssignPerformanceIndicatoToProject`, indicatorExecutionAssigment);
    }

    public getResumeAdministrationPerformanceIndicatorById(
        indicatorExecutionId: number): Observable<IndicatorExecution> {
        // tslint:disable-next-line:max-line-length
        return this.http.post<IndicatorExecution>(`${mainServiceUrl}/getResumeAdministrationPerformanceIndicatorById`, indicatorExecutionId);
    }

    public updateMonthValues(indicatorExecutionId: number, monthValues: MonthValues): Observable<number> {
        const monthValuesP = {
            month: undefined,
            indicatorValuesMap: undefined,
            customDissagregationValues: undefined
        };
        monthValuesP.month = monthValues.month;
        const convMap = {};
        const monthValuesMapP: Map<string, IndicatorValue[]> = monthValues.indicatorValuesMap;
        monthValuesMapP.forEach((value, key) => {
            convMap[key] = value;
        });
        monthValuesP.indicatorValuesMap = convMap;
        monthValuesP.customDissagregationValues = monthValues.customDissagregationValues;
        return this.http.put<number>(`${mainServiceUrl}/updateMonthValues/${indicatorExecutionId}`, monthValuesP);
    }

    public getPerformanceAllDirectImplementationByPeriodId(
        periodId: number): Observable<IndicatorExecution[]> {
        // tslint:disable-next-line:max-line-length
        return this.http.get<IndicatorExecution[]>(`${mainServiceUrl}/performanceAllDirectImplementationByPeriodId/${periodId}`);
    }

    public getDirectImplementationIndicatorExecutionsByIds(indicatorExecutionIds: number[]): Observable<IndicatorExecution[]> {
        return this.http.post<IndicatorExecution[]>(`${mainServiceUrl}/getDirectImplementationIndicatorExecutionsByIds`,
            indicatorExecutionIds);
    }

    public getDissagregationsAssignationsByIndicatorExecutionId(indicatorExecutionId: number): Observable<string[]> {
        return this.http.get<string[]>(`${mainServiceUrl}/getDissagregationsAssignationsByIndicatorExecutionId/${indicatorExecutionId}`);
    }

    // tslint:disable-next-line:max-line-length
    public updateDirectImplementationIndicatorExecutionLocationAssigment(indicatorExecutionId: number, cantones: Canton[]): Observable<any> {
        return this.http
            .post(`${mainServiceUrl}/updateDirectImplementationIndicatorExecutionLocationAssigment/${indicatorExecutionId}`, cantones);
    }

    public getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId(
        userId: number,
        periodId: number,
        officeId: number,
        supervisor: boolean,
        responsible: boolean,
        backup: boolean,
    ): Observable<IndicatorExecution[]> {
        // Initialize Params Object
        let params = new HttpParams();
        // Begin assigning parameters
        params = params.append('userId', String(userId ? userId : null));
        params = params.append('periodId', String(periodId ? periodId : null));
        params = params.append('officeId', String(officeId ? officeId : null));
        params = params.append('supervisor', String(supervisor ? supervisor : false));
        params = params.append('responsible', String(responsible ? responsible : false));
        params = params.append('backup', String(backup ? backup : false));
        return this.http.get<IndicatorExecution[]>(`${mainServiceUrl}/getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId`, {params});
    }
}
