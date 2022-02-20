import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class VersionCheckService {
    // this will be replaced by actual hash post-build.js
    private currentHash = '{{POST_BUILD_ENTERS_HASH_HERE}}';

    constructor(private http: HttpClient) {
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


        this.http.get(url + '?t=' + new Date().getTime(), {headers})
            .subscribe(
                (response: any) => {
                    const hash = response.hash;
                    console.error('hash: ' + hash);
                    console.error('currenthash: ' + this.currentHash);
                    const hashChanged = this.hasHashChanged(this.currentHash, hash);
// If new version, do something
                    console.error('has changed: ' + hashChanged);
                    if (hashChanged) {
                        console.error('reloading');
                        window.location.reload();
// ENTER YOUR CODE TO DO SOMETHING UPON VERSION CHANGE
// for an example: location.reload();
                    }
// store the new hash so we wouldn't trigger versionChange again
// only necessary in case you did not force refresh
                    this.currentHash = hash;
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
