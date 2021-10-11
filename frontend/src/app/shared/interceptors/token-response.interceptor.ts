import {Injectable} from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor, HttpResponse
} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {UserService} from '../services/user.service';

@Injectable()
export class TokenResponseInterceptor implements HttpInterceptor {

    constructor(private userService: UserService) {
    }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        return next.handle(request).pipe(
            tap(evt => {
                if (evt instanceof HttpResponse) {
                    // console.log(evt.headers);
                    if (evt.headers.get('refresh-token')) {
                        this.userService.setToken(evt.headers.get('refresh-token'));
                    }/* else {
                        this.userService.removeToken()
                    }*/
                }else{
                 // console.log(evt);
                }
            }));
    }
}
