import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IndicatorExecution, Quarter, QuarterMonthResume} from '../../shared/model/OsmosysModel';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsState} from '../../shared/model/UtilsModel';
import {MonthService} from '../../shared/services/month.service';
import {MessageService} from 'primeng/api';

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
    callMonth = new EventEmitter<number>();
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
        this.quarterMonthResumes = [];
        let yearSpan: number = null;
        let quarterSpan: string = null;
        this.quarters.forEach(quarter => {
            quarter.months.forEach(month => {
                const qmr = new QuarterMonthResume();
                qmr.quarterId = quarter.id;
                qmr.quarterQuarter = quarter.quarter;
                qmr.quarterOrder = quarter.order;
                qmr.quarterYear = quarter.year;
                qmr.quarterTarget = quarter.target;
                qmr.quarterTotalExecution = quarter.totalExecution;
                qmr.quarterExecutionPercentage = quarter.executionPercentage;
                qmr.quarterMonthCount = quarter.months.length;
                qmr.monthId = month.id;
                qmr.monthMonth = month.month;
                qmr.monthOrder = month.order;
                qmr.monthYear = month.year;
                qmr.monthTotalExecution = month.totalExecution;
                qmr.blockUpdate = month.blockUpdate;
                // year rowspan
                if (!yearSpan) {
                    yearSpan = quarter.year;
                    qmr.yearSpan = true;
                } else if (month.year !== yearSpan) {
                    qmr.yearSpan = true;
                    yearSpan = quarter.year;
                } else if (month.year === yearSpan) {
                    qmr.yearSpan = false;
                }
                qmr.yearSpanCount = this.quarters
                    .filter(value => value.year === yearSpan)
                    .reduce(
                        (a, b) => a.concat(b.months), []
                    ).length;
                // quarter rowspan
                if (!quarterSpan) {
                    quarterSpan = quarter.quarter;
                    qmr.quarterSpan = true;
                } else if (quarter.quarter !== quarterSpan) {
                    qmr.quarterSpan = true;
                    quarterSpan = quarter.quarter;
                } else if (quarter.quarter === quarterSpan) {
                    qmr.quarterSpan = false;
                }
                qmr.quarterSpanCount = quarter.months.length;

                this.quarterMonthResumes.push(qmr);
            });
        });
    }


    callMonthInParenth(monthId: number) {
        this.callMonth.emit(monthId);
    }

    changeMonthBlocking(quarterMonthResume: QuarterMonthResume, $event: any) {
        console.log(quarterMonthResume);
        console.log($event);
        this.monthService.changeBlockedState(quarterMonthResume.monthId, $event.checked).subscribe(() => {
            this.messageService.add({
                severity: 'success',
                summary: 'Mes actualizado correctamente',
                life: 3000
            });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al actualizar el mes',
                detail: error.error.message,
                life: 3000
            });
        }, () => {
            this.refreshData.emit(quarterMonthResume.monthId);
        });
    }
}
