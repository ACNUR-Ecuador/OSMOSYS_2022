import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Period, ProjectResume} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {MessageService, SelectItem} from 'primeng/api';
import {PeriodService} from '../../shared/services/period.service';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {ProjectService} from '../../shared/services/project.service';
import {Router} from '@angular/router';
import {UserService} from '../../shared/services/user.service';
import {EnumValuesToLabelPipe} from '../../shared/pipes/enum-values-to-label.pipe';
import {Table} from 'primeng/table';

@Component({
    selector: 'app-partners-project-list',
    templateUrl: './partners-project-list.component.html',
    styleUrls: ['./partners-project-list.component.scss']
})
export class PartnersProjectListComponent implements OnInit {
    periodForm: FormGroup;
    periods: Period[];
    cols: ColumnTable[];
    items: ProjectResume[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    states: SelectItem[];

    constructor(
        private fb: FormBuilder,
        private periodService: PeriodService,
        private messageService: MessageService,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private projectService: ProjectService,
        private router: Router,
        public userService: UserService,
        private enumValuesToLabelPipe: EnumValuesToLabelPipe
    ) {
    }

    ngOnInit(): void {
        this.createForms();
        this.loadPeriods();
        this.loadOptions();
    }

    loadPeriods() {
        this.periodService.getAll().subscribe(value => {
            this.periods = value;
            if (this.periods.length < 1) {
                this.messageService.add({severity: 'error', summary: 'No se encontraron periodos', detail: ''});
            } else {
                const currentYear = (new Date()).getFullYear();
                if (this.periods.some(e => e.year === currentYear)) {
                    this.periods.filter(p => p.year === currentYear).forEach(value1 => {
                        this.periodForm.get('selectedPeriod').patchValue(value1);
                        if (value1) {
                            this.loadProjects(value1.id);
                        }
                    });
                } else {
                    const smallestYear = Math.min(...this.periods.map(value1 => value1.year));
                    const smallestPeriod = this.periods.filter(value1 => {
                        return value1.year === smallestYear;
                    })[0];
                    this.periodForm.get('selectedPeriod').patchValue(smallestPeriod);
                    this.loadProjects(smallestPeriod.id);
                }
            }
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las áreas',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    private loadProjects(periodId: number) {
        if (this.userService.isUNHCRUser()) {
            this.projectService.getProjectResumenWebByPeriodId(periodId).subscribe(value => {
                this.items = value;
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los proyectos',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            this.projectService
                .getProjectResumenWebByPeriodIdAndOrganizationId(periodId, this.userService.getLogedUsername().organization.id)
                .subscribe(value => {
                    this.items = value;
                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los proyectos',
                        detail: error.error.message,
                        life: 3000
                    });
                });
        }

    }

    private createForms() {
        this.periodForm = this.fb.group({
            selectedPeriod: new FormControl('')
        });
        this.cols = [
            {field: 'id', header: 'id', type: ColumnDataType.numeric},
            {field: 'code', header: 'Código', type: ColumnDataType.text},
            {field: 'name', header: 'Título del proyecto', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text, pipeRef: this.enumValuesToLabelPipe, arg1: EnumsType.State},
            {field: 'organizationId', header: 'Id Organización', type: ColumnDataType.numeric},
            {field: 'organizationDescription', header: 'Organización', type: ColumnDataType.text},
            {field: 'organizationAcronym', header: 'Organización Acr.', type: ColumnDataType.text},
            {field: 'periodId', header: 'Id Periodo', type: ColumnDataType.numeric},
            {field: 'periodYear', header: 'Periodo', type: ColumnDataType.numeric},
        ];

        const hiddenColumns: string[] = ['id', 'organizationId', 'periodId', 'periodYear'];
        this._selectedColumns = this.cols.filter(value => !hiddenColumns.includes(value.field));
    }


    onPeriodChange(period: Period) {
        this.loadProjects(period.id);
    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'proyectos');
    }

    createItem() {
        const period = this.periodForm.get('selectedPeriod').value;
        this.router.navigate(['/administration/partnerProjectAdministration', {periodId: period.id}]);
    }

    goToProject(projectId: number) {
        this.router.navigate(['/partners/partnersProject', {projectId}]);
    }

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }

    private loadOptions() {
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
    }
}
