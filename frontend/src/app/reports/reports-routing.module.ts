import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AllProjectsStateComponent} from './all-projects-state/all-projects-state.component';
import {TestPlotyComponent} from './test-ploty/test-ploty.component';
import {ReportControlPartnersComponent} from './report-control-partners/report-control-partners.component';
import {ReportControlDirectImplementationComponent} from './report-control-direct-implementation/report-control-direct-implementation.component';

const routes: Routes = [{
    path: '',
    children: [
        {path: 'allProjectsState', component: AllProjectsStateComponent},
        {path: 'reportControlPartners', component: ReportControlPartnersComponent},
        {path: 'reportControlDirectImplementation', component: ReportControlDirectImplementationComponent},
        {path: 'testPloty', component: TestPlotyComponent}
    ]
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ReportsRoutingModule {
}
