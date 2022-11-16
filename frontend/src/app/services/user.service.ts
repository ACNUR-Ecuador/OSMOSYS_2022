import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {User} from '../shared/model/User';
import jwtDecode from 'jwt-decode';
import {NgxPermissionsService} from 'ngx-permissions';
import {MessageService} from 'primeng/api';
import {Router} from '@angular/router';

const mainServiceUrl = environment.base_url + '/authentication';
const app_code = environment.app_code;

@Injectable({
    providedIn: 'root'
})
export class UserService {
    // @ts-ignore
    public currentUserSubject: BehaviorSubject<User> = new BehaviorSubject<User>(null);

    constructor(private http: HttpClient,
                private router: Router,
                private messageService: MessageService,
                private ngxPermissionsService: NgxPermissionsService) {
        this.setUser();


        this.currentUserSubject.subscribe(value => {
            if (value && value.roles) {
                const roles: string[] = value.roles.filter(role => {
                    return role.state === 'ACTIVO';
                }).map(role => {
                    return role.name;
                });
                ngxPermissionsService.loadPermissions(roles);
            }
        });
    }

    public login(user: User) {
        return this.http.post(`${mainServiceUrl}/login`, user)
            .pipe(
                tap(() => {
                    this.setUser();
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
                // @ts-ignore
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
            user.focalPointProjects = decToken.focalPointProjects;
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

    logout() {
        if (this.currentUserSubject) {
            // @ts-ignore
            this.currentUserSubject.next(null);
        }
        this.removeToken();
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigateByUrl('/auth/login');
    }

    public removeToken() {
        localStorage.removeItem(`${app_code}_token`);
    }

    public setToken(token: string) {
        localStorage.setItem(`${app_code}_token`, token);
    }

    public getActiveUNHCRUsers() {
        return this.http.get<User[]>(`${mainServiceUrl}/users/active/UNHCR`);
    }

    public getAllUser() {
        return this.http.get<User[]>(`${mainServiceUrl}/users`);
    }

    public getById(userId: number) {
        return this.http.get<User>(`${mainServiceUrl}/users/${userId}`);
    }

    public createUser(user: User): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}/users`, user);
    }

    public updateUser(user: User): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}/users`, user);
    }

    recoverPassword(email: string) {
        const changeRequest = {
            oldPassword: null,
            newPassword: email
        };
        return this.http.post(`${mainServiceUrl}/recoverpassword`, changeRequest);
    }

    public getLogedUsername(): User {
        return this.currentUserSubject.value;
    }

    changePassword(oldPassword: string, newPassword: string) {
        const changeRequest = {
            oldPassword,
            newPassword
        };
        return this.http.post(`${mainServiceUrl}/changepassword`, changeRequest);


    }

    hasRole(role: string): boolean {
        const user = this.currentUserSubject.value;
        let result = false;
        if (user && user.roles && user.roles.length > 0) {
            // @ts-ignore
            user.roles.forEach(roleU => {
                if (roleU.name.toUpperCase() === role.toUpperCase()) {
                    result = true;
                }
            });
        }
        return result;
    }

    public hasAnyRole(roles: string[]): boolean {
        let result = false;
        const user = this.currentUserSubject.value;
        if (user && user.roles && user.roles.length > 0) {
            user.roles.forEach(roleU => {
                if (roles && roles.length > 0) {
                    roles.forEach(roleAsked => {
                        if (roleU.name.toUpperCase() === roleAsked.toUpperCase()) {
                            result = true;
                        }
                    });
                }
            });
        }
        return result;

    }

    public isUNHCRUser(): boolean {
        const user = this.currentUserSubject.value;
        if (!user) {
            return false;
        }
        return user.organization === null || user.organization.acronym === 'ACNUR' || user.organization.acronym === 'UNHCR';
    }
}
