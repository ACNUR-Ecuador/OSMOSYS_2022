import {Component} from '@angular/core';
import {AppComponent} from '../../app.component';
import {BreadcrumbService} from '../../shared/services/app.breadcrumb.service';

@Component({
    templateUrl: './app.invoice.component.html'
})
export class AppInvoiceComponent {

    constructor(private breadcrumbService: BreadcrumbService, public app: AppComponent) {
        this.breadcrumbService.setItems([
            {label: 'Invoice'}
        ]);
    }

    print() {
        window.print();
    }
}
