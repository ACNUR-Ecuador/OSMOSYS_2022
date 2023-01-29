import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {VersionModel} from "../shared/model/OsmosysModel";

@Injectable({
  providedIn: 'root'
})
export class VersionCheckService {


    constructor(private http: HttpClient) {
    }
    // this will be replaced by actual hash post-build.js
    private currentHash = '{{POST_BUILD_ENTERS_HASH_HERE}}';

    private counter = 0;

    private static dateDifference(d1: number) {
        return Math.floor((new Date().getTime() - d1) / (3600 * 1000));
    }

    /**
     * Checks in every set frequency the version of frontend application
     * @param url where to check url
     * @param frequency to check {number} frequency - in milliseconds, defaults to 60 minutes
     */
    public initVersionCheck(url, frequency = 1000 * 60 * 60) {
        setInterval(() => {
            this.checkVersion(url);
        }, frequency);
    }

    /**
     * Will do the call and check if the hash has changed or not
     * @param url where to check url
     */
    public checkVersion(url) {
// timestamp these requests to invalidate caches
        const headers = new HttpHeaders({'Content-Type': 'application/json'});
        headers.append('Access-Control-Allow-Origin', '*');
        // headers.append('Access-Control-Allow-Origin', origin);
        headers.append('Access-Control-Allow-Headers', 'content-type, withcredentials, Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers');
        headers.append('Access-Control-Allow-Credentials', 'true');
        headers.append('Access-Control-Allow-Methods', 'GET, HEAD, OPTIONS, POST, PUT');

        this.counter++;
        this.http.get(url + '?t=' + new Date().getTime(), {headers})
            .subscribe(
                (response: any) => {
                    const hash = response.hash;
                    console.error('hash: ' + hash);
                    console.error('currenthash: ' + this.currentHash);
                    const hashChanged = this.hasHashChanged(this.currentHash, hash);
                    // If new version, do something
                    console.error('has changed: ' + hashChanged);
                    this.currentHash = hash;
                    let version: VersionModel;
                    if (!window.localStorage.getItem('version')) {
                        version = new VersionModel(Number(this.currentHash), new Date().getTime(), 0);
                    } else {
                        version = JSON.parse(window.localStorage.getItem('version'));
                        version.loaderCounter = version.loaderCounter + 1;
                    }
                    window.localStorage.setItem('version', JSON.stringify(version));
                    if (hashChanged && version.loaderCounter < 5) {
                        console.error('reloading');
                        window.location.reload();
                        // ENTER YOUR CODE TO DO SOMETHING UPON VERSION CHANGE
                        // for an example: location.reload();
                    } else if (version.loaderCounter > 4 && VersionCheckService.dateDifference(version.loaderTime) > 24) {
                        version.loaderCounter = 1;
                        version.loaderTime = new Date().getTime();
                        window.localStorage.setItem('version', JSON.stringify(version));
                    }
// store the new hash so we wouldn't trigger versionChange again
// only necessary in case you did not force refresh

                },
                (err) => {
                    console.error(err, 'Could not get version');

                }
            );
    }

    // noinspection JSMethodCanBeStatic
    /**
     * Checks if hash has changed.
     * This file has the JS hash, if it is a different one than in the version.json
     * we are dealing with version change
     * @param currentHash current version currentHash
     * @param newHash hash newHash
     * @returns true if changes {boolean}
     */
    private hasHashChanged(currentHash, newHash): boolean {
        if (!currentHash || currentHash === '{{POST_BUILD_ENTERS_HASH_HERE}}') {
            return false;
        }
        const change: boolean = currentHash !== newHash;
        console.error('changed: ' + change);
        return change;
    }
}
