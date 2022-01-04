import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {DissagregationAssignationToGeneralIndicator, GeneralIndicator, Period} from '../../shared/model/OsmosysModel';
import {PeriodService} from '../../shared/services/period.service';
import {MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {GeneralIndicatorService} from '../../shared/services/general-indicator.service';

@Component({
    selector: 'app-general-indicator-configuration',
    templateUrl: './general-indicator-configuration.component.html',
    styleUrls: ['./general-indicator-configuration.component.scss']
})
export class GeneralIndicatorConfigurationComponent implements OnInit {
    periodForm: FormGroup;
    indicatorForm: FormGroup;
    periods: Period[];
    measureTypes: SelectItem[];
    states: SelectItem[];
    dissagregationTypes: SelectItem[];

    constructor(private fb: FormBuilder,
                private periodService: PeriodService,
                private messageService: MessageService,
                public utilsService: UtilsService,
                private enumsService: EnumsService,
                private generalIndicatorService: GeneralIndicatorService,
    ) {
    }

    ngOnInit(): void {
        this.loadPeriods();
        this.createForms();
        this.loadOptions();
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
                        if (value1) {
                            this.onPeriodChange(value1);
                        }
                    });
                } else {
                    const smallestYear = Math.min(...this.periods.map(value1 => value1.year));
                    const smallestPeriod = this.periods.filter(value1 => {
                        return value1.year === smallestYear;
                    })[0];
                    this.periodForm.get('selectedPeriod').patchValue(smallestPeriod);
                    if (smallestPeriod) {
                        this.onPeriodChange(smallestPeriod);
                    }
                }
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

    private createForms() {
        this.periodForm = this.fb.group({
            selectedPeriod: new FormControl('')
        });
        this.indicatorForm = this.fb.group({
            id: new FormControl(''),
            description: new FormControl('', [Validators.required, Validators.maxLength(255)]),
            measureType: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
            period: new FormControl({value: '', disabled: true}, Validators.required),
            dissagregations: new FormControl('', Validators.required),
            dissagregationAssignationsToGeneralIndicator: new FormControl('')
        });
    }

    onPeriodChange(period: Period) {

        if (period) {
            this.generalIndicatorService.getByPeriodId(period.id).subscribe(value => {
                if (value) {
                    const {
                        id,
                        // tslint:disable-next-line:no-shadowed-variable
                        period,
                        state,
                        description,
                        measureType,
                        dissagregationAssignationsToGeneralIndicator
                    } = value;

                    const dissa = dissagregationAssignationsToGeneralIndicator.filter(value1 => {
                        return value1.state === EnumsState.ACTIVE;
                    }).map(value1 => {
                        return value1.dissagregationType;
                    });

                    this.indicatorForm.patchValue({
                        id,
                        period,
                        state,
                        description,
                        measureType,
                        dissagregationAssignationsToGeneralIndicator,
                        dissagregations: dissa
                    });
                } else {
                    const indicator = new GeneralIndicator();
                    indicator.period = period;
                    this.utilsService.resetForm(this.indicatorForm);
                    this.indicatorForm.patchValue(indicator);
                    this.indicatorForm.get('dissagregations').patchValue([]);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Crea una nueva configuración',
                        life: 3000
                    });
                }
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar el periodo',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {

        }
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

    saveItem() {
        this.messageService.clear();
        const {
            id,
            state,
            description,
            measureType,
        }
            = this.indicatorForm.value;
        const dissagregationAssignationsToGeneralIndicator =
            this.indicatorForm.get('dissagregationAssignationsToGeneralIndicator').value as DissagregationAssignationToGeneralIndicator[];
        const period =
            this.indicatorForm.get('period').value as Period;

        const dissagregations =
            this.indicatorForm.get('dissagregations').value as string[];
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
            id,
            period,
            state,
            description,
            measureType,
            dissagregationAssignationsToGeneralIndicator
        };
        if (indicator.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.generalIndicatorService.update(indicator).subscribe(id => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Guardado con éxito',
                    life: 3000
                });
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al guardar el indicador',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.generalIndicatorService.save(indicator).subscribe(id => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Guardado con éxito',
                    life: 3000
                });
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

    cancel() {
        this.onPeriodChange(this.periodForm.get('selectedPeriod').value as Period);
    }
}
