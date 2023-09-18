import {
    ChangeDetectorRef,
    Component,
    EventEmitter,
    Input,
    OnChanges,
    OnInit,
    Output,
    SimpleChanges
} from '@angular/core';
import {IndicatorExecution, Project} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {FilterService, MessageService, SelectItem} from 'primeng/api';
import {PercentPipe} from '@angular/common';
import {MonthPipe} from '../../shared/pipes/month.pipe';
import {Table} from 'primeng/table';
import {OverlayPanel} from 'primeng/overlaypanel';
import {EnumsService} from '../../services/enums.service';
import {UtilsService} from '../../services/utils.service';
import {IndicatorExecutionService} from '../../services/indicator-execution.service';
import {FilterUtilsService} from '../../services/filter-utils.service';

@Component({
    selector: 'app-partners-project-general-indicator-list',
    templateUrl: './partners-project-general-indicator-list.component.html',
    styleUrls: ['./partners-project-general-indicator-list.component.scss']
})
export class PartnersProjectGeneralIndicatorListComponent implements OnInit, OnChanges {
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

    public generalIndicators: IndicatorExecution[];
    // tslint:disable-next-line:variable-name
    _selectedColumnsGeneralIndicators: ColumnTable[];
    colsGeneralIndicators: ColumnTable[];

    states: SelectItem[];
    selectedIndicator: IndicatorExecution;

    constructor(
        private messageService: MessageService,
        private enumsService: EnumsService,
        public utilsService: UtilsService,
        private codeDescriptionPipe: CodeDescriptionPipe,
        private percentPipe: PercentPipe,
        private indicatorExecutionService: IndicatorExecutionService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private monthPipe: MonthPipe,
        private cd: ChangeDetectorRef
    ) {
    }

    ngOnInit(): void {
        this.loadGeneralIndicators(this.project.id);
        this.loadOptions();
        this.registerFilters();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.project.previousValue) {
            this.loadGeneralIndicators(this.project.id);
        } else {
        }
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
            {
                field: 'executionPercentage',
                header: 'Porcentaje de ejecución',
                type: ColumnDataType.numeric,
                pipeRef: this.percentPipe
            },
            {
                field: 'lastReportedMonth',
                header: 'Último Mes Reportado',
                type: ColumnDataType.text,
                pipeRef: this.monthPipe
            },

        ];

        const hiddenColumns: string[] = ['id'];
        this._selectedColumnsGeneralIndicators = this.colsGeneralIndicators.filter(value => !hiddenColumns.includes(value.field));
    }

    @Input() get selectedColumnsGeneralIndicators(): any[] {
        return this._selectedColumnsGeneralIndicators;
    }

    set selectedColumnsGeneralIndicators(val: any[]) {
        // restore original order
        this._selectedColumnsGeneralIndicators = this.colsGeneralIndicators.filter(col => val.includes(col));
    }

    private loadOptions() {

        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
    }

    callMonth(parameters: Map<string, number | string>, overlayPanel: OverlayPanel) {
        overlayPanel.hide();
        const parametersMap = new Map<string, number | string | IndicatorExecution>();
        parametersMap.set('monthId', parameters.get('monthId') as number);
        parametersMap.set('month', parameters.get('month') as string);
        parametersMap.set('year', parameters.get('year') as number);
        parametersMap.set('indicator', this.selectedIndicator);
        this.callMonthParent.emit(parametersMap);
    }

    refreshData() {
        this.loadGeneralIndicators(this.project.id);
        this.cd.detectChanges();
    }

    selectedIndicatorSet(indicator: IndicatorExecution) {
        this.selectedIndicator = indicator;
    }

    clearIndicator() {
        this.selectedIndicator = null;
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumnsGeneralIndicators,
            table.filteredValue ? table.filteredValue : this._selectedColumnsGeneralIndicators,
            'indicadores');
    }

    private registerFilters() {
        this.filterService.register('indicatorFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['code', 'description'], filter);
        });
        this.filterService.register('monthFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['month', 'year'], filter);
        });
    }


}
