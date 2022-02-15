import {Component, OnInit} from '@angular/core';
import {Period} from '../../shared/model/OsmosysModel';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {PeriodService} from '../../shared/services/period.service';
import {MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {ReportsService} from '../../shared/services/reports.service';
import {HttpResponse} from '@angular/common/http';

@Component({
    selector: 'app-all-projects-state',
    templateUrl: './all-projects-state.component.html',
    styleUrls: ['./all-projects-state.component.scss']
})
export class AllProjectsStateComponent implements OnInit {
    periodOptions: SelectItem[];
    periodForm: FormGroup;

    constructor(private fb: FormBuilder,
                private periodService: PeriodService,
                private messageService: MessageService,
                public utilsService: UtilsService,
                private reportsService: ReportsService) {
    }

    ngOnInit(): void {
        this.createForms();
        this.loadPeriods();
    }

    createForms() {
        this.periodForm = this.fb.group({
            period: new FormControl('')
        });
    }

    loadPeriods() {
        this.periodService.getAll().subscribe(value => {
            this.periodOptions = value
                .sort((a, b) => a.year - b.year)
                .map(period => {
                    return {
                        label: period.year.toString(),
                        value: period
                    };
                });
            const selectedPeriod: Period = this.utilsService.getCurrectPeriodOrDefault(value);
            if (!selectedPeriod) {
                return;
            }
            this.periodForm.get('period').patchValue(selectedPeriod);
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las áreas',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    downloadReport() {
        this.reportsService.getAllPartnertsStateReport(this.periodForm.get('period').value.id).subscribe(
            (response: HttpResponse<Blob>) => {
                this.utilsService.downloadFileResponse(response);
            }
        );
    }
}
