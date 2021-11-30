import {PipeTransform} from '@angular/core';

export class ColumnTable {
    field: string;
    type: ColumnDataType;
    header: string;
    styleClass?: string;
    pipeRef?: PipeTransform;
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
    CountyOfOrigin = 'CountyOfOrigin',
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
