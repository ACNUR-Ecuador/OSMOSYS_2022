import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GeneralIndicatorFormComponent } from './general-indicator-form/general-indicator-form.component';
import { DissagregationTwoDimentionsComponent } from './dissagregation-two-dimentions/dissagregation-two-dimentions.component';
import { DissagregationTwoIntegerDimentionsComponent } from './dissagregationForms/dissagregation-two-integer-dimentions/dissagregation-two-integer-dimentions.component';



@NgModule({
  declarations: [
    GeneralIndicatorFormComponent,
    DissagregationTwoDimentionsComponent,
    DissagregationTwoIntegerDimentionsComponent
  ],
  imports: [
    CommonModule
  ]
})
export class IndicatorFormsModule { }
