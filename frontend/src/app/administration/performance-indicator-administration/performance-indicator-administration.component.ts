import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {
    AreaType,
    CustomDissagregation,
    CustomDissagregationAssignationToIndicator,
    DissagregationAssignationToIndicator,
    ImportFile,
    Indicator,
    Period,
    Statement
} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {FilterService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {EnumsService} from '../../services/enums.service';
import {IndicatorService} from '../../services/indicator.service';
import {StatementService} from '../../services/statement.service';
import {CustomDissagregationService} from '../../services/custom-dissagregation.service';
import {EnumValuesToLabelPipe} from '../../shared/pipes/enum-values-to-label.pipe';
import {BooleanYesNoPipe} from '../../shared/pipes/boolean-yes-no.pipe';
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
    formItem: FormGroup;
    states: SelectItem[];
    indicatorTypes: SelectItem[];
    measureTypes: SelectItem[];
    frecuencies: SelectItem[];
    areaTypes: SelectItem[];
    unitTypes: SelectItem[];
    totalIndicatorCalculationTypes: SelectItem[];
    statements: Statement[];
    filteredStatements: SelectItem<Statement>[]; /*{
        labelItem: string,
        valueItem: Statement
    }[];*/
    customDissagregations: CustomDissagregation[];
    isMonitoredOptions: any[];
    isCalculatedOptions: any[];
    periods: Period[];
    periodsForStatements: Period[];
    periodsSelectItems: SelectItem[];

    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    showDialogImport = false;
    importForm: FormGroup;

    constructor(
        private messageService: MessageService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private indicatorService: IndicatorService,
        private statementService: StatementService,
        private enumValuesToLabelPipe: EnumValuesToLabelPipe,
        private customDissagregationService: CustomDissagregationService,
        private booleanYesNoPipe: BooleanYesNoPipe,
        private periodsFromIndicatorPipe: PeriodsFromIndicatorPipe,
        private codeDescriptionPipe: CodeDescriptionPipe,
        private dissagregationsAssignationToIndicatorPipe: DissagregationsAssignationToIndicatorPipe,
        private customDissagregationsAssignationToIndicatorPipe: CustomDissagregationsAssignationToIndicatorPipe,
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

        const hiddenColumns: string[] = ['id', 'guideDirectImplementation', 'customDissagregationAssignationToIndicators', 'dissagregationsAssignationToIndicator', 'unit', 'instructions'];
        this._selectedColumns = this.cols.filter(value => !hiddenColumns.includes(value.field));

        this.formItem = this.fb.group({
            id: new FormControl(''),
            code: new FormControl('', [Validators.required, Validators.maxLength(15)]),
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
            statement: new FormControl(null,Validators.required),
            dissagregationAssignationToIndicators: new FormControl(''),
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
        this.setPeriodForStatment(null);

        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.showDialog = true;
        this.filteredStatements = [];
        const newItem = new Indicator();
        this.formItem.patchValue(newItem);
        this.setDefaultIndicatorValues();
        this.formItem.get('statement').enable();
        this.ref.detectChanges();
    }

    editItem(indicator: Indicator) {
        this.setPeriodForStatment(indicator.statement);
        this.utilsService.resetForm(this.formItem);
        this.showDialog = true;
        this.formItem.patchValue(indicator);
        this.formItem.get('dissagregationAssignationToIndicators').patchValue(indicator.dissagregationsAssignationToIndicator);
        this.formItem.get('customDissagregationAssignationToIndicators').patchValue(indicator.customDissagregationAssignationToIndicators);
        this.filterStatementsByAreaType(indicator.areaType as AreaType, false);


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
            statement,
            dissagregationAssignationToIndicators,
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
            statement,
            unit,
            dissagregationsAssignationToIndicator: dissagregationAssignationToIndicators,
            customDissagregationAssignationToIndicators: customDissagregationAssignationToIndicators,
            blockAfterUpdate
        };
        if (indicator.state) {
            indicator.state = 'ACTIVO';
        } else {
            indicator.state = 'INACTIVO';
        }
        // process assignation dissagregations


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
                            summary: 'Error al actualizar el indicador',
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


    cancelDialog() {
        this.showDialog = false;
    }

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }

    // noinspection DuplicatedCode

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
                let selectItem: SelectItem<Statement> = {
                    label: value.code + '-' + value.description +
                        "(" + (value.periodStatementAsignations.map(value1 => {
                            return value1.period.year
                        }).join("-")) + ")",
                    value: value,
                };
                return selectItem
            });
        this.filteredStatements.sort((a, b) => a.label.localeCompare(b.label));


        this.formItem.get('statement').enable();
        if (clearStatements) {
            let emptyItem: SelectItem<Statement> = {
                label: 'Selecciona una declaración.... ',
                value: null,
            };
            this.filteredStatements.push(emptyItem);
            this.formItem.get('statement').patchValue(null);
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

        this.formItem.get('dissagregationAssignationToIndicators').patchValue([]);
        this.formItem.get('customDissagregationAssignationToIndicators').patchValue([]);
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
        this.messageService.clear();
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
                    life: 30000
                });
                this.loadItems();
                this.showDialogImport = false;
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al descargar la plantilla',
                    detail: err.error.message,
                    sticky: true
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
        const periodId: number = this.importForm.get('period').value.id;
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

    public getdissagregationsAssignationToIndicator(period: Period): DissagregationAssignationToIndicator[] {
        let dissagregationAssignations: DissagregationAssignationToIndicator[] = this.formItem.get('dissagregationAssignationToIndicators').value;
        if (!dissagregationAssignations) {
            dissagregationAssignations = []
        }
        let dissagregationAssignationToIndicators: DissagregationAssignationToIndicator[] = dissagregationAssignations.filter(value => value.period.id === period.id);
        if (!dissagregationAssignationToIndicators) {
            dissagregationAssignationToIndicators = [];
        }
        return dissagregationAssignationToIndicators;
    }

    public getCustomDissagregationsAssignationToIndicator(period: Period): CustomDissagregationAssignationToIndicator[] {
        let dissagregationAssignations: CustomDissagregationAssignationToIndicator[] = this.formItem.get('customDissagregationAssignationToIndicators').value;
        if (!dissagregationAssignations) {
            dissagregationAssignations = []
        }
        let dissagregationAssignationToIndicators: CustomDissagregationAssignationToIndicator[] =
            dissagregationAssignations.filter(value => value.period.id === period.id);
        if (!dissagregationAssignationToIndicators) {
            dissagregationAssignationToIndicators = [];
        }
        return dissagregationAssignationToIndicators;
    }

    setDissagregationAssignationsToIndicator(parametersMap: Map<string, Period | DissagregationAssignationToIndicator[]>) {
        let oldDissagregationAssignationToIndicators: DissagregationAssignationToIndicator[] = this.formItem.get('dissagregationAssignationToIndicators').value;

        let period: Period = parametersMap.get('period') as Period;
        // remuevo los editados
        oldDissagregationAssignationToIndicators = oldDissagregationAssignationToIndicators
            .filter(value => value.period.id !== period.id);
        oldDissagregationAssignationToIndicators = oldDissagregationAssignationToIndicators.concat(parametersMap.get('asignations') as DissagregationAssignationToIndicator[]);
        this.formItem.get('dissagregationAssignationToIndicators').patchValue(oldDissagregationAssignationToIndicators);

    }

    setCustomDissagregationAssignationsToIndicator(parametersMap: Map<string, Period | CustomDissagregationAssignationToIndicator[]>) {
        let period: Period = parametersMap.get('period') as Period;
        let oldDissagregationAssignationToIndicators: CustomDissagregationAssignationToIndicator[] =
            this.formItem.get('customDissagregationAssignationToIndicators').value;
        // remuevo los editados
        oldDissagregationAssignationToIndicators = oldDissagregationAssignationToIndicators
            .filter(value => value.period.id !== period.id);
        oldDissagregationAssignationToIndicators = oldDissagregationAssignationToIndicators.concat(parametersMap.get('asignations') as CustomDissagregationAssignationToIndicator[]);
        this.formItem.get('customDissagregationAssignationToIndicators').patchValue(oldDissagregationAssignationToIndicators);

    }

    setPeriodForStatment(value:Statement) {
        if(!value){
            this.periodsForStatements=[];
        }else {
            this.periodsForStatements=value.periodStatementAsignations.filter(value1 => value1.state===EnumsState.ACTIVE)
                .map(value1 => value1.period)
                .sort((a, b) => a.year-b.year);
        }


    }
}
