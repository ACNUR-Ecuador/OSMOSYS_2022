import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {ConfirmationService} from 'primeng/api';
import {
    AreaType,
    CoreIndicator,
    CustomDissagregation,
    CustomDissagregationAssignationToIndicator,
    DissagregationAssignationToIndicator,
    ImportFile,
    Indicator,
    Period,
    Statement,
    AsyncResponse
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
import { User } from 'src/app/shared/model/User';
import { UserService } from 'src/app/services/user.service';


@Component({
    selector: 'app-performance-indicator-administration',
    templateUrl: './performance-indicator-administration.component.html',
    styleUrls: ['./performance-indicator-administration.component.scss']
})
export class PerformanceIndicatorAdministrationComponent implements OnInit {

    items: Indicator[];
    coreIndicators: SelectItem[];
    public focalPoints: User[];
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
    quarterReportCalcTypes: SelectItem[];

    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    showDialogImport = false;
    importForm: FormGroup;
    isCoreIndicator: boolean = false;
    toggleQuarterReportCalc=false;
    toggleAggRuleComment=false;


    constructor(
        private confirmationService: ConfirmationService,
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
        private ref: ChangeDetectorRef,
        public userService: UserService,

    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.loadStatements();
        this.registerFilters();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'periods', header: 'Años', type: ColumnDataType.text},
            {field: 'code', header: 'Código', type: ColumnDataType.text},
            {field: 'regionalCode', header: 'Código Regional', type: ColumnDataType.text},
            {field: 'coreIndicator', header: 'Core Indicator', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            {field: 'category', header: 'Categoría', type: ColumnDataType.text},
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
            {field: 'statement', header: 'Enunciado', type: ColumnDataType.text, pipeRef: this.codeDescriptionPipe},
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
            },
            {
                field: 'compassIndicator',
                header: 'Indicador Compass',
                type: ColumnDataType.boolean,
                pipeRef: this.booleanYesNoPipe
            },
            {   field: 'resultManager.name',
                header: 'Mánager de Resultado',
                type: ColumnDataType.text 
            },
            {
                field: 'state', header: 'Estado', type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.IndicatorType
            },
        ];

        const hiddenColumns: string[] = ['id', 'indicatorType', 'areaType',  'measureType', 'isMonitored', 'totalIndicatorCalculationType', 'isCalculated', 'frecuency', 'category','guideDirectImplementation', 'customDissagregationAssignationToIndicators', 'dissagregationsAssignationToIndicator', 'unit', 'instructions', 'compassIndicator','resultManager.name'];
        this._selectedColumns = this.cols.filter(value => !hiddenColumns.includes(value.field));

        this.formItem = this.fb.group({
            id: new FormControl(''),
            code: new FormControl('', [Validators.required, Validators.maxLength(25)]),
            regionalCode: new FormControl(''),
            coreIndicator: new FormControl(''),
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
            resultManager: new FormControl(''),
            unit: new FormControl(''),
            quarterReportCalculation: new FormControl(''),
            aggregationRuleComment: new FormControl(''),
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
        this.enumsService.getByType(EnumsType.QuarterReportCalculation).subscribe(value => {
                this.quarterReportCalcTypes = value;
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

        this.formItem.get('unit').valueChanges.subscribe(value => {
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

        this.indicatorService.getCoreIndicators().subscribe({
            next: value => {
                this.coreIndicators = value.map(value1 => {
                    const selectItem: SelectItem = {
                        label: this.codeDescriptionPipe.transform(value1),
                        value: value1
                    };
                    return selectItem;
                });;
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar las CoreIndicators',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });

        this.userService.getActiveUNHCRUsers()
            .subscribe({
                next: value => {
                    this.focalPoints = value;
                    this.ref.detectChanges();
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los responsables del proyecto',
                        detail: error.error.message,
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
        console.log(indicator)
        this.onResultManagerSelect(indicator?.resultManager)
        this.onQuarterReportCalcChange(indicator?.quarterReportCalculation)
        this.onIsCoreIndicatorChange(indicator.coreIndicator)
        this.isCoreIndicator=indicator.coreIndicator
        this.setPeriodForStatment(indicator.statement);
        this.utilsService.resetForm(this.formItem);
        this.showDialog = true;
        this.formItem.patchValue(indicator);
        this.formItem.get('dissagregationAssignationToIndicators').patchValue(indicator.dissagregationsAssignationToIndicator);
        this.formItem.get('customDissagregationAssignationToIndicators').patchValue(indicator.customDissagregationAssignationToIndicators);
        const state:boolean=indicator.state === EnumsState.ACTIVE;
        this.formItem.get('state').patchValue(state)

        if(indicator.resultManager?.id){
            const resultManager = this.focalPoints.find(fp => fp.id === indicator.resultManager.id);
            this.formItem.get('resultManager').patchValue(resultManager);

        }
        this.filterStatementsByAreaType(indicator.areaType as AreaType, false);
        this.ref.detectChanges();
        console.log(this.formItem)
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            code,
            regionalCode,
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
            coreIndicator,
            statement,
            dissagregationAssignationToIndicators,
            customDissagregationAssignationToIndicators,
            blockAfterUpdate,
            unit,
            resultManager,
            quarterReportCalculation,
            aggregationRuleComment
        }
            = this.formItem.value;
        const indicator: Indicator = {
            id,
            code,
            regionalCode,
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
            coreIndicator,
            statement,
            unit,
            dissagregationsAssignationToIndicator: dissagregationAssignationToIndicators,
            customDissagregationAssignationToIndicators: customDissagregationAssignationToIndicators,
            blockAfterUpdate,
            resultManager,
            quarterReportCalculation,
            aggregationRuleComment
        };
        if (indicator.state) {
            indicator.state = 'ACTIVO';
        } else {
            indicator.state = 'INACTIVO';
        }
        if(indicator.coreIndicator){
            indicator.regionalCode=this.formItem.get("regionalCode").value
            indicator.frecuency=this.formItem.get("frecuency").value
            indicator.measureType=this.formItem.get("measureType").value
            indicator.description=this.formItem.get("description").value
            indicator.instructions=this.formItem.get("instructions").value
            indicator.qualitativeInstructions=this.formItem.get("qualitativeInstructions").value
        }else{
            indicator.coreIndicator=false;
        }
        // process assignation dissagregations


        if (indicator.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.indicatorService.update(indicator)
                .subscribe({
                    next: (response: AsyncResponse | number ) => {
                        if (typeof response === 'object' && response.progress !== undefined && response.progress < 100) {
                            return;
                        }
                        this.cancelDialog();
                        this.loadItems();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Indicador guardado exitosamente',
                            closable: true  // Asegúrate de que el mensaje tenga un botón para cerrarlo
                        });
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
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Indicador guardado exitosamente',
                            life: 3000
                        });
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

    confirmBeforeSave() {
        if (!this.hasPopulationTypeDissagregation() && this.formItem.get('unit').value === "PERSONAS_INTERES") {
            this.confirmationService.confirm({
                message: 'Se seleccionó "Personas Desplazadas y Apátridas" como tipo de medida, pero no se encontraron desgregaciones con Tipo de Población para todos los años. ¿Desea continuar de todos modos?',
                header: 'Confirmación Requerida',
                icon: 'pi pi-exclamation-triangle',
                acceptLabel: "Si",
                acceptButtonStyleClass: "warn",
                accept: () => {
                    this.saveItem();
                },
                reject: () => {
                    this.messageService.add({
                        severity: 'warn',
                        summary: 'No se guardaron los cambios',
                        detail: 'Seleccione alguna desagregación con Tipo de Población en todos los s.',
                        life: 4000
                    });
                }
            });
        } else {
            this.saveItem();
        }
    }

    hasPopulationTypeDissagregation() {
        let dissagregationsAssignationToIndicators = this.formItem.get('dissagregationAssignationToIndicators').value
        const hasPopulationTypeDissagregation = dissagregationsAssignationToIndicators?.some(
            (item) => item.dissagregationType.includes('TIPO_POBLACION') && item.state === 'ACTIVO'
        );
        return hasPopulationTypeDissagregation;
    }
    cancelDialog() {
        this.onResultManagerClear()
        this.showDialog = false;
        this.onIsCoreIndicatorChange(false)
        this.formItem.get('regionalCode').patchValue(null);
        this.formItem.get('description').patchValue(null);
        this.formItem.get('frecuency').patchValue(null);
        this.formItem.get('measureType').patchValue(null)
        this.setDefaultIndicatorValues();
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
        this.filterService.register('customFilterPeriod', (value, filter): boolean => {
            return this.filterUtilsService.customFilterPeriod(value, filter);
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
                label: 'Selecciona un Enunciado.... ',
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
                summary: 'Seleccione un año',
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

    setCoreIndicator(value:any) {
        const coreInd:CoreIndicator=value.value
        if(coreInd){
            this.formItem.get('regionalCode').patchValue(coreInd.code);
            this.formItem.get('description').patchValue(coreInd.description);
            this.formItem.get('frecuency').patchValue(coreInd.frecuency);
            this.formItem.get('measureType').patchValue(coreInd.measureType);
            this.formItem.get('instructions').patchValue(null);
            this.formItem.get('qualitativeInstructions').patchValue(null);

        }
    }


    isEditing() {
        let isEditing = this.formItem.get('id')?.value;
        if(isEditing == null)
        {
            return true;
        }
        else{
            return false;
        }
    }

    onIsCoreIndicatorChange(isCoreIndicator: boolean) {
        this.isCoreIndicator=isCoreIndicator
        if(!isCoreIndicator){
            this.formItem.get('regionalCode').patchValue(null);
            this.formItem.get('regionalCode').enable();
            this.formItem.get('frecuency').patchValue(null);
            this.formItem.get('frecuency').enable();
            this.formItem.get('description').patchValue(null);
            this.formItem.get('description').enable();
            this.formItem.get('instructions').patchValue(null);
            this.formItem.get('instructions').enable();
            this.formItem.get('qualitativeInstructions').patchValue(null);
            this.formItem.get('qualitativeInstructions').enable();
            this.formItem.get('measureType').patchValue(null);
            this.formItem.get('measureType').enable();

        }else{
            this.formItem.get('regionalCode').disable();
            this.formItem.get('frecuency').disable();
            this.formItem.get('description').disable();
            this.formItem.get('instructions').disable();
            this.formItem.get('qualitativeInstructions').disable();
            this.formItem.get('measureType').disable();

        }
       
    }

    onResultManagerSelect(rm:User){
        if(rm){
            this.toggleQuarterReportCalc=true;
            this.formItem.get('quarterReportCalculation').setValidators([Validators.required]);
            this.formItem.get('quarterReportCalculation').updateValueAndValidity();
        }
    }
    onResultManagerClear(){
        this.toggleQuarterReportCalc=false;
        this.toggleAggRuleComment=false;
        this.formItem.get('quarterReportCalculation').patchValue(null)
        this.formItem.get('aggregationRuleComment').patchValue(null)
        this.formItem.get('quarterReportCalculation').clearValidators()
        this.formItem.get('quarterReportCalculation').updateValueAndValidity();
        this.formItem.get('aggregationRuleComment').clearValidators();
        this.formItem.get('aggregationRuleComment').updateValueAndValidity();
    }
    onQuarterReportCalcChange(quarterReportCalc:string){
        if(quarterReportCalc === "AGGREGATION_RULE"){
            this.toggleAggRuleComment=true;
            this.formItem.get('aggregationRuleComment').setValidators([Validators.required]);
        }else{
            this.toggleAggRuleComment=false;
            this.formItem.get('aggregationRuleComment').patchValue(null)
            this.formItem.get('aggregationRuleComment').clearValidators();

        }
        this.formItem.get('aggregationRuleComment').updateValueAndValidity();

    }
    
}
