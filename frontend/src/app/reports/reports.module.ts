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
import { ShelterComponent } from './2022/shelter/shelter.component';
import { CbiComponent } from './2022/cbi/cbi.component';
import { CommunityProtectionComponent } from './2022/community-protection/community-protection.component';
import { HabitabilityComponent } from './2022/habitability/habitability.component';
import { LivehoodsComponent } from './2022/livehoods/livehoods.component';
import { ResettlementComponent } from './2022/resettlement/resettlement.component';
import { SpecialNeedsComponent } from './2022/special-needs/special-needs.component';
import { ProductIndicatorsComponent } from './2022/product-indicators/product-indicators.component';
import { RbaIndicatorsComponent } from './2022/rba-indicators/rba-indicators.component';
import { IndicatorComparisonComponent } from './2022/indicator-comparison/indicator-comparison.component';
import { IndicatorTrendsComponent } from './2022/indicator-trends/indicator-trends.component';
import { ProductIndicators2021Component } from './2021/product-indicators2021/product-indicators2021.component';
import { Beneficiaries2021Component } from './2021/beneficiaries2021/beneficiaries2021.component';
import { DataExportComponent } from './data-export/data-export.component';
import {CardModule} from "primeng/card";
import {SplitButtonModule} from "primeng/splitbutton";


@NgModule({
  declarations: [
    IndicatorCatalogReportsComponent,
    LateReportsComponent,
    ShelterComponent,
    CbiComponent,
    CommunityProtectionComponent,
    HabitabilityComponent,
    LivehoodsComponent,
    ResettlementComponent,
    SpecialNeedsComponent,
    ProductIndicatorsComponent,
    RbaIndicatorsComponent,
    IndicatorComparisonComponent,
    IndicatorTrendsComponent,
    ProductIndicators2021Component,
    Beneficiaries2021Component,
    DataExportComponent
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
        FormsModule,
        CardModule,
        SplitButtonModule
    ]
})
export class ReportsModule { }
