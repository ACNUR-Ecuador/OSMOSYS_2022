import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";

@Component({
    selector: 'app-power-bi-report-template',
    templateUrl: './power-bi-report-template.component.html',
    styleUrls: ['./power-bi-report-template.component.scss']
})
export class PowerBiReportTemplateComponent implements OnInit {
    recentParam: SafeUrl;

    constructor(private route: ActivatedRoute,
                protected _sanitizer: DomSanitizer) {
    }

    ngOnInit(): void {
        console.log("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
        this.route.queryParams.subscribe(params => {
            // Access the query parameters here
            this.recentParam = params['recent']; // Access the 'recent' query parameter
            // You can now use the value of the query parameter as needed
            console.log(this.recentParam); // Output the value of the 'recent' query parameter
        });
    }

}
