import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {Office, Period} from "../../shared/model/OsmosysModel";
import {PeriodService} from "../../services/period.service";
import {MessageService, SelectItem} from "primeng/api";
import {UtilsService} from "../../services/utils.service";
import {ReportsService} from "../../services/reports.service";
import {UserService} from "../../services/user.service";
import {HttpResponse} from "@angular/common/http";
import {User} from "../../shared/model/User";
import {OfficeService} from "../../services/office.service";

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
    currentUser: User;
    isAdmin: boolean;

    userResponsibleItems: SelectItem[];
    userSupervisorItems: SelectItem[];
    officesItems: SelectItem[];
    selectedUserResponsible: User;
    selectedUserSupervisor: User;
    selectedOffice: Office;

    constructor(private fb: FormBuilder,
                private periodService: PeriodService,
                private messageService: MessageService,
                public utilsService: UtilsService,
                private reportsService: ReportsService,
                private userService: UserService,
                private officeService: OfficeService) {
    }

    ngOnInit(): void {
        this.currentUser = this.userService.getLogedUsername();
        this.userId = this.currentUser.id;
        this.isAdmin = this.userService.hasAnyRole(['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL','ADMINISTRADOR_LOCAL']);
        this.createForms();
        this.loadPeriods();
        this.enableReport = this.userService.getLogedUsername().organization.id === 1;


    }

    loadOptions(periodId: number) {
        this.userService.getActiveResponsableDirectImplementationUsers(periodId)
            .subscribe({
                next: value => {
                    this.userResponsibleItems = value.map(value1 => {
                        return {label: value1.name + "-" + value1.office.acronym, value: value1}
                    }).sort((a, b) => {
                        return a.label.localeCompare(b.label)
                    });

                    if (this.userResponsibleItems.filter(value1 => value1.value.id === this.currentUser.id).length > 0) {
                        this.selectedUserResponsible = this.userResponsibleItems.filter(value1 => value1.value.id === this.currentUser.id).pop().value;

                    } else {
                        this.selectedUserResponsible = null;
                    }
                }, error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las usuarios',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
        this.userService.getActiveSupervisorDirectImplementationUsers(periodId)
            .subscribe({
                next: value => {
                    this.userSupervisorItems = value.map(value1 => {
                        return {label: value1.name + "-" + value1.office.acronym, value: value1}
                    }).sort((a, b) => {
                        return a.label.localeCompare(b.label)
                    });

                    if (this.userSupervisorItems.filter(value1 => value1.value.id === this.currentUser.id).length > 0) {
                        this.selectedUserSupervisor = this.userSupervisorItems.filter(value1 => value1.value.id === this.currentUser.id).pop().value;

                    } else {
                        this.selectedUserSupervisor = null;
                    }
                }, error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las usuarios',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
        this.officeService.getReportingOfficesByPeriodId(periodId)
            .subscribe({
                next: value => {
                    this.officesItems = value.map(value1 => {
                        return {label: value1.acronym, value: value1}
                    }).sort((a, b) => {
                        return a.label.localeCompare(b.label)
                    });

                    if (this.officesItems.filter(value1 => value1.value.id === this.currentUser.office.id).length > 0) {
                        this.selectedOffice = this.officesItems.filter(value1 => value1.value.id === this.currentUser.office.id).pop().value;

                    } else {
                        this.selectedOffice = null;
                    }
                }, error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las oficinas',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
    }

    loadPeriods() {
        this.periodService.getAll().subscribe({
            next: value => {
                this.periods = value;
                if (this.periods.length < 1) {
                    this.messageService.add({severity: 'error', summary: 'No se encontraron años', detail: ''});
                } else {
                    const currentPeriod = this.utilsService.getCurrectPeriodOrDefault(this.periods);
                    this.periodForm.get('selectedPeriod').patchValue(currentPeriod);
                    this.loadOptions(currentPeriod.id);
                }
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar las los años',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    private createForms() {
        this.periodForm = this.fb.group({
            selectedPeriod: new FormControl(''),
            selectedReport: new FormControl('')
        });

    }


    getFocalPointLateReport() {
        this.messageService.clear();
        const period = this.periodForm.get('selectedPeriod').value as Period;
        this.messageService.clear();
        if (period) {
            this.reportsService.getFocalPointLateReport(this.userId)
                .subscribe({
                    next: response => {
                        if (response.status === 204) {
                            this.messageService.add({
                                severity: 'success',
                                summary: 'No tienes indicadores pendientes, gracias por tu colaboración',
                                sticky: true
                            });
                        } else {
                            this.utilsService.downloadFileResponse(response);
                        }
                    }, error: error => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al Generar el Reporte',
                            detail: error.error.message,
                            sticky: true
                        });
                    }
                });


        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona un año',
                life: 3000
            });
        }
    }

    getAllLateReportPartners() {
        this.messageService.clear();
        const period = this.periodForm.get('selectedPeriod').value as Period;
        this.reportsService.getAllLateReportPartners(period.id)
            .subscribe({
                next: response => {
                    if (response.status === 204) {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'No tienes indicadores pendientes, gracias por tu colaboración',
                            sticky: true
                        });
                    } else {
                        this.utilsService.downloadFileResponse(response);
                    }
                }, error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al Generar el Reporte',
                        detail: error.error.message,
                        sticky: true
                    });
                }
            });


    }


    getResponsableLateReport() {
        const period = this.periodForm.get('selectedPeriod').value as Period;
        this.messageService.clear();
        if (period) {
            this.reportsService.getResponsableLateReport(this.selectedUserResponsible.id, period.id).subscribe((response: HttpResponse<Blob>) => {
                if (response.status === 204) {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'No tienes indicadores pendientes, gracias por tu colaboración',
                        sticky: true
                    });
                } else {
                    this.utilsService.downloadFileResponse(response);
                }

            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al Generar el Reporte',
                    detail: error.error.message,
                    sticky: true
                });
            });
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona un año',
                life: 3000
            });
        }
    }

    getSupervisorLateDirectImplementationReport() {
        const period = this.periodForm.get('selectedPeriod').value as Period;
        this.messageService.clear();
        if (period) {
            this.reportsService.getSupervisorLateDirectImplementationReport(period.id, this.selectedUserSupervisor.id).subscribe((response: HttpResponse<Blob>) => {
                if (response.status === 204) {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'No tienes indicadores pendientes, gracias por tu colaboración',
                        sticky: true
                    });
                } else {
                    this.utilsService.downloadFileResponse(response);
                }

            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al Generar el Reporte',
                    detail: error.error.message,
                    sticky: true
                });
            });
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona un año',
                life: 3000
            });
        }
    }

    getOfficeLateDirectImplementationReport() {
        const period = this.periodForm.get('selectedPeriod').value as Period;
        this.messageService.clear();
        if (period) {
            this.reportsService.getOfficeLateDirectImplementationReport(period.id, this.selectedOffice.id).subscribe((response: HttpResponse<Blob>) => {
                if (response.status === 204) {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'No tienes indicadores pendientes, gracias por tu colaboración',
                        sticky: true
                    });
                } else {
                    this.utilsService.downloadFileResponse(response);
                }

            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al Generar el Reporte',
                    detail: error.error.message,
                    sticky: true
                });
            });
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona un año',
                life: 3000
            });
        }
    }


    getAllLateReportDirectImplementation() {
        const period = this.periodForm.get('selectedPeriod').value as Period;
        this.messageService.clear();

        this.reportsService.getAllLateReportDirectImplementation(period.id).subscribe((response: HttpResponse<Blob>) => {
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

    onChangePeriod() {
        const currentPeriod: Period = this.periodForm.get('selectedPeriod').value;
        this.loadOptions(currentPeriod.id);
    }
}
