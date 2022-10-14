import {ChangeDetectorRef, Component} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {UserService} from '../../shared/services/user.service';
import {GoogleAnalyticsService} from 'ngx-google-analytics';

@Component({
    selector: 'app-login',
    templateUrl: './app.login.component.html',
})
export class AppLoginComponent {
    public loginForm = this.fb.group({
        username: ['', [Validators.required]],
        password: ['', [Validators.required]]
    });
    submitted = false;

    constructor(private router: Router,
                private fb: FormBuilder,
                private userService: UserService,
                private messageService: MessageService,
                private cd: ChangeDetectorRef,
                private $gaService: GoogleAnalyticsService) {
    }

    login() {
        this.submitted = true;
        this.messageService.clear();
        if (this.loginForm.invalid) {
            return;
        } else {
            this.userService.login(this.loginForm.value).subscribe(() => {
                this.$gaService.event('login_success', 'login_success', this.loginForm.get('username').value);
                this.router.navigateByUrl('/');
            }, error => {
                if (error.status === 0) {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Al momento estamos realizando mantenimiento del sistema. Por favor vuelve a intentar despues de 30 minutos'
                    });
                } else {
                    this.messageService.add({severity: 'error', summary: 'Usuario o contraseña incorrectos'});
                }
            });
        }
    }
}
