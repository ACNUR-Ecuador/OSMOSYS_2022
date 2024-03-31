import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Menu, MenuItemBackend} from "../shared/model/OsmosysModel";


const mainServiceUrl = environment.base_url + '/menuItem';

@Injectable({
    providedIn: 'root'
})
export class MenuItemsService {

    constructor(private http: HttpClient) {
    }

    public getAll(): Observable<MenuItemBackend[]> {
        return this.http.get<MenuItemBackend[]>(`${mainServiceUrl}`);
    }

    public getMenuStructure(): Observable<MenuItemBackend[]> {
        return this.http.get<MenuItemBackend[]>(`${mainServiceUrl}/getMenuStructure`);
    }

    public save(menuItem: MenuItemBackend): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, menuItem);
    }

    public update(menuItem: MenuItemBackend): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, menuItem);
    }

    public processMenuItemService(item: MenuItemBackend): Menu {
        let proccesed: Menu={};
        proccesed.idItem = item.id;
        proccesed.label = item.label;
        proccesed.icon = item.icon;
        proccesed.roles = item.assignedRoles;
        if (item.openInNewTab) {
            proccesed.target = '_blank';
        }
        if (item.powerBi) {
            proccesed.routerLink = ['/reports/powerbiReportTemplate']
        }
        if(item.children && item.children.length>0){
            proccesed.items =[];
            item.children.forEach(value => {
                proccesed.items.push(this.processMenuItemService(value));
            });
        }
        //proccesed.menuData=item;
        return proccesed;
    }
}
