import {PipeTransform} from '@angular/core';
import {SelectItem} from 'primeng/api';

export class ColumnTable {
    field: string;
    type: ColumnDataType;
    header: string;
    styleClass?: string;
    pipeRef?: PipeTransform;
    arg1?: any;
    arg2?: any;
    arg3?: any;
    width?: string;
}

export enum ColumnDataType {
    text = 'text',
    numeric = 'numeric',
    boolean = 'boolean',
    date = 'date'
}

export enum EnumsState {
    ACTIVE = 'ACTIVO',
    INACTIVE = 'INACTIVO'
}

export enum EnumsType {
    /*AgeType = 'AgeType',
    CountryOfOrigin = 'CountryOfOrigin',
    GenderType = 'GenderType',
    DiversityType = 'DiversityType',
    PopulationType = 'PopulationType',*/
    AreaType = 'AreaType',
    DissagregationType = 'DissagregationType',
    Frecuency = 'Frecuency',
    IndicatorType = 'IndicatorType',
    MeasureType = 'MeasureType',
    OfficeType = 'OfficeType',
    State = 'State',
    TotalIndicatorCalculationType = 'TotalIndicatorCalculationType',
    RoleType = 'RoleType',
    SourceType = 'SourceType',
    UnitType = 'UnitType',
    TimeStateEnum = 'TimeStateEnum',
    AuditAction='AuditAction'
}



export enum QuarterType {
    I = 'I',
    II = 'II',
    III = 'III',
    IV = 'IV'
}

export class SelectItemWithOrder<T> implements SelectItem<T> {
    disabled: boolean;
    icon: string;
    label: string;
    styleClass: string;
    title: string;
    value: T;
    order: number;
}

export enum EnumsIndicatorType {
    CORE = 'CORE',
    BUENAS_PRACTICAS = 'BUENAS_PRACTICAS',
    OPERACION = 'OPERACION',
    GENERAL = 'GENERAL',
}
