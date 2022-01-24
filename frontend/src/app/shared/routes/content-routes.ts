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
        path: 'demo',
        loadChildren: () => import('../../demo/demo.module').then(m => m.DemoModule)
    },
];
