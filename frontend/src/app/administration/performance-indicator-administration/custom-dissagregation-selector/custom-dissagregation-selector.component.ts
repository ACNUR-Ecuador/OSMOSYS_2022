import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {
    CustomDissagregation,
    CustomDissagregationAssignationToIndicator,
    Period
} from "../../../shared/model/OsmosysModel";
import {ColumnDataType, ColumnTable, EnumsState, SelectItemWithOrder} from "../../../shared/model/UtilsModel";
import {CustomDissagregationService} from "../../../services/custom-dissagregation.service";
import {MessageService} from "primeng/api";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {BooleanYesNoPipe} from "../../../shared/pipes/boolean-yes-no.pipe";
import {CustomDissagregationOptionsListPipe} from "../../../shared/pipes/custom-dissagregation-options-list.pipe";
import {UtilsService} from "../../../services/utils.service";

@Component({
    selector: 'app-custom-dissagregation-selector',
    templateUrl: './custom-dissagregation-selector.component.html',
    styleUrls: ['./custom-dissagregation-selector.component.scss']
})
export class CustomDissagregationSelectorComponent implements OnInit {
    @Input() customDissagregationAssignationToIndicators: CustomDissagregationAssignationToIndicator[];
    @Input() period: Period;
    @Output() newDissagregationAssignationToIndicators = new EventEmitter<Map<string, Period | CustomDissagregationAssignationToIndicator[]>>();


    customDissagregations: SelectItemWithOrder<any>[];
    formItem: FormGroup;
    cols: ColumnTable[];
    parametersMap= new Map<string, Period | CustomDissagregationAssignationToIndicator[]>();

    constructor(
        private customDissagregationService: CustomDissagregationService,
        private messageService: MessageService,
        private booleanYesNoPipe: BooleanYesNoPipe,
        private customDissagregationOptionsListPipe: CustomDissagregationOptionsListPipe,
        public utilsService: UtilsService,
        private fb: FormBuilder,
    ) {
    }

    ngOnInit(): void {
        this.parametersMap.set('period', this.period);

        this.formItem = this.fb.group({
            dissagregationAssignationToIndicators: new FormControl(''),
            selectedDissagregations: new FormControl(''),

        });

        this.cols = [
            {
                field: 'customDissagregation.name',
                header: 'Desagregación',
                type: ColumnDataType.text
            },
            {
                field: 'customDissagregation.controlTotalValue',
                header: 'Validación de totales',
                type: ColumnDataType.text,
                pipeRef: this.booleanYesNoPipe
            },
            {
                field: 'customDissagregation.customDissagregationOptions',
                header: 'Opciones de la desagregación',
                type: ColumnDataType.text,
                pipeRef: this.customDissagregationOptionsListPipe
            }
        ];

        this.formItem.get('dissagregationAssignationToIndicators')
            .patchValue(this.customDissagregationAssignationToIndicators);
        this.load();
    }

    private load() {
        this.customDissagregationService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    this.customDissagregations = value
                        .sort((a, b) => a.name.localeCompare(b.name))
                        .map((value1, index) => {
                                let selectItem = new SelectItemWithOrder();
                                selectItem.value = value1;
                                selectItem.label = value1.name;
                                selectItem.order = index;
                                return selectItem;
                            }
                        )
                    ;
                    let selectedOptions=this.customDissagregationAssignationToIndicators.filter(
                        value => value.state===EnumsState.ACTIVE
                    ).map(value => value.customDissagregation)
                        ;
                    this.formItem.get('selectedDissagregations').patchValue(selectedOptions);
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los estados',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });

    }

    saveItem() {

    }

    selectItem($event: any) {
        let newDA: CustomDissagregation[] = $event.value;
        newDA.forEach(value => {
            let customDissagregationAssignationToIndicator: CustomDissagregationAssignationToIndicator =
                this.customDissagregationAssignationToIndicators.find(value1 => value1.customDissagregation.id === value.id);
            if (customDissagregationAssignationToIndicator) {
                customDissagregationAssignationToIndicator.state = EnumsState.ACTIVE;
            } else {
                customDissagregationAssignationToIndicator = new CustomDissagregationAssignationToIndicator();
                customDissagregationAssignationToIndicator.customDissagregation = value;
                customDissagregationAssignationToIndicator.period = this.period;
                this.customDissagregationAssignationToIndicators.push(customDissagregationAssignationToIndicator);
            }


        });

        let final =
            this.customDissagregationAssignationToIndicators.filter(value => {
                return newDA.some(value1 => {
                    return value1.id === value.customDissagregation.id;

                });
            });


        this.customDissagregationAssignationToIndicators = final;
        this.formItem.get('dissagregationAssignationToIndicators').patchValue(this.customDissagregationAssignationToIndicators);
        this.parametersMap.set('asignations', this.customDissagregationAssignationToIndicators);
        this.newDissagregationAssignationToIndicators.emit(this.parametersMap);

    }


    getDissagregationAssignationToIndicators(): CustomDissagregationAssignationToIndicator[] {
        let dissagregationAssignationToIndicators: CustomDissagregationAssignationToIndicator[] =
            this.formItem.get('dissagregationAssignationToIndicators').value;
        dissagregationAssignationToIndicators = dissagregationAssignationToIndicators
            .filter(value => value.state === EnumsState.ACTIVE)
            .sort((a, b) => {
                console.log(a.customDissagregation.name);
                return a.customDissagregation.name.localeCompare(b.customDissagregation.name)
            });
        return dissagregationAssignationToIndicators;
    }
}
