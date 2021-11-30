import {Component, Input, OnInit} from '@angular/core';
import {Period} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {PeriodService} from '../../shared/services/period.service';

@Component({
    selector: 'app-period-administration',
    templateUrl: './period-administration.component.html',
    styleUrls: ['./period-administration.component.scss']
})
export class PeriodAdministrationComponent implements OnInit {
    items: Period[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    private states: SelectItem[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private periodService: PeriodService
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'year', header: 'Año', type: ColumnDataType.numeric},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');

        this.formItem = this.fb.group({
            id: new FormControl(''),
            year: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
        });

        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });

    }

    private loadItems() {
        this.periodService.getAll().subscribe(value => {
            this.items = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los periodos',
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
            this.utilsService.saveAsExcelFile(excelBuffer, 'periodos');
        });
    }

    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Period();
        this.formItem.patchValue(newItem);
        console.log(this.formItem.value);
    }

    editItem(period: Period) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(period);
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            year,
            state
        }
            = this.formItem.value;
        const period: Period = {
            id,
            year,
            state
        };
        if (period.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.periodService.update(period).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al actualizar el periodo',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.periodService.save(period).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al guardar el periodo',
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
