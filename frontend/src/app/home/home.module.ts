import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home/home.component';
import { HomeDashboardFocalPointComponent } from './home-dashboard-focal-point/home-dashboard-focal-point.component';
import {CardModule} from 'primeng/card';
import {DropdownModule} from 'primeng/dropdown';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ChartModule} from 'primeng/chart';
import { HomeDashboardDirectImplementationComponent } from './home-dashboard-direct-implementation/home-dashboard-direct-implementation.component';
import {MultiSelectModule} from 'primeng/multiselect';
import {MessagesModule} from 'primeng/messages';
import {ToastModule} from 'primeng/toast';
import {ButtonModule} from 'primeng/button';
import {NgxPrintModule} from 'ngx-print';


@NgModule({
  declarations: [
    HomeComponent,
    HomeDashboardFocalPointComponent,
    HomeDashboardDirectImplementationComponent
  ],
    imports: [
        CommonModule,
        HomeRoutingModule,
        CardModule,
        DropdownModule,
        FormsModule,
        ChartModule,
        ReactiveFormsModule,
        MultiSelectModule,
        MessagesModule,
        ToastModule,
        ButtonModule,
        NgxPrintModule
    ]
})
export class HomeModule { }
