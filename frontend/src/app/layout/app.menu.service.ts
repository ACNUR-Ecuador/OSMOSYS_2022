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
            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR_LOCAL','ADMINISTRADOR_REGIONAL'],
            items: [
                // {label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']},

                {
                    label: 'Configuración del sistema',
                    icon: 'pi pi-fw pi-cog',
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR_LOCAL','ADMINISTRADOR_REGIONAL'],
                    items: [
                        {
                            label: 'Parámetros de Configuración',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/appconfiguration'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL']
                        },
                        {
                            label: 'Áreas',
                            icon: 'pi pi-fw pi-map',
                            routerLink: ['/administration/areas'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL']
                        },
                        {
                            label: 'Grupos Poblacionales',
                            icon: 'pi pi-fw pi-users',
                            routerLink: ['/administration/pillars'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL']
                        },
                        {
                            label: 'Años',
                            icon: 'pi pi-fw pi-calendar',
                            routerLink: ['/administration/periods'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL']
                        },

                        {
                            label: 'Oficinas/Unidades',
                            icon: 'pi pi-fw pi-building',
                            routerLink: ['/administration/offices'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL','ADMINISTRADOR_LOCAL']
                        },
                        {
                            label: 'Implementadores',
                            icon: 'pi pi-fw pi-plus',
                            routerLink: ['/administration/organizations'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                        },
                        {
                            label: 'Tags',
                            icon: 'pi pi-fw pi-tags',
                            routerLink: ['/administration/tags'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                        },
                        {
                            label: 'Desagregaciones Personalizadas',
                            icon: 'pi pi-fw pi-chart-bar',
                            routerLink: ['/administration/customDissagregation'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                        },
                        {
                            label: 'Usuarios',
                            icon: 'pi pi-fw pi-user',
                            routerLink: ['/administration/users'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                        },
                        {
                            label: 'Auditoría',
                            icon: 'pi pi-fw pi-shield',
                            routerLink: ['/administration/audits'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                        },



                        // tslint:disable-next-line:max-line-length


                    ]
                },

                {
                    label: 'Indicadores de Producto',
                    icon: 'pi pi-fw pi-box',
                    roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL'],
                    items: [


                        {
                            label: 'Marco de Resultados',
                            icon: 'pi pi-fw pi-list',
                            routerLink: ['/administration/statements'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                        },
                        {
                            label: 'Catálogo de Indicadores',
                            icon: 'pi pi-fw pi-chart-line',
                            routerLink: ['/administration/performanceIndicator'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                        },
                        {
                            label: 'Indicadores Socios',
                            icon: 'pi pi-fw pi-user-plus',
                            routerLink: ['/administration/partnerProjectListAdministration'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                        },
                        {
                            label: 'Indicadores Implementación Directa',
                            icon: 'pi pi-fw pi-briefcase',
                            routerLink: ['/administration/directImplementationAdministration'],
                            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                        },

                    ]},


                {
                    label: 'Envío Masivo de Correos',
                    icon: 'pi pi-fw pi-envelope',
                    routerLink: ['/administration/massMailing'],
                    roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                },
                {
                    label: 'Bloqueo Masivo de Indicadores ',
                    icon: 'pi pi-fw pi-lock',
                    routerLink: ['/administration/massBlocking'],
                    roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                },
                {
                    label: 'Menús de Tableros',
                    icon: 'pi pi-fw pi-bars',
                    routerLink: ['/administration/menuItemsAdministration'],
                    roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL']
                },

            ]
        },
        {
            label: 'Socios',
            icon: 'pi pi-users',
            roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL','ADMINISTRADOR_LOCAL', 'MONITOR_PROYECTOS', 'EJECUTOR_PROYECTOS', 'PUNTO_FOCAL', 'MONITOR_ID'],
            items: [
                {
                    label: 'Proyectos',
                    icon: 'pi pi-fw pi-home',
                    routerLink: ['/partners/partnersProjectList'],
                    roles: ['SUPER_ADMINISTRADOR','ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL', 'MONITOR_PROYECTOS', 'EJECUTOR_PROYECTOS', 'PUNTO_FOCAL', 'MONITOR_ID']
                }
            ]
        },
        {
            label: 'Implementación Directa',
            icon: 'pi pi-sitemap',
            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL', 'EJECUTOR_ID', 'MONITOR_ID'],
            items: [
                {
                    label: 'Reporte de indicadores',
                    icon: 'pi pi-fw pi-th-large',
                    routerLink: ['/directImplementation/areasMenu'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL', 'EJECUTOR_ID', 'MONITOR_ID'],
                }
            ]
        },
        {separator: true},
        {
            label: 'Mánager de Resultado',
            icon: 'pi pi-chart-bar',
            roles: ['RESULT_MANAGER', 'ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL'],
            items: [
                {
                    label: 'Verificación de Indicadores',
                    icon: 'pi pi-fw pi-check-square',
                    routerLink: ['/resultManager/resultManagerIndicatorList'],
                    roles: ['RESULT_MANAGER', 'ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL'],
                }
            ]
        },
        {separator: true},
        {
            label: 'Reportes',
            icon: 'pi pi-file-o',
            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL'],
            items: [
                {
                    label: 'Catálogo de Indicadores',
                    icon: 'pi pi-fw pi-file-excel',
                    routerLink: ['/reports/indicatorsCatalog'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL', 'EJECUTOR_ID', 'MONITOR_ID'],
                },
                {
                    label: 'Exportación de Datos',
                    icon: 'pi pi-fw pi-file-excel',
                    routerLink: ['/reports/dataExport'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL', 'EJECUTOR_ID', 'MONITOR_ID'],
                },
                {
                    label: 'Reportes de retrasos',
                    icon: 'pi pi-fw pi-calendar-times',
                    routerLink: ['/reports/lateReports'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR_REGIONAL', 'ADMINISTRADOR_LOCAL', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL'],
                }
            ]
        },
        {separator: true}
    ];

}
