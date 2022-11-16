import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {IndicatorExecution, Project} from '../../shared/model/OsmosysModel';
import {FilterService, MessageService, SelectItem} from 'primeng/api';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {PercentPipe} from '@angular/common';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {IndicatorPipe} from '../../shared/pipes/indicator.pipe';
import {MonthPipe} from '../../shared/pipes/month.pipe';
import {Table} from 'primeng/table';
import {OverlayPanel} from 'primeng/overlaypanel';
import {EnumsService} from '../../services/enums.service';
import {UtilsService} from '../../services/utils.service';
import {IndicatorExecutionService} from '../../services/indicator-execution.service';
import {FilterUtilsService} from '../../services/filter-utils.service';

@Component({
    selector: 'app-partners-project-performance-indicator-list',
    templateUrl: './partners-project-performance-indicator-list.component.html',
    styleUrls: ['./partners-project-performance-indicator-list.component.scss']
})
export class PartnersProjectPerformanceIndicatorListComponent implements OnInit, OnChanges {
    @Input()
    public project: Project;
    // roles
    @Input()
    isAdmin = false;
    @Input()
    isProjectFocalPoint = false;
    @Input()
    isEjecutor = false;

    @Output()
    callMonthParent = new EventEmitter<Map<string, number | string | IndicatorExecution>>();

    public performanceIndicators: IndicatorExecution[];
    // tslint:disable-next-line:variable-name
    _selectedColumnsPerformanceIndicators: ColumnTable[];
    colsGeneralIndicators: ColumnTable[];

    states: SelectItem[];
    selectedIndicator: IndicatorExecution;

    constructor(private messageService: MessageService,
                private enumsService: EnumsService,
                public utilsService: UtilsService,
                private codeDescriptionPipe: CodeDescriptionPipe,
                private percentPipe: PercentPipe,
                private indicatorPipe: IndicatorPipe,
                private indicatorExecutionService: IndicatorExecutionService,
                private filterService: FilterService,
                private filterUtilsService: FilterUtilsService,
                private monthPipe: MonthPipe
    ) {
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.project.previousValue) {
            this.loadPerformanceIndicators(this.project.id);
        } else {
        }
    }

    ngOnInit(): void {
        this.loadPerformanceIndicators(this.project.id);
        this.loadOptions();
    }

    private loadPerformanceIndicators(idProject: number) {
        this.indicatorExecutionService.getPerformanceIndicatorResume(idProject)
            .subscribe(value => {
                this.performanceIndicators = value
                    .sort((a, b) => {
                        return a.indicator.code.localeCompare(b.indicator.code);
                    })
                    .sort((a, b) => {
                        return a.projectStatement.code.localeCompare(b.projectStatement.code);
                    });
                this.createPerformanceIndicatorColumns();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar indicadores',
                    detail: error.error.message,
                    life: 3000
                });
            });
    }

    private createPerformanceIndicatorColumns() {

        this.colsGeneralIndicators = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'indicatorType', header: 'Tipo', type: ColumnDataType.text},
            {field: 'projectStatement', header: 'Declaración de Producto', type: ColumnDataType.text, pipeRef: this.codeDescriptionPipe},
            {field: 'projectStatement.productCode', header: 'Código de Producto', type: ColumnDataType.text},
            {field: 'indicator', header: 'Indicador', type: ColumnDataType.text, pipeRef: this.indicatorPipe},
            {field: 'target', header: 'Meta', type: ColumnDataType.numeric},
            {field: 'totalExecution', header: 'Ejecución Actual', type: ColumnDataType.numeric},
            {field: 'executionPercentage', header: 'Porcentaje de ejecución', type: ColumnDataType.numeric, pipeRef: this.percentPipe},
            {field: 'lastReportedMonth', header: 'Último mes reportado', type: ColumnDataType.text, pipeRef: this.monthPipe},

        ];

        const hiddenColumns: string[] = ['id', 'indicatorType'];
        this._selectedColumnsPerformanceIndicators = this.colsGeneralIndicators.filter(value => !hiddenColumns.includes(value.field));
        this.registerFilters();
    }

    private registerFilters() {
        this.filterService.register('projectStatementFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['code', 'description'], filter);
        });
        this.filterService.register('indicatorFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['code', 'description', 'category'], filter);
        });
        this.filterService.register('monthFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['month', 'year'], filter);
        });
    }

    private loadOptions() {

        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
    }

    selectedIndicatorSet(indicator: IndicatorExecution) {
        this.selectedIndicator = indicator;
    }

    clearIndicator() {
        this.selectedIndicator = null;
    }

    callMonth(parameters: Map<string, number | string>, overlayPanel: OverlayPanel) {
        overlayPanel.hide();
        const parametersMap = new Map<string, number | string | IndicatorExecution>();
        parametersMap.set('monthId', parameters.get('monthId'));
        parametersMap.set('month', parameters.get('month'));
        parametersMap.set('year', parameters.get('year'));
        parametersMap.set('indicator', this.selectedIndicator);
        this.callMonthParent.emit(parametersMap);
    }

    callMonthV2(parameters: Map<string, number | string>) {
        const parametersMap = new Map<string, number | string | IndicatorExecution>();
        parametersMap.set('monthId', parameters.get('monthId'));
        parametersMap.set('month', parameters.get('month'));
        parametersMap.set('year', parameters.get('year'));
        parametersMap.set('indicator', this.selectedIndicator);
        this.callMonthParent.emit(parametersMap);
    }

    // noinspection JSUnusedLocalSymbols
    refreshData(monthId: number, overlayPanel: OverlayPanel) {
        this.loadPerformanceIndicators(this.project.id);
    }

    refreshDataV2(monthId: number) {
        this.loadPerformanceIndicators(this.project.id);
    }

    @Input() get selectedColumnsPerformanceIndicators(): any[] {
        return this._selectedColumnsPerformanceIndicators;
    }

    set selectedColumnsPerformanceIndicators(val: any[]) {
        // restore original order
        this._selectedColumnsPerformanceIndicators = this.colsGeneralIndicators.filter(col => val.includes(col));
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumnsPerformanceIndicators,
            table.filteredValue ? table.filteredValue : this.performanceIndicators,
            'indicadores');
    }
}
