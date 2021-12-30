import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {FilterService, MessageService, SelectItem} from 'primeng/api';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';
import {UtilsService} from '../../shared/services/utils.service';
import {ProjectService} from '../../shared/services/project.service';
import {ActivatedRoute} from '@angular/router';
import {Indicator, IndicatorExecutionResumeWeb, Project} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {EnumValuesToLabelPipe} from '../../shared/pipes/enum-values-to-label.pipe';
import {trigger, state, style, transition, animate} from '@angular/animations';
import {DialogService} from 'primeng/dynamicdialog';
import {GeneralIndicatorFormComponent} from '../../indicator-forms/general-indicator-form/general-indicator-form.component';

@Component({
    selector: 'app-partners-project',
    templateUrl: './partners-project.component.html',
    styleUrls: ['./partners-project.component.scss'],
    animations: [
        trigger('rowExpansionTrigger', [
            state('void', style({
                transform: 'translateX(-10%)',
                opacity: 0
            })),
            state('active', style({
                transform: 'translateX(0)',
                opacity: 1
            })),
            transition('* <=> *', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
        ])
    ]
})
export class PartnersProjectComponent implements OnInit {

    public idProjectParam;

    public formItem: FormGroup;
    public states: SelectItem[];
    public project: Project;
    public generalIndicators: IndicatorExecutionResumeWeb[];

    // tslint:disable-next-line:variable-name
    _selectedColumnsGeneralIndicators: ColumnTable[];
    colsGeneralIndicators: ColumnTable[];
    colsGeneralIndicatorsIndicatorExecution: ColumnTable[] = [];

    constructor(
        public dialogService: DialogService,
        public utilsService: UtilsService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private messageService: MessageService,
        private projectService: ProjectService,
        private indicatorExecutionService: IndicatorExecutionService,
        private codeDescriptionPipe: CodeDescriptionPipe,
        private enumValuesToLabelPipe: EnumValuesToLabelPipe,
        private route: ActivatedRoute
    ) {
        this.idProjectParam = this.route.snapshot.paramMap.get('projectId');
        if (this.idProjectParam === 'null') {
            this.idProjectParam = null;
        }

    }

    ngOnInit(): void {
        this.loadProject(this.idProjectParam);


    }

    private loadProject(idProject: number) {
        this.projectService.getProjectById(idProject).subscribe(value => {
            this.project = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar el proyecto',
                detail: error.error.message,
                life: 3000
            });
        });
    }


    private createGeneralIndicatorColumns() {

        this.colsGeneralIndicators = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'indicatorType', header: 'Tipo', type: ColumnDataType.text},
            {field: 'indicator', header: 'Indicador', type: ColumnDataType.text, pipeRef: this.codeDescriptionPipe},
            {field: 'target', header: 'Meta', type: ColumnDataType.numeric},
            {field: 'totalExecution', header: 'Ejecución Actual', type: ColumnDataType.numeric},
            {field: 'executionPercentage', header: 'Porcentaje de ejecución', type: ColumnDataType.numeric},

        ];

        const hiddenColumns: string[] = ['id'];
        this._selectedColumnsGeneralIndicators = this.colsGeneralIndicators.filter(value => !hiddenColumns.includes(value.field));
        if (this.generalIndicators && this.generalIndicators.length > 0) {
            const generalIndicator = this.generalIndicators[0];
            this.colsGeneralIndicatorsIndicatorExecution = [];
            const quarters = generalIndicator.quarters.filter(value => {
                value.state = EnumsState.ACTIVE;
            });
            if (quarters && quarters.length > 0) {
                quarters.forEach(value => {
                    this.colsGeneralIndicatorsIndicatorExecution.push(
                        {field: 'order', header: 'Orden', type: ColumnDataType.numeric},
                        {field: 'year', header: 'Año', type: ColumnDataType.numeric},
                        {field: 'quarter', header: 'Trimestre', type: ColumnDataType.text},
                        {field: 'target', header: 'Año', type: ColumnDataType.numeric},
                        {field: 'totalExecution', header: 'Año', type: ColumnDataType.numeric},
                        {field: 'executionPercentage', header: 'Año', type: ColumnDataType.numeric},
                    );
                });
            }
        }
    }

    @Input() get selectedColumnsGeneralIndicators(): any[] {
        return this._selectedColumnsGeneralIndicators;
    }

    set selectedColumnsGeneralIndicators(val: any[]) {
        // restore original order
        this._selectedColumnsGeneralIndicators = this.colsGeneralIndicators.filter(col => val.includes(col));
    }

    generalIndicatorsExpand(): number {
        if (this._selectedColumnsGeneralIndicators) {
            return this._selectedColumnsGeneralIndicators.length;
        } else {
            return 1;
        }
    }

    viewDesagregationGeneralIndicator(parameters: Map<string, number | IndicatorExecutionResumeWeb>) {
        const monthId = parameters.get('monthId') as number;
        const indicatorExecution = parameters.get('indicator') as IndicatorExecutionResumeWeb;
        const ref = this.dialogService.open(GeneralIndicatorFormComponent, {
                header: 'Indicador General: ' + indicatorExecution.indicator.description,
                width: '90%',
                height: '90%',
                closeOnEscape: false,
                autoZIndex: true,
                closable: true,

                data: {
                    indicatorExecution,
                    monthId
                }
            }
        );
        ref.onClose.subscribe(value => {
            this.loadProject(this.idProjectParam);
        }, error => {
            this.loadProject(this.idProjectParam);
        });
    }
}
