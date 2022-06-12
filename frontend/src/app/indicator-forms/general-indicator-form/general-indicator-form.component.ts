import {Component, OnInit} from '@angular/core';
import {IndicatorExecution, IndicatorValue, Month, MonthValues} from '../../shared/model/OsmosysModel';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {MonthService} from '../../shared/services/month.service';
import {MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {DissagregationType, EnumsType, SelectItemWithOrder} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../shared/services/user.service';

@Component({
    selector: 'app-general-indicator-form',
    templateUrl: './general-indicator-form.component.html',
    styleUrls: ['./general-indicator-form.component.scss']
})
export class GeneralIndicatorFormComponent implements OnInit {
    indicatorExecution: IndicatorExecution;
    monthId: number;
    projectId: number;
    isAdmin = false;
    isProjectFocalPoint = false;
    isEjecutor = false;
    monthValues: MonthValues;
    month: Month;
    monthValuesMap: Map<string, IndicatorValue[]>;
    formItem: FormGroup;
    editable: boolean;

    sourceTypes: SelectItemWithOrder<any>[];

    render = false;
    showErrorResume = false;
    showOtherSource: boolean;
    totalsValidation: Map<string, number> = null;

    oneDimentionDissagregations: DissagregationType[] = [];
    twoDimentionDissagregations: DissagregationType[] = [];
    threeDimentionDissagregations: DissagregationType[] = [];
    fourDimentionDissagregations: DissagregationType[] = [];
    noDimentionDissagregations: DissagregationType[] = [];
    chekedOptions: SelectItem[];


    constructor(public ref: DynamicDialogRef,
                public config: DynamicDialogConfig,
                public indicatorExecutionService: IndicatorExecutionService,
                public monthService: MonthService,
                public enumsService: EnumsService,
                public utilsService: UtilsService,
                public userService: UserService,
                private messageService: MessageService,
                private fb: FormBuilder
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
            if (this.isEjecutor && (this.month.checked === null || this.month.checked === false)) {
                this.editable = true;
            }
        }
        if (this.editable) {
            this.formItem.get('sources').enable();
        } else {

            this.formItem.get('sources').disable();
        }
    }

    loadMonthValues(monthId: number) {
        this.monthService.getMonthIndicatorValueByMonthId(monthId).subscribe(value => {
            this.monthValues = value as MonthValues;
            this.month = value.month;
            this.setRoles();
            this.monthValuesMap = value.indicatorValuesMap;
            this.formItem.get('commentary').patchValue(this.month.commentary);
            this.formItem.get('sources').patchValue(this.month.sources);
            this.formItem.get('checked').patchValue(this.month.checked);
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
        const totalsValidation = this.utilsService.validateMonth(this.monthValuesMap, null);
        this.monthValues.month.commentary = this.formItem.get('commentary').value;
        this.monthValues.month.sources = this.formItem.get('sources').value;
        this.monthValues.month.sourceOther = this.formItem.get('sourceOther').value;
        if (!this.formItem.get('checked').value == null || this.formItem.get('checked').value === '') {
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
