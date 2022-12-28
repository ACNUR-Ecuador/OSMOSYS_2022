import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {IndicatorCatalogReportsComponent} from "./indicator-catalog-reports/indicator-catalog-reports.component";

const routes: Routes = [
    {
        path: '',
        children: [
            {path: 'indicatorsCatalog', component: IndicatorCatalogReportsComponent},
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ReportsRoutingModule {
}
