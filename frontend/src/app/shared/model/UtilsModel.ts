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
}

export enum DissagregationType {
    TIPO_POBLACION = 'TIPO_POBLACION',
    EDAD = 'EDAD',
    GENERO = 'GENERO',
    LUGAR = 'LUGAR',
    PAIS_ORIGEN = 'PAIS_ORIGEN',
    DIVERSIDAD = 'DIVERSIDAD',
    SIN_DESAGREGACION = 'SIN_DESAGREGACION',
    TIPO_POBLACION_Y_GENERO = 'TIPO_POBLACION_Y_GENERO',
    TIPO_POBLACION_Y_EDAD = 'TIPO_POBLACION_Y_EDAD',
    TIPO_POBLACION_Y_DIVERSIDAD = 'TIPO_POBLACION_Y_DIVERSIDAD',
    TIPO_POBLACION_Y_PAIS_ORIGEN = 'TIPO_POBLACION_Y_PAIS_ORIGEN',
    TIPO_POBLACION_Y_LUGAR = 'TIPO_POBLACION_Y_LUGAR',
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

export class SelectItemWithOrder<T> implements SelectItem<T> {
    disabled: boolean;
    icon: string;
    label: string;
    styleClass: string;
    title: string;
    value: T;
    order: number;
}



