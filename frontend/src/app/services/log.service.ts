import {Injectable} from '@angular/core';
import { environment } from 'src/environments/environment';

const debug = !environment.production;

@Injectable({
    providedIn: 'root'
})
export class LogService {
   
     constructor() {}

     private formatMessage(message: string): string {
         const now = new Date();
         return `${now.toLocaleTimeString('en-GB')} - ${message}`;
        }
 
     public log(message: string) {
         if (debug) {                      
             console.log(this.formatMessage(message));
         }
     }
 
     public warn(message: string) {
         if (debug) {
             console.warn(this.formatMessage(message));
         }
     }
 
     public error(message: string) {
         if (debug) {
             console.error(this.formatMessage(message));
         }
     }
}
