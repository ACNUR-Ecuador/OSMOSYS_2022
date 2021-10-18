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

export class Area {

    constructor() {
        this.state = 'ACTIVO';
    }

    public id: number;
    public state: string;
    public areaType: string;
    public code: string;
    public shortDescription: string;
    public description: string;
    public definition: string[];
}

export class Period {

    constructor() {
        this.state = 'ACTIVO';
    }

    public id: number;
    public year: number;
    public state: string;
}
