import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {GeneralIndicatorFormComponent} from './general-indicator-form/general-indicator-form.component';
import {DissagregationTwoIntegerDimentionsComponent} from './dissagregationForms/dissagregation-two-integer-dimentions/dissagregation-two-integer-dimentions.component';
import {TableModule} from 'primeng/table';
import {FormsModule} from '@angular/forms';
import {InputNumberModule} from 'primeng/inputnumber';
import {DissagregationOneIntegerDimentionsComponent} from './dissagregationForms/dissagregation-one-integer-dimentions/dissagregation-one-integer-dimentions.component';
import {ToastModule} from 'primeng/toast';
import {MessagesModule} from 'primeng/messages';
import {ButtonModule} from 'primeng/button';
import {DialogModule} from 'primeng/dialog';
import {SharedModule} from '../shared/shared.module';
import {DissagregationNoDissagregationIntegerComponent} from './dissagregationForms/dissagregation-no-dissagregation-integer/dissagregation-no-dissagregation-integer.component';
import { PerformanceIndicatorFormComponent } from './performance-indicator-form/performance-indicator-form.component';


@NgModule({
    declarations: [
        GeneralIndicatorFormComponent,
        DissagregationTwoIntegerDimentionsComponent,
        DissagregationOneIntegerDimentionsComponent,
        DissagregationNoDissagregationIntegerComponent,
        PerformanceIndicatorFormComponent
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
        SharedModule
    ]
})
export class IndicatorFormsModule {
}
