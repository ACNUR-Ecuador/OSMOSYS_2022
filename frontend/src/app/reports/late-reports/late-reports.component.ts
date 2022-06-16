import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Period} from '../../shared/model/OsmosysModel';
import {PeriodService} from '../../shared/services/period.service';
import {MessageService} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {ReportsService} from '../../shared/services/reports.service';
import {UserService} from '../../shared/services/user.service';
import {HttpResponse} from '@angular/common/http';

@Component({
    selector: 'app-late-reports',
    templateUrl: './late-reports.component.html',
    styleUrls: ['./late-reports.component.scss']
})
export class LateReportsComponent implements OnInit {
    periodForm: FormGroup;
    periods: Period[];
    enableReport = false;
    userId: number;
    isAdmin: boolean;


    constructor(private fb: FormBuilder,
                private periodService: PeriodService,
                private messageService: MessageService,
                public utilsService: UtilsService,
                private reportsService: ReportsService,
                private userService: UserService) {
    }

    ngOnInit(): void {
        this.loadPeriods();
        this.createForms();
        this.enableReport = this.userService.getLogedUsername().organization.id === 1;
        this.userId = this.userService.getLogedUsername().id;
        this.isAdmin = this.userService.hasAnyRole(['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']);
    }

    loadPeriods() {
        this.periodService.getAll().subscribe(value => {
            this.periods = value;
            if (this.periods.length < 1) {
                this.messageService.add({severity: 'error', summary: 'No se encontraron periodos', detail: ''});
            } else {
                const currentPeriod = this.utilsService.getCurrectPeriodOrDefault(this.periods);
                this.periodForm.get('selectedPeriod').patchValue(currentPeriod);
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

    getFocalPointLateReviewReport() {
        const period = this.periodForm.get('selectedPeriod').value as Period;
        this.messageService.clear();
        if (period) {
            this.reportsService.getFocalPointLateReviewReport(this.userId).subscribe((response: HttpResponse<Blob>) => {
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
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona un periodo',
                life: 3000
            });
        }
    }

    getFocalPointLateReport() {
        const period = this.periodForm.get('selectedPeriod').value as Period;
        this.messageService.clear();
        if (period) {
            this.reportsService.getFocalPointLateReport(this.userId).subscribe((response: HttpResponse<Blob>) => {
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
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona un periodo',
                life: 3000
            });
        }
    }

    getResponsableLateReport() {
        const period = this.periodForm.get('selectedPeriod').value as Period;
        this.messageService.clear();
        if (period) {
            this.reportsService.getResponsableLateReport(this.userId).subscribe((response: HttpResponse<Blob>) => {
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
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona un periodo',
                life: 3000
            });
        }
    }


    getSupervisorLateReviewReport() {
        const period = this.periodForm.get('selectedPeriod').value as Period;
        this.messageService.clear();
        if (period) {
            this.reportsService.getSupervisorLateReviewReport(this.userId).subscribe((response: HttpResponse<Blob>) => {
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
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona un periodo',
                life: 3000
            });
        }
    }

    getAllLateReviewReportDirectImplementation() {
        this.messageService.clear();

        this.reportsService.getAllLateReviewReportDirectImplementation().subscribe((response: HttpResponse<Blob>) => {
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

    getAllLateReportDirectImplementation() {
        this.messageService.clear();

        this.reportsService.getAllLateReportDirectImplementation().subscribe((response: HttpResponse<Blob>) => {
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


    getAllLateReportPartners() {
        this.messageService.clear();

        this.reportsService.getAllLateReportPartners().subscribe((response: HttpResponse<Blob>) => {
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

    getAllLateReviewPartners() {
        this.messageService.clear();

        this.reportsService.getAllLateReviewPartners().subscribe((response: HttpResponse<Blob>) => {
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
