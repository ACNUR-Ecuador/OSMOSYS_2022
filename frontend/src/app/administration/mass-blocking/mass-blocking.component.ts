import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {Period, YearMonth} from "../../shared/model/OsmosysModel";
import {PeriodService} from "../../services/period.service";
import {MessageService} from "primeng/api";
import {UtilsService} from "../../services/utils.service";
import {EmailService} from "../../services/email.service";
import {MonthService} from "../../services/month.service";

@Component({
    selector: 'app-mass-blocking',
    templateUrl: './mass-blocking.component.html',
    styleUrls: ['./mass-blocking.component.scss']
})
export class MassBlockingComponent implements OnInit {
    periodForm: FormGroup;
    periods: Period[];
    yearMonths: YearMonth[];

    constructor(private fb: FormBuilder,
                private periodService: PeriodService,
                private messageService: MessageService,
                public utilsService: UtilsService,
                private monthService: MonthService
    ) {
    }

    ngOnInit(): void {
        this.createForms();
        this.loadPeriods();
    }

    private createForms() {
        this.periodForm = this.fb.group({
            selectedPeriod: new FormControl(''),
            selectedReport: new FormControl('')
        });

    }

    loadPeriods() {
        this.periodService.getAll().subscribe({
            next: value => {
                this.periods = value;
                if (this.periods.length < 1) {
                    this.messageService.add({severity: 'error', summary: 'No se encontraron periodos', detail: ''});
                } else {
                    const currentPeriod = this.utilsService.getCurrectPeriodOrDefault(this.periods);
                    this.periodForm.get('selectedPeriod').patchValue(currentPeriod);
                    this.loadYearMonths(currentPeriod.id);
                }
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar las los periodos',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    loadYearMonths(periodId: number) {
        this.monthService.getYearMonthByPeriodId(periodId).subscribe({
            next: value => {
                this.yearMonths = value;
                console.log(this.yearMonths);
            }, error: err => {
            }
        });
    }

    blockYearMonth(yearMonth: YearMonth) {
        console.log(yearMonth);
        this.monthService.massiveBlock(yearMonth).subscribe({
            next: value => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'bloquedo exitosamente ' + yearMonth.year + '-' + yearMonth.month
                });
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'error en el bloqueo de ' + yearMonth.year + '-' + yearMonth.month
                });
            }
        });
    }

    unblockYearMonth(yearMonth: YearMonth) {
        this.monthService.massiveUnblock(yearMonth).subscribe({
            next: value => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'bloquedo exitosamente ' + yearMonth.year + '-' + yearMonth.month
                });
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'error en el bloqueo de ' + yearMonth.year + '-' + yearMonth.month
                });
            }
        });
    }

    onPeriodChange(period: Period) {
        this.loadYearMonths(period.id);
    }
}
