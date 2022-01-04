import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GeneralIndicatorFormComponent } from './general-indicator-form/general-indicator-form.component';
import { DissagregationTwoIntegerDimentionsComponent } from './dissagregationForms/dissagregation-two-integer-dimentions/dissagregation-two-integer-dimentions.component';
import {TableModule} from 'primeng/table';
import {FormsModule} from '@angular/forms';
import {InputNumberModule} from 'primeng/inputnumber';
import { DissagregationOneIntegerDimentionsComponent } from './dissagregationForms/dissagregation-one-integer-dimentions/dissagregation-one-integer-dimentions.component';
import {ToastModule} from 'primeng/toast';
import {MessagesModule} from 'primeng/messages';
import {ButtonModule} from 'primeng/button';
import {DialogModule} from 'primeng/dialog';
import {SharedModule} from '../shared/shared.module';



@NgModule({
  declarations: [
    GeneralIndicatorFormComponent,
    DissagregationTwoIntegerDimentionsComponent,
    DissagregationOneIntegerDimentionsComponent
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
export class IndicatorFormsModule { }
