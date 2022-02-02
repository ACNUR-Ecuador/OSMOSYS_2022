import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor, HttpResponse
} from '@angular/common/http';
import {LoaderService} from '../services/loader.service';
import {catchError, map} from 'rxjs/operators';
import {Observable, throwError} from 'rxjs';

@Injectable()
export class LoaderInterceptor implements HttpInterceptor {
    constructor(private loaderService: LoaderService) {
    }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        this.loaderService.setLoading(true, request.url);
        console.log('loading true');
        return next.handle(request)
            .pipe(catchError((err) => {
                this.loaderService.setLoading(false, request.url);
                console.log('loading false');
                return throwError(err);
            }))
            .pipe(map<HttpEvent<any>, any>((evt: HttpEvent<any>) => {
                if (evt instanceof HttpResponse) {
                    this.loaderService.setLoading(false, request.url);
                    console.log('loading false');
                }
                return evt;
            }));
    }
}
