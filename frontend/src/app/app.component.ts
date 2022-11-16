import {Component, OnInit} from '@angular/core';
import {PrimeNGConfig} from 'primeng/api';
import {LayoutService} from './layout/service/app.layout.service';
import {EnumsService} from './services/enums.service';
import {Angulartics2GoogleAnalytics} from 'angulartics2';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

    constructor(
        private primengConfig: PrimeNGConfig,
        private layoutService: LayoutService,
        private enumsService: EnumsService,
        private angulartics2GoogleAnalytics: Angulartics2GoogleAnalytics) {
        angulartics2GoogleAnalytics.startTracking();
    }

    ngOnInit(): void {


        this.primengConfig.ripple = true;

        // optional configuration with the default configuration
        this.layoutService.config = {
            ripple: true,                      // toggles ripple on and off
            inputStyle: 'outlined',             // default style for input elements
            // tslint:disable-next-line:max-line-length
            menuMode: 'horizontal',                 // layout mode of the menu, valid values are "static", "overlay", "slim", "compact" and "horizontal"
            colorScheme: 'light',               // color scheme of the template, valid values are "light", "dim" and "dark"
            theme: 'unhcr',                      // default component theme for PrimeNG, see theme section for available values
            menuTheme: 'unhcr',              // theme of the menu, see menu theme section for available values
            scale: 12                           // size of the body font size to scale the whole application
        };
        this.layoutService.onConfigUpdate();
        this.applyScale();

        this.enumsService.loadcache();
    }

    applyScale() {
        document.documentElement.style.fontSize = this.layoutService.config.scale + 'px';
    }
}
