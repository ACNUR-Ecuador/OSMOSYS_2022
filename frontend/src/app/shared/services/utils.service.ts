import {Injectable} from '@angular/core';
import * as FileSaver from 'file-saver';
import {FormGroup} from '@angular/forms';
import {ColumnTable, DissagregationType, EnumsState, EnumsType, MonthType, SelectItemWithOrder} from '../model/UtilsModel';
import {EnumsService} from './enums.service';
import {IndicatorValue, MonthValues, Quarter} from '../model/OsmosysModel';
import {SelectItem} from 'primeng/api';

@Injectable({
    providedIn: 'root'
})
export class UtilsService {
    static daysToReport = 30;

    constructor(
        private enumsService: EnumsService
    ) {
    }

    saveAsExcelFile(buffer: any, fileName: string): void {
        const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
        const EXCEL_EXTENSION = '.xlsx';
        const data: Blob = new Blob([buffer], {
            type: EXCEL_TYPE
        });
        FileSaver.saveAs(data, fileName + '_export_' + new Date().getTime() + EXCEL_EXTENSION);
    }


    /**
     * utilidad para tablas para uso de pipes
     * @param data recupera el datp
     * @param field nombre del campo
     */
    public resolveFieldData(data: any, field: string): any {
        if (data && field) {
            if (field.indexOf('.') === -1) {
                return data[field];
            } else {
                const fields: string[] = field.split('.');
                let value = data;
                for (let i = 0, len = fields.length; i < len; ++i) {
                    if (value == null) {
                        return null;
                    }
                    value = value[fields[i]];
                }
                return value;
            }
        } else {
            return null;
        }
    }

    /**
     * Resetera el formulario
     * @param form formularios a resetar
     */
    public resetForm(form: FormGroup): void {
        form.reset();
        form.markAsPristine();
        form.markAsUntouched();
    }

    showErrorForm(formControlName: string, formGroup: FormGroup): boolean {
        return formGroup.get(formControlName).invalid && formGroup.get(formControlName).dirty;
    }

    getErrorMessageForm(formControlName: string, formGroup: FormGroup): string {
        if (this.showErrorForm(formControlName, formGroup)) {
            if (formGroup.get(formControlName).hasError('required')) {
                return 'Dato obligatorio';
            }
            if (formGroup.get(formControlName).hasError('email')) {
                return 'Correo no válido';
            }
            if (formGroup.get(formControlName).hasError('maxlength')) {
                return `No debe tener más de ${formGroup.get(formControlName).getError('maxlength').requiredLength} caracteres (${formGroup.get(formControlName).getError('maxlength').actualLength} caracteres)`;
            }

            return 'Dato no válido';
        }
        return null;
    }

    /**
     * cambia keys del objeto a nombre del header para exportar
     * @param objects lista e objetos
     * @param cols columna de tipos columnas de tabla
     */
    renameKeys(objects: any[], cols: ColumnTable[]) {
        const result = [];
        objects.forEach(obj => {
            const on = {};
            cols.forEach(col => {
                on[col.header] = obj[col.field];
            });
            result.push(on);
        });
        return result;
    }

    valueToBadgeStatusAlert(value: number, month: MonthType, year: number): string {
        const now: Date = new Date();
        now.setHours(0, 0, 0, 0);
        const monthNow = now.getMonth();
        const yearNow = now.getFullYear();
        const monthValue = this.enumsService.monthTypeToNumber(month);

        const dateValueFirstDay = this.addMonths(new Date(year, monthValue - 1, 1), 1);
        const dateValueDayExpiration = this.addMonths(new Date(year, monthValue - 1, UtilsService.daysToReport), 1);
        let result: string;
        if (value) {
            // fecha ya expiró es correcto
            if (dateValueDayExpiration.getTime() < now.getTime()) {
                result = 'correct';
            } else {
                result = 'white';
            }
        } else {
            // si ya esat pasado el mes
            if (now.getTime() > dateValueFirstDay.getTime() && now.getTime() <= dateValueDayExpiration.getTime()) {
                result = 'alert';
            } else if (now.getTime() > dateValueDayExpiration.getTime()) {
                result = 'error';
            } else {
                result = 'white';
            }
        }
        return result;
    }

    valueToBadgeValue(value: number, month: MonthType, year: number): string {
        const now: Date = new Date();
        const monthNow = now.getMonth();
        const yearNow = now.getFullYear();
        const monthValue = this.enumsService.monthTypeToNumber(month);

        const dateValueFirstDay = this.addMonths(new Date(year, monthValue - 1, 1), 1);
        const dateValueDayExpiration = this.addMonths(new Date(year, monthValue - 1, UtilsService.daysToReport), 1);

        if (value) {
            return value.toString();
        } else {
            // si ya esat pasado el mes
            if (now.getTime() > dateValueFirstDay.getTime() && now.getTime() <= dateValueDayExpiration.getTime()) {
                return 'Sin datos- requiere actualización';
            } else if (now.getTime() > dateValueDayExpiration.getTime()) {
                return 'Sin datos- requiere actualización urgente';
            } else {
                return 'Sin datos';
            }
        }
    }

    addMonths(date, months) {
        const d = date.getDate();
        date.setMonth(date.getMonth() + +months);
        if (date.getDate() !== d) {
            date.setDate(0);
        }
        return date;
    }

    getQuarterLastMonth(quarter: Quarter) {

    }

    getEnymTypesByDissagregationTypes(dissagregationType: DissagregationType): EnumsType[] {
        const dissagregationTypeE = DissagregationType[dissagregationType];
        const result: EnumsType[] = [];
        switch (dissagregationTypeE) {
            case DissagregationType.TIPO_POBLACION: {
                result.push(EnumsType.PopulationType);
                return result;
            }
            case DissagregationType.EDAD: {
                result.push(EnumsType.AgeType);
                return result;
            }
            case DissagregationType.GENERO: {
                result.push(EnumsType.GenderType);
                return result;
            }
            case DissagregationType.LUGAR: {
                return result;
            }
            case DissagregationType.PAIS_ORIGEN: {
                result.push(EnumsType.CountryOfOrigin);
                return result;
            }
            case DissagregationType.DIVERSIDAD: {
                result.push(EnumsType.DiversityType);
                return result;
            }
            case DissagregationType.SIN_DESAGREGACION: {
                return result;
            }
            case DissagregationType.TIPO_POBLACION_Y_GENERO: {
                result.push(EnumsType.GenderType);
                result.push(EnumsType.PopulationType);
                return result;
            }
            case DissagregationType.TIPO_POBLACION_Y_EDAD: {
                result.push(EnumsType.AgeType);
                result.push(EnumsType.PopulationType);
                return result;
            }
            case DissagregationType.TIPO_POBLACION_Y_DIVERSIDAD: {
                result.push(EnumsType.DiversityType);
                result.push(EnumsType.PopulationType);
                return result;
            }
            case DissagregationType.TIPO_POBLACION_Y_PAIS_ORIGEN: {
                result.push(EnumsType.CountryOfOrigin);
                result.push(EnumsType.PopulationType);
                return result;
            }
            case DissagregationType.TIPO_POBLACION_Y_LUGAR: {
                result.push(null);
                result.push(EnumsType.PopulationType);
                return result;
            }
        }
    }

    getDimentionsByDissagregationTypes(dissagregationType: DissagregationType): number {
        const dissagregationTypeE = DissagregationType[dissagregationType];
        const result: EnumsType[] = [];
        switch (dissagregationTypeE) {
            case DissagregationType.TIPO_POBLACION:
            case DissagregationType.EDAD:
            case DissagregationType.GENERO:
            case DissagregationType.LUGAR:
            case DissagregationType.PAIS_ORIGEN:
            case DissagregationType.DIVERSIDAD:
                return 1;
            case DissagregationType.SIN_DESAGREGACION:
                return 0;
            case DissagregationType.TIPO_POBLACION_Y_GENERO:
            case DissagregationType.TIPO_POBLACION_Y_EDAD:
            case DissagregationType.TIPO_POBLACION_Y_DIVERSIDAD:
            case DissagregationType.TIPO_POBLACION_Y_PAIS_ORIGEN:
            case DissagregationType.TIPO_POBLACION_Y_LUGAR:
                return 2;
        }
    }

    getNoDimentionsDissagregationTypes(): DissagregationType[] {
        const result: DissagregationType[] = [];
        result.push(DissagregationType.SIN_DESAGREGACION);
        return result;
    }

    getOneDimentionsDissagregationTypes(): DissagregationType[] {
        const result: DissagregationType[] = [];
        result.push(DissagregationType.TIPO_POBLACION);
        result.push(DissagregationType.EDAD);
        result.push(DissagregationType.GENERO);
        result.push(DissagregationType.LUGAR);
        result.push(DissagregationType.PAIS_ORIGEN);
        result.push(DissagregationType.DIVERSIDAD);
        return result;
    }

    getTwoDimentionsDissagregationTypes(): DissagregationType[] {
        const result: DissagregationType[] = [];
        result.push(DissagregationType.TIPO_POBLACION_Y_EDAD);
        result.push(DissagregationType.TIPO_POBLACION_Y_GENERO);
        result.push(DissagregationType.TIPO_POBLACION_Y_LUGAR);
        result.push(DissagregationType.TIPO_POBLACION_Y_PAIS_ORIGEN);
        result.push(DissagregationType.TIPO_POBLACION_Y_DIVERSIDAD);
        return result;
    }

    getDissagregationTypesByDissagregationType(dissagregationType: DissagregationType): DissagregationType[] {
        const dissagregationTypeE = DissagregationType[dissagregationType];
        const result: DissagregationType[] = [];
        switch (dissagregationTypeE) {
            case DissagregationType.TIPO_POBLACION:
            case DissagregationType.EDAD:
            case DissagregationType.GENERO:
            case DissagregationType.LUGAR:
            case DissagregationType.PAIS_ORIGEN:
            case DissagregationType.DIVERSIDAD:
            case DissagregationType.SIN_DESAGREGACION: {
                result.push(dissagregationTypeE);
                return result;
            }
            case DissagregationType.TIPO_POBLACION_Y_GENERO: {
                result.push(DissagregationType.GENERO);
                result.push(DissagregationType.TIPO_POBLACION);
                return result;
            }
            case DissagregationType.TIPO_POBLACION_Y_EDAD: {
                result.push(DissagregationType.EDAD);
                result.push(DissagregationType.TIPO_POBLACION);
                return result;
            }
            case DissagregationType.TIPO_POBLACION_Y_DIVERSIDAD: {
                result.push(DissagregationType.DIVERSIDAD);
                result.push(DissagregationType.TIPO_POBLACION);
                return result;
            }
            case DissagregationType.TIPO_POBLACION_Y_PAIS_ORIGEN: {
                result.push(DissagregationType.PAIS_ORIGEN);
                result.push(DissagregationType.TIPO_POBLACION);
                return result;
            }
            case DissagregationType.TIPO_POBLACION_Y_LUGAR: {
                result.push(DissagregationType.LUGAR);
                result.push(DissagregationType.TIPO_POBLACION);
                return result;
            }
        }
    }


    getOptionValueByDissagregationType(dissagregationType: DissagregationType, value: IndicatorValue) {
        const dissagregationTypeE = DissagregationType[dissagregationType];
        switch (dissagregationTypeE) {
            case DissagregationType.TIPO_POBLACION:
                return value.populationType;
            case DissagregationType.EDAD:
                return value.ageType;
            case DissagregationType.GENERO:
                return value.genderType;
            case DissagregationType.LUGAR:
                return value.location;
            case DissagregationType.PAIS_ORIGEN:
                return value.countryOfOrigin;
            case DissagregationType.DIVERSIDAD:
                return value.diversityType;
            default:
                return null;
        }
    }

    getOrderByDissagregationOption(dissagregationOption: string, dissagregationOptionsRows: SelectItemWithOrder<any>[]): number {
        const dissagregationOptionFound = dissagregationOptionsRows
            .filter(value => {
                return value.value === dissagregationOption;
            })[0];
        return dissagregationOptionFound.order;

    }

    getTotalIndicatorValuesArray(indicatorValues: IndicatorValue[]) {
        return indicatorValues.map(value => value.value).reduce((previousValue, currentValue) => previousValue + currentValue, 0);
    }

    getTotalIndicatorValuesColumnArrayArray(indicatorValues: IndicatorValue[][], indexColumn: number) {
        return indicatorValues
            .map((value, index) => value[indexColumn])
            .map(value => value.value)
            .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
    }

    getTotalIndicatorValuesArrayArray(indicatorValues: IndicatorValue[][]) {
        return indicatorValues.reduce((previousValue, currentValue) => previousValue.concat(currentValue), [])
            .map(value => value.value)
            .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
    }

    validateMonth(monthValuesMap: Map<string, IndicatorValue[]>): Map<string, number> {
        const monthValuesTotals: Map<string, number> = new Map<string, number>();
        monthValuesMap.forEach((value, key) => {
            const dissagregationTypeE = DissagregationType[key];
            if (value && value.length > 0 && this.shouldvalidate(dissagregationTypeE)) {
                const totalDissagregation = value.reduce((previousValue, currentValue) => previousValue + currentValue.value, 0);
                monthValuesTotals.set(dissagregationTypeE, totalDissagregation);
            }
        });
        const totalMonth = Math.max(...monthValuesTotals.values());
        console.log('totalMonth: ' + totalMonth);
        let errorExists = false;
        monthValuesTotals.forEach(value => {
            if (totalMonth !== value) {
                errorExists = true;
            }
        });
        if (!errorExists) {
            // no error
            return null;
        } else {
            // error exits
            return monthValuesTotals;
        }
    }

    setZerosMonthValues(monthValuesMap: Map<string, IndicatorValue[]>) {
        monthValuesMap.forEach((value, key) => {
            if (value && value.length > 0) {
                value.forEach(value1 => {
                    if (value1.state === EnumsState.ACTIVE && value1.value === null) {
                        value1.value = 0;
                    }
                });
            }
        });
    }

    shouldvalidate(dissagregationType: DissagregationType): boolean {
        const dissagregationTypeE = DissagregationType[dissagregationType];
        switch (dissagregationTypeE) {
            case DissagregationType.TIPO_POBLACION:
            case DissagregationType.EDAD:
            case DissagregationType.GENERO:
            case DissagregationType.LUGAR:
            case DissagregationType.PAIS_ORIGEN:
            case DissagregationType.TIPO_POBLACION_Y_GENERO:
            case DissagregationType.TIPO_POBLACION_Y_EDAD:
            case DissagregationType.TIPO_POBLACION_Y_PAIS_ORIGEN:
            case DissagregationType.TIPO_POBLACION_Y_LUGAR:
            case DissagregationType.SIN_DESAGREGACION:
                return true;
            case DissagregationType.TIPO_POBLACION_Y_DIVERSIDAD:
            case DissagregationType.DIVERSIDAD:
                return false;
        }
    }
}


