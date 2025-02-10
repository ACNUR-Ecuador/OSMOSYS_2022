import { HttpResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, Input, OnInit, Pipe, PipeTransform } from '@angular/core';
import {
    FormBuilder,
    FormControl,
    FormGroup,
    Validators,
} from '@angular/forms';
import {
    ConfirmationService,
    FilterService,
    MessageService,
    SelectItem,
} from 'primeng/api';
import { Table } from 'primeng/table';
import { EnumsService } from 'src/app/services/enums.service';
import { FilterUtilsService } from 'src/app/services/filter-utils.service';
import { IndicatorService } from 'src/app/services/indicator.service';
import { PeriodService } from 'src/app/services/period.service';
import { ReportsService } from 'src/app/services/reports.service';
import { PercentagePipe } from 'src/app/shared/pipes/percentage.pipe';
import { UtilsService } from 'src/app/services/utils.service';
import {
    Indicator,
    Month,
    Office,
    Period,
    Project,
    Quarter,
    ResultManagerIndicator,
    Statement,
} from 'src/app/shared/model/OsmosysModel';
import { User } from 'src/app/shared/model/User';
import {
    ColumnDataType,
    ColumnTable,
    EnumsState,
    EnumsType,
} from 'src/app/shared/model/UtilsModel';
import { ResultManagerService } from 'src/app/services/result-manager.service';
import { UserService } from 'src/app/services/user.service';

@Component({
    selector: 'app-result-manager-indicator-list',
    templateUrl: './result-manager-indicator-list.component.html',
    styleUrls: ['./result-manager-indicator-list.component.scss'],
})
export class ResultManagerIndicatorListComponent implements OnInit {
    items: ResultManagerIndicator[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    states: SelectItem[];
    _selectedColumns: ColumnTable[];
    periodsItems: SelectItem<Period>[];
    inputNameValue: string = '';
    indicators: Indicator[];
    selectedIndicators: Indicator[];
    operations: any[];
    resultManagerIndicators: ResultManagerIndicator[];

    exampleData1: ResultManagerIndicator = {
        indicator: {
            id: 1,
            code: '',
            regionalCode: '',
            description: '# de personas que desean acceder a beneficios de protección social',
            category: '',
            qualitativeInstructions: '',
            state: '',
            indicatorType: '',
            measureType: '',
            frecuency: '',
            areaType: '',
            isMonitored: false,
            isCalculated: false,
            totalIndicatorCalculationType: '',
            compassIndicator: false,
            coreIndicator: false,
            statement: new Statement,
            unit: '',
            blockAfterUpdate: false,
            dissagregationsAssignationToIndicator: [],
            customDissagregationAssignationToIndicators: [],
            resultManager: new User,
        },
        anualTarget: 1000,
        anualExecution: 750,
        resultManagerIndicatorQuarter: [
            {
                quarter: 1,
                quarterExecution: 200,
                resultManagerQuarterPopulationType: [
                    {
                        quarterPopulationTypeExecution: 120,
                        populationType: {
                            id: 1,
                            name: "Children",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: true,
                    },
                    {
                        quarterPopulationTypeExecution: 80,
                        populationType: {
                            id: 2,
                            name: "Adults",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: false,
                    }
                ],
                resultManagerQuarterImplementer: [
                    {
                        IndicatorExecution: {
                            id: 1,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 100,
                    },
                    {
                        IndicatorExecution: {
                            id: 2,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 100,
                    }
                ]
            },
            {
                quarter: 2,
                quarterExecution: 300,
                resultManagerQuarterPopulationType: [
                    {
                        quarterPopulationTypeExecution: 180,
                        populationType: {
                            id: 1,
                            name: "Children",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: true,
                    },
                    {
                        quarterPopulationTypeExecution: 120,
                        populationType: {
                            id: 2,
                            name: "Adults",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: true,
                    }
                ],
                resultManagerQuarterImplementer: [
                    {
                        IndicatorExecution: {
                            id: 3,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 150,
                    },
                    {
                        IndicatorExecution: {
                            id: 4,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 150,
                    }
                ]
            }
        ],
        hasExecutions: false
    };

    exampleData2: ResultManagerIndicator = {
        indicator: {
            id: 2,
            code: '',
            regionalCode: '',
            description: '# de personas que desean acceder a beneficios de protección social',
            category: '',
            qualitativeInstructions: '',
            state: '',
            indicatorType: '',
            measureType: '',
            frecuency: '',
            areaType: '',
            isMonitored: false,
            isCalculated: false,
            totalIndicatorCalculationType: '',
            compassIndicator: false,
            coreIndicator: false,
            statement: new Statement,
            unit: '',
            blockAfterUpdate: false,
            dissagregationsAssignationToIndicator: [],
            customDissagregationAssignationToIndicators: [],
            resultManager: new User,
        },
        anualTarget: 1000,
        anualExecution: 750,
        resultManagerIndicatorQuarter: [
            {
                quarter: 1,
                quarterExecution: 200,
                resultManagerQuarterPopulationType: [
                    {
                        quarterPopulationTypeExecution: 120,
                        populationType: {
                            id: 1,
                            name: "Children",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: true,
                    },
                    {
                        quarterPopulationTypeExecution: 80,
                        populationType: {
                            id: 2,
                            name: "Adults",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: false,
                    }
                ],
                resultManagerQuarterImplementer: [
                    {
                        IndicatorExecution: {
                            id: 1,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 100,
                    },
                    {
                        IndicatorExecution: {
                            id: 2,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 100,
                    }
                ]
            },
            {
                quarter: 2,
                quarterExecution: 300,
                resultManagerQuarterPopulationType: [
                    {
                        quarterPopulationTypeExecution: 180,
                        populationType: {
                            id: 1,
                            name: "Children",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: true,
                    },
                    {
                        quarterPopulationTypeExecution: 120,
                        populationType: {
                            id: 2,
                            name: "Adults",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: true,
                    }
                ],
                resultManagerQuarterImplementer: [
                    {
                        IndicatorExecution: {
                            id: 3,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 150,
                    },
                    {
                        IndicatorExecution: {
                            id: 4,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 150,
                    }
                ]
            }
        ],
        hasExecutions: false
    };

    exampleData3: ResultManagerIndicator = {
        indicator: {
            id: 3,
            code: '',
            regionalCode: '',
            description: '# de personas que desean acceder a beneficios de protección social',
            category: '',
            qualitativeInstructions: '',
            state: '',
            indicatorType: '',
            measureType: '',
            frecuency: '',
            areaType: '',
            isMonitored: false,
            isCalculated: false,
            totalIndicatorCalculationType: '',
            compassIndicator: false,
            coreIndicator: false,
            statement: new Statement,
            unit: '',
            blockAfterUpdate: false,
            dissagregationsAssignationToIndicator: [],
            customDissagregationAssignationToIndicators: [],
            resultManager: new User,
        },
        anualTarget: 1000,
        anualExecution: 750,
        resultManagerIndicatorQuarter: [
            {
                quarter: 1,
                quarterExecution: 200,
                resultManagerQuarterPopulationType: [
                    {
                        quarterPopulationTypeExecution: 120,
                        populationType: {
                            id: 1,
                            name: "Children",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: true,
                    },
                    {
                        quarterPopulationTypeExecution: 80,
                        populationType: {
                            id: 2,
                            name: "Adults",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: false,
                    }
                ],
                resultManagerQuarterImplementer: [
                    {
                        IndicatorExecution: {
                            id: 1,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 100,
                    },
                    {
                        IndicatorExecution: {
                            id: 2,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 100,
                    }
                ]
            },
            {
                quarter: 2,
                quarterExecution: 300,
                resultManagerQuarterPopulationType: [
                    {
                        quarterPopulationTypeExecution: 180,
                        populationType: {
                            id: 1,
                            name: "Children",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: true,
                    },
                    {
                        quarterPopulationTypeExecution: 120,
                        populationType: {
                            id: 2,
                            name: "Adults",
                            order: 0,
                            groupName: '',
                            regionGroupName: '',
                            otherGroupName: '',
                            state: ''
                        },
                        confirmation: true,
                    }
                ],
                resultManagerQuarterImplementer: [
                    {
                        IndicatorExecution: {
                            id: 3,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 150,
                    },
                    {
                        IndicatorExecution: {
                            id: 4,
                            commentary: '',
                            activityDescription: '',
                            indicatorType: '',
                            state: '',
                            target: 0,
                            compassIndicator: false,
                            indicator: new Indicator,
                            period: new Period,
                            totalExecution: 0,
                            executionPercentage: 0,
                            lastReportedQuarter: new Quarter,
                            lastReportedMonth: new Month,
                            quarters: [],
                            late: '',
                            project: new Project,
                            projectStatement: new Statement,
                            reportingOffice: new Office,
                            supervisorUser: new User,
                            assignedUser: new User,
                            assignedUserBackup: new User,
                            locations: [],
                            keepBudget: false,
                            assignedBudget: 0,
                            availableBudget: 0,
                            totalUsedBudget: 0
                        },
                        quarterImplementerExecution: 150,
                    }
                ]
            }
        ],
        hasExecutions: false
    };

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private periodService: PeriodService,
        private indicatorService: IndicatorService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private cd: ChangeDetectorRef,
        private reportsService: ReportsService,
        private percentagePipe: PercentagePipe,
        private resultManagerService: ResultManagerService,
        private userService: UserService
    ) {}

    ngOnInit(): void {

        this.loadItems();
        this.cols = [
            { field: 'indicator.id', header: 'Código', type: ColumnDataType.text },
            {
                field: 'indicator.description',
                header: 'Indicador',
                type: ColumnDataType.text,
            },
            {
                field: 'anualTarget',
                header: 'Meta Anual',
                type: ColumnDataType.text,
            },
            {
                field: 'anualExecution',
                header: 'Ejecución Anual',
                type: ColumnDataType.text,
            },
            {
                field: 'anualPercentage',
                header: 'Porcentaje Anual',
                type: ColumnDataType.text,  
            },
            {
                field: 'confirmation',
                header: 'Confirmación',
                type: ColumnDataType.text,
            },            
        ];

        this._selectedColumns = this.cols.filter(
            (value) => value.field !== 'id'
        );


        this.formItem = this.fb.group({
           
        });
    }

    expandedRowKeys: { [key: number]: boolean } = {}; // Objeto para manejar las filas expandidas

    toggleRow(rowData: any) {
        if (this.expandedRowKeys[rowData.indicador.id]) {
            delete this.expandedRowKeys[rowData.indicador.id]; // Si está expandida, la colapsamos
        } else {
            this.expandedRowKeys[rowData.indicador.id] = true; // Si no está expandida, la expandimos
        }
    }

    private loadItems() {


        this.items = [
            this.exampleData1,
            this.exampleData2,
            this.exampleData3,
        ]
        

        // this.items.forEach(item => {
        //     item.anualPercentage = item.anualTarget 
        //         ? (item.anualExecution / item.anualTarget) * 100 
        //         : 0;
        // });

        // this.tagService.getAll().subscribe({
        //     next: (value) => {
        //         this.items = value;

        //     },
        //     error: (err) => {
        //         this.messageService.add({
        //             severity: 'error',
        //             summary: 'Error al cargar los tags',
        //             detail: err.error.message,
        //             life: 3000,
        //         });
        //     },
        // });
    }
    
    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(
            this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'tags'
        );
    }
    
    saveItem() {

    }

    openDialog(ResultManagerIndicator: ResultManagerIndicator) {
       
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
        this._selectedColumns = this.cols.filter((col) => val.includes(col));
    }   

    private loadResultManagerIndicators() {
        const period=3;
        const user=this.userService.getLogedUsername();
        this.resultManagerService.getAll(period, user.id).subscribe({
            next: (value) => {
                this.resultManagerIndicators = value;
                console.log(this.resultManagerIndicators)
            },
            error: (err) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los indicadores del Manager de resultado',
                    detail: err.error.message,
                    life: 3000,
                });
            },
        });
    }
}
