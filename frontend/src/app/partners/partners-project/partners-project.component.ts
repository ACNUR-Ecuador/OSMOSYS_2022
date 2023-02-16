import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {FilterService, MenuItem, MessageService, SelectItem} from 'primeng/api';
import {ActivatedRoute} from '@angular/router';
import {IndicatorExecution, MonthState, Project} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable} from '../../shared/model/UtilsModel';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {EnumValuesToLabelPipe} from '../../shared/pipes/enum-values-to-label.pipe';
import {DialogService} from 'primeng/dynamicdialog';
import {GeneralIndicatorFormComponent} from '../../indicator-forms/general-indicator-form/general-indicator-form.component';
import {PerformanceIndicatorFormComponent} from '../../indicator-forms/performance-indicator-form/performance-indicator-form.component';
import {IndicatorPipe} from '../../shared/pipes/indicator.pipe';
import {HttpResponse} from '@angular/common/http';
import {UtilsService} from '../../services/utils.service';
import {FilterUtilsService} from '../../services/filter-utils.service';
import {ProjectService} from '../../services/project.service';
import {IndicatorExecutionService} from '../../services/indicator-execution.service';
import {UserService} from '../../services/user.service';
import {ReportsService} from '../../services/reports.service';

@Component({
    selector: 'app-partners-project',
    templateUrl: './partners-project.component.html',
    styleUrls: ['./partners-project.component.scss']
})
export class PartnersProjectComponent implements OnInit {

    public idProjectParam;

    public formItem: FormGroup;
    public states: SelectItem[];
    public project: Project;
    public monthsState: MonthState[];

    // tslint:disable-next-line:variable-name
    _selectedColumnsGeneralIndicators: ColumnTable[];
    colsGeneralIndicators: ColumnTable[];
    itemsReportTypeFull: MenuItem[];
// roles
    isAdmin = false;
    isProjectFocalPoint = false;
    isEjecutor = false;

    colsMonthState: ColumnTable[];

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
        this.colsMonthState = [
            {field: 'year', header: 'Año', type: ColumnDataType.numeric},
            {field: 'month', header: 'Mes', type: ColumnDataType.text}
        ];

    }

    private loadProject(idProject: number) {
        this.projectService.getProjectById(idProject).subscribe(value => {
            this.project = value;
            this.setRoles();
            this.projectService.getMonthsStateByProjectId(idProject)
                .subscribe({
                    next: value1 => {
                        this.monthsState = value1;
                    }, error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al cargar el estados de meses del proyecto',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
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

    viewDesagregationGeneralIndicator(parameters: Map<string, number | string | IndicatorExecution>) {
        const monthId = parameters.get('monthId') as number;
        const indicatorExecution = parameters.get('indicator') as IndicatorExecution;
        const month = parameters.get('month') as string;
        const year = parameters.get('year') as number;
        let title: string = indicatorExecution.indicator.description;
        if (month && year) {
            title = title + ' (' + month + '-' + year + ')';
        }
        const ref = this.dialogService.open(GeneralIndicatorFormComponent, {
                header: 'Indicador General: ' + title + this.getRoleTitle(),
                width: '98%',
                height: '80%',
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

    viewDesagregationPerformanceIndicator(parameters: Map<string, number | string | IndicatorExecution>) {
        const monthId = parameters.get('monthId') as number;
        const month = parameters.get('month') as string;
        const year = parameters.get('year') as number;
        const indicatorExecution = parameters.get('indicator') as IndicatorExecution;
        let title: string = this.indicatorPipe.transform(indicatorExecution.indicator);
        if (month && year) {
            title = title + ' (' + month + '-' + year + ')';
        }
        const ref = this.dialogService.open(PerformanceIndicatorFormComponent, {
                header: 'Indicador: ' + title + this.getRoleTitle(),
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

    private getRoleTitle(): string {

        const roles: string[] = [];
        if (this.isAdmin) {
            roles.push('Administrador');
        }
        if (this.isProjectFocalPoint) {
            roles.push('Punto Focal');
        }
        if (this.isEjecutor) {
            roles.push('Ejecutor');
        }
        if (roles.length > 0) {
            return ' (' + roles.join(', ') + ')';
        } else {
            return '';
        }
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
            },
            {
                label: 'Resumen Demográfico', tooltip: 'sdfgsdfg', icon: 'pi pi-file-excel', command: () => {
                    this.getDemographic();
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
   private getDemographic() {
        this.getReport('Demographic');
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
            case 'getPartnerDemographicByProjectId':
                reportObservable = this.reportsService.getDemographicByProjectId(this.project.id);
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

    private setRoles() {
        const userId = this.userService.getLogedUsername().id;
        const orgId = this.userService.getLogedUsername().organization.id;
        this.isAdmin = this.userService.hasAnyRole(['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']);
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

    changeProjectMonthBlocking(monthState: MonthState, $event: any) {
        monthState.blockUpdate = $event.checked;
        this.projectService.changeMonthStateByProjectId(this.project.id, monthState).subscribe({
            next: () => {
                console.log('done');
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cambiar el estado',
                    detail: err.error.message,
                    life: 3000
                });
            }, complete: () => {
                this.projectService.getMonthsStateByProjectId(this.project.id)
                    .subscribe({
                        next: value1 => {
                            this.monthsState = value1;
                        }, error: err => {
                            this.messageService.add({
                                severity: 'error',
                                summary: 'Error al cargar el estados de meses del proyecto',
                                detail: err.error.message,
                                life: 3000
                            });
                        }
                    });
            }
        });
    }

    reload() {
        this.loadProject(this.idProjectParam);
    }
}
