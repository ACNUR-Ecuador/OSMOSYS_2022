import {Component, Input, OnInit} from '@angular/core';
import {
    CustomDissagregation,
    CustomDissagregationAssignationToIndicator,
    DissagregationAssignationToIndicator,
    Indicator,
    Marker
} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, FilterService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {IndicatorService} from '../../shared/services/indicator.service';
import {StatementService} from '../../shared/services/statement.service';
import {CodeShortDescriptionPipe} from '../../shared/pipes/code-short-description.pipe';
import {CustomDissagregationService} from '../../shared/services/custom-dissagregation.service';
import {MarkerService} from '../../shared/services/marker.service';
import {EnumValuesToLabelPipe} from '../../shared/pipes/enum-values-to-label.pipe';
import {BooleanYesNoPipe} from '../../shared/pipes/boolean-yes-no.pipe';
import {MarkersListPipe} from '../../shared/pipes/markers-list.pipe';
import {DissagregationsAssignationToIndicatorPipe} from '../../shared/pipes/dissagregations-assignation-to-indicator.pipe';
import {CustomDissagregationsAssignationToIndicatorPipe} from '../../shared/pipes/custom-dissagregations-assignation-to-indicator.pipe';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';

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
    private states: SelectItem[];
    private indicatorTypes: SelectItem[];
    private measureTypes: SelectItem[];
    private frecuencies: SelectItem[];
    private areaTypes: SelectItem[];
    private totalIndicatorCalculationTypes: SelectItem[];
    private statements: SelectItem[];
    private customDissagregations: CustomDissagregation[];
    private dissagregationTypes: SelectItem[];
    markers: Marker[];
    private isMonitoredOptions: any[];
    private isCalculatedOptions: any[];

    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];

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
        private markersListPipe: MarkersListPipe,
        private dissagregationsAssignationToIndicatorPipe: DissagregationsAssignationToIndicatorPipe,
        private customDissagregationsAssignationToIndicatorPipe: CustomDissagregationsAssignationToIndicatorPipe,
        private markerService: MarkerService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.loadStatements();
        this.registerFilters();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'code', header: 'Código', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            /*{field: 'guidePartners', header: 'Guía para socios', type: ColumnDataType.text},
            {field: 'guideDirectImplementation', header: 'Guía para Implementación Directa', type: ColumnDataType.text},*/
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
            {field: 'compassIndicator', header: 'Indicador Compass', type: ColumnDataType.boolean, pipeRef: this.booleanYesNoPipe},
            {field: 'isMonitored', header: 'Monitoreado', type: ColumnDataType.boolean, pipeRef: this.booleanYesNoPipe},
            {field: 'isCalculated', header: 'Calculado', type: ColumnDataType.boolean, pipeRef: this.booleanYesNoPipe},
            {
                field: 'totalIndicatorCalculationType',
                header: 'Tipo de Cálculo Total',
                type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.TotalIndicatorCalculationType
            },
            {field: 'markers', header: 'Marcadores', type: ColumnDataType.text, pipeRef: this.markersListPipe},
            {field: 'statements', header: 'Staments', type: ColumnDataType.text, pipeRef: this.markersListPipe},
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
            }
        ];

        const hiddenColumns: string[] = ['id', 'guidePartners', 'guideDirectImplementation', 'markers', 'customDissagregationAssignationToIndicators', 'dissagregationsAssignationToIndicator'];
        this._selectedColumns = this.cols.filter(value => !hiddenColumns.includes(value.field));

        this.formItem = this.fb.group({
            id: new FormControl(''),
            code: new FormControl('', [Validators.required, Validators.maxLength(10)]),
            description: new FormControl('', [Validators.required, Validators.maxLength(255)]),
            guidePartners: new FormControl(''),
            guideDirectImplementation: new FormControl('', Validators.required),
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
            statements: new FormControl(''),
            dissagregations: new FormControl('', Validators.required),
            dissagregationsAssignationToIndicator: new FormControl(''),
            customDissagregations: new FormControl(''),
            customDissagregationAssignationToIndicators: new FormControl('')
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
        this.enumsService.getByType(EnumsType.TotalIndicatorCalculationType).subscribe(value => {
            this.totalIndicatorCalculationTypes = value;
        });
        this.enumsService.getByType(EnumsType.DissagregationType).subscribe(value => {
            this.dissagregationTypes = value;
        });
        this.customDissagregationService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.customDissagregations = value;
        }, error => {
            console.log(error);
        });
        this.isCalculatedOptions = [{label: 'Calculado', value: true}, {label: 'No Calculado', value: false}];
        this.isMonitoredOptions = [{label: 'Monitoreado', value: true}, {label: 'No Monitoreado', value: false}];
    }

    private loadItems() {
        this.indicatorService.getAll().subscribe(value => {
            this.items = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las situaciones',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    private loadStatements() {
        this.statementService.getByState(EnumsState.ACTIVE).subscribe(statements => {
            this.statements = statements.map(value => {
                return {
                    label: value.code + ' - ' + value.description,
                    value
                };
            });

        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los statements',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    exportExcel() {
        import('xlsx').then(xlsx => {
            const itemsRenamed = this.utilsService.renameKeys(this.items, this.cols);
            const worksheet = xlsx.utils.json_to_sheet(itemsRenamed);
            const workbook = {Sheets: {data: worksheet}, SheetNames: ['data']};
            const excelBuffer: any = xlsx.write(workbook, {bookType: 'xlsx', type: 'array'});
            this.utilsService.saveAsExcelFile(excelBuffer, 'situaciones');
        });
    }


    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Indicator();
        this.formItem.patchValue(newItem);
        this.formItem.get('dissagregations').patchValue([]);
        this.formItem.get('customDissagregations').patchValue([]);
        this.loadMarkers([]);
    }

    editItem(indicator: Indicator) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(indicator);
        const dissagregations: string[] = indicator.dissagregationsAssignationToIndicator
            .filter(value => {
                return value.state === EnumsState.ACTIVE;
            }).map(value => {
                return value.dissagregationType;
            });
        this.formItem.get('dissagregations').patchValue(dissagregations);


        const customDissagregations: number[] = indicator.customDissagregationAssignationToIndicators
            .filter(value => {
                return value.state === EnumsState.ACTIVE;
            }).map(value => {
                return value.customDissagregation;
            }).map(value => {
                return value.id;
            });
        this.formItem.get('customDissagregations').patchValue(customDissagregations);
        this.loadMarkers(indicator.markers);
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            code,
            description,
            guidePartners,
            guideDirectImplementation,
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
            statements,
            dissagregations,
            dissagregationsAssignationToIndicator,
            customDissagregations,
            customDissagregationAssignationToIndicators
        }
            = this.formItem.value;

        const productCode = null;
        const indicator: Indicator = {
            id,
            code,
            productCode,
            description,
            guidePartners,
            guideDirectImplementation,
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
            statements,
            dissagregationsAssignationToIndicator,
            customDissagregationAssignationToIndicators
        };
        // process assignation dissagregations
        if (indicator.id) {
            // desagregaciones
            // veo lo q ya no estan
            for (const dissagregationsAssignationToIndicatorElement of indicator.dissagregationsAssignationToIndicator) {
                if ((dissagregations as string[]).filter(value => {
                    return value === dissagregationsAssignationToIndicatorElement.dissagregationType;
                }).length < 1) {
                    dissagregationsAssignationToIndicatorElement.state = EnumsState.INACTIVE;
                } else {
                    dissagregationsAssignationToIndicatorElement.state = EnumsState.ACTIVE;
                }
            }
            for (const dissagregation of dissagregations) {
                if (indicator.dissagregationsAssignationToIndicator.filter(value => {
                    return value.dissagregationType === dissagregation;
                }).length < 1) {
                    const a = new DissagregationAssignationToIndicator();
                    a.dissagregationType = dissagregation;
                    indicator.dissagregationsAssignationToIndicator.push(a);
                }
            }
            for (const customDissagregationAssignationToIndicator of indicator.customDissagregationAssignationToIndicators) {
                if ((customDissagregations as number[]).filter(value => {
                    return value === customDissagregationAssignationToIndicator.customDissagregation.id;
                }).length < 1) {
                    customDissagregationAssignationToIndicator.state = EnumsState.INACTIVE;
                } else {
                    customDissagregationAssignationToIndicator.state = EnumsState.ACTIVE;
                }
            }
            for (const customDissagregation of (customDissagregations as number[])) {
                if (indicator.customDissagregationAssignationToIndicators.filter(value => {
                    return value.customDissagregation.id === customDissagregation;
                }).length < 1) {
                    const a = new CustomDissagregationAssignationToIndicator();
                    a.customDissagregation = this.customDissagregations.filter(value => value.id === customDissagregation).pop();
                    indicator.customDissagregationAssignationToIndicators.push(a);
                }
            }


        } else {
            indicator.dissagregationsAssignationToIndicator = [];
            for (const dissagregation of dissagregations) {
                const da = new DissagregationAssignationToIndicator();
                da.dissagregationType = dissagregation;
                indicator.dissagregationsAssignationToIndicator.push(da);

            }
            indicator.customDissagregationAssignationToIndicators = [];
            for (const customDissagregation of customDissagregations) {
                const da = new CustomDissagregationAssignationToIndicator();
                da.customDissagregation = customDissagregation;
                indicator.customDissagregationAssignationToIndicators.push(da);

            }
        }

        if (indicator.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.indicatorService.update(indicator).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al actualizar la situación',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.indicatorService.save(indicator).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al guardar el indicador',
                    detail: error.error.message,
                    life: 3000
                });
            });
        }
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

    private loadMarkers(markersToRemove: Marker[]) {
        this.markerService.getByState(EnumsState.ACTIVE).subscribe(value => {

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
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los marcadores',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    private registerFilters() {
        this.filterService.register('customDissagregationsAssignationToIndicatorFilter', (value, filter): boolean => {
            return this.filterUtilsService.customDissagregationsAssignationToIndicatorFilter(value, filter);
        });
        this.filterService.register('dissagregationsAssignationToIndicatorFilter', (value, filter): boolean => {
            return this.filterUtilsService.dissagregationsAssignationToIndicatorFilter(value, filter);
        });
        this.filterService.register('markersFilter', (value, filter): boolean => {
            return this.filterUtilsService.markersFilter(value, filter);
        });
    }
}
