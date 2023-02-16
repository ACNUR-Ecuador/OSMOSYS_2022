import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {ProjectService} from "../../services/project.service";
import {VersionCheckService} from "../../services/version-check.service";
import {environment} from '../../../environments/environment';
import {UtilsService} from "../../services/utils.service";
import {PeriodService} from "../../services/period.service";
import {MessageService} from "primeng/api";
import {Period} from "../../shared/model/OsmosysModel";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
    render = false;
    isAcnurUser: boolean;
    isFocalPoint: boolean;
    isPartner: boolean;
    isAdmin: boolean;
    focalPointProjectsIds: number[];

    currentPeriod: Period;
    periods: Period[];


    constructor(
        private userService: UserService,
        private projectService: ProjectService,
        private utilsService: UtilsService,
        private periodService: PeriodService,
        private versionCheckService: VersionCheckService,
        private messageService: MessageService
    ) {
    }

    ngOnInit(): void {
        this.loadPeriods();
        this.versionCheckService.checkVersion(environment.versionCheckURL);
    }

    loadUsers() {
        this.isAdmin = this.userService.hasAnyRole(['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']);
        this.isFocalPoint = this.userService.hasAnyRole(['PUNTO_FOCAL']);
        this.isAcnurUser = this.userService.isUNHCRUser();
        this.isPartner = !this.isAcnurUser;
        this.render = true;
        this.focalPointProjectsIds = this.userService.getLogedUsername().focalPointProjects;
    }

    loadPeriods() {
        this.periodService.getAll().subscribe({
            next: value1 => {
                this.periods = value1;
                this.currentPeriod = this.utilsService.getCurrectPeriodOrDefault(this.periods);
                this.loadUsers();
            }, error: error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: error.error.message
                });
            }
        });
    }


}
