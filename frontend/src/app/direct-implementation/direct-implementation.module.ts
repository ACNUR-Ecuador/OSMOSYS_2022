import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {DirectImplementationRoutingModule} from './direct-implementation-routing.module';
import {AreasMenuComponent} from './areas-menu/areas-menu.component';
import {ButtonModule} from 'primeng/button';
import {CardModule} from 'primeng/card';
import {BadgeModule} from 'primeng/badge';
import {DropdownModule} from 'primeng/dropdown';
import {ReactiveFormsModule} from '@angular/forms';
import {MultiSelectModule} from 'primeng/multiselect';
import {MessageModule} from 'primeng/message';
import {MessagesModule} from 'primeng/messages';
import {ToastModule} from 'primeng/toast';


@NgModule({
    declarations: [
        AreasMenuComponent
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
        ToastModule
    ]
})
export class DirectImplementationModule {
}
