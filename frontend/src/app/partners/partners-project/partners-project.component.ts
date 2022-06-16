import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {FilterService, MenuItem, MessageService, SelectItem} from 'primeng/api';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';
import {UtilsService} from '../../shared/services/utils.service';
import {ProjectService} from '../../shared/services/project.service';
import {ActivatedRoute} from '@angular/router';
import {IndicatorExecution, Period, Project, QuarterState} from '../../shared/model/OsmosysModel';
import {ColumnTable} from '../../shared/model/UtilsModel';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {EnumValuesToLabelPipe} from '../../shared/pipes/enum-values-to-label.pipe';
import {trigger, state, style, transition, animate} from '@angular/animations';
import {DialogService} from 'primeng/dynamicdialog';
import {GeneralIndicatorFormComponent} from '../../indicator-forms/general-indicator-form/general-indicator-form.component';
import {PerformanceIndicatorFormComponent} from '../../indicator-forms/performance-indicator-form/performance-indicator-form.component';
import {IndicatorPipe} from '../../shared/pipes/indicator.pipe';
import {ReportsService} from '../../shared/services/reports.service';
import {HttpResponse} from '@angular/common/http';
import {UserService} from '../../shared/services/user.service';

@Component({
    selector: 'app-partners-project',
    templateUrl: './partners-project.component.html',
    styleUrls: ['./partners-project.component.scss'],
    animations: [
        trigger('rowExpansionTrigger', [
            state('void', style({
                transform: 'translateX(-10%)',
                opacity: 0
            })),
            state('active', style({
                transform: 'translateX(0)',
                opacity: 1
            })),
            transition('* <=> *', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
        ])
    ]
})
export class PartnersProjectComponent implements OnInit {

    public idProjectParam;

    public formItem: FormGroup;
    public states: SelectItem[];
    public project: Project;
    public quarterStates: QuarterState[];
    public generalIndicators: IndicatorExecution[];

    // tslint:disable-next-line:variable-name
    _selectedColumnsGeneralIndicators: ColumnTable[];
    colsGeneralIndicators: ColumnTable[];
    itemsReportTypeFull: MenuItem[];
    public projectType: string = null;
// roles
    isAdmin = false;
    isProjectFocalPoint = false;
    isEjecutor = false;

    constructor(
        public dialogService: DialogService,
        public utilsService: UtilsService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private messageService: MessageService,
        private projectService: ProjectService,
        private indicatorExecutionService: IndicatorExecutionService,
        private codeDescriptionPipe: CodeDescriptionPipe,
        private enumValuesToLabelPipe: EnumValuesToLabelPipe,
        private indicatorPipe: IndicatorPipe,
        private route: ActivatedRoute,
        private reportsService: ReportsService,
        private userService: UserService
    ) {
        this.idProjectParam = this.route.snapshot.paramMap.get('projectId');
        if (this.idProjectParam === 'null') {
            this.idProjectParam = null;
        }

    }

    ngOnInit(): void {
        this.loadProject(this.idProjectParam);
        this.generateItemsReportType();

    }

    private loadProject(idProject: number) {
        this.projectService.getProjectById(idProject).subscribe(value => {
            this.project = value;
            this.setRoles();
            this.projectService.getQuartersStateByProjectId(idProject).subscribe(value1 => {
                this.quarterStates = value1;
                console.log(this.quarterStates);
            });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar el proyecto',
                detail: error.error.message,
                life: 3000
            });
        });
    }


    @Input() get selectedColumnsGeneralIndicators(): any[] {
        return this._selectedColumnsGeneralIndicators;
    }

    set selectedColumnsGeneralIndicators(val: any[]) {
        // restore original order
        this._selectedColumnsGeneralIndicators = this.colsGeneralIndicators.filter(col => val.includes(col));
    }

    viewDesagregationGeneralIndicator(parameters: Map<string, number | IndicatorExecution>) {
        const monthId = parameters.get('monthId') as number;
        const indicatorExecution = parameters.get('indicator') as IndicatorExecution;
        const ref = this.dialogService.open(GeneralIndicatorFormComponent, {
                header: 'Indicador General: ' + indicatorExecution.indicator.description,
                width: '90%',
                height: '90%',
                closeOnEscape: false,
                autoZIndex: false,
                closable: false,
                baseZIndex: 1000,
                modal: true,
                dismissableMask: false,

                data: {
                    indicatorExecution,
                    monthId,
                    projectId: this.project.id,
                    isAdmin: this.isAdmin,
                    isProjectFocalPoint: this.isProjectFocalPoint,
                    isEjecutor: this.isEjecutor
                }
            }
        );
        ref.onClose.subscribe(() => {
            this.loadProject(this.idProjectParam);
        }, () => {
            this.loadProject(this.idProjectParam);
        });
    }

    viewDesagregationPerformanceIndicator(parameters: Map<string, number | IndicatorExecution>) {
        const monthId = parameters.get('monthId') as number;
        const indicatorExecution = parameters.get('indicator') as IndicatorExecution;
        const ref = this.dialogService.open(PerformanceIndicatorFormComponent, {
                header: 'Indicador: ' + this.indicatorPipe.transform(indicatorExecution.indicator),
                width: '90%',
                height: '90%',
                closeOnEscape: false,
                autoZIndex: false,
                closable: false,

                data: {
                    indicatorExecution,
                    monthId,
                    projectId: this.project.id,
                    isAdmin: this.isAdmin,
                    isProjectFocalPoint: this.isProjectFocalPoint,
                    isEjecutor: this.isEjecutor
                }
            }
        );
        // noinspection JSUnusedLocalSymbols
        ref.onClose.subscribe(() => {
            this.loadProject(this.idProjectParam);
        }, error => {
            this.loadProject(this.idProjectParam);
        });
    }

    private generateItemsReportType() {
        this.itemsReportTypeFull = [
            {
                label: 'Total', icon: 'pi pi-file-excel', command: () => {
                    this.getReportAnnual();
                }
            },
            {
                label: 'Trimestral', icon: 'pi pi-file-excel', command: () => {
                    this.getReportQuarterly();
                }
            },
            {
                label: 'Mensual', icon: 'pi pi-file-excel', command: () => {
                    this.getReportMonthly();
                }
            },
            {
                label: 'Con Desagregaciones', tooltip: 'sdfgsdfg', icon: 'pi pi-file-excel', command: () => {
                    this.getReportDetailed();
                }
            }
        ];
    }

    private getReportAnnual() {
        this.getReport('Annual');
    }

    private getReportQuarterly() {
        this.getReport('Quarterly');
    }

    private getReportMonthly() {
        this.getReport('Monthly');
    }

    private getReportDetailed() {
        this.getReport('Detailed');
    }


    public getReport(type: string) {
        const reportName = 'getPartnerXXXByProjectId';
        this.messageService.clear();
        const report: string = reportName.replace('XXX', type);
        let reportObservable = null;
        switch (report) {
            case 'getPartnerAnnualByProjectId':
                reportObservable = this.reportsService.getPartnerAnnualByProjectId(this.project.id);
                break;
            case 'getPartnerQuarterlyByProjectId':
                reportObservable = this.reportsService.getPartnerQuarterlyByProjectId(this.project.id);
                break;

            case 'getPartnerMonthlyByProjectId':
                reportObservable = this.reportsService.getPartnerMonthlyByProjectId(this.project.id);
                break;

            case 'getPartnerDetailedByProjectId':
                reportObservable = this.reportsService.getPartnerDetailedByProjectId(this.project.id);
                break;
            default: {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Reporte no implementado',
                    detail: report,
                    life: 3000
                });
                return;
            }
        }
        reportObservable.subscribe((response: HttpResponse<Blob>) => {
            this.utilsService.downloadFileResponse(response);
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al Generar el Reporte',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    changeQuarterState(quarter, event) {
        quarter.blockUpdate = !event.checked;
        console.log('--------------------');
        console.log(quarter);
        console.log(event);
        console.log(this.quarterStates);
        this.projectService.blockQuarterStateByProjectId(this.project.id, quarter).subscribe(value => {
            this.quarterStates = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al Actualizar el Trimestre',
                detail: error.error.message,
                life: 3000
            });
            this.projectService.getQuartersStateByProjectId(this.project.id).subscribe(value => {
                this.quarterStates = value;
            });
        });
    }

    private setRoles() {
        const userId = this.userService.getLogedUsername().id;
        const orgId = this.userService.getLogedUsername().organization.id;
        this.isAdmin = this.userService.hasAnyRole(['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']);
        this.isProjectFocalPoint = this.project.focalPoint && this.project.focalPoint.id === userId;
        this.isEjecutor = this.project.organization.id === orgId && this.userService.hasRole('EJECUTOR_PROYECTOS');
    }


    getPartnerLateReportByProjectId() {
        this.messageService.clear();

        this.reportsService.getPartnerLateReportByProjectId(this.project.id).subscribe((response: HttpResponse<Blob>) => {
            if (response.status === 204) {
                this.messageService.add({
                    severity: 'success',
                    summary: 'No tienes indicadores pendientes, gracias por tu colaboración',
                    life: 3000
                });
            } else {
                this.utilsService.downloadFileResponse(response);
            }

        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al Generar el Reporte',
                detail: error.error.message,
                life: 3000
            });
        });
    }
    getPartnerLateReviewByProjectId() {
        this.messageService.clear();

        this.reportsService.getPartnerLateReviewByProjectId(this.project.id).subscribe((response: HttpResponse<Blob>) => {
            if (response.status === 204) {
                this.messageService.add({
                    severity: 'success',
                    summary: 'No tienes indicadores pendientes, gracias por tu colaboración',
                    life: 3000
                });
            } else {
                this.utilsService.downloadFileResponse(response);
            }

        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al Generar el Reporte',
                detail: error.error.message,
                life: 3000
            });
        });
    }
}
