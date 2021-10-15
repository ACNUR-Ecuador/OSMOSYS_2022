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
}

