import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {MenuItem, MessageService} from 'primeng/api';
import {Period} from "../../shared/model/OsmosysModel";
import {PeriodService} from "../../services/period.service";
import {UtilsService} from "../../services/utils.service";
import {ReportsService} from "../../services/reports.service";
import {HttpResponse} from "@angular/common/http";

@Component({
    selector: 'app-data-export',
    templateUrl: './data-export.component.html',
    styleUrls: ['./data-export.component.scss']
})
export class DataExportComponent implements OnInit {
    periodForm: FormGroup;
    periods: Period[];

    itemsReportTypeFull: MenuItem[];
    itemsReportTypeAnualMonthlyDetailed: MenuItem[];

    constructor(private fb: FormBuilder,
                private periodService: PeriodService,
                private messageService: MessageService,
                public utilsService: UtilsService,
                private reportsService: ReportsService) {
    }

    ngOnInit(): void {
        this.loadPeriods();
        this.createForms();
        this.generateItemsReportType();
    }

    loadPeriods() {
        this.periodService.getAll().subscribe(value => {
            this.periods = value;
            if (this.periods.length < 1) {
                this.messageService.add({severity: 'error', summary: 'No se encontraron periodos', detail: ''});
            } else {
                const currentPeriod = this.utilsService.getCurrectPeriodOrDefault(this.periods);
                console.log(currentPeriod);
                const currentPeriodOption = this.periods.filter(value1 => {
                    return value1.id === currentPeriod.id
                })[0];
                console.log(currentPeriodOption);
                this.periodForm.get('selectedPeriod').patchValue(currentPeriodOption);
                console.log(this.periodForm.value);
            }
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las los periodos',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    private createForms() {
        this.periodForm = this.fb.group({
            selectedPeriod: new FormControl(''),
            selectedReport: new FormControl('')
        });
    }


    public getReport(period: Period, reportName: string, type: string) {
        this.messageService.clear();
        const report: string = reportName.replace('XXX', type);
        let reportObservable = null;
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
}
