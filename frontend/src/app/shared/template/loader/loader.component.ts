import {ChangeDetectorRef, Component, Input, OnInit, ViewChild} from '@angular/core';
import {LoaderService} from '../../services/loader.service';
import {Loader} from '../../model/loader.model';

@Component({
    selector: 'app-loader',
    templateUrl: './loader.component.html',
    styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {

    @ViewChild('loader') boxElement;

    @Input() public id = 'global';
    public show: boolean;

    constructor(private loaderService: LoaderService, private cd: ChangeDetectorRef) {
    }

    public ngOnInit(): void {
        // console.log('loader c init');
        this.loaderService.loaderStatus$.subscribe((response: Loader) => {
            // console.log('responce' + response.id);
            // console.log('responce' + response.status);
            // console.log('responce id' + this.id);
            this.show = this.id === response.id && response.status;
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
        // console.log('show: ' + show);
        return this.boxElement && this.boxElement.nativeElement.scrollHeight > this.boxElement.nativeElement.clientHeight;
    }

}
