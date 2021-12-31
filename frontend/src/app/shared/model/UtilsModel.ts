import {PipeTransform} from '@angular/core';

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
    AreaType = 'AreaType',
    CountryOfOrigin = 'CountryOfOrigin',
    DissagregationType = 'DissagregationType',
    Frecuency = 'Frecuency',
    GenderType = 'GenderType',
    IndicatorType = 'IndicatorType',
    MarkerType = 'MarkerType',
    MeasureType = 'MeasureType',
    OfficeType = 'OfficeType',
    PopulationType = 'PopulationType',
    State = 'State',
    TotalIndicatorCalculationType = 'TotalIndicatorCalculationType',
    DiversityType = 'DiversityType',
}

export enum EnumsState {
    ACTIVE = 'ACTIVO',
    INACTIVE = 'INACTIVO'
}

export enum EnumsIndicatorType {
    CORE = 'Core',
    BUENAS_PRACTICAS = 'Buenas Prácticas',
    OPERACION = 'Operación',
    GENERAL = 'General',
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
    IMPACTO = 'Impacto',
    RESULTADOS = 'Resultados',
    PRODUCTO = 'Producto',
    APOYO = 'Apoyo',
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

export enum DissagregationType {
    TIPO_POBLACION = 'TIPO_POBLACION',
    EDAD = 'EDAD',
    GENERO = 'GENERO',
    LUGAR = 'LUGAR',
    PAIS_ORIGEN = 'PAIS_ORIGEN',
    DIVERSIDAD = 'DIVERSIDAD',
    SIN_DESSAGREGACION = 'SIN_DESSAGREGACION',
    TIPO_POBLACION_Y_GENERO = 'TIPO_POBLACION_Y_GENERO',
    TIPO_POBLACION_Y_EDAD = 'TIPO_POBLACION_Y_EDAD',
    TIPO_POBLACION_Y_DIVERSIDAD = 'TIPO_POBLACION_Y_DIVERSIDAD',
    TIPO_POBLACION_Y_PAIS_ORIGEN = 'TIPO_POBLACION_Y_PAIS_ORIGEN',
    TIPO_POBLACION_Y_LUGAR = 'TIPO_POBLACION_Y_LUGAR',
}


