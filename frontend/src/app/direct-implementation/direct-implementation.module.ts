import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {DirectImplementationRoutingModule} from './direct-implementation-routing.module';
import {AreasMenuComponent} from './areas-menu/areas-menu.component';
import {ButtonModule} from 'primeng/button';
import {CardModule} from 'primeng/card';
import {BadgeModule} from 'primeng/badge';
import {DropdownModule} from 'primeng/dropdown';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MultiSelectModule} from 'primeng/multiselect';
import {MessageModule} from 'primeng/message';
import {MessagesModule} from 'primeng/messages';
import {ToastModule} from 'primeng/toast';
import { IndicatorsListComponent } from './indicators-list/indicators-list.component';
import {TableModule} from 'primeng/table';
import {RippleModule} from 'primeng/ripple';
import {TooltipModule} from 'primeng/tooltip';


@NgModule({
    declarations: [
        AreasMenuComponent,
        IndicatorsListComponent
    ],
    imports: [
        CommonModule,
        DirectImplementationRoutingModule,
        ButtonModule,
        CardModule,
        BadgeModule,
        DropdownModule,
        ReactiveFormsModule,
        MultiSelectModule,
        MessageModule,
        MessagesModule,
        ToastModule,
        TableModule,
        FormsModule,
        RippleModule,
        TooltipModule
    ]
})
export class DirectImplementationModule {
}
