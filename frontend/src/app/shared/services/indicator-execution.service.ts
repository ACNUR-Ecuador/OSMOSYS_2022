import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {
    IndicatorExecution, IndicatorExecutionAssigment, IndicatorValue, MonthValues,
    TargetUpdateDTOWeb
} from '../model/OsmosysModel';
import {HttpClient} from '@angular/common/http';

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
}
