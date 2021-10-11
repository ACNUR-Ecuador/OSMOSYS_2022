export class Organization {
    public id: number;
    public code: string;
    public state: string;
    public description: string;
    public acronym: string;
}

export class Office {
    public id: number;
    public state: string;
    public description: string;
    public acronym: string;
    public officeType: string;
    public parentOffice: Office;
    public childOffices: Office[];
}
