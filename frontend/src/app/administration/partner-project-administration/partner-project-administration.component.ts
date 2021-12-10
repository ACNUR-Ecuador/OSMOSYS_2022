import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder} from '@angular/forms';
import {FilterService, MessageService} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {UserService} from '../../shared/services/user.service';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';
import {Location} from '@angular/common';
import {Canton, Organization, Period, Project} from '../../shared/model/OsmosysModel';
import {OrganizationService} from '../../shared/services/organization.service';
import {CantonService} from '../../shared/services/canton.service';
import {EnumsState} from '../../shared/model/UtilsModel';

@Component({
    selector: 'app-partner-project-administration',
    templateUrl: './partner-project-administration.component.html',
    styleUrls: ['./partner-project-administration.component.scss']
})
export class PartnerProjectAdministrationComponent implements OnInit {
    public period: Period;
    public project: Project;
    public cantones: Canton[];
    public organizations: Organization[];


    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private location: Location,
        private fb: FormBuilder,
        private messageService: MessageService,
        public utilsService: UtilsService,
        private userService: UserService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private organizationService: OrganizationService,
        private cantonService: CantonService
    ) {

        const params = this.router.getCurrentNavigation().extras.state;
        this.period = params.period as Period;
        this.project = params.project as Project;
    }

    ngOnInit(): void {
        console.log(this.period);
        console.log(this.project);
        this.loadOptions();
    }

    loadOptions() {
        this.cantonService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.cantones = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los cantones',
                detail: error.error.message,
                life: 3000
            });
        });

        this.organizationService.getByState(EnumsState.ACTIVE).subscribe(value => {
           this.organizations = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las organizaciones',
                detail: error.error.message,
                life: 3000
            });
        });

    }


}
