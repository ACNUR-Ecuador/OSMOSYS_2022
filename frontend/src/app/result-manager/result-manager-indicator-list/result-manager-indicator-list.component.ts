import { HttpResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import {
    FormBuilder,
    FormControl,
    FormGroup,
    Validators,
} from '@angular/forms';
import {
    ConfirmationService,
    FilterService,
    MessageService,
    SelectItem,
} from 'primeng/api';
import { Table } from 'primeng/table';
import { EnumsService } from 'src/app/services/enums.service';
import { FilterUtilsService } from 'src/app/services/filter-utils.service';
import { IndicatorService } from 'src/app/services/indicator.service';
import { PeriodService } from 'src/app/services/period.service';
import { ReportsService } from 'src/app/services/reports.service';
import { ResultManagerService } from 'src/app/services/result-manager.service';
import { TagService } from 'src/app/services/tag.service';
import { UserService } from 'src/app/services/user.service';
import { UtilsService } from 'src/app/services/utils.service';
import {
    Indicator,
    IndicatorTagAsignation,
    Period,
    PeriodTagAsignation,
    ResultManagerIndicator,
    Tag,
} from 'src/app/shared/model/OsmosysModel';
import {
    ColumnDataType,
    ColumnTable,
    EnumsState,
    EnumsType,
} from 'src/app/shared/model/UtilsModel';
import { TagPeriodTagAsignationsListPipe } from 'src/app/shared/pipes/tag-period-tag-asignations-list.pipe';

@Component({
    selector: 'app-result-manager-indicator-list',
    templateUrl: './result-manager-indicator-list.component.html',
    styleUrls: ['./result-manager-indicator-list.component.scss'],
})
export class ResultManagerIndicatorListComponent implements OnInit {
    items: Tag[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    states: SelectItem[];
    _selectedColumns: ColumnTable[];
    periodsItems: SelectItem<Period>[];
    inputNameValue: string = '';
    indicators: Indicator[];
    selectedIndicators: Indicator[];
    operations: any[];
    resultManagerIndicators: ResultManagerIndicator[]

    products: any[] = [
        { name: 'Apple', category: 'Fruit', price: 1.2 },
        { name: 'Banana', category: 'Fruit', price: 0.8 },
        { name: 'Carrot', category: 'Vegetable', price: 0.5 },
        { name: 'Tomato', category: 'Vegetable', price: 1.0 },
        { name: 'Broccoli', category: 'Vegetable', price: 1.5 },
        { name: 'Orange', category: 'Fruit', price: 1.3 },
        { name: 'Strawberry', category: 'Fruit', price: 2.0 },
        { name: 'Cucumber', category: 'Vegetable', price: 0.7 },
        { name: 'Lettuce', category: 'Vegetable', price: 0.6 },
        { name: 'Grapes', category: 'Fruit', price: 2.5 }
      ];

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private tagService: TagService,
        private periodService: PeriodService,
        private indicatorService: IndicatorService,
        private tagPeriodTagAsignationListPipe: TagPeriodTagAsignationsListPipe,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private cd: ChangeDetectorRef,
        private reportsService: ReportsService,
        private resultManagerService: ResultManagerService,
        private userService: UserService

    ) {}

    ngOnInit(): void {
        this.loadResultManagerIndicators();
        this.selectedIndicators = [];
        this.loadItems();
        this.loadPeriods();
        //console.log(this.periodsItems)
        this.cols = [
            { field: 'id', header: 'id', type: ColumnDataType.numeric },
            { field: 'name', header: 'Nombre', type: ColumnDataType.text },
            {
                field: 'description',
                header: 'Descripción',
                type: ColumnDataType.text,
            },
            {
                field: 'periodTagAsignations',
                header: 'Año',
                type: ColumnDataType.numeric,
                pipeRef: this.tagPeriodTagAsignationListPipe,
            },
            {
                field: 'operation',
                header: 'Cálculo',
                type: ColumnDataType.text,
            },
            { field: 'state', header: 'Estado', type: ColumnDataType.text },
            
        ];
        this._selectedColumns = this.cols.filter(
            (value) => value.field !== 'id'
        );

        this.registerFilters();
        this.formItem = this.fb.group({
            id: new FormControl(''),
            name: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
            periods: new FormControl(''),
            period: new FormControl('', Validators.required),
            periodTagAsignations: new FormControl(''),
            indicatorTagAsignations: new FormControl(''),
            operation: new FormControl(''),
            state: new FormControl('', Validators.required),
        });

        this.enumsService.getByType(EnumsType.State).subscribe((value) => {
            this.states = value;
        });
        this.operations = ['Seleccione ...', 'Suma', 'Máximo', 'Mínimo', 'Promedio'];
    }

    private registerFilters() {
        this.filterService.register(
            'periodTagAsignationsFilter',
            (value, filter): boolean => {
                return this.filterUtilsService.periodTagAsignationsFilter(
                    value,
                    filter
                );
            }
        );
    }

    private loadItems() {
        this.tagService.getAll().subscribe({
            next: (value) => {
                this.items = value;

            },
            error: (err) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los tags',
                    detail: err.error.message,
                    life: 3000,
                });
            },
        });
    }

    private loadResultManagerIndicators() {
        const period=3;
        const user=this.userService.getLogedUsername();
        this.resultManagerService.getAll(period, user.id).subscribe({
            next: (value) => {
                this.resultManagerIndicators = value;
                console.log(this.resultManagerIndicators)
            },
            error: (err) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los indicadores del Manager de resultado',
                    detail: err.error.message,
                    life: 3000,
                });
            },
        });
    }

    loadIndicators() {
        const period = this.formItem.get('period').value as Period;
        if(!period) return;

        this.indicatorService.getAll().subscribe({
            next: (value) => {
                const filteredData = value.filter((item) => {
                    const hasActiveDissagregationForYear =
                        item.dissagregationsAssignationToIndicator.some(
                            (dissagregation) =>
                                dissagregation.state === 'ACTIVO' && period.year === dissagregation.period.year                             
                        );
                    return hasActiveDissagregationForYear;
                });

                this.indicators = filteredData.filter(
                    (indicator) => indicator.state === 'ACTIVO'
                );
                if(this.selectedIndicators.length >0){
                    this.indicators =this.indicators.filter((ind) => !this.selectedIndicators.find(ind2 => ind.id === ind2.id));

                }
            },
            error: (err) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los indicadores',
                    detail: err.error.message,
                    life: 3000,
                });
            },
        });
        
    }

    private loadPeriods() {
        this.periodService.getByState(EnumsState.ACTIVE).subscribe({
            next: (value) => {
                this.periodsItems = value.map((value1) => {
                    const selectItem: SelectItem = {
                        label: value1.year.toString(),
                        value: value1,
                    };
                    return selectItem;
                });
            },
            error: (err) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los años',
                    detail: err.error.message,
                    life: 3000,
                });
            },
        });
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(
            this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'tags'
        );
    }

    createItem() {
        this.indicators=[]
        this.selectedIndicators=[]
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Tag();
        this.formItem.patchValue(newItem);
    }

    getReport(tag: Tag) {
        let period: Period;
        if(tag.periodTagAsignations.length <= 0 || tag.periodTagAsignations[0].period == null) return;
        period = tag.periodTagAsignations[0].period;
        this.reportsService.getTagReport(tag, period).subscribe((response: HttpResponse<Blob>) => {
            this.utilsService.downloadFileResponse(response);
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al Generar el Reporte',
                detail: error.error.message,
                life: 3000
            });
        });
    }
    editItem(tag: Tag) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const periodTagAsignations: PeriodTagAsignation[] =
            tag.periodTagAsignations;
        const assignedPeriods: Period[] = tag.periodTagAsignations
            .filter((value) => {
                return value.state === EnumsState.ACTIVE;
            })
            .map((value) => {
                return value.period;
            });

        const assignedIndicators: Indicator[] = tag.indicatorTagAsignations
        .filter((value) => {
            return value.state === EnumsState.ACTIVE;
        }).map(ia => ia.indicator)       

        this.formItem.patchValue(tag);
        this.formItem
            .get('periodTagAsignations')
            .patchValue(periodTagAsignations);
        
        this.selectedIndicators = assignedIndicators;
        this.formItem.get('periods').patchValue(assignedPeriods);        
        if(assignedPeriods.length > 0)
            this.formItem.get('period').patchValue(assignedPeriods[0]);        
        this.loadIndicators();
        this.cd.detectChanges();
    }

    saveItem() {
        this.messageService.clear();
        let {
            id,
            name,
            description,
            period,
            periods,
            periodTagAsignations,
            indicatorTagAsignations,
            operation,
            state,
        } = this.formItem.value;
        name = this.removeAccents(name);
        const tag: Tag = {
            id,
            name,
            description,
            periodTagAsignations: [],
            indicatorTagAsignations: [],
            operation,
            state,
        };

        const periodsCasted = period as Period;
        let periodTagAsignationsCasted =
            periodTagAsignations as PeriodTagAsignation[];

        if (periodTagAsignations) {
            periodTagAsignationsCasted.forEach(
                (value) => (value.state = EnumsState.INACTIVE)
            );
        } else {
            periodTagAsignationsCasted = [];
        }
        const periodTagAsignationF = periodTagAsignationsCasted
            .filter((value) => {
                return value.period.id === period.id;
            })
            .pop();
        if (periodTagAsignationF) {
            periodTagAsignationF.state = EnumsState.ACTIVE;
        } else {
            const assignation: PeriodTagAsignation =
                new PeriodTagAsignation();
            assignation.period = period;
            assignation.state = EnumsState.ACTIVE;
            periodTagAsignationsCasted.push(assignation);
        }        
        let indicatorTagAsignationsCasted =
        indicatorTagAsignations as IndicatorTagAsignation[];

        if (indicatorTagAsignationsCasted) {
            indicatorTagAsignationsCasted.forEach(
                (value) => (value.state = EnumsState.INACTIVE)
            );
        } else {
            indicatorTagAsignationsCasted = [];
        }

        for (const indicator of this.selectedIndicators) {
            const indicatorTagAsignationF = indicatorTagAsignationsCasted
                .filter((value) => {
                    return value.indicator.id === indicator.id;
                })
                .pop();

            if (indicatorTagAsignationF) {
                indicatorTagAsignationF.state = EnumsState.ACTIVE;
            } else {
                const assignation: IndicatorTagAsignation =
                    new IndicatorTagAsignation();
                assignation.indicator = indicator;
                assignation.state = EnumsState.ACTIVE;
                indicatorTagAsignationsCasted.push(assignation);
            }
        }

        tag.periodTagAsignations = periodTagAsignationsCasted;
        tag.indicatorTagAsignations = indicatorTagAsignationsCasted;
        //tag.indicatorTagAsignations = this.selectedIndicators;
        if (tag.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.tagService.update(tag).subscribe({
                next: () => {
                    this.cancelDialog();
                    this.loadItems();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Tag guardado exitosamente',
                        life: 3000
                    });
                },
                error: (err) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al actualizar los tags',
                        detail: err.error.message,
                        life: 3000,
                    });
                },
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.tagService.save(tag).subscribe({
                next: () => {
                    this.cancelDialog();
                    this.loadItems();
                },
                error: (err) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar los tags',
                        detail: err.error.message,
                        life: 3000,
                    });
                    console.log(err.error.message);
                },
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
        this._selectedColumns = this.cols.filter((col) => val.includes(col));
    }

    toUpperCase() {
        this.inputNameValue = this.inputNameValue.toUpperCase();
    }

    removeAccents(texto: string): string {
        const accents: { [key: string]: string } = {
            á: 'a',
            é: 'e',
            í: 'i',
            ó: 'o',
            ú: 'u',
            Á: 'A',
            É: 'E',
            Í: 'I',
            Ó: 'O',
            Ú: 'U',
            ñ: 'n',
            Ñ: 'N',
        };

        return texto
            .split('')
            .map((char) => accents[char] || char)
            .join('');
    }
}
