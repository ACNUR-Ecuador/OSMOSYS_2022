import {ChangeDetectorRef, Component} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {UserService} from '../../shared/services/user.service';

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
                private cd: ChangeDetectorRef) {
    }

    login() {
        this.submitted = true;
        if (this.loginForm.invalid) {
            return;
        } else {
            this.userService.login(this.loginForm.value).subscribe(value => {
                this.router.navigateByUrl('/');
            }, error => {
                console.error(error.error.message);
                if (error.status === 0) {
                    this.messageService.add({severity: 'error', summary: 'Al momento estamos realizando mantenimiento del sistema. Por favor vuelve a intentar despues de 30 minutos'});
                } else {
                    this.messageService.add({severity: 'error', summary: 'Usuario o contraseña incorrectos'});
                }
            });
        }
    }
}
