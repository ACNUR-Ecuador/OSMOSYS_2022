import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {DirectImplementationRoutingModule} from './direct-implementation-routing.module';
import {AreasMenuComponent} from './areas-menu/areas-menu.component';
import {ButtonModule} from 'primeng/button';
import {CardModule} from 'primeng/card';
import {BadgeModule} from 'primeng/badge';


@NgModule({
    declarations: [
        AreasMenuComponent
    ],
    imports: [
        CommonModule,
        DirectImplementationRoutingModule,
        ButtonModule,
        CardModule,
        BadgeModule
    ]
})
export class DirectImplementationModule {
}
