import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {FilterService, MessageService, SelectItem} from 'primeng/api';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';
import {UtilsService} from '../../shared/services/utils.service';
import {ProjectService} from '../../shared/services/project.service';
import {ActivatedRoute} from '@angular/router';
import {IndicatorExecution, Project} from '../../shared/model/OsmosysModel';
import {ColumnTable} from '../../shared/model/UtilsModel';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {EnumValuesToLabelPipe} from '../../shared/pipes/enum-values-to-label.pipe';
import {trigger, state, style, transition, animate} from '@angular/animations';
import {DialogService} from 'primeng/dynamicdialog';
import {GeneralIndicatorFormComponent} from '../../indicator-forms/general-indicator-form/general-indicator-form.component';
import {PerformanceIndicatorFormComponent} from '../../indicator-forms/performance-indicator-form/performance-indicator-form.component';
import {IndicatorPipe} from '../../shared/pipes/indicator.pipe';

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
    public generalIndicators: IndicatorExecution[];

    // tslint:disable-next-line:variable-name
    _selectedColumnsGeneralIndicators: ColumnTable[];
    colsGeneralIndicators: ColumnTable[];

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
        private indicatorPipe: IndicatorPipe,
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


    @Input() get selectedColumnsGeneralIndicators(): any[] {
        return this._selectedColumnsGeneralIndicators;
    }

    set selectedColumnsGeneralIndicators(val: any[]) {
        // restore original order
        this._selectedColumnsGeneralIndicators = this.colsGeneralIndicators.filter(col => val.includes(col));
    }

    viewDesagregationGeneralIndicator(parameters: Map<string, number | IndicatorExecution>) {
        const monthId = parameters.get('monthId') as number;
        const indicatorExecution = parameters.get('indicator') as IndicatorExecution;
        const ref = this.dialogService.open(GeneralIndicatorFormComponent, {
                header: 'Indicador General: ' + indicatorExecution.indicator.description,
                width: '90%',
                height: '90%',
                closeOnEscape: false,
                autoZIndex: true,
                closable: true,

                data: {
                    indicatorExecution,
                    monthId,
                    projectId: this.project.id
                }
            }
        );
        ref.onClose.subscribe(() => {
            this.loadProject(this.idProjectParam);
        }, () => {
            this.loadProject(this.idProjectParam);
        });
    }

    viewDesagregationPerformanceIndicator(parameters: Map<string, number | IndicatorExecution>) {
        const monthId = parameters.get('monthId') as number;
        const indicatorExecution = parameters.get('indicator') as IndicatorExecution;
        const ref = this.dialogService.open(PerformanceIndicatorFormComponent, {
                header: 'Indicador: ' + this.indicatorPipe.transform(indicatorExecution.indicator),
                width: '90%',
                height: '90%',
                closeOnEscape: false,
                autoZIndex: true,
                closable: true,

                data: {
                    indicatorExecution,
                    monthId,
                    projectId: this.project.id
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
