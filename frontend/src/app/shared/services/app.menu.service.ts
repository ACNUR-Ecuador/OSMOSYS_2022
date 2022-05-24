import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {UserService} from './user.service';
import {NgxPermissionsObject, NgxPermissionsService} from 'ngx-permissions';

export interface Menu {
    label?: string;
    icon?: string;
    roles?: string[];

    routerLink?: string[];
    items?: Menu[];
    separator?: boolean;
    class?: string;
    url?: string;
    target?: string;
    visible?: boolean;
}

@Injectable()
export class MenuService {


    MENUITEMS: Menu[] = [
        {
            label: 'Administración', icon: 'pi pi-cog', roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'PUNTO_FOCAL'],
            items: [
                // {label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']},
                {
                    label: 'Usuarios',
                    icon: 'pi pi-fw pi-user',
                    routerLink: ['/administration/users'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
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
                    ]
                },
                {
                    label: 'Configuración de indicadores',
                    icon: 'pi pi-fw pi-cog',
                    roles: ['SUPER_ADMINISTRADOR'],
                    items: [
                        {
                            label: 'Organizaciones',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/organizations'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                        },
                        {
                            label: 'Marcadores',
                            icon: 'pi pi-fw pi-cog',
                            routerLink: ['/administration/marker'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                        },
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
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR']
                },

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
                },
                {
                    label: 'Tablero de indicadores',
                    icon: 'pi pi-fw pi-th-large',
                    routerLink: ['/home/homeDashboardDirectImplementation'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID'],
                },
            ]
        },
        {separator: true},
        {
            label: 'Reportes',
            icon: 'pi pi-file-o',
            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'MONITOR_PROYECTOS', 'EJECUTOR_PROYECTOS', 'PUNTO_FOCAL'],
            items: [
                {
                    label: 'Catálogo de Indicadores',
                    icon: 'pi pi-fw pi-file-excel',
                    routerLink: ['/reports/indicatorsCatalog'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'MONITOR_PROYECTOS', 'EJECUTOR_PROYECTOS'],
                }, {
                    label: 'Exportación de Datos',
                    icon: 'pi pi-fw pi-file-excel',
                    routerLink: ['/reports/dataExport'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'MONITOR_PROYECTOS', 'EJECUTOR_PROYECTOS'],
                }
            ]
        },
        {separator: true},
        {
            label: 'Tableros de Control',
            icon: 'pi pi-chart-line',
            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL'],
            items: [
                {
                    label: 'Reportes Temáticos',
                    icon: 'pi pi-fw pi-chart-bar',
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL'],
                    items: [
                        {
                            label: 'Albergue de Emergencia',
                            icon: 'pi pi-fw pi-chart-bar',
                            routerLink: ['/reports/shelter'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                        },
                        {
                            label: 'Transferencias Monetarias',
                            icon: 'pi pi-fw pi-chart-bar',
                            routerLink: ['/reports/cbi'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                        }, {
                            label: 'Protección Comunitaria',
                            icon: 'pi pi-fw pi-chart-bar',
                            routerLink: ['/reports/communityProtection'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                        }, {
                            label: 'Habitabilidad',
                            icon: 'pi pi-fw pi-chart-bar',
                            routerLink: ['/reports/habitability'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                        }, {
                            label: 'Medios de Vida',
                            icon: 'pi pi-fw pi-chart-bar',
                            routerLink: ['/reports/livelihoods'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                        }, {
                            label: 'Reasentamiento',
                            icon: 'pi pi-fw pi-chart-bar',
                            routerLink: ['/reports/resettlement'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                        }, {
                            label: 'Necesidades Especiales',
                            icon: 'pi pi-fw pi-chart-bar',
                            routerLink: ['/reports/specialNeeds'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                        }
                    ]
                },
                {
                    label: 'Indicadores de Producto',
                    icon: 'pi pi-fw pi-chart-line',
                    routerLink: ['/reports/productIndicators'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'PUNTO_FOCAL'],
                },
                {
                    label: 'Indicadores RBA',
                    icon: 'pi pi-fw pi-chart-line',
                    routerLink: ['/reports/rbaIndicators'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'PUNTO_FOCAL'],
                },

                {
                    label: 'Indicadores ActivityInfo',
                    icon: 'pi pi-fw pi-chart-line',
                    routerLink: ['/reports/activityInfoIndicators'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'PUNTO_FOCAL'],
                },
                {
                    label: 'Tablero Control Reporte de Socios',
                    icon: 'pi pi-fw book',
                    routerLink: ['/reports/reportControlPartners'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'PUNTO_FOCAL'],
                },
                {
                    label: 'Tablero Control Reporte de ID',
                    icon: 'pi pi-fw book',
                    routerLink: ['/reports/reportControlDirectImplementation'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID'],
                },
                {
                    label: 'Reporte de Estado de Reporte de Proyectos',
                    icon: 'pi pi-fw book',
                    routerLink: ['/reports/allProjectsState'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'PUNTO_FOCAL'],
                },
            ]
        }
    ];

    private menuSource = new Subject<string>();
    private resetSource = new Subject();

    menuSource$ = this.menuSource.asObservable();
    resetSource$ = this.resetSource.asObservable();


    constructor(
        private userService: UserService,
        private ngxPermissionsService: NgxPermissionsService
    ) {

        ngxPermissionsService.permissions$.subscribe((permissions) => {
            this.setCanChow(this.MENUITEMS, permissions);
        });

    }

    onMenuStateChange(key: string) {
        this.menuSource.next(key);
    }

    reset() {
        this.resetSource.next();

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

}
