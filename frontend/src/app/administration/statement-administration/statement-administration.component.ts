import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {Area, ImportFile, Period, PeriodStatementAsignation, Pillar, Statement} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, FilterService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {EnumsService} from '../../services/enums.service';
import {StatementService} from '../../services/statement.service';
import {AreaService} from '../../services/area.service';
import {PillarService} from '../../services/pillar.service';
import {SituationService} from '../../services/situation.service';
import {CodeShortDescriptionPipe} from '../../shared/pipes/code-short-description.pipe';
import {PeriodService} from '../../services/period.service';
import {FilterUtilsService} from '../../services/filter-utils.service';
import {Table} from 'primeng/table';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {
    StatementPeriodStatementAsignationsListPipe
} from "../../shared/pipes/statement-period-statement-asignations-list.pipe";
import {HttpResponse} from "@angular/common/http";
import {EnumValuesToLabelPipe} from "../../shared/pipes/enum-values-to-label.pipe";


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
    periodsItems: SelectItem<Period>[];
    parentStatementsItemsFiltered: SelectItem[];
    parentStatementsItems: SelectItem[];
    areaTypesItems: SelectItem[];
    filterAreaList: SelectItem[];
    selectedAreaType: string;


    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    showDialogImport = false;
    importForm: FormGroup;


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
        private codeDescriptionPipe: CodeDescriptionPipe,
        private statementPeriodStatementAsignationsListPipe: StatementPeriodStatementAsignationsListPipe,
        private enumValuesToLabelPipe: EnumValuesToLabelPipe,
        private cd: ChangeDetectorRef
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'code', header: 'Código', type: ColumnDataType.text},
            {
                field: 'areaType',
                header: 'Nivel de Resultado',
                type: ColumnDataType.text,
                pipeRef: this.enumValuesToLabelPipe,
                arg1: EnumsType.AreaType
            },
            {field: 'description', header: 'Enunciado', type: ColumnDataType.text},
            {
                field: 'parentStatement',
                header: 'Enunciado Padre',
                type: ColumnDataType.text,
                pipeRef: this.codeDescriptionPipe
            },
            {field: 'area', header: 'Área', type: ColumnDataType.text, pipeRef: this.codeShortDescriptionPipe},
            {field: 'pillar', header: 'Grupo Poblacional', type: ColumnDataType.text, pipeRef: this.codeShortDescriptionPipe},            
            {
                field: 'periodStatementAsignations',
                header: 'Años',
                type: ColumnDataType.numeric,
                pipeRef: this.statementPeriodStatementAsignationsListPipe
            },
            {field: 'state', header: 'Estado', type: ColumnDataType.text},

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
            areaType: new FormControl('', Validators.required),
            pillar: new FormControl('', Validators.required),
            situation: new FormControl(''),
            periods: new FormControl('', Validators.required),
            periodStatementAsignations: new FormControl('')
        });


        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
        this.enumsService.getByType(EnumsType.AreaType).subscribe(value => {
            this.areaTypesItems = value;
        });

        this.importForm = this.fb.group({
            period: new FormControl('', [Validators.required]),
            fileName: new FormControl('', [Validators.required]),
            file: new FormControl(''),
        });

    }

    private registerFilters() {
        this.filterService.register('objectIdFilter', (value, filter): boolean => {
            return this.filterUtilsService.objectFilterId(value, filter);
        });
        this.filterService.register('parentStatementFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['description', 'code'], filter);
        });
        this.filterService.register('periodStatementAsignationsFilter', (value, filter): boolean => {
            return this.filterUtilsService.periodStatementAsignationsFilter(value, filter);
        });
    }

    private loadItems() {
        this.statementService.getAll().subscribe({
            next: value => {
                this.items = value;
                this.parentStatementsItems = this.items.map(value1 => {
                    return this.statementToSelectItem(value1);
                });
                this.loadAreas();
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los Grupos de Población',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    private loadAreas() {
        this.areaService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
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
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las áreas',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
    }

    private loadPillars() {
        this.pillarService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    this.pillarsItems = value.map(value1 => {
                        const selectItem: SelectItem = {
                            label: this.codeShortDescriptionPipe.transform(value1),
                            value: value1
                        };
                        return selectItem;
                    });
                    this.loadSituations();
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los Grupos de Población',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
    }


    private loadSituations() {
        this.situationService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    this.situationsItems = value.map(value1 => {
                        const selectItem: SelectItem = {
                            label: this.codeShortDescriptionPipe.transform(value1),
                            value: value1
                        };
                        return selectItem;
                    });
                    this.loadPeriods();
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las situaciones',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
    }

    private loadPeriods() {
        this.periodService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    this.periodsItems = value.map(value1 => {
                        const selectItem: SelectItem = {
                            label: value1.year.toString(),
                            value: value1
                        };
                        return selectItem;
                    });
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los años',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'marco de resultados');
    }


    createItem() {
        this.parentStatementsItems = this.items.map(value => {
            return this.statementToSelectItem(value);
        });
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Statement();
        this.formItem.patchValue(newItem);
    }

    editItem(statement: Statement) {
        /*this.parentStatementsItems = this.items.filter(value => {
            return value.id !== statement.id;
        }).map(value => {
            return this.statementToSelectItem(value);
        });*/
        // obtengo los periods
        
        
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;

        const periodStatementAsignations: PeriodStatementAsignation[] =
            statement.periodStatementAsignations;
        const assignedPeriods: Period[] = statement.periodStatementAsignations.filter(value => {
            return value.state === EnumsState.ACTIVE;
        }).map(value => {
            return value.period;
        });


        this.formItem.patchValue(statement);
        this.formItem.get('periodStatementAsignations').patchValue(periodStatementAsignations);
        this.formItem.get('periods').patchValue(assignedPeriods);
        this.cd.detectChanges();
        this.onResultLevelChange(statement.areaType)
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
        let periodStatementAsignationsCasted = periodStatementAsignations as PeriodStatementAsignation[];

        if (periodStatementAsignations) {
            periodStatementAsignationsCasted.forEach(value => value.state = EnumsState.INACTIVE);
        } else {
            periodStatementAsignationsCasted = [];
        }
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
            this.statementService.update(statement)
                .subscribe({
                    next: () => {
                        this.cancelDialog();
                        this.loadItems();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Enunciado guardado exitosamente',
                            life: 3000
                        });
                    },
                    error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al actualizar el Grupo de Población',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
                });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.statementService.save(statement)
                .subscribe({
                    next: () => {
                        this.cancelDialog();
                        this.loadItems();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Enunciado guardado exitosamente',
                            life: 3000
                        });
                    },
                    error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al guardar el Grupo de Población',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
                });
        }
    }

    cancelDialog() {
        this.parentStatementsItems = this.items.map(value => {
            return this.statementToSelectItem(value);
        });
        this.showDialog = false;
        this.submitted = false;
        this.filterAreaList=[];
        this.parentStatementsItemsFiltered=[]
        this.formItem.get('parentStatement').enable();
        this.formItem.get('parentStatement').updateValueAndValidity();
    }

    statementToSelectItem(value: Statement): SelectItem {
        return {
            value: value,
            label: value.areaType + "(" + (value.periodStatementAsignations.map(value1 => {
                return value1.period.year
            }).join("-")) + ") " + value.code + ' - ' + value.description
        }
    }

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }

    importCatalog() {
        const {
            period,
            fileName,
            file
        } = this.importForm.value;
        const importFile: ImportFile = {
            period,
            fileName,
            file
        };

        this.statementService.importStatementsCatalog(importFile).subscribe({
            next: () => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Catálogo cargado correctamente',
                    life: 3000
                });
                this.loadItems();
                this.showDialogImport = false;
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al descargar la plantilla',
                    detail: err.error.message,
                    life: 3000
                });
            }
        })

    }

    downloadImportTemplate() {
        this.statementService.getStatementImportTemplate().subscribe({
            next: (response: HttpResponse<Blob>) => {
                this.utilsService.downloadFileResponse(response);
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al descargar la plantilla',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    initiateCatalogImport() {
        this.importForm.reset();
        this.showDialogImport = true;

    }

    cancelImportDialog() {
        this.showDialogImport = false;
        this.importForm.reset();
    }

    fileUploader(event: any) {
        const file = event.files[0];
        this.importForm.get('fileName').setValue(file.name);
        this.importForm.get('fileName').markAsTouched();
        // event.
        const fileReader = new FileReader();

        fileReader.readAsDataURL(file);
        // tslint:disable-next-line:only-arrow-functions
        fileReader.onload = () => {
            this.importForm.get('file').setValue(fileReader.result);
            this.importForm.get('file').markAsTouched();
        };
    }

    filterStatementsByPeriod(value) {
        let selectedPeriods: Period[] = value;
        const selectedAreaType=this.formItem.get("areaType").value
        if ( selectedAreaType && selectedPeriods && selectedPeriods.length > 0 ) {
            const parentStatementPeriodList= this.items
                .filter(statementTmp => {
                let periodIds = statementTmp.periodStatementAsignations.map(value2 => {
                    return value2.period.id
                });
                for (let selectedPeriod of selectedPeriods) {
                    return periodIds.includes(selectedPeriod.id);
                }
                return false;
            }).map(value1 => this.statementToSelectItem(value1));
            this.parentStatementsItemsFiltered=parentStatementPeriodList.filter(value1 => {
                if(selectedAreaType==='RESULTADO'){
                   return value1.value?.areaType === 'IMPACTO'
                }else if(selectedAreaType==='PRODUCTO'){
                    return value1.value?.areaType === 'RESULTADO'
                }else{
                    return value1
                }
            })
        } else {
            this.parentStatementsItemsFiltered = [];
        }

    }

    onResultLevelChange(areaType: string){
        this.selectedAreaType=areaType
        const areaList= JSON.parse(JSON.stringify(this.areasItems))
        this.filterAreaList=areaList.filter(value1 => value1.value.areaType === areaType)
        this.filterStatementsByPeriod(this.formItem.get("periods").value)
        if (areaType !== "IMPACTO") {
            this.formItem.get('parentStatement').enable();
            this.formItem.get('parentStatement').updateValueAndValidity();
        } else {
            this.formItem.get('parentStatement').patchValue(null);
            this.formItem.get('parentStatement').updateValueAndValidity();
            this.formItem.get('parentStatement').disable();
        }
    }
}
