import {EnumsState} from './UtilsModel';
import {User} from './User';

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
    public generalIndicator?: GeneralIndicator;
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
        this.compassIndicator = false;
    }

    public id: number;
    public code: string;
    public productCode: string;
    public description: string;
    public category: string;
    public state: string;
    public indicatorType: string;
    public measureType: string;
    public frecuency: string;
    public areaType: string;
    public isMonitored: boolean;
    public isCalculated: boolean;
    public totalIndicatorCalculationType: string;
    public markers: Marker[];
    public compassIndicator: boolean;
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

export class Project {

    constructor() {
        this.state = EnumsState.ACTIVE;
        this.locations = [];
    }

    public id: number;
    public code: string;
    public name: string;
    public state: string;
    public organization: Organization;
    public period: Period;
    public startDate: Date;
    public endDate: Date;
    public locations: Canton[];
    public focalPoint?: User;
}

export class ProjectResume {
    public id: number;
    public code: string;
    public name: string;
    public state: string;
    public organizationId: number;
    public organizationDescription: string;
    public organizationAcronym: string;
    public periodId: number;
    public periodYear: number;
}

export class Canton {
    public id: number;
    public code: string;
    public description: string;
    public state: string;
    public provincia: Provincia;
    public office: Office;
}

export class CantonForList extends Canton {

    public enabled: boolean;
}

export class Provincia {
    public id: number;
    public code: string;
    public description: string;
    public state: string;
}

export class GeneralIndicator {
    constructor() {
        this.state = 'ACTIVO';
        this.dissagregationAssignationsToGeneralIndicator = [];
    }

    public id: number;
    public description: string;
    public measureType: string;
    public state: string;
    public period: Period;
    public dissagregationAssignationsToGeneralIndicator: DissagregationAssignationToGeneralIndicator[];
}

export class DissagregationAssignationToGeneralIndicator {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: number;
    public state: string;
    public dissagregationType: string;
}

export class IndicatorExecutionAdministrationResumeWeb {
    public id: number;
    public commentary: string;
    public target: number;
    public indicatorDescription: string;
    public activityDescription: string;
    public indicatorType: string;
    public state: string;
    public totalExecution: number;
    public executionPercentage: number;
    public quarters: QuarterResumeWeb[];
}

export class IndicatorExecutionGeneralIndicatorAdministrationResumeWeb extends IndicatorExecutionAdministrationResumeWeb {
}

export class IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb extends IndicatorExecutionAdministrationResumeWeb {
    public indicatorCode: string;
}

export class IndicatorExecutionResumeWeb {
    public id: number;
    public commentary: string;
    public target: number;
    public indicator: Indicator;
    public indicatorType: string;
    public state: string;
    public totalExecution: number;
    public executionPercentage: number;
    public quarters: Quarter[];
}

export class Quarter {
    public id: number;
    public quarter: string;
    public commentary: string;
    public order: number;
    public year: number;
    public target: number;
    public totalExecution: number;
    public executionPercentage: number;
    public state: string;
    public months: Month[];
}

export class Month {
    public id: number;
    public month: string;
    public order: number;
    public year: number;
    public state: string;
    public totalExecution: number;
    public commentary: string;
}

export class QuarterMonthResume {
    public quarterId: number;
    public quarterMonthCount: number;
    public quarterQuarter: string;
    public quarterOrder: number;
    public quarterYear: number;
    public quarterTarget: number;
    public quarterTotalExecution: number;
    public quarterExecutionPercentage: number;
    public quarterSpan: boolean;
    public quarterSpanCount: number;

    public monthId: number;
    public monthMonth: string;
    public monthOrder: number;
    public monthYear: number;
    public monthTotalExecution: number;

    public yearSpan: boolean;
    public yearSpanCount: number;
}


export class QuarterResumeWeb {
    public id: number;
    public quarter: string;
    public commentary: string;
    public order: number;
    public year: number;
    public target: number;
    public totalExecution: number;
    public executionPercentage: number;
    public state: string;
}

export class TargetUpdateDTOWeb {
    public indicatorExecutionId: number;
    public quarters: QuarterResumeWeb[];
}

export class IndicatorExecutionAssigment {
    constructor() {
        this.state = 'ACTIVO';
        this.locations = [];
    }

    public id: number;
    public commentary: string;
    public indicator: Indicator;
    public state: string;
    public period: Period;
    // socios
    public project: Project;
    // direct implementation
    public reportingOffice: Office;
    public assignedUser: User;
    public assignedUserBackup: User;
    public locations: Canton[];
}

export class StartEndDatesWeb {
    public startDate: Date;
    public endDate: Date;
}

export class MonthValues {
    public month: Month;
    public indicatorValuesMap: any;

}

export class IndicatorValue {
    public id: string;
    public state: string;
    public monthEnum: string;
    public dissagregationType: string;
    public populationType: string;
    public countryOfOrigin: string;
    public genderType: string;
    public ageType: string;
    public diversityType: string;
    public location: Canton;
    public showValue: boolean;
    public value: number;
    public denominatorValue: number;
    public numeratorValue: number;
}
