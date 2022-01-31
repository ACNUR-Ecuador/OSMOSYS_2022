import {Component, OnInit} from '@angular/core';
import {UserService} from '../../shared/services/user.service';
import {ProjectService} from '../../shared/services/project.service';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
    render = false;
    projectIdsFocalPoint: number[];

    constructor(
        private userService: UserService,
        private projectService: ProjectService
    ) {
    }

    ngOnInit(): void {
        this.userService.currentUserSubject.subscribe(value => {
            if (value.roles.filter(value1 => {
                return value1.name === 'PUNTO_FOCAL';
            }).length > 0) {
                this.projectIdsFocalPoint = value.focalPointProjects;
            } else if (!this.userService.isUNHCRUser()) {
                // todo period!!!
                this.projectService.getProjectResumenWebByPeriodIdAndOrganizationId(1, value.organization.id).subscribe(value1 => {
                    this.projectIdsFocalPoint = value1.map(value2 => {
                        return value2.id;
                    });
                });
            }else {
                this.projectService.getProjectResumenWebByPeriodId(1).subscribe(value1 => {
                    this.projectIdsFocalPoint = value1.map(value2 => {
                        return value2.id;
                    });
                });
            }
        });
    }

}
