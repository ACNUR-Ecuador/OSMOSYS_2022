import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, ConfirmEventType, FilterService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {UserService} from '../../shared/services/user.service';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';
import {Location} from '@angular/common';
import {
    Canton, CantonForList,
    GeneralIndicator, Indicator, IndicatorExecutionAdministrationResumeWeb, IndicatorExecutionAssigment,
    IndicatorExecutionGeneralIndicatorAdministrationResumeWeb, IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb,
    Period,
    Project, QuarterResumeWeb, TargetUpdateDTOWeb
} from '../../shared/model/OsmosysModel';
import {OrganizationService} from '../../shared/services/organization.service';
import {CantonService} from '../../shared/services/canton.service';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {EnumsService} from '../../shared/services/enums.service';
import {OfficeOrganizationPipe} from '../../shared/pipes/officeOrganization.pipe';
import {PeriodService} from '../../shared/services/period.service';
import {ProjectService} from '../../shared/services/project.service';
import {User} from '../../shared/model/User';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {validate} from 'codelyzer/walkerFactory/walkerFn';
import {IndicatorService} from '../../shared/services/indicator.service';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {QuarterService} from '../../shared/services/quarter.service';
import {BooleanYesNoPipe} from '../../shared/pipes/boolean-yes-no.pipe';

@Component({
    selector: 'app-partner-project-administration',
    templateUrl: './partner-project-administration.component.html',
    styleUrls: ['./partner-project-administration.component.scss']
})
export class PartnerProjectAdministrationComponent implements OnInit {
    public periods: Period[];
    public cantones: Canton[];

    public generalIndicators: IndicatorExecutionGeneralIndicatorAdministrationResumeWeb[] = [];
    public performanceIndicators: IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb[] = [];
    public organizations: SelectItem[];
    public states: SelectItem[];
    public formItem: FormGroup;
    public formTargets: FormGroup;
    public formLocations: FormGroup;
    public formPerformanceIndicator: FormGroup;
    public idProjectParam;
    public idPeriodParam;
    public focalPoints: User[];
    public indicatorOptions: SelectItem[] = [];
    public showLocationMenu = false;
    public showPerformanceIndicatorDialog = false;
    cols: ColumnTable[];
    colsCantonList: ColumnTable[];
    colsCantonListNotEditable: ColumnTable[];
    showLocationsDialog = false;
    colsGeneralIndicators: ColumnTable[];
    colsPerformancelIndicators: ColumnTable[];
    // tslint:disable-next-line:variable-name
    _selectedColumnsGeneralIndicators: ColumnTable[];
    // tslint:disable-next-line:variable-name
    _selectedColumnsPerformanceIndicators: ColumnTable[];
    public showTargetDialog = false;


    constructor(
        private route: ActivatedRoute,
        private activatedRoute: ActivatedRoute,
        private location: Location,
        private fb: FormBuilder,
        private messageService: MessageService,
        public utilsService: UtilsService,
        private userService: UserService,
        private indicatorService: IndicatorService,
        private quarterService: QuarterService,
        private enumsService: EnumsService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private organizationService: OrganizationService,
        private cantonService: CantonService,
        private periodService: PeriodService,
        private projectService: ProjectService,
        private confirmationService: ConfirmationService,
        public officeOrganizationPipe: OfficeOrganizationPipe,
        private indicatorExecutionService: IndicatorExecutionService,
        private codeDescriptionPipe: CodeDescriptionPipe,
        private booleanYesNoPipe: BooleanYesNoPipe,
        private router: Router
    ) {

        this.idProjectParam = this.route.snapshot.paramMap.get('projectId');
        this.idPeriodParam = this.route.snapshot.paramMap.get('periodId');

        if (this.idProjectParam === 'null') {
            this.idProjectParam = null;
        }
        if (this.idPeriodParam === 'null') {
            this.idPeriodParam = null;
        }
    }

    ngOnInit(): void {
        this.showLocationMenu = true;

        this.cols = [
            {field: 'provincia.description', header: 'Provincia', type: ColumnDataType.text},
            {field: 'description', header: 'Cantón', type: ColumnDataType.text}
        ];

        this.colsCantonList = [
            {field: 'provincia.description', header: 'Provincia', type: ColumnDataType.text},
            {field: 'description', header: 'Cantón', type: ColumnDataType.text},
            {field: 'enabled', header: 'Activo', type: ColumnDataType.boolean, pipeRef: this.booleanYesNoPipe}
        ];
        this.colsCantonListNotEditable = this.colsCantonList.filter(value => {
            return value.field !== 'enabled';
        });
        this.createForms();
        this.createTables();
        this.loadOptions();
        if (this.idProjectParam) {
            const idProject = Number(this.idProjectParam);
            this.loadProject(idProject);

        } else if (this.idPeriodParam) {
            this.periodService.getById(this.idPeriodParam as number).subscribe(value => {
                this.periods = [];
                this.periods.push(value);
                const project = new Project();
                project.period = value;

                this.formItem.patchValue(project);
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar el periodo',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar la pantalla',
                detail: 'Parámetros incorrectos',
                life: 3000
            });
        }
    }

    private loadProject(idProject: number) {
        this.projectService.getProjectById(idProject).subscribe(value => {
            const {
                id,
                code,
                name,
                state,
                organization,
                period,
                startDate,
                endDate,
                locations,
                focalPoint
            } = value;
            this.sortCantones(locations);
            this.formItem.patchValue({
                id,
                code,
                name,
                state,
                organization,
                period,
                startDate,
                endDate,
                locations,
                focalPoint
            });
            this.periods = [];
            this.periods.push(period);
            this.loadGeneralIndicators(id);
            this.assignNewPerformanceIndicator();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar el proyecto',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    loadGeneralIndicators(projectId: number) {
        this.indicatorExecutionService.getGeneralIndicatorAdministrationResume(projectId).subscribe(value => {
            this.generalIndicators = value;
            this.loadPerformanceIndicators(projectId);
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar el proyecto',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    loadPerformanceIndicators(projectId: number) {
        this.indicatorExecutionService.getPerformanceIndicatorAdministrationResume(projectId).subscribe(value => {
            this.performanceIndicators = value;
            this.showTargetAlerts();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar el proyecto',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    showTargetAlerts() {
        let message = '';
        let showAlert = false;

        if (this.performanceIndicators.filter(value => {
            return value.state === EnumsState.ACTIVE && value.target === null;
        }).length < 1) {
            message += 'El proyecto no tiene indicadores de rendimiento asignados. </br>';
            showAlert = true;
        }
        const generalIndicatorsTargetsToAlert = this.generalIndicators.filter(value => {
            return value.state === EnumsState.ACTIVE && value.target === null;
        });
        const performanceIndicatorsTargetsToAlert = this.performanceIndicators.filter(value => {
            return value.state === EnumsState.ACTIVE && value.target === null;
        });

        if (generalIndicatorsTargetsToAlert.length > 0 || performanceIndicatorsTargetsToAlert.length > 0) {
            showAlert = true;
            message += 'Las metas de los siguientes indicadores están pendientes de actualización. </br>';
            generalIndicatorsTargetsToAlert.forEach(value => {
                message = message + 'Indicador General: ' + value.indicatorDescription;
            });
            performanceIndicatorsTargetsToAlert.forEach(value => {
                message = message + 'Indicador de Rendimiento: ' + value.indicatorDescription;
            });


        }
        if (showAlert) {
            this.confirmationService.confirm({
                message,
                header: 'Proyecto pendiente de actualización',
                icon: 'pi pi-exclamation-triangle',
                accept: () => {
                    this.messageService.add({severity: 'warn', summary: 'Confirmado', detail: 'No olvides actualizar el proyecto'});
                }
            });
        }

    }

    loadOptions() {
        this.cantonService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.cantones = this.sortCantones(value);
            this.cantones = this.cantones.map(value1 => {
                const can: any = value1;
                can.provinciaDescription = value1.provincia.description;
                return can;
            });

        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los cantones',
                detail: error.error.message,
                life: 3000
            });
        });

        this.organizationService.getByState(EnumsState.ACTIVE).subscribe(value => {
            this.organizations = value.filter(value1 => {
                return value1.acronym.toLowerCase() !== 'acnur';
            }).map(value1 => {
                return {label: this.officeOrganizationPipe.transform(value1), value: value1} as SelectItem;
            });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las organizaciones',
                detail: error.error.message,
                life: 3000
            });
        });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los estados',
                detail: error.error.message,
                life: 3000
            });
        });

        this.userService.getActiveUNHCRUsers().subscribe(value => {
            this.focalPoints = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los puntos focales',
                detail: error.error.message,
                life: 3000
            });
        });


    }


    private createForms() {
        this.formItem = this.fb.group({
            id: new FormControl(''),
            code: new FormControl('', Validators.required),
            name: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
            organization: new FormControl('', Validators.required),
            period: new FormControl('', Validators.required),
            startDate: new FormControl('', Validators.required),
            endDate: new FormControl('', Validators.required),
            focalPoint: new FormControl('', Validators.required),
            locations: new FormControl('')
        });

        this.formLocations = this.fb.group({
            locationsSelected: new FormControl('')
        });

        this.formTargets = this.fb.group({
            indicatorExecutionId: new FormControl(''),
            quarterGroups: this.fb.array([])
        });

        this.formPerformanceIndicator = this.fb.group({
            id: new FormControl(''),
            commentary: new FormControl(''),
            indicator: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
            project: new FormControl(''),
            locations: new FormControl('')
        });


    }

    saveItem() {
        this.messageService.clear();
        const {
            id,
            code,
            name,
            state,
            organization,
            period,
            startDate,
            endDate,
            locations,
            focalPoint
        } = this.formItem.value;
        const project: Project = {
            id,
            code,
            name,
            state,
            organization,
            period,
            startDate,
            endDate,
            focalPoint,
            locations: []
        };
        if (!locations || locations.length < 1) {
            this.messageService.add({
                severity: 'error',
                summary: 'Agrega al menos un cantón'
            });
            return;
        }

        project.locations = (locations as any[]).map(value1 => {
            delete value1.provinciaDescription;
            return value1 as Canton;
        });

        if (project.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.projectService.update(project).subscribe(id => {
                this.loadProject(id);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Guardado con éxito',
                    life: 3000
                });
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al guardar el proyecto',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.projectService.save(project).subscribe(id => {

                this.messageService.add({
                    severity: 'success',
                    summary: 'Guardado con éxito',
                    life: 3000
                });
                this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
                    this.router.navigate(['/administration/partnerProjectAdministration', {projectId: id}])
                );

            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al guardar el proyecto',
                    detail: error.error.message,
                    life: 3000
                });
            });
        }

    }

    cancel() {
        console.log(this.formItem);
    }

    saveLocationsForm() {
        this.showLocationsDialog = false;
        if (this.formLocations.get('locationsSelected').value) {
            const cantonesG = this.sortCantones(this.formLocations.get('locationsSelected').value);
            this.formItem.get('locations').patchValue(cantonesG);
        }
    }

    cancelDialogLocations() {
        this.showLocationsDialog = false;
    }

    editLocations() {
        this.showLocationsDialog = true;
        if (!this.formItem.get('locations').value) {
            this.formLocations.get('locationsSelected').patchValue([]);
        } else {
            this.formLocations.get('locationsSelected').patchValue(this.formItem.get('locations').value);
        }
    }

    private sortCantones(cantones: Canton[]): Canton[] {
        return cantones.sort((a, b) => {
            const x = a.provincia.description.localeCompare(b.provincia.description);
            if (x === 0) {
                return a.description.localeCompare(b.description);
            } else {
                return x;
            }
        });
    }

    @Input() get selectedColumnsGeneralIndicators(): any[] {
        return this._selectedColumnsGeneralIndicators;
    }

    set selectedColumnsGeneralIndicators(val: any[]) {
        // restore original order
        this._selectedColumnsGeneralIndicators = this.colsGeneralIndicators.filter(col => val.includes(col));
    }

    @Input() get selectedColumnsPerformanceIndicators(): any[] {
        return this._selectedColumnsPerformanceIndicators;
    }

    set selectedColumnsPerformanceIndicators(val: any[]) {
        // restore original order
        this._selectedColumnsPerformanceIndicators = this.cols.filter(col => val.includes(col));
    }

    private createTables() {
        this.colsGeneralIndicators = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            // {field: 'commentary', header: 'Código', type: ColumnDataType.numeric},
            {field: 'indicatorDescription', header: 'Descripción', type: ColumnDataType.text},
            {field: 'target', header: 'Meta', type: ColumnDataType.text},
            {field: 'totalExecution', header: 'Ejecución actual', type: ColumnDataType.text},
            {field: 'executionPercentage', header: 'Porcentaje de ejecución', type: ColumnDataType.numeric},
            // {field: 'indicatorType', header: 'Estado', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
        ];
        this._selectedColumnsGeneralIndicators = this.colsGeneralIndicators.filter(value => value.field !== 'id');
        this.colsPerformancelIndicators = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'indicatorCode', header: 'Código', type: ColumnDataType.text},
            {field: 'indicatorDescription', header: 'Descripción', type: ColumnDataType.text},
            {field: 'target', header: 'Meta', type: ColumnDataType.text},
            {field: 'totalExecution', header: 'Ejecución actual', type: ColumnDataType.text},
            {field: 'executionPercentage', header: 'Porcentaje de ejecución', type: ColumnDataType.numeric},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
        ];
        this._selectedColumnsPerformanceIndicators = this.colsPerformancelIndicators.filter(value => value.field !== 'id');
    }

    updateTargets(
        indicator: IndicatorExecutionAdministrationResumeWeb
    ) {
        this.formTargets = this.fb.group({
            indicatorExecutionId: new FormControl(''),
            quarterGroups: this.fb.array([])
        });
        this.utilsService.resetForm(this.formTargets);
        this.formTargets.get('indicatorExecutionId').patchValue(indicator.id);
        const quarters = indicator.quarters
            .filter(value => value.state === EnumsState.ACTIVE)
            .sort((a, b) => a.order - b.order);
        const quarterGroup = this.fb.array([]);
        this.quarterGroups.patchValue([]);
        quarters.forEach(quarter => {
            const control = this.fb.group({
                id: new FormControl(quarter.id),
                quarter: new FormControl(quarter.quarter),
                commentary: new FormControl(quarter.commentary),
                order: new FormControl(quarter.order),
                year: new FormControl(quarter.year),
                target: new FormControl(quarter.target, Validators.required),
                totalExecution: new FormControl(quarter.totalExecution),
                executionPercentage: new FormControl(quarter.executionPercentage),
                state: new FormControl(quarter.state),
            });
            this.quarterGroups.push(control);
        });
        this.showTargetDialog = true;
    }

    get quarterGroups(): FormArray {
        return this.formTargets.controls.quarterGroups as FormArray;
    }

    saveTargets() {
        this.messageService.clear();
        console.log(this.formTargets);
        console.log(this.formTargets.controls.quarterGroups.value);
        const targetForms = this.formTargets.controls.quarterGroups.value as Array<any>;
        const targetUpdateDTOWeb: TargetUpdateDTOWeb = new TargetUpdateDTOWeb();
        targetUpdateDTOWeb.indicatorExecutionId = this.formTargets.get('indicatorExecutionId').value;
        targetUpdateDTOWeb.quarters = targetForms.map(value => {
            const {
                id,
                quarter,
                commentary,
                order,
                year,
                target,
                totalExecution,
                executionPercentage,
                state
            } = value;
            const q: QuarterResumeWeb = {
                id,
                quarter,
                commentary,
                order,
                year,
                target,
                totalExecution,
                executionPercentage,
                state
            };
            return q;
        });

        this.indicatorExecutionService.updateTargets(targetUpdateDTOWeb).subscribe(value => {
            this.messageService.add({
                severity: 'success',
                summary: 'Metas actualizadas correctamente',
                life: 3000
            });
            const idProject = Number(this.idProjectParam);
            this.loadProject(idProject);
            this.showTargetDialog = false;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al actualizar las metas',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    cancelTargets() {
        this.quarterGroups.patchValue([]);
        this.showTargetDialog = false;
    }

    assignNewPerformanceIndicator() {
        console.log('assignNewPerformanceIndicator');
        const period: Period = this.formItem.get('period').value as Period;

        this.indicatorService.getByPeriodAssignment(period.id).subscribe(value => {
            this.indicatorOptions = value.map(value1 => {
                const selectItem: SelectItem = {
                    value: value1,
                    label: this.codeDescriptionPipe.transform(value1)
                };
                return selectItem;
            });
            this.messageService.clear();
            this.utilsService.resetForm(this.formPerformanceIndicator);
            const newItem = new IndicatorExecutionAssigment();
            this.formPerformanceIndicator.patchValue(newItem);
            this.showPerformanceIndicatorDialog = true;
            const {
                startDate,
                endDate
            } = this.formItem.value;
            const locations: CantonForList [] = this.formItem.get('locations').value;
            locations.map(value1 => {
                const canton = value1 as CantonForList;
                canton.enabled = true;
            });
            this.formPerformanceIndicator.get('locations').patchValue(locations);
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los indicadores del periodo',
                detail: error.error.message,
                life: 3000
            });
        });


    }

    exportExcelPerformancceIndicators() {
        import('xlsx').then(xlsx => {
            const itemsRenamed = this.utilsService.renameKeys(this.performanceIndicators, this.colsPerformancelIndicators);
            const worksheet = xlsx.utils.json_to_sheet(itemsRenamed);
            const workbook = {Sheets: {data: worksheet}, SheetNames: ['data']};
            const excelBuffer: any = xlsx.write(workbook, {bookType: 'xlsx', type: 'array'});
            this.utilsService.saveAsExcelFile(excelBuffer, 'indicadores_rendimiento_' + this.formItem.get('name').value);
        });
    }

    cancelPerformanceIndicatorDialog() {
        console.log(this.formPerformanceIndicator);
        this.messageService.clear();
        this.showPerformanceIndicatorDialog = false;
    }

    savePerformanceIndicator() {
        console.log(this.formPerformanceIndicator.value);
        this.messageService.clear();

        const indicatorExecution: IndicatorExecutionAssigment = new IndicatorExecutionAssigment();
        const {
            state,
            indicator
        } = this.formPerformanceIndicator.value;
        const locationstotal: CantonForList[] = [];
        (this.formPerformanceIndicator.get('locations').value as CantonForList[])
            .forEach(value => locationstotal.push(Object.assign({}, value)));
        const locations: Canton[] = locationstotal.filter(value => {
            return value.enabled;
        }).map(value => {
            delete value.enabled;
            return value as Canton;
        });

        indicatorExecution.indicator = indicator;
        indicatorExecution.state = state;
        indicatorExecution.project = new Project();
        indicatorExecution.project.id = this.formItem.get('id').value;
        indicatorExecution.locations = locations;
        console.log(indicatorExecution);
        this.indicatorExecutionService.assignPerformanceIndicatoToProject(indicatorExecution)
            .subscribe(value => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Indicador agregado correctamente',
                    life: 3000
                });

                this.indicatorExecutionService.getResumeAdministrationPerformanceIndicatorById(value)
                    .subscribe(value1 => {
                        this.updateTargets(value1);
                    }, error => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al recuperar los trimestres',
                            detail: error.error.message,
                            life: 3000
                        });
                    });
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al agregar el indicador',
                    detail: error.error.message,
                    life: 3000
                });
            });

    }
}
