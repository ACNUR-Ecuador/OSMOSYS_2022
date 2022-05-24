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
import { ShelterComponent } from './powerbi/shelter/shelter.component';
import { ResettlementComponent } from './powerbi/resettlement/resettlement.component';
import { HabitabilityComponent } from './powerbi/habitability/habitability.component';
import { LivelihoodsComponent } from './powerbi/livelihoods/livelihoods.component';
import { CbiComponent } from './powerbi/cbi/cbi.component';
import { CommunityProtectionComponent } from './powerbi/community-protection/community-protection.component';
import { SpecialNeedsComponent } from './powerbi/special-needs/special-needs.component';
import { ProductIndicatorsComponent } from './powerbi/product-indicators/product-indicators.component';
import { RbaIndicatorsComponent } from './powerbi/rba-indicators/rba-indicators.component';
import { ActivityInfoIndicatorsComponent } from './powerbi/activity-info-indicators/activity-info-indicators.component';


@NgModule({
  declarations: [
    AllProjectsStateComponent,
    TestPlotyComponent,
    ReportControlPartnersComponent,
    ReportControlDirectImplementationComponent,
    DataExportComponent,
    IndicatorCatalogReportsComponent,
    ShelterComponent,
    ResettlementComponent,
    HabitabilityComponent,
    LivelihoodsComponent,
    CbiComponent,
    CommunityProtectionComponent,
    SpecialNeedsComponent,
    ProductIndicatorsComponent,
    RbaIndicatorsComponent,
    ActivityInfoIndicatorsComponent
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
