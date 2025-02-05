import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ResultManagerIndicatorListComponent } from './result-manager-indicator-list/result-manager-indicator-list.component';

const routes: Routes = [{
    path: '',
    children: [
        {path: 'resultManagerIndicatorList', component: ResultManagerIndicatorListComponent},
    ]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ResultManagerRoutingModule { }
