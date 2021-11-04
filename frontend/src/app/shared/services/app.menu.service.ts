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
            label: 'Administration', icon: 'pi pi-home',  roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR'],
            items: [
                {label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']},
                {label: 'Areas', icon: 'pi pi-fw pi-home', routerLink: ['/administration/areas'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']},
                {label: 'Periodos', icon: 'pi pi-fw pi-home', routerLink: ['/administration/periods'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']},
                {label: 'Pilares', icon: 'pi pi-fw pi-home', routerLink: ['/administration/pillars'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']},
                {label: 'Situaciones', icon: 'pi pi-fw pi-home', routerLink: ['/administration/situations'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']},
                {label: 'Organizaciones', icon: 'pi pi-fw pi-home', routerLink: ['/administration/organizations'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']},
                {label: 'Oficinas', icon: 'pi pi-fw pi-home', routerLink: ['/administration/offices'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']},
                {label: 'Statements', icon: 'pi pi-fw pi-home', routerLink: ['/administration/statements'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']},
                // tslint:disable-next-line:max-line-length
                {label: 'Indicadores de Rendimiento', icon: 'pi pi-fw pi-home', routerLink: ['/administration/performanceIndicator'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']},
                {label: 'Marcadores', icon: 'pi pi-fw pi-home', routerLink: ['/administration/marker'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']},
                // tslint:disable-next-line:max-line-length
                {label: 'Desagregaciones Personalizadas', icon: 'pi pi-fw pi-home', routerLink: ['/administration/customDissagregation'], roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRATOR']}
            ]
        },
        {separator: true},
        {
            label: 'Demo', icon: 'pi pi-home',
            items: [
                {
                    label: 'UI Kit', icon: 'pi pi-fw pi-star', routerLink: ['/uikit'],
                    items: [
                        {label: 'Form Layout', icon: 'pi pi-fw pi-id-card', routerLink: ['/demo/formlayout']},
                        {label: 'Input', icon: 'pi pi-fw pi-check-square', routerLink: ['/demo/input']},
                        {label: 'Float Label', icon: 'pi pi-fw pi-bookmark', routerLink: ['/demo/floatlabel']},
                        {label: 'Invalid State', icon: 'pi pi-fw pi-exclamation-circle', routerLink: ['/demo/invalidstate']},
                        {label: 'Button', icon: 'pi pi-fw pi-mobile', routerLink: ['/demo/button'], class: 'rotated-icon'},
                        {label: 'Table', icon: 'pi pi-fw pi-table', routerLink: ['/demo/table']},
                        {label: 'List', icon: 'pi pi-fw pi-list', routerLink: ['/demo/list']},
                        {label: 'Tree', icon: 'pi pi-fw pi-share-alt', routerLink: ['/demo/tree']},
                        {label: 'Panel', icon: 'pi pi-fw pi-tablet', routerLink: ['/demo/panel']},
                        {label: 'Overlay', icon: 'pi pi-fw pi-clone', routerLink: ['/demo/overlay']},
                        {label: 'Media', icon: 'pi pi-fw pi-image', routerLink: ['/demo/media']},
                        {label: 'Menu', icon: 'pi pi-fw pi-bars', routerLink: ['/demo/menu']},
                        {label: 'Message', icon: 'pi pi-fw pi-comment', routerLink: ['/demo/message']},
                        {label: 'File', icon: 'pi pi-fw pi-file', routerLink: ['/demo/file']},
                        {label: 'Chart', icon: 'pi pi-fw pi-chart-bar', routerLink: ['/demo/charts']},
                        {label: 'Misc', icon: 'pi pi-fw pi-circle-off', routerLink: ['/demo/misc']}
                    ]
                },
                {separator: true},
                {
                    label: 'Utilities', icon: 'pi pi-fw pi-compass', routerLink: ['utilities'],
                    items: [
                        {label: 'Display', icon: 'pi pi-fw pi-desktop', routerLink: ['demo/utilities/display']},
                        {label: 'Elevation', icon: 'pi pi-fw pi-external-link', routerLink: ['demo/utilities/elevation']},
                        {label: 'FlexBox', icon: 'pi pi-fw pi-directions', routerLink: ['demo/utilities/flexbox']},
                        {label: 'Icons', icon: 'pi pi-fw pi-search', routerLink: ['demo/utilities/icons']},
                        {label: 'Text', icon: 'pi pi-fw pi-pencil', routerLink: ['demo/utilities/text']},
                        {label: 'Widgets', icon: 'pi pi-fw pi-star-o', routerLink: ['demo/utilities/widgets']},
                        {label: 'Grid System', icon: 'pi pi-fw pi-th-large', routerLink: ['demo/utilities/grid']},
                        {label: 'Spacing', icon: 'pi pi-fw pi-arrow-right', routerLink: ['demo/utilities/spacing']},
                        {label: 'Typography', icon: 'pi pi-fw pi-align-center', routerLink: ['demo/utilities/typography']}
                    ]
                },
                {separator: true},
                {
                    label: 'Pages', icon: 'pi pi-fw pi-briefcase', routerLink: ['/pages'],
                    items: [
                        {label: 'Crud', icon: 'pi pi-fw pi-pencil', routerLink: ['demo/pages/crud']},
                        {label: 'Calendar', icon: 'pi pi-fw pi-calendar-plus', routerLink: ['demo/pages/calendar']},
                        {label: 'Timeline', icon: 'pi pi-fw pi-calendar', routerLink: ['demo/pages/timeline']},
                        {label: 'Landing', icon: 'pi pi-fw pi-globe', url: 'assets/pages/landing.html', target: '_blank'},
                        {label: 'Login', icon: 'pi pi-fw pi-sign-in', routerLink: ['/login']},
                        {label: 'Invoice', icon: 'pi pi-fw pi-dollar', routerLink: ['demo/pages/invoice']},
                        {label: 'Help', icon: 'pi pi-fw pi-question-circle', routerLink: ['demo/pages/help']},
                        {label: 'Error', icon: 'pi pi-fw pi-times-circle', routerLink: ['/error']},
                        {label: 'Not Found', icon: 'pi pi-fw pi-exclamation-circle', routerLink: ['/notfound']},
                        {label: 'Access Denied', icon: 'pi pi-fw pi-lock', routerLink: ['/access']},
                        {label: 'Empty', icon: 'pi pi-fw pi-circle-off', routerLink: ['demo/pages/empty']}
                    ]
                },
                {separator: true},
                {
                    label: 'Hierarchy', icon: 'pi pi-fw pi-align-left',
                    items: [
                        {
                            label: 'Submenu 1', icon: 'pi pi-fw pi-align-left',
                            items: [
                                {
                                    label: 'Submenu 1.1', icon: 'pi pi-fw pi-align-left',
                                    items: [
                                        {label: 'Submenu 1.1.1', icon: 'pi pi-fw pi-align-left'},
                                        {label: 'Submenu 1.1.2', icon: 'pi pi-fw pi-align-left'},
                                        {label: 'Submenu 1.1.3', icon: 'pi pi-fw pi-align-left'},
                                    ]
                                },
                                {
                                    label: 'Submenu 1.2', icon: 'pi pi-fw pi-align-left',
                                    items: [
                                        {label: 'Submenu 1.2.1', icon: 'pi pi-fw pi-align-left'}
                                    ]
                                },
                            ]
                        },
                        {
                            label: 'Submenu 2', icon: 'pi pi-fw pi-align-left',
                            items: [
                                {
                                    label: 'Submenu 2.1', icon: 'pi pi-fw pi-align-left',
                                    items: [
                                        {label: 'Submenu 2.1.1', icon: 'pi pi-fw pi-align-left'},
                                        {label: 'Submenu 2.1.2', icon: 'pi pi-fw pi-align-left'},
                                    ]
                                },
                                {
                                    label: 'Submenu 2.2', icon: 'pi pi-fw pi-align-left',
                                    items: [
                                        {label: 'Submenu 2.2.1', icon: 'pi pi-fw pi-align-left'},
                                    ]
                                },
                            ]
                        }
                    ]
                },
                {separator: true},
            ]
        },
        {separator: true},

        {
            label: 'Start', icon: 'pi pi-fw pi-download',
            items: [
                {
                    label: 'Buy Now', icon: 'pi pi-fw pi-shopping-cart', url: 'https://www.primefaces.org/store'
                },
                {
                    label: 'Documentation', icon: 'pi pi-fw pi-info-circle', routerLink: ['demo/documentation']
                }
            ]
        },
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
