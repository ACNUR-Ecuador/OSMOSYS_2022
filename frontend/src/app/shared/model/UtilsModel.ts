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
    AgeType = 'AgeType',
    CountryOfOrigin = 'CountryOfOrigin',
    GenderType = 'GenderType',
    DiversityType = 'DiversityType',
    PopulationType = 'PopulationType',
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
    TimeStateEnum = 'TimeStateEnum'
}

// todo 2024 eliminar
export enum DissagregationType {

    TIPO_POBLACION = 'TIPO_POBLACION',
    EDAD = 'EDAD',
    GENERO = 'GENERO',
    LUGAR = 'LUGAR',
    PAIS_ORIGEN = 'PAIS_ORIGEN',
    DIVERSIDAD = 'DIVERSIDAD',
    SIN_DESAGREGACION = 'SIN_DESAGREGACION',
    GENERO_Y_EDAD = 'GENERO_Y_EDAD',
    GENERO_Y_DIVERSIDAD = 'GENERO_Y_DIVERSIDAD',
    TIPO_POBLACION_Y_GENERO = 'TIPO_POBLACION_Y_GENERO',
    TIPO_POBLACION_Y_EDAD = 'TIPO_POBLACION_Y_EDAD',
    TIPO_POBLACION_Y_DIVERSIDAD = 'TIPO_POBLACION_Y_DIVERSIDAD',
    TIPO_POBLACION_Y_PAIS_ORIGEN = 'TIPO_POBLACION_Y_PAIS_ORIGEN',
    TIPO_POBLACION_Y_LUGAR = 'TIPO_POBLACION_Y_LUGAR',
    LUGAR_EDAD_Y_GENERO = 'LUGAR_EDAD_Y_GENERO',
    DIVERSIDAD_EDAD_Y_GENERO = 'DIVERSIDAD_EDAD_Y_GENERO',
   TIPO_POBLACION_LUGAR_EDAD_Y_GENERO = 'TIPO_POBLACION_LUGAR_EDAD_Y_GENERO',
    LUGAR_Y_DIVERSIDAD = 'LUGAR_Y_DIVERSIDAD',
    LUGAR_Y_GENERO = 'LUGAR_Y_GENERO',
    LUGAR_PAIS_ORIGEN_EDAD_Y_GENERO = 'LUGAR_PAIS_ORIGEN_EDAD_Y_GENERO',
    PAIS_ORIGEN_EDAD_Y_GENERO = 'PAIS_ORIGEN_EDAD_Y_GENERO',
    }


export enum MonthType {
    ENERO = 'Enero',
    FEBRERO = 'Febrero',
    MARZO = 'Marzo',
    ABRIL = 'Abril',
    MAYO = 'Mayo',
    JUNIO = 'Junio',
    JULIO = 'Julio',
    AGOSTO = 'Agosto',
    SEPTIEMBRE = 'Septiembre',
    OCTUBRE = 'Octubre',
    NOVIEMBRE = 'Noviembre',
    DICIEMBRE = 'Diciembre',
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
