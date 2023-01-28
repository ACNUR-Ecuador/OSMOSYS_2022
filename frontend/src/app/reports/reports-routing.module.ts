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

const routes: Routes = [
    {
        path: '',
        children: [
            {path: 'indicatorsCatalog', component: IndicatorCatalogReportsComponent},
            {path: 'lateReports', component: LateReportsComponent},
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
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ReportsRoutingModule {
}
