import {Component, OnInit} from '@angular/core';
import {CustomDissagregationValues, IndicatorExecution, IndicatorValue, Month, MonthValues} from '../../shared/model/OsmosysModel';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {MessageService, SelectItem} from 'primeng/api';
import {DissagregationType, EnumsType, SelectItemWithOrder} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {IndicatorExecutionService} from '../../services/indicator-execution.service';
import {MonthService} from '../../services/month.service';
import {EnumsService} from '../../services/enums.service';
import {UtilsService} from '../../services/utils.service';
import {UserService} from '../../services/user.service';


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
                private userService: UserService,
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
    }

    setRoles() {
        const userId = this.userService.getLogedUsername().id;

        this.isAdmin = this.userService.hasAnyRole(['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']);
        if (this.indicatorExecution.project.focalPoint && this.indicatorExecution.project.focalPoint.id === userId) {
            this.isProjectFocalPoint = true;
        }
        this.isEjecutor = this.userService.hasAnyRole(['EJECUTOR_PROYECTOS'])
            && this.userService.getLogedUsername().organization.id === this.indicatorExecution.project.organization.id;
        this.setEditable();
    }

    private setEditable() {
        if (this.isAdmin) {
            this.editable = true;
        } else {
            if (this.month.blockUpdate) {
                this.editable = false;
            } else {
                if (this.isEjecutor && (this.month.checked === null || this.month.checked === false)) {
                    this.editable = true;
                }
            }
        }
        if (this.editable) {
            this.formItem.get('sources').enable();
        } else {

            this.formItem.get('sources').disable();
        }
        if (!this.indicatorExecution.indicator.blockAfterUpdate) {
            this.chekedOptions.push({
                label: 'Requiere correcciones por parte del socio',
                value: false
            });
        }

    }

    loadMonthValues(monthId: number) {
        this.monthService.getMonthIndicatorValueByMonthId(monthId).subscribe(value => {
            this.monthValues = value as MonthValues;
            this.month = value.month;
            this.setRoles();
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
            if (this.isProjectFocalPoint || this.isAdmin) {
                this.formItem.get('checked').enable();
            } else {
                this.formItem.get('checked').disable();
            }
            this.setOtherSource(this.formItem.get('sources').value);
            this.enumsService.getByType(EnumsType.SourceType).subscribe(value1 => {
                this.sourceTypes = value1;
            });
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
        if (!this.formItem.get('checked').value === null || this.formItem.get('checked').value === '') {
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

        if (this.isEjecutor && this.monthValues.month.checked === false) {
            this.monthValues.month.checked = null;
        }
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
        this.render = false;
        const dimensionsMap: Map<number, DissagregationType[]> = this.utilsService.setDimentionsDissagregations(
            this.monthValuesMap
        );
        this.noDimentionDissagregations = dimensionsMap.get(0);
        this.oneDimentionDissagregations = dimensionsMap.get(1);
        this.twoDimentionDissagregations = dimensionsMap.get(2);
        this.threeDimentionDissagregations = dimensionsMap.get(3);
        this.fourDimentionDissagregations = dimensionsMap.get(4);
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
