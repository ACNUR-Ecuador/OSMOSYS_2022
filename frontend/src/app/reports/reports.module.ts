import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportsRoutingModule } from './reports-routing.module';
import { AllProjectsStateComponent } from './all-projects-state/all-projects-state.component';
import {ReactiveFormsModule} from '@angular/forms';
import {CardModule} from 'primeng/card';
import {DropdownModule} from 'primeng/dropdown';
import {MessagesModule} from 'primeng/messages';
import {ToastModule} from 'primeng/toast';
import {ButtonModule} from 'primeng/button';


@NgModule({
  declarations: [
    AllProjectsStateComponent
  ],
    imports: [
        CommonModule,
        ReportsRoutingModule,
        ReactiveFormsModule,
        CardModule,
        DropdownModule,
        MessagesModule,
        ToastModule,
        ButtonModule
    ]
})
export class ReportsModule { }
