import {Component, OnInit} from '@angular/core';
import {UserService} from '../../shared/services/user.service';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
    render = false;
    projectIdsFocalPoint: number[];

    constructor(
        private userService: UserService
    ) {
    }

    ngOnInit(): void {
        this.userService.currentUserSubject.subscribe(value => {
            if (value.roles.filter(value1 => {
                return value1.name === 'PUNTO_FOCAL';
            }).length > 0) {
                this.projectIdsFocalPoint = value.focalPointProjects;
            } else {
                this.projectIdsFocalPoint = null;
            }
            console.log(this.projectIdsFocalPoint);
        });
    }

}
