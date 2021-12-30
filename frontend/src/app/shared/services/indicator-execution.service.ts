import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {
    Indicator, IndicatorExecutionAssigment,
    IndicatorExecutionGeneralIndicatorAdministrationResumeWeb,
    IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb, IndicatorExecutionResumeWeb,
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
        Observable<IndicatorExecutionGeneralIndicatorAdministrationResumeWeb[]> {
        return this.http.get<IndicatorExecutionGeneralIndicatorAdministrationResumeWeb[]>(`${mainServiceUrl}/generalAdmin/${projectId}`);
    }
    public getGeneralIndicatorResume(projectId: number):
        Observable<IndicatorExecutionResumeWeb[]> {
        return this.http.get<IndicatorExecutionResumeWeb[]>(`${mainServiceUrl}/general/${projectId}`);
    }

    public getPerformanceIndicatorAdministrationResume(projectId: number):
        Observable<IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb[]> {
        return this.http.get<IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb[]>(`${mainServiceUrl}/performanceAdminByProject/${projectId}`);
    }

    public updateTargets(targetUpdateDTOWeb: TargetUpdateDTOWeb): Observable<void> {
        return this.http.put<void>(`${mainServiceUrl}/targetsUpdate`, targetUpdateDTOWeb);
    }

    public assignPerformanceIndicatoToProject(indicatorExecutionAssigment: IndicatorExecutionAssigment): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}/assignPerformanceIndicatoToProject`, indicatorExecutionAssigment);
    }

    public getResumeAdministrationPerformanceIndicatorById(
        indicatorExecutionId: number): Observable<IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb> {
        return this.http.post<IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb>(`${mainServiceUrl}/getResumeAdministrationPerformanceIndicatorById`, indicatorExecutionId);
    }
}
