import { Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'app-footer',
    templateUrl: './app.footer.component.html'
})
export class AppFooterComponent {
    operationName=`${environment.operationName}`;
    constructor(public layoutService: LayoutService) { }

    showDialog = false;
    
    get logo() {
        return this.layoutService.config.colorScheme === 'light' ? 'dark' : 'white';
    }

}
