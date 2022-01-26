import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, Validators} from '@angular/forms';
import {UserService} from '../../shared/services/user.service';
import {ConfirmationService, MessageService} from 'primeng/api';

@Component({
    selector: 'app-change-password',
    templateUrl: './change-password.component.html',
    styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {


    public changePasswordForm = this.fb.group({
        oldpass: ['', [Validators.required]],
        newpass: ['', [Validators.required]],
        newpass2: ['', [Validators.required]]
    });

    public formSumitted = false;

    constructor(private router: Router, private fb: FormBuilder,
                private userService: UserService,
                private messageService: MessageService,
                private confirmationService: ConfirmationService) {
    }

    ngOnInit(): void {
        if (!this.userService.getLogedUsername()) {
            this.cancelar();
        }
    }

    changePassword() {
        this.formSumitted = true;

        if (this.changePasswordForm.invalid || this.newPasswordValidation()) {
            return;
        } else {
            this.userService.changePassword(this.changePasswordForm.get('oldpass').value, this.changePasswordForm.get('newpass').value)
                .subscribe(value => {
                        this.confirm();

                    }, error => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error',
                            detail: error.error.message
                        });
                    }
                );
        }
    }

    newPasswordValidation(): boolean {
        const pass1 = this.changePasswordForm.get('newpass').value;
        const pass2 = this.changePasswordForm.get('newpass2').value;

        return (pass1 !== pass2) && this.formSumitted;
    }

    validField(field: string): boolean {
        return this.changePasswordForm.get(field).invalid && this.formSumitted;
    }

    cancelar() {
        this.router.navigateByUrl('/');
    }

    confirm() {
        this.confirmationService.confirm({
            message: 'Su contraseña ha cambiado. Por favor vuelva a ingresar al sistema.',
            accept: () => {
                this.userService.logout();
                this.router.navigateByUrl('/login');
            }
        });
    }
}
