import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {IndicatorCatalogReportsComponent} from "./indicator-catalog-reports/indicator-catalog-reports.component";
import {LateReportsComponent} from "./late-reports/late-reports.component";
import {ShelterComponent} from "./2022/shelter/shelter.component";
import {CbiComponent} from "./2022/cbi/cbi.component";
import {CommunityProtectionComponent} from "./2022/community-protection/community-protection.component";
import {HabitabilityComponent} from "./2022/habitability/habitability.component";
import {ResettlementComponent} from "./2022/resettlement/resettlement.component";
import {SpecialNeedsComponent} from "./2022/special-needs/special-needs.component";
import {LivehoodsComponent} from "./2022/livehoods/livehoods.component";
import {ProductIndicatorsComponent} from "./2022/product-indicators/product-indicators.component";
import {RbaIndicatorsComponent} from "./2022/rba-indicators/rba-indicators.component";
import {IndicatorComparisonComponent} from "./2022/indicator-comparison/indicator-comparison.component";
import {IndicatorTrendsComponent} from "./2022/indicator-trends/indicator-trends.component";
import {ProductIndicators2021Component} from "./2021/product-indicators2021/product-indicators2021.component";
import {Beneficiaries2021Component} from "./2021/beneficiaries2021/beneficiaries2021.component";
import {DataExportComponent} from "./data-export/data-export.component";
import { PiIndicators2023Component } from './2023/pi-indicators2023/pi-indicators2023.component';
import {ProductIndicators2023Component} from "./2023/product-indicators2023/product-indicators2023.component";

const routes: Routes = [
    {
        path: '',
        children: [
            {path: 'indicatorsCatalog', component: IndicatorCatalogReportsComponent},
            {path: 'lateReports', component: LateReportsComponent},
            {path: 'lateReports', component: LateReportsComponent},
            {path: 'dataExport', component: DataExportComponent},
            //2023
            {path: '2023/productIndicators2023', component: ProductIndicators2023Component},
            {path: '2023/piIndicators2023', component: PiIndicators2023Component},
            // 2022
            {path: '2022/shelter', component: ShelterComponent},
            {path: '2022/cbi', component: CbiComponent},
            {path: '2022/communityProtection', component: CommunityProtectionComponent},
            {path: '2022/habitability', component: HabitabilityComponent},
            {path: '2022/livelihoods', component: LivehoodsComponent},
            {path: '2022/resettlement', component: ResettlementComponent},
            {path: '2022/specialNeeds', component: SpecialNeedsComponent},
            {path: '2022/productIndicators', component: ProductIndicatorsComponent},
            {path: '2022/rbaIndicators', component: RbaIndicatorsComponent},
            {path: '2022/indicatorComparison', component: IndicatorComparisonComponent},
            {path: '2022/indicatorTrends', component: IndicatorTrendsComponent},
            // 2021
            {path: '2021/productIndicators2021', component: ProductIndicators2021Component},
            {path: '2021/beneficiaries2021', component: Beneficiaries2021Component},
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ReportsRoutingModule {
}
