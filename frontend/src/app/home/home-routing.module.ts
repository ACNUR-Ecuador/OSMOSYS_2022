import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {HomeDashboardFocalPointComponent} from "./home-dashboard-focal-point/home-dashboard-focal-point.component";
import {
    HomeDashboardDirectImplementationComponent
} from "./home-dashboard-direct-implementation/home-dashboard-direct-implementation.component";
import {AboutUsComponent} from "./about-us/about-us.component";
import { HomeDashboardPartnerComponent } from './home-dashboard-partner/home-dashboard-partner.component';

const routes: Routes = [{
    path: '',
    children: [
        {path: 'home', component: HomeComponent},
        {path: 'homeDashboardFocalPoint', component: HomeDashboardFocalPointComponent},
        {path: 'homeDashboardPartner', component: HomeDashboardPartnerComponent},
        {path: 'homeDashboardDirectImplementation', component: HomeDashboardDirectImplementationComponent},
        {path: 'about-us', component: AboutUsComponent},
    ]
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class HomeRoutingModule {
}
