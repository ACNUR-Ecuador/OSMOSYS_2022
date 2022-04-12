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

export enum EnumsType {
    AgeType = 'AgeType',
    AgePrimaryEducationType = 'AgePrimaryEducationType',
    AgeTertiaryEducationType = 'AgeTertiaryEducationType',
    CountryOfOrigin = 'CountryOfOrigin',
    GenderType = 'GenderType',
    DiversityType = 'DiversityType',
    PopulationType = 'PopulationType',
    AreaType = 'AreaType',
    DissagregationType = 'DissagregationType',
    Frecuency = 'Frecuency',
    IndicatorType = 'IndicatorType',
    MarkerType = 'MarkerType',
    MeasureType = 'MeasureType',
    OfficeType = 'OfficeType',
    State = 'State',
    TotalIndicatorCalculationType = 'TotalIndicatorCalculationType',
    RoleType = 'RoleType',
    SourceType = 'SourceType',
    UnitType = 'UnitType',
}

export enum DissagregationType {
    TIPO_POBLACION = 'TIPO_POBLACION',
    EDAD = 'EDAD',
    EDAD_EDUCACION_PRIMARIA = 'EDAD_EDUCACION_PRIMARIA',
    EDAD_EDUCACION_TERCIARIA = 'EDAD_EDUCACION_TERCIARIA',
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
    TIPO_POBLACION_LUGAR_EDAD_Y_GENERO = 'TIPO_POBLACION_LUGAR_EDAD_Y_GENERO',
    TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO = 'TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO',
    TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO = 'TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO',
}

export enum EnumsState {
    ACTIVE = 'ACTIVO',
    INACTIVE = 'INACTIVO'
}

export enum EnumsIndicatorType {
    CORE = 'CORE',
    BUENAS_PRACTICAS = 'BUENAS_PRACTICAS',
    OPERACION = 'OPERACION',
    GENERAL = 'GENERAL',
}

export enum MeasureType {
    NUMERO = 'Número enteros',
    PROPORCION = 'Proporción',
    TEXTO = 'Texto',
}

export enum Frecuency {
    MENSUAL = 'Mensual',
    TRIMESTRAL = 'Trimestral',
    SEMESTRAL = 'Semestral',
    ANUAL = 'Anual',
}

export enum AreaType {
    IMPACTO = 'IMPACTO',
    RESULTADOS = 'RESULTADOS',
    PRODUCTO = 'PRODUCTO',
    APOYO = 'APOYO',
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



