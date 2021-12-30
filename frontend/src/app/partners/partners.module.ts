import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PartnersRoutingModule} from './partners-routing.module';
import {PartnersProjectListComponent} from './partners-project-list/partners-project-list.component';
import {ToastModule} from 'primeng/toast';
import {ToolbarModule} from 'primeng/toolbar';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {DropdownModule} from 'primeng/dropdown';
import {TableModule} from 'primeng/table';
import {MultiSelectModule} from 'primeng/multiselect';
import {ButtonModule} from 'primeng/button';
import {PartnersProjectComponent} from './partners-project/partners-project.component';
import {SelectButtonModule} from 'primeng/selectbutton';
import {InputTextModule} from 'primeng/inputtext';
import {CalendarModule} from 'primeng/calendar';
import {MessagesModule} from 'primeng/messages';
import {EnumValuesToLabelPipe} from '../shared/pipes/enum-values-to-label.pipe';
import {SharedModule} from '../shared/shared.module';
import {TabViewModule} from 'primeng/tabview';
import {CardModule} from 'primeng/card';
import {PartnersProjectGeneralIndicatorListComponent} from './partners-project-general-indicator-list/partners-project-general-indicator-list.component';
import {PartnersIndicatorQuarterListComponent} from './partners-indicator-quarter-list/partners-indicator-quarter-list.component';
import {OverlayPanelModule} from 'primeng/overlaypanel';
import {DialogService} from 'primeng/dynamicdialog';
import {ValuesStatePipe} from '../shared/pipes/values-state.pipe';
import {Indicator} from '../shared/model/OsmosysModel';
import {IndicatorFormsModule} from '../indicator-forms/indicator-forms.module';


@NgModule({
    declarations: [
        PartnersProjectListComponent,
        PartnersProjectComponent,
        PartnersProjectGeneralIndicatorListComponent,
        PartnersIndicatorQuarterListComponent
    ],
    imports: [
        CommonModule,
        PartnersRoutingModule,
        ToastModule,
        ToolbarModule,
        ReactiveFormsModule,
        DropdownModule,
        TableModule,
        MultiSelectModule,
        ButtonModule,
        SelectButtonModule,
        InputTextModule,
        CalendarModule,
        MessagesModule,
        FormsModule,
        SharedModule,
        TabViewModule,
        CardModule,
        OverlayPanelModule,
        IndicatorFormsModule
    ],
    providers: [
        EnumValuesToLabelPipe,
        DialogService,
        ValuesStatePipe
    ]
})
export class PartnersModule {
}
