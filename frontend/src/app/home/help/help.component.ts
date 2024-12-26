import {Component} from '@angular/core';

@Component({
    selector: 'app-help',
    templateUrl: './help.component.html',
    styleUrls: ['./help.component.scss']
})
export class HelpComponent {

    visibleMember: number = -1;


    constructor() {
    }


}
