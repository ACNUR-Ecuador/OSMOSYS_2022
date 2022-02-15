import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AllProjectsStateComponent} from './all-projects-state/all-projects-state.component';

const routes: Routes = [{
    path: '',
    children: [
        {path: 'allProjectsState', component: AllProjectsStateComponent}
    ]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportsRoutingModule { }
