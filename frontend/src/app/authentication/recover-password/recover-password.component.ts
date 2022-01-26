import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from '../../shared/services/user.service';
import {ConfirmationService, MessageService} from 'primeng/api';

@Component({
  selector: 'app-recover-password',
  templateUrl: './recover-password.component.html',
  styleUrls: ['./recover-password.component.scss']
})
export class RecoverPasswordComponent implements OnInit {

    public recoverPasswordForm = this.fb.group({
        email: ['', [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]]
    });
    public formSumitted = false;

    constructor(private router: Router, private fb: FormBuilder,
                private userService: UserService,
                private messageService: MessageService,
                private confirmationService: ConfirmationService) {
    }

    ngOnInit(): void {
    }

    cancelar() {

        this.router.navigateByUrl('/');
    }

    recoverPassword() {

        this.formSumitted = true;

        if (this.recoverPasswordForm.valid) {
            this.userService.recoverPassword(this.recoverPasswordForm.get('email').value).subscribe(value => {
                this.confirm();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: error.error.message
                });
            });
        }
    }

    validField(field: string): boolean {
        if (this.recoverPasswordForm.get(field).invalid && this.formSumitted) {
            return true;
        }
        return false;
    }

    confirm() {
        this.confirmationService.confirm({
            message: 'Se ha enviado una nueva contraseña a su correo.',
            accept: () => {
                this.router.navigateByUrl('/login');
            }
        });
    }
}
