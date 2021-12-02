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

export class Marker {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: string;
    public state: string;
    public type: string;
    public subType: string;
    public description: string;
    public shortDescription: string;
}

export class CustomDissagregation {
    constructor() {
        this.state = 'ACTIVO';
        this.controlTotalValue = false;
        this.customDissagregationOptions = [];
    }

    public id: number;
    public name: string;
    public description: string;
    public controlTotalValue: boolean;
    public state: string;
    public customDissagregationOptions: CustomDissagregationOption[];
}

export class CustomDissagregationOption {
    constructor() {
        this.state = 'ACTIVO';
        this.markers = [];
    }

    public id: number;
    public name: string;
    public description: string;
    public state: string;
    public markers: Marker[];
}

export class Indicator {
    constructor() {
        this.state = 'ACTIVO';
        this.markers = [];
        this.statements = [];
        this.dissagregationsAssignationToIndicator = [];
        this.customDissagregationAssignationToIndicators = [];
        this.isCalculated = false;
        this.isMonitored = true;
    }

    public id: number;
    public code: string;
    public description: string;
    public guidePartners: string;
    public guideDirectImplementation: string;
    public state: string;
    public indicatorType: string;
    public measureType: string;
    public frecuency: string;
    public areaType: string;
    public isMonitored: boolean;
    public isCalculated: boolean;
    public totalIndicatorCalculationType: string;
    public markers: Marker[];
    public statements: Statement[];
    public dissagregationsAssignationToIndicator: DissagregationAssignationToIndicator[];
    public customDissagregationAssignationToIndicators: CustomDissagregationAssignationToIndicator[];
}

export class DissagregationAssignationToIndicator {
    constructor() {
        this.state = 'ACTIVO';
        this.dissagregationFilterIndicators = [];
    }

    public id: string;
    public state: string;
    public dissagregationType: string;
    public dissagregationFilterIndicators: DissagregationFilterIndicator[];
}

export class DissagregationFilterIndicator {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: number;
    public state: string;
    public dissagregationType: string;
    public populationType: string;
    public countryOfOrigin: string;
    public genderType: string;
    public ageType: string;

}

export class CustomDissagregationAssignationToIndicator {
    constructor() {
        this.state = 'ACTIVO';
        this.customDissagregationFilterIndicators = [];
    }

    public id: number;
    public state: string;
    public customDissagregation: CustomDissagregation;
    public customDissagregationFilterIndicators: CustomDissagregationFilterIndicator[];
}

export class CustomDissagregationFilterIndicator {
    constructor() {
        this.state = 'ACTIVO';
        this.customDissagregationOptions = [];
    }

    public id: number;
    public state: string;
    public customDissagregationOptions: CustomDissagregationOption[];
}
