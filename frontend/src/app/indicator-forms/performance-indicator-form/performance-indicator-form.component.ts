import {Component, OnInit} from '@angular/core';
import {IndicatorExecutionResumeWeb, IndicatorValue, Month, MonthValues} from '../../shared/model/OsmosysModel';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {MonthService} from '../../shared/services/month.service';
import {EnumsService} from '../../shared/services/enums.service';
import {UtilsService} from '../../shared/services/utils.service';
import {MessageService} from 'primeng/api';
import {DissagregationType} from '../../shared/model/UtilsModel';

@Component({
    selector: 'app-performance-indicator-form',
    templateUrl: './performance-indicator-form.component.html',
    styleUrls: ['./performance-indicator-form.component.scss']
})
export class PerformanceIndicatorFormComponent implements OnInit {
    indicatorExecution: IndicatorExecutionResumeWeb;
    monthId: number;
    monthValues: MonthValues;
    month: Month;
    monthValuesMap: Map<string, IndicatorValue[]>;
    oneDimentionDissagregations: DissagregationType[] = [];
    twoDimentionDissagregations: DissagregationType[] = [];
    noDimentionDissagregations: DissagregationType[] = [];

    render = false;
    showErrorResume = false;
    totalsValidation: Map<string, number> = null;

    constructor(public ref: DynamicDialogRef,
                public config: DynamicDialogConfig,
                public indicatorExecutionService: IndicatorExecutionService,
                public monthService: MonthService,
                public enumsService: EnumsService,
                public utilsService: UtilsService,
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
            this.month = value.month;
            this.monthValuesMap = value.indicatorValuesMap;
            this.setDimentionsDissagregations();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los valores del mes',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    saveMonth() {
        // console.log(this.monthValues);
        this.utilsService.setZerosMonthValues(this.monthValuesMap);
        const totalsValidation = this.utilsService.validateMonth(this.monthValuesMap);
        if (totalsValidation) {
            this.showErrorResume = true;
            this.totalsValidation = totalsValidation;
        } else {
            this.totalsValidation = null;
            this.sendMonthValue();
        }
    }

    cancel() {
        console.log(this.monthValues);
    }

    private sendMonthValue() {
        this.indicatorExecutionService.updateMonthValues(this.indicatorExecution.id, this.monthValues).subscribe(value => {
            this.messageService.add({severity: 'success', summary: 'Guardado con éxito', detail: ''});
            this.ref.close({test: 1});
        }, error => {
            this.messageService.add({severity: 'error', summary: 'Error al guardar los valores:', detail: error.error.message});
        });
    }

    closeErrorDialog() {
        this.showErrorResume = false;
    }

    setDimentionsDissagregations(): void {
        const totalOneDimentions = this.utilsService.getOneDimentionsDissagregationTypes();
        const totalTwoDimentions = this.utilsService.getTwoDimentionsDissagregationTypes();
        const totalNoDimentions = this.utilsService.getNoDimentionsDissagregationTypes();
        this.monthValuesMap.forEach((value, key) => {
            if (value && value.length > 0) {
                if (totalOneDimentions.indexOf(DissagregationType[key])) {
                    this.oneDimentionDissagregations.push(DissagregationType[key]);
                }else if (totalTwoDimentions.indexOf(DissagregationType[key])) {
                    this.twoDimentionDissagregations.push(DissagregationType[key]);
                }else if (totalNoDimentions.indexOf(DissagregationType[key])) {
                    this.noDimentionDissagregations.push(DissagregationType[key]);
                }
            }
        });
        this.render = true;
    }
}
