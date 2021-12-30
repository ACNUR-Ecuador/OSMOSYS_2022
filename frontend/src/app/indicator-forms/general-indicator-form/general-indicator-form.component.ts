import {Component, OnInit} from '@angular/core';
import {IndicatorExecutionResumeWeb, MonthValues} from '../../shared/model/OsmosysModel';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {MonthService} from '../../shared/services/month.service';
import {MessageService} from 'primeng/api';

@Component({
    selector: 'app-general-indicator-form',
    templateUrl: './general-indicator-form.component.html',
    styleUrls: ['./general-indicator-form.component.scss']
})
export class GeneralIndicatorFormComponent implements OnInit {
    indicatorExecution: IndicatorExecutionResumeWeb;
    monthId: number;
    monthValues: MonthValues;

    constructor(public ref: DynamicDialogRef,
                public config: DynamicDialogConfig,
                public monthService: MonthService,
                private messageService: MessageService
    ) {
    }

    ngOnInit(): void {
        this.indicatorExecution = this.config.data.indicatorExecution; //
        this.monthId = this.config.data.monthId; //
        this.loadMonthValues(this.monthId);
    }

    loadMonthValues(monthId: number) {
        this.monthService.getMonthIndicatorValueByMonthId(monthId).subscribe(value => {
            this.monthValues = value;
            console.log(value);
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los valores del mes',
                detail: error.error.message,
                life: 3000
            });
        });
    }

}
