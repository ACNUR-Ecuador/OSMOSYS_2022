import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {
    ImportFile,
    Indicator,
    IndicatorExecution,
    IndicatorExecutionAssigment, Office,
    Period
} from '../../shared/model/OsmosysModel';
import {FilterService, MessageService, SelectItem} from 'primeng/api';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {EnumValuesToLabelPipe} from '../../shared/pipes/enum-values-to-label.pipe';
import {IndicatorPipe} from '../../shared/pipes/indicator.pipe';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {BooleanYesNoPipe} from '../../shared/pipes/boolean-yes-no.pipe';
import {Table} from 'primeng/table';
import {PeriodService} from '../../services/period.service';
import {UtilsService} from '../../services/utils.service';
import {FilterUtilsService} from '../../services/filter-utils.service';
import {IndicatorExecutionService} from '../../services/indicator-execution.service';
import {OfficeService} from '../../services/office.service';
import {IndicatorService} from '../../services/indicator.service';
import {EnumsService} from '../../services/enums.service';
import {UserService} from '../../services/user.service';
import {OfficeOrganizationPipe} from '../../shared/pipes/office-organization.pipe';
import {UserPipe} from '../../shared/pipes/user.pipe';
import {MonthPipe} from '../../shared/pipes/month.pipe';
import {MonthListPipe} from '../../shared/pipes/month-list.pipe';
import {HttpResponse} from "@angular/common/http";
import {PercentPipe} from "@angular/common";


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

    showDialogImport = false;
    importForm: FormGroup;
    periodsItems: SelectItem[];
    adminOffices: Office[];

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
        private percentPipe: PercentPipe
    ) {
    }

    ngOnInit(): void {
        this.adminOffices = this.userService.getLogedUsername().administratedOffices;
        this.loadPeriods();
        this.createForms();
        this.registerFilters();

    }

    loadPeriods() {
        this.periodService.getAll()
            .subscribe({
                next: value => {
                    this.periods = value;
                    if (this.periods.length < 1) {
                        this.messageService.add({severity: 'error', summary: 'No se encontraron años', detail: ''});
                    } else {
                        this.periodsItems = value.map(value1 => {
                            const selectItem: SelectItem = {
                                label: value1.year.toString(),
                                value: value1
                            };
                            return selectItem;
                        });
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
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los años',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
    }

    loadOptions(period: Period) {
        this.officeService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: offices => {
                    this.officeOptions = offices
                        .sort((a, b) => a.acronym.localeCompare(b.acronym))
                        .map(office => {
                            const item: SelectItem = {
                                value: office,
                                label: this.officeOrganizationPipe.transform(office)
                            };
                            return item;
                        });
                    if (!this.userService.hasAnyRole(['SUPER_ADMINISTRADOR', 'ADMINISTRADOR_REGIONAL','ADMINISTRADOR_LOCAL'])
                        && this.userService.hasAnyRole(['ADMINISTRADOR_OFICINA']) ) {
                        const officesIds = this.adminOffices.map(value => value.id);
                        this.officeOptions = this.officeOptions.filter(value => {
                            return officesIds.includes(value.value.id)
                        });
                    }
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las oficinas',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });

        this.indicatorService.getByPeriodAssignment(period.id)
            .subscribe({
                next: indicators => {
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
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los indicadores',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
        this.userService.getActiveUNHCRUsers()
            .subscribe({
                next: users => {
                    this.userOptions = users
                        .sort((a, b) => a.name.localeCompare(b.name))
                        .map(user => {
                            const item: SelectItem = {
                                value: user,
                                label: this.userPipe.transform(user)
                            };
                            return item;
                        });
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los usuarios',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
    }

    loadItems(periodId: number) {
        if (this.userService.hasAnyRole(['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL'])) {
            this.indicatorExecutionService.getPerformanceAllDirectImplementationByPeriodId(periodId)
                .subscribe({
                    next: ies => {
                        this.items = ies;
                    },
                    error: error => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al cargar las indicadores Asignados',
                            detail: error.error.message,
                            life: 3000
                        });
                    }
                });
        } else if (this.userService.hasAnyRole(['ADMINISTRADOR_OFICINA',])) {
            this.indicatorExecutionService.getPerformanceAllDirectImplementationByPeriodId(periodId)
                .subscribe({
                    next: ies => {
                        const officesIds = this.adminOffices.map(value => value.id);
                        this.items = ies.filter(value => {
                            return officesIds.includes(value.reportingOffice.id)
                        });

                    },
                    error: error => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al cargar las indicadores Asignados',
                            detail: error.error.message,
                            life: 3000
                        });
                    }
                });
        }
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
            target: new FormControl('',Validators.required),
            supervisorUser: new FormControl('', Validators.required),
            assignedUser: new FormControl('', Validators.required),
            assignedUserBackup: new FormControl(''),
            keepBudget: new FormControl('', Validators.required),
            assignedBudget: new FormControl(''),
        });


        this.cols = [
            {field: 'id', header: 'id', type: ColumnDataType.numeric},
            {
                field: 'reportingOffice',
                header: 'Oficina/Unidad',
                type: ColumnDataType.text,
                pipeRef: this.officeOrganizationPipe
            },
            {
                field: 'indicator.statement',
                header: 'Enunciado de Producto',
                type: ColumnDataType.text,
                pipeRef: this.codeDescriptionPipe
            },
            {field: 'indicator.statement.productCode', header: 'Código Producto', type: ColumnDataType.text},
            {field: 'indicator', header: 'Indicador', type: ColumnDataType.text, pipeRef: this.indicatorPipe},
           
            {field: 'target', header: 'Meta', type: ColumnDataType.numeric},
            {field: 'totalExecution', header: 'Ejecución Total', type: ColumnDataType.numeric},
            {field: 'executionPercentage', header: 'Porcentaje de ejecución', type: ColumnDataType.numeric,pipeRef: this.percentPipe},
            {field: 'late', header: 'Atrasado', type: ColumnDataType.boolean, pipeRef: this.booleanYesNoPipe},
            {
                field: 'lastReportedMonth',
                header: 'Último mes reportado',
                type: ColumnDataType.text,
                pipeRef: this.monthPipe
            },
            {field: 'lateMonths', header: 'Meses Retrasado', type: ColumnDataType.text, pipeRef: this.monthListPipe},
            {field: 'supervisorUser', header: 'Supervisor de la Implementación', type: ColumnDataType.text, pipeRef: this.userPipe},
            {field: 'assignedUser', header: 'Responsable', type: ColumnDataType.text, pipeRef: this.userPipe},
            {
                field: 'assignedUserBackup',
                header: 'Responsable alterno',
                type: ColumnDataType.text,
                pipeRef: this.userPipe
            },
            {
                field: 'state',
                header: 'Estado',
                type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.State
            },
        ];

        const hiddenColumns: string[] = ['id', 'indicator.statement.productCode', 'lateMonths', 'assignedUserBackup'];
        this._selectedColumns = this.cols.filter(value => !hiddenColumns.includes(value.field));
        this.importForm = this.fb.group({
            period: new FormControl('', [Validators.required]),
            office: new FormControl('', [Validators.required]),
            fileName: new FormControl('', [Validators.required]),
            file: new FormControl(''),
        });
    }

    createItem() {
        const period: Period = this.periodForm.get('selectedPeriod').value as Period;
        this.messageService.clear();
        this.utilsService.resetForm(this.itemForm);
        this.showItemDialog = true;
        this.itemForm.get('indicator').enable();
        this.itemForm.get('state').patchValue(EnumsState.ACTIVE);
        this.itemForm.get('period').patchValue(period);
        this.itemForm.get('keepBudget').patchValue(false);
        this.setUpAssignedBudget(this.itemForm.get('keepBudget').value);
    }

    onPeriodChange(period: Period) {
        this.loadItems(period.id);
        this.loadOptions(period);
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'asignacion_indicadores_implemetacion_directa');
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
            assignedUserBackup,
            keepBudget,
            assignedBudget,
            target
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
            assignedUserBackup,
            keepBudget,
            assignedBudget,
            target
        };
        if (assigment.id) {
            this.indicatorExecutionService
                .updateAssignPerformanceIndicatorDirectImplementation(assigment)
                .subscribe({
                    next: () => {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Indicador guardado correctamente',
                            life: 3000
                        });
                        this.loadItems(period.id);
                        this.showItemDialog = false;
                    },
                    error: error => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al guardar el indicador',
                            detail: error.error.message,
                            life: 3000
                        });
                        return;
                    }
                });
        } else {
            this.indicatorExecutionService.assignPerformanceIndicatorDirectImplementation(assigment)
                .subscribe({
                    next: () => {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Indicador creado correctamente',
                            life: 3000
                        });
                        this.loadItems(period.id);
                        this.showItemDialog = false;
                    },
                    error: error => {

                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al guardar el indicador',
                            detail: error.error.message,
                            life: 3000
                        });
                        return;
                    }
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
        this.filterService.register('objectIdFilter', (value, filter): boolean => {
            return this.filterUtilsService.objectFilterId(value, filter);
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
            keepBudget,
            assignedBudget,
            target
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
            keepBudget,
            assignedBudget,
            target
        };
        this.messageService.clear();
        this.utilsService.resetForm(this.itemForm);
        this.itemForm.patchValue(assigment);
        this.itemForm.get('indicator').disable();
        this.showItemDialog = true;
        this.onChangeIndicator(indicator);
        this.setUpAssignedBudget(this.itemForm.get('keepBudget').value);
    }

    onKeepBudgetChange(event: any) {
        if (event.checked) {
            this.setUpAssignedBudget(true);
        } else {
            this.setUpAssignedBudget(false);
        }
    }

    setUpAssignedBudget(active: boolean) {
        if (active) {
            this.itemForm.get('assignedBudget').setValidators(Validators.required);
            this.itemForm.get('assignedBudget').enable();
        } else {
            this.itemForm.get('assignedBudget').clearValidators();
            this.itemForm.get('assignedBudget').disable();
            this.itemForm.get('assignedBudget').patchValue(null);
        }
    }

    initiateCatalogImport() {
        this.importForm.reset();
        this.showDialogImport = true;

    }

    importCatalog() {
        const {
            period,
            office,
            fileName,
            file
        } = this.importForm.value;
        const importFile: ImportFile = {
            period,
            fileName,
            file
        };

        this.indicatorExecutionService.importDirectImplementationIndicators(importFile, period.id, office.id).subscribe({
            next: () => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Catálogo cargado correctamente',
                    life: 3000
                });
                this.loadPeriods();
                this.showDialogImport = false;
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al descargar la plantilla',
                    detail: err.error.message,
                    life: 3000
                });
            }
        })

    }

    downloadImportTemplate() {
        const period: Period = this.importForm.get('period').value;
        const office: Period = this.importForm.get('office').value;
        if (!period || !period.id) {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona un año',
                life: 3000
            });
        }
        this.indicatorExecutionService.getDirectImplementationTemplate(period.id, office.id).subscribe({
            next: (response: HttpResponse<Blob>) => {
                this.utilsService.downloadFileResponse(response);
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al descargar la plantilla',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    fileUploader(event: any) {
        const file = event.files[0];
        this.importForm.get('fileName').setValue(file.name);
        this.importForm.get('fileName').markAsTouched();
        // event.
        const fileReader = new FileReader();

        fileReader.readAsDataURL(file);
        // tslint:disable-next-line:only-arrow-functions
        fileReader.onload = () => {
            this.importForm.get('file').setValue(fileReader.result);
            this.importForm.get('file').markAsTouched();
        };
    }

    cancelImportDialog() {
        this.showDialogImport = false;
        this.importForm.reset();
    }


}
