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
import {ToggleButtonModule} from 'primeng/togglebutton';
import {ScrollPanelModule} from 'primeng/scrollpanel';
import {FieldsetModule} from 'primeng/fieldset';
import {EnumValuesToLabelPipe} from '../shared/pipes/enum-values-to-label.pipe';
import {BooleanYesNoPipe} from '../shared/pipes/boolean-yes-no.pipe';
import {DissagregationsAssignationToIndicatorPipe} from '../shared/pipes/dissagregations-assignation-to-indicator.pipe';
import {CustomDissagregationsAssignationToIndicatorPipe} from '../shared/pipes/custom-dissagregations-assignation-to-indicator.pipe';
import {FilterUtilsService} from '../shared/services/filter-utils.service';
import { PartnerProjectAdministrationComponent } from './partner-project-administration/partner-project-administration.component';
import { PartnerProjectListAdministrationComponent } from './partner-project-list-administration/partner-project-list-administration.component';
import { GeneralIndicatorConfigurationComponent } from './general-indicator-configuration/general-indicator-configuration.component';
import {CardModule} from 'primeng/card';
import {CalendarModule} from 'primeng/calendar';
import {ProjectService} from '../shared/services/project.service';
import {PanelModule} from 'primeng/panel';
import {MenuModule} from 'primeng/menu';
import {TriStateCheckboxModule} from 'primeng/tristatecheckbox';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {CodeDescriptionPipe} from '../shared/pipes/code-description.pipe';
import {QuarterService} from '../shared/services/quarter.service';
import {SharedModule} from '../shared/shared.module';
import { UserAdministrationComponent } from './user-administration/user-administration.component';
import {RolesListPipe} from '../shared/pipes/roles-list.pipe';
import {AccordionModule} from 'primeng/accordion';
import {CodeDescriptionListPipe} from '../shared/pipes/code-description-list.pipe';
import {RippleModule} from 'primeng/ripple';
import {IndicatorPipe} from '../shared/pipes/indicator.pipe';
import { DirectImplementationAdministrationComponent } from './direct-implementation-administration/direct-implementation-administration.component';
import {UserPipe} from '../shared/pipes/user.pipe';


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
        CustomDissagregationAdministrationComponent,
        PartnerProjectAdministrationComponent,
        PartnerProjectListAdministrationComponent,
        GeneralIndicatorConfigurationComponent,
        UserAdministrationComponent,
        DirectImplementationAdministrationComponent
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
        PickListModule,
        ToggleButtonModule,
        ScrollPanelModule,
        FieldsetModule,
        CardModule,
        CalendarModule,
        PanelModule,
        MenuModule,
        TriStateCheckboxModule,
        ConfirmDialogModule,
        SharedModule,
        CheckboxModule,
        AccordionModule,
        RippleModule
    ],
    providers: [
        ConfirmationService,
        OfficeOrganizationPipe,
        CodeShortDescriptionPipe,
        CodeDescriptionPipe,
        MarkersListPipe,
        CustomDissagregationOptionsListPipe,
        EnumValuesToLabelPipe,
        BooleanYesNoPipe,
        DissagregationsAssignationToIndicatorPipe,
        CustomDissagregationsAssignationToIndicatorPipe,
        FilterUtilsService,
        ProjectService,
        QuarterService,
        RolesListPipe,
        CodeDescriptionListPipe,
        IndicatorPipe,
        UserPipe
    ]
})
export class AdministrationModule {
}
