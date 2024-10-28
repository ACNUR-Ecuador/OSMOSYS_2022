import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Tag } from '../shared/model/OsmosysModel';
import { EnumsState } from '../shared/model/UtilsModel';


const mainServiceUrl = environment.base_url + '/tags';

@Injectable({
  providedIn: 'root'
})
export class TagService {

  constructor(private http: HttpClient) { }
  
  public getAll(): Observable<Tag[]> {
    return this.http.get<Tag[]>(`${mainServiceUrl}`);
  }

  public save(tag: Tag): Observable<number> {
      return this.http.post<number>(`${mainServiceUrl}`, tag);
  }

  public update(tag: Tag): Observable<number> {
      return this.http.put<number>(`${mainServiceUrl}`, tag);
  }

  public getByState(state: EnumsState): Observable<Tag[]> {
    return this.http.get<Tag[]>(`${mainServiceUrl}/byState/${state}`);
}

public getActiveByPeriodId(periodId: number): Observable<Tag[]> {
  return this.http.get<Tag[]>(`${mainServiceUrl}/getActiveByPeriodId/${periodId}`);
}



}
