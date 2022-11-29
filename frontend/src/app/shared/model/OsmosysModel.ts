import {EnumsIndicatorType, EnumsState} from './UtilsModel';
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

export class AreaResume {
    public area: Area;
    public numberOfIndicators: number;
    public numberOfLateIndicators: number;
    public numberOfSoonReportIndicators: number;
    public indicators: Indicator[];
    public indicatorExecutionIds: number[];
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
    public productCode: string;
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
        this.dissagregationsAssignationToIndicator = [];
        this.customDissagregationAssignationToIndicators = [];
        this.isCalculated = false;
        this.isMonitored = true;
        this.compassIndicator = false;
        this.blockAfterUpdate = false;
    }

    public id: number;
    public code: string;
    public description: string;
    public category: string;
    public qualitativeInstructions: string;
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
    public statement: Statement;
    public unit: string;
    public blockAfterUpdate: boolean;
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
    public period: Period;
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
    public period: Period;
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
    public updateAllLocationsIndicators?: boolean;
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

export class QuarterState {
    public quarter: string;
    public year: number;
    public blockUpdate: boolean;
    public quarterYearOrder: number;
}

export class MonthState {
    public year: number;
    public Month: string;
    public order: number;
    public blockUpdate: boolean;
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

export class IndicatorExecution {
    public id: number;
    public commentary: string;
    public activityDescription: string;
    public indicatorType: string;
    public state: string;
    public target: number;
    public compassIndicator: boolean;
    public indicator: Indicator;
    public period: Period;
    public totalExecution: number;
    public executionPercentage: number;
    public lastReportedQuarter: Quarter;
    public lastReportedMonth: Month;
    public quarters: Quarter[];
    public late: string;
    public project: Project;
    public projectStatement: Statement;
    public reportingOffice: Office;
    public supervisorUser: User;
    public assignedUser: User;
    public assignedUserBackup: User;
    public locations: Canton[];
    public keepBudget: boolean;
    public assignedBudget: number;
    public availableBudget: number;
    public totalUsedBudget: number;

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
    public months?: Month[];
    public late?: string;
}

export class Month {
    public id: number;
    public month: string;
    public order: number;
    public year: number;
    public state: string;
    public totalExecution: number;
    public commentary: string;
    public sources: string[];
    public sourceOther: string;
    public checked: boolean;
    public usedBudget: number;
    public blockUpdate: boolean;
    public late?: string;
}

/********usado en pantalla*****/
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
    public monthLate: string;

    public yearSpan: boolean;
    public yearSpanCount: number;

    public blockUpdate: boolean;
}


export class TargetUpdateDTOWeb {
    public indicatorExecutionId: number;
    public indicatorType: EnumsIndicatorType;
    public quarters: Quarter[];
    public totalTarget: number;
}

export class IndicatorExecutionAssigment {
    constructor() {
        this.state = 'ACTIVO';
        this.locations = [];
    }

    public id: number;
    public indicator: Indicator;
    public state: string;
    public period: Period;
    // socios
    public project?: Project;
    public projectStatement?: Statement;
    public activityDescription?: string;
    public locations?: Canton[];
    // direct implementation
    public reportingOffice?: Office;
    public supervisorUser?: User;
    public assignedUser?: User;
    public assignedUserBackup?: User;
    public keepBudget: boolean;
    public assignedBudget: number;
}

export class StartEndDatesWeb {
    public startDate: Date;
    public endDate: Date;
}

export class MonthValues {
    public month: Month;
    public indicatorValuesMap: any;
    public customDissagregationValues: CustomDissagregationValues[];

}

export class CustomDissagregationValues {
    public customDissagregation: CustomDissagregation;
    public indicatorValuesCustomDissagregation: IndicatorValueCustomDissagregationWeb[];
}

export class IndicatorValueCustomDissagregationWeb {
    id: number;
    state: string;
    monthEnum: string;
    customDissagregationOption: CustomDissagregationOption;
    showValue: boolean;
    value: number;
    denominatorValue: number;
    numeratorValue: number;
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
    public agePrimaryEducationType: string;
    public ageTertiaryEducationType: string;
    public diversityType: string;
    public location: Canton;
    public showValue: boolean;
    public value: number;
    public denominatorValue: number;
    public numeratorValue: number;
}

export class VersionModel {

    constructor(version: number, loaderTime: number, loaderCounter: number) {
        this.version = version;
        this.loaderTime = loaderTime;
        this.loaderCounter = loaderCounter;
    }

    public version: number;
    public loaderTime: number;
    public loaderCounter: number;
}

export enum AreaType {
    IMPACTO = 'IMPACTO',
    RESULTADOS = 'RESULTADOS',
    PRODUCTO = 'PRODUCTO',
    APOYO = 'APOYO',
}

export class AppConfiguration {
    public id: number;
    public nombre: string;
    public descripcion: string;
    public clave: string;
    public valor: string;
}
