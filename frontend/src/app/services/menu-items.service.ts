import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Menu, MenuItemBackend} from "../shared/model/OsmosysModel";
import {UserService} from "./user.service";


const mainServiceUrl = environment.base_url + '/menuItem';

@Injectable({
    providedIn: 'root'
})
export class MenuItemsService {

    constructor(private http: HttpClient,
                private userService:UserService) {
    }

    public getAll(): Observable<MenuItemBackend[]> {
        return this.http.get<MenuItemBackend[]>(`${mainServiceUrl}`);
    }

    public getMenuStructure(): Observable<MenuItemBackend[]> {
        return this.http.get<MenuItemBackend[]>(`${mainServiceUrl}/getMenuStructure`);
    }
    public getById(id:number): Observable<MenuItemBackend> {
        return this.http.get<MenuItemBackend>(`${mainServiceUrl}/${id}`);
    }

    public save(menuItem: MenuItemBackend): Observable<number> {
        return this.http.post<number>(`${mainServiceUrl}`, menuItem);
    }

    public update(menuItem: MenuItemBackend): Observable<number> {
        return this.http.put<number>(`${mainServiceUrl}`, menuItem);
    }

    public processMenusItem(items: MenuItemBackend[]): Menu[] {
        let result:Menu[]=[];
        items.sort((a, b) => a.order-b.order);

        for (let item of items) {
            if(item.state==='ACTIVO'){
                if(item.restricted){
                    if(!item.organizations|| item.organizations.length<1){
                        continue;
                    }
                    const allowedOrganizationIds=item.organizations.map(value => value.id);
                    const currentOrganization=this.userService.getLogedUsername().organization.id;
                    if(!allowedOrganizationIds.includes(currentOrganization)){
                        continue;
                    }
                }
                let itemMenu=this.processMenuItemService(item);
                if(item.children && item.children.length>0){
                    itemMenu.items=this.processMenusItem(item.children);
                }

                result.push(itemMenu);
            }
        }

        return result;
    }

    public processMenuItemService(item: MenuItemBackend): Menu {
        let proccesed: Menu = {};
        //queryParams:{'recent': 'https://app.powerbi.com/view?r=eyJrIjoiZTJhNzhmZDUtMTA5MS00YjgxLTk0YmItZGU0OTE4ZGJmNWJlIiwidCI6ImU1YzM3OTgxLTY2NjQtNDEzNC04YTBjLTY1NDNkMmFmODBiZSIsImMiOjh9&pageName=ReportSectiond86c1c7752280a23a22a'}
        proccesed.queryParams = {'itemId':item.id};
        proccesed.label = item.label;
        proccesed.icon = item.icon;
        proccesed.roles = item.assignedRoles;
        if (item.openInNewTab) {
            proccesed.target = '_blank';
        }
        if (item.powerBi) {
            proccesed.routerLink = ['/reports/powerbiReportTemplate']
        }
        if (item.children && item.children.length > 0) {
            proccesed.items = [];
            item.children.forEach(value => {
                proccesed.items.push(this.processMenuItemService(value));
            });
        }
        //proccesed.menuData=item;
        return proccesed;
    }
}
