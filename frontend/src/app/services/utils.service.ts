import {Injectable} from '@angular/core';
import * as Excel from 'exceljs/dist/exceljs.min.js';
// import * as Excel from 'exceljs';
// import * as fs from 'file-saver';
import * as FileSaver from 'file-saver';
import {FormGroup} from '@angular/forms';
import {
    ColumnTable,
    EnumsState,
    EnumsType,
    QuarterType,
    SelectItemWithOrder
} from '../shared/model/UtilsModel';
import {
    Canton,
    CustomDissagregationValues,
    EnumWeb,
    IndicatorExecution,
    IndicatorValue,
    IndicatorValueCustomDissagregationWeb,
    Period,
    Quarter,
    QuarterMonthResume,
    StandardDissagregationOption
} from '../shared/model/OsmosysModel';
import {HttpResponse} from '@angular/common/http';
import {TableColumnProperties} from 'exceljs';
import {SortEvent} from "primeng/api";
import {EnumsService} from "./enums.service";

@Injectable({
    providedIn: 'root'
})
export class UtilsService {

    constructor(
        public enumsService: EnumsService
    ) {
    }


    exportTableAsExcel(selectedColumns: ColumnTable[], items: any[], filename: string) {
        // const Excel = require('exceljs');
        const workbook = new Excel.Workbook();
        const itemsRenamed = this.renameKeys(items, selectedColumns);
        const worksheet = workbook.addWorksheet('Data');
        // add a table to a sheet
        const header = Object.keys(itemsRenamed[0]);
        // Add Data and Conditional Formatting
        const rowsData = [];
        itemsRenamed.forEach(d => {
            rowsData.push(Object.values(d));
        });
        const rowsColumns = header.map(value => {
            const colum: TableColumnProperties = {
                name: value,
                filterButton: true
            };
            return colum;
        });
        worksheet.addTable({
            name: 'MyTable',
            ref: 'A1',
            headerRow: true,
            totalsRow: false,
            style: {
                theme: 'TableStyleMedium2',
                showRowStripes: true,
            },
            columns: rowsColumns,
            rows: rowsData
        });

        this.autoWidth(worksheet, 15);
        let rowIndex = 1;
        for (rowIndex; rowIndex <= worksheet.rowCount; rowIndex++) {
            worksheet.getRow(rowIndex).alignment = {vertical: 'top', horizontal: 'left', wrapText: true};
        }
        // @ts-ignore
        workbook.xlsx.writeBuffer().then(excelData => {
            const blob = new Blob([excelData], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
            const EXCEL_EXTENSION = '.xlsx';
            FileSaver.saveAs(blob, filename + '_export_' + new Date().getTime() + EXCEL_EXTENSION);

        });

    }


    autoWidth(worksheet, minimalWidth = 10, maximalWidth = 50) {
        worksheet.columns.forEach((column) => {
            let maxColumnLength = 0;
            column.eachCell({includeEmpty: true}, (cell) => {
                maxColumnLength = Math.max(
                    maxColumnLength,
                    minimalWidth,
                    cell.value ? cell.value.toString().length : 0
                );
            });
            column.width = maxColumnLength > maximalWidth ? maximalWidth : maxColumnLength + 2;
        });
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
        } else if (data && !field) {
            return data;
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

    // todo implementar en todos los forms
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
                if (col.pipeRef) {
                    on[col.header] = col.pipeRef.transform(this.resolveFieldData(obj, col.field), col.arg1);
                } else {
                    on[col.header] = this.resolveFieldData(obj, col.field);
                }

            });
            result.push(on);
        });
        return result;
    }

    valueToBadgeStatusAlert(timeState: string): string {
        switch (timeState) {
            case 'ON_TIME':
                return 'green';
            case 'SOON_REPORT':
                return 'yellow';
            case 'LATE':
                return 'red';
            case 'INVALID':
                return 'orange';
            case 'NO_TIME':
                return 'white';
            default:
                return 'white';
        }
    }

    public getTimeStateButtonClass(timeState: string) {
        switch (timeState) {
            case 'ON_TIME':
                return 'p-button-success';
            case 'SOON_REPORT':
                return 'p-button-warning';
            case 'LATE':
                return 'p-button-danger';
            case 'INVALID':
                return 'p-button-warning';
            case 'NO_TIME':
                return '';
            default:
                return '';
        }
    }


    valueToBadgeValue(value: number, timeStatus: string): string {
        if (value || value === 0) {
            return value.toString();
        } else {
            switch (timeStatus) {
                case 'SOON_REPORT':
                    return 'Sin datos';
                case 'LATE':
                    return 'Sin datos-requiere actualización urgente';
                case 'NO_TIME':
                    return 'Sin datos';
                default:
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


    isLocationDissagregation(dissagregationType: string): boolean {

        let dissagregation= this.enumsService.resolveEnumWeb(EnumsType.DissagregationType, dissagregationType);
        return dissagregation.locationsDissagregation;
    }



    setDimentionsDissagregationsV2(
        monthValuesMap: Map<string, IndicatorValue[]>): Map<number, EnumWeb[]> {
        const results: Map<number, EnumWeb[]> = new Map<number, EnumWeb[]>();
        results.set(0, []);
        results.set(1, []);
        results.set(2, []);
        results.set(3, []);
        results.set(4, []);
        results.set(5, []);
        results.set(6, []);


        monthValuesMap
            .forEach((value, key) => {
                if (value && value.length > 0) {
                    let dissagregationWeb = this.enumsService.resolveEnumWeb(EnumsType.DissagregationType, key);
                    const numberOfDissagregations = dissagregationWeb.numberOfDissagregations;
                    if (numberOfDissagregations >= 0 && numberOfDissagregations <= 6) {
                        results.get(numberOfDissagregations)?.push(dissagregationWeb);
                    }
                }
            });


        return results;
    }

    getRowsAndColumnsFromValues(
        dissagregationColumnsType: EnumWeb,
        dissagregationRowsType: EnumWeb,
        dissagregationOptionsRows: StandardDissagregationOption[],
        dissagregationOptionsColumns: StandardDissagregationOption[],
        values: IndicatorValue[]) {
        let rows = new Array<Array<IndicatorValue>>();
        dissagregationOptionsRows.forEach(rowOption => {
            const rowOrdered = new Array<IndicatorValue>();
            const rowTotal = values.filter(value => {
                const valueOption = this.getIndicatorValueByDissagregationType(dissagregationRowsType, value);
                return valueOption.id === rowOption.id;
            });
            dissagregationOptionsColumns.forEach(colOptions => {

                const val = rowTotal.find(value => {
                    const optionCol = this.getIndicatorValueByDissagregationType(dissagregationColumnsType, value);
                    return optionCol.id === colOptions.id
                });
                rowOrdered.push(val);
            });

            rows.push(rowOrdered);
        });

        return rows;
    }

    getIndicatorValueByDissagregationType(dissagregationType: EnumWeb, value: IndicatorValue): StandardDissagregationOption {
        switch (dissagregationType.value) {
            case 'TIPO_POBLACION':
                return value.populationType;
            case 'EDAD':
                return value.ageType;
            case 'GENERO':
                return value.genderType;
            case 'LUGAR':
                return value.location;
            case 'PAIS_ORIGEN':
                return value.countryOfOrigin;
            case 'DIVERSIDAD':
                return value.diversityType;
            default:
                return null;
        }
    }



    getTotalIndicatorValuesArray(indicatorValues: IndicatorValue[] | IndicatorValueCustomDissagregationWeb[]) {
        return indicatorValues.map(value => value.value).reduce((previousValue, currentValue) => previousValue + currentValue, 0);
    }

    getTotalIndicatorValuesColumnArrayArray(indicatorValues: IndicatorValue[][], indexColumn: number) {
        // noinspection JSUnusedLocalSymbols
        return indicatorValues
            .map((value, index) => value[indexColumn])
            .map(value => value.value)
            .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
    }

    getTotalIndicatorValuesArrayArray(indicatorValues: Array<Array<IndicatorValue | IndicatorValueCustomDissagregationWeb>>) {
        return indicatorValues
            .reduce((previousValue, currentValue) => previousValue.concat(currentValue), [])
            .map(value => value.value)
            .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
    }

    validateMonth(monthValuesMap: Map<string, IndicatorValue[]>,
                  customDissagregationValues: CustomDissagregationValues[]): Map<string, number> {


        const monthValuesTotals: Map<string, number> = new Map<string, number>();
        monthValuesMap.forEach((value, dissagregationType) => {

            if (value && value.length > 0 && this.shouldvalidate(dissagregationType)) {
                const totalDissagregation = value.reduce((previousValue, currentValue) => previousValue + currentValue.value, 0);
                monthValuesTotals.set(dissagregationType, totalDissagregation);
            }
        });
        const totalMonth = Math.max(...monthValuesTotals.values());

        let errorExists = false;
        monthValuesTotals.forEach(value => {
            if (totalMonth !== value) {
                errorExists = true;
            }
        });
        if (customDissagregationValues !== null && customDissagregationValues.length > 0) {
            customDissagregationValues.forEach(value => {
                if (value.customDissagregation.controlTotalValue) {
                    const total =
                        value.indicatorValuesCustomDissagregation.reduce(
                            (previousValue, currentValue) => previousValue + currentValue.value,
                            0);

                    if (totalMonth !== total) {
                        errorExists = true;
                        monthValuesTotals.set(value.customDissagregation.name, total);
                    }
                }
            });
        }
        if (!errorExists) {
            // no error
            return null;
        } else {
            // error exits
            return monthValuesTotals;
        }
    }

    setZerosMonthValues(monthValuesMap: Map<string, IndicatorValue[]>) {
        // noinspection JSUnusedLocalSymbols
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


    setZerosCustomMonthValues(monthCustomDissagregatoinValues: CustomDissagregationValues[]) {
        if (monthCustomDissagregatoinValues !== null && monthCustomDissagregatoinValues.length
            > 0) {
            monthCustomDissagregatoinValues.forEach(value => {
                value.indicatorValuesCustomDissagregation.forEach(value1 => {
                    if (value1.state === EnumsState.ACTIVE && value1.value === null) {
                        value1.value = 0;
                    }
                });
            });
        }
    }

    shouldvalidate(dissagregationType: string): boolean {
        const dissagregationTypeE = this.enumsService.resolveEnumWeb(EnumsType.DissagregationType, dissagregationType);
        return !(dissagregationTypeE.standardDissagregationTypes.lastIndexOf('DIVERSIDAD') >= 1);
    }

    getTargetNeedUpdate(indicatorExecution: IndicatorExecution) {
        let result = false;
        if (indicatorExecution.state === EnumsState.INACTIVE) {
            return false;
        }
        // cambio a anual target
        /*        if (indicatorExecution.indicatorType !== EnumsIndicatorType.GENERAL) {
                    if (indicatorExecution.quarters && indicatorExecution.quarters.length > 0) {
                        indicatorExecution.quarters.filter(value => {
                            return value.state === EnumsState.ACTIVE;
                        }).forEach(value => {
                            if (value.target === null) {
                                result = true;
                            }
                        });
                    }
                } else {
                    if (indicatorExecution.target === null) {
                        result = true;
                    }
                }*/
        if (indicatorExecution.target === null) {
            result = true;
        }
        return result;
    }

    // noinspection JSUnusedGlobalSymbols
    getCurrentQuarter(): QuarterType {
        const today = new Date();
        const mm = today.getMonth() + 1;
        if (mm > 0 && mm < 4) {
            return QuarterType.I;
        } else if (mm > 3 && mm < 7) {
            return QuarterType.II;
        } else if (mm > 6 && mm < 10) {
            return QuarterType.III;
        } else if (mm > 9 && mm < 13) {
            return QuarterType.III;
        } else {
            return null;
        }
    }

    getCurrectPeriodOrDefault(periods: Period[]): Period {
        if (periods.length < 1) {
            return null;
        } else {
            const currentYear = (new Date()).getFullYear();
            if (periods.some(e => e.year === currentYear)) {
                return periods.find(period => period.year === currentYear);
            } else {
                const moreCurrentYear = Math.max(...periods.filter(value => value.year < currentYear).map(value1 => value1.year));
                return periods.find(value => value.year === moreCurrentYear);
            }
        }
    }

    public downloadFileResponse(response: HttpResponse<any>) {

        const filename: string = this.getFileName(response);
        const binaryData = [];
        binaryData.push(response.body);
        const downloadLink = document.createElement('a');
        downloadLink.href = window.URL.createObjectURL(new Blob(binaryData, {type: 'blob'}));
        downloadLink.setAttribute('download', filename);
        document.body.appendChild(downloadLink);
        downloadLink.click();
    }

    public getFileName(response: HttpResponse<Blob>) {
        let filename: string;
        try {
            const contentDisposition: string = response.headers.get('content-disposition');
            // noinspection RegExpUnnecessaryNonCapturingGroup
            const r = /(?:filename=")(.+)(?:")/;
            filename = r.exec(contentDisposition)[1];
        } catch (e) {
            filename = 'reporte.xlsx';
        }
        return filename;
    }

    public sortCantones(cantones: Canton[]): Canton[] {
        return cantones.sort((a, b) => {
            const x = a.provincia.description.localeCompare(b.provincia.description);
            if (x === 0) {
                return a.name.localeCompare(b.name);
            } else {
                return x;
            }
        });
    }


    generateQuarterMonthsResumes(quarters: Quarter[]): QuarterMonthResume[] {
        const quarterMonthResumes = [];
        let yearSpan: number = null;
        let quarterSpan: string = null;
        quarters.forEach(quarter => {
            quarter.months.forEach(month => {
                const qmr = new QuarterMonthResume();
                qmr.quarterId = quarter.id;
                qmr.quarterQuarter = quarter.quarter;
                qmr.quarterOrder = quarter.order;
                qmr.quarterYear = quarter.year;
                qmr.quarterTarget = quarter.target;
                qmr.quarterTotalExecution = quarter.totalExecution;
                qmr.quarterExecutionPercentage = quarter.executionPercentage;
                qmr.quarterMonthCount = quarter.months.length;
                qmr.monthId = month.id;
                qmr.monthMonth = month.month;
                qmr.monthOrder = month.order;
                qmr.monthYear = month.year;
                qmr.monthTotalExecution = month.totalExecution;
                qmr.monthLate = month.late;
                qmr.blockUpdate = month.blockUpdate;
                // year rowspan
                if (!yearSpan) {
                    yearSpan = quarter.year;
                    qmr.yearSpan = true;
                } else if (month.year !== yearSpan) {
                    qmr.yearSpan = true;
                    yearSpan = quarter.year;
                } else if (month.year === yearSpan) {
                    qmr.yearSpan = false;
                }
                qmr.yearSpanCount = quarters
                    .filter(value => value.year === yearSpan)
                    .reduce(
                        (a, b) => a.concat(b.months), []
                    ).length;
                // quarter rowspan
                if (!quarterSpan) {
                    quarterSpan = quarter.quarter;
                    qmr.quarterSpan = true;
                } else if (quarter.quarter !== quarterSpan) {
                    qmr.quarterSpan = true;
                    quarterSpan = quarter.quarter;
                } else if (quarter.quarter === quarterSpan) {
                    qmr.quarterSpan = false;
                }
                qmr.quarterSpanCount = quarter.months.length;

                quarterMonthResumes.push(qmr);
            });
        });
        return quarterMonthResumes;
    }

    customSort(event: SortEvent, cols: ColumnTable[]) {
        event.data.sort((data1, data2) => {
            const col = cols.filter(value => {
                return value.field === event.field;
            }).pop();
            let value1 = this.resolveFieldData(data1, col.field);
            let value2 = this.resolveFieldData(data2, col.field);
            // noinspection JSUnusedAssignment
            let result = null;

            if (col.pipeRef) {
                value1 = col.pipeRef.transform(value1, col.arg1, col.arg2, col.arg3);
                value2 = col.pipeRef.transform(value2, col.arg1, col.arg2, col.arg3);
            }

            if (value1 == null && value2 != null)
                result = -1;
            else if (value1 != null && value2 == null)
                result = 1;
            else if (value1 == null && value2 == null)
                result = 0;
            else if (typeof value1 === 'string' && typeof value2 === 'string')
                result = value1.localeCompare(value2);
            else
                result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;

            return (event.order * result);
        });
    }

    getOptionsFromValuesByDissagregationType(values: IndicatorValue[], dissagregationType: EnumWeb): StandardDissagregationOption[] {


        let options = values
            .map(value => this.getIndicatorValueByDissagregationType(dissagregationType, value));

        const uniquesOptions = [...new Map(options.map(item =>
            [item['id'], item])).values()];

        let result = Array.from(uniquesOptions);

        if (dissagregationType.value === 'LUGAR') {
            result.forEach(value => {

                    if (value.name && !value.name.includes('--')) {
                        value.name = (value as unknown as Canton).provincia.description + " -- " + (value as unknown as Canton).name;
                    }

                }
            );
            result.sort((a, b) => (a as unknown as Canton).code.localeCompare((b as unknown as Canton).code));
        } else {
            result.sort((a, b) => a.order - b.order);
        }
        return result;

    }


    standandarDissagregationOptionsToSelectItems(value1: StandardDissagregationOption[]): SelectItemWithOrder<StandardDissagregationOption>[] {
        let result: SelectItemWithOrder<StandardDissagregationOption>[] = [];
        value1.forEach(value => {
            result.push(this.standandarDissagregationOptionToSelectItem(value));
        });
        result.sort((a, b) => a.value.order - b.value.order);
        result.sort((a, b) => a.value.groupName.localeCompare(b.value.groupName));

        return result;
    }

    standandarDissagregationOptionToSelectItem(value1: StandardDissagregationOption): SelectItemWithOrder<StandardDissagregationOption> {
        let option = new SelectItemWithOrder<StandardDissagregationOption>();
        delete value1.type;
        option.value = value1;
        option.label = value1.name;
        option.order = value1.order;
        option.title = value1.groupName
        return option;
    }
}

