import {Injectable} from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor, HttpHeaders
} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserService} from '../services/user.service';

@Injectable()
export class TokenRequestInterceptor implements HttpInterceptor {

    constructor(private userService: UserService) {
    }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        const token = this.userService.getToken();
        if (token) {


            const modifiedReq = request.clone({
                headers: request.headers.set('Authorization', `Bearer ${token}`),
            });
            return next.handle(modifiedReq);
        }
        return next.handle(request);
    }
}
