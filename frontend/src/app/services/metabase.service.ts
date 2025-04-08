import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { LogService } from './log.service';


const mainServiceUrl = environment.base_url + '/';

@Injectable({
    providedIn: 'root'
})
export class MetabaseService  {

    metabaseIframeUrl: SafeResourceUrl;

    constructor(private http: HttpClient,
        private sanitizer: DomSanitizer,
        private logService: LogService) {
    }
    

    public getMetabaseToken(): Observable<{ iframeUrl: SafeResourceUrl }> {
        
        return this.http.get<{ iframeUrl: string }>(`${mainServiceUrl}metabase-token/`)
        .pipe(
        map(response => {
            if(response.iframeUrl && response.iframeUrl == null) {
                this.logService.error('Error al obtener el token de Metabase: no está configurado correctamente las propiedades de la aplicación');
                return response;}
            return { iframeUrl: this.sanitizer.bypassSecurityTrustResourceUrl(response.iframeUrl) };
        })
        );
   }   

}
