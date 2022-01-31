import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Project, ProjectResume} from '../model/OsmosysModel';
import {EnumsState} from '../model/UtilsModel';
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

}
