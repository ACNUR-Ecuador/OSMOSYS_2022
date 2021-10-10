import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardDemoComponent} from './view/dashboarddemo.component';
import {FormLayoutDemoComponent} from './view/formlayoutdemo.component';
import {FloatLabelDemoComponent} from './view/floatlabeldemo.component';
import {InvalidStateDemoComponent} from './view/invalidstatedemo.component';
import {PanelsDemoComponent} from './view/panelsdemo.component';
import {OverlaysDemoComponent} from './view/overlaysdemo.component';
import {MediaDemoComponent} from './view/mediademo.component';
import {MenusDemoComponent} from './view/menusdemo.component';
import {MessagesDemoComponent} from './view/messagesdemo.component';
import {MiscDemoComponent} from './view/miscdemo.component';
import {EmptyDemoComponent} from './view/emptydemo.component';
import {ChartsDemoComponent} from './view/chartsdemo.component';
import {FileDemoComponent} from './view/filedemo.component';
import {DocumentationComponent} from './view/documentation.component';
import {InputDemoComponent} from './view/inputdemo.component';
import {ButtonDemoComponent} from './view/buttondemo.component';
import {TableDemoComponent} from './view/tabledemo.component';
import {ListDemoComponent} from './view/listdemo.component';
import {AppTimelineDemoComponent} from './view/app.timelinedemo.component';
import {TreeDemoComponent} from './view/treedemo.component';
import {DisplayComponent} from './utilities/display.component';
import {ElevationComponent} from './utilities/elevation.component';
import {FlexboxComponent} from './utilities/flexbox.component';
import {GridComponent} from './utilities/grid.component';
import {IconsComponent} from './utilities/icons.component';
import {WidgetsComponent} from './utilities/widgets.component';
import {SpacingComponent} from './utilities/spacing.component';
import {TypographyComponent} from './utilities/typography.component';
import {TextComponent} from './utilities/text.component';
import {AppCrudComponent} from './view/app.crud.component';
import {AppCalendarComponent} from './view/app.calendar.component';
import {AppInvoiceComponent} from './view/app.invoice.component';
import {AppHelpComponent} from './view/app.help.component';

const routes: Routes = [
    {
        path: '',
        children: [
            {path: 'dashboard', component: DashboardDemoComponent},
            {path: 'formlayout', component: FormLayoutDemoComponent},
            {path: 'floatlabel', component: FloatLabelDemoComponent},
            {path: 'invalidstate', component: InvalidStateDemoComponent},
            {path: 'input', component: InputDemoComponent},
            {path: 'button', component: ButtonDemoComponent},
            {path: 'table', component: TableDemoComponent},
            {path: 'list', component: ListDemoComponent},
            {path: 'tree', component: TreeDemoComponent},
            {path: 'panel', component: PanelsDemoComponent},
            {path: 'overlay', component: OverlaysDemoComponent},
            {path: 'menu', component: MenusDemoComponent},
            {path: 'media', component: MediaDemoComponent},
            {path: 'message', component: MessagesDemoComponent},
            {path: 'misc', component: MiscDemoComponent},
            {path: 'charts', component: ChartsDemoComponent},
            {path: 'file', component: FileDemoComponent},
            {path: 'timeline', component: AppTimelineDemoComponent},
            {path: 'empty', component: EmptyDemoComponent},
            {path: 'documentation', component: DocumentationComponent},
            {path: 'utilities/display', component: DisplayComponent},
            {path: 'utilities/elevation', component: ElevationComponent},
            {path: 'utilities/flexbox', component: FlexboxComponent},
            {path: 'utilities/grid', component: GridComponent},
            {path: 'utilities/icons', component: IconsComponent},
            {path: 'utilities/widgets', component: WidgetsComponent},
            {path: 'utilities/spacing', component: SpacingComponent},
            {path: 'utilities/typography', component: TypographyComponent},
            {path: 'utilities/text', component: TextComponent},
            {path: 'pages/crud', component: AppCrudComponent},
            {path: 'pages/calendar', component: AppCalendarComponent},
            {path: 'pages/invoice', component: AppInvoiceComponent},
            {path: 'pages/help', component: AppHelpComponent},
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DemoRoutingModule {
}
