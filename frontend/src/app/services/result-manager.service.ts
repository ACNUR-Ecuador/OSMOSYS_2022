import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { QuarterPopulationTypeConfirmation, ResultManagerIndicator, ResultManagerIndicatorQuarterReport } from '../shared/model/OsmosysModel';
import { Observable } from 'rxjs';

const mainServiceUrl = environment.base_url + '/resultManagerIndicators';

@Injectable({
  providedIn: 'root'
})
export class ResultManagerService {

  constructor(private http: HttpClient) { }

  public getAll(periodId:number, userId:number): Observable<ResultManagerIndicator[]> {
          return this.http.get<ResultManagerIndicator[]>(`${mainServiceUrl}/${periodId}/${userId}`);
      }
  public save(rmi: QuarterPopulationTypeConfirmation): Observable<number> {
          return this.http.post<number>(`${mainServiceUrl}`, rmi);
      }
  
  public update(rmi: QuarterPopulationTypeConfirmation): Observable<number> {
          return this.http.put<number>(`${mainServiceUrl}`, rmi);
      }
  public saveIndicatorQuarterReport(rmiqp: ResultManagerIndicatorQuarterReport): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}/quarterReport`, rmiqp);
      }
  public updateIndicatorQuarterReport(rmiqp: ResultManagerIndicatorQuarterReport): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}/quarterReport`, rmiqp);
      }

}
