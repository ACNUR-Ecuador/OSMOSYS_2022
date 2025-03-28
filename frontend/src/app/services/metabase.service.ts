import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';


const mainServiceUrl = environment.base_url + '/';

@Injectable({
    providedIn: 'root'
})
export class MetabaseService  {

    metabaseIframeUrl: SafeResourceUrl;

    constructor(private http: HttpClient,
        private sanitizer: DomSanitizer) {
    }
    

    public getMetabaseToken(): Observable<{ iframeUrl: SafeResourceUrl }> {
        
        return this.http.get<{ iframeUrl: string }>(`${mainServiceUrl}metabase-token`)
        .pipe(
        map(response => {
            return { iframeUrl: this.sanitizer.bypassSecurityTrustResourceUrl(response.iframeUrl) };
        })
        );
    }   

    public testItemLoadAsinc(): Observable<any> {
        return this.http.get<any>(`${mainServiceUrl}test/tester`);
    }
}
