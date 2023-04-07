import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {
    Area, AreaType,
    CustomDissagregation,
    CustomDissagregationAssignationToIndicator,
    DissagregationAssignationToIndicator, ImportFile,
    Indicator,
    Marker,
    Period,
    Statement
} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, FilterService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {EnumsService} from '../../services/enums.service';
import {IndicatorService} from '../../services/indicator.service';
import {StatementService} from '../../services/statement.service';
import {CodeShortDescriptionPipe} from '../../shared/pipes/code-short-description.pipe';
import {CustomDissagregationService} from '../../services/custom-dissagregation.service';
import {MarkerService} from '../../services/marker.service';
import {EnumValuesToLabelPipe} from '../../shared/pipes/enum-values-to-label.pipe';
import {BooleanYesNoPipe} from '../../shared/pipes/boolean-yes-no.pipe';
import {MarkersListPipe} from '../../shared/pipes/markers-list.pipe';
import {
    DissagregationsAssignationToIndicatorPipe
} from '../../shared/pipes/dissagregations-assignation-to-indicator.pipe';
import {
    CustomDissagregationsAssignationToIndicatorPipe
} from '../../shared/pipes/custom-dissagregations-assignation-to-indicator.pipe';
import {FilterUtilsService} from '../../services/filter-utils.service';
import {PeriodService} from '../../services/period.service';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {Table} from 'primeng/table';
import {PeriodsFromIndicatorPipe} from "../../shared/pipes/periods-from-indicator.pipe";
import {HttpResponse} from "@angular/common/http";


@Component({
    selector: 'app-performance-indicator-administration',
    templateUrl: './performance-indicator-administration.component.html',
    styleUrls: ['./performance-indicator-administration.component.scss']
})
export class PerformanceIndicatorAdministrationComponent implements OnInit {

    items: Indicator[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    states: SelectItem[];
    indicatorTypes: SelectItem[];
    measureTypes: SelectItem[];
    frecuencies: SelectItem[];
    areaTypes: SelectItem[];
    unitTypes: SelectItem[];
    totalIndicatorCalculationTypes: SelectItem[];
    statements: Statement[];
    filteredStatements: { labelItem: string, valueItem: Statement }[];
    customDissagregations: CustomDissagregation[];
    dissagregationTypes: SelectItem[];
    markers: Marker[];
    isMonitoredOptions: any[];
    isCalculatedOptions: any[];
    periods: Period[];
    periodsSelectItems: SelectItem[];

    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    showDialogImport = false;
    importForm: FormGroup;

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private indicatorService: IndicatorService,
        private statementService: StatementService,
        private codeShortDescriptionPipe: CodeShortDescriptionPipe,
        private enumValuesToLabelPipe: EnumValuesToLabelPipe,
        private customDissagregationService: CustomDissagregationService,
        private booleanYesNoPipe: BooleanYesNoPipe,
        private periodsFromIndicatorPipe: PeriodsFromIndicatorPipe,
        private markersListPipe: MarkersListPipe,
        private codeDescriptionPipe: CodeDescriptionPipe,
        private dissagregationsAssignationToIndicatorPipe: DissagregationsAssignationToIndicatorPipe,
        private customDissagregationsAssignationToIndicatorPipe: CustomDissagregationsAssignationToIndicatorPipe,
        private markerService: MarkerService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private periodService: PeriodService,
        private ref: ChangeDetectorRef
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.loadStatements();
        this.registerFilters();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: null, header: 'Periodos', type: ColumnDataType.text, pipeRef: this.periodsFromIndicatorPipe},
            {field: 'code', header: 'Código', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            {field: 'category', header: 'Categoría', type: ColumnDataType.text},
            {
                field: 'state', header: 'Estado', type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.IndicatorType
            },
            {
                field: 'indicatorType',
                header: 'Tipo',
                type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.IndicatorType
            },
            {
                field: 'measureType', header: 'Medida', type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.MeasureType
            },
            {
                field: 'frecuency', header: 'Frecuencia', type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.Frecuency
            },
            {
                field: 'areaType', header: 'Area', type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.AreaType
            },
            {
                field: 'compassIndicator',
                header: 'Indicador Compass',
                type: ColumnDataType.boolean,
                pipeRef: this.booleanYesNoPipe
            },
            {field: 'isMonitored', header: 'Monitoreado', type: ColumnDataType.boolean, pipeRef: this.booleanYesNoPipe},
            {field: 'isCalculated', header: 'Calculado', type: ColumnDataType.boolean, pipeRef: this.booleanYesNoPipe},
            {
                field: 'unit',
                header: 'Unidad',
                type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.UnitType
            },
            {
                field: 'totalIndicatorCalculationType',
                header: 'Tipo de Cálculo Total',
                type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.TotalIndicatorCalculationType
            },
            {field: 'markers', header: 'Marcadores', type: ColumnDataType.text, pipeRef: this.markersListPipe},
            {field: 'statement', header: 'Declaración', type: ColumnDataType.text, pipeRef: this.codeDescriptionPipe},
            {
                field: 'dissagregationsAssignationToIndicator',
                header: 'Desagregaciones',
                type: ColumnDataType.text,
                pipeRef: this.dissagregationsAssignationToIndicatorPipe
            },
            {
                field: 'customDissagregationAssignationToIndicators',
                header: 'Desagregaciones Personalizadas',
                type: ColumnDataType.text,
                pipeRef: this.customDissagregationsAssignationToIndicatorPipe
            },
            {
                field: 'instructions',
                header: 'Instrucciones',
                type: ColumnDataType.text
            }
        ];

        const hiddenColumns: string[] = ['id', 'guideDirectImplementation', 'markers', 'customDissagregationAssignationToIndicators', 'dissagregationsAssignationToIndicator', 'unit','instructions'];
        this._selectedColumns = this.cols.filter(value => !hiddenColumns.includes(value.field));

        this.formItem = this.fb.group({
            id: new FormControl(''),
            code: new FormControl('', [Validators.required, Validators.maxLength(10)]),
            description: new FormControl('', [Validators.required, Validators.maxLength(255)]),
            category: new FormControl('', [Validators.maxLength(255)]),
            instructions: new FormControl('', [Validators.maxLength(1000)]),
            qualitativeInstructions: new FormControl('', [Validators.maxLength(1000)]),
            state: new FormControl('', Validators.required),
            indicatorType: new FormControl('', Validators.required),
            measureType: new FormControl('', Validators.required),
            frecuency: new FormControl('', Validators.required),
            areaType: new FormControl('', Validators.required),
            isMonitored: new FormControl('', Validators.required),
            isCalculated: new FormControl('', Validators.required),
            totalIndicatorCalculationType: new FormControl('', Validators.required),
            compassIndicator: new FormControl('', Validators.required),
            markers: new FormControl(''),
            statement: new FormControl(''),
            dissagregations: new FormControl('', Validators.required),
            dissagregationsAssignationToIndicator: new FormControl(''),
            customDissagregations: new FormControl(''),
            customDissagregationAssignationToIndicators: new FormControl(''),
            blockAfterUpdate: new FormControl(''),
            unit: new FormControl(''),
        });


        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
        this.enumsService.getByType(EnumsType.IndicatorType).subscribe(value => {
            this.indicatorTypes = value;
        });
        this.enumsService.getByType(EnumsType.MeasureType).subscribe(value => {
            this.measureTypes = value;
        });
        this.enumsService.getByType(EnumsType.Frecuency).subscribe(value => {
            this.frecuencies = value;
        });
        this.enumsService.getByType(EnumsType.AreaType).subscribe(value => {
            this.areaTypes = value;
        });
        this.enumsService.getByType(EnumsType.UnitType).subscribe(value => {
            this.unitTypes = value;
        });
        this.enumsService.getByType(EnumsType.TotalIndicatorCalculationType).subscribe(value => {
            this.totalIndicatorCalculationTypes = value;
        });
        this.enumsService.getByType(EnumsType.DissagregationType).subscribe(value => {
            this.dissagregationTypes = value;
        });
        this.customDissagregationService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    this.customDissagregations = value;
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los estados',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
        this.isCalculatedOptions = [{label: 'Calculado', value: true}, {label: 'No Calculado', value: false}];
        this.isMonitoredOptions = [{label: 'Monitoreado', value: true}, {label: 'No Monitoreado', value: false}];
        this.periodsSelectItems = [];
        this.periodService.getAll().subscribe(value => {
            this.periods = value.sort((a, b) => a.year - b.year);
            this.periods.forEach(value1 => {
                const selectPeriodItem: SelectItem = {label: value1.year + '', value: value1};
                this.periodsSelectItems.push(selectPeriodItem);
            });

        });
        this.importForm = this.fb.group({
            period: new FormControl('', [Validators.required]),
            fileName: new FormControl('', [Validators.required]),
            file: new FormControl(''),
        });
    }


    public getDissagregationTypeByPeriod(period: Period): SelectItem[] {
        const r: SelectItem[] = [];
        this.dissagregationTypes.forEach(value => {
            const selectItem: SelectItem = {
                label: value.label,
                value: value.value + '-' + period.id
            };
            r.push(selectItem);
        });
        return r.sort((a, b) => {
            return a.label < b.label ? -1 : 1;
        });
    }

    public getSelectedDissagregationTypeByPeriod(period: Period): string[] {
        if (!this.formItem.controls['dissagregations'].value) {
            return [];
        }
        return (this.formItem.controls['dissagregations'].value as string[]).filter(value => {
            return value.split('-')[1] === period.id.toString();
        }).map(value => {
            return value.split('-')[0];
        });
    }

    public getCustomDissagregationTypeByPeriod(period: Period): SelectItem[] {
        const r: SelectItem[] = [];
        this.customDissagregations.forEach(value => {
            const selectItem: SelectItem = {
                label: value.description,
                value: value.id + '-' + period.id
            };
            r.push(selectItem);
        });
        return r;
    }

    private loadItems() {
        this.indicatorService.getAll().subscribe({
            next: value => {
                this.items = value;
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar las situaciones',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    private loadStatements() {
        this.statementService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    this.statements = value;
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los statements',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'indicadores');
    }


    createItem() {

        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.filteredStatements = [];
        const newItem = new Indicator();
        this.formItem.patchValue(newItem);
        this.setDefaultIndicatorValues();
        this.formItem.get('statement').enable();
        this.formItem.get('dissagregations').patchValue([]);
        this.formItem.get('customDissagregations').patchValue([]);
        this.loadMarkers([]);
        this.ref.detectChanges();
    }

    editItem(indicator: Indicator) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(indicator);
        this.filterStatementsByAreaType(indicator.areaType as AreaType, false);
        const dissagregationsSelectItems = indicator.dissagregationsAssignationToIndicator
            .filter(value => {
                return value.state === EnumsState.ACTIVE;
            }).map(value => {
                return value.dissagregationType + '-' + value.period.id;
            });
        this.formItem.get('dissagregations').patchValue(dissagregationsSelectItems);


        const customDissagregations: string[] = indicator.customDissagregationAssignationToIndicators
            .filter(value => {
                return value.state === EnumsState.ACTIVE;
            }).map(value => {
                return value.customDissagregation.id +
                    '-' + value.period.id;
            });
        this.formItem.get('customDissagregations').patchValue(customDissagregations);
        this.loadMarkers(indicator.markers);
        this.ref.detectChanges();
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            code,
            description,
            category,
            instructions,
            qualitativeInstructions,
            state,
            indicatorType,
            measureType,
            frecuency,
            areaType,
            isMonitored,
            isCalculated,
            totalIndicatorCalculationType,
            compassIndicator,
            markers,
            statement,
            dissagregations,
            dissagregationsAssignationToIndicator,
            customDissagregations,
            customDissagregationAssignationToIndicators,
            blockAfterUpdate,
            unit
        }
            = this.formItem.value;
        const indicator: Indicator = {
            id,
            code,
            description,
            category,
            instructions,
            qualitativeInstructions,
            state,
            indicatorType,
            measureType,
            frecuency,
            areaType,
            isMonitored,
            isCalculated,
            totalIndicatorCalculationType,
            compassIndicator,
            markers,
            statement,
            unit,
            dissagregationsAssignationToIndicator,
            customDissagregationAssignationToIndicators,
            blockAfterUpdate
        };
        // process assignation dissagregations
// desagregaciones
        const dissagregationsObjs = (dissagregations as Array<string>).map(value => {
            return this.dissagregationsItemsValuesToObject(value);
        });
        const customDissagregationsObjs = (customDissagregations as Array<string>).map(value => {
            return this.customDissagregationsItemsValuesToObject(value);
        });

        if (indicator.id) {
            // veo lo q ya no estan
            for (const dissagregationsAssignationToIndicatorElement of indicator.dissagregationsAssignationToIndicator) {
                if ((dissagregationsObjs as any[]).filter(value => {
                    return value.dissagregationType === dissagregationsAssignationToIndicatorElement.dissagregationType
                        && value.period.id === dissagregationsAssignationToIndicatorElement.period.id;
                }).length < 1) {
                    dissagregationsAssignationToIndicatorElement.state = EnumsState.INACTIVE;
                } else {
                    dissagregationsAssignationToIndicatorElement.state = EnumsState.ACTIVE;
                }
            }
            for (const dissagregation of dissagregationsObjs) {
                if (indicator.dissagregationsAssignationToIndicator.filter(value => {
                    return value.dissagregationType === dissagregation.dissagregationType
                        && value.period.id === dissagregation.period.id;
                }).length < 1) {
                    const a = new DissagregationAssignationToIndicator();
                    a.dissagregationType = dissagregation.dissagregationType;
                    a.period = dissagregation.period;
                    indicator.dissagregationsAssignationToIndicator.push(a);
                }
            }
            // veo los que ya no están y desactivo
            for (const customDissagregationAssignationToIndicatorElement of indicator.customDissagregationAssignationToIndicators) {
                if ((customDissagregationsObjs as any[]).filter(value => {
                    return value.customDissagregationType.id === customDissagregationAssignationToIndicatorElement.customDissagregation.id
                        && customDissagregationAssignationToIndicatorElement.period.id === value.period.id;
                }).length < 1) {
                    customDissagregationAssignationToIndicatorElement.state = EnumsState.INACTIVE;
                } else {
                    customDissagregationAssignationToIndicatorElement.state = EnumsState.ACTIVE;
                }
            }
            for (const customDissagregation of (customDissagregationsObjs as any[])) {
                if (indicator.customDissagregationAssignationToIndicators.filter(value => {
                    return value.customDissagregation.id === customDissagregation.customDissagregationType.id
                        && value.period.id === customDissagregation.period.id;
                }).length < 1) {
                    const a = new CustomDissagregationAssignationToIndicator();
                    a.customDissagregation = customDissagregation.customDissagregationType as CustomDissagregation;
                    a.period = customDissagregation.period;
                    indicator.customDissagregationAssignationToIndicators.push(a);
                }
            }


        } else {
            indicator.dissagregationsAssignationToIndicator = [];
            for (const dissagregation of dissagregationsObjs) {
                const da = new DissagregationAssignationToIndicator();
                da.dissagregationType = dissagregation.dissagregationType;
                da.period = dissagregation.period;
                da.state = EnumsState.ACTIVE;
                indicator.dissagregationsAssignationToIndicator.push(da);
            }
            indicator.customDissagregationAssignationToIndicators = [];
            for (const customDissagregation of customDissagregationsObjs) {
                const da = new CustomDissagregationAssignationToIndicator();
                da.customDissagregation = customDissagregation.customDissagregationType;
                da.period = customDissagregation.period;
                da.state = EnumsState.ACTIVE;
                indicator.customDissagregationAssignationToIndicators.push(da);

            }
        }
        if (indicator.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.indicatorService.update(indicator)
                .subscribe({
                    next: () => {
                        this.cancelDialog();
                        this.loadItems();
                    },
                    error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al actualizar la situación',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
                });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.indicatorService.save(indicator)
                .subscribe({
                    next: () => {
                        this.cancelDialog();
                        this.loadItems();
                    },
                    error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al guardar el indicador',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
                });
        }
        this.ref.detectChanges();
    }

    dissagregationsItemsValuesToObject(valueS: string): { dissagregationType: string, period: Period } {
        const valuesSeparated: string[] = valueS.split('-');
        const dissagregationType = valuesSeparated[0];
        const periodId: number = Number(valuesSeparated[1]);
        const period = this.periods.filter(value => {
            return value.id === periodId;
        }).pop();
        return {dissagregationType, period};
    }

    customDissagregationsItemsValuesToObject(valueS: string): { customDissagregationType: CustomDissagregation, period: Period } {
        const valuesSeparated: string[] = valueS.split('-');
        const customDissagregationTypeId = Number(valuesSeparated[0]);
        const customDissagregationType =
            this.customDissagregations.filter(value => {
                return value.id === customDissagregationTypeId;
            })[0];
        const periodId: number = Number(valuesSeparated[1]);
        const period = this.periods.filter(value => {
            return value.id === periodId;
        }).pop();
        return {customDissagregationType, period};
    }

    cancelDialog() {
        this.showDialog = false;
        this.submitted = false;
    }

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }

    // noinspection DuplicatedCode
    private loadMarkers(markersToRemove: Marker[]) {
        this.markerService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    if (markersToRemove && markersToRemove.length > 0) {
                        this.markers = value.filter(value1 => {
                            for (const marker of markersToRemove) {
                                if (marker.id === value1.id) {
                                    return false;
                                }
                            }
                            return true;
                        });
                    } else {
                        this.markers = value;
                    }
                },
                error: err => {

                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los marcadores',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
    }

    private registerFilters() {
        this.filterService.register('customDissagregationsAssignationToIndicatorFilter', (value, filter): boolean => {
            return this.filterUtilsService.customDissagregationsAssignationToIndicatorFilter(value, filter);
        });
        this.filterService.register('dissagregationsAssignationToIndicatorFilter', (value, filter): boolean => {
            return this.filterUtilsService.dissagregationsAssignationToIndicatorFilter(value, filter);
        });
        this.filterService.register('statementFilter', (value, filter): boolean => {
            return this.filterUtilsService.statementFilter(value, filter);
        });
        this.filterService.register('markersFilter', (value, filter): boolean => {
            return this.filterUtilsService.markersFilter(value, filter);
        });
        this.filterService.register('periodIndicatorFilter', (value, filter): boolean => {
            return this.filterUtilsService.periodIndicatorFilter(value, filter);
        });
    }

    onChangeArea($event: any) {
        let areaType;
        if (typeof $event === 'string') {
            areaType = $event;
        } else {
            areaType = $event.value;
        }
        this.filterStatementsByAreaType(areaType, true);
        this.ref.detectChanges();
    }

    filterStatementsByAreaType(areaType: AreaType, clearStatements: boolean) {
        this.filteredStatements = this.statements.filter(value => {
            return value.areaType === areaType;
        }).sort((a, b) => a.code.localeCompare(b.code))
            .map(value => {
                return {
                    labelItem: value.code + '-' + value.description +
                        "("+(value.periodStatementAsignations.map(value1 => {return value1.period.year}).join("-"))+")",
                    valueItem: value
                };
            });
        this.formItem.get('statement').enable();
        if (clearStatements) {
            this.formItem.get('statement').patchValue([]);
        }
    }


    getDissagregationLabel(dissa: string | SelectItem): string {
        if (typeof dissa === 'string' || dissa instanceof String) {
            return this.dissagregationTypes.filter(value => {
                return value.value === dissa.split('-')[0];
            }).map(value => value.label).pop();
        } else {
            return dissa.label;
        }
    }

    setDefaultIndicatorValues() {
        const areaType = this.areaTypes.filter(value => value.value === 'PRODUCTO').pop().value;
        this.formItem.get('areaType').patchValue(
            areaType
        );
        this.onChangeArea(areaType);
        const indicatorType = this.indicatorTypes.filter(value => value.value === 'OPERACION').pop().value;
        this.formItem.get('indicatorType').patchValue(
            indicatorType
        );

        const measureType = this.measureTypes.filter(value => value.value === 'NUMERO').pop().value;
        this.formItem.get('measureType').patchValue(
            measureType
        );
        const totalIndicatorCalculationType = this.totalIndicatorCalculationTypes.filter(value => value.value === 'SUMA').pop().value;
        this.formItem.get('totalIndicatorCalculationType').patchValue(
            totalIndicatorCalculationType
        );
        const frecuencyType = this.frecuencies.filter(value => value.value === 'MENSUAL').pop().value;
        this.formItem.get('frecuency').patchValue(
            frecuencyType
        );
        const isCompass = false;
        this.formItem.get('compassIndicator').patchValue(
            isCompass
        );
        const isMonitored = false;
        this.formItem.get('isMonitored').patchValue(
            isMonitored
        );
        const isCalculated = false;
        this.formItem.get('isCalculated').patchValue(
            isCalculated
        );
        this.ref.detectChanges();
    }

    initiateCatalogImport() {
        this.importForm.reset();
        this.showDialogImport = true;
    }


    cancelImportDialog() {
        this.showDialogImport = false;
        this.importForm.reset();
    }

    importCatalog() {
        const {
            period,
            fileName,
            file
        } = this.importForm.value;
        const importFile: ImportFile = {
            period,
            fileName,
            file
        };

        this.indicatorService.importCatalog(importFile).subscribe({
            next: () => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Catálogo cargado correctamente',
                    life: 3000
                });
                this.loadItems();
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
        if (!this.importForm.get('period').value || !this.importForm.get('period').value.id) {
            this.messageService.add({
                severity: 'error',
                summary: 'Seleccione un periodo',
                life: 3000
            });
        }
        const periodId:number = this.importForm.get('period').value.id;
        this.indicatorService.getImportTemplate(periodId).subscribe({
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
}
