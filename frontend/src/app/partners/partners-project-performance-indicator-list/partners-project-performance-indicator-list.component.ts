import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IndicatorExecutionResumeWeb, Project} from '../../shared/model/OsmosysModel';
import {FilterService, MessageService, SelectItem} from 'primeng/api';
import {EnumsService} from '../../shared/services/enums.service';
import {UtilsService} from '../../shared/services/utils.service';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {PercentPipe} from '@angular/common';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';
import {IndicatorPipe} from '../../shared/pipes/indicator.pipe';
import {MonthPipe} from '../../shared/pipes/month.pipe';

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
                private indicatorPipe: IndicatorPipe,
                private indicatorExecutionService: IndicatorExecutionService,
                private filterService: FilterService,
                private filterUtilsService: FilterUtilsService,
                private monthPipe: MonthPipe
    ) {
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

    exportExcel() {
        import('xlsx').then(xlsx => {
            const headers = this.selectedColumnsPerformanceIndicators.map(value => value.header);
            const itemsRenamed = this.utilsService.renameKeys(this.performanceIndicators, this.selectedColumnsPerformanceIndicators);
            const worksheet = xlsx.utils.json_to_sheet(itemsRenamed);
            const workbook = {Sheets: {data: worksheet}, SheetNames: ['data']};

            const excelBuffer: any = xlsx.write(workbook, {bookType: 'xlsx', type: 'array'});
            this.utilsService.saveAsExcelFile(excelBuffer, 'indicadores_producto_' + this.project.code + '_' + this.project.name);
        });
    }
}
