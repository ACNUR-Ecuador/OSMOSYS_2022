import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IndicatorExecution, Quarter, QuarterMonthResume} from "../../shared/model/OsmosysModel";
import {UtilsService} from "../../services/utils.service";
import {UserService} from "../../services/user.service";
import {MonthService} from "../../services/month.service";
import {MessageService} from "primeng/api";
import {EnumsState} from "../../shared/model/UtilsModel";

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
    @Output()
    refreshData = new EventEmitter<number>();

    quarterMonthResumes: QuarterMonthResume[];
    monthsCount = 0;

    isSupervisor = false;
    isEjecutor = false;
    isAdmin = false;

    constructor(public utilsService: UtilsService,
                private userService: UserService,
                private monthService: MonthService,
                public messageService: MessageService) {
    }

    ngOnInit(): void {
        this.setRoles();
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
        this.quarterMonthResumes = this.utilsService.generateQuarterMonthsResumes(this.quarters);
    }

    private setRoles() {
        const userId = this.userService.getLogedUsername().id;
        this.isAdmin = this.userService.hasAnyRole(['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']);
        this.isSupervisor = this.indicatorExecution.supervisorUser.id === userId;
        if (this.indicatorExecution.assignedUserBackup) {
            this.isEjecutor = this.indicatorExecution.assignedUser.id === userId
                || this.indicatorExecution.assignedUserBackup.id === userId;
        } else {
            this.isEjecutor = this.indicatorExecution.assignedUser.id === userId;
        }

    }

    viewValues(monthId: any) {
        this.callMonth.emit(monthId);
    }

    changeMonthBlocking(quarterMonthResume: QuarterMonthResume, $event: any) {
        this.monthService.changeBlockedState(quarterMonthResume.monthId, $event.checked)
            .subscribe({
                next:() => {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Mes actualizado correctamente',
                        life: 3000
                    });
                    this.refreshData.emit(quarterMonthResume.monthId)
                },
                error:error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al actualizar el mes',
                        detail: error.error.message,
                        life: 3000
                    });
                },
                complete:() => {
                    this.refreshData.emit(quarterMonthResume.monthId);
                }
            });

    }
}
