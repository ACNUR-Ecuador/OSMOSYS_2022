import {OnInit} from '@angular/core';
import {Component} from '@angular/core';
import {MenuService} from './app.menu.service';

@Component({
    selector: 'app-menu',
    templateUrl: './app.menu.component.html'
})
export class AppMenuComponent implements OnInit {

    model: any[] = [];
    constructor(
                private menuService: MenuService
    ) {
    }

    ngOnInit() {
        //this.model = this.menuService.MENUITEMS;
        this.menuService.menuModel$.subscribe(value => {
            this.model=value;
        })

    }
}
