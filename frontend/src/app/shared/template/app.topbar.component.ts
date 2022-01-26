import {Component, OnDestroy} from '@angular/core';
import {AppMainComponent} from '../../app.main.component';
import {BreadcrumbService} from '../services/app.breadcrumb.service';
import {Subscription} from 'rxjs';
import {MenuItem} from 'primeng/api';
import {AppComponent} from '../../app.component';
import {UserService} from '../services/user.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-topbar',
    templateUrl: './app.topbar.component.html'
})
export class AppTopBarComponent implements OnDestroy {

    subscription: Subscription;

    items: MenuItem[];

    constructor(public breadcrumbService: BreadcrumbService,
                public app: AppComponent,
                public appMain: AppMainComponent,
                public userService: UserService,
                private router: Router) {
        this.subscription = breadcrumbService.itemsHandler.subscribe(response => {
            this.items = response;
        });
    }

    ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    logout() {
        this.userService.logout();
    }
}
