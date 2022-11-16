import {Injectable} from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor
} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';


const app_code = environment.app_code;

@Injectable()
export class AppCodeInterceptor implements HttpInterceptor {

    constructor() {
    }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {


        const modifiedReq = request.clone({
            headers: request.headers.set('appCode', app_code),
        });
        return next.handle(modifiedReq);
    }
}
