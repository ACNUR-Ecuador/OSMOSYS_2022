import {Component, OnInit} from '@angular/core';
import {ConfirmationService, MessageService} from 'primeng/api';
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
    private areaTypes: string[];
    private states: string[];

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
            const worksheet = xlsx.utils.json_to_sheet(this.items);
            const workbook = {Sheets: {data: worksheet}, SheetNames: ['data']};
            const excelBuffer: any = xlsx.write(workbook, {bookType: 'xlsx', type: 'array'});
            this.utilsService.saveAsExcelFile(excelBuffer, 'products');
        });
    }

    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Area();
        this.formItem.patchValue(newItem);
        console.log(this.formItem.value);
    }

    editItem(area: Area) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(area);
        console.log(this.formItem.value);
    }

    disableItem(area: Area) {

    }


    saveItem() {
        console.log('saving');
        console.log(this.formItem.value);
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

}
