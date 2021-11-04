import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AreasAdministrationComponent} from './areas-administration/areas-administration.component';
import {PeriodAdministrationComponent} from './period-administration/period-administration.component';
import {PillarAdministrationComponent} from './pillar-administration/pillar-administration.component';
import {SituationAdministrationComponent} from './situation-administration/situation-administration.component';
import {OrganizationAdministrationComponent} from './organization-administration/organization-administration.component';
import {OfficeAdministrationComponent} from './office-administration/office-administration.component';
import {Statement} from '@angular/compiler';
import {StatementAdministrationComponent} from './statement-administration/statement-administration.component';
import {PerformanceIndicatorAdministrationComponent} from './performance-indicator-administration/performance-indicator-administration.component';
import {MarkerAdministrationComponent} from './marker-administration/marker-administration.component';
import {CustomDissagregationAdministrationComponent} from './custom-dissagregation-administration/custom-dissagregation-administration.component';

const routes: Routes = [
    {
        path: '',
        children: [
            {path: 'areas', component: AreasAdministrationComponent},
            {path: 'periods', component: PeriodAdministrationComponent},
            {path: 'pillars', component: PillarAdministrationComponent},
            {path: 'situations', component: SituationAdministrationComponent},
            {path: 'organizations', component: OrganizationAdministrationComponent},
            {path: 'offices', component: OfficeAdministrationComponent},
            {path: 'statements', component: StatementAdministrationComponent},
            {path: 'performanceIndicator', component: PerformanceIndicatorAdministrationComponent},
            {path: 'marker', component: MarkerAdministrationComponent},
            {path: 'customDissagregation', component: CustomDissagregationAdministrationComponent}
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AdministrationRoutingModule {
}
