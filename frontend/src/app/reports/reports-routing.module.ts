import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AllProjectsStateComponent} from './all-projects-state/all-projects-state.component';
import {TestPlotyComponent} from './test-ploty/test-ploty.component';
import {ReportControlPartnersComponent} from './report-control-partners/report-control-partners.component';
import {
    ReportControlDirectImplementationComponent
} from './report-control-direct-implementation/report-control-direct-implementation.component';
import {DataExportComponent} from './data-export/data-export.component';
import {IndicatorCatalogReportsComponent} from './indicator-catalog-reports/indicator-catalog-reports.component';
import {ShelterComponent} from './powerbi/shelter/shelter.component';
import {CbiComponent} from './powerbi/cbi/cbi.component';
import {CommunityProtectionComponent} from './powerbi/community-protection/community-protection.component';
import {HabitabilityComponent} from './powerbi/habitability/habitability.component';
import {LivelihoodsComponent} from './powerbi/livelihoods/livelihoods.component';
import {ResettlementComponent} from './powerbi/resettlement/resettlement.component';
import {SpecialNeedsComponent} from './powerbi/special-needs/special-needs.component';
import {ProductIndicatorsComponent} from './powerbi/product-indicators/product-indicators.component';

const routes: Routes = [{
    path: '',
    children: [
        {path: 'allProjectsState', component: AllProjectsStateComponent},
        {path: 'reportControlPartners', component: ReportControlPartnersComponent},
        {path: 'reportControlDirectImplementation', component: ReportControlDirectImplementationComponent},
        {path: 'dataExport', component: DataExportComponent},
        {path: 'indicatorsCatalog', component: IndicatorCatalogReportsComponent},
        {path: 'testPloty', component: TestPlotyComponent},
        {path: 'shelter', component: ShelterComponent},
        {path: 'cbi', component: CbiComponent},
        {path: 'communityProtection', component: CommunityProtectionComponent},
        {path: 'habitability', component: HabitabilityComponent},
        {path: 'livelihoods', component: LivelihoodsComponent},
        {path: 'resettlement', component: ResettlementComponent},
        {path: 'specialNeeds', component: SpecialNeedsComponent},
        {path: 'productIndicators', component: ProductIndicatorsComponent}
    ]
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ReportsRoutingModule {
}
