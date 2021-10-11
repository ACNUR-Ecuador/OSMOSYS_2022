import {RouterModule} from '@angular/router';
import {NgModule} from '@angular/core';

import {AppLoginComponent} from './authentication/login/app.login.component';

import {content} from './shared/routes/content-routes';
import {AppMainComponent} from './app.main.component';
import {AppErrorComponent} from './shared/template/errorPages/app.error.component';
import {AppAccessdeniedComponent} from './shared/template/errorPages/app.accessdenied.component';
import {AppNotfoundComponent} from './shared/template/errorPages/app.notfound.component';

@NgModule({
    imports: [
        RouterModule.forRoot([
            {
                path: '',
                redirectTo: 'demo/dashboard',
                pathMatch: 'full'
            },
            {
                path: '',
                component: AppMainComponent,
                children: content
            },
            {path: 'error', component: AppErrorComponent},
            {path: 'access', component: AppAccessdeniedComponent},
            {path: 'notfound', component: AppNotfoundComponent},
            {path: 'login', component: AppLoginComponent},
            {path: '**', redirectTo: '/notfound'},

        ], {scrollPositionRestoration: 'enabled'
            // , enableTracing: true
        })
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
