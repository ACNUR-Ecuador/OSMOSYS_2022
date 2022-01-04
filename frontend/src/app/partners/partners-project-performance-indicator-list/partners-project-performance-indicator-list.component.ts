import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IndicatorExecutionResumeWeb, Project} from '../../shared/model/OsmosysModel';
import {MessageService, SelectItem} from 'primeng/api';
import {EnumsService} from '../../shared/services/enums.service';
import {UtilsService} from '../../shared/services/utils.service';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {PercentPipe} from '@angular/common';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';

@Component({
    selector: 'app-partners-project-performance-indicator-list',
    templateUrl: './partners-project-performance-indicator-list.component.html',
    styleUrls: ['./partners-project-performance-indicator-list.component.scss']
})
export class PartnersProjectPerformanceIndicatorListComponent implements OnInit {
    @Input()
    public project: Project;
    @Output()
    callMonthParent = new EventEmitter<Map<string, number | IndicatorExecutionResumeWeb>>();

    public performanceIndicators: IndicatorExecutionResumeWeb[];
    // tslint:disable-next-line:variable-name
    _selectedColumnsPerformanceIndicators: ColumnTable[];
    colsGeneralIndicators: ColumnTable[];

    private states: SelectItem[];
    private selectedIndicator: IndicatorExecutionResumeWeb;

    constructor(private messageService: MessageService,
                private enumsService: EnumsService,
                private utilsService: UtilsService,
                private codeDescriptionPipe: CodeDescriptionPipe,
                private percentPipe: PercentPipe,
                private indicatorExecutionService: IndicatorExecutionService) {
    }

    ngOnInit(): void {
        this.loadPerformanceIndicators(this.project.id);
        this.loadOptions();
    }

    private loadPerformanceIndicators(idProject: number) {
        this.indicatorExecutionService.getPerformanceIndicatorResume(idProject)
            .subscribe(value => {
                this.performanceIndicators = value;
                this.createPerformanceIndicatorColumns();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar indicadores generales',
                    detail: error.error.message,
                    life: 3000
                });
            });
    }

    private createPerformanceIndicatorColumns() {

        this.colsGeneralIndicators = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'indicatorType', header: 'Tipo', type: ColumnDataType.text},
            {field: 'indicator', header: 'Indicador', type: ColumnDataType.text, pipeRef: this.codeDescriptionPipe},
            {field: 'target', header: 'Meta', type: ColumnDataType.numeric},
            {field: 'totalExecution', header: 'Ejecución Actual', type: ColumnDataType.numeric},
            {field: 'executionPercentage', header: 'Porcentaje de ejecución', type: ColumnDataType.numeric, pipeRef: this.percentPipe},

        ];

        const hiddenColumns: string[] = ['id'];
        this._selectedColumnsPerformanceIndicators = this.colsGeneralIndicators.filter(value => !hiddenColumns.includes(value.field));
    }

    private loadOptions() {

        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
    }

    selectedIndicatorSet(indicator: IndicatorExecutionResumeWeb) {
        this.selectedIndicator = indicator;
    }

    clearIndicator() {
        this.selectedIndicator = null;
    }

    callMonth(monthId: number) {
        const parametersMap = new Map<string, number | IndicatorExecutionResumeWeb>();
        parametersMap.set('monthId', monthId);
        parametersMap.set('indicator', this.selectedIndicator);
        this.callMonthParent.emit(parametersMap);
    }

    @Input() get selectedColumnsPerformanceIndicators(): any[] {
        return this._selectedColumnsPerformanceIndicators;
    }

    set selectedColumnsPerformanceIndicators(val: any[]) {
        // restore original order
        this._selectedColumnsPerformanceIndicators = this.colsGeneralIndicators.filter(col => val.includes(col));
    }
}
