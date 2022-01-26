import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserAdministrationComponent} from '../administration/user-administration/user-administration.component';
import {AreasAdministrationComponent} from '../administration/areas-administration/areas-administration.component';
import {PeriodAdministrationComponent} from '../administration/period-administration/period-administration.component';
import {PillarAdministrationComponent} from '../administration/pillar-administration/pillar-administration.component';
import {SituationAdministrationComponent} from '../administration/situation-administration/situation-administration.component';
import {OrganizationAdministrationComponent} from '../administration/organization-administration/organization-administration.component';
import {OfficeAdministrationComponent} from '../administration/office-administration/office-administration.component';
import {StatementAdministrationComponent} from '../administration/statement-administration/statement-administration.component';
import {MarkerAdministrationComponent} from '../administration/marker-administration/marker-administration.component';
import {CustomDissagregationAdministrationComponent} from '../administration/custom-dissagregation-administration/custom-dissagregation-administration.component';
import {PerformanceIndicatorAdministrationComponent} from '../administration/performance-indicator-administration/performance-indicator-administration.component';
import {GeneralIndicatorConfigurationComponent} from '../administration/general-indicator-configuration/general-indicator-configuration.component';
import {PartnerProjectAdministrationComponent} from '../administration/partner-project-administration/partner-project-administration.component';
import {PartnerProjectListAdministrationComponent} from '../administration/partner-project-list-administration/partner-project-list-administration.component';
import {ChangePasswordComponent} from './change-password/change-password.component';
import {UserProfileComponent} from './user-profile/user-profile.component';

const routes: Routes = [
    {
        path: '',
        children: [
            {path: 'changePassword', component: ChangePasswordComponent},
            {path: 'userProfile', component: UserProfileComponent}
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class UsersRoutingModule {
}
