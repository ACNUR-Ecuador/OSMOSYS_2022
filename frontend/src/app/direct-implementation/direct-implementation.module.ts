import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {DirectImplementationRoutingModule} from './direct-implementation-routing.module';
import {AreasMenuComponent} from './areas-menu/areas-menu.component';
import {IndicatorQuarterListComponent} from './indicator-quarter-list/indicator-quarter-list.component';
import {IndicatorsListComponent} from './indicators-list/indicators-list.component';
import {CardModule} from "primeng/card";
import {DropdownModule} from "primeng/dropdown";
import {MultiSelectModule} from "primeng/multiselect";
import {MessageModule} from "primeng/message";
import {ButtonModule} from "primeng/button";
import {BadgeModule} from "primeng/badge";
import {DialogService} from "primeng/dynamicdialog";
import {ReactiveFormsModule} from "@angular/forms";
import {ToastModule} from "primeng/toast";
import {MessagesModule} from "primeng/messages";
import {UserPipe} from "../shared/pipes/user.pipe";
import {OfficeOrganizationPipe} from "../shared/pipes/office-organization.pipe";
import {IndicatorPipe} from "../shared/pipes/indicator.pipe";
import {TooltipModule} from "primeng/tooltip";


@NgModule({
    declarations: [
        AreasMenuComponent,
        IndicatorQuarterListComponent,
        IndicatorsListComponent
    ],
    imports: [
        CommonModule,
        DirectImplementationRoutingModule,
        CardModule,
        DropdownModule,
        MultiSelectModule,
        MessageModule,
        MessagesModule,
        ButtonModule,
        BadgeModule,
        ReactiveFormsModule,
        ToastModule,
        TooltipModule
    ],
    providers: [
        DialogService,
        UserPipe,
        OfficeOrganizationPipe,
        IndicatorPipe
    ]
})
export class DirectImplementationModule {
}
