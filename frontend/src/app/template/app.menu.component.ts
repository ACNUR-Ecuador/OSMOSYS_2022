import {Component, OnInit} from '@angular/core';
import {AppMainComponent} from './app.main.component';

@Component({
    selector: 'app-menu',
    templateUrl: './app.menu.component.html',
})
export class AppMenuComponent implements OnInit {

    model: any[];

    constructor(public appMain: AppMainComponent) {
    }

    ngOnInit() {
        this.model = [
            {
                label: 'Test', icon: 'pi pi-home',
                items: [
                    {label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/']}
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
                        label: 'Buy Now', icon: 'pi pi-fw pi-shopping-cart', url: ['https://www.primefaces.org/store']
                    },
                    {
                        label: 'Documentation', icon: 'pi pi-fw pi-info-circle', routerLink: ['/documentation']
                    }
                ]
            },
        ];
    }
}
