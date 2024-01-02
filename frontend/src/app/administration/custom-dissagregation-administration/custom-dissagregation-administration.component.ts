import {Component, Input, OnInit} from '@angular/core';
import {CustomDissagregation, CustomDissagregationOption} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, FilterService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {EnumsService} from '../../services/enums.service';
import {CustomDissagregationService} from '../../services/custom-dissagregation.service';
import {CustomDissagregationOptionsListPipe} from '../../shared/pipes/custom-dissagregation-options-list.pipe';
import {FilterUtilsService} from '../../services/filter-utils.service';
import {Table} from 'primeng/table';
import {BooleanYesNoPipe} from '../../shared/pipes/boolean-yes-no.pipe';

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
    states: SelectItem[];

    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    // tslint:disable-next-line:variable-name
    _selectedColumnsOptions: ColumnTable[];


    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private enumsService: EnumsService,
        private customDissagregationService: CustomDissagregationService,
        private customDissagregationOptionsListPipe: CustomDissagregationOptionsListPipe,
        private booleanYesNoPipe: BooleanYesNoPipe
    ) {
    }

    ngOnInit(): void {


        this.loadItems();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'name', header: 'Nombre', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            {
                field: 'controlTotalValue',
                header: 'Control de valores totales',
                type: ColumnDataType.boolean,
                pipeRef: this.booleanYesNoPipe
            },
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
            {
                field: 'customDissagregationOptions',
                header: 'Desagregaciones',
                type: ColumnDataType.text,
                pipeRef: this.customDissagregationOptionsListPipe
            }
        ];
        this.registerFilters();

        this.colOptions = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'name', header: 'Nombre', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
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
        });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
    }

    private registerFilters() {
        this.filterService.register('dissagegationsFilters', (value, filter): boolean => {
            return this.filterUtilsService.customDissagregationName(value, filter);
        });
    }

    private loadItems() {
        this.customDissagregationService.getAll()
            .subscribe({
                next: value => {
                    this.items = value;
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los desagregaciones',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
    }

    // noinspection DuplicatedCode



    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'desagregaciones_personalizadas');
    }


    createItem() {

        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new CustomDissagregation();
        this.formItem.patchValue(newItem);
        this.formItem.get('customDissagregationOptions').patchValue(newItem.customDissagregationOptions);
    }


    editItem(customDissagregation: CustomDissagregation) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(customDissagregation);
        for (const customDissagregationOption of customDissagregation.customDissagregationOptions) {
            this.formItem.get('customDissagregationOptions').value.push(customDissagregationOption);
        }
    }

    createOption() {

        const op1 = new CustomDissagregationOption();
        this.utilsService.resetForm(this.formOptionItem);
        this.formOptionItem.patchValue(op1);
        this.showDialogOption = true;
    }

    editOption(customDissagregationOption: CustomDissagregationOption) {
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
            this.customDissagregationService.update(customDissagregation)
                .subscribe({
                    next: () => {
                        this.cancelDialog();
                        this.loadItems();
                    },
                    error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al actualizar la desagregación',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
                });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.customDissagregationService.save(customDissagregation)
                .subscribe({
                    next: () => {
                        this.cancelDialog();
                        this.loadItems();
                    },
                    error: err => {

                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al guardar la desagregación',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
                });
        }
    }

    saveOptionItem() {
        this.showDialogOption = false;
        const {
            id,
            name,
            description,
            state,
        }
            =
            this.formOptionItem.value;

        const option = {
            id,
            name,
            description,
            state,
        };

        let options = this.formItem.get('customDissagregationOptions').value as CustomDissagregationOption[];
        options = options.filter(value => {
            return option.id === null || value.id !== option.id;
        });
        options.push(option);
        this.formItem.get('customDissagregationOptions').reset();
        options.forEach(value => {
            this.formItem.get('customDissagregationOptions').value.push(value);
        });

    }

    cancelDialog() {
        this.showDialog = false;
        this.submitted = false;
    }


    cancelDialogOption() {
        this.showDialogOption = false;
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
