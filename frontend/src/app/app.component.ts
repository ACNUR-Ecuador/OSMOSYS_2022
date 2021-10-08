import {Component, OnInit} from '@angular/core';
import {PrimeNGConfig} from 'primeng/api';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

        menuMode = 'horizontal';
        colorScheme = 'light';
        menuTheme = 'layout-sidebar-white';

    /*    menuMode = 'static';
        colorScheme = 'light';
        menuTheme = 'layout-sidebar-unhcr';*/

    /*    menuMode = 'static';
        colorScheme = 'light';
        menuTheme = 'layout-sidebar-unhcr';*/
/*    menuMode = 'slim';
    colorScheme = 'light';
    menuTheme = 'layout-sidebar-unhcr';*/

    inputStyle = 'outlined';

    ripple: boolean;

    constructor(private primengConfig: PrimeNGConfig) {
    }

    ngOnInit() {
        this.primengConfig.ripple = true;
        this.ripple = true;
    }
}
