import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportsRoutingModule } from './reports-routing.module';
import { IndicatorCatalogReportsComponent } from './indicator-catalog-reports/indicator-catalog-reports.component';
import {ToastModule} from "primeng/toast";
import {MessagesModule} from "primeng/messages";
import {DropdownModule} from "primeng/dropdown";
import {ToolbarModule} from "primeng/toolbar";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    IndicatorCatalogReportsComponent
  ],
    imports: [
        CommonModule,
        ReportsRoutingModule,
        ToastModule,
        MessagesModule,
        DropdownModule,
        ToolbarModule,
        ReactiveFormsModule
    ]
})
export class ReportsModule { }
