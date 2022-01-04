import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GeneralIndicatorFormComponent } from './general-indicator-form/general-indicator-form.component';
import { DissagregationTwoIntegerDimentionsComponent } from './dissagregationForms/dissagregation-two-integer-dimentions/dissagregation-two-integer-dimentions.component';
import {TableModule} from 'primeng/table';
import {FormsModule} from '@angular/forms';
import {InputNumberModule} from 'primeng/inputnumber';



@NgModule({
  declarations: [
    GeneralIndicatorFormComponent,
    DissagregationTwoIntegerDimentionsComponent
  ],
    imports: [
        CommonModule,
        TableModule,
        FormsModule,
        InputNumberModule
    ]
})
export class IndicatorFormsModule { }
