import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardDemoComponent} from '../demo/view/dashboarddemo.component';
import {FormLayoutDemoComponent} from '../demo/view/formlayoutdemo.component';
import {FloatLabelDemoComponent} from '../demo/view/floatlabeldemo.component';
import {InvalidStateDemoComponent} from '../demo/view/invalidstatedemo.component';
import {InputDemoComponent} from '../demo/view/inputdemo.component';
import {ButtonDemoComponent} from '../demo/view/buttondemo.component';
import {TableDemoComponent} from '../demo/view/tabledemo.component';
import {ListDemoComponent} from '../demo/view/listdemo.component';
import {TreeDemoComponent} from '../demo/view/treedemo.component';
import {PanelsDemoComponent} from '../demo/view/panelsdemo.component';
import {OverlaysDemoComponent} from '../demo/view/overlaysdemo.component';
import {MenusDemoComponent} from '../demo/view/menusdemo.component';
import {MediaDemoComponent} from '../demo/view/mediademo.component';
import {MessagesDemoComponent} from '../demo/view/messagesdemo.component';
import {MiscDemoComponent} from '../demo/view/miscdemo.component';
import {ChartsDemoComponent} from '../demo/view/chartsdemo.component';
import {FileDemoComponent} from '../demo/view/filedemo.component';
import {AppTimelineDemoComponent} from '../demo/view/app.timelinedemo.component';
import {EmptyDemoComponent} from '../demo/view/emptydemo.component';
import {DocumentationComponent} from '../demo/view/documentation.component';
import {DisplayComponent} from '../demo/utilities/display.component';
import {ElevationComponent} from '../demo/utilities/elevation.component';
import {FlexboxComponent} from '../demo/utilities/flexbox.component';
import {GridComponent} from '../demo/utilities/grid.component';
import {IconsComponent} from '../demo/utilities/icons.component';
import {WidgetsComponent} from '../demo/utilities/widgets.component';
import {SpacingComponent} from '../demo/utilities/spacing.component';
import {TypographyComponent} from '../demo/utilities/typography.component';
import {TextComponent} from '../demo/utilities/text.component';
import {AppCrudComponent} from '../demo/view/app.crud.component';
import {AppCalendarComponent} from '../demo/view/app.calendar.component';
import {AppInvoiceComponent} from '../demo/view/app.invoice.component';
import {AppHelpComponent} from '../demo/view/app.help.component';
import {AreasAdministrationComponent} from './areas-administration/areas-administration.component';

const routes: Routes = [
    {
        path: '',
        children: [
            {path: 'areas', component: AreasAdministrationComponent}
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AdministrationRoutingModule {
}
