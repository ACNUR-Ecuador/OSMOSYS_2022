import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {MessageService, SelectItem} from "primeng/api";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {IndicatorExecution, Month, Period, Quarter} from "../../shared/model/OsmosysModel";
import {PeriodService} from "../../services/period.service";
import {UtilsService} from "../../services/utils.service";
import {UserService} from "../../services/user.service";
import {OfficeService} from "../../services/office.service";
import {EnumsService} from "../../services/enums.service";
import {AreaService} from "../../services/area.service";
import {UserPipe} from "../../shared/pipes/user.pipe";
import {OfficeOrganizationPipe} from "../../shared/pipes/office-organization.pipe";
import {IndicatorPipe} from "../../shared/pipes/indicator.pipe";
import {IndicatorExecutionService} from "../../services/indicator-execution.service";
import {EnumsState, EnumsType} from "../../shared/model/UtilsModel";

@Component({
    selector: 'app-home-dashboard-direct-implementation',
    templateUrl: './home-dashboard-direct-implementation.component.html',
    styleUrls: ['./home-dashboard-direct-implementation.component.scss']
})
export class HomeDashboardDirectImplementationComponent implements OnInit {

    @Input()
    periods: Period[];
    @Input()
    currentPeriod: Period;
    @Input()
    isAdmin: boolean;

    stateOptions: SelectItem[];
    officeOptions: SelectItem[];
    roleOptions: SelectItem[];
    userOptions: SelectItem[];
    periodOptions: SelectItem[];
    indicatorExecutionOptions: SelectItem[];
    queryForm: FormGroup;
    indicatorExecutions: IndicatorExecution[];

    countPerformanceIndicators = 0;
    countPerformanceIndicatorsLate = 0;
    selectedPerformanceIndicator: IndicatorExecution;
    chartPerformanceMonths: any;
    chartMonthsOptions: any;
    chartPerformanceTotal: any;

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
                private indicatorExecutionService: IndicatorExecutionService,
                private changeDetectorRef: ChangeDetectorRef
    ) {
    }

    ngOnInit(): void {
        this.createForms();
        this.loadOptions();

    }

    createForms() {
        this.queryForm = this.fb.group({
            period: new FormControl(''),
            office: new FormControl(''),
            roles: new FormControl(''),
            user: new FormControl('')
        });
    }

    setOptions() {
        this.queryForm.get('period').patchValue(this.currentPeriod);
        const user = this.userService.getLogedUsername();
        const currentOffice = this.officeOptions.filter(value => {
            return user.office.id === value.value.id;
        }).pop();
        this.queryForm.get('office').patchValue(currentOffice.value);
        this.loadIndicatorExecutions();
        /*        this.periodService.getAll().subscribe(value => {
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

                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las áreas',
                        detail: error.error.message,
                        life: 3000
                    });
                });*/
    }

    loadIndicatorExecutions() {
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
        this.indicatorExecutionService.getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId(
            user ? user.id : null,
            period ? period.id : null,
            office ? office.id : null, supervisor, responsible, responsibleBackup
        ).subscribe(value => {
            this.indicatorExecutions = value;
            this.countPerformanceIndicators = this.indicatorExecutions.length;
            this.countPerformanceIndicatorsLate = this.indicatorExecutions.filter(value1 => value1.late==='LATE').length;
            this.indicatorExecutionOptions = this.indicatorExecutions
                .map(value1 => {
                    return {
                        label: this.indicatorPipe.transform(value1.indicator),
                        value: value1
                    };
                });
            if (this.indicatorExecutionOptions.length > 0) {
                this.selectedPerformanceIndicator = this.indicatorExecutionOptions[0].value;
                this.loadPerformanceIndicator(this.selectedPerformanceIndicator);
            } else {
                this.selectedPerformanceIndicator = null;
            }
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error en carga de indicadores',
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
            this.officeService.getByState(EnumsState.ACTIVE).subscribe(value => {
                this.officeOptions = value.map(value1 => {
                    return {
                        label: this.officeOrganizationPipe.transform(value1),
                        value: value1
                    };
                });
                this.enumsService.getByType(EnumsType.State).subscribe(value => {
                    this.stateOptions = value;
                    this.setOptions();
                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error en carga de estados',
                        detail: error.error.message,
                        life: 3000
                    });
                });
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error en carga de oficinas',
                    detail: error.error.message,
                    life: 3000
                });
            });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error en carga de usuarios',
                detail: error.error.message,
                life: 3000
            });
        });


        this.roleOptions = [];
        this.roleOptions.push({label: 'Responsable', value: 'assignedUser'});
        this.roleOptions.push({label: 'Responsable Alterno', value: 'assignedUserBackup'});
        this.roleOptions.push({label: 'Supervisor', value: 'supervisorUser'});
    }


    loadPerformanceIndicator(indicatorExecution: IndicatorExecution) {
        this.selectedPerformanceIndicator = indicatorExecution;
        this.createIndicatorTargetChart(indicatorExecution.quarters);
        let months: Month[] = [];
        indicatorExecution.quarters.forEach(value => {
            value.months.forEach(value1 => {
                months.push(value1);
            });
        });
        months = months.sort((a, b) => a.order - b.order);
        this.createMonthPerformanceChart(months);
    }

    createMonthPerformanceChart(months: Month[]) {
        months = months.sort((a, b) => a.order - b.order);
        this.chartMonthsOptions = {
            responsive: false,
            plugins: {
                legend: {
                    labels: {
                        color: '#495057'
                    }
                }
            },
            scales: {
                x: {
                    ticks: {
                        color: '#495057'
                    },
                    grid: {
                        color: '#ebedef'
                    }
                },
                y: {
                    min: 0,
                    ticks: {
                        color: '#495057'
                    },
                    grid: {
                        color: '#ebedef'
                    }
                }
            }
        };
        const monthsLabels: string[] = [];
        const monthsExecution: number[] = [];
        let monthsExecutionAcumulatedTmp = 0;
        months.forEach(value => {
            monthsLabels.push(value.month);
            monthsExecution.push(value.totalExecution ? value.totalExecution : 0);
            monthsExecutionAcumulatedTmp += value.totalExecution;
        });
        this.chartPerformanceMonths = {
            labels: monthsLabels,
            datasets: [
                {
                    label: 'Ejecución Mensual',
                    type: 'bar',
                    data: monthsExecution,
                    backgroundColor: '#42A5F5',
                    tension: .4
                }]
        };
        this.changeDetectorRef.detectChanges();
        // this.chartBeneficiarios.reinit();

    }

    createIndicatorTargetChart(quarters: Quarter[]) {
        quarters = quarters.sort((a, b) => a.order - b.order);

        const quartersLabels: string[] = [];
        const quartersExecution: number[] = [];
        let monthsExecutionAcumulatedTmp = 0;
        quarters.forEach(value => {
            quartersLabels.push(value.quarter + '-' + value.year);
            quartersExecution.push(value.totalExecution ? value.totalExecution : 0);
            monthsExecutionAcumulatedTmp += value.totalExecution;
        });
        this.chartPerformanceTotal = {
            labels: quartersLabels,
            datasets: [
                {
                    label: 'Ejecución trimestral',
                    type: 'bar',
                    data: quartersExecution,
                    backgroundColor: '#42A5F5',
                    tension: .4
                }
            ]
        };
        this.changeDetectorRef.detectChanges();
    }

}
