import {NgModule} from '@angular/core';
import {CommonModule, PercentPipe} from '@angular/common';

import {AdministrationRoutingModule} from './administration-routing.module';
import {UserAdministrationComponent} from './user-administration/user-administration.component';
import {ToolbarModule} from 'primeng/toolbar';
import {TableModule} from 'primeng/table';
import {MultiSelectModule} from 'primeng/multiselect';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {DropdownModule} from 'primeng/dropdown';
import {OfficeOrganizationPipe} from '../shared/pipes/office-organization.pipe';
import {RolesListPipe} from '../shared/pipes/roles-list.pipe';
import {ButtonModule} from 'primeng/button';
import {DialogModule} from 'primeng/dialog';
import {MessagesModule} from 'primeng/messages';
import {CheckboxModule} from 'primeng/checkbox';
import {SelectButtonModule} from 'primeng/selectbutton';
import {RippleModule} from 'primeng/ripple';
import {TooltipModule} from 'primeng/tooltip';
import {InputTextModule} from 'primeng/inputtext';
import { AreasAdministrationComponent } from './areas-administration/areas-administration.component';
import { PillarAdministrationComponent } from './pillar-administration/pillar-administration.component';
import { PeriodAdministrationComponent } from './period-administration/period-administration.component';
import { SituationAdministrationComponent } from './situation-administration/situation-administration.component';
import { OfficeAdministrationComponent } from './office-administration/office-administration.component';
import { StatementAdministrationComponent } from './statement-administration/statement-administration.component';
import { CustomDissagregationAdministrationComponent } from './custom-dissagregation-administration/custom-dissagregation-administration.component';
import { OrganizationAdministrationComponent } from './organization-administration/organization-administration.component';
import {ToastModule} from 'primeng/toast';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {InputNumberModule} from 'primeng/inputnumber';
import {TriStateCheckboxModule} from 'primeng/tristatecheckbox';
import {PanelModule} from 'primeng/panel';
import {ScrollPanelModule} from 'primeng/scrollpanel';
import {TabViewModule} from 'primeng/tabview';
import {OrganizationChartModule} from 'primeng/organizationchart';
import {CodeShortDescriptionPipe} from '../shared/pipes/code-short-description.pipe';
import {CodeDescriptionPipe} from '../shared/pipes/code-description.pipe';
import {CustomDissagregationOptionsListPipe} from '../shared/pipes/custom-dissagregation-options-list.pipe';
import {PickListModule} from 'primeng/picklist';
import {MarkersListPipe} from '../shared/pipes/markers-list.pipe';
import { MarkerAdministrationComponent } from './marker-administration/marker-administration.component';
import { PerformanceIndicatorAdministrationComponent } from './performance-indicator-administration/performance-indicator-administration.component';
import { PartnerProjectListAdministrationComponent } from './partner-project-list-administration/partner-project-list-administration.component';
import { PartnerProjectAdministrationComponent } from './partner-project-administration/partner-project-administration.component';
import { DirectImplementationAdministrationComponent } from './direct-implementation-administration/direct-implementation-administration.component';
import {ToggleButtonModule} from 'primeng/togglebutton';
import {CardModule} from 'primeng/card';
import {AccordionModule} from 'primeng/accordion';
import {TagModule} from 'primeng/tag';
import {FieldsetModule} from 'primeng/fieldset';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {MessageModule} from 'primeng/message';
import {SharedModule} from '../shared/shared.module';
import {BooleanYesNoPipe} from '../shared/pipes/boolean-yes-no.pipe';
import {IndicatorPipe} from '../shared/pipes/indicator.pipe';
import {CalendarModule} from 'primeng/calendar';
import {InputSwitchModule} from 'primeng/inputswitch';
import {MenuModule} from 'primeng/menu';
import {UserPipe} from '../shared/pipes/user.pipe';
import {EnumValuesToLabelPipe} from '../shared/pipes/enum-values-to-label.pipe';
import {MonthPipe} from '../shared/pipes/month.pipe';
import {MonthListPipe} from '../shared/pipes/month-list.pipe';
import {DissagregationsAssignationToIndicatorPipe} from '../shared/pipes/dissagregations-assignation-to-indicator.pipe';
import {CustomDissagregationsAssignationToIndicatorPipe} from '../shared/pipes/custom-dissagregations-assignation-to-indicator.pipe';
import { AppconfigurationComponent } from './appconfiguration/appconfiguration.component';


@NgModule({
    declarations: [
        UserAdministrationComponent,
        AreasAdministrationComponent,
        PillarAdministrationComponent,
        PeriodAdministrationComponent,
        SituationAdministrationComponent,
        OfficeAdministrationComponent,
        StatementAdministrationComponent,
        CustomDissagregationAdministrationComponent,
        OrganizationAdministrationComponent,
        MarkerAdministrationComponent,
        PerformanceIndicatorAdministrationComponent,
        PartnerProjectListAdministrationComponent,
        PartnerProjectAdministrationComponent,
        DirectImplementationAdministrationComponent,
        AppconfigurationComponent
    ],
    imports: [
        CommonModule,
        AdministrationRoutingModule,
        ToolbarModule,
        TableModule,
        MultiSelectModule,
        FormsModule,
        ReactiveFormsModule,
        DropdownModule,
        ButtonModule,
        DialogModule,
        MessagesModule,
        CheckboxModule,
        SelectButtonModule,
        RippleModule,
        TooltipModule,
        InputTextModule,
        ToastModule,
        InputTextareaModule,
        InputNumberModule,
        TriStateCheckboxModule,
        PanelModule,
        ScrollPanelModule,
        TabViewModule,
        OrganizationChartModule,
        PickListModule,
        ToggleButtonModule,
        CardModule,
        AccordionModule,
        TagModule,
        FieldsetModule,
        MessageModule,
        ConfirmDialogModule,
        SharedModule,
        CalendarModule,
        InputSwitchModule,
        MenuModule
    ],
    providers: [
        OfficeOrganizationPipe,
        RolesListPipe,
        CodeShortDescriptionPipe,
        CodeDescriptionPipe,
        CustomDissagregationOptionsListPipe,
        MarkersListPipe,
        PercentPipe,
        BooleanYesNoPipe,
        IndicatorPipe,
        UserPipe,
        EnumValuesToLabelPipe,
        MonthPipe,
        MonthListPipe,
        DissagregationsAssignationToIndicatorPipe,
        CustomDissagregationsAssignationToIndicatorPipe
    ]

})
export class AdministrationModule {
}
