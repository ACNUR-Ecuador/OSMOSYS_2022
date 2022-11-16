import {NgModule} from '@angular/core';
import {CommonModule, PercentPipe} from '@angular/common';

import {PartnersRoutingModule} from './partners-routing.module';
import {PartnersProjectListComponent} from './partners-project-list/partners-project-list.component';
import {
    PartnersGeneralIndicatorQuarterListComponent
} from './partners-general-indicator-quarter-list/partners-general-indicator-quarter-list.component';
import {PartnersIndicatorQuarterListComponent} from './partners-indicator-quarter-list/partners-indicator-quarter-list.component';
import {PartnersProjectComponent} from './partners-project/partners-project.component';
import {TableModule} from 'primeng/table';
import {ToggleButtonModule} from 'primeng/togglebutton';
import {
    PartnersProjectGeneralIndicatorListComponent
} from './partners-project-general-indicator-list/partners-project-general-indicator-list.component';
import {ToastModule} from 'primeng/toast';
import {ToolbarModule} from 'primeng/toolbar';
import {DropdownModule} from 'primeng/dropdown';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MultiSelectModule} from 'primeng/multiselect';
import {
    PartnersProjectPerformanceIndicatorListComponent
} from './partners-project-performance-indicator-list/partners-project-performance-indicator-list.component';
import {IndicatorFormsModule} from '../indicator-forms/indicator-forms.module';
import {TabViewModule} from 'primeng/tabview';
import {CardModule} from 'primeng/card';
import {MessagesModule} from 'primeng/messages';
import {ButtonModule} from 'primeng/button';
import {SplitButtonModule} from 'primeng/splitbutton';
import {EnumValuesToLabelPipe} from '../shared/pipes/enum-values-to-label.pipe';
import {SharedModule} from '../shared/shared.module';
import {OverlayPanelModule} from 'primeng/overlaypanel';
import {ValuesStatePipe} from '../shared/pipes/values-state.pipe';
import {DialogService} from 'primeng/dynamicdialog';
import {CodeDescriptionPipe} from '../shared/pipes/code-description.pipe';
import {IndicatorPipe} from '../shared/pipes/indicator.pipe';
import {MonthPipe} from '../shared/pipes/month.pipe';
import {InputTextModule} from 'primeng/inputtext';


@NgModule({
    declarations: [
        PartnersProjectListComponent,
        PartnersGeneralIndicatorQuarterListComponent,
        PartnersIndicatorQuarterListComponent,
        PartnersProjectComponent,
        PartnersProjectGeneralIndicatorListComponent,
        PartnersProjectPerformanceIndicatorListComponent
    ],
    imports: [
        CommonModule,
        PartnersRoutingModule,
        TableModule,
        ToggleButtonModule,
        ToastModule,
        ToolbarModule,
        DropdownModule,
        ReactiveFormsModule,
        MultiSelectModule,
        TabViewModule,
        CardModule,
        MessagesModule,
        ButtonModule,
        SplitButtonModule,
        FormsModule,
        SharedModule,
        OverlayPanelModule,
        IndicatorFormsModule,
        ButtonModule,
        InputTextModule
    ],
    providers: [
        EnumValuesToLabelPipe,
        ValuesStatePipe,
        CodeDescriptionPipe,
        IndicatorPipe,
        PercentPipe,
        MonthPipe,
        DialogService
    ]
})
export class PartnersModule {
}
