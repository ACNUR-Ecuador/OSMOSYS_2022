import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ImportFile, Period, ProjectResume} from '../../shared/model/OsmosysModel';
import {PeriodService} from '../../services/period.service';
import {MessageService, SelectItem} from 'primeng/api';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {UtilsService} from '../../services/utils.service';
import {EnumsService} from '../../services/enums.service';
import {Router} from '@angular/router';
import {ProjectService} from '../../services/project.service';
import {UserService} from '../../services/user.service';
import {Table} from 'primeng/table';
import {HttpResponse} from "@angular/common/http";

@Component({
    selector: 'app-partner-project-list-administration',
    templateUrl: './partner-project-list-administration.component.html',
    styleUrls: ['./partner-project-list-administration.component.scss']
})
export class PartnerProjectListAdministrationComponent implements OnInit {

    periodForm: FormGroup;
    periods: Period[];
    periodsItems: SelectItem[];
    cols: ColumnTable[];
    items: ProjectResume[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    states: SelectItem[];

    showDialogImport = false;
    importForm: FormGroup;

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
            this.periodsItems = this.periods.map(value1 => {
                const selectItem: SelectItem = {
                    label: value1.year.toString(),
                    value: value1
                };
                return selectItem;
            });


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
        if (this.userService.hasAnyRole(['SUPER_ADMINISTRADOR', 'ADMINISTRADOR'])) {
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
            // {field: 'organizationId', header: 'Id Organización', type: ColumnDataType.numeric},
            {field: 'organizationDescription', header: 'Organización', type: ColumnDataType.text},
            {field: 'organizationAcronym', header: 'Organización Acr.', type: ColumnDataType.text},
            // {field: 'periodYear', header: 'Periodo', type: ColumnDataType.numeric},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},

        ];

        const hiddenColumns: string[] = ['id', 'organizationId', 'periodId'];
        this._selectedColumns = this.cols.filter(value => !hiddenColumns.includes(value.field));
        this.importForm = this.fb.group({
            period: new FormControl('', [Validators.required]),
            fileName: new FormControl('', [Validators.required]),
            file: new FormControl(''),
        });
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

    initiateCatalogImport() {
        this.importForm.reset();
        this.showDialogImport = true;

    }

    importCatalog() {
        const {
            period,
            fileName,
            file
        } = this.importForm.value;
        const importFile: ImportFile = {
            period,
            fileName,
            file
        };

        this.projectService.importCatalog(importFile).subscribe({
            next: () => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Catálogo cargado correctamente',
                    life: 3000
                });
                this.loadPeriods();
                this.showDialogImport = false;
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al descargar la plantilla',
                    detail: err.error.message,
                    life: 3000
                });
            }
        })

    }

    downloadImportTemplate() {
        const period: Period = this.importForm.get('period').value;
        if (!period || !period.id) {
            this.messageService.add({
                severity: 'error',
                summary: 'Selecciona un periodo',
                life: 3000
            });
        }
        this.projectService.getImportTemplate(period.id).subscribe({
            next: (response: HttpResponse<Blob>) => {
                this.utilsService.downloadFileResponse(response);
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al descargar la plantilla',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    fileUploader(event: any) {
        const file = event.files[0];
        this.importForm.get('fileName').setValue(file.name);
        this.importForm.get('fileName').markAsTouched();
        // event.
        const fileReader = new FileReader();

        fileReader.readAsDataURL(file);
        // tslint:disable-next-line:only-arrow-functions
        fileReader.onload = () => {
            this.importForm.get('file').setValue(fileReader.result);
            this.importForm.get('file').markAsTouched();
        };
    }

    cancelImportDialog() {
        this.showDialogImport = false;
        this.importForm.reset();
    }
}
