import {Injectable} from '@angular/core';
import {BehaviorSubject, Subject} from 'rxjs';
import {MenuChangeEvent} from './api/menuchangeevent';
import {NgxPermissionsObject, NgxPermissionsService} from 'ngx-permissions';
import {MenuItem} from "primeng/api";
import {MenuItemsService} from "../services/menu-items.service";


interface Menu extends MenuItem {
    roles?: string[];
    items?: Menu[];
}

@Injectable({
    providedIn: 'root'
})
export class MenuService {

    private menuSource = new Subject<MenuChangeEvent>();
    private resetSource = new Subject();
    private menuModel = new BehaviorSubject<Menu[]>([]);

    menuSource$ = this.menuSource.asObservable();
    resetSource$ = this.resetSource.asObservable();
    menuModel$ = this.menuModel.asObservable();

    constructor(
        private ngxPermissionsService: NgxPermissionsService,
        private menuItemsService: MenuItemsService,
    ) {


        ngxPermissionsService.permissions$.subscribe((permissions) => {

            this.menuItemsService.getMenuStructure().subscribe({
                next: value => {
                    let extraMenu = this.menuItemsService.processMenusItem(value);
                    let presettedMenu = this.MENUITEMS.concat(extraMenu);
                    let settedMenu = this.setCanChow(presettedMenu, permissions);
                    this.menuModel.next(settedMenu);
                }
            });

        });

    }

    onMenuStateChange(event: MenuChangeEvent) {
        this.menuSource.next(event);
    }

    reset() {
        this.resetSource.next(true);
    }

    public setCanChow(menuItems: Menu[], permissions: NgxPermissionsObject) {

        menuItems.forEach(menuItem => {
            // si tiene roles asignados los verifico
            if (menuItem.roles && menuItem.roles.length > 0) {
                // verifico con los roles q tiene, si puede mostrar
                this.ngxPermissionsService.hasPermission(menuItem.roles).then(value => {
                    menuItem.visible = value;

                });

            }
            // si no tiene asignado roles uso el can show por defecto
            if (menuItem.items) {
                menuItem.items = this.setCanChow(menuItem.items, permissions);
            }
        });
        return menuItems;
    }

    // tslint:disable-next-line
    MENUITEMS: Menu[] = [
        {
            label: 'Administración',
            icon: 'pi pi-cog',
            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'PUNTO_FOCAL', 'ADMINISTRADOR_OFICINA'],
            items: [
                // {label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']},
                {
                    label: 'Usuarios',
                    icon: 'pi pi-fw pi-user',
                    routerLink: ['/administration/users'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'PUNTO_FOCAL', 'ADMINISTRADOR_OFICINA']
                },
                {
                    label: 'Configuración del sistema',
                    icon: 'pi pi-fw pi-cog',
                    roles: ['SUPER_ADMINISTRADOR'],
                    items: [
                        {
                            label: 'Areas',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/areas'],
                            roles: ['SUPER_ADMINISTRADOR']
                        },
                        {
                            label: 'Tags',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/tags'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                        },
                        {
                            label: 'Pilares',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/pillars'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                        },
                        {
                            label: 'Periodos',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/periods'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                        },
                        {
                            label: 'Situaciones',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/situations'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                        },
                        {
                            label: 'Oficinas',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/offices'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                        },
                        {
                            label: 'Declaraciones',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/statements'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                        },
                        // tslint:disable-next-line:max-line-length
                        {
                            label: 'Desagregaciones Personalizadas',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/customDissagregation'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                        },
                        {
                            label: 'Organizaciones',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/organizations'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                        }
                    ]
                },
                {
                    label: 'Configuración de indicadores',
                    icon: 'pi pi-fw pi-cog',
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR'],
                    items: [
                        /*                        {
                                                    label: 'Marcadores',
                                                    icon: 'pi pi-fw pi-cog',
                                                    routerLink: ['/administration/marker'],
                                                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                                                },*/
                        // tslint:disable-next-line:max-line-length
                        {
                            label: 'Indicadores de Producto',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/performanceIndicator'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                        },

                    ]
                },
                {
                    label: 'Proyectos',
                    icon: 'pi pi-fw pi-cog',
                    routerLink: ['/administration/partnerProjectListAdministration'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'PUNTO_FOCAL']
                },
                {
                    label: 'Implementación Directa',
                    icon: 'pi pi-fw pi-cog',
                    routerLink: ['/administration/directImplementationAdministration'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'ADMINISTRADOR_OFICINA']
                },
                {
                    label: 'Envío Masivo de Correos',
                    icon: 'pi pi-fw pi-cog',
                    routerLink: ['/administration/massMailing'],
                    roles: ['SUPER_ADMINISTRADOR']
                },
                {
                    label: 'Bloqueo Masivo de Indicadores ',
                    icon: 'pi pi-fw pi-cog',
                    routerLink: ['/administration/massBlocking'],
                    roles: ['SUPER_ADMINISTRADOR']
                },
                {
                    label: 'Menu',
                    icon: 'pi pi-fw pi-cog',
                    routerLink: ['/administration/menuItemsAdministration'],
                    roles: ['SUPER_ADMINISTRADOR']
                },
                {
                    label: 'Configuración Global',
                    icon: 'pi pi-fw pi-cog',
                    routerLink: ['/administration/appconfiguration'],
                    roles: ['SUPER_ADMINISTRADOR']
                }
            ]
        },
        {
            label: 'Socios',
            icon: 'pi pi-users',
            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'MONITOR_PROYECTOS', 'EJECUTOR_PROYECTOS', 'PUNTO_FOCAL'],
            items: [
                {
                    label: 'Proyectos',
                    icon: 'pi pi-fw pi-home',
                    routerLink: ['/partners/partnersProjectList'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'MONITOR_PROYECTOS', 'EJECUTOR_PROYECTOS', 'PUNTO_FOCAL']
                }
            ]
        },
        {
            label: 'Implementación Directa',
            icon: 'pi pi-sitemap',
            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID'],
            items: [
                {
                    label: 'Reporte de indicadores',
                    icon: 'pi pi-fw pi-th-large',
                    routerLink: ['/directImplementation/areasMenu'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID'],
                }
            ]
        },
        {separator: true},
        {
            label: 'Reportes',
            icon: 'pi pi-file-o',
            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL'],
            items: [
                {
                    label: 'Catálogo de Indicadores',
                    icon: 'pi pi-fw pi-file-excel',
                    routerLink: ['/reports/indicatorsCatalog'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID'],
                },
                {
                    label: 'Exportación de Datos',
                    icon: 'pi pi-fw pi-file-excel',
                    routerLink: ['/reports/dataExport'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID'],
                },
                {
                    label: 'Reportes de retrasos',
                    icon: 'pi pi-fw pi-calendar-times',
                    routerLink: ['/reports/lateReports'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL'],
                }
            ]
        },
        {
            label: 'Acerca de OSMOSYS',
            icon: 'pi pi-file-o',
            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL'],
            routerLink: ['/home/about-us'],
        },
        {separator: true},
    ];

}
