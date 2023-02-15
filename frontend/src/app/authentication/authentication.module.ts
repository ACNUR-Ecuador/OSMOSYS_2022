import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {InputTextModule} from 'primeng/inputtext';
import {PasswordModule} from 'primeng/password';
import {ButtonModule} from 'primeng/button';
import {AppLoginComponent} from './app-login/app-login.component';
import {AuthenticationRoutingModule} from './authentication-routing.module';
import {ReactiveFormsModule} from '@angular/forms';
import {MessagesModule} from 'primeng/messages';
import {MessageService} from 'primeng/api';
import { RecoverPasswordComponent } from './recover-password/recover-password.component';
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToastModule} from "primeng/toast";
import {CardModule} from "primeng/card";

@NgModule({
    declarations: [
        AppLoginComponent,
        RecoverPasswordComponent
    ],
    imports: [
        CommonModule,
        AuthenticationRoutingModule,
        InputTextModule,
        PasswordModule,
        ButtonModule,
        ReactiveFormsModule,
        MessagesModule,
        ConfirmDialogModule,
        ToastModule,
        CardModule
    ],
    providers: [
        MessageService
    ]
})
export class AuthenticationModule {
}
