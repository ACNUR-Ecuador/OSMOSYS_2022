import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {DissagregationAssignationToGeneralIndicator, GeneralIndicator, Period} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {PeriodService} from '../../shared/services/period.service';
import {GeneralIndicatorService} from '../../shared/services/general-indicator.service';

@Component({
    selector: 'app-period-administration',
    templateUrl: './period-administration.component.html',
    styleUrls: ['./period-administration.component.scss']
})
export class PeriodAdministrationComponent implements OnInit {
    items: Period[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    private states: SelectItem[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    dissagregationTypes: SelectItem[];
    measureTypes: SelectItem[];

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private periodService: PeriodService,
        private ref: ChangeDetectorRef
    ) {
    }

    ngOnInit(): void {
        this.loadOptions();
        this.loadItems();
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
            generalIndicatorMeasureType: new FormControl(''),
            generalIndicatorState: new FormControl(''),
            generalIndicatorPeriod: new FormControl({value: '', disabled: true}),
            generalIndicatorDissagregations: new FormControl(''),
            generalIndicatorDissagregationAssignationsToGeneralIndicator: new FormControl('')
        });
    }

    private loadItems() {
        this.periodService.getwithGeneralIndicatorAll().subscribe(value => {
            this.items = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los periodos',
                detail: error.error.message,
                life: 3000
            });
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
            this.dissagregationTypes = value;
        });
    }

    exportExcel() {
        import('xlsx').then(xlsx => {
            const itemsRenamed = this.utilsService.renameKeys(this.items, this.cols);
            const worksheet = xlsx.utils.json_to_sheet(itemsRenamed);
            const workbook = {Sheets: {data: worksheet}, SheetNames: ['data']};
            const excelBuffer: any = xlsx.write(workbook, {bookType: 'xlsx', type: 'array'});
            this.utilsService.saveAsExcelFile(excelBuffer, 'periodos');
        });
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

        const {
            id: generalIndicatorid,
            description: generalIndicatorDescription,
            measureType: generalIndicatorMeasureType,
            state: generalIndicatorState,
            period: generalIndicatorPeriod,
            // generalIndicatorDissagregations,
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
        this.formItem.get('generalIndicatorDissagregations').patchValue([]);
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
                // generalIndicatorDissagregations,
                dissagregationAssignationsToGeneralIndicator: generalIndicatorDissagregationAssignationsToGeneralIndicator
            } = period.generalIndicator;

            if (generalIndicatorState === EnumsState.ACTIVE) {
                this.formItem.get('hasGeneralIndicator').patchValue(true);
            } else {
                this.formItem.get('hasGeneralIndicator').patchValue(false);
            }

            const generalIndicatorDissagregations = generalIndicatorDissagregationAssignationsToGeneralIndicator.filter(value1 => {
                return value1.state === EnumsState.ACTIVE;
            }).map(value1 => {
                return value1.dissagregationType;
            });

            this.formItem.patchValue({
                generalIndicatorId,
                generalIndicatorPeriod,
                generalIndicatorState,
                generalIndicatorDescription,
                generalIndicatorMeasureType,
                generalIndicatorDissagregationAssignationsToGeneralIndicator,
                generalIndicatorDissagregations
            });
        }
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            year,
            state
        }
            = this.formItem.value;
        const period: Period = {
            id,
            year,
            state
        };


        const {
            generalIndicatorId,
            generalIndicatorPeriod,
            generalIndicatorState,
            generalIndicatorDescription,
            generalIndicatorMeasureType,
            generalIndicatorDissagregations
        }
            = this.formItem.value;
        const dissagregationAssignationsToGeneralIndicator =
            this.formItem.get('generalIndicatorDissagregationAssignationsToGeneralIndicator')
                .value as DissagregationAssignationToGeneralIndicator[];

        const dissagregations =
            this.formItem.get('generalIndicatorDissagregations').value as string[];
        dissagregationAssignationsToGeneralIndicator.forEach(dissaAssig => {
            if (dissagregations.includes(dissaAssig.dissagregationType)) {
                dissaAssig.state = EnumsState.ACTIVE;
            } else {
                dissaAssig.state = EnumsState.INACTIVE;
            }
        });
        dissagregations.forEach(dissa => {
            if (!dissagregationAssignationsToGeneralIndicator.map(value => {
                return value.dissagregationType;
            }).includes(dissa)) {
                const dissaAssNew = new DissagregationAssignationToGeneralIndicator();
                dissaAssNew.dissagregationType = dissa;
                dissagregationAssignationsToGeneralIndicator.push(dissaAssNew);
            }
        });

        const indicator: GeneralIndicator = {
            id: generalIndicatorId,
            period: null,
            state: generalIndicatorState,
            description: generalIndicatorDescription,
            measureType: generalIndicatorMeasureType,
            dissagregationAssignationsToGeneralIndicator
        };
        period.generalIndicator = indicator;
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
            this.periodService.update(period).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al actualizar el periodo',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.periodService.save(period).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al guardar el periodo',
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

    hasGeneralIndicatorValueChange(value: boolean) {
        this.ref.detectChanges();
        if (value && !this.formItem.get('generalIndicatorId').value) {
            const indicator = new GeneralIndicator();

            const {
                id: generalIndicatorid,
                description: generalIndicatorDescription,
                measureType: generalIndicatorMeasureType,
                state: generalIndicatorState,
                period: generalIndicatorPeriod,
                // generalIndicatorDissagregations,
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
            this.formItem.get('generalIndicatorDissagregations').patchValue([]);
        } else if (value && !this.formItem.get('generalIndicatorId')) {
            this.formItem.get('generalIndicatorDissagregations').patchValue([]);
        } else if (!value && !this.formItem.get('generalIndicatorId')) {
            this.formItem.patchValue({
                generalIndicatorid: null,
                generalIndicatorPeriod: null,
                generalIndicatorState: null,
                generalIndicatorDescription: null,
                generalIndicatorMeasureType: null,
                generalIndicatorDissagregationAssignationsToGeneralIndicator: null
            });
        } else {
            this.formItem.get('generalIndicatorState').patchValue(EnumsState.INACTIVE);
        }
    }

}
