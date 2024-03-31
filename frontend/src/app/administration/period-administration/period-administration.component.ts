import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {
    DissagregationAssignationToGeneralIndicator, GeneralIndicator,
    Period,
    StandardDissagregationOption
} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType, SelectItemWithOrder} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MessageService, SelectItem, SelectItemGroup} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {EnumsService} from '../../services/enums.service';
import {Table} from 'primeng/table';
import {PeriodService} from '../../services/period.service';
import {StandardDissagregationsService} from "../../services/standardDissagregations.service";
import {EnumValuesToLabelPipe} from "../../shared/pipes/enum-values-to-label.pipe";

@Component({
    selector: 'app-period-administration',
    templateUrl: './period-administration.component.html',
    styleUrls: ['./period-administration.component.scss']
})
export class PeriodAdministrationComponent implements OnInit {
    items: Period[];
    populationTypeOptions: SelectItemWithOrder<StandardDissagregationOption>[];
    ageOptions: SelectItemWithOrder<StandardDissagregationOption>[];
    genderOptions: SelectItemWithOrder<StandardDissagregationOption>[];
    diversityOptions: SelectItemWithOrder<StandardDissagregationOption>[];
    countryOfOriginOptions: SelectItemWithOrder<StandardDissagregationOption>[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    states: SelectItem[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    dissagregationTypes: SelectItemGroup[];
    measureTypes: SelectItem[];

    colsDissagregation: ColumnTable[];

    constructor(
        private messageService: MessageService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private periodService: PeriodService,
        private standardDissagregationsService: StandardDissagregationsService,
        private ref: ChangeDetectorRef,
        private enumValuesToLabelPipe: EnumValuesToLabelPipe,
    ) {
    }

    ngOnInit(): void {
        this.loadOptions();
        this.loadItems();
        this.loadDissagregationOptions();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'year', header: 'Año', type: ColumnDataType.numeric},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');

        this.formItem = this.fb.group({
            id: new FormControl(''),
            year: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
            hasGeneralIndicator: new FormControl('', Validators.required),
            generalIndicatorId: new FormControl(''),
            generalIndicatorDescription: new FormControl('', [Validators.maxLength(255)]),
            generalIndicatorMeasureType: new FormControl({value: 'NUMERO', disabled: true}),
            generalIndicatorState: new FormControl(''),
            generalIndicatorPeriod: new FormControl({value: '', disabled: true}),
            selectedGeneralIndicatorDissagregations: new FormControl(''),
            totalGeneralIndicatorDissagregations: new FormControl(''),
            ageOptions: new FormControl('', Validators.required),
            genderOptions: new FormControl('', Validators.required),
            populationTypeOptions: new FormControl('', Validators.required),
            diversityOptions: new FormControl('', Validators.required),
            countryOfOriginOptions: new FormControl('', Validators.required),

        });

        this.colsDissagregation = [
            {
                field: 'dissagregationType',
                header: 'Desagregación',
                type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.DissagregationType
            }];
    }

    private loadItems() {
        this.periodService.getwithGeneralIndicatorAll().subscribe({
            next: value => {
                this.items = value;
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los periodos',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    private loadDissagregationOptions() {
        this.standardDissagregationsService.getActiveAgeOptions().subscribe({
            next: value => {
                this.ageOptions = this.utilsService.standandarDissagregationOptionsToSelectItems(value);
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los tipos de edad',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });

        this.standardDissagregationsService.getActiveGenderOptions().subscribe({
            next: value => {
                this.genderOptions = this.utilsService.standandarDissagregationOptionsToSelectItems(value);
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los tipos de género',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
        this.standardDissagregationsService.getActivePopulationTypeOptions().subscribe({
            next: value => {
                this.populationTypeOptions = this.utilsService.standandarDissagregationOptionsToSelectItems(value);
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los tipos de población',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
        this.standardDissagregationsService.getActiveDiversityOptions().subscribe({
            next: value => {
                this.diversityOptions = this.utilsService.standandarDissagregationOptionsToSelectItems(value);
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los tipos de diversidad',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
        this.standardDissagregationsService.getActiveCountryOfOriginOptions().subscribe({
            next: value => {
                this.countryOfOriginOptions = this.utilsService.standandarDissagregationOptionsToSelectItems(value);
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los países de origen',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }


    private loadOptions() {
        this.enumsService.getByType(EnumsType.MeasureType).subscribe(value => {
            this.measureTypes = value;
        });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
        this.enumsService.getByType(EnumsType.DissagregationType).subscribe(value => {
            const groupedByNumber: SelectItemGroup[] = [];
            value
                .map(value1 => {
                    let objectAny: any = value1;
                    return objectAny;
                })
                .forEach(value1 => {
                    const existingGroup = groupedByNumber.find(group => group.label === value1.numberOfDissagregations + ' desagregaciones');
                    if (existingGroup) {
                        existingGroup.items.push(value1);
                    } else {
                        groupedByNumber.push({
                            label: value1.numberOfDissagregations + ' desagregaciones',
                            items: [value1]
                        });
                    }
                });
            this.dissagregationTypes = groupedByNumber;
            /*let selectedDissagregations = this.dissagregationAssignationToIndicators
                .filter(value1 => value1.state === EnumsState.ACTIVE)
                .map(value1 => value1.dissagregationType);
            this.formItem.get('selectedDissagregations').patchValue(selectedDissagregations);*/
        });
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'periodos');
    }

    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Period();
        this.formItem.patchValue(newItem);
        this.formItem.get('hasGeneralIndicator').patchValue(false);
        const indicator = new GeneralIndicator();

        // solo numeros
        indicator.measureType = 'NUMERO';
        const {
            id: generalIndicatorid,
            description: generalIndicatorDescription,
            measureType: generalIndicatorMeasureType,
            state: generalIndicatorState,
            period: generalIndicatorPeriod,
            // generalIndicatorDissagregations,
            dissagregationAssignationsToGeneralIndicator: totalGeneralIndicatorDissagregations
        } = indicator;
        this.formItem.patchValue({
            generalIndicatorid,
            generalIndicatorPeriod,
            generalIndicatorState,
            generalIndicatorDescription,
            generalIndicatorMeasureType,
            totalGeneralIndicatorDissagregations
        });
        this.formItem.get('selectedGeneralIndicatorDissagregations').patchValue([]);
    }

    editItem(period: Period) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(period);
        if (period.generalIndicator) {

            const {
                id: generalIndicatorId,
                description: generalIndicatorDescription,
                measureType: generalIndicatorMeasureType,
                state: generalIndicatorState,
                period: generalIndicatorPeriod,
                dissagregationAssignationsToGeneralIndicator: totalGeneralIndicatorDissagregations
            } = period.generalIndicator;

            if (generalIndicatorState === EnumsState.ACTIVE) {
                this.formItem.get('hasGeneralIndicator').patchValue(true);
            } else {
                this.formItem.get('hasGeneralIndicator').patchValue(false);
            }

            let selectedGeneralIndicatorDissagregations =
                totalGeneralIndicatorDissagregations.filter(value1 => value1.state === EnumsState.ACTIVE)
                    .map(value1 => value1.dissagregationType);


            this.formItem.patchValue({
                generalIndicatorId,
                generalIndicatorPeriod,
                generalIndicatorState,
                generalIndicatorDescription,
                generalIndicatorMeasureType,
                totalGeneralIndicatorDissagregations,
                selectedGeneralIndicatorDissagregations
            });
            console.log(this.formItem.value);
        }else {
            this.formItem.get('hasGeneralIndicator').patchValue(false);
        }
        this.formItem.patchValue({
            ageOptions: period.periodAgeDissagregationOptions,
            genderOptions: period.periodGenderDissagregationOptions,
            populationTypeOptions: period.periodPopulationTypeDissagregationOptions as StandardDissagregationOption[],
            diversityOptions: period.periodDiversityDissagregationOptions,
            countryOfOriginOptions: period.periodCountryOfOriginDissagregationOptions
        });
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            year,
            state,
            ageOptions,
            genderOptions,
            populationTypeOptions,
            diversityOptions,
            countryOfOriginOptions,
        }
            = this.formItem.value;
        const period: Period = {
            id,
            year,
            state,
            periodAgeDissagregationOptions: ageOptions,
            periodGenderDissagregationOptions: genderOptions,
            periodPopulationTypeDissagregationOptions: populationTypeOptions,
            periodDiversityDissagregationOptions: diversityOptions,
            periodCountryOfOriginDissagregationOptions: countryOfOriginOptions


        };


        const {
            generalIndicatorId,
            generalIndicatorState,
            generalIndicatorDescription
        }
            = this.formItem.value;
        const dissagregationAssignationsToGeneralIndicator =
            this.formItem.get('totalGeneralIndicatorDissagregations')
                .value as DissagregationAssignationToGeneralIndicator[];


        period.generalIndicator = {
            id: generalIndicatorId,
            period: null,
            state: generalIndicatorState,
            description: generalIndicatorDescription,
            measureType: 'NUMERO',// valor por defecto
            dissagregationAssignationsToGeneralIndicator
        };
        if (this.formItem.get('hasGeneralIndicator').value) {
            period.generalIndicator.state = EnumsState.ACTIVE;
        } else {
            if (period.generalIndicator.id) {
                period.generalIndicator.state = EnumsState.INACTIVE;
            } else {
                period.generalIndicator = null;
            }
        }


        if (period.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.periodService.update(period).subscribe({
                next: () => {
                    this.cancelDialog();
                    this.loadItems();
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al actualizar el periodo',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.periodService.save(period).subscribe({
                next: () => {
                    this.cancelDialog();
                    this.loadItems();
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar el periodo',
                        detail: err.error.message,
                        life: 3000
                    });
                }
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

    hasGeneralIndicatorValueChange(value: boolean) {
        this.ref.detectChanges();
        if (value && !this.formItem.get('generalIndicatorId').value) {
            const indicator = new GeneralIndicator();
            indicator.measureType = 'NUMERO';

            const {
                id: generalIndicatorid,
                description: generalIndicatorDescription,
                measureType: generalIndicatorMeasureType,
                state: generalIndicatorState,
                period: generalIndicatorPeriod,
                dissagregationAssignationsToGeneralIndicator: generalIndicatorDissagregationAssignationsToGeneralIndicator
            } = indicator;
            this.formItem.patchValue({
                generalIndicatorid,
                generalIndicatorPeriod,
                generalIndicatorState,
                generalIndicatorDescription,
                generalIndicatorMeasureType,
                generalIndicatorDissagregationAssignationsToGeneralIndicator
            });
            this.formItem.get('totalGeneralIndicatorDissagregations').patchValue([]);
            this.formItem.get('selectedGeneralIndicatorDissagregations').patchValue([]);
        } else if (value && !this.formItem.get('generalIndicatorId')) {
            this.formItem.get('totalGeneralIndicatorDissagregations').patchValue([]);
            this.formItem.get('selectedGeneralIndicatorDissagregations').patchValue([]);
        } else if (!value && !this.formItem.get('generalIndicatorId')) {
            this.formItem.patchValue({
                generalIndicatorid: null,
                generalIndicatorPeriod: null,
                generalIndicatorState: null,
                generalIndicatorDescription: null,
                generalIndicatorMeasureType: null,
                totalGeneralIndicatorDissagregations: null,
                selectedGeneralIndicatorDissagregations: null
            });
        } else {
            this.formItem.get('generalIndicatorState').patchValue(EnumsState.INACTIVE);
        }
    }

    selectItemGeneralDissagregation(event: any) {
        let selectValues: string[] = event.value;
        let totalGeneralIndicatorDissagregations: DissagregationAssignationToGeneralIndicator[] =
            this.formItem.get('totalGeneralIndicatorDissagregations').value;

        totalGeneralIndicatorDissagregations.forEach(value => value.state=EnumsState.INACTIVE);

        selectValues.forEach(value => {

            let dissagregationAssignationToIndicator: DissagregationAssignationToGeneralIndicator
                = totalGeneralIndicatorDissagregations.find(value1 => value1.dissagregationType === value);

            if (dissagregationAssignationToIndicator) {
                dissagregationAssignationToIndicator.state = EnumsState.ACTIVE;

            } else {
                dissagregationAssignationToIndicator = new DissagregationAssignationToGeneralIndicator();
                dissagregationAssignationToIndicator.dissagregationType = value;
                totalGeneralIndicatorDissagregations.push(dissagregationAssignationToIndicator);
            }
        });
        this.formItem.get('totalGeneralIndicatorDissagregations').patchValue(totalGeneralIndicatorDissagregations);
    }

    getDissagregationAssignationToIndicators(): DissagregationAssignationToGeneralIndicator[] {
        let dissagregationAssignationToIndicators: DissagregationAssignationToGeneralIndicator[] =
            this.formItem.get('totalGeneralIndicatorDissagregations').value?this.formItem.get('totalGeneralIndicatorDissagregations').value:[];
        dissagregationAssignationToIndicators = dissagregationAssignationToIndicators
            .filter(value => value.state===EnumsState.ACTIVE)
            .sort((a, b) => a.dissagregationType.localeCompare(b.dissagregationType));
        return dissagregationAssignationToIndicators;
    }
}
