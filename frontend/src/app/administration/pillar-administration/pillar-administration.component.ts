// noinspection DuplicatedCode

import {Component, Input, OnInit} from '@angular/core';
import {Pillar} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {EnumsService} from '../../services/enums.service';
import {PillarService} from '../../services/pillar.service';
import {Table} from 'primeng/table';

@Component({
    selector: 'app-pillar-administration',
    templateUrl: './pillar-administration.component.html',
    styleUrls: ['./pillar-administration.component.scss']
})
export class PillarAdministrationComponent implements OnInit {
    items: Pillar[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    states: SelectItem[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private pillarService: PillarService
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'code', header: 'Código', type: ColumnDataType.numeric},
            {field: 'shortDescription', header: 'Descripción Corta', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');

        this.formItem = this.fb.group({
            id: new FormControl(''),
            code: new FormControl('', Validators.required),
            shortDescription: new FormControl('', Validators.required),
            description: new FormControl(''),
            state: new FormControl('', Validators.required)
        });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });

    }

    private loadItems() {
        this.pillarService.getAll().subscribe({
            next: value => {
                this.items = value;
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los pilares',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'pilares');
    }


    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Pillar();
        this.formItem.patchValue(newItem);
    }

    editItem(pillar: Pillar) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(pillar);
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            code,
            shortDescription,
            description,
            state
        }
            = this.formItem.value;
        const pillar: Pillar = {
            id,
            code,
            shortDescription,
            description,
            state
        };
        if (pillar.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.pillarService.update(pillar).subscribe({
                next: () => {
                    this.cancelDialog();
                    this.loadItems();
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al actualizar el pilar',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.pillarService.save(pillar).subscribe({
                next: () => {
                    this.cancelDialog();
                    this.loadItems();
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar el pilar',
                        detail: err.error.message,
                        life: 3000
                    });
                }
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
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }
}
