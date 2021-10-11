import {Office, Organization} from './OsmosysModel';

export class User {
    public id: number;
    public name: string;
    public username: string;
    public password?: string;
    public email?: string;
    public organization?: Organization;
    public office?: Office;
    public roles?: Role[];
    public state: string;

}

export class Role {
    public id: number;
    public name: string;
    public state: string;
}
