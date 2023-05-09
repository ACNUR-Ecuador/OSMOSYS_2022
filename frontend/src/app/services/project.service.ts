import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ImportFile, MonthState, Project, ProjectResume, QuarterState} from '../shared/model/OsmosysModel';
import {EnumsState} from '../shared/model/UtilsModel';
import {map} from 'rxjs/operators';

const mainServiceUrl = environment.base_url + '/projects';

@Injectable({
    providedIn: 'root'
})
export class ProjectService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<Project[]> {
        return this.http.get<Project[]>(`${mainServiceUrl}`);
    }

    public save(project: Project): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, project);
    }

    public update(project: Project): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, project);
    }

    public getByState(state: EnumsState): Observable<Project[]> {
        return this.http.get<Project[]>(`${mainServiceUrl}/byState/${state}`);
    }

    public getProjectResumenWebByPeriodId(periodId: number): Observable<ProjectResume[]> {
        return this.http.get<ProjectResume[]>(`${mainServiceUrl}/getProjectResumenWebByPeriodId/${periodId}`);
    }

    public getProjectResumenWebByPeriodIdAndFocalPointId(periodId: number, focalPointId: number): Observable<ProjectResume[]> {
        // tslint:disable-next-line:max-line-length
        return this.http.get<ProjectResume[]>(`${mainServiceUrl}/getProjectResumenWebByPeriodIdAndFocalPointId/${periodId}/${focalPointId}`);
    }

    public getProjectResumenWebByPeriodIdAndOrganizationId(periodId: number, organizationId: number): Observable<ProjectResume[]> {
        return this.http.get<ProjectResume[]>(`${mainServiceUrl}/getProjectResumenWebByPeriodIdAndOrganizationId/${periodId}/${organizationId}`);
    }

    public getProjectById(projectId: number): Observable<Project> {
        return this.http.get<Project>(`${mainServiceUrl}/${projectId}`).pipe(
            map(value => {
                value.startDate = new Date(value.startDate);
                value.endDate = new Date(value.endDate);
                return value;
            })
        );
    }

    public getQuartersStateByProjectId(projectId: number): Observable<QuarterState[]> {
        return this.http.get<QuarterState[]>(`${mainServiceUrl}/getQuartersStateByProjectId/${projectId}`);
    }

    public getMonthsStateByProjectId(projectId: number): Observable<MonthState[]> {
        return this.http.get<MonthState[]>(`${mainServiceUrl}/getMonthsStateByProjectId/${projectId}`);
    }

    public blockQuarterStateByProjectId(projectId: number, quarterState: QuarterState): Observable<QuarterState[]> {
        return this.http.post<QuarterState[]>(`${mainServiceUrl}/blockQuarterStateByProjectId/${projectId}`, quarterState);
    }

    public changeMonthStateByProjectId(projectId: number, monthState: MonthState): Observable<any> {
        return this.http.post(`${mainServiceUrl}/changeMonthStateByProjectId/${projectId}`, monthState);
    }

    public importCatalog(importFile: ImportFile) {
        return this.http.post(`${mainServiceUrl}/importCatalog`, importFile);
    }

    public getImportTemplate(periodId:number) {
        return this.http.get(`${mainServiceUrl}/getImportTemplateTotalTarget/${periodId}`, {
            observe: 'response',
            responseType: 'blob' as 'json'
        });
    }
}
