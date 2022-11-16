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

@NgModule({
    declarations: [
        AppLoginComponent
    ],
    imports: [
        CommonModule,
        AuthenticationRoutingModule,
        InputTextModule,
        PasswordModule,
        ButtonModule,
        ReactiveFormsModule,
        MessagesModule
    ],
    providers: [
        MessageService
    ]
})
export class AuthenticationModule {
}
