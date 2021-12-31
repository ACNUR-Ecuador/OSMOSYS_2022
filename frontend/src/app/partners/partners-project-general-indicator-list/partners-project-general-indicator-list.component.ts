import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Indicator, IndicatorExecutionResumeWeb, Project} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {MessageService, SelectItem} from 'primeng/api';
import {EnumsService} from '../../shared/services/enums.service';
import {UtilsService} from '../../shared/services/utils.service';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';

@Component({
    selector: 'app-partners-project-general-indicator-list',
    templateUrl: './partners-project-general-indicator-list.component.html',
    styleUrls: ['./partners-project-general-indicator-list.component.scss']
})
export class PartnersProjectGeneralIndicatorListComponent implements OnInit {
    @Input()
    public project: Project;
    @Output()
    callMonthParent = new EventEmitter<Map<string, number | IndicatorExecutionResumeWeb>>();

    public generalIndicators: IndicatorExecutionResumeWeb[];
    // tslint:disable-next-line:variable-name
    _selectedColumnsGeneralIndicators: ColumnTable[];
    colsGeneralIndicators: ColumnTable[];

    private states: SelectItem[];
    private selectedIndicator: IndicatorExecutionResumeWeb;

    constructor(
        private messageService: MessageService,
        private enumsService: EnumsService,
        private utilsService: UtilsService,
        private codeDescriptionPipe: CodeDescriptionPipe,
        private indicatorExecutionService: IndicatorExecutionService,
    ) {
    }

    ngOnInit(): void {
        this.loadGeneralIndicators(this.project.id);
        this.loadOptions();
    }

    private loadGeneralIndicators(idProject: number) {
        this.indicatorExecutionService.getGeneralIndicatorResume(idProject)
            .subscribe(value => {
                this.generalIndicators = value;
                this.createGeneralIndicatorColumns();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar indicadores generales',
                    detail: error.error.message,
                    life: 3000
                });
            });
    }

    private createGeneralIndicatorColumns() {

        this.colsGeneralIndicators = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'indicatorType', header: 'Tipo', type: ColumnDataType.text},
            {field: 'indicator', header: 'Indicador', type: ColumnDataType.text, pipeRef: this.codeDescriptionPipe},
            {field: 'target', header: 'Meta', type: ColumnDataType.numeric},
            {field: 'totalExecution', header: 'Ejecución Actual', type: ColumnDataType.numeric},
            {field: 'executionPercentage', header: 'Porcentaje de ejecución', type: ColumnDataType.numeric},

        ];

        const hiddenColumns: string[] = ['id'];
        this._selectedColumnsGeneralIndicators = this.colsGeneralIndicators.filter(value => !hiddenColumns.includes(value.field));
        /* if (this.generalIndicators && this.generalIndicators.length > 0) {
             const generalIndicator = this.generalIndicators[0];
             this.colsGeneralIndicatorsIndicatorExecution = [];
             const quarters = generalIndicator.quarters.filter(value => {
                 value.state = EnumsState.ACTIVE;
             });
             if (quarters && quarters.length > 0) {
                 quarters.forEach(value => {
                     this.colsGeneralIndicatorsIndicatorExecution.push(
                         {field: 'order', header: 'Orden', type: ColumnDataType.numeric},
                         {field: 'year', header: 'Año', type: ColumnDataType.numeric},
                         {field: 'quarter', header: 'Trimestre', type: ColumnDataType.text},
                         {field: 'target', header: 'Año', type: ColumnDataType.numeric},
                         {field: 'totalExecution', header: 'Año', type: ColumnDataType.numeric},
                         {field: 'executionPercentage', header: 'Año', type: ColumnDataType.numeric},
                     );
                 });
             }
         }*/
    }

    @Input() get selectedColumnsGeneralIndicators(): any[] {
        return this._selectedColumnsGeneralIndicators;
    }

    set selectedColumnsGeneralIndicators(val: any[]) {
        // restore original order
        this._selectedColumnsGeneralIndicators = this.colsGeneralIndicators.filter(col => val.includes(col));
    }

    generalIndicatorsExpand(): number {
        if (this._selectedColumnsGeneralIndicators) {
            return this._selectedColumnsGeneralIndicators.length;
        } else {
            return 1;
        }
    }

    private loadOptions() {
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
    }

    callMonth(monthId: number) {
        const parametersMap = new Map<string, number | IndicatorExecutionResumeWeb>();
        parametersMap.set('monthId', monthId);
        parametersMap.set('indicator', this.selectedIndicator);
        this.callMonthParent.emit(parametersMap);
    }

    selectedIndicatorSet(indicator: IndicatorExecutionResumeWeb) {
        this.selectedIndicator = indicator;
    }

    clearIndicator() {
        this.selectedIndicator = null;
    }
}
