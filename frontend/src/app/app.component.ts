import {Component, OnInit} from '@angular/core';
import {ConfirmationService, PrimeNGConfig} from 'primeng/api';
import {EnumsService} from './shared/services/enums.service';
import {environment} from '../environments/environment';
import {VersionCheckService} from './shared/services/version-check.service';
import {CheckBrowserService} from './shared/services/check-browser.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';
import {GoogleAnalyticsService} from 'ngx-google-analytics';

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

    constructor(private primengConfig: PrimeNGConfig,
                private enumsService: EnumsService,
                private versionCheckService: VersionCheckService,
                private checkBrowserService: CheckBrowserService,
                private confirmationService: ConfirmationService,
                private router: Router,
                private activatedRoute: ActivatedRoute,
                private $gaService: GoogleAnalyticsService
    ) {
        this.router.events.pipe(
            filter(event => event instanceof NavigationEnd)
        ).subscribe((event: NavigationEnd) => {
            console.log('1');
            console.log(event);
            $gaService.pageView(event.urlAfterRedirects);
            /* gtag('event', 'page_view', {
                 page_path: event.urlAfterRedirects
             })*/
        });
        /** END : Code to Track Page View  using gtag.js */

        // Add dynamic title for selected pages - Start
        router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                console.log('2');
                console.log(event);
            }
        });
    }

    ngOnInit() {
        this.primengConfig.ripple = true;
        this.ripple = true;
        if (this.checkBrowserService.getBrowser() !== 'Chrome') {
            this.confirmationService.confirm({
                message: `<p>Por favor usa el Explorador Google Chrome</p>
                          <p>
                            Si no lo tienes puedes descargarlo aquí:
                            <a href="https://www.google.com/chrome/?brand=YTUH&gclid=EAIaIQobChMIw6i-hNqB-QIVSI9oCR2gKwrZEAAYASAAEgIaSfD_BwE&gclsrc=aw.ds">Google Chrome</a>.
                          </p>
                          `,
                header: 'Por favor usa el Explorador Google Chrome',
                icon: 'pi pi-exclamation-triangle',
                acceptVisible: false,
                rejectVisible: false,
                closeOnEscape: false,
            });
        }
        this.enumsService.loadcache();
        this.setLocale(this.primengConfig, environment.locale);
        this.versionCheckService.initVersionCheck(environment.versionCheckURL);
        this.versionCheckService.checkVersion(environment.versionCheckURL);

    }

    /*
    ** Globally configure the locale (i18n).
    */
    setLocale(config: PrimeNGConfig, locale: string): void {
        switch (locale.toLowerCase()) {
            case 'en': {
                // already english
                const dow: string[] = config.getTranslation('dayNames');
                if (dow[0] !== 'Sunday') {
                    // noinspection JSUnusedLocalSymbols
                    const msg = 'Default locale not EN.';
                }
                break;
            }
            case 'es': {
                config.setTranslation({
                    startsWith: 'Comienza con',
                    contains: 'Contiene',
                    notContains: 'No contiene',
                    endsWith: 'Termina con',
                    equals: 'Igual',
                    notEquals: 'No es igual',
                    noFilter: 'Sin filtro',
                    lt: 'Menos que',
                    lte: 'Menos que o igual a',
                    gt: 'Mas grande que',
                    gte: 'Mayor qué o igual a',
                    is: 'Es',
                    isNot: 'No es',
                    before: 'Before',
                    after: 'After',
                    apply: 'Aplicar',
                    matchAll: 'Coincidir con todos',
                    matchAny: 'Coincidir con cualquiera',
                    addRule: 'Agregar regla',
                    removeRule: 'Eliminar regla',
                    accept: 'Si',
                    reject: 'No',
                    choose: 'Escoger',
                    upload: 'Subir',
                    cancel: 'Cancelar',
                    dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
                    dayNamesShort: ['dom', 'lun', 'mar', 'mié', 'jue', 'vie', 'sáb'],
                    dayNamesMin: ['D', 'L', 'M', 'X', 'J', 'V', 'S'],
                    monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
                    monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
                    today: 'Hoy',
                    clear: 'Limpiar',
                    weekHeader: 'Sm'
                });
                break;
            }
            case 'de': {
                config.setTranslation({
                    startsWith: 'Beginnt mit',
                    contains: 'Enthält',
                    notContains: 'Enthält nicht',
                    endsWith: 'Endet mit',
                    equals: 'Gleich',
                    notEquals: 'Kein Filter',
                    noFilter: 'Kein Filter',
                    lt: 'Weniger als',
                    lte: 'Weniger als oder gleich',
                    gt: 'Größer als',
                    gte: 'Größer als oder gleich wie',
                    is: 'Ist',
                    isNot: 'Ist nicht',
                    before: 'Vor',
                    after: 'Nach dem',
                    apply: 'Anwenden',
                    matchAll: 'Alle zusammenbringen',
                    matchAny: 'Passen Sie zu einem',
                    addRule: 'Regel hinzufügen',
                    removeRule: 'Regel entfernen',
                    accept: 'Ja',
                    reject: 'Nein',
                    choose: 'Wählen',
                    upload: 'Hochladen',
                    cancel: 'Stornieren',
                    dayNames: ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samastag'],
                    dayNamesShort: ['Son', 'Mon', 'Die', 'Mit', 'Don', 'Fre', 'Sam'],
                    dayNamesMin: ['So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa'],
                    monthNames: ['Januar', 'Februar', 'März', 'April', 'Mai', 'Juni', 'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember'],
                    monthNamesShort: ['Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun', 'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez'],
                    today: 'Heute',
                    clear: 'Löschen',
                    weekHeader: 'Wo'
                });
                break;
            }
            default: {
                const msg = `Locale: '${locale}' not available.`;
                console.error(`${msg}`);
                break;
            }
        }
    }
}
