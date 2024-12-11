import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Audit } from '../shared/model/OsmosysModel';
import { aU } from '@fullcalendar/core/internal-common';
import { EnumsState } from '../shared/model/UtilsModel';

const mainServiceUrl = environment.base_url + '/audits';

@Injectable({
  providedIn: 'root'
})
export class AuditService {

  constructor(private http: HttpClient) { }

  public getAll(): Observable<Audit[]> {
    return this.http.get<Audit[]>(`${mainServiceUrl}`);
  }

  public save(audit: Audit): Observable<number> {
      return this.http.post<number>(`${mainServiceUrl}`, audit);
  }

  public update(audit: Audit): Observable<number> {
      return this.http.put<number>(`${mainServiceUrl}`, audit);
  }

  public getByState(state: EnumsState): Observable<Audit[]> {
      return this.http.get<Audit[]>(`${mainServiceUrl}/byState/${state}`);
  }

  public getAuditsByTableName(tableName: string): Observable<Audit[]>{
    return this.http.get<Audit[]>(`${mainServiceUrl}/getAuditsByTableName/${tableName}`)
  }

  public getAuditsByTableNameAndDate(tableName: string, year: number, month:number): Observable<Audit[]>{
    return this.http.get<Audit[]>(`${mainServiceUrl}/getAuditsByTableNameAndDate/${tableName}/${year}/${month}`)
  }

}
