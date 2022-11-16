import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IndicatorExecution, Quarter, QuarterMonthResume} from '../../shared/model/OsmosysModel';
import {EnumsState} from '../../shared/model/UtilsModel';
import {MessageService} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {MonthService} from '../../services/month.service';

@Component({
    selector: 'app-partners-general-indicator-quarter-list',
    templateUrl: './partners-general-indicator-quarter-list.component.html',
    styleUrls: ['./partners-general-indicator-quarter-list.component.scss']
})
export class PartnersGeneralIndicatorQuarterListComponent implements OnInit {
    @Input()
    quarters: Quarter[];

    @Input()
    indicatorExecution: IndicatorExecution;
    // roles
    @Input()
    isAdmin = false;
    @Input()
    isProjectFocalPoint = false;
    @Output()
    callMonth = new EventEmitter<Map<string, number | string>>();
    @Output()
    refreshData = new EventEmitter<number>();

    quarterMonthResumes: QuarterMonthResume[];
    monthsCount = 0;

    constructor(public utilsService: UtilsService,
                private monthService: MonthService,
                public messageService: MessageService) {
    }

    ngOnInit(): void {
        this.quarters = this.quarters.filter(value => {
            return value.state === EnumsState.ACTIVE;
        })
            .sort((a, b) => a.order - b.order);
        this.quarters.forEach(value => {
            value.months = value.months
                .filter(value1 => {
                    return value1.state === EnumsState.ACTIVE;
                })
                .sort((a, b) => a.order - b.order);
            this.monthsCount += value.months.length;
        });
        this.quarterMonthResumes = this.utilsService.generateQuarterMonthsResumes(this.quarters);
    }

    callMonthInParenth(monthId: number, month: string, year: number) {
        const parameters = new Map<string, number | string>();
        parameters.set('monthId', monthId);
        parameters.set('month', month);
        parameters.set('year', year);
        this.callMonth.emit(parameters);
    }

    changeMonthBlocking(quarterMonthResume: QuarterMonthResume, $event: any) {
        this.monthService.changeBlockedState(quarterMonthResume.monthId, $event.checked)
            .subscribe({
                    next: () => {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Mes actualizado correctamente',
                            life: 3000
                        });
                    },
                    error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al actualizar el mes',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
                }
            );
    }
}
