import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Canton, EnumWeb, IndicatorExecution, IndicatorValue, Month, MonthValues} from '../../shared/model/OsmosysModel';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {MessageService, SelectItem} from 'primeng/api';
import {EnumsState, EnumsType, SelectItemWithOrder} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {IndicatorExecutionService} from '../../services/indicator-execution.service';
import {MonthService} from '../../services/month.service';
import {EnumsService} from '../../services/enums.service';
import {UtilsService} from '../../services/utils.service';
import {UserService} from '../../services/user.service';
import {CantonService} from "../../services/canton.service";
import { EnumValuesToLabelPipe } from 'src/app/shared/pipes/enum-values-to-label.pipe';

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
    noEditionMessage: string = '';

    sourceTypes: SelectItemWithOrder<any>[];

    render = false;
    
    showOtherSource: boolean;
    totalsValidation: Map<string, any> = null;
    showTotalErrorResume = false;
    showMissmatchErrorResume = false;
    noDimentionDissagregations: EnumWeb[] = [];
    oneDimentionDissagregations: EnumWeb[] = [];
    twoDimentionDissagregations: EnumWeb[] = [];
    threeDimentionDissagregations: EnumWeb[] = [];
    fourDimentionDissagregations: EnumWeb[] = [];
    fiveDimentionDissagregations: EnumWeb[] = [];
    sixDimentionDissagregations: EnumWeb[] = [];


    chekedOptions: SelectItem[];

    hasLocationDissagregation: boolean;
    disableSave = false;
    public cantonesOptions: Canton[]=[];
    public formLocations: FormGroup;
    cantonesAvailable: Canton[];
    showLocationsDialog = false;

    constructor(public ref: DynamicDialogRef,
                public config: DynamicDialogConfig,
                public indicatorExecutionService: IndicatorExecutionService,
                public monthService: MonthService,
                public enumsService: EnumsService,
                public utilsService: UtilsService,
                public userService: UserService,
                private messageService: MessageService,
                private fb: FormBuilder,
                private cantonService: CantonService,
                private cd: ChangeDetectorRef,
                private enumValuesToLabelPipe: EnumValuesToLabelPipe
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
            commentary: new FormControl('', [Validators.maxLength(1000), Validators.required]),
            sources: new FormControl(''),
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
        this.formLocations = this.fb.group({
            locationsSelected: new FormControl('')
        });
    }

    setRoles() {
        const userId = this.userService.getLogedUsername().id;

        this.isAdmin = this.userService.hasAnyRole(['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL','ADMINISTRADOR_LOCAL']);
        if (this.indicatorExecution.project.focalPoints.some( fp => fp.id === userId) ) {
            this.isProjectFocalPoint = true;
        }
        this.isEjecutor = this.userService.hasAnyRole(['EJECUTOR_PROYECTOS'])
            && this.userService.getLogedUsername().organization.id === this.indicatorExecution.project.organization.id;
        this.setEditable();
    }

    private setEditable() {
        this.noEditionMessage = null;
        if (this.isAdmin) {
            this.editable = true;
        } else if (this.month.blockUpdate && (this.isEjecutor || this.isProjectFocalPoint)) {
            this.editable = false;
            this.noEditionMessage = "El indicador está bloqueado, comuníquese con el punto focal si desea actualizarlo";
        } else if (!this.month.blockUpdate && (this.isEjecutor || this.isProjectFocalPoint)) {
            this.editable = true;
        } else {
            this.editable = false;
            this.noEditionMessage = "No tiene los permisos para editar la información";
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
            this.getHasLocationDissagregation();

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
        const totalsValidation = this.utilsService.validateMonthAndOptions(this.monthValuesMap, null);
        this.monthValues.month.commentary = this.formItem.get('commentary').value;
        this.monthValues.month.sources = this.formItem.get('sources').value;
        this.monthValues.month.sourceOther = this.formItem.get('sourceOther').value;
        if (!this.formItem.get('checked').value == null || this.formItem.get('checked').value === '') {
            this.monthValues.month.checked = null;
        } else {
            this.monthValues.month.checked = this.formItem.get('checked').value;
        }
        if (totalsValidation) {
            if(totalsValidation.type=='totalsError'){
                this.showTotalErrorResume = true;
                this.totalsValidation = totalsValidation.value;
            }else{
                this.showMissmatchErrorResume = true;
                this.totalsValidation = totalsValidation.value;
            }
        } else {
            this.messageService.clear();
            this.totalsValidation = null;
            this.sendMonthValue();
        }
    }
    missMatchMessage(dissagregation){
        const dissMap=this.totalsValidation.get(dissagregation)
        const firstKey = dissMap?.keys().next().value;
        const firstSubkey = dissMap.get(firstKey)?.keys().next().value;
        const firstValue = dissMap.get(firstKey)?.get(firstSubkey);    

        const result = `${this.enumValuesToLabelPipe.transform(dissagregation, 'DissagregationType')}: ${this.utilsService.getDissagregationlabelByKey(firstKey)} - ${firstSubkey}`;

        return result

    }

    missMatchTotal(dissagregation){
        const dissMap=this.totalsValidation.get(dissagregation)
        const firstKey = dissMap?.keys().next().value;
        const firstSubkey = dissMap.get(firstKey)?.keys().next().value;
        const firstValue = dissMap.get(firstKey)?.get(firstSubkey);    

        const result = `${firstValue}`;

        return result

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
            this.messageService.add({
                severity: 'error',
                summary: 'Error al guardar los valores:',
                detail: error.error.message
            });
        });
    }

    closeErrorDialog() {
        this.showTotalErrorResume = false;
        this.showMissmatchErrorResume = false;
    }

    setDimentionsDissagregations(): void {
        this.render = false;
        const dimensionsMap: Map<number, EnumWeb[]> = this.utilsService.setDimentionsDissagregationsV2(
            this.monthValuesMap
        );

        this.noDimentionDissagregations = dimensionsMap.get(0);
        this.oneDimentionDissagregations = dimensionsMap.get(1);
        this.twoDimentionDissagregations = dimensionsMap.get(2);
        this.threeDimentionDissagregations = dimensionsMap.get(3);
        this.fourDimentionDissagregations = dimensionsMap.get(4);
        this.fiveDimentionDissagregations = dimensionsMap.get(5);
        this.sixDimentionDissagregations = dimensionsMap.get(6);
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

    getHasLocationDissagregation() {
        this.hasLocationDissagregation = false;
        for (const entry of Array.from(this.monthValuesMap.entries())) {
            const key = entry[0];
            const value = entry[1];
            if (value && this.utilsService.isLocationDissagregation(key)) {
                this.hasLocationDissagregation = true;
                this.validateSegregations();
                return;
            }
        }
    }

    validateSegregations() {
        this.disableSave = false;
        this.monthValuesMap.forEach((value, key) => {
            if (value && value.length === 0) {
                console.log(value.length);
                /* this.messageService.add({
                     severity: 'error',
                     summary: 'Actualiza los valores para ' + this.enumsService.resolveLabel(EnumsType.DissagregationType, key),
                     detail: 'Agrega lugares de ejecución',
                     sticky: true
                 });*/
                this.disableSave = true;
            }
        });
    }

    openLocationsDialog() {
        this.messageService.clear();
        this.cantonService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.cantonesOptions = this.utilsService.sortCantones(value);
            // noinspection JSMismatchedCollectionQueryUpdate
            let cantonesCurrent: Canton[] = [];
            this.monthValuesMap.forEach((value1, key) => {
                if (this.utilsService.isLocationDissagregation(key) && value1) {
                    const cantones = value1
                        .filter(value2 => value2.state === EnumsState.ACTIVE)
                        .filter(value2 => value2.location)
                        .map(value2 => value2.location);
                    // todo 2024 restaurar
                    //cantonesCurrent = cantonesCurrent.concat(cantones);
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

    cancelLocations() {
        this.showLocationsDialog = false;
    }


    saveLocations() {
        const cantones: Canton[] = this.formLocations.get('locationsSelected').value;
        if (!cantones || cantones.length < 1) {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona al menos un lugar',
                life: 3000
            });
        } else {
            this.indicatorExecutionService
                .updatePartnerIndicatorExecutionLocationAssigment(this.indicatorExecution.id, cantones)
                .subscribe(() => {
                    this.loadMonthValues(this.monthId);
                    this.showLocationsDialog = false;
                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar los lugar',
                        detail: error.error.message,
                        sticky: true
                    });
                });
        }
    }
}
