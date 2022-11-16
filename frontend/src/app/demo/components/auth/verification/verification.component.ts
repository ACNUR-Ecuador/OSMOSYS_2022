import { Component, ElementRef } from '@angular/core';
import { DomHandler } from 'primeng/dom';
import { InputNumber } from 'primeng/inputnumber';

@Component({
    templateUrl: './verification.component.html'
})
export class VerificationComponent {

    val1!: number;
    
    val2!: number;
    
    val3!: number;
    
    val4!: number;

    focusOnNext(inputEl: InputNumber) {
        inputEl.input.nativeElement.focus();
    }
}
