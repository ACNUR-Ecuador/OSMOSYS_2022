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
                private userPipe: UserPipe,
                public utilsService: UtilsService,
                private enumsService: EnumsService,
                private filterService: FilterService,
                private filterUtilsService: FilterUtilsService,
    ) {
        if (this.router.getCurrentNavigation().extras.state) {
            this.indicatorExecutionIds = this.router.getCurrentNavigation().extras.state.indicatorExecutionIds;
            console.log('->');
            console.log(this.indicatorExecutionIds);
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

    exportExcel() {
        this.utilsService.exportTableAsExcelV3(this._selectedColumns, this.items, 'estado_indicadores_id');
    }
}
