import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IndicatorExecution, Quarter, QuarterMonthResume} from '../../shared/model/OsmosysModel';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsState} from '../../shared/model/UtilsModel';

@Component({
    selector: 'app-indicator-quarter-list',
    templateUrl: './indicator-quarter-list.component.html',
    styleUrls: ['./indicator-quarter-list.component.scss']
})
export class IndicatorQuarterListComponent implements OnInit {

    @Input()
    quarters: Quarter[];

    @Input()
    indicatorExecution: IndicatorExecution;

    @Output()
    callMonth = new EventEmitter<number>();
    quarterMonthResumes: QuarterMonthResume[];
    monthsCount = 0;

    constructor(public utilsService: UtilsService) {
    }

    ngOnInit(): void {
        console.log('qaurters');
        console.log(this.quarters);
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
                this.quarterMonthResumes.push(qmr);
            });
        });
    }

    viewValues(monthId: any) {
        this.callMonth.emit(monthId);
    }
}
