import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AreasMenuComponent} from './areas-menu/areas-menu.component';

const routes: Routes = [{
    path: '',
    children: [
        {path: 'areasMenu', component: AreasMenuComponent}
    ]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DirectImplementationRoutingModule { }
