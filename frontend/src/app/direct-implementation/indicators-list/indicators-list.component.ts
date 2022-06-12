import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {IndicatorExecution} from '../../shared/model/OsmosysModel';
import {FilterService, MessageService, SelectItem} from 'primeng/api';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {EnumValuesToLabelPipe} from '../../shared/pipes/enum-values-to-label.pipe';
import {IndicatorPipe} from '../../shared/pipes/indicator.pipe';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {OfficeOrganizationPipe} from '../../shared/pipes/officeOrganization.pipe';
import {MonthPipe} from '../../shared/pipes/month.pipe';
import {MonthListPipe} from '../../shared/pipes/month-list.pipe';
import {BooleanYesNoPipe} from '../../shared/pipes/boolean-yes-no.pipe';
import {UserPipe} from '../../shared/pipes/user.pipe';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';
import {DialogService} from 'primeng/dynamicdialog';
import {
    DirectImplementationPerformanceIndicatorFormComponent
} from '../../indicator-forms/direct-implementation-performance-indicator-form/direct-implementation-performance-indicator-form.component';
import {Table} from 'primeng/table';
import {UserService} from '../../shared/services/user.service';
import {OverlayPanel} from 'primeng/overlaypanel';

@Component({
    selector: 'app-indicators-list',
    templateUrl: './indicators-list.component.html',
    styleUrls: ['./indicators-list.component.scss']
})
export class IndicatorsListComponent implements OnInit {
    indicatorExecutionIds: number[];
    items: IndicatorExecution[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    cols: ColumnTable[];

    public stateOptions: SelectItem[];

    private selectedIndicatorIndicatorExecution: IndicatorExecution;

    constructor(private router: Router,
                private indicatorExecutionService: IndicatorExecutionService,
                private messageService: MessageService,
                private enumValuesToLabelPipe: EnumValuesToLabelPipe,
                private indicatorPipe: IndicatorPipe,
                private codeDescriptionPipe: CodeDescriptionPipe,
                private officeOrganizationPipe: OfficeOrganizationPipe,
                private monthPipe: MonthPipe,
                private monthListPipe: MonthListPipe,
                private booleanYesNoPipe: BooleanYesNoPipe,
                private userService: UserService,
                private userPipe: UserPipe,
                public utilsService: UtilsService,
                private enumsService: EnumsService,
                private filterService: FilterService,
                private filterUtilsService: FilterUtilsService,
                private dialogService: DialogService
    ) {
        if (this.router.getCurrentNavigation().extras.state) {
            this.indicatorExecutionIds = this.router.getCurrentNavigation().extras.state.indicatorExecutionIds;
        } else {
            this.router.navigateByUrl('/directImplementation/areasMenu');
        }
    }

    ngOnInit(): void {
        this.loadIndicatorExecutions();
        this.createCols();
        this.loadOptions();
        this.registerFilters();
    }

    private loadIndicatorExecutions() {
        this.indicatorExecutionService
            .getDirectImplementationIndicatorExecutionsByIds(this.indicatorExecutionIds).subscribe(value => {
            this.items = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los indicadores',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    private createCols() {
        // noinspection DuplicatedCode
        this.cols = [
            {field: 'id', header: 'id', type: ColumnDataType.numeric},
            {field: 'reportingOffice', header: 'Oficina', type: ColumnDataType.text, pipeRef: this.officeOrganizationPipe},
            {field: 'indicator.statement', header: 'Declaración de Producto', type: ColumnDataType.text, pipeRef: this.codeDescriptionPipe},
            {field: 'indicator.statement.productCode', header: 'Código Producto', type: ColumnDataType.text},
            {field: 'indicator', header: 'Indicador', type: ColumnDataType.text, pipeRef: this.indicatorPipe},
            {field: 'state', header: 'Estado', type: ColumnDataType.text, pipeRef: this.enumValuesToLabelPipe, arg1: EnumsType.State},
            {field: 'totalExecution', header: 'Ejecución Total', type: ColumnDataType.numeric},
            {field: 'late', header: 'Atrasado', type: ColumnDataType.boolean, pipeRef: this.booleanYesNoPipe},
            {field: 'lastReportedMonth', header: 'Último mes reportado', type: ColumnDataType.text, pipeRef: this.monthPipe},
            {field: 'lateMonths', header: 'Meses Retrasado', type: ColumnDataType.text, pipeRef: this.monthListPipe},
            {field: 'supervisorUser', header: 'Supervisor', type: ColumnDataType.text, pipeRef: this.userPipe},
            {field: 'assignedUser', header: 'Responsable', type: ColumnDataType.text, pipeRef: this.userPipe},
            {field: 'assignedUserBackup', header: 'Responsable alterno', type: ColumnDataType.text, pipeRef: this.userPipe}
        ];

        const hiddenColumns: string[] = ['id', 'indicator.statement.productCode', 'lateMonths', 'assignedUserBackup'];
        this._selectedColumns = this.cols.filter(value => !hiddenColumns.includes(value.field));
    }

    private registerFilters() {
        this.filterService.register('indicatorFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['code', 'description', 'category'], filter);
        });
        this.filterService.register('statementFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['code', 'description'], filter);
        });
        this.filterService.register('userFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['name'], filter);
        });
        this.filterService.register('officeFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['description', 'acronym'], filter);
        });
        this.filterService.register('monthFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['month', 'year'], filter);
        });
        this.filterService.register('monthListFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalListFilter(value, ['month', 'year'], filter);
        });
        this.filterService.register('userFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['name'], filter);
        });
    }

    private loadOptions() {
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.stateOptions = value;
        });
    }

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'indicadores');
    }

    selectedIndicatorIndicatorExecutionSet(indicatorExecution: IndicatorExecution) {
        this.selectedIndicatorIndicatorExecution = indicatorExecution;
    }

    clearSelectedIndicatorIndicatorExecution() {
        this.selectedIndicatorIndicatorExecution = null;
    }

    callMonth(monthId: any, overlayer: OverlayPanel) {
        overlayer.hide();
        const parametersMap = new Map<string, number | IndicatorExecution>();
        parametersMap.set('monthId', monthId);
        parametersMap.set('indicator', this.selectedIndicatorIndicatorExecution);
        this.viewDesagregationPerformanceIndicator(parametersMap);
    }

    viewDesagregationPerformanceIndicator(parameters: Map<string, number | IndicatorExecution>) {
        const monthId = parameters.get('monthId') as number;
        const indicatorExecution = parameters.get('indicator') as IndicatorExecution;
        const ref = this.dialogService.open(DirectImplementationPerformanceIndicatorFormComponent, {
                header: 'Indicador: ' + this.getHeaderIndicatorForm(indicatorExecution),
                width: '90%',
                height: '90%',
                closeOnEscape: false,
                autoZIndex: false,
                closable: false,
                modal: true,
                data: {
                    indicatorExecution,
                    monthId
                }
            }
        );
        // noinspection JSUnusedLocalSymbols
        ref.onClose.subscribe(() => {
            this.loadIndicatorExecutions();
        }, error => {
            this.loadIndicatorExecutions();
        });
    }

    private getHeaderIndicatorForm(indicatorExecution: IndicatorExecution) {
        const userId = this.userService.getLogedUsername().id;
        let header = this.indicatorPipe.transform(indicatorExecution.indicator);
        header += '(' + indicatorExecution.reportingOffice.acronym + ')';
        const roles: string[] = [];
        if (indicatorExecution.supervisorUser && indicatorExecution.supervisorUser.id === userId) {
            roles.push('Supervisor');
        }
        if (indicatorExecution.assignedUser && indicatorExecution.assignedUser.id === userId) {
            roles.push('Responsable de reporte');
        }
        if (indicatorExecution.assignedUserBackup && indicatorExecution.assignedUserBackup.id === userId) {
            roles.push('Responsable de reporte alterno');
        }
        if (roles.length > 0) {
            header = header + ' (' + roles.join(', ') + ')';
        }
        return header;
    }

    // noinspection JSUnusedLocalSymbols
    refreshData(monthId: number, overlayPanel: OverlayPanel) {
        this.loadIndicatorExecutions();
    }
}
