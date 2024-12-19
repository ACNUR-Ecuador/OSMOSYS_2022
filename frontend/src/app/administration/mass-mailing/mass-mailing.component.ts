import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {Period} from "../../shared/model/OsmosysModel";
import {PeriodService} from "../../services/period.service";
import {MessageService} from "primeng/api";
import {UtilsService} from "../../services/utils.service";
import {EmailService} from "../../services/email.service";

@Component({
    selector: 'app-mass-mailing',
    templateUrl: './mass-mailing.component.html',
    styleUrls: ['./mass-mailing.component.scss']
})
export class MassMailingComponent implements OnInit {

    periodForm: FormGroup;
    periods: Period[];

    constructor(private fb: FormBuilder,
                private periodService: PeriodService,
                private messageService: MessageService,
                public utilsService: UtilsService,
                private emailService: EmailService
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
                    this.messageService.add({severity: 'error', summary: 'No se encontraron años', detail: ''});
                } else {
                    const currentPeriod = this.utilsService.getCurrectPeriodOrDefault(this.periods);
                    this.periodForm.get('selectedPeriod').patchValue(currentPeriod);
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

    sendRecordatoryToPartners() {

        this.emailService.recordatoryPartners().subscribe({
            next: value => {
                this.messageService.add({
                    severity: 'sucess',
                    summary: 'Correos enviados',
                    life: 3000
                });
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al enviar',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    sendRecordatoryToId() {
        this.emailService.recordatoryId().subscribe({
            next: value => {
                this.messageService.add({
                    severity: 'sucess',
                    summary: 'Correos enviados',
                    life: 3000
                });
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al enviar',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    sendAlertToPartners() {
        this.emailService.alertPartners().subscribe({
            next: value => {
                this.messageService.add({
                    severity: 'sucess',
                    summary: 'Correos enviados',
                    life: 3000
                });
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al enviar',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    sendAlertToId() {
        this.emailService.alertsId().subscribe({
            next: value => {
                this.messageService.add({
                    severity: 'sucess',
                    summary: 'Correos enviados',
                    life: 3000
                });
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al enviar',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }
}
