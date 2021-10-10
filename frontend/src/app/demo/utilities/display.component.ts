import {Component, OnInit} from '@angular/core';
import {BreadcrumbService} from '../../shared/template/app.breadcrumb.service';


@Component({
    templateUrl: './display.component.html'
})
export class DisplayComponent implements OnInit {

    constructor(private breadcrumbService: BreadcrumbService) {
        this.breadcrumbService.setItems([
            {label: 'Display'}
        ]);
    }

    ngOnInit(): void {
    }

    delay(ms: number) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

}
