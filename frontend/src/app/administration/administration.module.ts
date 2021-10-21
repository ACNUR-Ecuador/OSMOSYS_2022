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
import {MultiSelectModule} from 'primeng/multiselect';
import { PeriodAdministrationComponent } from './period-administration/period-administration.component';
import {InputNumberModule} from 'primeng/inputnumber';
import { PillarAdministrationComponent } from './pillar-administration/pillar-administration.component';
import {MessageModule} from 'primeng/message';
import {MessagesModule} from 'primeng/messages';
import { SituationAdministrationComponent } from './situation-administration/situation-administration.component';
import { OrganizationAdministrationComponent } from './organization-administration/organization-administration.component';
import { OfficeAdministrationComponent } from './office-administration/office-administration.component';
import {OfficeOrganizationPipe} from '../shared/pipes/officeOrganization.pipe';
import {TabViewModule} from 'primeng/tabview';
import {OrganizationChartModule} from 'primeng/organizationchart';


@NgModule({
    declarations: [
        AreasAdministrationComponent,
        PeriodAdministrationComponent,
        PillarAdministrationComponent,
        SituationAdministrationComponent,
        OrganizationAdministrationComponent,
        OfficeAdministrationComponent
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
        SelectButtonModule,
        MultiSelectModule,
        InputNumberModule,
        MessageModule,
        MessagesModule,
        TabViewModule,
        OrganizationChartModule
    ],
    providers: [
        ConfirmationService,
        OfficeOrganizationPipe
    ]
})
export class AdministrationModule {
}
