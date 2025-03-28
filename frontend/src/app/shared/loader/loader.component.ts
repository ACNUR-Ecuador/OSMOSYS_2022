import {ChangeDetectorRef, Component, Input, OnInit, ViewChild} from '@angular/core';
import {LoaderService} from '../../services/loader.service';
import {Loader} from '../model/loader.model';


@Component({
    selector: 'app-loader',
    templateUrl: './loader.component.html',
    styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {

    // @ts-ignore
    @ViewChild('loader') boxElement;

    @Input() public id = 'global';
    public show: boolean;
    public progress: number = 0;
    public statusText: string = '';

    constructor(private loaderService: LoaderService, private cd: ChangeDetectorRef) {
    }

    public ngOnInit(): void {        
        this.loaderService.loaderStatus$.subscribe((response: Loader) => {
            this.show = this.id === response.id && response.status;
        
            this.progress = response.progress;            
            this.statusText = response.statusText;            
            this.cd.detectChanges();
        });
    }

    ngAfterViewCheckedx(): void {
        const show = this.isShowExpand();
        if (show !== this.show) {
            this.show = show;
            this.cd.detectChanges();
        }
    }

    isShowExpand() {
        return this.boxElement && this.boxElement.nativeElement.scrollHeight > this.boxElement.nativeElement.clientHeight;
    }
    

}
