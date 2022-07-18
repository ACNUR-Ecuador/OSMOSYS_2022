import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class CheckBrowserService {

    constructor() {
    }

    public getBrowser() {
        const agent = window.navigator.userAgent.toLowerCase();
        const browser =
            agent.indexOf('edge') > -1 ? 'Microsoft Edge'
                : agent.indexOf('edg') > -1 ? 'Chromium-based Edge'
                    : agent.indexOf('opr') > -1 ? 'Opera'
                        : agent.indexOf('chrome') > -1 ? 'Chrome'
                            : agent.indexOf('trident') > -1 ? 'Internet Explorer'
                                : agent.indexOf('firefox') > -1 ? 'Firefox'
                                    : agent.indexOf('safari') > -1 ? 'Safari'
                                        : 'other';
        console.log(browser);
        return browser;

    }
}
