import {Component, OnInit} from '@angular/core';
import {CustomDissagregationValues, IndicatorExecution, IndicatorValue, Month, MonthValues} from '../../shared/model/OsmosysModel';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {MonthService} from '../../shared/services/month.service';
import {EnumsService} from '../../shared/services/enums.service';
import {UtilsService} from '../../shared/services/utils.service';
import {MessageService, SelectItem} from 'primeng/api';
import {DissagregationType, EnumsType, SelectItemWithOrder} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
    selector: 'app-performance-indicator-form',
    templateUrl: './performance-indicator-form.component.html',
    styleUrls: ['./performance-indicator-form.component.scss']
})
export class PerformanceIndicatorFormComponent implements OnInit {
    indicatorExecution: IndicatorExecution;
    monthId: number;
    projectId: number;
    isAdmin = false;
    isProjectFocalPoint = false;
    isEjecutor = false;
    monthValues: MonthValues;
    month: Month;
    monthValuesMap: Map<string, IndicatorValue[]>;
    monthCustomDissagregatoinValues: CustomDissagregationValues[];
    formItem: FormGroup;

    oneDimentionDissagregations: DissagregationType[] = [];
    twoDimentionDissagregations: DissagregationType[] = [];
    threeDimentionDissagregations: DissagregationType[] = [];
    fourDimentionDissagregations: DissagregationType[] = [];
    noDimentionDissagregations: DissagregationType[] = [];

    sourceTypes: SelectItemWithOrder<any>[];

    render = false;
    showErrorResume = false;
    showOtherSource: boolean;
    totalsValidation: Map<string, number> = null;
    chekedOptions: SelectItem[];
    editable: boolean;

    constructor(public ref: DynamicDialogRef,
                public config: DynamicDialogConfig,
                public indicatorExecutionService: IndicatorExecutionService,
                public monthService: MonthService,
                public enumsService: EnumsService,
                public utilsService: UtilsService,
                private messageService: MessageService,
                private fb: FormBuilder,
    ) {
    }

    ngOnInit(): void {
        this.indicatorExecution = this.config.data.indicatorExecution; //
        this.monthId = this.config.data.monthId; //
        this.projectId = this.config.data.projectId; //
        this.isAdmin = this.config.data.isAdmin; //
        this.isProjectFocalPoint = this.config.data.isProjectFocalPoint; //
        this.isEjecutor = this.config.data.isEjecutor; //

        this.formItem = this.fb.group({
            commentary: new FormControl('', [Validators.maxLength(1000)]),
            sources: new FormControl('', Validators.required),
            sourceOther: new FormControl(''),
            checked: new FormControl(''),
            usedBudget: new FormControl(''),
        });
        this.loadMonthValues(this.monthId);
        this.chekedOptions = [];
        this.chekedOptions.push({
            label: 'No Verificado',
            value: null
        });
        this.chekedOptions.push({
            label: 'Valores Verificados',
            value: true
        });
        this.chekedOptions.push({
            label: 'Requiere correcciones por parte del socio',
            value: false
        });
    }

    private setEditable() {
        if (this.isAdmin) {
            this.editable = true;
        } else { // noinspection RedundantIfStatementJS
            if (!this.month.blockUpdate &&
                        (
                            this.isProjectFocalPoint
                            || this.isEjecutor
                        )
                    ) {
                        this.editable = true;
                    } else {
                        this.editable = false;
                    }
        }
    }

    loadMonthValues(monthId: number) {
        this.monthService.getMonthIndicatorValueByMonthId(monthId).subscribe(value => {
            this.monthValues = value as MonthValues;
            this.month = value.month;
            this.monthValuesMap = value.indicatorValuesMap;
            this.monthCustomDissagregatoinValues = value.customDissagregationValues;
            this.formItem.get('commentary').patchValue(this.month.commentary);
            this.formItem.get('sources').patchValue(this.month.sources);
            this.formItem.get('checked').patchValue(this.month.checked);
            if (this.indicatorExecution.keepBudget) {
                this.formItem.get('usedBudget').patchValue(this.month.usedBudget);
                this.formItem.get('usedBudget').setValidators(Validators.required);
            } else {
                this.formItem.get('usedBudget').clearValidators();
            }
            if (this.isProjectFocalPoint) {
                this.formItem.get('checked').enable();
            } else {
                this.formItem.get('checked').disable();
            }
            this.setOtherSource(this.formItem.get('sources').value);
            this.enumsService.getByType(EnumsType.SourceType).subscribe(value1 => {
                this.sourceTypes = value1;
            });
            this.setEditable();
            this.setDimentionsDissagregations();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los valores del mes',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    saveMonth() {
        this.utilsService.setZerosMonthValues(this.monthValuesMap);
        this.utilsService.setZerosCustomMonthValues(this.monthCustomDissagregatoinValues);
        const totalsValidation = this.utilsService.validateMonth(this.monthValuesMap, this.monthCustomDissagregatoinValues);
        this.monthValues.month.commentary = this.formItem.get('commentary').value;
        this.monthValues.month.sources = this.formItem.get('sources').value;
        this.monthValues.month.sourceOther = this.formItem.get('sourceOther').value;
        this.monthValues.month.usedBudget = this.formItem.get('usedBudget').value;
        if (!this.formItem.get('checked').value || this.formItem.get('checked').value === '') {
            this.monthValues.month.checked = null;
        } else {
            this.monthValues.month.checked = this.formItem.get('checked').value;
        }
        if (totalsValidation) {
            this.showErrorResume = true;
            this.totalsValidation = totalsValidation;
        } else {
            this.messageService.clear();
            this.totalsValidation = null;
            this.sendMonthValue();
        }
    }

    cancel() {
        this.ref.close({test: 2});
    }

    private sendMonthValue() {
        this.indicatorExecutionService.updateMonthValues(this.indicatorExecution.id, this.monthValues).subscribe(() => {
            this.messageService.add({severity: 'success', summary: 'Guardado con éxito', detail: ''});
            this.ref.close({test: 1});
        }, error => {
            this.messageService.add({severity: 'error', summary: 'Error al guardar los valores:', detail: error.error.message});
        });
    }

    closeErrorDialog() {
        this.showErrorResume = false;
    }

    setDimentionsDissagregations(): void {
        const totalOneDimentions = this.utilsService.getOneDimentionsDissagregationTypes();
        const totalTwoDimentions = this.utilsService.getTwoDimentionsDissagregationTypes();
        const totalThreeDimentions = this.utilsService.getThreeDimentionsDissagregationTypes();
        const totalFourDimentions = this.utilsService.getFourDimentionsDissagregationTypes();
        const totalNoDimentions = this.utilsService.getNoDimentionsDissagregationTypes();
        this.monthValuesMap.forEach((value, key) => {
            if (value && value.length > 0) {
                const dissagregationType: DissagregationType = DissagregationType[key];
                if (totalOneDimentions.indexOf(dissagregationType) >= 0) {
                    this.oneDimentionDissagregations.push(dissagregationType);
                } else if (totalTwoDimentions.indexOf(dissagregationType) >= 0) {
                    this.twoDimentionDissagregations.push(dissagregationType);
                } else if (totalNoDimentions.indexOf(dissagregationType) >= 0) {
                    this.noDimentionDissagregations.push(dissagregationType);
                } else if (totalThreeDimentions.indexOf(dissagregationType) >= 0) {
                    this.threeDimentionDissagregations.push(dissagregationType);
                } else if (totalFourDimentions.indexOf(dissagregationType) >= 0) {
                    this.fourDimentionDissagregations.push(dissagregationType);
                }
            }
        });
        this.render = true;
    }

    setOtherSource(sources: string[]) {
        if (sources && sources.includes('OTROS')) {
            this.showOtherSource = true;
            this.formItem.get('sourceOther').patchValue(this.month.sourceOther);
            this.formItem.get('sourceOther').setValidators([Validators.required]);
            this.formItem.get('sourceOther').updateValueAndValidity();
        } else {
            this.showOtherSource = false;
            this.formItem.get('sourceOther').patchValue(null);
            this.formItem.get('sourceOther').clearValidators();
            this.formItem.get('sourceOther').updateValueAndValidity();
        }
    }
}
