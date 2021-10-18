import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AreasAdministrationComponent} from './areas-administration/areas-administration.component';
import {PeriodAdministrationComponent} from './period-administration/period-administration.component';

const routes: Routes = [
    {
        path: '',
        children: [
            {path: 'areas', component: AreasAdministrationComponent},
            {path: 'periods', component: PeriodAdministrationComponent}
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AdministrationRoutingModule {
}
