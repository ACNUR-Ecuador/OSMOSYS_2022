import {Component, Input, OnInit} from '@angular/core';
import {Marker} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {MarkerService} from '../../shared/services/marker.service';
import {Table} from 'primeng/table';

@Component({
    selector: 'app-marker-administration',
    templateUrl: './marker-administration.component.html',
    styleUrls: ['./marker-administration.component.scss']
})
export class MarkerAdministrationComponent implements OnInit {
    items: Marker[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    states: SelectItem[];
    types: SelectItem[];
    subTypes: string[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private markerService: MarkerService
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'type', header: 'Tipo', type: ColumnDataType.text},
            {field: 'subType', header: 'Subtipo', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            {field: 'shortDescription', header: 'Descripción Corta', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text}
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');

        this.formItem = this.fb.group({
            id: new FormControl(''),
            type: new FormControl('', Validators.required),
            subType: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
            shortDescription: new FormControl('', Validators.required)
        });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
        this.enumsService.getByType(EnumsType.MarkerType).subscribe(value => {
            this.types = value;
        });
    }

    private loadItems() {
        this.markerService.getAll().subscribe(value => {
            this.items = value;
            const subTypesTotal = this.items.map(value1 => {
                return value1.subType;
            });
            this.subTypes = [...new Set(subTypesTotal)];
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los marcadores',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'marcadores');
    }


    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Marker();
        this.formItem.patchValue(newItem);
    }

    editItem(marker: Marker) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(marker);
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            type,
            subType,
            state,
            description,
            shortDescription
        }
            = this.formItem.value;
        const marker: Marker = {
            id,
            type,
            subType,
            state,
            description,
            shortDescription
        };
        if (marker.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.markerService.update(marker).subscribe(() => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al actualizar el marcador',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.markerService.save(marker).subscribe(() => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al guardar el marcador',
                    detail: error.error.message,
                    life: 3000
                });
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
