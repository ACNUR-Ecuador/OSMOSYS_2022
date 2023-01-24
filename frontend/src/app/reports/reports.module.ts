import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportsRoutingModule } from './reports-routing.module';
import { IndicatorCatalogReportsComponent } from './indicator-catalog-reports/indicator-catalog-reports.component';
import {ToastModule} from "primeng/toast";
import {MessagesModule} from "primeng/messages";
import {DropdownModule} from "primeng/dropdown";
import {ToolbarModule} from "primeng/toolbar";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {FieldsetModule} from "primeng/fieldset";
import {ButtonModule} from "primeng/button";
import { LateReportsComponent } from './late-reports/late-reports.component';


@NgModule({
  declarations: [
    IndicatorCatalogReportsComponent,
    LateReportsComponent
  ],
    imports: [
        CommonModule,
        ReportsRoutingModule,
        ToastModule,
        MessagesModule,
        DropdownModule,
        ToolbarModule,
        ReactiveFormsModule,
        FieldsetModule,
        ButtonModule,
        FormsModule
    ]
})
export class ReportsModule { }
