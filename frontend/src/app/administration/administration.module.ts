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
import {PeriodAdministrationComponent} from './period-administration/period-administration.component';
import {InputNumberModule} from 'primeng/inputnumber';
import {PillarAdministrationComponent} from './pillar-administration/pillar-administration.component';
import {MessageModule} from 'primeng/message';
import {MessagesModule} from 'primeng/messages';
import {SituationAdministrationComponent} from './situation-administration/situation-administration.component';
import {OrganizationAdministrationComponent} from './organization-administration/organization-administration.component';
import {OfficeAdministrationComponent} from './office-administration/office-administration.component';
import {OfficeOrganizationPipe} from '../shared/pipes/officeOrganization.pipe';
import {TabViewModule} from 'primeng/tabview';
import {OrganizationChartModule} from 'primeng/organizationchart';
import {StatementAdministrationComponent} from './statement-administration/statement-administration.component';
import {CodeShortDescriptionPipe} from '../shared/pipes/code-short-description.pipe';
import {CheckboxModule} from 'primeng/checkbox';
import {PerformanceIndicatorAdministrationComponent} from './performance-indicator-administration/performance-indicator-administration.component';
import {MarkerAdministrationComponent} from './marker-administration/marker-administration.component';
import {CustomDissagregationAdministrationComponent} from './custom-dissagregation-administration/custom-dissagregation-administration.component';
import {PickListModule} from 'primeng/picklist';
import {MarkersListPipe} from '../shared/pipes/markers-list.pipe';
import {CustomDissagregationOptionsListPipe} from '../shared/pipes/custom-dissagregation-options-list.pipe';


@NgModule({
    declarations: [
        AreasAdministrationComponent,
        PeriodAdministrationComponent,
        PillarAdministrationComponent,
        SituationAdministrationComponent,
        OrganizationAdministrationComponent,
        OfficeAdministrationComponent,
        StatementAdministrationComponent,
        PerformanceIndicatorAdministrationComponent,
        MarkerAdministrationComponent,
        CustomDissagregationAdministrationComponent
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
        OrganizationChartModule,
        CheckboxModule,
        PickListModule
    ],
    providers: [
        ConfirmationService,
        OfficeOrganizationPipe,
        CodeShortDescriptionPipe,
        MarkersListPipe,
        CustomDissagregationOptionsListPipe
    ]
})
export class AdministrationModule {
}
