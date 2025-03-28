import {NgModule} from '@angular/core';
import {CommonModule, PercentPipe} from '@angular/common';

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
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ToastModule} from "primeng/toast";
import {MessagesModule} from "primeng/messages";
import {UserPipe} from "../shared/pipes/user.pipe";
import {OfficeOrganizationPipe} from "../shared/pipes/office-organization.pipe";
import {IndicatorPipe} from "../shared/pipes/indicator.pipe";
import {TooltipModule} from "primeng/tooltip";
import {CodeDescriptionPipe} from "../shared/pipes/code-description.pipe";
import {MonthPipe} from "../shared/pipes/month.pipe";
import {MonthListPipe} from "../shared/pipes/month-list.pipe";
import {BooleanYesNoPipe} from "../shared/pipes/boolean-yes-no.pipe";
import {TableModule} from "primeng/table";
import {OverlayPanelModule} from "primeng/overlaypanel";
import {ToggleButtonModule} from "primeng/togglebutton";
import {SharedModule} from "../shared/shared.module";
import { LateStatePipe } from '../shared/pipes/late-state.pipe';


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
        TooltipModule,
        TableModule,
        FormsModule,
        OverlayPanelModule,
        ToggleButtonModule,
        SharedModule
    ],
    providers: [
        DialogService,
        UserPipe,
        OfficeOrganizationPipe,
        IndicatorPipe,
        CodeDescriptionPipe,
        MonthPipe,
        MonthListPipe,
        BooleanYesNoPipe,
        PercentPipe,
        LateStatePipe
    ]
})
export class DirectImplementationModule {
}
