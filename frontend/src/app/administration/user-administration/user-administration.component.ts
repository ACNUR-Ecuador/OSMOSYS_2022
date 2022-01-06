import {Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../shared/services/user.service';
import {Role, User} from '../../shared/model/User';
import {FilterService, MessageService, SelectItem} from 'primeng/api';
import {EnumsService} from '../../shared/services/enums.service';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {OrganizationService} from '../../shared/services/organization.service';
import {OfficeService} from '../../shared/services/office.service';
import {OfficeOrganizationPipe} from '../../shared/pipes/officeOrganization.pipe';
import {UtilsService} from '../../shared/services/utils.service';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';
import {RolesListPipe} from '../../shared/pipes/roles-list.pipe';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
    selector: 'app-user-administration',
    templateUrl: './user-administration.component.html',
    styleUrls: ['./user-administration.component.scss']
})
export class UserAdministrationComponent implements OnInit {

    items: User[];

    // options
    private states: SelectItem[];
    private offices: SelectItem[];
    private organizations: SelectItem[];
    private roles: SelectItem[];

    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    cols: ColumnTable[];

    // form
    formItem: FormGroup;
    private officesActive: SelectItem[];
    private organizationsActive: SelectItem[];
    showDialog = false;

    constructor(
        private messageService: MessageService,
        private fb: FormBuilder,
        private enumsService: EnumsService,
        public utilsService: UtilsService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private organizationService: OrganizationService,
        private officeService: OfficeService,
        public officeOrganizationPipe: OfficeOrganizationPipe,
        public rolesListPipe: RolesListPipe,
        private userService: UserService
    ) {
    }

    ngOnInit(): void {
        this.loadOptions();
        this.loadItems();
        this.createColumns();
        this.registerFilters();
        this.createForm();
    }

    loadItems() {
        this.userService.getAllUser().subscribe(value => {
            this.items = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los usuarios',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    createColumns() {
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'name', header: 'Nombre', type: ColumnDataType.text},
            {field: 'username', header: 'Nombre de usuario', type: ColumnDataType.text},
            {field: 'email', header: 'Correo', type: ColumnDataType.text},
            {field: 'organization', header: 'Organización', type: ColumnDataType.text, pipeRef: this.officeOrganizationPipe},
            {field: 'office', header: 'Oficina', type: ColumnDataType.text, pipeRef: this.officeOrganizationPipe},
            {field: 'roles', header: 'Permisos', type: ColumnDataType.text, pipeRef: this.rolesListPipe},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
        ];
        const hiddenColumns: string[] = ['id'];
        this._selectedColumns = this.cols.filter(value => !hiddenColumns.includes(value.field));
    }

    createForm() {
        this.formItem = this.fb.group({
            id: new FormControl(''),
            name: new FormControl('', Validators.required),
            username: new FormControl('', Validators.required),
            email: new FormControl('', [Validators.required, Validators.email]),
            organization: new FormControl('', Validators.required),
            office: new FormControl(''),
            roles: new FormControl(''),
            state: new FormControl(''),
            roleTypes: new FormControl('', Validators.required)
        });
    }

    loadOptions() {
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los estados',
                detail: error.error.message,
                life: 3000
            });
        });
        this.enumsService.getByType(EnumsType.RoleType).subscribe(value => {
            this.roles = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los estados',
                detail: error.error.message,
                life: 3000
            });
        });
        this.officeService.getAll().subscribe(value => {
            this.offices = value.map(value1 => {
                return {
                    label: this.officeOrganizationPipe.transform(value1),
                    value: value1,
                    disabled: value1.state !== EnumsState.ACTIVE
                } as SelectItem;
            }).sort((a, b) => {
                return a.label.localeCompare(b.label);
            });
            this.officesActive = this.offices.filter(value1 => {
                return value1.value.state === EnumsState.ACTIVE;
            });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las oficinas',
                detail: error.error.message,
                life: 3000
            });
        });
        this.organizationService.getAll().subscribe(value => {
            this.organizations = value.map(value1 => {
                return {
                    label: this.officeOrganizationPipe.transform(value1),
                    value: value1,
                    disabled: value1.state !== EnumsState.ACTIVE
                } as SelectItem;
            }).sort((a, b) => {
                return a.label.localeCompare(b.label);
            });
            this.organizationsActive = this.organizations.filter(value1 => {
                return value1.value.state === EnumsState.ACTIVE;
            });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las organizaciones',
                detail: error.error.message,
                life: 3000
            });
        });

    }

    private registerFilters() {
        this.filterService.register('officeOrganizationFilter', (value, filter): boolean => {
            return this.filterUtilsService.objectFilterId(value, filter);
        });
        this.filterService.register('rolesFilter', (value, filter): boolean => {
            return this.filterUtilsService.roleListFilterId(value, filter);
        });
    }

    createItem() {
        this.utilsService.resetForm(this.formItem);
        const user: User = new User();
        user.state = EnumsState.ACTIVE;
        user.roles = [];
        this.formItem.patchValue(user);
        this.formItem.get('roleTypes').patchValue([]);
        this.formItem.get('username').enable();
        this.showDialog = true;
        console.log(this.formItem.value);
    }

    editItem(user: User) {
        this.utilsService.resetForm(this.formItem);
        this.showDialog = true;
        this.formItem.patchValue(user);
        const roleTypes: string[] = user.roles
            .filter(value => value.state === EnumsState.ACTIVE)
            .map(value => {
                return value.name;
            });
        this.formItem.get('roleTypes').patchValue(roleTypes);
        this.formItem.get('username').disable();
        console.log(this.formItem.value);
    }

    exportExcel() {

    }

    cancelDialog() {
        this.showDialog = false;
        this.messageService.clear();
    }

    saveItem() {
        this.messageService.clear();
        const {
            id,
            name,
            username,
            email,
            organization,
            office,
            roles,
            state,
            roleTypes
        } = this.formItem.value;
        // procesos los roles
        const rolesProceced: Role[] = roles as Role[];
        // los que ya no tengo desactivo
        rolesProceced.forEach(value => {
            if (!roleTypes.includes(value.name)) {
                value.state = EnumsState.INACTIVE;
            }
        });
        // los que no estaban aggrego
        const originalRoles = (roles as Role[]).map(value => {
            return value.name;
        });
        const newRoleTypes = (roleTypes as string[])
            .filter(value => {
                return !originalRoles.includes(value);
            });
        newRoleTypes.forEach(value => {
            const role: Role = {
                id: null,
                state: EnumsState.ACTIVE,
                name: value
            };
            rolesProceced.push(role);
        });

        const user: User = {
            id,
            name,
            username,
            email,
            organization,
            office,
            roles: rolesProceced,
            state
        };

        console.log(user);
        if (user.id) {
            this.userService.updateUser(user)
                .subscribe(value => {
                    this.showDialog = false;
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Guardado con éxito',
                        life: 3000
                    });
                    this.loadItems();
                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar el usuario',
                        detail: error.error.message,
                        life: 3000
                    });
                });
        } else {
            this.userService.createUser(user)
                .subscribe(value => {
                    this.showDialog = false;
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Guardado con éxito',
                        life: 3000
                    });
                    this.loadItems();
                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar el usuario',
                        detail: error.error.message,
                        life: 3000
                    });
                });
        }
    }

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }


}
