import {Component, Input, OnInit} from '@angular/core';
import {MenuItemBackend, Organization} from "../../shared/model/OsmosysModel";
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from "../../shared/model/UtilsModel";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MessageService, SelectItem} from "primeng/api";
import {UtilsService} from "../../services/utils.service";
import {EnumsService} from "../../services/enums.service";
import {MenuItemsService} from "../../services/menu-items.service";
import {Table} from "primeng/table";
import {OrganizationService} from "../../services/organization.service";

@Component({
    selector: 'app-menu-items',
    templateUrl: './menu-items.component.html',
    styleUrls: ['./menu-items.component.scss']
})
export class MenuItemsComponent implements OnInit {
    items: MenuItemBackend[];
    cols: ColumnTable[];
    showDialog = false;
    formItem: FormGroup;
    states: SelectItem[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];

    organizationSelectItem: SelectItem<Organization>[];
    parentItems: SelectItem<MenuItemBackend>[];
    rolesItems: SelectItem<string>[];/*=
        [
            {label:'',value:'SUPER_ADMINISTRADOR' },
            {label:'',value:'ADMINISTRADOR' },
            {label:'',value:'EJECUTOR_PROYECTOS' },
            {label:'',value:'MONITOR_PROYECTOS' },
            {label:'',value:'EJECUTOR_ID' },
            {label:'',value:'MONITOR_ID' },
            {label:'',value:'PUNTO_FOCAL' },
            {label:'',value:'ADMINISTRADOR_OFICINA' },
        ];*/

    constructor(private messageService: MessageService,
                private fb: FormBuilder,
                public utilsService: UtilsService,
                public menuItemsService: MenuItemsService,
                public organizationService: OrganizationService,
                private enumsService: EnumsService) {
    }

    ngOnInit(): void {


        this.loadItems();
        this.cols = [
            {field: 'id', header: 'id', type: ColumnDataType.numeric},
            {field: 'label', header: 'Etiqueta', type: ColumnDataType.text},
            {field: 'parent.label', header: 'Padre', type: ColumnDataType.text},
            {field: 'url', header: 'url', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
            {field: 'icon', header: 'Ícono', type: ColumnDataType.text},
            {field: 'assignedRoles', header: 'Roles', type: ColumnDataType.text},
            {field: 'powerBi', header: 'Es PowerBi', type: ColumnDataType.boolean},
            {field: 'restricted', header: 'Es Restringido', type: ColumnDataType.boolean},
            {field: 'openInNewTab', header: 'Abrir en otra ventana', type: ColumnDataType.boolean},
            {field: 'order', header: 'Orden', type: ColumnDataType.numeric},
            {field: 'organizations', header: 'Organizaciones', type: ColumnDataType.text},


        ];
        const hiddenColumns: string[] = ['id', 'powerBi', 'restricted', 'order', 'organizations', 'Roles'];
        this._selectedColumns = this.cols.filter(value => !hiddenColumns.includes(value.field));

        this.formItem = this.fb.group({
            id: new FormControl(''),
            label: new FormControl('', Validators.required),
            url: new FormControl(''),
            state: new FormControl('', Validators.required),
            icon: new FormControl(''),
            assignedRoles: new FormControl(''),
            powerBi: new FormControl('', Validators.required),
            restricted: new FormControl('', Validators.required),
            order: new FormControl('', Validators.required),
            organizations: new FormControl(''),
            parent: new FormControl(''),
            openInNewTab: new FormControl('', Validators.required)
        });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
        this.enumsService.getByType(EnumsType.RoleType).subscribe(value => {
            this.rolesItems = value;
        });

    }

    private loadItems() {
        this.organizationService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    this.organizationSelectItem =
                        value.map(value1 => {
                            let item: SelectItem = {
                                value: value1,
                                label: value1.acronym
                            };
                            return item;
                        }).sort((a, b) => a.label.localeCompare(b.label));
                }, error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las organizaciones',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
        this.menuItemsService.getAll().subscribe({
            next: value => {
                this.items = value;
                this.parentItems = value.map(value1 => {
                    let item: SelectItem = {
                        value: value1,
                        label: value1.label
                    };
                    return item;
                });
                this.parentItems.push({
                    value: null,
                    label: null
                });
                this.parentItems.sort((a, b) => a.label.localeCompare(b.label));
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los menus',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.showDialog = true;
        const newItem = new MenuItemBackend();

        this.formItem.patchValue(newItem);
    }

    editItem(menuItem: MenuItemBackend) {
        this.utilsService.resetForm(this.formItem);
        this.showDialog = true;
        this.formItem.patchValue(menuItem);
        //this.formItem.get('id').patchValue(menuItem.id);
    }

    saveItem() {
        this.messageService.clear();
        this.messageService.clear();
        const {
            id,
            label,
            url,
            state,
            icon,
            assignedRoles,
            powerBi,
            restricted,
            order,
            organizations,
            parent,
            openInNewTab
        } = this.formItem.value;

        const item: MenuItemBackend = {
            id,
            label,
            url,
            state,
            icon,
            assignedRoles,
            powerBi,
            restricted,
            order,
            organizations,
            parent,
            openInNewTab
        };

        // item.id = this.formItem.get('id').value;
        if (item.id) {
            this.menuItemsService.update(item).subscribe({
                next: () => {
                    this.cancelDialog();
                    this.loadItems();
                }, error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar',
                        detail: err.error.message,
                        life: 3000
                    });


                }
            });
        } else {
            this.menuItemsService.save(item).subscribe({
                next: () => {
                    this.cancelDialog();
                    this.loadItems();
                }, error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar',
                        detail: err.error.message,
                        life: 3000
                    });


                }
            });
        }

    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'areas');
    }

    cancelDialog() {
        this.showDialog = false;


    }


    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }
}
