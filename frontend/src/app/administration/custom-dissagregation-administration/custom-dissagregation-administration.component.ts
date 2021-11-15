import {Component, Input, OnInit} from '@angular/core';
import {CustomDissagregation, CustomDissagregationOption, Marker} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, MessageService} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {CustomDissagregationService} from '../../shared/services/custom-dissagregation.service';
import {MarkerService} from '../../shared/services/marker.service';

@Component({
    selector: 'app-custom-dissagregation-administration',
    templateUrl: './custom-dissagregation-administration.component.html',
    styleUrls: ['./custom-dissagregation-administration.component.scss']
})
export class CustomDissagregationAdministrationComponent implements OnInit {
    items: CustomDissagregation[];
    // itemsOptions: CustomDissagregationOption[];
    cols: ColumnTable[];
    colOptions: ColumnTable[];
    showDialog = false;
    showDialogOption = false;
    private submitted = false;
    formItem: FormGroup;
    formOptionItem: FormGroup;
    private states: string[];

    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    // tslint:disable-next-line:variable-name
    _selectedColumnsOptions: ColumnTable[];
    markers: Marker[];
    selectedMarkers: Marker[] = [];

    sourceCities: any[];

    targetCities: any[];

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private customDissagregationService: CustomDissagregationService,
        private markerService: MarkerService
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

        this.colOptions = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'name', header: 'Nombre', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
            {field: 'markers', header: 'Marcadores', type: ColumnDataType.text},
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');
        this._selectedColumnsOptions = this.colOptions.filter(value => value.field !== 'id');

        this.formItem = this.fb.group({
            id: new FormControl(''),
            name: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
            controlTotalValue: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
            customDissagregationOptions: this.fb.array([])
        });

        this.formOptionItem = this.fb.group({
            id: new FormControl(''),
            name: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
            markers: this.fb.array([])
        });

        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
    }

    private loadItems() {
        this.customDissagregationService.getAll().subscribe(value => {
            this.items = value;
            this.loadMarkers();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los desagregaciones',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    private loadMarkers() {
        this.markerService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.markers = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los marcadores',
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
        this.formItem.get('customDissagregationOptions').patchValue(newItem.customDissagregationOptions);
        // this.itemsOptions = [];

        console.log(this.formItem.value);
    }


    editItem(customDissagregation: CustomDissagregation) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(customDissagregation);
        // this.itemsOptions = customDissagregation.customDissagregationOptions;
    }

    createOption() {
        const op1 = new CustomDissagregationOption();
        this.showDialogOption = true;
        this.formOptionItem.patchValue(op1);
        console.log(this.formOptionItem);
        console.log(this.formOptionItem.get('markers').value);
    }

    editOption(customDissagregationOption: CustomDissagregationOption) {
        if (!customDissagregationOption.markers) {
            customDissagregationOption.markers = [];
        }
        this.formOptionItem.patchValue(customDissagregationOption);
        this.showDialogOption = true;
        // this.itemsOptions.push(op1);
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
                    summary: 'Error al actualizar la desagregación',
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
                    summary: 'Error al guardar la desagregación',
                    detail: error.error.message,
                    life: 3000
                });
            });
        }

    }

    saveOptionItem() {

        this.formItem.get('customDissagregationOptions').value.push(this.formOptionItem);
        this.showDialogOption = false;

        console.log(this.formOptionItem);
        console.log(this.formItem);
    }

    cancelDialog() {
        this.showDialog = false;
        this.submitted = false;
    }


    cancelDialogOption() {
        console.log(this.formOptionItem);
        Object.keys(this.formOptionItem.controls).forEach(key => {
            if (!this.formOptionItem.get(key).valid) {
                console.log(key);
                console.log(this.formOptionItem.get(key));
            }
        });
        // this.showDialogOption = false;
    }

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }

    @Input() get selectedColumnsOptions(): any[] {
        return this._selectedColumnsOptions;
    }

    set selectedColumnsOptions(val: any[]) {
        // restore original order
        this._selectedColumnsOptions = this.cols.filter(col => val.includes(col));
    }


}
