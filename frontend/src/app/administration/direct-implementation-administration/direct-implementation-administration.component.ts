import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Period} from '../../shared/model/OsmosysModel';
import {PeriodService} from '../../shared/services/period.service';
import {MessageService} from 'primeng/api';

@Component({
    selector: 'app-direct-implementation-administration',
    templateUrl: './direct-implementation-administration.component.html',
    styleUrls: ['./direct-implementation-administration.component.scss']
})
export class DirectImplementationAdministrationComponent implements OnInit {
    periodForm: FormGroup;
    periods: Period[];

    constructor(
        private fb: FormBuilder,
        private periodService: PeriodService,
        private messageService: MessageService,
    ) {
    }

    ngOnInit(): void {
        this.loadPeriods();
    }

    loadPeriods() {
        this.periodService.getAll().subscribe(value => {
            this.periods = value;
            if (this.periods.length < 1) {
                this.messageService.add({severity: 'error', summary: 'No se encontraron periodos', detail: ''});
            } else {
                const currentYear = (new Date()).getFullYear();
                if (this.periods.some(e => e.year === currentYear)) {
                    this.periods.filter(p => p.year === currentYear).forEach(value1 => {
                        this.periodForm.get('selectedPeriod').patchValue(value1);
                        if (value1) {
                            // this.loadProjects(value1.id);
                        }
                    });
                } else {
                    const smallestYear = Math.min(...this.periods.map(value1 => value1.year));
                    const smallestPeriod = this.periods.filter(value1 => {
                        return value1.year === smallestYear;
                    })[0];
                    this.periodForm.get('selectedPeriod').patchValue(smallestPeriod);
                    // this.loadProjects(smallestPeriod.id);
                }
            }
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las áreas',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    createItem() {

    }

    onPeriodChange(period: Period) {

    }

    exportExcel() {

    }
}
