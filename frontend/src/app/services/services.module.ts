import {ModuleWithProviders, NgModule, Optional, SkipSelf} from '@angular/core';
import { CommonModule } from '@angular/common';
import {EnumsService} from './enums.service';
import {AreaService} from './area.service';
import {CantonService} from './canton.service';
import {CustomDissagregationService} from './custom-dissagregation.service';
import {FilterUtilsService} from './filter-utils.service';
import {IndicatorService} from './indicator.service';
import {IndicatorExecutionService} from './indicator-execution.service';
import {LoaderService} from './loader.service';
import {OfficeService} from './office.service';
import {OrganizationService} from './organization.service';
import {PeriodService} from './period.service';
import {PillarService} from './pillar.service';
import {SituationService} from './situation.service';
import {StatementService} from './statement.service';
import {UserService} from './user.service';
import {UtilsService} from './utils.service';



@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class ServicesModule {
    static forRoot(): ModuleWithProviders<any> {
        return {
            ngModule: ServicesModule,
            providers: [
                AreaService,
                CantonService,
                CustomDissagregationService,
                FilterUtilsService,
                IndicatorService,
                IndicatorExecutionService,
                LoaderService,
                OfficeService,
                OrganizationService,
                PeriodService,
                PillarService,
                SituationService,
                StatementService,
                UserService,
                UtilsService,
                EnumsService
            ]
        };
    }
    constructor(@Optional() @SkipSelf() parentModule: ServicesModule) {
        if (parentModule) {
            throw new Error('CoreModule already loaded; Import in root module only.');
        }
    }
}
