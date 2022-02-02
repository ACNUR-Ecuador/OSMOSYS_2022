import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AppMainComponent} from '../app.main.component';
import {AppRightmenuComponent} from './template/app.rightmenu.component';
import {AppMenuComponent} from './template/app.menu.component';
import {AppMenuitemComponent} from './template/app.menuitem.component';
import {AppConfigComponent} from './template/app.config.component';
import {AppTopBarComponent} from './template/app.topbar.component';
import {AppSearchComponent} from './template/app.search.component';
import {AppFooterComponent} from './template/app.footer.component';
import {AppNotfoundComponent} from './template/errorPages/app.notfound.component';
import {AppErrorComponent} from './template/errorPages/app.error.component';
import {AppAccessdeniedComponent} from './template/errorPages/app.accessdenied.component';
import {MenuService} from './services/app.menu.service';
import {BreadcrumbService} from './services/app.breadcrumb.service';

import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AccordionModule} from 'primeng/accordion';
import {AutoCompleteModule} from 'primeng/autocomplete';
import {AvatarModule} from 'primeng/avatar';
import {AvatarGroupModule} from 'primeng/avatargroup';
import {BadgeModule} from 'primeng/badge';
import {BreadcrumbModule} from 'primeng/breadcrumb';
import {ButtonModule} from 'primeng/button';
import {CalendarModule} from 'primeng/calendar';
import {CardModule} from 'primeng/card';
import {CarouselModule} from 'primeng/carousel';
import {CascadeSelectModule} from 'primeng/cascadeselect';
import {ChartModule} from 'primeng/chart';
import {CheckboxModule} from 'primeng/checkbox';
import {ChipModule} from 'primeng/chip';
import {ChipsModule} from 'primeng/chips';
import {CodeHighlighterModule} from 'primeng/codehighlighter';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {ConfirmPopupModule} from 'primeng/confirmpopup';
import {ColorPickerModule} from 'primeng/colorpicker';
import {ContextMenuModule} from 'primeng/contextmenu';
import {DataViewModule} from 'primeng/dataview';
import {DialogModule} from 'primeng/dialog';
import {DividerModule} from 'primeng/divider';
import {DropdownModule} from 'primeng/dropdown';
import {FieldsetModule} from 'primeng/fieldset';
import {FileUploadModule} from 'primeng/fileupload';
import {FullCalendarModule} from '@fullcalendar/angular';
import {GalleriaModule} from 'primeng/galleria';
import {InplaceModule} from 'primeng/inplace';
import {InputNumberModule} from 'primeng/inputnumber';
import {InputMaskModule} from 'primeng/inputmask';
import {InputSwitchModule} from 'primeng/inputswitch';
import {InputTextModule} from 'primeng/inputtext';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {KnobModule} from 'primeng/knob';
import {LightboxModule} from 'primeng/lightbox';
import {ListboxModule} from 'primeng/listbox';
import {MegaMenuModule} from 'primeng/megamenu';
import {MenuModule} from 'primeng/menu';
import {MenubarModule} from 'primeng/menubar';
import {MessageModule} from 'primeng/message';
import {MessagesModule} from 'primeng/messages';
import {MultiSelectModule} from 'primeng/multiselect';
import {OrderListModule} from 'primeng/orderlist';
import {OrganizationChartModule} from 'primeng/organizationchart';
import {OverlayPanelModule} from 'primeng/overlaypanel';
import {PaginatorModule} from 'primeng/paginator';
import {PanelModule} from 'primeng/panel';
import {PanelMenuModule} from 'primeng/panelmenu';
import {PasswordModule} from 'primeng/password';
import {PickListModule} from 'primeng/picklist';
import {ProgressBarModule} from 'primeng/progressbar';
import {RadioButtonModule} from 'primeng/radiobutton';
import {RatingModule} from 'primeng/rating';
import {RippleModule} from 'primeng/ripple';
import {ScrollPanelModule} from 'primeng/scrollpanel';
import {ScrollTopModule} from 'primeng/scrolltop';
import {SelectButtonModule} from 'primeng/selectbutton';
import {SidebarModule} from 'primeng/sidebar';
import {SkeletonModule} from 'primeng/skeleton';
import {SlideMenuModule} from 'primeng/slidemenu';
import {SliderModule} from 'primeng/slider';
import {SplitButtonModule} from 'primeng/splitbutton';
import {SplitterModule} from 'primeng/splitter';
import {StepsModule} from 'primeng/steps';
import {TableModule} from 'primeng/table';
import {TabMenuModule} from 'primeng/tabmenu';
import {TabViewModule} from 'primeng/tabview';
import {TagModule} from 'primeng/tag';
import {TerminalModule} from 'primeng/terminal';
import {TimelineModule} from 'primeng/timeline';
import {TieredMenuModule} from 'primeng/tieredmenu';
import {ToastModule} from 'primeng/toast';
import {ToggleButtonModule} from 'primeng/togglebutton';
import {ToolbarModule} from 'primeng/toolbar';
import {TooltipModule} from 'primeng/tooltip';
import {TreeModule} from 'primeng/tree';
import {TreeTableModule} from 'primeng/treetable';
import {VirtualScrollerModule} from 'primeng/virtualscroller';
import {UserService} from './services/user.service';
import {NgxPermissionsModule, NgxPermissionsService} from 'ngx-permissions';
import { OfficeOrganizationPipe } from './pipes/officeOrganization.pipe';
import { CodeShortDescriptionPipe } from './pipes/code-short-description.pipe';
import { MarkersListPipe } from './pipes/markers-list.pipe';
import { CustomDissagregationOptionsListPipe } from './pipes/custom-dissagregation-options-list.pipe';
import { EnumValuesToLabelPipe } from './pipes/enum-values-to-label.pipe';
import { BooleanYesNoPipe } from './pipes/boolean-yes-no.pipe';
import { DissagregationsAssignationToIndicatorPipe } from './pipes/dissagregations-assignation-to-indicator.pipe';
import { CustomDissagregationsAssignationToIndicatorPipe } from './pipes/custom-dissagregations-assignation-to-indicator.pipe';
import { CodeDescriptionPipe } from './pipes/code-description.pipe';
import { ValuesStatePipe } from './pipes/values-state.pipe';
import { RolesListPipe } from './pipes/roles-list.pipe';
import { CodeDescriptionListPipe } from './pipes/code-description-list.pipe';
import { IndicatorPipe } from './pipes/indicator.pipe';
import { MonthPipe } from './pipes/month.pipe';
import { LoaderComponent } from './template/loader/loader.component';


@NgModule({
    declarations: [
        AppMainComponent,
        AppRightmenuComponent,
        AppMenuComponent,
        AppMenuitemComponent,
        AppConfigComponent,
        AppTopBarComponent,
        AppSearchComponent,
        AppFooterComponent,
        AppNotfoundComponent,
        AppErrorComponent,
        AppAccessdeniedComponent,
        OfficeOrganizationPipe,
        CodeShortDescriptionPipe,
        MarkersListPipe,
        CustomDissagregationOptionsListPipe,
        EnumValuesToLabelPipe,
        BooleanYesNoPipe,
        DissagregationsAssignationToIndicatorPipe,
        CustomDissagregationsAssignationToIndicatorPipe,
        CodeDescriptionPipe,
        ValuesStatePipe,
        RolesListPipe,
        CodeDescriptionListPipe,
        IndicatorPipe,
        MonthPipe,
        LoaderComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        AccordionModule,
        AutoCompleteModule,
        AvatarModule,
        AvatarGroupModule,
        BadgeModule,
        BreadcrumbModule,
        ButtonModule,
        CalendarModule,
        CardModule,
        CarouselModule,
        CascadeSelectModule,
        ChartModule,
        CheckboxModule,
        ChipModule,
        ChipsModule,
        CodeHighlighterModule,
        ConfirmDialogModule,
        ConfirmPopupModule,
        ColorPickerModule,
        ContextMenuModule,
        DataViewModule,
        DialogModule,
        DividerModule,
        DropdownModule,
        FieldsetModule,
        FileUploadModule,
        FullCalendarModule,
        GalleriaModule,
        InplaceModule,
        InputNumberModule,
        InputMaskModule,
        InputSwitchModule,
        InputTextModule,
        InputTextareaModule,
        KnobModule,
        LightboxModule,
        ListboxModule,
        MegaMenuModule,
        MenuModule,
        MenubarModule,
        MessageModule,
        MessagesModule,
        MultiSelectModule,
        OrderListModule,
        OrganizationChartModule,
        OverlayPanelModule,
        PaginatorModule,
        PanelModule,
        PanelMenuModule,
        PasswordModule,
        PickListModule,
        ProgressBarModule,
        RadioButtonModule,
        RatingModule,
        RippleModule,
        ScrollPanelModule,
        ScrollTopModule,
        SelectButtonModule,
        SidebarModule,
        SkeletonModule,
        SlideMenuModule,
        SliderModule,
        SplitButtonModule,
        SplitterModule,
        StepsModule,
        TableModule,
        TabMenuModule,
        TabViewModule,
        TagModule,
        TerminalModule,
        TimelineModule,
        TieredMenuModule,
        ToastModule,
        ToggleButtonModule,
        ToolbarModule,
        TooltipModule,
        TreeModule,
        TreeTableModule,
        VirtualScrollerModule,
        NgxPermissionsModule
    ],
    providers: [
        MenuService,
        BreadcrumbService,
        UserService,
        NgxPermissionsService

    ],
    exports: [
        AppMainComponent,
        AppRightmenuComponent,
        AppMenuComponent,
        AppMenuitemComponent,
        AppConfigComponent,
        AppTopBarComponent,
        AppSearchComponent,
        AppFooterComponent,
        AppNotfoundComponent,
        AppErrorComponent,
        AppAccessdeniedComponent,
        BooleanYesNoPipe,
        EnumValuesToLabelPipe,
        CodeDescriptionPipe,
        ValuesStatePipe,
        LoaderComponent
    ]
})
export class SharedModule {
}
