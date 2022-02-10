import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Indicator, IndicatorExecution, IndicatorExecutionAssigment, Period} from '../../shared/model/OsmosysModel';
import {PeriodService} from '../../shared/services/period.service';
import {FilterService, MessageService, SelectItem} from 'primeng/api';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {UtilsService} from '../../shared/services/utils.service';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {EnumValuesToLabelPipe} from '../../shared/pipes/enum-values-to-label.pipe';
import {IndicatorPipe} from '../../shared/pipes/indicator.pipe';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {OfficeService} from '../../shared/services/office.service';
import {OfficeOrganizationPipe} from '../../shared/pipes/officeOrganization.pipe';
import {IndicatorService} from '../../shared/services/indicator.service';
import {EnumsService} from '../../shared/services/enums.service';
import {UserService} from '../../shared/services/user.service';
import {UserPipe} from '../../shared/pipes/user.pipe';
import {MonthPipe} from '../../shared/pipes/month.pipe';
import {BooleanYesNoPipe} from '../../shared/pipes/boolean-yes-no.pipe';
import {MonthListPipe} from '../../shared/pipes/month-list.pipe';

@Component({
    selector: 'app-direct-implementation-administration',
    templateUrl: './direct-implementation-administration.component.html',
    styleUrls: ['./direct-implementation-administration.component.scss']
})
export class DirectImplementationAdministrationComponent implements OnInit {
    periodForm: FormGroup;
    itemForm: FormGroup;
    showItemDialog = false;
    periods: Period[];
    cols: ColumnTable[];
    items: IndicatorExecution[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    public states: SelectItem[];
    public officeOptions: SelectItem[];
    public indicatorOptions: SelectItem[];
    public userOptions: SelectItem[];

    constructor(
        private fb: FormBuilder,
        private periodService: PeriodService,
        private messageService: MessageService,
        public utilsService: UtilsService,
        private filterUtilsService: FilterUtilsService,
        private filterService: FilterService,
        private indicatorExecutionService: IndicatorExecutionService,
        private officeService: OfficeService,
        private indicatorService: IndicatorService,
        private enumsService: EnumsService,
        private userService: UserService,
        private enumValuesToLabelPipe: EnumValuesToLabelPipe,
        private indicatorPipe: IndicatorPipe,
        private codeDescriptionPipe: CodeDescriptionPipe,
        private officeOrganizationPipe: OfficeOrganizationPipe,
        private userPipe: UserPipe,
        private monthPipe: MonthPipe,
        private monthListPipe: MonthListPipe,
        private booleanYesNoPipe: BooleanYesNoPipe,
    ) {
    }

    ngOnInit(): void {
        this.loadPeriods();
        this.createForms();
        this.registerFilters();

    }

    loadPeriods() {
        this.periodService.getAll().subscribe(value => {
            this.periods = value;
            if (this.periods.length < 1) {
                this.messageService.add({severity: 'error', summary: 'No se encontraron periodos', detail: ''});
            } else {
                const currentYear = (new Date()).getFullYear();
                if (this.periods.some(e => e.year === currentYear)) {
                    this.periods.filter(p => p.year === currentYear).forEach(value1 => {
                        this.periodForm.get('selectedPeriod').patchValue(value1);
                    });
                } else {
                    const smallestYear = Math.min(...this.periods.map(value1 => value1.year));
                    const smallestPeriod = this.periods.filter(value1 => {
                        return value1.year === smallestYear;
                    })[0];
                    this.periodForm.get('selectedPeriod').patchValue(smallestPeriod);
                }
                this.loadOptions(this.periodForm.get('selectedPeriod').value);
                this.loadItems(this.periodForm.get('selectedPeriod').value.id);
            }
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las áreas',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    loadOptions(period: Period) {
        this.officeService.getByState(EnumsState.ACTIVE).subscribe(offices => {
            this.officeOptions = offices
                .sort((a, b) => a.acronym.localeCompare(b.acronym))
                .map(office => {
                    const item: SelectItem = {
                        value: office,
                        label: this.officeOrganizationPipe.transform(office)
                    };
                    return item;
                });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las oficinas',
                detail: error.error.message,
                life: 3000
            });
        });
        this.indicatorService.getByPeriodAssignment(period.id)
            .subscribe(indicators => {
                this.indicatorOptions = indicators
                    .sort((a, b) =>
                        a.description.localeCompare(b.description)
                    )
                    .map(indicator => {
                        const item: SelectItem = {
                            value: indicator,
                            label: this.indicatorPipe.transform(indicator)
                        };
                        return item;
                    });
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los indicadores',
                    detail: error.error.message,
                    life: 3000
                });
            });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
        this.userService.getActiveUNHCRUsers().subscribe(users => {
            this.userOptions = users
                .sort((a, b) => a.name.localeCompare(b.name))
                .map(user => {
                    const item: SelectItem = {
                        value: user,
                        label: this.userPipe.transform(user)
                    };
                    return item;
                });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los usuarios',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    loadItems(periodId: number) {
        this.indicatorExecutionService.getPerformanceAllDirectImplementationByPeriodId(periodId)
            .subscribe(ies => {
                this.items = ies;

            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar las indicadores Asignados',
                    detail: error.error.message,
                    life: 3000
                });
            });

    }

    private createForms() {
        this.periodForm = this.fb.group({
            selectedPeriod: new FormControl('')
        });
        this.itemForm = this.fb.group({
            id: new FormControl(''),
            reportingOffice: new FormControl('', Validators.required),
            state: new FormControl(''),
            period: new FormControl(''),
            statement: new FormControl({value: '', disabled: true}),
            product: new FormControl({value: '', disabled: true}),
            indicator: new FormControl(''),
            supervisorUser: new FormControl('', Validators.required),
            assignedUser: new FormControl('', Validators.required),
            assignedUserBackup: new FormControl(''),
        });


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

    createItem() {
        const period: Period = this.periodForm.get('selectedPeriod').value as Period;
        this.messageService.clear();
        this.utilsService.resetForm(this.itemForm);
        this.showItemDialog = true;
        this.itemForm.get('indicator').enable();
        this.itemForm.get('state').patchValue(EnumsState.ACTIVE);
        this.itemForm.get('period').patchValue(period);
    }

    onPeriodChange(period: Period) {
        this.loadItems(period.id);
    }

    exportExcel() {
        this.utilsService.exportTableAsExcel(this._selectedColumns, this.items, 'Asignacion_indicadores_id');
    }

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }

    saveItem() {
        this.messageService.clear();

        const {
            id,
            reportingOffice,
            state,
            period,
            indicator,
            supervisorUser,
            assignedUser,
            assignedUserBackup
        } = this.itemForm.getRawValue();
        if (assignedUserBackup && assignedUser.id === assignedUserBackup.id) {
            this.messageService.add({
                severity: 'error',
                summary: 'Error en usuarios reponsables',
                detail: 'El usuario responsable y el usuario responsable alterno no pueden ser la misma persona',
                life: 3000
            });
            return;
        }
        const assigment: IndicatorExecutionAssigment = {
            id,
            indicator,
            state,
            period,
            reportingOffice,
            supervisorUser,
            assignedUser,
            assignedUserBackup
        };
        console.log(assigment);
        if (assigment.id) {
            this.indicatorExecutionService
                .updateAssignPerformanceIndicatorDirectImplementation(assigment)
                .subscribe(() => {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Indicador guardado correctamente',
                        life: 3000
                    });
                    this.loadItems(period.id);
                    this.showItemDialog = false;
                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar el indicador',
                        detail: error.error.message,
                        life: 3000
                    });
                    return;
                });
        } else {
            this.indicatorExecutionService.assignPerformanceIndicatorDirectImplementation(assigment)
                .subscribe(() => {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Indicador creado correctamente',
                        life: 3000
                    });
                    this.loadItems(period.id);
                    this.showItemDialog = false;
                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar el indicador',
                        detail: error.error.message,
                        life: 3000
                    });
                    return;
                });

        }


    }

    cancelItemDialog() {
        // this.utilsService.resetForm(this.itemForm);
        // this.messageService.clear();
        this.showItemDialog = false;
    }

    onChangeIndicator(indicator: Indicator) {
        if (indicator) {
            this.itemForm.get('statement').patchValue(this.codeDescriptionPipe.transform(indicator.statement));
            this.itemForm.get('product').patchValue(indicator.statement.productCode);
        } else {
            this.itemForm.get('statement').patchValue(null);
            this.itemForm.get('product').patchValue(null);
        }
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

    updateAssigment(indicatorExecution: IndicatorExecution) {
        const {
            id,
            indicator,
            state,
            period,
            reportingOffice,
            supervisorUser,
            assignedUser,
            assignedUserBackup,
        } = indicatorExecution;
        const assigment: IndicatorExecutionAssigment = {
            id,
            indicator,
            state,
            period,
            reportingOffice,
            supervisorUser,
            assignedUser,
            assignedUserBackup,
        };
        this.messageService.clear();
        this.utilsService.resetForm(this.itemForm);
        this.itemForm.patchValue(assigment);
        this.itemForm.get('indicator').disable();
        this.showItemDialog = true;
        this.onChangeIndicator(indicator);
        console.log(this.itemForm.value);
    }
}
