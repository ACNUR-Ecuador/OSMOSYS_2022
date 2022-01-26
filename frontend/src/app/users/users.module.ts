import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UsersRoutingModule } from './users-routing.module';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import {ToastModule} from 'primeng/toast';
import {ReactiveFormsModule} from '@angular/forms';
import {CardModule} from 'primeng/card';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {InputTextModule} from 'primeng/inputtext';
import {ButtonModule} from 'primeng/button';
import {DropdownModule} from 'primeng/dropdown';


@NgModule({
  declarations: [
    ChangePasswordComponent,
    UserProfileComponent
  ],
    imports: [
        CommonModule,
        UsersRoutingModule,
        ToastModule,
        ReactiveFormsModule,
        CardModule,
        ConfirmDialogModule,
        InputTextModule,
        ButtonModule,
        DropdownModule
    ],
})
export class UsersModule { }
