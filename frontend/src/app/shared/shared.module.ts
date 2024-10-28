import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ConfirmationService, MessageService} from 'primeng/api';
import {LoaderComponent} from './loader/loader.component';
import { OfficeOrganizationPipe } from './pipes/office-organization.pipe';
import {RolesListPipe} from './pipes/roles-list.pipe';
import { CodeShortDescriptionPipe } from './pipes/code-short-description.pipe';
import { CodeDescriptionPipe } from './pipes/code-description.pipe';
import { CustomDissagregationOptionsListPipe } from './pipes/custom-dissagregation-options-list.pipe';
import {IndicatorPipe} from './pipes/indicator.pipe';
import {EnumValuesToLabelPipe} from './pipes/enum-values-to-label.pipe';
import {DissagregationsAssignationToIndicatorPipe} from './pipes/dissagregations-assignation-to-indicator.pipe';
import { UserPipe } from './pipes/user.pipe';
import { MonthPipe } from './pipes/month.pipe';
import { MonthListPipe } from './pipes/month-list.pipe';
import {BooleanYesNoPipe} from './pipes/boolean-yes-no.pipe';
import { ValuesStatePipe } from './pipes/values-state.pipe';
import {
    CustomDissagregationsAssignationToIndicatorPipe
} from "./pipes/custom-dissagregations-assignation-to-indicator.pipe";
import { PeriodsFromIndicatorPipe } from './pipes/periods-from-indicator.pipe';
import { StatementPeriodStatementAsignationsListPipe } from './pipes/statement-period-statement-asignations-list.pipe';
import { StandardDissagreationListPipe } from './pipes/standard-dissagreation-list.pipe';
import { SafePipe } from './pipes/safe.pipe';
import { TagPeriodTagAsignationsListPipe } from './pipes/tag-period-tag-asignations-list.pipe';



@NgModule({
    declarations: [
        LoaderComponent,
        OfficeOrganizationPipe,
        RolesListPipe,
        CodeShortDescriptionPipe,
        CodeDescriptionPipe,
        CustomDissagregationOptionsListPipe,
        IndicatorPipe,
        CodeDescriptionPipe,
        EnumValuesToLabelPipe,
        DissagregationsAssignationToIndicatorPipe,
        UserPipe,
        MonthPipe,
        MonthListPipe,
        BooleanYesNoPipe,
        ValuesStatePipe,
        CustomDissagregationsAssignationToIndicatorPipe,
        PeriodsFromIndicatorPipe,
        StatementPeriodStatementAsignationsListPipe,
        OfficeOrganizationPipe,
        StandardDissagreationListPipe,
        SafePipe,
        TagPeriodTagAsignationsListPipe
    ],
    imports: [
        CommonModule
    ],
    exports: [
        LoaderComponent,
        IndicatorPipe,
        CodeDescriptionPipe,
        UserPipe,
        EnumValuesToLabelPipe,
        ValuesStatePipe,
        EnumValuesToLabelPipe,
        OfficeOrganizationPipe,
        SafePipe
    ],
    providers: [
        MessageService,
        ConfirmationService
    ]
})
export class SharedModule {
}
