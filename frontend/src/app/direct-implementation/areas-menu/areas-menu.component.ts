import {Component, OnInit} from '@angular/core';
import {MessageService, SelectItem} from "primeng/api";
import {Area, AreaResume, Indicator, Period} from "../../shared/model/OsmosysModel";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {PeriodService} from "../../services/period.service";
import {UtilsService} from "../../services/utils.service";
import {UserService} from "../../services/user.service";
import {OfficeService} from "../../services/office.service";
import {EnumsService} from "../../services/enums.service";
import {AreaService} from "../../services/area.service";
import {UserPipe} from "../../shared/pipes/user.pipe";
import {OfficeOrganizationPipe} from "../../shared/pipes/office-organization.pipe";
import {IndicatorPipe} from "../../shared/pipes/indicator.pipe";
import {Router} from "@angular/router";
import {EnumsState, EnumsType} from "../../shared/model/UtilsModel";

@Component({
    selector: 'app-areas-menu',
    templateUrl: './areas-menu.component.html',
    styleUrls: ['./areas-menu.component.scss']
})
export class AreasMenuComponent implements OnInit {

    stateOptions: SelectItem[];
    officeOptions: SelectItem[];
    roleOptions: SelectItem[];
    userOptions: SelectItem[];
    periodOptions: SelectItem[];
    indicatorOptions: SelectItem[];

    areas: AreaResume[];
    highlightArea: Area;
    queryForm: FormGroup;
    indicatorForm: FormGroup;
    viewAllDisabled = true;

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
                private indicatorPipe: IndicatorPipe,
                private router: Router
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
        this.indicatorForm = this.fb.group({
            indicator: new FormControl('')
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
            this.queryForm.get('roles').patchValue(this.roleOptions.map(value1 => value1.value));
            // this.loadAreas(currentUser.id, selectedPeriod.id, null, true, true, true);
            this.loadAreas(currentUser.id, selectedPeriod.id, null, true, true, true);
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
        this.messageService.clear();
        this.areaService.getDirectImplementationAreaResume(userId,
            periodId,
            officeId,
            supervisor,
            responsible,
            backup
        ).subscribe(value => {
            this.areas = value;
            this.loadIndicatorOptions();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error en carga de indicadores',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    loadIndicatorOptions() {
        this.highlightArea = null;
        this.indicatorOptions = this.areas
            .map(value => {
                return value.indicators;
            })
            .reduce((previousValue, currentValue) => previousValue.concat(currentValue), [])
            .map(indicator => {
                return {
                    value: indicator,
                    label: this.indicatorPipe.transform(indicator)
                } as SelectItem;
            });
        if (this.indicatorOptions.length < 1) {
            this.viewAllDisabled = true;
            this.messageService.clear();
            this.messageService.add({
                severity: 'warn',
                summary: 'No se encontraron indicadores, prueba con los filtros',
                life: 3000
            });
        } else {
            this.viewAllDisabled = false;
        }
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
        this.loadAreas(user ? user.id : null,
            period ? period.id : null,
            office ? office.id : null, supervisor, responsible, responsibleBackup);
    }

    goToArea(area: AreaResume) {
        this.router.navigateByUrl('/directImplementation/indicatorLists',
            {state: {indicatorExecutionIds: area.indicatorExecutionIds}});

    }

    viewAll() {
        const indicatorExecutionsIds: number[] =
            this.areas
                .map(value => value.indicatorExecutionIds)
                .reduce((previousValue, currentValue) => previousValue.concat(currentValue), []);
        this.router.navigateByUrl('/directImplementation/indicatorLists',
            {state: {indicatorExecutionIds: indicatorExecutionsIds}});

    }

    highlightIndicator(indicator: Indicator) {
        if (!indicator) {
            this.highlightArea = null;
            return;
        }
        this.highlightArea = this.areas.filter(area => {
            return area.indicators.some(indicatorF => indicator.id === indicatorF.id);
        }).pop().area;
    }

    getButtonStyles(areaResume: AreaResume): string {
        let result: string;
        if (areaResume.numberOfLateIndicators > 0) {
            result = 'p-button-outlined p-button-raised p-button-rounded p-button-danger ';
        } else if (areaResume.numberOfSoonReportIndicators > 0) {
            result = 'p-button-outlined p-button-raised p-button-rounded p-button-warning '
        } else {
            result = 'p-button-outlined p-button-raised p-button-rounded p-button-info'
        }

        if (this.highlightArea && areaResume.area.id === this.highlightArea.id) {
            result = 'p-button-rounded p-button-warning';
        }
        return result;
    }
}
