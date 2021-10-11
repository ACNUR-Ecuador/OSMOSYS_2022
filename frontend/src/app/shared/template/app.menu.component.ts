import {Component, OnInit} from '@angular/core';
import {AppMainComponent} from '../../app.main.component';
import {MenuService} from '../services/app.menu.service';

@Component({
    selector: 'app-menu',
    templateUrl: './app.menu.component.html',
})
export class AppMenuComponent implements OnInit {

    model: any[];

    constructor(public appMain: AppMainComponent,
                private menuService: MenuService) {
    }

    ngOnInit() {
        this.model = this.menuService.MENUITEMS;
        console.log(this.model);
    }
}
