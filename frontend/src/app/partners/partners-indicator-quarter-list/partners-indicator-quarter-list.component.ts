import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Month, Quarter, QuarterMonthResume} from '../../shared/model/OsmosysModel';
import {EnumsState} from '../../shared/model/UtilsModel';
import {UtilsService} from '../../shared/services/utils.service';

@Component({
    selector: 'app-partners-indicator-quarter-list',
    templateUrl: './partners-indicator-quarter-list.component.html',
    styleUrls: ['./partners-indicator-quarter-list.component.scss']
})
export class PartnersIndicatorQuarterListComponent implements OnInit {
    @Input()
    quarters: Quarter[];

    @Output()
    callMonth = new EventEmitter<number>();
    quarterMonthResumes: QuarterMonthResume[];

    constructor(public utilsService: UtilsService) {
    }

    ngOnInit(): void {
        console.log('quarters--S');
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
        console.log('this.quarterMonthResumes');
        console.log(this.quarterMonthResumes);
    }


    callMonthInParenth(monthId: number) {
        this.callMonth.emit(monthId);
    }
}
