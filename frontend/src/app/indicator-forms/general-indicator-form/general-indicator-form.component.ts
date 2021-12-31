import {Component, OnInit} from '@angular/core';
import {IndicatorExecutionResumeWeb, IndicatorValue, Month, MonthValues} from '../../shared/model/OsmosysModel';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {MonthService} from '../../shared/services/month.service';
import {MessageService} from 'primeng/api';
import {DissagregationType} from '../../shared/model/UtilsModel';

@Component({
    selector: 'app-general-indicator-form',
    templateUrl: './general-indicator-form.component.html',
    styleUrls: ['./general-indicator-form.component.scss']
})
export class GeneralIndicatorFormComponent implements OnInit {
    indicatorExecution: IndicatorExecutionResumeWeb;
    monthId: number;
    monthValues: MonthValues;
    month: Month;
    monthValuesMap: Map<string, IndicatorValue[]>;
    dissagregationType: DissagregationType = DissagregationType.TIPO_POBLACION_Y_DIVERSIDAD;
    dissagregationTypeValues: IndicatorValue[];

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
            this.monthValues = value as MonthValues;
            console.log(value.month);
            console.log(value.indicatorValuesMap);
            this.month = value.month;
            this.monthValuesMap = value.indicatorValuesMap;
            console.log(this.monthValuesMap);
            console.log(this.dissagregationType);
            this.dissagregationTypeValues = this.monthValuesMap.get(this.dissagregationType);


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
