import {Component, Input, OnInit} from '@angular/core';
import {Organization} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {EnumsService} from '../../services/enums.service';
import {OrganizationService} from '../../services/organization.service';
import {Table} from 'primeng/table';

@Component({
    selector: 'app-organization-administration',
    templateUrl: './organization-administration.component.html',
    styleUrls: ['./organization-administration.component.scss']
})
export class OrganizationAdministrationComponent implements OnInit {
    items: Organization[];
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
        private organizationService: OrganizationService
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'code', header: 'Código', type: ColumnDataType.text},
            {field: 'acronym', header: 'Acrónimo', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text}
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');

        this.formItem = this.fb.group({
            id: new FormControl(''),
            code: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
            acronym: new FormControl('', Validators.required),
        });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });

    }

    private loadItems() {
        this.organizationService.getAll()
            .subscribe({
                next: value => {
                    this.items = value;
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los organizationos',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'implementadores');
    }

    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Organization();
        this.formItem.patchValue(newItem);
    }

    editItem(organization: Organization) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(organization);
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            code,
            state,
            description,
            acronym
        }
            = this.formItem.value;
        const organization: Organization = {
            id,
            code,
            state,
            description,
            acronym
        };
        if (organization.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.organizationService.update(organization)
                .subscribe({
                    next: () => {
                        this.cancelDialog();
                        this.loadItems();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Organización guardada exitosamente',
                            life: 3000
                        });
                    },
                    error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al actualizar la organización',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
                });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.organizationService.save(organization)
                .subscribe({
                    next: () => {
                        this.cancelDialog();
                        this.loadItems();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Organización guardada exitosamente',
                            life: 3000
                        });
                    },
                    error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al guardar la organización',
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
