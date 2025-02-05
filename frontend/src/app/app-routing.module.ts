import {NgModule} from '@angular/core';
import {ExtraOptions, RouterModule, Routes} from '@angular/router';
import {AppLayoutComponent} from './layout/app.layout.component';
import {Angulartics2Module} from 'angulartics2';

const routerOptions: ExtraOptions = {
    anchorScrolling: 'enabled'
};

const routes: Routes = [
    {

        path: '', component: AppLayoutComponent,
        children: [
            {
                path: '',
                redirectTo: 'home/home',
                pathMatch: 'full'
            },
            {
                path: 'home',
                loadChildren: () => import('./home/home.module').then(m => m.HomeModule)
            },
            {
                path: 'administration',
                loadChildren: () => import('./administration/administration.module').then(m => m.AdministrationModule)
            },
            {
                path: 'partners',
                loadChildren: () => import('./partners/partners.module').then(m => m.PartnersModule)
            },
            {
                path: 'resultManager',
                loadChildren: () => import('./result-manager/result-manager-routing.module').then(m => m.ResultManagerRoutingModule)
            },
            {
                path: 'directImplementation',
                loadChildren: () => import('./direct-implementation/direct-implementation.module').then(m => m.DirectImplementationModule)
            },
            {
                path: 'reports',
                loadChildren: () => import('./reports/reports.module').then(m => m.ReportsModule)
            },
            {
                path: 'users',
                loadChildren: () => import('./users/users.module').then(m => m.UsersModule)
            },
            {
                path: 'demo',
                loadChildren: () => import('./demo/components/dashboards/dashboards.module').then(m => m.DashboardsModule)
            },
            {
                path: 'uikit',
                loadChildren: () => import('./demo/components/uikit/uikit.module').then(m => m.UIkitModule)
            },
            {
                path: 'utilities',
                loadChildren: () => import('./demo/components/utilities/utilities.module').then(m => m.UtilitiesModule)
            },
            {
                path: 'pages',
                loadChildren: () => import('./demo/components/pages/pages.module').then(m => m.PagesModule)
            },
            {
                path: 'profile',
                loadChildren: () => import('./demo/components/profile/profile.module').then(m => m.ProfileModule)
            },
            {
                path: 'documentation',
                loadChildren: () => import('./demo/components/documentation/documentation.module').then(m => m.DocumentationModule)
            },
            {
                path: 'blocks',
                loadChildren: () => import('./demo/components/primeblocks/primeblocks.module').then(m => m.PrimeBlocksModule)
            },
            {
                path: 'ecommerce',
                loadChildren: () => import('./demo/components/ecommerce/ecommerce.module').then(m => m.EcommerceModule)
            },
            {path: 'apps', loadChildren: () => import('./demo/components/apps/apps.module').then(m => m.AppsModule)}
        ]
    },
    {
        path: 'auth',
        loadChildren: () => import('./authentication/authentication.module').then(m => m.AuthenticationModule)
    },
    {
        path: 'landing',
        loadChildren: () => import('./demo/components/landing/landing.module').then(m => m.LandingModule)
    },
    {
        path: 'notfound',
        loadChildren: () => import('./demo/components/notfound/notfound.module').then(m => m.NotfoundModule)
    },
    {path: '**', redirectTo: '/notfound'}
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes, routerOptions),
        // google analitycs
        Angulartics2Module.forRoot({
            pageTracking: {
                clearIds: true,
                clearQueryParams: true,
            }
        })
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
