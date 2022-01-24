import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home/home.component';
import { HomeDashboardFocalPointComponent } from './home-dashboard-focal-point/home-dashboard-focal-point.component';
import {CardModule} from 'primeng/card';
import {DropdownModule} from 'primeng/dropdown';
import {FormsModule} from '@angular/forms';
import {ChartModule} from 'primeng/chart';


@NgModule({
  declarations: [
    HomeComponent,
    HomeDashboardFocalPointComponent
  ],
    imports: [
        CommonModule,
        HomeRoutingModule,
        CardModule,
        DropdownModule,
        FormsModule,
        ChartModule
    ]
})
export class HomeModule { }
