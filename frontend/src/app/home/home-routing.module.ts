import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from './home/home.component';
import {HomeDashboardFocalPointComponent} from './home-dashboard-focal-point/home-dashboard-focal-point.component';

const routes: Routes = [
    {
        path: '',
        children: [
            {path: 'home', component: HomeComponent},
            {path: 'homeDashboardFocalPoint', component: HomeDashboardFocalPointComponent}
        ]
    }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
