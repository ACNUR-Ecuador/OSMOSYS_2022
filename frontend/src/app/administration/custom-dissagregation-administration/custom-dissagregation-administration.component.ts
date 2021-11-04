import {Component, Input, OnInit} from '@angular/core';
import {CustomDissagregation} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, MessageService} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {CustomDissagregationService} from '../../shared/services/custom-dissagregation.service';

@Component({
  selector: 'app-custom-dissagregation-administration',
  templateUrl: './custom-dissagregation-administration.component.html',
  styleUrls: ['./custom-dissagregation-administration.component.scss']
})
export class CustomDissagregationAdministrationComponent implements OnInit {
    items: CustomDissagregation[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    private states: string[];

    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private customDissagregationService: CustomDissagregationService
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'name', header: 'Tipo', type: ColumnDataType.text},
            {field: 'description', header: 'Subtipo', type: ColumnDataType.text},
            {field: 'controlTotalValue', header: 'Descripción', type: ColumnDataType.text},
            {field: 'state', header: 'Descripción Corta', type: ColumnDataType.text},
            {field: 'customDissagregationOptions', header: 'Estado', type: ColumnDataType.text}
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');

        this.formItem = this.fb.group({
            id: new FormControl(''),
            name: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
            controlTotalValue: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
            customDissagregationOptions: new FormControl('', Validators.required)
        });

        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
    }

    private loadItems() {
        this.customDissagregationService.getAll().subscribe(value => {
            this.items = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los desagregaciones',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    exportExcel() {
        import('xlsx').then(xlsx => {
            const itemsRenamed = this.utilsService.renameKeys(this.items, this.cols);
            const worksheet = xlsx.utils.json_to_sheet(itemsRenamed);
            const workbook = {Sheets: {data: worksheet}, SheetNames: ['data']};
            const excelBuffer: any = xlsx.write(workbook, {bookType: 'xlsx', type: 'array'});
            this.utilsService.saveAsExcelFile(excelBuffer, 'desagregaciones');
        });
    }


    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new CustomDissagregation();
        this.formItem.patchValue(newItem);
        console.log(this.formItem.value);
    }

    editItem(customDissagregation: CustomDissagregation) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(customDissagregation);
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            name,
            description,
            controlTotalValue,
            state,
            customDissagregationOptions
        }
            = this.formItem.value;
        const customDissagregation: CustomDissagregation = {
            id,
            name,
            description,
            controlTotalValue,
            state,
            customDissagregationOptions
        };
        if (customDissagregation.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.customDissagregationService.update(customDissagregation).subscribe(id => {
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
            this.customDissagregationService.save(customDissagregation).subscribe(id => {
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
