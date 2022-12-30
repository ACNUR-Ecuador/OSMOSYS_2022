import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UsersRoutingModule } from './users-routing.module';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import {ToastModule} from "primeng/toast";
import {ReactiveFormsModule} from "@angular/forms";
import {CardModule} from "primeng/card";
import {ButtonModule} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";
import {OfficeOrganizationPipe} from "../shared/pipes/office-organization.pipe";
import {ConfirmDialogModule} from "primeng/confirmdialog";


@NgModule({
  declarations: [
    UserProfileComponent,
    ChangePasswordComponent
  ],
    imports: [
        CommonModule,
        UsersRoutingModule,
        ToastModule,
        ReactiveFormsModule,
        CardModule,
        ButtonModule,
        DropdownModule,
        ConfirmDialogModule
    ],
    providers:[
        OfficeOrganizationPipe
    ]
})
export class UsersModule { }
