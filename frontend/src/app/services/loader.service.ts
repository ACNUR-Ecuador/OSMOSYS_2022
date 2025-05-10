/* tslint:disable */
import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {Loader} from '../shared/model/loader.model';
import { LogService } from './log.service';


@Injectable({
    providedIn: 'root'
})
export class LoaderService {
    loader = new BehaviorSubject<Loader>({id: 'global', status: false});

    loaderStatus$ = this.loader.asObservable();
    /**
     * Contains in-progress loading requests
     */
    loadingMap: Map<string, boolean> = new Map<string, boolean>();

    constructor(private logService: LogService) {}

    /**
     * Sets the loadingSub property value based on the following:
     * - If loading is true, add the provided url to the loadingMap with a true value, set loadingSub value to true
     * - If loading is false, remove the loadingMap entry and only when the map is empty will we set loadingSub to false
     * This pattern ensures if there are multiple requests awaiting completion, we don't set loading to false before
     * other requests have completed. At the moment, this function is only called from the @link{HttpRequestInterceptor}
     * @param loading {boolean}
     * @param url {string}
     */
    setLoading(loading: boolean, url: string): void {
        // log('service loading: ' + loading);
        //  log('service url: ' + url);
        if (!url) {
            throw new Error('The request URL must be provided to the LoadingService.setLoading function');
        }
        if (loading) {
            this.loadingMap.set(url, loading);
            this.loader.next({id: 'global', status: true});
        } else if (!loading && this.loadingMap.has(url)) {
            this.loadingMap.delete(url);
        }
        if (this.loadingMap.size === 0) {
            this.loader.next({id: 'global', status: false});
            this.logService.log('hideLoader 1');

        }
    }

    /**
     * Update progress
     * @param {string} url
     * @param {number} progress
     */
    public updateProgress(url: string, progress: number, statusText?: string): void {
        if (progress >= 100) {
            progress = undefined;
        }
        this.loader.next({ id: 'global', status: true, progress: progress, statusText: statusText });

    }

    /**
     * Show loader
     * @param {string} id
     */
    public showLoader(id: string = 'global'): void {
        this.loader.next({id, status: true});
    }

    /**
     * Hide loader
     * @param {string} id
     */
    public hideLoader(id: string = 'global'): void {
        this.loader.next({id, status: false});
        this.logService.log('hideLoader');
    }
}
