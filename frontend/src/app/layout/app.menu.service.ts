import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {MenuChangeEvent} from './api/menuchangeevent';
import {UserService} from '../services/user.service';
import {NgxPermissionsObject, NgxPermissionsService} from 'ngx-permissions';

export interface Menu {
    label?: string;
    icon?: string;
    roles?: string[];

    routerLink?: string[];
    routerLinkActiveOptions?;
    items?: Menu[];
    separator?: boolean;
    class?: string;
    url?;
    target?: string;
    visible?: boolean;
}

@Injectable({
    providedIn: 'root'
})
export class MenuService {

    private menuSource = new Subject<MenuChangeEvent>();
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
                },
                {
                    label: 'Reportes de retrasos',
                    icon: 'pi pi-fw pi-calendar-times',
                    routerLink: ['/reports/lateReports'],
                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL'],
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
                    label: '2023',
                    icon: 'pi pi-chart-line',
                    roles: ['SUPER_ADMINISTRADOR'],
                    items: []
                },
                {
                    label: '2022',
                    icon: 'pi pi-chart-line',
                    roles: ['SUPER_ADMINISTRADOR'],
                    items: [
                        {
                            label: 'Reportes Temáticos',
                            icon: 'pi pi-fw pi-chart-bar',
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL'],
                            items: [
                                {
                                    label: 'Albergue de Emergencia',
                                    icon: 'pi pi-fw pi-chart-bar',
                                    routerLink: ['/reports/2022/shelter'],
                                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                                },
                                {
                                    label: 'Transferencias Monetarias',
                                    icon: 'pi pi-fw pi-chart-bar',
                                    routerLink: ['/reports/2022/cbi'],
                                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                                }, {
                                    label: 'Protección Comunitaria',
                                    icon: 'pi pi-fw pi-chart-bar',
                                    routerLink: ['/reports/2022/communityProtection'],
                                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                                }, {
                                    label: 'Habitabilidad',
                                    icon: 'pi pi-fw pi-chart-bar',
                                    routerLink: ['/reports/2022/habitability'],
                                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                                }, {
                                    label: 'Medios de Vida',
                                    icon: 'pi pi-fw pi-chart-bar',
                                    routerLink: ['/reports/2022/livelihoods'],
                                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                                }, {
                                    label: 'Reasentamiento',
                                    icon: 'pi pi-fw pi-chart-bar',
                                    routerLink: ['/reports/2022/resettlement'],
                                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                                }, {
                                    label: 'Necesidades Especiales',
                                    icon: 'pi pi-fw pi-chart-bar',
                                    routerLink: ['/reports/2022/specialNeeds'],
                                    roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL']
                                }
                            ]
                        },
                        {
                            label: 'Indicadores de Producto',
                            icon: 'pi pi-fw pi-chart-line',
                            routerLink: ['/reports/2022/productIndicators'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'PUNTO_FOCAL', 'EJECUTOR_ID', 'MONITOR_ID'],
                        },
                        {
                            label: 'Indicadores RBA',
                            icon: 'pi pi-fw pi-chart-line',
                            routerLink: ['/reports/2022/rbaIndicators'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'PUNTO_FOCAL', 'EJECUTOR_ID', 'MONITOR_ID'],
                        },
                        {
                            label: 'Reportes de tendencia de indicadores',
                            icon: 'pi pi-fw pi-calendar-times',
                            routerLink: ['/reports/2022/indicatorTrends'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL'],
                        },
                        {
                            label: 'Reportes de comparación de ejecución directa',
                            icon: 'pi pi-fw pi-calendar-times',
                            routerLink: ['/reports/2022/indicatorComparison'],
                            roles: ['SUPER_ADMINISTRADOR', 'ADMINISTRADOR', 'EJECUTOR_ID', 'MONITOR_ID', 'PUNTO_FOCAL'],
                        }
                    ]
                }
            ]
        },
        {
            label: 'Demo',
            icon: 'pi pi-home',
            items: [
                {
                    label: 'Dashboards',
                    icon: 'pi pi-home',
                    items: [
                        {
                            label: 'E-Commerce',
                            icon: 'pi pi-fw pi-home',
                            routerLink: ['/']
                        },
                        {
                            label: 'Banking',
                            icon: 'pi pi-fw pi-image',
                            routerLink: ['/dashboard-banking']
                        }
                    ]
                },
                {separator: true},
                {
                    label: 'Apps',
                    icon: 'pi pi-th-large',
                    items: [
                        {
                            label: 'Blog',
                            icon: 'pi pi-fw pi-comment',
                            items: [
                                {
                                    label: 'List',
                                    icon: 'pi pi-fw pi-image',
                                    routerLink: ['/apps/blog/list']
                                },
                                {
                                    label: 'Detail',
                                    icon: 'pi pi-fw pi-list',
                                    routerLink: ['/apps/blog/detail']
                                },
                                {
                                    label: 'Edit',
                                    icon: 'pi pi-fw pi-pencil',
                                    routerLink: ['/apps/blog/edit']
                                }
                            ]
                        },
                        {
                            label: 'Calendar',
                            icon: 'pi pi-fw pi-calendar',
                            routerLink: ['/apps/calendar']
                        },
                        {
                            label: 'Chat',
                            icon: 'pi pi-fw pi-comments',
                            routerLink: ['/apps/chat']
                        },
                        {
                            label: 'Files',
                            icon: 'pi pi-fw pi-folder',
                            routerLink: ['/apps/files']
                        },
                        {
                            label: 'Kanban',
                            icon: 'pi pi-fw pi-sliders-v',
                            routerLink: ['/apps/kanban']
                        },
                        {
                            label: 'Mail',
                            icon: 'pi pi-fw pi-envelope',
                            items: [
                                {
                                    label: 'Inbox',
                                    icon: 'pi pi-fw pi-inbox',
                                    routerLink: ['/apps/mail/inbox']
                                },
                                {
                                    label: 'Compose',
                                    icon: 'pi pi-fw pi-pencil',
                                    routerLink: ['/apps/mail/compose']
                                },
                                {
                                    label: 'Detail',
                                    icon: 'pi pi-fw pi-comment',
                                    routerLink: ['/apps/mail/detail/1000']
                                }
                            ]
                        },
                        {
                            label: 'Task List',
                            icon: 'pi pi-fw pi-check-square',
                            routerLink: ['/apps/tasklist']
                        }
                    ]
                },
                {separator: true},
                {
                    label: 'UI Kit',
                    icon: 'pi pi-fw pi-star-fill',
                    routerLink: ['/uikit'],
                    items: [
                        {
                            label: 'Form Layout',
                            icon: 'pi pi-fw pi-id-card',
                            routerLink: ['/uikit/formlayout']
                        },
                        {
                            label: 'Input',
                            icon: 'pi pi-fw pi-check-square',
                            routerLink: ['/uikit/input']
                        },
                        {
                            label: 'Float Label',
                            icon: 'pi pi-fw pi-bookmark',
                            routerLink: ['/uikit/floatlabel']
                        },
                        {
                            label: 'Invalid State',
                            icon: 'pi pi-fw pi-exclamation-circle',
                            routerLink: ['/uikit/invalidstate']
                        },
                        {
                            label: 'Button',
                            icon: 'pi pi-fw pi-mobile',
                            routerLink: ['/uikit/button'],
                            class: 'rotated-icon'
                        },
                        {
                            label: 'Table',
                            icon: 'pi pi-fw pi-table',
                            routerLink: ['/uikit/table']
                        },
                        {
                            label: 'List',
                            icon: 'pi pi-fw pi-list',
                            routerLink: ['/uikit/list']
                        },
                        {
                            label: 'Tree',
                            icon: 'pi pi-fw pi-share-alt',
                            routerLink: ['/uikit/tree']
                        },
                        {
                            label: 'Panel',
                            icon: 'pi pi-fw pi-tablet',
                            routerLink: ['/uikit/panel']
                        },
                        {
                            label: 'Overlay',
                            icon: 'pi pi-fw pi-clone',
                            routerLink: ['/uikit/overlay']
                        },
                        {
                            label: 'Media',
                            icon: 'pi pi-fw pi-image',
                            routerLink: ['/uikit/media']
                        },
                        {
                            label: 'Menu',
                            icon: 'pi pi-fw pi-bars',
                            routerLink: ['/uikit/menu'],
                            routerLinkActiveOptions: {exact: false}
                        },
                        {
                            label: 'Message',
                            icon: 'pi pi-fw pi-comment',
                            routerLink: ['/uikit/message']
                        },
                        {
                            label: 'File',
                            icon: 'pi pi-fw pi-file',
                            routerLink: ['/uikit/file']
                        },
                        {
                            label: 'Chart',
                            icon: 'pi pi-fw pi-chart-bar',
                            routerLink: ['/uikit/charts']
                        },
                        {
                            label: 'Misc',
                            icon: 'pi pi-fw pi-circle-off',
                            routerLink: ['/uikit/misc']
                        }
                    ]
                },
                {separator: true},
                {
                    label: 'Prime Blocks',
                    icon: 'pi pi-fw pi-prime',
                    routerLink: ['/blocks'],
                    items: [
                        {
                            label: 'Free Blocks',
                            icon: 'pi pi-fw pi-eye',
                            routerLink: ['/blocks']
                        },
                        {
                            label: 'All Blocks',
                            icon: 'pi pi-fw pi-globe',
                            url: ['https://www.primefaces.org/primeblocks-ng'],
                            target: '_blank'
                        }
                    ]
                },
                {separator: true},
                {
                    label: 'Utilities',
                    icon: 'pi pi-fw pi-compass',
                    routerLink: ['/utilities'],
                    items: [
                        {
                            label: 'PrimeIcons',
                            icon: 'pi pi-fw pi-prime',
                            routerLink: ['utilities/icons']
                        },
                        {
                            label: 'Colors',
                            icon: 'pi pi-fw pi-palette',
                            routerLink: ['utilities/colors']
                        },
                        {
                            label: 'PrimeFlex',
                            icon: 'pi pi-fw pi-desktop',
                            url: ['https://www.primefaces.org/primeflex/'],
                            target: '_blank'
                        },
                        {
                            label: 'Figma',
                            icon: 'pi pi-fw pi-pencil',
                            url: ['https://www.figma.com/file/lKooXEoqqWz7PBYwJ7B8QS/Preview-%7C-Diamond-2022?node-id=271%3A12531'],
                            target: '_blank'
                        }
                    ]
                },
                {separator: true},
                {
                    label: 'Pages',
                    icon: 'pi pi-fw pi-briefcase',
                    routerLink: ['/pages'],
                    items: [
                        {
                            label: 'Landing',
                            icon: 'pi pi-fw pi-globe',
                            routerLink: ['/landing']
                        },
                        {
                            label: 'Auth',
                            icon: 'pi pi-fw pi-user',
                            items: [
                                {
                                    label: 'Login',
                                    icon: 'pi pi-fw pi-sign-in',
                                    routerLink: ['/auth/login']
                                },
                                {
                                    label: 'Error',
                                    icon: 'pi pi-fw pi-times-circle',
                                    routerLink: ['/auth/error']
                                },
                                {
                                    label: 'Access Denied',
                                    icon: 'pi pi-fw pi-lock',
                                    routerLink: ['/auth/access']
                                },
                                {
                                    label: 'Register',
                                    icon: 'pi pi-fw pi-user-plus',
                                    routerLink: ['/auth/register']
                                },
                                {
                                    label: 'Forgot Password',
                                    icon: 'pi pi-fw pi-question',
                                    routerLink: ['/auth/forgotpassword']
                                },
                                {
                                    label: 'New Password',
                                    icon: 'pi pi-fw pi-cog',
                                    routerLink: ['/auth/newpassword']
                                },
                                {
                                    label: 'Verification',
                                    icon: 'pi pi-fw pi-envelope',
                                    routerLink: ['/auth/verification']
                                },
                                {
                                    label: 'Lock Screen',
                                    icon: 'pi pi-fw pi-eye-slash',
                                    routerLink: ['/auth/lockscreen']
                                }
                            ]
                        },
                        {
                            label: 'Crud',
                            icon: 'pi pi-fw pi-pencil',
                            routerLink: ['/pages/crud']
                        },
                        {
                            label: 'Timeline',
                            icon: 'pi pi-fw pi-calendar',
                            routerLink: ['/pages/timeline']
                        },
                        {
                            label: 'Invoice',
                            icon: 'pi pi-fw pi-dollar',
                            routerLink: ['/pages/invoice']
                        },
                        {
                            label: 'About Us',
                            icon: 'pi pi-fw pi-user',
                            routerLink: ['/pages/aboutus']
                        },
                        {
                            label: 'Help',
                            icon: 'pi pi-fw pi-question-circle',
                            routerLink: ['/pages/help']
                        },
                        {
                            label: 'Not Found',
                            icon: 'pi pi-fw pi-exclamation-circle',
                            routerLink: ['/pages/notfound']
                        },
                        {
                            label: 'Empty',
                            icon: 'pi pi-fw pi-circle-off',
                            routerLink: ['/pages/empty']
                        },
                        {
                            label: 'FAQ',
                            icon: 'pi pi-fw pi-question',
                            routerLink: ['/pages/faq']
                        },
                        {
                            label: 'Contact Us',
                            icon: 'pi pi-fw pi-phone',
                            routerLink: ['/pages/contact']
                        }
                    ]
                },
                {separator: true},
                {
                    label: 'E-Commerce',
                    icon: 'pi pi-fw pi-wallet',
                    items: [
                        {
                            label: 'Product Overview',
                            icon: 'pi pi-fw pi-image',
                            routerLink: ['ecommerce/product-overview']
                        },
                        {
                            label: 'Product List',
                            icon: 'pi pi-fw pi-list',
                            routerLink: ['ecommerce/product-list']
                        },
                        {
                            label: 'New Product',
                            icon: 'pi pi-fw pi-plus',
                            routerLink: ['ecommerce/new-product']
                        },
                        {
                            label: 'Shopping Cart',
                            icon: 'pi pi-fw pi-shopping-cart',
                            routerLink: ['ecommerce/shopping-cart']
                        },
                        {
                            label: 'Checkout Form',
                            icon: 'pi pi-fw pi-check-square',
                            routerLink: ['ecommerce/checkout-form']
                        },
                        {
                            label: 'Order History',
                            icon: 'pi pi-fw pi-history',
                            routerLink: ['ecommerce/order-history']
                        },
                        {
                            label: 'Order Summary',
                            icon: 'pi pi-fw pi-file',
                            routerLink: ['ecommerce/order-summary']
                        }
                    ]
                },
                {separator: true},
                {
                    label: 'User Management',
                    icon: 'pi pi-fw pi-user',
                    items: [
                        {
                            label: 'List',
                            icon: 'pi pi-fw pi-list',
                            routerLink: ['profile/list']
                        },
                        {
                            label: 'Create',
                            icon: 'pi pi-fw pi-plus',
                            routerLink: ['profile/create']
                        }
                    ]
                },
                {separator: true},
                {
                    label: 'Hierarchy',
                    icon: 'pi pi-fw pi-align-left',
                    items: [
                        {
                            label: 'Submenu 1',
                            icon: 'pi pi-fw pi-align-left',
                            items: [
                                {
                                    label: 'Submenu 1.1',
                                    icon: 'pi pi-fw pi-align-left',
                                    items: [
                                        {
                                            label: 'Submenu 1.1.1',
                                            icon: 'pi pi-fw pi-align-left',
                                        },
                                        {
                                            label: 'Submenu 1.1.2',
                                            icon: 'pi pi-fw pi-align-left',
                                        },
                                        {
                                            label: 'Submenu 1.1.3',
                                            icon: 'pi pi-fw pi-align-left',
                                        }
                                    ]
                                },
                                {
                                    label: 'Submenu 1.2',
                                    icon: 'pi pi-fw pi-align-left',
                                    items: [
                                        {
                                            label: 'Submenu 1.2.1',
                                            icon: 'pi pi-fw pi-align-left',
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            label: 'Submenu 2',
                            icon: 'pi pi-fw pi-align-left',
                            items: [
                                {
                                    label: 'Submenu 2.1',
                                    icon: 'pi pi-fw pi-align-left',
                                    items: [
                                        {
                                            label: 'Submenu 2.1.1',
                                            icon: 'pi pi-fw pi-align-left',
                                        },
                                        {
                                            label: 'Submenu 2.1.2',
                                            icon: 'pi pi-fw pi-align-left',
                                        }
                                    ]
                                },
                                {
                                    label: 'Submenu 2.2',
                                    icon: 'pi pi-fw pi-align-left',
                                    items: [
                                        {
                                            label: 'Submenu 2.2.1',
                                            icon: 'pi pi-fw pi-align-left',
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                },
                {separator: true},
                {
                    label: 'Start',
                    icon: 'pi pi-fw pi-download',
                    items: [
                        {
                            label: 'Buy Now',
                            icon: 'pi pi-fw pi-shopping-cart',
                            url: ['https://www.primefaces.org/store']
                        },
                        {
                            label: 'Documentation',
                            icon: 'pi pi-fw pi-info-circle',
                            routerLink: ['/documentation']
                        }
                    ]
                }
            ]
        }
    ];
}
