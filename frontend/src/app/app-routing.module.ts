import {RouterModule} from '@angular/router';
import {NgModule} from '@angular/core';

import {AppMainComponent} from './template/app.main.component';
import {AppNotfoundComponent} from './template/errorPages/app.notfound.component';
import {AppErrorComponent} from './template/errorPages/app.error.component';
import {AppAccessdeniedComponent} from './template/errorPages/app.accessdenied.component';
import {AppLoginComponent} from './security/app.login.component';


import {DisplayComponent} from './demo/utilities/display.component';
import {ElevationComponent} from './demo/utilities/elevation.component';
import {FlexboxComponent} from './demo/utilities/flexbox.component';
import {GridComponent} from './demo/utilities/grid.component';
import {IconsComponent} from './demo/utilities/icons.component';
import {WidgetsComponent} from './demo/utilities/widgets.component';
import {SpacingComponent} from './demo/utilities/spacing.component';
import {TypographyComponent} from './demo/utilities/typography.component';
import {TextComponent} from './demo/utilities/text.component';
import {AppCrudComponent} from './demo/view/app.crud.component';
import {AppCalendarComponent} from './demo/view/app.calendar.component';
import {AppInvoiceComponent} from './demo/view/app.invoice.component';
import {AppHelpComponent} from './demo/view/app.help.component';
import {content} from './content-routes';
import {DashboardDemoComponent} from './demo/view/dashboarddemo.component';

@NgModule({
    imports: [
        RouterModule.forRoot([
            {
                path: '',
                component: AppMainComponent,
                children: content
                /* children: [


                 ]*/
            },
            {path: 'error', component: AppErrorComponent},
            {path: 'access', component: AppAccessdeniedComponent},
            {path: 'notfound', component: AppNotfoundComponent},
            {path: 'login', component: AppLoginComponent},
            {path: '**', redirectTo: '/notfound'},

        ], {scrollPositionRestoration: 'enabled', enableTracing: true})
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
