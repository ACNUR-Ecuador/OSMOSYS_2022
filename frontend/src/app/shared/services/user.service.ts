import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {BehaviorSubject} from 'rxjs';
import {User} from '../model/User';
import jwtDecode from 'jwt-decode';
import {tap} from 'rxjs/operators';
import {NgxPermissionsService} from 'ngx-permissions';

const mainServiceUrl = environment.base_url + '/authentication';
const app_code = environment.app_code;

@Injectable({
    providedIn: 'root'
})
export class UserService {

    public currentUserSubject: BehaviorSubject<User> = new BehaviorSubject(null);

    constructor(private http: HttpClient,
                private router: Router,
                private messageService: MessageService,
                private ngxPermissionsService: NgxPermissionsService
    ) {
        this.setUser();


        this.currentUserSubject.subscribe(value => {
            if (value) {
                const roles: string[] = value.roles.filter(role => {
                    return role.state === 'ACTIVO';
                }).map(role => {
                    return role.name;
                });
                ngxPermissionsService.loadPermissions(roles);
                // ngxPermissionsService.addPermission('tester');
            }
        });
    }

    public login(user: User, remember: boolean = false) {
        return this.http.post(`${mainServiceUrl}/login`, user)
            .pipe(
                tap(resp => {
                    const userF = this.setUser();
                    this.setRemember(remember, userF);
                })
            );
    }

    public setUser() {
        const token = this.getToken();
        let decToken: any;
        let user = null;
        if (token) {


            decToken = jwtDecode(token);
            const exp = decToken.exp;
            // verifico expiración token
            if (Date.now() >= exp * 1000) {
                this.currentUserSubject = new BehaviorSubject<User>(null);
                this.logout();
                this.messageService.add({
                    severity: 'error',
                    summary: 'Su sesión a caducado',
                    detail: 'Por favor vuelva a ingresar al sistema'
                });
                return;
            }
            user = new User();
            user.id = decToken.id;
            user.username = decToken.sub;
            user.name = decToken.name;
            user.email = decToken.email;
            user.roles = decToken.roles;
            user.organization = decToken.organization;
            user.office = decToken.office;
            this.currentUserSubject.next(user);
        } else {
            this.logout();
            return;
        }

        return user;
    }

    public getToken() {
        return localStorage.getItem(`${app_code}_token`);
    }

    public setToken(token: string) {
        localStorage.setItem(`${app_code}_token`, token);
    }

    logout() {
        if (this.currentUserSubject) {
            this.currentUserSubject.next(null);
        }
        this.removeToken();
        this.router.navigateByUrl('/login');
    }

    public removeToken() {
        localStorage.removeItem(`${app_code}_token`);
    }

    public setRemember(remember: boolean, user: User) {
        if (remember && user) {
            localStorage.setItem(`${app_code}_username`, user.username);
        }

    }
}
