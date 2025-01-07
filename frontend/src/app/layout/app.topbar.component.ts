import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {LayoutService} from 'src/app/layout/service/app.layout.service';
import {AppSidebarComponent} from './app.sidebar.component';
import {UserService} from "../services/user.service";
import {environment} from "../../environments/environment";

@Component({
    selector: 'app-topbar',
    templateUrl: './app.topbar.component.html'
})
export class AppTopbarComponent implements OnInit {
    operationName = `${environment.operationName}`;
    flagToolbarFile = `assets/layout/images/${environment.flagToolbarFile}`;

    @ViewChild('menubutton') menuButton!: ElementRef;

    @ViewChild(AppSidebarComponent) appSidebar!: AppSidebarComponent;

    public userInitials = '';
    item: any = {
        label: 'Acerca de OSMOSYS',
        icon: 'pi pi-info-circle',
        routerLink: ['/home/about-us']
    };

    constructor(public layoutService: LayoutService,
                public userService: UserService,
                public el: ElementRef) {
    }


    onMenuButtonClick() {
        this.layoutService.onMenuToggle();
    }

    onProfileButtonClick() {
        this.layoutService.showRightMenu();
    }

    onSearchClick() {
        this.layoutService.toggleSearchBar();
    }

    onRightMenuClick() {
        this.layoutService.showRightMenu();
    }

    get logo() {
        return this.layoutService.config.menuTheme === 'white' || this.layoutService.config.menuTheme === 'orange' ? 'dark' : 'white';
    }

    logout() {
        this.userService.logout();
    }


    ngOnInit(): void {
        const user = this.userService.getLogedUsername();
        if (user && user.name) {
            this.userInitials = user.name.split(' ').map(n => n[0]).join('');
        }
    }
}
