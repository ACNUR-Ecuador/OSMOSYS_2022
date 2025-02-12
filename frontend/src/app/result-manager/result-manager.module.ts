import {NgModule} from '@angular/core';
import {CommonModule, PercentPipe} from '@angular/common';

import {TableModule} from 'primeng/table';
import {ToggleButtonModule} from 'primeng/togglebutton';
import {ToastModule} from 'primeng/toast';
import {ToolbarModule} from 'primeng/toolbar';
import {DropdownModule} from 'primeng/dropdown';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MultiSelectModule} from 'primeng/multiselect';
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
import {InputTextareaModule} from 'primeng/inputtextarea';
import {SelectButtonModule} from 'primeng/selectbutton';
import {RippleModule} from "primeng/ripple";
import {DialogModule} from "primeng/dialog";
import {PickListModule} from "primeng/picklist";
import { ResultManagerIndicatorListComponent } from './result-manager-indicator-list/result-manager-indicator-list.component';
import { ResultManagerRoutingModule } from './result-manager-routing.module';
import { PercentagePipe } from '../shared/pipes/percentage.pipe'; // Asegúrate de que la ruta sea correcta
import { ResultManagerExecutionPipe } from '../shared/pipes/result-manager-execution.pipe';
import {CheckboxModule} from 'primeng/checkbox';
import { RadioButtonModule } from 'primeng/radiobutton';
import { TagModule } from 'primeng/tag';


@NgModule({
    declarations: [
        ResultManagerIndicatorListComponent,
        
    ],
    imports: [
        CommonModule,
        ResultManagerRoutingModule,
        InputTextareaModule,
        SelectButtonModule,
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
        InputTextModule,
        RippleModule,
        DialogModule,
        PickListModule,
        CheckboxModule,
        RadioButtonModule,
        TagModule
    ],
    providers: [
        EnumValuesToLabelPipe,
        ValuesStatePipe,
        CodeDescriptionPipe,
        IndicatorPipe,
        PercentPipe,
        MonthPipe,
        DialogService,
        PercentagePipe,
        ResultManagerExecutionPipe

    ]
})
export class ResultManagerModule {
}
