import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {
    DissagregationAssignationToIndicator, Period,
    StandardDissagregationOption
} from "../../../shared/model/OsmosysModel";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MessageService, SelectItemGroup} from "primeng/api";
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from "../../../shared/model/UtilsModel";
import {EnumsService} from "../../../services/enums.service";
import {BooleanYesNoPipe} from "../../../shared/pipes/boolean-yes-no.pipe";
import {UtilsService} from "../../../services/utils.service";
import {EnumValuesToLabelPipe} from "../../../shared/pipes/enum-values-to-label.pipe";
import {StandardDissagreationListPipe} from "../../../shared/pipes/standard-dissagreation-list.pipe";
import {StandardDissagregationsService} from "../../../services/standardDissagregations.service";

@Component({
    selector: 'app-dissagregation-selector',
    templateUrl: './dissagregation-selector.component.html',
    styleUrls: ['./dissagregation-selector.component.scss']
})
export class DissagregationSelectorComponent implements OnInit {
    @Input() dissagregationAssignationToIndicators: DissagregationAssignationToIndicator[];
    @Input() period: Period;
    @Output() newDissagregationAssignationToIndicators = new EventEmitter<Map<string, Period | DissagregationAssignationToIndicator[]>>();


    formItem: FormGroup;
    formAge: FormGroup;
    dissagregationTypes: SelectItemGroup[];
    cols: ColumnTable[];
    colsAgeOptions: ColumnTable[];
    ageOptions: StandardDissagregationOption[];
    selectedAgeOptions: StandardDissagregationOption[];
    showDialogAges: boolean = false;
    parametersMap = new Map<string, Period | DissagregationAssignationToIndicator[]>();

    constructor(
        private fb: FormBuilder,
        private enumsService: EnumsService,
        private booleanYesNoPipe: BooleanYesNoPipe,
        private enumValuesToLabelPipe: EnumValuesToLabelPipe,
        private standardDissagreationListPipe: StandardDissagreationListPipe,
        private standardDissagregationsService: StandardDissagregationsService,
        private messageService: MessageService,
        public utilsService: UtilsService,
    ) {
    }

    ngOnInit(): void {
        this.parametersMap.set('period',this.period);
        this.formItem = this.fb.group({
            dissagregationAssignationToIndicators: new FormControl(''),
            selectedDissagregations: new FormControl(''),

        });

        this.formAge = this.fb.group({
            useCustomAgeDissagregations: new FormControl('', Validators.required),
            dissagregationAssignationToIndicator: new FormControl('', Validators.required),

        });

        this.cols = [
            {
                field: 'dissagregationType',
                header: 'Desagregación',
                type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.DissagregationType
            },
            {
                field: 'useCustomAgeDissagregations',
                header: 'Uso de Edad Personalizada',
                type: ColumnDataType.text,
                pipeRef: this.booleanYesNoPipe
            },
            {
                field: 'customIndicatorOptions',
                header: 'Desagregaciones de Edad Personalizadas',
                type: ColumnDataType.text,
                pipeRef: this.standardDissagreationListPipe
            }
        ];
        this.colsAgeOptions = [
            {
                field: 'name',
                header: 'Desagregación',
                type: ColumnDataType.text
            },
            {
                field: 'groupName',
                header: 'Grupo',
                type: ColumnDataType.text
            }
        ];
        this.formItem.get('dissagregationAssignationToIndicators').patchValue(this.dissagregationAssignationToIndicators);
        this.load();
    }

    private load() {
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
            let selectedDissagregations = this.dissagregationAssignationToIndicators
                .filter(value1 => value1.state === EnumsState.ACTIVE)
                .map(value1 => value1.dissagregationType);
            this.formItem.get('selectedDissagregations').patchValue(selectedDissagregations);
        });


        this.standardDissagregationsService.getActiveAgeOptions().subscribe({
            next: value => {
                this.ageOptions = value;
                console.log(this.ageOptions);
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
    }

    selectItem(event: any) {
        let selectValues: string[] = event.value;
        selectValues.forEach(value => {
            let dissagregationAssignationToIndicator: DissagregationAssignationToIndicator =
                this.dissagregationAssignationToIndicators
                    .find(value1 => value1.dissagregationType === value);
            if (dissagregationAssignationToIndicator) {
                dissagregationAssignationToIndicator.state = EnumsState.ACTIVE;

            } else {
                dissagregationAssignationToIndicator = new DissagregationAssignationToIndicator();
                dissagregationAssignationToIndicator.period = this.period;
                dissagregationAssignationToIndicator.customIndicatorOptions = [];
                dissagregationAssignationToIndicator.dissagregationType = value;
                this.dissagregationAssignationToIndicators.push(dissagregationAssignationToIndicator);
            }
        });

        let final =
            this.dissagregationAssignationToIndicators.filter(value => {
                console.log("value: " + value.dissagregationType);
                let result = selectValues.some(value1 => {
                    console.log("value:1 " + value1);
                    let result2 = value1 === value.dissagregationType
                    console.log('result2: ' + result2);
                    return result2;

                });
                console.log('result: ' + result);
                return result;
            });


        this.dissagregationAssignationToIndicators = final;

        console.log(final);
        this.formItem.get('dissagregationAssignationToIndicators').patchValue(this.dissagregationAssignationToIndicators);
        this.parametersMap.set('asignations',this.dissagregationAssignationToIndicators);
        this.newDissagregationAssignationToIndicators.emit(this.parametersMap);
    }

    getDissagregationAssignationToIndicators(): DissagregationAssignationToIndicator[] {
        let dissagregationAssignationToIndicators: DissagregationAssignationToIndicator[] =
            this.formItem.get('dissagregationAssignationToIndicators').value;
        dissagregationAssignationToIndicators = dissagregationAssignationToIndicators
            .filter(value => value.state===EnumsState.ACTIVE)
            .sort((a, b) => a.dissagregationType.localeCompare(b.dissagregationType));
        return dissagregationAssignationToIndicators;
    }

    saveItem() {

    }

    customizeAge(rowData: DissagregationAssignationToIndicator) {
        console.log(rowData);
        let dissaObj = this.enumsService.resolveEnum(EnumsType.DissagregationType, rowData.dissagregationType);
        console.log(dissaObj);
        this.messageService.clear();
        this.utilsService.resetForm(this.formAge);
        this.showDialogAges = true;
        this.selectedAgeOptions = rowData.customIndicatorOptions
        this.formAge.get('useCustomAgeDissagregations').patchValue(rowData.useCustomAgeDissagregations);
        this.formAge.get('dissagregationAssignationToIndicator').patchValue(rowData);

    }

    isAgeDissagregation(rowData: any): boolean {
        let dissaObj = this.enumsService.resolveEnum(EnumsType.DissagregationType, rowData.dissagregationType);
        return dissaObj.ageDissagregation;

    }

    submitAges() {
        let dissagregationAssignationToIndicatorTmp: DissagregationAssignationToIndicator = this.formAge.get('dissagregationAssignationToIndicator').value;
        let dissagregationAssignationToIndicatorFinal =
            this.formItem.get('dissagregationAssignationToIndicators').value
                .find(value => value.dissagregationType === dissagregationAssignationToIndicatorTmp.dissagregationType);
        let useCustomAgeDissagregations: boolean = this.formAge.get('useCustomAgeDissagregations').value;
        dissagregationAssignationToIndicatorFinal.useCustomAgeDissagregations = useCustomAgeDissagregations;
        if (useCustomAgeDissagregations) {
            if (this.selectedAgeOptions.length < 1) {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Seleccione al menos una opción',
                    life: 3000
                });
                return;
            }
            dissagregationAssignationToIndicatorFinal.customIndicatorOptions = this.selectedAgeOptions;
        } else {
            dissagregationAssignationToIndicatorFinal.customIndicatorOptions = [];
        }
        this.showDialogAges = false;

        let dissagregationAssignationToIndicators: DissagregationAssignationToIndicator[] =
            this.formItem.get('dissagregationAssignationToIndicators').value
        let c = dissagregationAssignationToIndicators
            .find(value => value.dissagregationType === dissagregationAssignationToIndicatorFinal.dissagregationType);
        c.useCustomAgeDissagregations = dissagregationAssignationToIndicatorFinal.useCustomAgeDissagregations;
        c.customIndicatorOptions = dissagregationAssignationToIndicatorFinal.customIndicatorOptions;
        this.parametersMap.set('asignations', this.dissagregationAssignationToIndicators);
        this.newDissagregationAssignationToIndicators.emit(this.parametersMap)


    }

    cancelAgeDialog() {
        this.showDialogAges = false;
    }
}
