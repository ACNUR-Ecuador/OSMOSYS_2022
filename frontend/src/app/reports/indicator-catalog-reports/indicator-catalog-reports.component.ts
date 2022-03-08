import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Period} from '../../shared/model/OsmosysModel';
import {MessageService} from 'primeng/api';
import {PeriodService} from '../../shared/services/period.service';
import {UtilsService} from '../../shared/services/utils.service';
import {ReportsService} from '../../shared/services/reports.service';
import {HttpResponse} from '@angular/common/http';
import {UserService} from '../../shared/services/user.service';

@Component({
    selector: 'app-indicator-catalog-reports',
    templateUrl: './indicator-catalog-reports.component.html',
    styleUrls: ['./indicator-catalog-reports.component.scss']
})
export class IndicatorCatalogReportsComponent implements OnInit {
    periodForm: FormGroup;
    periods: Period[];
    enableReport = false;

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

    getIndicatorCatalogReport() {
        const period = this.periodForm.get('selectedPeriod').value as Period;
        if (period) {
            this.reportsService.getIndicatorsCatalogByPeriodId(period.id).subscribe((response: HttpResponse<Blob>) => {
                this.utilsService.downloadFileResponse(response);
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

    getIndicatorsCatalogWithImplementersSimple() {
        const period = this.periodForm.get('selectedPeriod').value as Period;
        if (period) {
            this.reportsService.getIndicatorsCatalogWithImplementersSimple(period.id).subscribe((response: HttpResponse<Blob>) => {
                this.utilsService.downloadFileResponse(response);
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

    getIndicatorsCatalogWithImplementersDetailed() {
        const period = this.periodForm.get('selectedPeriod').value as Period;
        if (period) {
            this.reportsService.getIndicatorsCatalogWithImplementersDetailed(period.id).subscribe((response: HttpResponse<Blob>) => {
                this.utilsService.downloadFileResponse(response);
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

}
