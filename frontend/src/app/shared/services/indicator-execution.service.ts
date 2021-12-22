import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {
    Indicator,
    IndicatorExecutionGeneralIndicatorAdministrationResumeWeb,
    IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb,
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
        return this.http.get<IndicatorExecutionGeneralIndicatorAdministrationResumeWeb[]>(`${mainServiceUrl}/general/${projectId}`);
    }

    public getPerformanceIndicatorAdministrationResume(projectId: number):
        Observable<IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb[]> {
        return this.http.get<IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb[]>(`${mainServiceUrl}/performanceByProject/${projectId}`);
    }

    public updateTargets(targetUpdateDTOWeb: TargetUpdateDTOWeb): Observable<void> {
        return this.http.put<void>(`${mainServiceUrl}/targetsUpdate`, targetUpdateDTOWeb);
    }
}
