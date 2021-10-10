import {Component} from '@angular/core';
import {BreadcrumbService} from '../../shared/template/app.breadcrumb.service';

@Component({
    templateUrl: './emptydemo.component.html'
})
export class EmptyDemoComponent {

    constructor(private breadcrumbService: BreadcrumbService) {
        this.breadcrumbService.setItems([
            {label: 'Empty Page'}
        ]);
    }
}
