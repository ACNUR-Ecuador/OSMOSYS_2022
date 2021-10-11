import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AuthenticationRoutingModule} from './authentication-routing.module';
import {AppLoginComponent} from './login/app.login.component';
import {ButtonModule} from 'primeng/button';
import {PasswordModule} from 'primeng/password';
import {InputTextModule} from 'primeng/inputtext';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MessageModule} from 'primeng/message';
import {MessageService} from 'primeng/api';
import {ToastModule} from 'primeng/toast';
import {MessagesModule} from 'primeng/messages';


@NgModule({
    declarations: [
        AppLoginComponent
    ],
    imports: [
        CommonModule,
        ButtonModule,
        PasswordModule,
        InputTextModule,
        AuthenticationRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        MessageModule,
        MessagesModule,
        ToastModule
    ],
    providers: [
        MessageService
    ]
})
export class AuthenticationModule {
}
