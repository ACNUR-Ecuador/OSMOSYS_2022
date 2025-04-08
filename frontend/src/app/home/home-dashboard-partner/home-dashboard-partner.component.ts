import {AfterViewInit, ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {IndicatorExecution, Month, Period, ProjectResume, Quarter} from "../../shared/model/OsmosysModel";
import {UserService} from "../../services/user.service";
import { MetabaseService } from '../../services/metabase.service';
import { SelectItem } from 'primeng/api';


@Component({
    selector: 'app-home-dashboard-partner',
    templateUrl: './home-dashboard-partner.component.html',
    styleUrls: ['./home-dashboard-partner.component.scss']
})
export class HomeDashboardPartnerComponent implements OnInit, AfterViewInit {
    


    @Input()
    periods: Period[];
    @Input()
    currentPeriod: Period;
    @Input()
    isPartner: boolean;


    renderProject = false;

    metabaseIframeUrl: any;


    constructor(
     
        public userService: UserService,
        private changeDetectorRef: ChangeDetectorRef,
        private metabaseService: MetabaseService, // Inyectamos el servicio de Metabase
    ) {    }

    ngAfterViewInit(): void {
        throw new Error('Method not implemented.');
    }

    ngOnInit(): void {
        
        this.metabaseService.getMetabaseToken().subscribe(response => {
            this.metabaseIframeUrl = response.iframeUrl;
            this.changeDetectorRef.detectChanges();
          }, error => {
            console.error('Error al obtener el token de Metabase:', error);
          });
    }
}
