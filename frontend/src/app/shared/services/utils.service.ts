import {Injectable} from '@angular/core';
import * as FileSaver from 'file-saver';
import {FormGroup} from '@angular/forms';
import {ColumnTable} from '../model/UtilsModel';

@Injectable({
    providedIn: 'root'
})
export class UtilsService {

    constructor() {
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
}
