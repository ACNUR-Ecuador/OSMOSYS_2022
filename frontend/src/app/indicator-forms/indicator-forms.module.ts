import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {GeneralIndicatorFormComponent} from './general-indicator-form/general-indicator-form.component';
import {DissagregationTwoIntegerDimentionsComponent} from './dissagregationForms/dissagregation-two-integer-dimentions/dissagregation-two-integer-dimentions.component';
import {TableModule} from 'primeng/table';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {InputNumberModule} from 'primeng/inputnumber';
import {DissagregationOneIntegerDimentionsComponent} from './dissagregationForms/dissagregation-one-integer-dimentions/dissagregation-one-integer-dimentions.component';
import {ToastModule} from 'primeng/toast';
import {MessagesModule} from 'primeng/messages';
import {ButtonModule} from 'primeng/button';
import {DialogModule} from 'primeng/dialog';
import {SharedModule} from '../shared/shared.module';
import {DissagregationNoDissagregationIntegerComponent} from './dissagregationForms/dissagregation-no-dissagregation-integer/dissagregation-no-dissagregation-integer.component';
import { PerformanceIndicatorFormComponent } from './performance-indicator-form/performance-indicator-form.component';
import {CardModule} from 'primeng/card';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {ChartModule} from 'primeng/chart';
import { ChartIndicatorExecutionComponent } from './chart-indicator-execution/chart-indicator-execution.component';
import { CustomDissagregationIntegerComponent } from './dissagregationForms/custom-dissagregation-integer/custom-dissagregation-integer.component';
import {MultiSelectModule} from 'primeng/multiselect';
import {InputTextModule} from 'primeng/inputtext';
import {ToggleButtonModule} from 'primeng/togglebutton';
import {DropdownModule} from 'primeng/dropdown';
import { DirectImplementationPerformanceIndicatorFormComponent } from './direct-implementation-performance-indicator-form/direct-implementation-performance-indicator-form.component';
import {RippleModule} from 'primeng/ripple';
import {PickListModule} from 'primeng/picklist';


@NgModule({
    declarations: [
        GeneralIndicatorFormComponent,
        DissagregationTwoIntegerDimentionsComponent,
        DissagregationOneIntegerDimentionsComponent,
        DissagregationNoDissagregationIntegerComponent,
        PerformanceIndicatorFormComponent,
        ChartIndicatorExecutionComponent,
        CustomDissagregationIntegerComponent,
        DirectImplementationPerformanceIndicatorFormComponent
    ],
    imports: [
        CommonModule,
        TableModule,
        FormsModule,
        InputNumberModule,
        ToastModule,
        MessagesModule,
        ButtonModule,
        DialogModule,
        SharedModule,
        CardModule,
        ReactiveFormsModule,
        InputTextareaModule,
        ChartModule,
        MultiSelectModule,
        InputTextModule,
        ToggleButtonModule,
        DropdownModule,
        RippleModule,
        PickListModule
    ]
})
export class IndicatorFormsModule {
}
