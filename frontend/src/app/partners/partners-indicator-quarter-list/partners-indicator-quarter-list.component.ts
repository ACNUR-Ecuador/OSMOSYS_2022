import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IndicatorExecution, Quarter, QuarterMonthResume} from '../../shared/model/OsmosysModel';
import {EnumsState} from '../../shared/model/UtilsModel';
import {MessageService} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {MonthService} from '../../services/month.service';

@Component({
    selector: 'app-partners-indicator-quarter-list',
    templateUrl: './partners-indicator-quarter-list.component.html',
    styleUrls: ['./partners-indicator-quarter-list.component.scss']
})
export class PartnersIndicatorQuarterListComponent implements OnInit {
    @Input()
    quarters: Quarter[];
    // roles
    @Input()
    isAdmin = false;
    @Input()
    isProjectFocalPoint = false;

    @Input()
    indicatorExecution: IndicatorExecution;

    @Output()
    callMonth = new EventEmitter<Map<string, number | string>>();

    @Output()
    refreshData = new EventEmitter<number>();

    quarterMonthResumes: QuarterMonthResume[];

    constructor(
        public utilsService: UtilsService,
        private monthService: MonthService,
        public messageService: MessageService
    ) {
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
        });

        this.quarterMonthResumes = this.utilsService.generateQuarterMonthsResumes(this.quarters);
    }


    callMonthInParenth(monthId: number, month: string, year: number) {
        const parametersMap = new Map<string, number | string>();
        parametersMap.set('monthId', monthId);
        parametersMap.set('month', month);
        parametersMap.set('year', year);
        this.callMonth.emit(parametersMap);
    }

    changeMonthBlocking(quarterMonthResume: QuarterMonthResume, $event: any) {
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
        });
    }
}
