import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AdministrationRoutingModule} from './administration-routing.module';
import {AreasAdministrationComponent} from './areas-administration/areas-administration.component';
import {ToastModule} from 'primeng/toast';
import {TableModule} from 'primeng/table';
import {ToolbarModule} from 'primeng/toolbar';
import {ButtonModule} from 'primeng/button';
import {ConfirmationService} from 'primeng/api';
import {DialogModule} from 'primeng/dialog';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {InputTextModule} from 'primeng/inputtext';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {DropdownModule} from 'primeng/dropdown';
import {SelectButtonModule} from 'primeng/selectbutton';


@NgModule({
    declarations: [
        AreasAdministrationComponent
    ],
    imports: [
        CommonModule,
        AdministrationRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        ToastModule,
        TableModule,
        ToolbarModule,
        ButtonModule,
        DialogModule,
        InputTextModule,
        InputTextareaModule,
        DropdownModule,
        SelectButtonModule
    ],
    providers: [
        ConfirmationService
    ]
})
export class AdministrationModule {
}
