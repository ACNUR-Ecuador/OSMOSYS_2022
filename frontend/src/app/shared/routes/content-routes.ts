import {Routes} from '@angular/router';

export const content: Routes = [

    {
        path: 'home',
        loadChildren: () => import('../../home/home.module').then(m => m.HomeModule)
    },
    {
        path: 'administration',
        loadChildren: () => import('../../administration/administration.module').then(m => m.AdministrationModule)
    },
    {
        path: 'partners',
        loadChildren: () => import('../../partners/partners.module').then(m => m.PartnersModule)
    },
    {
        path: 'directImplementation',
        loadChildren: () => import('../../direct-implementation/direct-implementation.module').then(m => m.DirectImplementationModule)
    },
    {
        path: 'users',
        loadChildren: () => import('../../users/users.module').then(m => m.UsersModule)
    },
    {
        path: 'reports',
        loadChildren: () => import('../../reports/reports.module').then(m => m.ReportsModule)
    },
    {
        path: 'demo',
        loadChildren: () => import('../../demo/demo.module').then(m => m.DemoModule)
    },
];
