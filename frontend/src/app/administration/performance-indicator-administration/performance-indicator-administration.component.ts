import {Component, Input, OnInit} from '@angular/core';
import {Indicator} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {IndicatorService} from '../../shared/services/indicator.service';

@Component({
    selector: 'app-performance-indicator-administration',
    templateUrl: './performance-indicator-administration.component.html',
    styleUrls: ['./performance-indicator-administration.component.scss']
})
export class PerformanceIndicatorAdministrationComponent implements OnInit {
    items: Indicator[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    private states: SelectItem[];
    private indicatorTypes: SelectItem[];
    private measureTypes: SelectItem[];
    private frecuencies: SelectItem[];
    private areaTypes: SelectItem[];
    private totalIndicatorCalculationType: SelectItem[];
    private isMonitoredOptions: any[];
    private isCalculatedOptions: any[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private indicatorService: IndicatorService
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'code', header: 'Código', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción', type: ColumnDataType.text},
            {field: 'guidePartners', header: 'Guía para socios', type: ColumnDataType.text},
            {field: 'guideDirectImplementation', header: 'Guía para Implementación Directa', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
            {field: 'indicatorType', header: 'Tipo', type: ColumnDataType.text},
            {field: 'measureType', header: 'Medida', type: ColumnDataType.text},
            {field: 'frecuency', header: 'Frecuencia', type: ColumnDataType.text},
            {field: 'areaType', header: 'Area', type: ColumnDataType.text},
            {field: 'isMonitored', header: 'Id', type: ColumnDataType.boolean},
            {field: 'isCalculated', header: 'Código', type: ColumnDataType.numeric},
            {field: 'totalIndicatorCalculationType', header: 'Descripción Corta', type: ColumnDataType.text},
            {field: 'markers', header: 'Descripción', type: ColumnDataType.text},
            {field: 'statements', header: 'Estado', type: ColumnDataType.text},
            {field: 'dissagregationsAssignationToIndicator', header: 'Id', type: ColumnDataType.numeric},
            {field: 'customDissagregationAssignationToIndicators', header: 'Código', type: ColumnDataType.numeric}
        ];

        const hiddenColumns: string[] = ['id', 'guidePartners', 'guideDirectImplementation'];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');

        this.formItem = this.fb.group({
            id: new FormControl(''),
            code: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
            guidePartners: new FormControl(''),
            guideDirectImplementation: new FormControl('', Validators.required),
            state: new FormControl(''),
            indicatorType: new FormControl(''),
            measureType: new FormControl(''),
            frecuency: new FormControl(''),
            areaType: new FormControl(''),
            isMonitored: new FormControl(''),
            isCalculated: new FormControl(''),
            totalIndicatorCalculationType: new FormControl(''),
            markers: new FormControl(''),
            statements: new FormControl(''),
            dissagregationsAssignationToIndicator: new FormControl(''),
            customDissagregationAssignationToIndicators: new FormControl('')
        });

        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
        this.enumsService.getByType(EnumsType.IndicatorType).subscribe(value => {
            this.indicatorTypes = value;
        });
        this.enumsService.getByType(EnumsType.MeasureType).subscribe(value => {
            this.measureTypes = value;
        });
        this.enumsService.getByType(EnumsType.Frecuency).subscribe(value => {
            this.frecuencies = value;
        });
        this.enumsService.getByType(EnumsType.AreaType).subscribe(value => {
            this.areaTypes = value;
        });
        this.enumsService.getByType(EnumsType.TotalIndicatorCalculationType).subscribe(value => {
            this.totalIndicatorCalculationType = value;
        });
        this.isCalculatedOptions = [{label: 'Calculado', value: true}, {label: 'No Calculado', value: false}];
        this.isMonitoredOptions = [{label: 'Monitoreado', value: true}, {label: 'No Monitoreado', value: false}];

    }

    private loadItems() {
        this.indicatorService.getAll().subscribe(value => {
            this.items = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las situaciones',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    exportExcel() {
        import('xlsx').then(xlsx => {
            const itemsRenamed = this.utilsService.renameKeys(this.items, this.cols);
            const worksheet = xlsx.utils.json_to_sheet(itemsRenamed);
            const workbook = {Sheets: {data: worksheet}, SheetNames: ['data']};
            const excelBuffer: any = xlsx.write(workbook, {bookType: 'xlsx', type: 'array'});
            this.utilsService.saveAsExcelFile(excelBuffer, 'situaciones');
        });
    }


    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Indicator();
        this.formItem.patchValue(newItem);
        console.log(this.formItem.value);
    }

    editItem(indicator: Indicator) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(indicator);
    }


    saveItem() {
        /*this.messageService.clear();
        const {
            id,
            code,
            shortDescription,
            description,
            state
        }
            = this.formItem.value;
        const situacion: Indicator = {
            id,
            code,
            description,
            state
        };
        if (situacion.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.indicatorService.update(situacion).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al actualizar la situación',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.indicatorService.save(situacion).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al guardar la situación',
                    detail: error.error.message,
                    life: 3000
                });
            });
        }*/

    }

    cancelDialog() {
        this.showDialog = false;
        this.submitted = false;
    }

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }
}
