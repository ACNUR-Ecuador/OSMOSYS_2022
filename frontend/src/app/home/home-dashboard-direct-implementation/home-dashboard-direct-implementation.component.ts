import {Component, OnInit} from '@angular/core';
import {MessageService, SelectItem} from 'primeng/api';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {PeriodService} from '../../shared/services/period.service';
import {UtilsService} from '../../shared/services/utils.service';
import {UserService} from '../../shared/services/user.service';
import {OfficeService} from '../../shared/services/office.service';
import {EnumsService} from '../../shared/services/enums.service';
import {AreaService} from '../../shared/services/area.service';
import {UserPipe} from '../../shared/pipes/user.pipe';
import {OfficeOrganizationPipe} from '../../shared/pipes/officeOrganization.pipe';
import {Period} from '../../shared/model/OsmosysModel';
import {EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';

@Component({
    selector: 'app-home-dashboard-direct-implementation',
    templateUrl: './home-dashboard-direct-implementation.component.html',
    styleUrls: ['./home-dashboard-direct-implementation.component.scss']
})
export class HomeDashboardDirectImplementationComponent implements OnInit {

    stateOptions: SelectItem[];
    officeOptions: SelectItem[];
    roleOptions: SelectItem[];
    userOptions: SelectItem[];
    periodOptions: SelectItem[];
    indicatorExecutionOptions: SelectItem[];
    queryForm: FormGroup;

    constructor(private fb: FormBuilder,
                private periodService: PeriodService,
                private messageService: MessageService,
                public utilsService: UtilsService,
                private userService: UserService,
                private officeService: OfficeService,
                private enumsService: EnumsService,
                private areaService: AreaService,
                private userPipe: UserPipe,
                private officeOrganizationPipe: OfficeOrganizationPipe,
                private indicatorExecutionService: IndicatorExecutionService
    ) {
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
            this.queryForm.get('roles').patchValue(this.roleOptions.filter(value1 => value1.value === 'supervisorUser' || value1.value === 'assignedUser').map(value1 => value1.value));
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

    search() {
        const {
            period,
            office,
            roles,
            user,
        } = this.queryForm.value;
        const rolesF = roles as string[];
        const responsible = rolesF.includes('assignedUser');
        const responsibleBackup = rolesF.includes('assignedUserBackup');
        const supervisor = rolesF.includes('supervisorUser');
        this.indicatorExecutionService
            .getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId(
                user.id, period.id, office.id, supervisor, responsible, responsibleBackup)
            .subscribe(value => {
                this.indicatorExecutionOptions = value.map(value1 => {
                    return {
                        label: value1.id + '',
                        value: value1
                    };
                });
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los indicadores',
                    detail: error.error.message,
                    life: 3000
                });
            });
    }

}
