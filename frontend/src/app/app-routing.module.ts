import {RouterModule} from '@angular/router';
import {NgModule} from '@angular/core';

import {AppLoginComponent} from './security/app.login.component';

import {content} from './content-routes';
import {DashboardDemoComponent} from './demo/view/dashboarddemo.component';
import {AppMainComponent} from './app.main.component';
import {AppErrorComponent} from './shared/template/errorPages/app.error.component';
import {AppAccessdeniedComponent} from './shared/template/errorPages/app.accessdenied.component';
import {AppNotfoundComponent} from './shared/template/errorPages/app.notfound.component';

@NgModule({
    imports: [
        RouterModule.forRoot([
            {
                path: '',
                component: AppMainComponent,
                children: content
                /* children: [


                 ]*/
            },
            {path: 'error', component: AppErrorComponent},
            {path: 'access', component: AppAccessdeniedComponent},
            {path: 'notfound', component: AppNotfoundComponent},
            {path: 'login', component: AppLoginComponent},
            {path: '**', redirectTo: '/notfound'},

        ], {scrollPositionRestoration: 'enabled', enableTracing: true})
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
