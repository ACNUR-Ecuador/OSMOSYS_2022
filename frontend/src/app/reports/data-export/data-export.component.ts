import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {MenuItem, MessageService, SelectItem} from 'primeng/api';
import {Period, Project} from "../../shared/model/OsmosysModel";
import {PeriodService} from "../../services/period.service";
import {UtilsService} from "../../services/utils.service";
import {ReportsService} from "../../services/reports.service";
import {HttpResponse} from "@angular/common/http";
import { EnumsState } from 'src/app/shared/model/UtilsModel';
import { AreaService } from 'src/app/services/area.service';
import { CodeShortDescriptionPipe } from 'src/app/shared/pipes/code-short-description.pipe';
import { ProjectService } from 'src/app/services/project.service';
import { CodeNamePipe } from 'src/app/shared/pipes/code-name.pipe';
import { IndicatorService } from 'src/app/services/indicator.service';
import { IndicatorPipe } from 'src/app/shared/pipes/indicator.pipe';
import { TagService } from 'src/app/services/tag.service';

@Component({
    selector: 'app-data-export',
    templateUrl: './data-export.component.html',
    styleUrls: ['./data-export.component.scss']
})
export class DataExportComponent implements OnInit {
    periodForm: FormGroup;
    periods: Period[];
    areasItems: SelectItem[];
    implementationTypes: SelectItem[];
    indicatorTypes: SelectItem[];
    tagsItems: SelectItem[];
    itemsReportTypeFull: MenuItem[];
    itemsReportTypeAnualMonthlyDetailed: MenuItem[];
    months:any
    projectsItems:SelectItem[];
    indicatorItems: SelectItem[];
    filterOptions: SelectItem[];
    selectedFilters:any[]=[]
    constructor(private fb: FormBuilder,
                private periodService: PeriodService,
                private messageService: MessageService,
                public utilsService: UtilsService,
                private reportsService: ReportsService,
                private areaService: AreaService,
                private codeShortDescriptionPipe: CodeShortDescriptionPipe,
                private codeNamePipe: CodeNamePipe,
                private projectService: ProjectService,
                private indicatorService: IndicatorService,
                private indicatorPipe: IndicatorPipe,
                private tagService: TagService) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.createForms();
        this.generateItemsReportType();
    }

    

    loadItems(){
        this.loadPeriods();
        this.months=this.getMonths();
        this.loadAreas();
        this.implementationTypes = [
            {
              label:"Socios",
              value:"partners"
            },
            {
              label:"Implementación Directa",
              value:"direct_impl"
            },
            {
              label:"Implementación Directa y Socios",
              value:"partner_direct_impl"
            },
        ]
        this.indicatorTypes = [
            {
                label:"Indicadores de Producto",
                value:"Producto"
            },
            {
                label:"Indicadores Generales",
                value:"General"
            },
            {
                label:"Indicadores Generales y de Producto",
                value:"Producto y General"
            },
        ]
        this.filterOptions = [
            {
                label:"Mes",
                value:"month"
            },
            {
                label:"Área",
                value:"area"
            },
            {
                label:"Proyecto",
                value:"project"
            },
            {
                label:"Indicador",
                value:"indicator"
            },
            {
                label:"Tags",
                value:"tags"
            },
        ]    
    }

    getMonths(): { value: number; label: string }[] {
        const formatter = new Intl.DateTimeFormat('es-ES', { month: 'long' }); // Cambia 'es-ES' por el idioma deseado
        return Array.from({ length: 12 }, (_, index) => {
            const date = new Date(0, index); // Crea una fecha para cada mes
            return { 
                value: index + 1, // El valor numérico del mes (1 a 12)
                label: formatter.format(date).charAt(0).toUpperCase() + formatter.format(date).slice(1) // Nombre del mes con capitalización
            };
        });
    }

    loadPeriods() {
        this.periodService.getAll().subscribe(value => {
            this.periods = value;
            if (this.periods.length < 1) {
                this.messageService.add({severity: 'error', summary: 'No se encontraron años', detail: ''});
            } else {
                const currentPeriod = this.utilsService.getCurrectPeriodOrDefault(this.periods);
                const currentPeriodOption = this.periods.filter(value1 => {
                    return value1.id === currentPeriod.id
                })[0];
                this.periodForm.get('selectedPeriod').patchValue(currentPeriodOption);
                this.loadProjects(currentPeriod.id);
                this.loadPerformanceIndicatorsOptions(currentPeriod.id);
                this.loadTags(currentPeriod.id);
            }
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las los años',
                detail: error.error.message,
                life: 3000
            });
        });
        
    }

    private loadAreas() {
        this.areaService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    this.areasItems = value.map(value1 => {
                        const selectItem: SelectItem = {
                            label: this.codeShortDescriptionPipe.transform(value1),
                            value: value1
                        };
                        return selectItem;
                    });
                    this.areasItems.sort((a, b) => {
                        return a.value.code.localeCompare(b.value.code);
                    });
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las áreas',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
    }

    private loadProjects(periodId: number) {
            this.projectService.getProjectResumenWebByPeriodId(periodId)
            .subscribe({
                next: value => {
                    this.projectsItems = value.map(value1 => {
                        const selectItem: SelectItem = {
                            label: this.codeNamePipe.transform(value1),
                            value: value1
                        };
                        return selectItem;
                    });
                    this.projectsItems.sort((a, b) => {
                        return a.value.code.localeCompare(b.value.code);
                    });
                }, 
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los proyectos',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });


    }

    loadPerformanceIndicatorsOptions(periodId: number) {
            this.indicatorService.getByPeriodAssignment(periodId)
                .subscribe({
                    next: value => {
                        this.indicatorItems = value
                            .map(value1 => {
                                const selectItem: SelectItem = {
                                    value: value1,
                                    label: this.indicatorPipe.transform(value1)
                                };
                                return selectItem;
                            });
                    },
                    error: error => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al cargar los indicadores del año',
                            detail: error.error.message,
                            life: 3000
                        });
                    }
                });
    }

    loadTags(periodId: number){
        this.tagService.getActiveByPeriodId(periodId).subscribe({
            next: value => {
                this.tagsItems = value
                            .map(value1 => {
                                const selectItem: SelectItem = {
                                    value: value1,
                                    label: value1.name
                                };
                                return selectItem;
                            });

            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los tags',
                    detail: err.error.message,
                    life: 3000,
                });
            },
        });
    }

    private createForms() {
        this.periodForm = this.fb.group({
            selectedPeriod: new FormControl(''),
            selectedReport: new FormControl(''),
            indicatorType: new FormControl(''),
            implementationType: new FormControl('partners'),
            selectedFilters: new FormControl(''),
            selectedMonths: new FormControl(''),
            selectedAreas: new FormControl(''),
            selectedProjects: new FormControl(''),
            selectedIndicators: new FormControl(''),
            selectedTags: new FormControl(''),
            
        });
    }


    public getReport(period: Period, reportName: string, type: string) {
        this.messageService.clear();
        const report: string = reportName.replace('XXX', type);
        let reportObservable = null;
        const selectedMonthList = this.periodForm.get("selectedMonths")?.value;

        const monthList = Array.isArray(this.periodForm.get("selectedMonths").value)
        ? selectedMonthList.map(month => month.value): [];
        
        const areaList = Array.isArray(this.periodForm.get("selectedAreas").value)
        ? selectedMonthList.map(month => month.value): [];  
        console.log(monthList)
        switch (report) {
            case 'getAllImplementationsAnnualByPeriodId':
                reportObservable = this.reportsService.getAllImplementationsAnnualByPeriodId(period.id);
                break;
            case 'getAllImplementationsQuarterlyByPeriodId':
                reportObservable = this.reportsService.getAllImplementationsQuarterlyByPeriodId(period.id);
                break;

            case 'getAllImplementationsMonthlyByPeriodId':
                reportObservable = this.reportsService.getAllImplementationsMonthlyByPeriodId(period.id);
                break;

            case 'getAllImplementationsDetailedByPeriodId':
                reportObservable = this.reportsService.getAllImplementationsDetailedByPeriodId(period.id);
                break;
            case 'getAllImplementationsPerformanceIndicatorsAnnualByPeriodId':
                reportObservable = this.reportsService.getAllImplementationsPerformanceIndicatorsAnnualByPeriodId(period.id);
                break;
            case 'getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId':
                reportObservable = this.reportsService.getAllImplementationsPerformanceIndicatorsQuarterlyByPeriodId(period.id);
                break;

            case 'getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId':
                reportObservable = this.reportsService.getAllImplementationsPerformanceIndicatorsMonthlyByPeriodId(period.id);
                break;

            case 'getAllImplementationsPerformanceIndicatorsDetailedByPeriodId':
                reportObservable = this.reportsService.getAllImplementationsPerformanceIndicatorsDetailedByPeriodId(period.id);
                break;
            case 'getPartnersAnnualByPeriodId':
                reportObservable = this.reportsService.getPartnersAnnualByPeriodId(period.id);
                break;
            case 'getPartnersQuarterlyByPeriodId':
                reportObservable = this.reportsService.getPartnersQuarterlyByPeriodId(period.id);
                break;

            case 'getPartnersMonthlyByPeriodId':
                reportObservable = this.reportsService.getPartnersMonthlyByPeriodId(period.id);
                break;

            case 'getPartnersDetailedByPeriodId':
                reportObservable = this.reportsService.getPartnersDetailedByPeriodId(period.id);
                break;
            case 'getPartnersGeneralIndicatorsAnnualByPeriodId':
                reportObservable = this.reportsService.getPartnersGeneralIndicatorsAnnualByPeriodId(period.id);
                break;

            case 'getPartnersGeneralIndicatorsMonthlyByPeriodId':
                reportObservable = this.reportsService.getPartnersGeneralIndicatorsMonthlyByPeriodId(period.id);
                break;

            case 'getPartnersGeneralIndicatorsDetailedByPeriodId':
                reportObservable = this.reportsService.getPartnersGeneralIndicatorsDetailedByPeriodId(period.id);
                break;

            case 'getPartnersPerformanceIndicatorsAnnualByPeriodId':
                reportObservable = this.reportsService.getPartnersPerformanceIndicatorsAnnualByPeriodId(period.id);
                break;
            case 'getPartnersPerformanceIndicatorsQuarterlyByPeriodId':
                reportObservable = this.reportsService.getPartnersPerformanceIndicatorsQuarterlyByPeriodId(period.id);
                break;

            case 'getPartnersPerformanceIndicatorsMonthlyByPeriodId':
                reportObservable = this.reportsService.getPartnersPerformanceIndicatorsMonthlyByPeriodId(period.id);
                break;

            case 'getPartnersPerformanceIndicatorsDetailedByPeriodId':
                reportObservable = this.reportsService.getPartnersPerformanceIndicatorsDetailedByPeriodId(period.id);
                break;

            case 'getDirectImplementationPerformanceIndicatorsAnnualByPeriodId':
                reportObservable = this.reportsService.getDirectImplementationPerformanceIndicatorsAnnualByPeriodId(period.id);
                break;
            case 'getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId':
                reportObservable = this.reportsService.getDirectImplementationPerformanceIndicatorsQuarterlyByPeriodId(period.id);
                break;

            case 'getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId':
                reportObservable = this.reportsService.getDirectImplementationPerformanceIndicatorsMonthlyByPeriodId(period.id);
                break;

            case 'getDirectImplementationPerformanceIndicatorsDetailedByPeriodId':
                reportObservable = this.reportsService.getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(period.id);
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
                label: 'Con Desagregaciones', icon: 'pi pi-file-excel', command: () => {
                    this.getReportDetailed();
                }
            }
        ];
        this.itemsReportTypeAnualMonthlyDetailed = [
            {
                label: 'Total', icon: 'pi pi-file-excel', command: () => {
                    this.getReportAnnual();
                }
            },
            {
                label: 'Mensual', icon: 'pi pi-file-excel', command: () => {
                    this.getReportMonthly();
                }
            },
            {
                label: 'Con Desagregaciones', icon: 'pi pi-file-excel', command: () => {
                    this.getReportDetailed();
                }
            }
        ];
    }

    private getReportAnnual() {
        this.getReport(this.periodForm.get('selectedPeriod').value as Period, this.periodForm.get('selectedReport').value as string, 'Annual');
    }

    private getReportQuarterly() {
        this.getReport(this.periodForm.get('selectedPeriod').value as Period, this.periodForm.get('selectedReport').value as string, 'Quarterly');
    }

    private getReportMonthly() {
        this.getReport(this.periodForm.get('selectedPeriod').value as Period, this.periodForm.get('selectedReport').value as string, 'Monthly');
    }

    private getReportDetailed() {
        this.getReport(this.periodForm.get('selectedPeriod').value as Period, this.periodForm.get('selectedReport').value as string, 'Detailed');
    }

    setReport(reportName: string) {
        this.periodForm.get('selectedReport').patchValue(reportName);
    }

    generateResultManagersReport(){
        const period:Period=this.periodForm.get('selectedPeriod').value
        let reportObservable = null;
        reportObservable = this.reportsService.getAllResultManagersIndicatorsValidationReportByPeriodId(period.id);
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

    onPeriodChange(period: Period){
        this.loadProjects(period.id)
        this.loadPerformanceIndicatorsOptions(period.id)
        this.loadTags(period.id)
    }
    onIndicatorTypeChange(indicatorType: string){
        if(indicatorType==="Producto"){
            this.periodForm.get('selectedIndicators').enable();
        }else if(indicatorType==="General"){
            this.periodForm.get('selectedIndicators').disable();
        }else{
            this.periodForm.get('selectedIndicators').enable();
        }
    }

    onFilterChange(){
        this.selectedFilters=this.periodForm.get('selectedFilters').value.map(item=>{
            return item.value
        })
    }

    onFilterClear(){
        this.selectedFilters=[]
        this.periodForm.get('selectedIndicators').patchValue(null);
        this.periodForm.get('selectedMonths').patchValue(null);
        this.periodForm.get('selectedAreas').patchValue(null);
        this.periodForm.get('selectedProjects').patchValue(null);
        this.periodForm.get('selectedTags').patchValue(null);
    }

    onImplementationTypeChange(){
        console.log(this.periodForm.get('implementationType').value)
    }
    
}
