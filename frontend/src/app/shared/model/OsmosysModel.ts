export class Organization {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: number;
    public code: string;
    public state: string;
    public description: string;
    public acronym: string;
}

export class Office {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: number;
    public state: string;
    public description: string;
    public acronym: string;
    public type: string;
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

export class Pillar {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: string;
    public code: string;
    public shortDescription: string;
    public description: string;
    public state: string;
}

export class Situation {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: string;
    public code: string;
    public shortDescription: string;
    public description: string;
    public state: string;
}


export class Statement {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: string;
    public code: string;
    public shortDescription: string;
    public description: string;
    public areaType: string;
    public state: string;
    public parentStatement: Statement;
    public area: Area;
    public pillar: Pillar;
    public situation: Situation;
    public periodStatementAsignations: PeriodStatementAsignation[];
}

export class PeriodStatementAsignation {
    constructor() {
        this.state = 'ACTIVO';
    }
    public id: string;
    public state: string;
    public period: Period;
    public populationCoverage: number;
}
