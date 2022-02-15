import {Component, OnInit} from '@angular/core';
import {MessageService, SelectItem} from 'primeng/api';
import {PeriodService} from '../../shared/services/period.service';
import {UtilsService} from '../../shared/services/utils.service';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {AreaResume, Period} from '../../shared/model/OsmosysModel';
import {UserService} from '../../shared/services/user.service';
import {UserPipe} from '../../shared/pipes/user.pipe';
import {OfficeService} from '../../shared/services/office.service';
import {EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {OfficeOrganizationPipe} from '../../shared/pipes/officeOrganization.pipe';
import {EnumsService} from '../../shared/services/enums.service';
import {AreaService} from '../../shared/services/area.service';

@Component({
    selector: 'app-areas-menu',
    templateUrl: './areas-menu.component.html',
    styleUrls: ['./areas-menu.component.scss']
})
export class AreasMenuComponent implements OnInit {
    numbers: number[];

    stateOptions: SelectItem[];
    officeOptions: SelectItem[];
    roleOptions: SelectItem[];
    userOptions: SelectItem[];
    periodOptions: SelectItem[];

    areas: AreaResume[];
    queryForm: FormGroup;

    constructor(private fb: FormBuilder,
                private periodService: PeriodService,
                private messageService: MessageService,
                private utilsService: UtilsService,
                private userService: UserService,
                private officeService: OfficeService,
                private enumsService: EnumsService,
                private areaService: AreaService,
                private userPipe: UserPipe,
                private officeOrganizationPipe: OfficeOrganizationPipe,
    ) {
        this.numbers = Array(21).fill(0).map((x, i) => i + 1);
    }

    ngOnInit(): void {
        this.createForms();
        this.loadOptions();
        this.loadPeriods();
    }

    createForms() {
        this.queryForm = this.fb.group({
            period: new FormControl(''),
            office: new FormControl(''),
            roles: new FormControl(''),
            user: new FormControl('')
        });
    }

    loadPeriods() {
        this.periodService.getAll().subscribe(value => {
            this.periodOptions = value
                .sort((a, b) => a.year - b.year)
                .map(period => {
                    return {
                        label: period.year.toString(),
                        value: period
                    };
                });
            const selectedPeriod: Period = this.utilsService.getCurrectPeriodOrDefault(value);
            if (!selectedPeriod) {
                return;
            }
            const currentUser = this.userService.getLogedUsername();
            this.queryForm.get('period').patchValue(selectedPeriod);
            this.queryForm.get('user').patchValue(currentUser);
            this.loadAreas(currentUser.id, selectedPeriod.id, null, true, true, false);
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las áreas',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    loadOptions() {
        this.userService.getActiveUNHCRUsers().subscribe(value => {
            this.userOptions = value.map(value1 => {
                return {
                    label: this.userPipe.transform(value1),
                    value: value1
                };
            });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error en carga de usuarios',
                detail: error.error.message,
                life: 3000
            });
        });

        this.officeService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.officeOptions = value.map(value1 => {
                return {
                    label: this.officeOrganizationPipe.transform(value1),
                    value: value1
                };
            });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error en carga de oficinas',
                detail: error.error.message,
                life: 3000
            });
        });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.stateOptions = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error en carga de estados',
                detail: error.error.message,
                life: 3000
            });
        });

        this.roleOptions = [];
        this.roleOptions.push({label: 'Responsable', value: 'assignedUser'});
        this.roleOptions.push({label: 'Responsable Alterno', value: 'assignedUserBackup'});
        this.roleOptions.push({label: 'Supervisor', value: 'supervisorUser'});
    }

    loadAreas(userId: number,
              periodId: number,
              officeId: number,
              supervisor: boolean,
              responsible: boolean,
              backup: boolean) {
        this.areaService.getDirectImplementationAreaResume(userId,
            periodId,
            officeId,
            supervisor,
            responsible,
            backup
        ).subscribe(value => {
            this.areas = value;
            console.log(this.areas);
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error en carga de indicadores',
                detail: error.error.message,
                life: 3000
            });
        });
    }
}
