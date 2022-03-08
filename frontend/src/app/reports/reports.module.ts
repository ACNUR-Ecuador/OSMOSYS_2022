import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportsRoutingModule } from './reports-routing.module';
import { AllProjectsStateComponent } from './all-projects-state/all-projects-state.component';
import {ReactiveFormsModule} from '@angular/forms';
import {CardModule} from 'primeng/card';
import {DropdownModule} from 'primeng/dropdown';
import {MessagesModule} from 'primeng/messages';
import {ToastModule} from 'primeng/toast';
import {ButtonModule} from 'primeng/button';
import { TestPlotyComponent } from './test-ploty/test-ploty.component';
import {PlotlyViaCDNModule} from 'angular-plotly.js';
import { ReportControlPartnersComponent } from './report-control-partners/report-control-partners.component';
import { ReportControlDirectImplementationComponent } from './report-control-direct-implementation/report-control-direct-implementation.component';
import { DataExportComponent } from './data-export/data-export.component';
import {ToolbarModule} from 'primeng/toolbar';
import {FieldsetModule} from 'primeng/fieldset';
import {SplitButtonModule} from 'primeng/splitbutton';
import { IndicatorCatalogReportsComponent } from './indicator-catalog-reports/indicator-catalog-reports.component';


@NgModule({
  declarations: [
    AllProjectsStateComponent,
    TestPlotyComponent,
    ReportControlPartnersComponent,
    ReportControlDirectImplementationComponent,
    DataExportComponent,
    IndicatorCatalogReportsComponent
  ],
    imports: [
        CommonModule,
        ReportsRoutingModule,
        ReactiveFormsModule,
        CardModule,
        DropdownModule,
        MessagesModule,
        ToastModule,
        ButtonModule,
        PlotlyViaCDNModule,
        ToolbarModule,
        FieldsetModule,
        SplitButtonModule
    ]
})
export class ReportsModule { }
