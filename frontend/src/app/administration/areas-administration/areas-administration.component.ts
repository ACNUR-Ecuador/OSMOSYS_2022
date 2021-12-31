import {Component, Input, OnInit} from '@angular/core';
import {ConfirmationService, MessageService, SelectItem} from 'primeng/api';
import {AreaService} from '../../shared/services/area.service';
import {Area} from '../../shared/model/OsmosysModel';
import {UtilsService} from '../../shared/services/utils.service';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {EnumsService} from '../../shared/services/enums.service';

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
    private areaTypes: SelectItem[];
    private states: SelectItem[];
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
            {field: 'id', header: 'id', type: ColumnDataType.numeric},
            {field: 'code', header: 'Código', type: ColumnDataType.text},
            {field: 'shortDescription', header: 'Nombre', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            {field: 'areaType', header: 'Tipo', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');

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
        this.areaService.getAll().subscribe(value => {
            this.items = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las áreas',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    exportExcel() {
        import('xlsx').then(xlsx => {
            const headers = this.cols.map(value => value.header);
            const itemsRenamed = this.utilsService.renameKeys(this.items, this.cols);
            const worksheet = xlsx.utils.json_to_sheet(itemsRenamed);
            const workbook = {Sheets: {data: worksheet}, SheetNames: ['data']};

            const excelBuffer: any = xlsx.write(workbook, {bookType: 'xlsx', type: 'array'});
            this.utilsService.saveAsExcelFile(excelBuffer, 'areas');
        });
    }


    renameKey(obj, oldKey, newKey) {
        obj[newKey] = obj[oldKey];
        delete obj[oldKey];
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
            this.areaService.update(area).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar las áreas',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.areaService.save(area).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar las áreas',
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
