import {Component} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {UserService} from '../../services/user.service';
import {MessageService} from 'primeng/api';
import {Router} from '@angular/router';
import {User} from '../../shared/model/User';
import {environment} from "../../../environments/environment";
import { ColumnTable } from 'src/app/shared/model/UtilsModel';
import {UtilsService} from '../../services/utils.service';
import { IndicatorExecution } from 'src/app/shared/model/OsmosysModel';
import * as FileSaver from 'file-saver';
import * as _ from 'lodash';

import * as ExcelJS from 'exceljs';
import * as fs from 'fs';

@Component({
    selector: 'app-app-login',
    templateUrl: './app-login.component.html',
    styleUrls: ['./app-login.component.scss']
})
export class AppLoginComponent{
    loginPhotoFile=`assets/layout/images/${environment.loginPhoto}`;
    flagLoginFile=`assets/layout/images/${environment.flagLoginFile}`;
    public performanceIndicators: IndicatorExecution[];

    _selectedColumnsPerformanceIndicators: ColumnTable[];


    constructor(
        public router: Router,
        private fb: FormBuilder,
        private userService: UserService,
        private messageService: MessageService,
        public utilsService: UtilsService,
    ) {
    }

    public loginForm = this.fb.group({
        username: ['', [Validators.required]],
        password: ['', [Validators.required]]
    });


    login() {
        this.messageService.clear();
        if (this.loginForm.invalid) {
            return;
        } else {
            const user = new User();
            user.username = this.loginForm.controls.username.value as string;
            user.password = this.loginForm.controls.password.value as string;
            this.userService.login(user).subscribe({
                next: () => {
                    // noinspection JSIgnoredPromiseFromCall
                    this.router.navigateByUrl('/');
                },
                error: (error) => {
                    if (error.status === 0) {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Al momento estamos realizando mantenimiento del sistema. Por favor vuelve a intentar despues de 30 minutos'
                        });
                    } else {
                        this.messageService.add({severity: 'error', summary: 'Usuario o contraseña incorrectos'});
                    }
                }
            });


        }
    }
        


}
