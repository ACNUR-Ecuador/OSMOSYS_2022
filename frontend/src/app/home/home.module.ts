import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home/home.component';
import { HomeDashboardDirectImplementationComponent } from './home-dashboard-direct-implementation/home-dashboard-direct-implementation.component';
import { HomeDashboardFocalPointComponent } from './home-dashboard-focal-point/home-dashboard-focal-point.component';
import {CardModule} from "primeng/card";
import {ButtonModule} from "primeng/button";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DropdownModule} from "primeng/dropdown";
import {MultiSelectModule} from "primeng/multiselect";
import {MessagesModule} from "primeng/messages";
import {ToastModule} from "primeng/toast";
import {ChartModule} from "primeng/chart";
import {UserPipe} from "../shared/pipes/user.pipe";
import {OfficeOrganizationPipe} from "../shared/pipes/office-organization.pipe";
import {IndicatorPipe} from "../shared/pipes/indicator.pipe";


@NgModule({
  declarations: [
    HomeComponent,
    HomeDashboardDirectImplementationComponent,
    HomeDashboardFocalPointComponent
  ],
    imports: [
        CommonModule,
        HomeRoutingModule,
        CardModule,
        ButtonModule,
        ReactiveFormsModule,
        DropdownModule,
        MultiSelectModule,
        MessagesModule,
        ToastModule,
        FormsModule,
        ChartModule
    ],
    providers:[
        UserPipe,
        OfficeOrganizationPipe,
        IndicatorPipe
    ]
})
export class HomeModule { }
