import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ChangePasswordComponent} from "./change-password/change-password.component";
import {UserProfileComponent} from "./user-profile/user-profile.component";

const routes: Routes = [
    {
        path: '',
        children: [
            {path: 'changePassword', component: ChangePasswordComponent},
            {path: 'userProfile', component: UserProfileComponent}
        ]
    }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }
