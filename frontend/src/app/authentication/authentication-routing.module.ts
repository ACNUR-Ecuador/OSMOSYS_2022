import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AppLoginComponent} from './login/app.login.component';
import {RecoverPasswordComponent} from './recover-password/recover-password.component';

const routes: Routes = [
    {path: 'login', component: AppLoginComponent},
    {path: 'recover_password', component: RecoverPasswordComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthenticationRoutingModule { }
