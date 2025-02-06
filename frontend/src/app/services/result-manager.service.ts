import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ResultManagerIndicator } from '../shared/model/OsmosysModel';
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

}
