import { Component, Input, OnInit } from '@angular/core';
import { Role, User } from '../../shared/model/User';
import { FilterService, MessageService, SelectItem } from 'primeng/api';
import { ColumnDataType, ColumnTable, EnumsState, EnumsType } from '../../shared/model/UtilsModel';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { EnumsService } from '../../services/enums.service';
import { UtilsService } from '../../services/utils.service';
import { FilterUtilsService } from '../../services/filter-utils.service';
import { OrganizationService } from '../../services/organization.service';
import { OfficeOrganizationPipe } from '../../shared/pipes/office-organization.pipe';
import { RolesListPipe } from '../../shared/pipes/roles-list.pipe';
import { Table } from 'primeng/table';
import { OfficeService } from '../../services/office.service';
import { Organization } from "../../shared/model/OsmosysModel";

@Component({
    selector: 'app-user-administration',
    templateUrl: './user-administration.component.html',
    styleUrls: ['./user-administration.component.scss']
})
export class UserAdministrationComponent implements OnInit {

    items: User[];

    // options
    states: SelectItem[];
    offices: SelectItem[];
    organizations: SelectItem[];
    roles: SelectItem[];
    assignableRoles: SelectItem[];
    selectRoles: SelectItem[];

    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    cols: ColumnTable[];

    // form
    formItem: FormGroup;
    officesActive: SelectItem[];
    organizationsActive: SelectItem[];
    showDialog = false;
    showPermisions= false;


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
        this.userService.getAllUser().subscribe({
            next: value => {
                if(this.userService.hasAnyRole(['SUPER_ADMINISTRADOR'])){
                    this.items=value
                }else if(this.userService.hasAnyRole(['ADMINISTRADOR_REGIONAL'])){
                    this.items=value.filter(user =>{
                        return user.roles.every(role => role.name !== 'SUPER_ADMINISTRADOR');
                    })
                }else if(this.userService.hasAnyRole(['ADMINISTRADOR_LOCAL'])){
                    this.items=value.filter(user =>{
                        return user.roles.every(role => role.name !== 'SUPER_ADMINISTRADOR' &&  role.name !== 'ADMINISTRADOR_REGIONAL');
                    })
                }else{
                    this.items=value.filter(user =>{
                       return user.roles.every(role => role.name !== 'SUPER_ADMINISTRADOR' &&  
                        role.name !== 'ADMINISTRADOR_REGIONAL' && role.name !== 'ADMINISTRADOR_LOCAL');
                   })
                }
                
            },
            error: err => this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los usuarios',
                detail: err.error.message,
                life: 3000
            })
        });
    }

    createColumns() {
        this.cols = [
            { field: 'id', header: 'Id', type: ColumnDataType.numeric },
            { field: 'name', header: 'Nombre', type: ColumnDataType.text },
            { field: 'username', header: 'Username', type: ColumnDataType.text },
            { field: 'email', header: 'Correo', type: ColumnDataType.text },
            {
                field: 'organization',
                header: 'Organización',
                type: ColumnDataType.text,
                pipeRef: this.officeOrganizationPipe
            },
            { field: 'office', header: 'Oficina/Unidad', type: ColumnDataType.text, pipeRef: this.officeOrganizationPipe },
            { field: 'roles', header: 'Permisos', type: ColumnDataType.text, pipeRef: this.rolesListPipe },
            { field: 'state', header: 'Estado', type: ColumnDataType.text },
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
            roleTypes: new FormControl('')
        });
    }

    loadOptions() {
        this.enumsService.getByType(EnumsType.State).subscribe({
            next: value => this.states = value,
            error: error => this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los estados',
                detail: error.error.message,
                life: 3000
            })
        });

        this.enumsService.getByType(EnumsType.RoleType).subscribe({
            next: value => {
                this.roles = value
                if (this.userService.hasAnyRole(['SUPER_ADMINISTRADOR'])) {
                    this.roles = this.roles
                } else if (this.userService.hasAnyRole(['ADMINISTRADOR_REGIONAL'])) {
                    this.roles = this.roles.filter(value1 => value1.value !== 'SUPER_ADMINISTRADOR')
                } else if (this.userService.hasAnyRole(['ADMINISTRADOR_LOCAL'])) {
                    this.roles = this.roles.filter(value1 => value1.value !== 'SUPER_ADMINISTRADOR'
                        && value1.value !== 'ADMINISTRADOR_REGIONAL'
                    )
                } else {
                    this.roles = this.roles.filter(value1 => value1.value !== 'SUPER_ADMINISTRADOR'
                        && value1.value !== 'ADMINISTRADOR_REGIONAL' && value1.value !== 'ADMINISTRADOR_LOCAL'
                    )
                }
                this.selectRoles = JSON.parse(JSON.stringify(this.roles));
                this.selectRoles.forEach(value1 => value1.disabled = false)
                
                this.roles.forEach(value1 => {
                        if (value1.value === 'PUNTO_FOCAL' || value1.value === 'ADMINISTRADOR_OFICINA' || value1.value === 'RESULT_MANAGER'
                                            || value1.value === 'SUPERVISOR_REPORTE_SOCIO' || value1.value === 'SUPERVISOR_REPORTE_ID'
                        ) {
                            value1.disabled = true;
                       
                        } else {
                            value1.disabled = false;
                        }
    
    
                });
                
                const rolDescriptions = new Map()
                rolDescriptions.set('SUPER_ADMINISTRADOR','Configura y administra cualquier aspecto del sistema.')
                rolDescriptions.set('ADMINISTRADOR_REGIONAL','Configura aspectos del sistema a nivel regional, global y de operaciones, como: Parámetros de configuración, Áreas, Grupos Poblacionales, Oficina/Unidad, Implementadores, Tags, Desagregaciones personalizadas, Usuarios, Auditoría, Indicadores de producto, Envío masivo de correos, Bloqueo masivo de indicadores, Menús de tableros.')
                rolDescriptions.set('ADMINISTRADOR_LOCAL','Configura aspectos del sistema que son específicos de su operación, como: Oficina/Unidad, Implementadores, Tags, Desagregaciones personalizadas, Usuarios, Auditoría, Indicadores de producto, Envío masivo de correos, Bloqueo masivo de indicadores, Menús de tableros.')
                rolDescriptions.set('RESULT_MANAGER','Responsable de monitorear y revisar los indicadores, asegurando la calidad y verificación de los datos, sin poder reportar ni corregir información.')
                rolDescriptions.set('MONITOR_ID','Puede ver los datos de todos los proyectos y de implementación directa, sin capacidad para reportar ni corregir información.')
                rolDescriptions.set('PUNTO_FOCAL','Da seguimiento a los proyectos como responsable, recibe notificaciones, revisa y corrige datos en consulta con el socio.')
                rolDescriptions.set('SUPERVISOR_REPORTE_SOCIO','Supervisa los reportes del socio, recibe notificaciones, revisa y corrige datos en consulta con la persona responsable del reporte.')
                rolDescriptions.set('EJECUTOR_PROYECTOS','Responsable de reporte del socio, reporta los valores para los indicadores del acuerdo')
                rolDescriptions.set('MONITOR_PROYECTOS','Puede ver los datos específicos de su proyecto, sin capacidad para reportar ni corregir información.')
                rolDescriptions.set('ADMINISTRADOR_OFICINA','Puede reportar en colaboración con la persona responsable para las implementaciones donde es jefe de Oficina/Unidad.')
                rolDescriptions.set('SUPERVISOR_REPORTE_ID','Recibe notificaciones sobre el cumplimiento de los reportes, revisa y corrige datos en consulta con la persona responsable cuando sea necesario.')
                rolDescriptions.set('EJECUTOR_ID','Responsable de reporte de implementación directa, reporta los valores para los indicadores relacionados a sus actividades.')

                this.roles.forEach(value1 => {
                        if(rolDescriptions.get(value1.value)){
                            value1.title=rolDescriptions.get(value1.value)
                        }else{
                            value1.title=""
                        }
                       
    
                });
                  
                this.assignableRoles = JSON.parse(JSON.stringify(this.roles));

            },
            error: error => this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los estados',
                detail: error.error.message,
                life: 3000
            })
        });

        this.officeService.getAll().subscribe({
            next: value => {
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
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar las oficinas',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });

        this.organizationService.getAll()
            .subscribe({
                next: value => {
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
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las organizaciones',
                        detail: err.error.message,
                        life: 3000
                    });
                }
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
        this.formItem.get('office').enable();
        this.showDialog = true;
    }

    editItem(user: User) {
        this.utilsService.resetForm(this.formItem);
        this.showDialog = true;
        this.formItem.patchValue(user);
        const office = this.officesActive.filter(office => office.value.id === user.office?.id)[0]?.value
        const roleTypes: string[] = user.roles
            .filter(value => value.state === EnumsState.ACTIVE)
            .map(value => {
                return value.name;
            });
        if (office) {
            this.formItem.get('office').patchValue(office);
        }
        const org = user.organization;
        this.onOrganizacionChange(org)
        this.formItem.get('roleTypes').patchValue(roleTypes);
        this.formItem.get('username').disable();

    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'usuarios');
    }

    cancelDialog() {
        this.showPermisions=false;
        this.showDialog = false;
        this.messageService.clear();
        this.assignableRoles = JSON.parse(JSON.stringify(this.roles));

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

        if (user.id) {
            this.userService.updateUser(user).subscribe({
                next: () => {
                    this.showDialog = false;
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Guardado con éxito',
                        life: 3000
                    });
                    this.loadItems();
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar el usuario',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
        } else {
            this.userService.createUser(user)
                .subscribe({
                    next: () => {
                        this.showDialog = false;
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Guardado con éxito',
                            life: 3000
                        });
                        this.loadItems();
                    },
                    error: err => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al guardar el usuario',
                            detail: err.error.message,
                            life: 3000
                        });
                    }
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

    onEmailChange() {
        if (!this.formItem.get('id').value && this.formItem.get('email').valid) {
            const username = this.formItem.get('email').value.split('@', 1) ? this.formItem.get('email').value.split('@', 1)[0] : null;
            this.formItem.get('username').patchValue(username);
        }
    }

    onOrganizacionChange(org: Organization) {
        this.showPermisions=true;
        if (org) {
            this.assignableRoles = JSON.parse(JSON.stringify(this.roles));
            if (org.id === 1) {
                this.formItem.get('office').setValidators([Validators.required]);
                this.formItem.get('office').enable();
                this.formItem.get('office').updateValueAndValidity();
                this.assignableRoles = this.assignableRoles
                    .filter(value1 => value1.value !== 'EJECUTOR_PROYECTOS' && value1.value !== 'MONITOR_PROYECTOS'
                                       && value1.value !== 'SUPERVISOR_REPORTE_SOCIO'
                    )
            } else {
                this.assignableRoles = this.assignableRoles
                    .filter(value1 => value1.value !== 'EJECUTOR_ID' && value1.value !== 'MONITOR_ID'
                                        && value1.value !== 'ADMINISTRADOR_REGIONAL' && value1.value !== 'ADMINISTRADOR_LOCAL'
                                        && value1.value !== 'SUPER_ADMINISTRADOR' && value1.value !== 'PUNTO_FOCAL'
                                        && value1.value !== 'ADMINISTRADOR_OFICINA' && value1.value !== 'RESULT_MANAGER'
                                        && value1.value !== 'SUPERVISOR_REPORTE_ID'
                    )
                this.formItem.get('office').patchValue(null);
                this.formItem.get('office').clearValidators();
                this.formItem.get('office').updateValueAndValidity();
                this.formItem.get('office').disable();
            }
        }
    }
}
