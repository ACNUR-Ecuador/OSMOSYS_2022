import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor, HttpResponse
} from '@angular/common/http';
import {LoaderService} from '../../services/loader.service';
import {catchError, map, mergeMap, switchMap, takeWhile, tap} from 'rxjs/operators';
import {interval, Observable, of, throwError} from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { LogService } from 'src/app/services/log.service';
import {environment} from '../../../environments/environment';


@Injectable()
export class LoaderInterceptor implements HttpInterceptor {
    constructor(private loaderService: LoaderService, private http: HttpClient, private logService: LogService) {
    }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

        if (request.url.includes('jobStatus')) {
            this.logService.log('Evitando intercept de request por ser jobStatus');
            return next.handle(request);
        }                        

        this.loaderService.setLoading(true, request.url);

        // Activa el loader para esta petición.
        return next.handle(request)
            .pipe(catchError((err) => {
                this.loaderService.setLoading(false, request.url);
                this.logService.log('cerrando loader por ser error');

                return throwError(err);
            }),
            mergeMap((evt: HttpEvent<any>) => {
                if (evt instanceof HttpResponse) {
                    // Verificamos si el response indica procesamiento asíncrono.
                    // Se asume que el body incluye una propiedad 'jobId' si el proceso es asíncrono.
                    if (evt.body && (evt.body.jobId)) {
                        this.logService.log('Iniciando reques asincrono');
                        this.loaderService.setLoading(true, request.url);

                        const jobId = evt.body.jobId;
                        var base_url = environment.base_url;
                        var urlJobStatus = `${base_url}/jobStatus/${jobId}`;

                        // Inicia el polling al endpoint de jobStatus cada 2 segundos.
                        return interval(2000).pipe(
                            switchMap(() =>
                                this.http.get<any>(urlJobStatus)
                            ),

                            // Continúa el polling mientras el progreso sea menor a 100 y el estado no sea "Completado".
                            takeWhile(status => status.progress < 100 && status.state !== 'Completado', true),
                            tap(status => {
                                this.loaderService.updateProgress(urlJobStatus, status.progress, status.state);
                                this.logService.log('progresando - ' + status.progress);
                            }),
                            map(finalStatus => {
                                this.logService.log('mapeando loader');

                                // Sólo se cierra el loader cuando se cumple la condición de finalización.
                                if (finalStatus.state === 'Completado' || finalStatus.progress >= 100) {
                                    this.loaderService.setLoading(false, request.url);
                                    this.logService.log('cerrando loader por finalizar Job');
                                }
                                return new HttpResponse({ body: finalStatus });
                            })
                        );
                    } else {
                        this.loaderService.setLoading(false, request.url);
                        this.logService.log('cerrando loader por terminar request sincrono');
                                            
                        return of(evt);
                    }
                }
                return of(evt);
            })
        );
    }
}
