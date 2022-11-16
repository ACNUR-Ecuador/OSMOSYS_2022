import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ChartIndicatorExecutionComponent} from './chart-indicator-execution/chart-indicator-execution.component';
import {
    DirectImplementationPerformanceIndicatorFormComponent
} from './direct-implementation-performance-indicator-form/direct-implementation-performance-indicator-form.component';
import {GeneralIndicatorFormComponent} from './general-indicator-form/general-indicator-form.component';
import {PerformanceIndicatorFormComponent} from './performance-indicator-form/performance-indicator-form.component';
import {
    CustomDissagregationIntegerComponent
} from './dissagregationForms/custom-dissagregation-integer/custom-dissagregation-integer.component';
import {
    DissagregationFourIntegerDimensionsComponent
} from './dissagregationForms/dissagregation-four-integer-dimensions/dissagregation-four-integer-dimensions.component';
import {
    DissagregationNoDissagregationIntegerComponent
} from './dissagregationForms/dissagregation-no-dissagregation-integer/dissagregation-no-dissagregation-integer.component';
import {
    DissagregationThreeIntegerDimensionsComponent
} from './dissagregationForms/dissagregation-three-integer-dimensions/dissagregation-three-integer-dimensions.component';
import {
    DissagregationTwoIntegerDimentionsComponent
} from './dissagregationForms/dissagregation-two-integer-dimentions/dissagregation-two-integer-dimentions.component';
import {ChartModule} from 'primeng/chart';
import {CardModule} from 'primeng/card';
import {ToastModule} from 'primeng/toast';
import {MessagesModule} from 'primeng/messages';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {InputNumberModule} from 'primeng/inputnumber';
import {MultiSelectModule} from 'primeng/multiselect';
import {DropdownModule} from 'primeng/dropdown';
import {MessageModule} from 'primeng/message';
import {DialogModule} from 'primeng/dialog';
import {TableModule} from 'primeng/table';
import {SharedModule} from '../shared/shared.module';
import {PickListModule} from 'primeng/picklist';
import {
    DissagregationOneIntegerDimensionsComponent
} from './dissagregationForms/dissagregation-one-integer-dimensions/dissagregation-one-integer-dimensions.component';
import {EnumValuesToLabelPipe} from '../shared/pipes/enum-values-to-label.pipe';
import {PanelModule} from 'primeng/panel';
import {AccordionModule} from 'primeng/accordion';
import {ButtonModule} from 'primeng/button';
import {RippleModule} from 'primeng/ripple';
import {InputTextModule} from 'primeng/inputtext';
import {InputTextareaModule} from 'primeng/inputtextarea';


@NgModule({
    declarations: [
        ChartIndicatorExecutionComponent,
        DirectImplementationPerformanceIndicatorFormComponent,
        GeneralIndicatorFormComponent,
        PerformanceIndicatorFormComponent,
        CustomDissagregationIntegerComponent,
        DissagregationFourIntegerDimensionsComponent,
        DissagregationNoDissagregationIntegerComponent,
        DissagregationThreeIntegerDimensionsComponent,
        DissagregationTwoIntegerDimentionsComponent,
        DissagregationOneIntegerDimensionsComponent
    ],
    imports: [
        CommonModule,
        ChartModule,
        CardModule,
        ToastModule,
        MessagesModule,
        FormsModule,
        ReactiveFormsModule,
        InputNumberModule,
        MultiSelectModule,
        DropdownModule,
        MessageModule,
        DialogModule,
        TableModule,
        SharedModule,
        PickListModule,
        PanelModule,
        AccordionModule,
        ButtonModule,
        RippleModule,
        InputTextModule,
        InputTextareaModule
    ],
    exports: [
        ChartIndicatorExecutionComponent,
        DirectImplementationPerformanceIndicatorFormComponent,
        GeneralIndicatorFormComponent,
        PerformanceIndicatorFormComponent,
        CustomDissagregationIntegerComponent,
        DissagregationFourIntegerDimensionsComponent,
        DissagregationNoDissagregationIntegerComponent,
        DissagregationThreeIntegerDimensionsComponent,
        DissagregationTwoIntegerDimentionsComponent,
        DissagregationOneIntegerDimensionsComponent
    ],
    providers: [EnumValuesToLabelPipe]
})
export class IndicatorFormsModule {
}
