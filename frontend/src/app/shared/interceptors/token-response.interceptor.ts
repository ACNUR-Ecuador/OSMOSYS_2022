import {Injectable} from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor, HttpResponse, HttpErrorResponse
} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {UserService} from '../../services/user.service';
import {ConfirmationService} from 'primeng/api';
import {Router} from '@angular/router';

@Injectable()
export class TokenResponseInterceptor implements HttpInterceptor {

    constructor(private userService: UserService, private confirmationService: ConfirmationService, private router: Router) {
    }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        return next.handle(request)
            .pipe(
                tap(evt => {
                    if (evt instanceof HttpResponse) {
                        if (evt.headers.get('refresh-token')) {
                            this.userService.setToken(evt.headers.get('refresh-token') as string);
                        }
                    }
                }, err => {
                    if (err instanceof HttpErrorResponse && this.router.routerState.snapshot.url !== '/login') {
                        if (err.status === 401) {
                            this.confirmationService.confirm(
                                {
                                    message: 'Tu sesión ha caducado, por favor vuelve a ingresar al sistema',
                                    header: 'Sesión Caducada',
                                    icon: 'pi pi-exclamation-triangle',
                                    acceptVisible: true,
                                    rejectVisible: false,
                                    closeOnEscape: true,
                                    acceptLabel: 'Ok',
                                    accept: () => {
                                        this.userService.logout();
                                    },
                                    reject: () => {
                                        this.userService.logout();
                                    }
                                }
                            );
                        } else {
                            return;
                        }
                    }
                })
            );
    }
}
