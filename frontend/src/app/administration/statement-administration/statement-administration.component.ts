import {Component, Input, OnInit} from '@angular/core';
import {Period, PeriodStatementAsignation, Pillar, Statement} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, FilterService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {StatementService} from '../../shared/services/statement.service';
import {AreaService} from '../../shared/services/area.service';
import {PillarService} from '../../shared/services/pillar.service';
import {SituationService} from '../../shared/services/situation.service';
import {CodeShortDescriptionPipe} from '../../shared/pipes/code-short-description.pipe';
import {PeriodService} from '../../shared/services/period.service';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';
import {Table} from 'primeng/table';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';


@Component({
    selector: 'app-statement-administration',
    templateUrl: './statement-administration.component.html',
    styleUrls: ['./statement-administration.component.scss']
})
export class StatementAdministrationComponent implements OnInit {
    items: Statement[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    states: SelectItem[];
    areasItems: SelectItem[];
    pillarsItems: SelectItem[];
    situationsItems: SelectItem[];
    periodsItems: SelectItem[];
    parentStatementsItems: SelectItem[];


    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];


    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private statementService: StatementService,
        private areaService: AreaService,
        private pillarService: PillarService,
        private situationService: SituationService,
        private periodService: PeriodService,
        private codeShortDescriptionPipe: CodeShortDescriptionPipe,
        private codeDescriptionPipe: CodeDescriptionPipe
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'code', header: 'Código', type: ColumnDataType.text},
            {field: 'productCode', header: 'Código de Producto', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
            {field: 'parentStatement', header: 'Declaración Padre', type: ColumnDataType.text, pipeRef: this.codeDescriptionPipe},
            {field: 'area', header: 'Área', type: ColumnDataType.text, pipeRef: this.codeShortDescriptionPipe},
            {field: 'pillar', header: 'Pillar', type: ColumnDataType.text, pipeRef: this.codeShortDescriptionPipe},
            {field: 'situation', header: 'Situación', type: ColumnDataType.text, pipeRef: this.codeShortDescriptionPipe},
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');

        this.registerFilters();
        this.formItem = this.fb.group({
            id: new FormControl(''),
            code: new FormControl('', Validators.required),
            productCode: new FormControl('', [Validators.maxLength(20)]),
            description: new FormControl(''),
            state: new FormControl('', Validators.required),
            parentStatement: new FormControl(''),
            area: new FormControl(''),
            pillar: new FormControl('', Validators.required),
            situation: new FormControl('', Validators.required),
            periods: new FormControl('', Validators.required),
            periodStatementAsignations: new FormControl('')
        });


        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });

    }

    private registerFilters() {
        this.filterService.register('objectIdFilter', (value, filter): boolean => {
            return this.filterUtilsService.objectFilterId(value, filter);
        });
        this.filterService.register('parentStatementFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['description', 'code'], filter);
        });
    }

    private loadItems() {
        this.statementService.getAll().subscribe(value => {
            this.items = value;
            this.parentStatementsItems = this.items.map(value1 => {
                return {
                    label: this.codeDescriptionPipe.transform(value1),
                    value: value1
                };
            });
            this.loadAreas();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los pilares',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    private loadAreas() {
        this.areaService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.areasItems = value.map(value1 => {
                const selectItem: SelectItem = {
                    label: this.codeShortDescriptionPipe.transform(value1),
                    value: value1
                };
                return selectItem;
            });
            this.areasItems.sort((a, b) => {
                return a.value.code.localeCompare(b.value.code);
            });
            this.loadPillars();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las áreas',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    private loadPillars() {
        this.pillarService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.pillarsItems = value.map(value1 => {
                const selectItem: SelectItem = {
                    label: this.codeShortDescriptionPipe.transform(value1),
                    value: value1
                };
                return selectItem;
            });
            this.loadSituations();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los pilares',
                detail: error.error.message,
                life: 3000
            });
        });
    }


    private loadSituations() {
        this.situationService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.situationsItems = value.map(value1 => {
                const selectItem: SelectItem = {
                    label: this.codeShortDescriptionPipe.transform(value1),
                    value: value1
                };
                return selectItem;
            });
            this.loadPeriods();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las situaciones',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    private loadPeriods() {
        this.periodService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.periodsItems = value.map(value1 => {
                const selectItem: SelectItem = {
                    label: value1.year.toString(),
                    value: value1
                };
                return selectItem;
            });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los periodos',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'declaraciones');
    }


    createItem() {
        this.parentStatementsItems = this.items.map(value => {
            return {
                label: value.code + ' - ' + value.description,
                value
            };
        });
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Statement();
        this.formItem.patchValue(newItem);
    }

    editItem(statement: Statement) {
        this.parentStatementsItems = this.items.filter(value => {
            return value.id !== statement.id;
        }).map(value => {
            return {
                label: value.code + ' - ' + value.description,
                value
            };
        });
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;

        const assignedPeriods = statement.periodStatementAsignations.filter(value => {
            return value.state === EnumsState.ACTIVE;
        }).map(value => {
            return value.period;
        });

        this.formItem.get('periods').patchValue(assignedPeriods);
        this.formItem.patchValue(statement);
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            code,
            productCode,
            description,
            areaType,
            state,
            parentStatement,
            area,
            pillar,
            situation,
            periods,
            periodStatementAsignations
        }
            = this.formItem.value;
        const statement: Statement = {
            id,
            code,
            productCode,
            description,
            areaType,
            state,
            parentStatement,
            area,
            pillar,
            situation,
            periodStatementAsignations: []
        };

        const periodsCasted = periods as Period[];
        const periodStatementAsignationsCasted = periodStatementAsignations as PeriodStatementAsignation[];
        periodStatementAsignationsCasted.forEach(value => value.state = EnumsState.INACTIVE);
        for (const period of periodsCasted) {
            const periodStatementAsignationF = periodStatementAsignationsCasted.filter(value => {
                return value.period.id === period.id;
            }).pop();
            if (periodStatementAsignationF) {
                periodStatementAsignationF.state = EnumsState.ACTIVE;
            } else {
                const assignation: PeriodStatementAsignation = new PeriodStatementAsignation();
                assignation.period = period;
                assignation.state = EnumsState.ACTIVE;
                periodStatementAsignationsCasted.push(assignation);
            }
        }
        statement.periodStatementAsignations = periodStatementAsignationsCasted;
        // noinspection DuplicatedCode
        if (statement.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.statementService.update(statement).subscribe(() => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al actualizar el pilar',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.statementService.save(statement).subscribe(() => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al guardar el pilar',
                    detail: error.error.message,
                    life: 3000
                });
            });
        }

    }

    cancelDialog() {
        this.parentStatementsItems = this.items.map(value => {
            return {
                label: value.code + ' - ' + value.description,
                value
            };
        });
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
}
