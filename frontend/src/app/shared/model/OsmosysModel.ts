import {EnumsIndicatorType, EnumsState} from './UtilsModel';
import {User} from './User';
import {MenuItem} from "primeng/api";

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
    public administrators?: User[];
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
    public periodAgeDissagregationOptions?: StandardDissagregationOption[];
    public periodGenderDissagregationOptions?: StandardDissagregationOption[];
    public periodPopulationTypeDissagregationOptions?: StandardDissagregationOption[];
    public periodDiversityDissagregationOptions?: StandardDissagregationOption[];
    public periodCountryOfOriginDissagregationOptions?: StandardDissagregationOption[];
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
    }

    public id: number;
    public name: string;
    public description: string;
    public state: string;
}

export class Indicator {
    constructor() {
        this.state = 'ACTIVO';
        this.dissagregationsAssignationToIndicator = [];
        this.customDissagregationAssignationToIndicators = [];
        this.isCalculated = false;
        this.isMonitored = true;
        this.compassIndicator = false;
        this.blockAfterUpdate = false;
        
    }

    public id: number;
    public code: string;
    public regionalCode: string;
    public description: string;
    public category: string;
    public instructions?: string;
    public qualitativeInstructions: string;
    public state: string;
    public indicatorType: string;
    public measureType: string;
    public frecuency: string;
    public areaType: string;
    public isMonitored: boolean;
    public isCalculated: boolean;
    public totalIndicatorCalculationType: string;
    public compassIndicator: boolean;
    public coreIndicator: boolean;
    public statement: Statement;
    public unit: string;
    public blockAfterUpdate: boolean;
    public dissagregationsAssignationToIndicator: DissagregationAssignationToIndicator[];
    public customDissagregationAssignationToIndicators: CustomDissagregationAssignationToIndicator[];
    public periods?:string;
    public resultManager: User;
    public quarterReportCalculation?:string;
    public aggregationRuleComment?:string;
}


export class CoreIndicator {
    constructor() {
    }

    public id: number;
    public code: string;
    public areaCode: string;
    public description: string;
    public measureType: string;
    public frecuency: string;
    public guiadance: string;
    public state: string;   
}



export class DissagregationAssignationToIndicator {
    constructor() {
        this.state = 'ACTIVO';
        this.useCustomAgeDissagregations = false;
    }

    public id: string;
    public state: string;
    public period: Period;
    public dissagregationType: string;
    public useCustomAgeDissagregations: boolean;
    public customIndicatorOptions: StandardDissagregationOption[];

}

export class StandardDissagregationOption {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: number;
    public name: string;
    public order: number;
    public groupName: string;
    public regionGroupName: string;
    public otherGroupName: string;
    public state: string;
    public type?: string;
    children?: StandardDissagregationOption[];  

}

export class CustomDissagregationAssignationToIndicator {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: number;
    public state: string;
    public period: Period;
    public customDissagregation: CustomDissagregation;
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
        this.focalPoints = [];
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
    public focalPoints: User[];
    public updateAllLocationsIndicators?: boolean;
    public partnerManager?: User;
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

export class Canton extends StandardDissagregationOption {

    public code: string;
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
    public target?: number;
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
    public populationType: StandardDissagregationOption;
    public countryOfOrigin: StandardDissagregationOption;
    public genderType: StandardDissagregationOption;
    public ageType: StandardDissagregationOption;
    public diversityType: StandardDissagregationOption;
    public location: StandardDissagregationOption | Canton;
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

export class ImportFile {
    public period: Period;
    public fileName: string;
    public file: string;
}

export class YearMonth {
    public year: number;
    public month: string;
    public monthYearOrder: number;
}

export class EnumWeb {
    public value: string;
    public label: string;
    public order: number;
    public locationsDissagregation?: boolean;
    public ageDissagregation?: boolean;
    public standardDissagregationTypes?: string[];
    public numberOfDissagregations?: number;
}


export class MenuItemBackend {

    constructor() {
        this.state = 'ACTIVO';
        this.powerBi = false;
        this.restricted = false;
        this.openInNewTab = false;
    }

    public id: number;
    public state: string;
    public label: string;
    public icon: string;
    public assignedRoles: string[];
    public powerBi: boolean;
    public restricted: boolean;
    public order: number;
    public organizations: Organization[];
    public url: string;
    public parent: MenuItemBackend;
    public openInNewTab: boolean;
    public children?: MenuItemBackend[];
}

export interface Menu extends MenuItem {
    roles?: string[];
    items?: Menu[];
    target?: string;
    idItem?: number;
}

export class Tag {

    constructor() {
        this.state = 'ACTIVO';
    }

    public id: number;
    public state: string;
    public name: string;
    public operation: string;
    public description: string;
    public periodTagAsignations: PeriodTagAsignation[];
    public indicatorTagAsignations: IndicatorTagAsignation[];
}


// Result Manager Page
export class ResultManagerIndicator {

    public indicator: Indicator;
    public anualTarget: number;
    public anualExecution: number;
    public resultManagerIndicatorQuarter?: ResultManagerIndicatorQuarter[];
    public hasExecutions: boolean;
}

export class ResultManagerIndicatorQuarter {
    public id?:number;
    public reportComment:string;
    public quarter: number;
    public quarterExecution: number;
    public resultManagerQuarterPopulationType?: ResultManagerQuarterPopulationType[];
    public resultManagerQuarterImplementer?: ResultManagerQuarterImplementer[];
}

export class ResultManagerQuarterPopulationType {
    public id?:number;
    public quarterPopulationTypeExecution: number;
    public populationType: StandardDissagregationOption
    public confirmation?: boolean;
    public reportValue?:number;    
}

export class ResultManagerQuarterImplementer {
    public indicatorExecution: IndicatorExecution;
    public quarterImplementerExecution: number;
}

export class QuarterPopulationTypeConfirmation{
    public id?:number;
    public indicator:Indicator;
    public quarterYearOrder:number;
    public populationType:StandardDissagregationOption;
    public confirmed:boolean;
    public reportValue?:number;
    public period:Period;
}

export class ResultManagerIndicatorQuarterReport{
    public id?:number;
    public indicator:Indicator;
    public quarterYearOrder:number;
    public reportComment:string;
    public period:Period;
}

//end Result manager page

export class PeriodTagAsignation {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: string;
    public state: string;
    public period: Period;
}

export class Audit{

    constructor() {
        this.state = 'ACTIVO';
    }

    public id:number;
    public entity:string;
    public recordId:number;
    public action:string;
    public responsibleUser:User;
    public changeDate:Date;
    public oldData:string;
    public newData:string;
    public state:string;


}



export class IndicatorTagAsignation {
    constructor() {
        this.state = 'ACTIVO';
    }

    public id: string;
    public state: string;
    public period: Period;
    public indicator: Indicator;
}

