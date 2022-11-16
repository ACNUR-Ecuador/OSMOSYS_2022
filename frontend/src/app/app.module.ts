import {NgModule, CUSTOM_ELEMENTS_SCHEMA, LOCALE_ID} from '@angular/core';
import {HashLocationStrategy, LocationStrategy, registerLocaleData} from '@angular/common';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AppLayoutModule} from './layout/app.layout.module';
import {SharedModule} from './shared/shared.module';
import {NgxPermissionsModule} from 'ngx-permissions';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {LoaderInterceptor} from './shared/interceptors/loader.interceptor';
import {TokenResponseInterceptor} from './shared/interceptors/token-response.interceptor';
import {TokenRequestInterceptor} from './shared/interceptors/token-request.interceptor';
import {AppCodeInterceptor} from './shared/interceptors/app-code.interceptor';
import {ServicesModule} from './services/services.module';
import Es from '@angular/common/locales/es';
import {IndicatorFormsModule} from './indicator-forms/indicator-forms.module';
registerLocaleData(Es);

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        AppRoutingModule,
        AppLayoutModule,
        IndicatorFormsModule,
        SharedModule,
        ServicesModule.forRoot(),
        // permisos
        NgxPermissionsModule.forRoot()
    ],
    providers: [
        {provide: LOCALE_ID, useValue: 'es-ES'},
        {provide: LocationStrategy, useClass: HashLocationStrategy},
        {
            provide: HTTP_INTERCEPTORS,
            useClass: LoaderInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenRequestInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenResponseInterceptor,
            multi: true
        },

        {
            provide: HTTP_INTERCEPTORS,
            useClass: AppCodeInterceptor,
            multi: true
        }
    ],
    bootstrap: [AppComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule {
}
