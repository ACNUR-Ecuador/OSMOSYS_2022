import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PartnersProjectListComponent} from './partners-project-list/partners-project-list.component';
import {PartnersProjectComponent} from './partners-project/partners-project.component';

const routes: Routes = [{
    path: '',
    children: [
        {path: 'partnersProjectList', component: PartnersProjectListComponent},
        {path: 'partnersProject', component: PartnersProjectComponent},
    ]
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PartnersRoutingModule {
}
