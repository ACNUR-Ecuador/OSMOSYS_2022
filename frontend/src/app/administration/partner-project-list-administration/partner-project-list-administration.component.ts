import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Period, ProjectResume} from '../../shared/model/OsmosysModel';
import {PeriodService} from '../../shared/services/period.service';
import {MessageService, SelectItem} from 'primeng/api';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {Router} from '@angular/router';
import {ProjectService} from '../../shared/services/project.service';
import {UserService} from '../../shared/services/user.service';

@Component({
    selector: 'app-partner-project-list-administration',
    templateUrl: './partner-project-list-administration.component.html',
    styleUrls: ['./partner-project-list-administration.component.scss']
})
export class PartnerProjectListAdministrationComponent implements OnInit {

    periodForm: FormGroup;
    periods: Period[];
    cols: ColumnTable[];
    items: ProjectResume[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    private states: SelectItem[];

    constructor(
        private fb: FormBuilder,
        private periodService: PeriodService,
        private messageService: MessageService,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private projectService: ProjectService,
        private router: Router,
        public userService: UserService) {
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
        if (this.userService.hasAnyRole(['SUPER_ADMINISTRADOR', 'ADMINISTRATOR'])) {
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
            // tslint:disable-next-line:max-line-length
            this.projectService.getProjectResumenWebByPeriodIdAndFocalPointId(periodId, this.userService.getLogedUsername().id).subscribe(value => {
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
            {field: 'name', header: 'Nombre', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
            {field: 'organizationId', header: 'Id Organización', type: ColumnDataType.numeric},
            {field: 'organizationDescription', header: 'Organización', type: ColumnDataType.text},
            {field: 'organizationAcronym', header: 'Organización Acr.', type: ColumnDataType.text},
            {field: 'periodId', header: 'Id Periodo', type: ColumnDataType.numeric},
            {field: 'periodYear', header: 'Periodo', type: ColumnDataType.numeric},
        ];

        const hiddenColumns: string[] = ['id', 'organizationId', 'periodId'];
        this._selectedColumns = this.cols.filter(value => !hiddenColumns.includes(value.field));
    }


    onPeriodChange(period: Period) {
        this.loadProjects(period.id);
    }

    exportExcel() {
        import('xlsx').then(xlsx => {
            const headers = this.cols.map(value => value.header);
            const itemsRenamed = this.utilsService.renameKeys(this.items, this.cols);
            const worksheet = xlsx.utils.json_to_sheet(itemsRenamed);
            const workbook = {Sheets: {data: worksheet}, SheetNames: ['data']};

            const excelBuffer: any = xlsx.write(workbook, {bookType: 'xlsx', type: 'array'});
            this.utilsService.saveAsExcelFile(excelBuffer, 'areas');
        });
    }

    createItem() {
        const period = this.periodForm.get('selectedPeriod').value;
        this.router.navigate(['/administration/partnerProjectAdministration', {periodId: period.id}]);
    }

    editItem(projectId: number) {
        this.router.navigate(['/administration/partnerProjectAdministration', {projectId}]);
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
