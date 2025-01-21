import { Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'app-footer',
    templateUrl: './app.footer.component.html'
})
export class AppFooterComponent {
    operationName=`${environment.operationName}`;
    isProduction = `${environment.production}`;
    backgroundColor: string = 'white'; // Color inicial
    showDialog = false;


    constructor(public layoutService: LayoutService) { 
        this.isProduction === 'true' ? this.backgroundColor = 'white' : 
        this.backgroundColor = '#ffd700'; // Color inicial

    }

    
    get logo() {
        return this.layoutService.config.colorScheme === 'light' ? 'dark' : 'white';
    }

}
