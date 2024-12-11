import {Component, Input, OnInit} from '@angular/core';
import {Area} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {EnumsService} from '../../services/enums.service';
import {Table} from 'primeng/table';
import {AreaService} from '../../services/area.service';

@Component({
    selector: 'app-areas-administration',
    templateUrl: './areas-administration.component.html',
    styleUrls: ['./areas-administration.component.scss']
})
export class AreasAdministrationComponent implements OnInit {
    items: Area[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    areaTypes: SelectItem[];
    states: SelectItem[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private areaService: AreaService
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.cols = [
            {field: 'id', header: 'id', type: ColumnDataType.numeric, },
            {field: 'code', header: 'Código', type: ColumnDataType.text},
            {field: 'shortDescription', header: 'Nombre corto', type: ColumnDataType.text},
            {field: 'description', header: 'Nombre completo', type: ColumnDataType.text},
            {field: 'areaType', header: 'Tipo', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');
        this._selectedColumns = this._selectedColumns.filter(value => value.field !== 'state');

        this.formItem = this.fb.group({
            id: new FormControl(''),
            code: new FormControl('', Validators.required),
            shortDescription: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
            areaType: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
            definition: new FormControl('')
        });
        this.enumsService.getByType(EnumsType.AreaType).subscribe(value => {
            this.areaTypes = value;
        });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
    }

    private loadItems() {
        this.areaService.getAll().subscribe({
            next: value => {
                this.items = value;
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

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'areas');
    }

    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Area();
        this.formItem.patchValue(newItem);
    }

    editItem(area: Area) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(area);
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            code,
            shortDescription,
            description,
            areaType,
            state,
            definition
        }
            = this.formItem.value;
        const area: Area = {
            id,
            code,
            shortDescription,
            description,
            areaType,
            state,
            definition
        };
        if (area.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.areaService.update(area)
                .subscribe({
                    next: () => {
                        this.cancelDialog();
                        this.loadItems();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Área guardado exitosamente',
                            life: 3000
                        });
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
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.areaService.save(area)
                .subscribe({
                    next: () => {
                        this.cancelDialog();
                        this.loadItems();
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
