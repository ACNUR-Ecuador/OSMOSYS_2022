import {Component, OnInit} from '@angular/core';
import {PrimeNGConfig} from 'primeng/api';
import {EnumsService} from './shared/services/enums.service';
import {environment} from '../environments/environment';

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
                private enumsService: EnumsService
    ) {
    }

    ngOnInit() {
        this.primengConfig.ripple = true;
        this.ripple = true;
        this.enumsService.loadcache();
        this.setLocale(this.primengConfig, environment.locale);
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
                    const msg = 'Default locale not EN.';
                    console.error(` ${msg}`);
                } else {
                    console.log(` Using locale: '${locale}'`);
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
