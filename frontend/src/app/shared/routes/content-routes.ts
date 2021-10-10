import { Routes } from '@angular/router';

export const content: Routes = [
  {
    path: 'demo',
    loadChildren: () => import('../../demo/demo.module').then(m => m.DemoModule)
  },
];
