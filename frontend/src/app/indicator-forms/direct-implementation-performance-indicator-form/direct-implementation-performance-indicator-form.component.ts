import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Canton, CustomDissagregationValues, IndicatorExecution, IndicatorValue, Month, MonthValues} from '../../shared/model/OsmosysModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {DissagregationType, EnumsState, EnumsType, SelectItemWithOrder} from '../../shared/model/UtilsModel';
import {MessageService, SelectItem} from 'primeng/api';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {MonthService} from '../../shared/services/month.service';
import {EnumsService} from '../../shared/services/enums.service';
import {UtilsService} from '../../shared/services/utils.service';
import {CantonService} from '../../shared/services/canton.service';
import {UserService} from '../../shared/services/user.service';

@Component({
    selector: 'app-direct-implementation-performance-indicator-form',
    templateUrl: './direct-implementation-performance-indicator-form.component.html',
    styleUrls: ['./direct-implementation-performance-indicator-form.component.scss']
})
export class DirectImplementationPerformanceIndicatorFormComponent implements OnInit {
    indicatorExecution: IndicatorExecution;
    monthId: number;
    monthValues: MonthValues;
    month: Month;
    monthValuesMap: Map<string, IndicatorValue[]>;
    monthCustomDissagregatoinValues: CustomDissagregationValues[];
    formItem: FormGroup;
    isAdmin = false;
    isSupervisor = false;
    isResponsible = false;

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
    disableSave = false;
    hasLocationDissagregation: boolean;
    showLocationsDialog = false;
    cantonesAvailable: Canton[];
    public formLocations: FormGroup;
    public cantonesOptions: Canton[];
    editable: boolean;

    constructor(public ref: DynamicDialogRef,
                public config: DynamicDialogConfig,
                public indicatorExecutionService: IndicatorExecutionService,
                public monthService: MonthService,
                public enumsService: EnumsService,
                public utilsService: UtilsService,
                private cantonService: CantonService,
                private messageService: MessageService,
                private userService: UserService,
                private fb: FormBuilder,
                private cd: ChangeDetectorRef
    ) {
    }

    ngOnInit(): void {
        this.indicatorExecution = this.config.data.indicatorExecution;
        this.monthId = this.config.data.monthId;

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
        this.formLocations = this.fb.group({
            locationsSelected: new FormControl('')
        });
    }

    setRoles() {
        const userId = this.userService.getLogedUsername().id;
        this.isAdmin = this.userService.hasAnyRole(['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']);
        if (this.indicatorExecution.supervisorUser && this.indicatorExecution.supervisorUser.id === userId) {
            this.isSupervisor = true;
        }
        if (this.indicatorExecution.assignedUser && this.indicatorExecution.assignedUser.id === userId) {
            this.isResponsible = true;
        }
        if (this.indicatorExecution.assignedUserBackup && this.indicatorExecution.assignedUserBackup.id === userId) {
            this.isResponsible = true;
        }
        this.setEditable();
    }

    private setEditable() {
        if (this.isAdmin) {
            this.editable = true;
        } else {
            if (this.month.blockUpdate) {
                this.editable = false;
            } else {
                if (this.isResponsible && (this.month.checked === null || this.month.checked === false)) {
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
                label: 'Requiere correcciones',
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
            if (this.isSupervisor || this.isAdmin) {
                this.formItem.get('checked').enable();
            } else {
                this.formItem.get('checked').disable();
            }
            this.setOtherSource(this.formItem.get('sources').value);
            this.enumsService.getByType(EnumsType.SourceType).subscribe(value1 => {
                this.sourceTypes = value1.filter(value2 => value2.value !== 'SISTEMA_ORGANIZACION');

            });
            this.setDimentionsDissagregations();
            this.validateSegregations();
            this.getHasLocationDissagregation();

            this.cd.detectChanges();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los valores del mes',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    getHasLocationDissagregation() {
        this.hasLocationDissagregation = false;
        for (const entry of Array.from(this.monthValuesMap.entries())) {
            const key = entry[0];
            const value = entry[1];
            if (value && this.utilsService.isLocationDissagregation(key as DissagregationType)) {
                this.hasLocationDissagregation = true;
                return;
            }
        }
    }

    validateSegregations() {
        this.disableSave = false;
        this.monthValuesMap.forEach((value, key) => {
            if (value && value.length === 0) {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Actualiza los valores para ' + this.enumsService.resolveLabel(EnumsType.DissagregationType, key),
                    detail: 'Agrega lugares de ejecución',
                    life: 3000
                });
                this.disableSave = true;
            }
        });
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
        if (this.isResponsible && this.monthValues.month.checked === false) {
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

    openLocationsDialog() {
        this.messageService.clear();
        this.cantonService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.cantonesOptions = this.utilsService.sortCantones(value);
            // noinspection JSMismatchedCollectionQueryUpdate
            let cantonesCurrent: Canton[] = [];
            this.monthValuesMap.forEach((value1, key) => {
                if (this.utilsService.isLocationDissagregation(key as DissagregationType) && value1) {
                    const cantones = value1
                        .filter(value2 => value2.state === EnumsState.ACTIVE)
                        .filter(value2 => value2.location)
                        .map(value2 => value2.location);
                    cantonesCurrent = cantonesCurrent.concat(cantones);
                }

            });
            const keyId = 'id';
            const cantonesCurrentUnique: Canton[] = [...new Map(cantonesCurrent.map(item =>
                [item[keyId], item])).values()];


            if (!cantonesCurrentUnique || cantonesCurrentUnique.length < 1) {
                this.formLocations.get('locationsSelected').patchValue([]);
                this.cantonesAvailable = this.utilsService.sortCantones(this.cantonesOptions);
            } else {
                this.formLocations.get('locationsSelected').patchValue(this.utilsService.sortCantones(cantonesCurrentUnique));
                this.cantonesAvailable =
                    this.cantonesOptions.filter((canton1) => !cantonesCurrentUnique.find(canton2 => canton1.id === canton2.id));
            }
            this.showLocationsDialog = true;
            this.cd.detectChanges();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los cantones',
                detail: error.error.message,
                life: 3000
            });
        });

    }

    saveLocations() {
        const cantones: Canton[] = this.formLocations.get('locationsSelected').value;
        if (!cantones || cantones.length < 1) {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona al menos un cantón',
                life: 3000
            });
        } else {
            this.indicatorExecutionService
                .updateDirectImplementationIndicatorExecutionLocationAssigment(this.indicatorExecution.id, cantones)
                .subscribe(() => {
                    this.loadMonthValues(this.monthId);
                    this.showLocationsDialog = false;
                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar los cantones',
                        detail: error.error.message,
                        life: 3000
                    });
                });
        }
    }

    cancelLocations() {
        this.showLocationsDialog = false;
    }
}
