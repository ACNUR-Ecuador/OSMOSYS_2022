import {AfterViewInit, ChangeDetectorRef, Component, Input, OnInit, ViewChild} from '@angular/core';
import {UIChart} from "primeng/chart";
import {MessageService, SelectItem} from "primeng/api";
import {IndicatorExecution, Month, Period, ProjectResume, Quarter} from "../../shared/model/OsmosysModel";
import {ProjectService} from "../../services/project.service";
import {IndicatorExecutionService} from "../../services/indicator-execution.service";
import {MonthService} from "../../services/month.service";
import {EnumsService} from "../../services/enums.service";
import {UserService} from "../../services/user.service";

@Component({
    selector: 'app-home-dashboard-focal-point',
    templateUrl: './home-dashboard-focal-point.component.html',
    styleUrls: ['./home-dashboard-focal-point.component.scss']
})
export class HomeDashboardFocalPointComponent implements OnInit, AfterViewInit {


    @Input()
    periods: Period[];
    @Input()
    currentPeriod: Period;
    @Input()
    isAcnurUser: boolean;
    @Input()
    isFocalPoint: boolean;
    @Input()
    isPartner: boolean;
    @Input()
    isAdmin: boolean;
    @Input()
    focalPointProjectsIds: number[];



    @ViewChild('chartBeneficirios', {static: false}) chartBeneficiarios: UIChart;

    projects: SelectItem[] = [];
    selectedProject: ProjectResume;
    selectedPeriod: Period;
    generalIndicator: IndicatorExecution;
    performanceIndicators: IndicatorExecution[];
    performanceIndicatorsSelectList: SelectItem[];
    selectedPerformanceIndicator: IndicatorExecution;
    countPerformanceIndicators = 0;
    countPerformanceIndicatorsLate = 0;
    renderProject = false;

    chartBeneficiariesMonths: any;
    chartBeneficiariesMonthsOptions: any;
    chartBeneficiariesTarget: any;
    chartPerformanceMonths: any;
    chartPerformanceTarget: any;
    generalIndicatorLate: boolean;


    constructor(
        private projectService: ProjectService,
        private messageService: MessageService,
        private indicatorExecutionService: IndicatorExecutionService,
        private monthService: MonthService,
        private enumsService: EnumsService,
        private userService: UserService,
        private changeDetectorRef: ChangeDetectorRef
    ) {
    }

    // eslint-disable-next-line
    ngAfterViewInit() {
    }

    ngOnInit(): void {
        this.selectedPeriod = this.currentPeriod;
        this.selectedProject = null;

        this.loadProjectsByUser(this.currentPeriod);

    }

    private loadProjectsByUser(period: Period) {
        const user = this.userService.getLogedUsername();
        if (this.isFocalPoint) {
            this.projectService.getProjectResumenWebByPeriodIdAndFocalPointId(period.id, user.id)
                .subscribe({
                    next: projects => {
                        this.loadProjectsItems(projects);
                    }, error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al cargar los proyectos',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
                });
        } else if (this.isPartner) {
            this.projectService.getProjectResumenWebByPeriodIdAndOrganizationId(period.id, user.organization.id)
                .subscribe({
                    next: projects => {
                        this.loadProjectsItems(projects);
                    }, error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al cargar los proyectos',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
                });
        } else if (this.isAdmin) {
            this.projectService.getProjectResumenWebByPeriodId(period.id)
                .subscribe({
                    next: projects => {
                        this.loadProjectsItems(projects);

                    }, error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al cargar los proyectos',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
                });
        }

    }

    loadPeriod(value) {
        this.loadProjectsByUser(value);
    }

    private loadProjectsItems(projectResumes: ProjectResume[]) {
        this.projects = [];
        for (let projectResume of projectResumes) {
            this.projects.push({
                label: projectResume.organizationAcronym + '-' + projectResume.name+ '-' + projectResume.periodYear,
                value: projectResume
            });
        }
        this.projects.sort((a, b) => a.label.localeCompare(b.label));
        if (this.projects.length > 0) {
            this.selectedProject = this.projects[0].value;
            this.loadProject(this.selectedProject.id);
            this.changeDetectorRef.detectChanges();
        }

    }

    loadProject(projectId: number) {
        this.loadGeneralIndicator(projectId);


    }

    loadGeneralIndicator(projectId: number) {
        this.indicatorExecutionService.getGeneralIndicatorResume(projectId).subscribe(value => {
            if (value && value.length > 0) {
                this.generalIndicator = value[0];
                this.loadGeneralIndicatorMonths(projectId, this.generalIndicator.id);
                this.generalIndicatorLate = this.generalIndicator.late === 'LATE';
                this.createGeneralTargetChart(this.generalIndicator);
            }


            this.loadPerformanceIndicators(projectId);
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los proyectos',
                detail: error.error.message,
                life: 3000
            });
        });
    }


    loadGeneralIndicatorMonths(projectId: number, generalIndicatorId) {
        // todo flat
        this.monthService.getMonthsByIndicatorExecutionId(generalIndicatorId).subscribe(months => {
            this.createMonthGeneralChart(months);
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los proyectos',
                detail: error.error.message,
                life: 3000
            });
        });
        this.loadPerformanceIndicators(projectId);
    }

    createMonthGeneralChart(months: Month[]) {
        months = months.sort((a, b) => a.order - b.order);

        const monthsLabels: string[] = [];
        const monthsExecution: number[] = [];
        let monthsExecutionAcumulatedTmp = 0;
        months.forEach(value => {
            monthsLabels.push(value.month);
            monthsExecution.push(value.totalExecution ? value.totalExecution : 0);
            monthsExecutionAcumulatedTmp += value.totalExecution;
        });
        this.chartBeneficiariesMonths = {
            labels: monthsLabels,
            datasets: [
                {
                    label: 'Beneficiarios Totales del Proyecto Mensuales',
                    type: 'bar',
                    data: monthsExecution,
                    backgroundColor: '#42A5F5',
                    tension: .4
                }]
        };
        this.chartBeneficiariesMonthsOptions = {
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
        this.changeDetectorRef.detectChanges();
        // this.chartBeneficiarios.reinit();

    }

    createGeneralTargetChart(generalIndicator: IndicatorExecution) {
        this.chartBeneficiariesTarget = {
            labels: ['Valore totales del proyecto'],
            datasets: [
                {
                    label: 'Ejecución Total Reportada',
                    type: 'bar',
                    data: [generalIndicator.totalExecution],
                    backgroundColor: '#42A5F5',
                    tension: .4
                },
                {
                    label: 'Meta Total',
                    type: 'bar',
                    data: [generalIndicator.target],
                    backgroundColor: '#66BB6A',
                    tension: .4
                }
            ]
        };
        this.changeDetectorRef.detectChanges();
        // this.chartBeneficiarios.reinit();

    }

    createMonthPerformanceChart(months: Month[]) {
        months = months.sort((a, b) => a.order - b.order);

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
        this.chartBeneficiariesMonthsOptions = {
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
        this.changeDetectorRef.detectChanges();
        // this.chartBeneficiarios.reinit();

    }

    createIndicatorTargetChart(quarters: Quarter[]) {
        quarters = quarters.sort((a, b) => a.order - b.order);

        const quartersLabels: string[] = [];
        const quartersExecution: number[] = [];
        const quartersTargets: number[] = [];
        let monthsExecutionAcumulatedTmp = 0;
        quarters.forEach(value => {
            quartersLabels.push(value.quarter + '-' + value.year);
            quartersExecution.push(value.totalExecution ? value.totalExecution : 0);
            quartersTargets.push(value.target ? value.target : 0);
            monthsExecutionAcumulatedTmp += value.totalExecution;
        });
        this.chartPerformanceTarget = {
            labels: quartersLabels,
            datasets: [
                {
                    label: 'Ejecución trimestral',
                    type: 'bar',
                    data: quartersExecution,
                    backgroundColor: '#42A5F5',
                    tension: .4
                },
                {
                    label: 'Meta trimestral',
                    type: 'bar',
                    data: quartersTargets,
                    backgroundColor: '#66BB6A',
                    tension: .4
                }
            ]
        };
        this.changeDetectorRef.detectChanges();
        // this.chartBeneficiarios.reinit();

    }

    loadPerformanceIndicators(projectId: number) {

        this.indicatorExecutionService.getPerformanceIndicatorResume(projectId).subscribe(value => {
            if (value && value.length > 0) {
                this.performanceIndicators = value;
                this.performanceIndicatorsSelectList = [];
                value.forEach(value1 => {
                    this.performanceIndicatorsSelectList.push(
                        {
                            label: value1.indicator.code + '-' + value1.indicator.description,
                            value: value1
                        });
                });
                this.countPerformanceIndicators = value.length;
                this.countPerformanceIndicatorsLate = 0;
                this.countPerformanceIndicatorsLate = this.performanceIndicators.filter(value1 => value1.late==='LATE').length;
                this.loadPerformanceIndicator(this.performanceIndicators[0]);
            }

            this.renderProject = true;
            this.changeDetectorRef.detectChanges();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los proyectos',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    loadPerformanceIndicator(indicatorExecution: IndicatorExecution) {
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


}
