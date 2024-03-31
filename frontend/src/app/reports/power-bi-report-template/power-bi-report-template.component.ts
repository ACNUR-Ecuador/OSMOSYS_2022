import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {MenuItemsService} from "../../services/menu-items.service";

@Component({
    selector: 'app-power-bi-report-template',
    templateUrl: './power-bi-report-template.component.html',
    styleUrls: ['./power-bi-report-template.component.scss']
})
export class PowerBiReportTemplateComponent implements OnInit {
    itemId: number;

    iframeDef;

    constructor(private route: ActivatedRoute,
                private menuItemsService: MenuItemsService
    ) {
    }

    ngOnInit(): void {
         this.route.queryParams.subscribe(params => {
            // Access the query parameters here
            this.itemId = params['itemId']; // Access the 'recent' query parameter
            console.log(this.itemId);
            this.menuItemsService.getById(this.itemId).subscribe({
                next: value => {
                    const recentParamv2: string = value.url;

                    this.iframeDef = `<iframe class="powerbi-total-hidde" style=" width: 98%;" src=" ${recentParamv2}"> </iframe>`;
                }
            });

            // You can now use the value of the query parameter as needed

        });
    }

}
