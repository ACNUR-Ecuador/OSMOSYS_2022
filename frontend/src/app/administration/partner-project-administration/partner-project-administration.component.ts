import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, ConfirmEventType, FilterService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {UserService} from '../../services/user.service';
import {FilterUtilsService} from '../../services/filter-utils.service';
import {Location, PercentPipe} from '@angular/common';
import {
    AreaType,
    Canton,
    CantonForList, ImportFile,
    Indicator, IndicatorExecution,
    IndicatorExecutionAssigment,
    Period,
    Project,
    Quarter,
    Statement,
    TargetUpdateDTOWeb
} from '../../shared/model/OsmosysModel';
import {OrganizationService} from '../../services/organization.service';
import {CantonService} from '../../services/canton.service';
import {

    ColumnDataType,
    ColumnTable,
    DissagregationType,
    EnumsIndicatorType,
    EnumsState,
    EnumsType
} from '../../shared/model/UtilsModel';
import {EnumsService} from '../../services/enums.service';

import {PeriodService} from '../../services/period.service';
import {ProjectService} from '../../services/project.service';
import {User} from '../../shared/model/User';
import {IndicatorExecutionService} from '../../services/indicator-execution.service';
import {IndicatorService} from '../../services/indicator.service';
import {CodeDescriptionPipe} from '../../shared/pipes/code-description.pipe';
import {BooleanYesNoPipe} from '../../shared/pipes/boolean-yes-no.pipe';
import {StatementService} from '../../services/statement.service';
import {IndicatorPipe} from '../../shared/pipes/indicator.pipe';
import {Table} from 'primeng/table';
import {OfficeOrganizationPipe} from '../../shared/pipes/office-organization.pipe';
import {HttpResponse} from "@angular/common/http";

@Component({
    selector: 'app-partner-project-administration',
    templateUrl: './partner-project-administration.component.html',
    styleUrls: ['./partner-project-administration.component.scss']
})
export class PartnerProjectAdministrationComponent implements OnInit {
    public periods: Period[];
    public cantones: Canton[];
    public cantonesAvailable: Canton[];

    public generalIndicators: IndicatorExecution[] = [];
    public performanceIndicators: IndicatorExecution[] = [];
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
    showTargetDialog = false;
    statements: Statement[];
    statementsOptions: SelectItem[];
    messageAlert = '';
    messageAlertArray = [];
    showAlert = false;
    showTargetsPerformanceIndicatorUpdateDialog = false;

    /* update quarters*/
    quarterOrders: number[];
    quarterTitles: string[];
    quartersToUpdate: Quarter[] = [];

    showDialogImport = false;
    importForm: FormGroup;

    constructor(
        private route: ActivatedRoute,
        private activatedRoute: ActivatedRoute,
        private location: Location,
        private fb: FormBuilder,
        private messageService: MessageService,
        public utilsService: UtilsService,
        private userService: UserService,
        private indicatorService: IndicatorService,
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
        private percentPipe: PercentPipe,
        private booleanYesNoPipe: BooleanYesNoPipe,
        private router: Router,
        private statementService: StatementService,
        private indicatorPipe: IndicatorPipe,
        // tslint:disable-next-line:variable-name
        private _location: Location,
        private ref: ChangeDetectorRef
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
        this.createForms();
        this.createTables();
        this.loadOptions();
        this.showLocationMenu = true;

        if (this.idProjectParam) {
            const idProject = Number(this.idProjectParam);
            this.loadProject(idProject);

        } else if (this.idPeriodParam) {
            this.loadPeriod();
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar la pantalla',
                detail: 'Parámetros incorrectos',
                life: 3000
            });
        }
        this.registerFilters();
    }

    private loadPeriod() {

        this.periodService.getById(this.idPeriodParam as number)
            .subscribe({
                next: value => {
                    this.periods = [];
                    this.periods.push(value);
                    const project = new Project();
                    project.period = value;
                    this.formItem.patchValue(project);
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar el periodo',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
    }

    private loadProject(idProject: number) {
        this.projectService.getProjectById(idProject)
            .subscribe({
                next: value => {
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
                    this.utilsService.sortCantones(locations);
                    const originalLocations = [];
                    locations.forEach(val => originalLocations.push(Object.assign({}, val)));
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
                        focalPoint,
                        originalLocations,
                        updateAllLocationsIndicators: false
                    });
                    this.periods = [];
                    this.periods.push(period);
                    this.loadPerformanceIndicatorsOptions(value as Project);
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar el proyecto',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
    }

    loadGeneralIndicators(projectId: number) {
        this.indicatorExecutionService.getGeneralIndicatorAdministrationResume(projectId)
            .subscribe({
                next: value => {
                    this.generalIndicators = value;
                    this.loadPerformanceIndicators(projectId);
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar el proyecto',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
    }

    loadPerformanceIndicators(projectId: number) {
        this.indicatorExecutionService.getPerformanceIndicatorAdministrationResume(projectId)
            .subscribe({
                next: value => {
                    this.performanceIndicators = value;
                    this.showTargetAlerts();
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar el proyecto',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
    }

    showTargetAlerts() {
        this.messageAlert = '';
        if (this.performanceIndicators.filter(value => {
            return value.state === EnumsState.ACTIVE;
        }).length < 1) {
            this.messageAlert += 'El proyecto no tiene indicadores de producto asignados. </br>';
            this.showAlert = true;
        }
        const generalIndicatorsTargetsToAlert = this.generalIndicators
            .filter(value => value.state === EnumsState.ACTIVE).filter(value => {
                return this.utilsService.getTargetNeedUpdate(value);
            });
        const performanceIndicatorsTargetsToAlert = this.performanceIndicators
            .filter(value => value.state === EnumsState.ACTIVE).filter(value => {
                return this.utilsService.getTargetNeedUpdate(value);
            });

        if (generalIndicatorsTargetsToAlert.length > 0 || performanceIndicatorsTargetsToAlert.length > 0) {
            this.showAlert = true;
            this.messageAlert += 'Las metas de los siguientes indicadores están pendientes de actualización. </br>';
            generalIndicatorsTargetsToAlert.forEach(value => {
                this.messageAlert = this.messageAlert + 'Indicador General: ' + this.indicatorPipe.transform(value.indicator) + '</br>';
            });
            performanceIndicatorsTargetsToAlert.forEach(value => {
                this.messageAlert = this.messageAlert + 'Indicador de Producto: ' + this.indicatorPipe.transform(value.indicator) + '</br>';
            });


        }
        if (this.showAlert) {
            this.messageAlertArray = this.messageAlert.split('</br>');
            this.confirmationService.confirm({
                message: this.messageAlert,
                header: 'Proyecto pendiente de actualización',
                icon: 'pi pi-exclamation-triangle',
                key: 'cda',
                accept: () => {
                    this.confirmationService.close();
                }
            });

        }

    }

    loadOptions() {
        this.cantonService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    this.cantones = this.utilsService.sortCantones(value);
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los cantones',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
        this.organizationService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    this.organizations = value.filter(value1 => {
                        return value1.acronym.toLowerCase() !== 'acnur';
                    }).map(value1 => {
                        return {label: this.officeOrganizationPipe.transform(value1), value: value1} as SelectItem;
                    });
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las organizaciones',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });

        this.userService.getActiveUNHCRUsers()
            .subscribe({
                next: value => {
                    this.focalPoints = value;
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los puntos focales',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
        this.statementService.getByState(EnumsState.ACTIVE)
            .subscribe({
                next: value => {
                    this.statements = value
                        .sort((a, b) => a.code.localeCompare(b.code));
                    this.statementsOptions = this.statements
                        .filter(value2 => {
                            return value2.areaType === AreaType.PRODUCTO.toString();
                        })
                        .map(value1 => {
                            return {label: value1.code + '-' + value1.description, value: value1};
                        }).sort((a, b) => a.label.localeCompare(b.label));
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los statements',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
    }

    loadPerformanceIndicatorsOptions(project: Project) {
        this.indicatorService.getByPeriodAssignment(project.period.id)
            .subscribe({
                next: value => {
                    this.indicatorOptions = value
                        .map(value1 => {
                            const selectItem: SelectItem = {
                                value: value1,
                                label: this.indicatorPipe.transform(value1)
                            };
                            return selectItem;
                        });
                    this.loadGeneralIndicators(project.id);
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar los indicadores del periodo',
                        detail: error.error.message,
                        life: 3000
                    });
                }
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
            locations: new FormControl(''),
            originalLocations: new FormControl(''),
            updateAllLocationsIndicators: new FormControl('')
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
            activityDescription: new FormControl(''),
            indicator: new FormControl('', Validators.required),
            isBorrowedStatement: new FormControl(''),
            projectStatement: new FormControl('', Validators.required),
            state: new FormControl('', Validators.required),
            project: new FormControl(''),
            locations: new FormControl(''),
            keepBudget: new FormControl(''),
            assignedBudget: new FormControl(''),
        });

        this.importForm = this.fb.group({
            fileName: new FormControl('', [Validators.required]),
            file: new FormControl(''),
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
            focalPoint,
            updateAllLocationsIndicators
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
            locations: [],
            updateAllLocationsIndicators
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
            this.projectService.update(project)
                .subscribe({
                    next: () => {
                        this.loadProject(id);
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Guardado con éxito',
                            life: 3000
                        });
                    },
                    error: error => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al guardar el proyecto',
                            detail: error.error.message,
                            life: 3000
                        });
                    }
                });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.projectService.save(project)
                .subscribe({
                    next: () => {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Guardado con éxito',
                            life: 3000
                        });
                        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
                            this.router.navigate(['/administration/partnerProjectAdministration', {projectId: id}])
                        );

                    },
                    error: error => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al guardar el proyecto',
                            detail: error.error.message,
                            life: 3000
                        });

                    }
                });
        }
    }

    cancel() {
        if (this.idProjectParam) {
            const idProject = Number(this.idProjectParam);
            this.loadProject(idProject);
        } else {
            this._location.back();
        }

    }

    saveLocationsForm() {

        if (this.formLocations.get('locationsSelected').value) {
            let locationsBefore: Canton[] = [];
            let cantonesG: Canton[] = [];
            if (this.formItem.get('originalLocations').value) {
                locationsBefore = this.formItem.get('originalLocations').value as Canton[];
            }
            if (this.formLocations.get('locationsSelected').value) {
                cantonesG = this.utilsService.sortCantones(this.formLocations.get('locationsSelected').value);
            }
            const agregatedLocation = cantonesG.filter((canton1) => !locationsBefore.find(canton2 => canton1.id === canton2.id));
            const deletedLocations = locationsBefore.filter((canton1) => !cantonesG.find(canton2 => canton1.id === canton2.id));
            if (agregatedLocation.length > 0) {
                const cantonesList = agregatedLocation.map(value => {
                    return value.description + '-' + value.provincia.description;
                }).join('<br>');
                if (this.idProjectParam) {
                    this.confirmationService.confirm({
                        message: 'Quieres agregar los cantones nuevos a todos los indicadores de producto?<br>' + cantonesList,
                        header: 'Actualización de indicadores',
                        closeOnEscape: false,
                        icon: 'pi pi-exclamation-triangle',
                        key: 'cdl',
                        accept: () => {
                            this.formItem.get('updateAllLocationsIndicators').patchValue(true);
                            this.showLocationsDialog = false;
                        },
                        reject: (type) => {
                            switch (type) {
                                case ConfirmEventType.REJECT:
                                    this.formItem.get('updateAllLocationsIndicators').patchValue(false);
                                    this.showLocationsDialog = false;
                                    break;
                                case ConfirmEventType.CANCEL:
                                    this.formItem.get('updateAllLocationsIndicators').patchValue(false);
                                    this.showLocationsDialog = false;
                                    break;
                            }
                        }
                    });
                } else {
                    this.formItem.get('updateAllLocationsIndicators').patchValue(false);
                    this.showLocationsDialog = false;
                }
            }
            if (
                (agregatedLocation && agregatedLocation.length > 0)
                ||
                (deletedLocations && deletedLocations.length > 0)
            ) {
                this.formItem.get('locations').markAsDirty();
            }
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
            this.cantonesAvailable = this.cantones;
        } else {
            const currentCantones = this.formItem.get('locations').value as Canton[];
            this.formLocations.get('locationsSelected').patchValue(this.formItem.get('locations').value);
            this.cantonesAvailable =
                this.cantones.filter((canton1) => !currentCantones.find(canton2 => canton1.id === canton2.id));
        }
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
        this._selectedColumnsPerformanceIndicators = this.colsPerformancelIndicators.filter(col => val.includes(col));
    }

    private createTables() {
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
        this.colsGeneralIndicators = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            // {field: 'commentary', header: 'Código', type: ColumnDataType.numeric},
            {field: 'indicator.description', header: 'Descripción', type: ColumnDataType.text},
            {field: 'target', header: 'Meta', type: ColumnDataType.text},
            {field: 'totalExecution', header: 'Ejecución actual', type: ColumnDataType.text},
            {
                field: 'executionPercentage',
                header: 'Porcentaje de ejecución',
                type: ColumnDataType.numeric,
                pipeRef: this.percentPipe,
                arg1: '1.2'
            },
            // {field: 'indicatorType', header: 'Estado', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
        ];
        this._selectedColumnsGeneralIndicators = this.colsGeneralIndicators.filter(value => value.field !== 'id');
        this.colsPerformancelIndicators = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {
                field: 'indicator.statement',
                header: 'Declaración Indicador',
                type: ColumnDataType.text,
                pipeRef: this.codeDescriptionPipe
            },
            {
                field: 'projectStatement',
                header: 'Declaración de Producto',
                type: ColumnDataType.text,
                pipeRef: this.codeDescriptionPipe
            },
            {field: 'projectStatement.productCode', header: 'Código Producto', type: ColumnDataType.text},
            {field: 'indicator', header: 'Descripción', type: ColumnDataType.text, pipeRef: this.indicatorPipe},
            {field: 'target', header: 'Meta', type: ColumnDataType.text},
            {field: 'totalExecution', header: 'Ejecución actual', type: ColumnDataType.text},
            {
                field: 'executionPercentage',
                header: 'Porcentaje de ejecución',
                type: ColumnDataType.numeric,
                pipeRef: this.percentPipe,
                arg1: '1.2'
            },
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
        ];
        const hiddenColumns: string[] = ['id', 'indicator.statement'];
        this._selectedColumnsPerformanceIndicators = this.colsPerformancelIndicators
            .filter(value => !hiddenColumns.includes(value.field));
    }

    updateTargets(
        indicator: IndicatorExecution
    ) {
        this.formTargets = this.fb.group({
            indicatorExecutionId: new FormControl(''),
            indicatorType: new FormControl('', Validators.required),
            // quarterGroups: this.fb.array([]),
            // anualTarget: new FormControl(''),

        });
        this.utilsService.resetForm(this.formTargets);
        this.formTargets.get('indicatorExecutionId').patchValue(indicator.id);
        this.formTargets.get('indicatorType').patchValue(indicator.indicatorType);
        if (indicator.indicatorType === EnumsIndicatorType.GENERAL) {
            this.formTargets.addControl('anualTarget', new FormControl('', Validators.required));
            this.formTargets.get('anualTarget').patchValue(indicator.target);
        } else {
            this.formTargets.addControl('quarterGroups', this.fb.array([]));

            const quarters = indicator.quarters
                .filter(value => value.state === EnumsState.ACTIVE)
                .sort((a, b) => a.order - b.order);
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
        }

        this.showTargetDialog = true;
    }

    get quarterGroups(): FormArray {
        return this.formTargets.controls.quarterGroups as FormArray;
    }

    saveTargets() {
        this.messageService.clear();
        const targetUpdateDTOWeb: TargetUpdateDTOWeb = new TargetUpdateDTOWeb();
        const indicatorType = this.formTargets.get('indicatorType').value as EnumsIndicatorType;
        targetUpdateDTOWeb.indicatorExecutionId = this.formTargets.get('indicatorExecutionId').value;
        targetUpdateDTOWeb.indicatorType = indicatorType;
        if (indicatorType === EnumsIndicatorType.GENERAL) {
            targetUpdateDTOWeb.totalTarget =
                this.formTargets.get('anualTarget').value;
        } else {
            const targetForms = this.formTargets.controls.quarterGroups.value as Array<any>;
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
                const q: Quarter = {
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
        }


        this.indicatorExecutionService.updateTargets(targetUpdateDTOWeb)
            .subscribe({
                next: () => {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Metas actualizadas correctamente',
                        life: 3000
                    });
                    const idProject = Number(this.idProjectParam);
                    this.loadProject(idProject);
                    this.showTargetDialog = false;
                },
                error: error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al actualizar las metas',
                        detail: error.error.message,
                        life: 3000
                    });
                }
            });
    }

    cancelTargets() {
        if (this.quarterGroups) {
            this.quarterGroups.patchValue([]);
        }

        this.showTargetDialog = false;
    }

    assignNewPerformanceIndicator() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formPerformanceIndicator);
        const newItem = new IndicatorExecutionAssigment();
        this.formPerformanceIndicator.patchValue(newItem);
        this.formPerformanceIndicator.get('isBorrowedStatement').patchValue(false);
        this.formPerformanceIndicator.get('projectStatement').disable();
        this.formPerformanceIndicator.get('indicator').enable();
        this.showPerformanceIndicatorDialog = true;
        const locations: CantonForList [] = this.formItem.get('locations').value;
        locations.map(value1 => {
            const canton = value1 as CantonForList;
            canton.enabled = true;
        });
        this.formPerformanceIndicator.get('locations').patchValue(locations);
        this.formPerformanceIndicator.get('keepBudget').patchValue(false);
        this.setUpAssignedBudget(this.formPerformanceIndicator.get('keepBudget').value);
    }

    exportExcelPerformancceIndicators(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumnsPerformanceIndicators,
            table.filteredValue ? table.filteredValue : this.performanceIndicators,
            'indicadores');
    }

    cancelPerformanceIndicatorDialog() {
        this.messageService.clear();
        this.showPerformanceIndicatorDialog = false;
    }

    savePerformanceIndicator() {
        this.messageService.clear();
        const {
            id,
            activityDescription,
            isBorrowedStatement,
            projectStatement,
            state,
            keepBudget,
            assignedBudget
        } = this.formPerformanceIndicator.value;
        const indicatorExecution: IndicatorExecutionAssigment = new IndicatorExecutionAssigment();
        const indicator = this.formPerformanceIndicator.get('indicator').value;

        indicatorExecution.id = id;
        indicatorExecution.project = new Project();
        indicatorExecution.project.id = this.formItem.get('id').value;
        indicatorExecution.indicator = indicator;
        indicatorExecution.activityDescription = activityDescription;
        indicatorExecution.keepBudget = keepBudget;
        indicatorExecution.assignedBudget = assignedBudget;
        if (state) {
            indicatorExecution.state = EnumsState.ACTIVE;
        } else {
            indicatorExecution.state = EnumsState.INACTIVE;
        }

        if (isBorrowedStatement) {
            indicatorExecution.projectStatement = projectStatement;
        } else {
            indicatorExecution.projectStatement = indicator.statement;
        }

        if (indicatorExecution.id) {
            // update+
            let locationEnabled = [];
            (this.formPerformanceIndicator.get('locations').value as any[])
                .forEach(value => locationEnabled.push(Object.assign({}, value)));

            locationEnabled = locationEnabled.filter(value => {
                return value.enabled;
            }).map(value => {
                delete value.enabled;
                return value as Canton;
            });
            indicatorExecution.locations = locationEnabled;
            if (indicatorExecution.locations.length < 1 && this.indicatorHasLocationDissagregation(indicatorExecution.indicator)) {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Seleccione al menos un cantón',
                    detail: 'Este indicador tiene segregación por lugar, es necesario activar al menos un cantón',
                    life: 3000
                });
                return;
            }
            this.indicatorExecutionService
                .updateAssignPerformanceIndicatoToProject(indicatorExecution)
                .subscribe({
                    next: () => {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Indicador actualizado correctamente',
                            life: 3000
                        });
                        this.loadProject(indicatorExecution.project.id);
                        this.showPerformanceIndicatorDialog = false;
                    },
                    error: error => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al agregar el indicador',
                            detail: error.error.message,
                            life: 3000
                        });
                    }
                });

        } else {
            // create
            const locationstotal: CantonForList[] = [];
            (this.formPerformanceIndicator.get('locations').value as CantonForList[])
                .forEach(value => locationstotal.push(Object.assign({}, value)));
            indicatorExecution.locations = locationstotal.filter(value => {
                return value.enabled;
            }).map(value => {
                delete value.enabled;
                return value as Canton;
            });
            if (indicatorExecution.locations.length < 1 && this.indicatorHasLocationDissagregation(indicatorExecution.indicator)) {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Seleccione al menos un cantón',
                    detail: 'Este indicador tiene segregación por lugar, es necesario activar al menos un cantón',
                    life: 3000
                });
                return;
            }
            this.indicatorExecutionService.assignPerformanceIndicatoToProject(indicatorExecution)
                .subscribe({
                    next: value => {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Indicador agregado correctamente',
                            life: 3000
                        });
                        this.indicatorExecutionService.getResumeAdministrationPerformanceIndicatorById(value)
                            .subscribe({
                                next: value1 => {
                                    this.showPerformanceIndicatorDialog = false;
                                    this.updateTargets(value1);
                                },
                                error: error => {
                                    this.messageService.add({
                                        severity: 'error',
                                        summary: 'Error al recuperar los trimestres',
                                        detail: error.error.message,
                                        life: 3000
                                    });
                                }
                            });

                    },
                    error: error => {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al agregar el indicador',
                            detail: error.error.message,
                            life: 3000
                        });
                    }
                });
        }


    }

    onChangeIsBorrowed(value: boolean) {
        if (value) {
            this.formPerformanceIndicator.get('projectStatement').enable();
            this.formPerformanceIndicator.get('projectStatement').patchValue(null);
            this.formPerformanceIndicator.get('projectStatement').setValidators([Validators.required]);
        } else {
            let projectStatement: Statement = null;
            if (this.formPerformanceIndicator.get('indicator').value) {
                projectStatement = this.statementsOptions
                    .map(item => {
                        return item.value as Statement;
                    })
                    .filter(statement => {
                        return statement.id === this.formPerformanceIndicator.get('indicator').value.statement.id;
                    }).pop();
            }
            this.formPerformanceIndicator.get('projectStatement').patchValue(projectStatement);
            this.formPerformanceIndicator.get('projectStatement').disable();
            this.formPerformanceIndicator.get('projectStatement').clearValidators();
        }
        this.formPerformanceIndicator.get('projectStatement').updateValueAndValidity();
        this.ref.detectChanges();
    }

    onChangePerformanceIndicator(indicator: Indicator) {
        let projectStatement: Statement = null;
        if (indicator) {
            projectStatement = this.statementsOptions
                .map(item => {
                    return item.value as Statement;
                })
                .filter(statement => {
                    return statement.id === this.formPerformanceIndicator.get('indicator').value.statement.id;
                }).pop();

        }
        this.formPerformanceIndicator.get('isBorrowedStatement').patchValue(false);
        this.formPerformanceIndicator.get('projectStatement').disable();
        this.formPerformanceIndicator.get('projectStatement').patchValue(projectStatement);
    }

    updatePerformanceIndicator(indicatorExecution: IndicatorExecution) {
        this.messageService.clear();
        this.utilsService.resetForm(this.formPerformanceIndicator);

        const editinItem = new IndicatorExecutionAssigment();
        editinItem.id = indicatorExecution.id;
        editinItem.project = new Project();
        editinItem.project.id = this.formItem.get('id').value;
        editinItem.indicator = this.indicatorOptions
            .map(value => {
                return value.value;
            }).filter(value => value.id === indicatorExecution.indicator.id).pop();
        editinItem.keepBudget = indicatorExecution.keepBudget;
        editinItem.assignedBudget = indicatorExecution.assignedBudget;
        if (indicatorExecution.projectStatement) {
            editinItem.projectStatement =
                this.statementsOptions
                    .map(value => {
                        return value.value as Statement;
                    })
                    .filter(statement => {
                        return statement.id === indicatorExecution.projectStatement.id;
                    }).pop();
        } else {
            editinItem.projectStatement =
                this.statementsOptions
                    .map(value => {
                        return value.value as Statement;
                    })
                    .filter(statement => {
                        return statement.id === indicatorExecution.indicator.statement.id;
                    }).pop();
        }
        editinItem.activityDescription = indicatorExecution.activityDescription;
        /**********locations**********/
        const projectCantons = [];
        // clone array
        (this.formItem.get('originalLocations').value as Canton[]).forEach(value => projectCantons.push(Object.assign({}, value)));
        projectCantons.forEach(value => {
            const r = indicatorExecution.locations.filter(value1 => {
                return value1.id === value.id;
            });
            value.enabled = r && r.length > 0;
        });
        editinItem.locations = projectCantons;
        /**********locations**********/

        this.formPerformanceIndicator.patchValue(editinItem);
        this.formPerformanceIndicator.get('indicator').disable();
        if (editinItem.projectStatement.id === editinItem.indicator.statement.id) {
            this.formPerformanceIndicator.get('isBorrowedStatement').patchValue(false);
            this.formPerformanceIndicator.get('projectStatement').disable();
        } else {
            this.formPerformanceIndicator.get('isBorrowedStatement').patchValue(true);
            this.formPerformanceIndicator.get('projectStatement').enable();
        }
        this.showPerformanceIndicatorDialog = true;
        this.setUpAssignedBudget(this.formPerformanceIndicator.get('keepBudget').value);
    }

    indicatorHasLocationDissagregation(indicator: Indicator): boolean {
        if (indicator) {
            const indicatorTotal: Indicator = this.indicatorOptions.map(value => {
                return value.value as Indicator;
            }).filter(value => {
                return indicator.id === value.id;
            }).pop();
            if (!indicatorTotal) {
                return false;
            }
            return indicatorTotal.dissagregationsAssignationToIndicator
                .filter(value => {
                    return value.state === EnumsState.ACTIVE;
                })
                .filter(value => {
                    const dissagregationTypeE = DissagregationType[value.dissagregationType];
                    return this.utilsService.isLocationDissagregation(dissagregationTypeE);
                }).length > 0;
        } else {
            return false;
        }
    }

    private registerFilters() {
        this.filterService.register('indicatorFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['code', 'description', 'category'], filter);
        });
        this.filterService.register('statementFilter', (value, filter): boolean => {
            return this.filterUtilsService.generalFilter(value, ['code', 'description'], filter);
        });
    }

    onKeepBudgetChange(event: any) {
        if (event.checked) {
            this.setUpAssignedBudget(true);
        } else {
            this.setUpAssignedBudget(false);
        }
    }

    setUpAssignedBudget(active: boolean) {
        if (active) {
            this.formPerformanceIndicator.get('assignedBudget').setValidators(Validators.required);
            this.formPerformanceIndicator.get('assignedBudget').enable();
        } else {
            this.formPerformanceIndicator.get('assignedBudget').clearValidators();
            this.formPerformanceIndicator.get('assignedBudget').disable();
            this.formPerformanceIndicator.get('assignedBudget').patchValue(null);
        }
    }

    updatePerformanceIndicatorsTargets() {
        this.showTargetsPerformanceIndicatorUpdateDialog = true;
        const ieTemporal = this.performanceIndicators.pop();
        const quarters = ieTemporal.quarters.sort((a, b) => a.order - b.order);

        this.quarterOrders = quarters.map(value => value.order);
        this.quarterTitles = quarters.map(value => value.quarter + '-' + value.year);
        this.performanceIndicators.forEach(ie => ie.quarters.sort((a, b) => a.order - b.order));

    }

    getTotalTarget(ie: IndicatorExecution): number {
        return ie.quarters.reduce<number>(
            (previousValue, currentValue) =>
                previousValue + (currentValue.target ? Number(currentValue.target) : 0), 0);
    }

    onTargetMasiveUpdate(quarter: Quarter) {
        this.quartersToUpdate.push(quarter);
    }

    saveTargetsUpdate() {
        this.showTargetsPerformanceIndicatorUpdateDialog = false;
        if (this.quartersToUpdate.length > 0) {
            this.indicatorExecutionService.quartersTargetUpdate(this.quartersToUpdate)
                .subscribe({
                    next: () => {
                        this.quartersToUpdate = [];
                        this.showTargetsPerformanceIndicatorUpdateDialog = false;
                        this.loadProject(Number(this.idProjectParam));
                    },
                    error: error => {

                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al actualizar las metas',
                            detail: error.error.message,
                            life: 3000
                        });
                        this.quartersToUpdate = [];
                    }
                });
            this.showTargetsPerformanceIndicatorUpdateDialog = false;
            this.loadProject(Number(this.idProjectParam));
        }


    }

    cancelTargetsUpdate() {
        this.quartersToUpdate = [];
        this.showTargetsPerformanceIndicatorUpdateDialog = false;
        this.loadProject(Number(this.idProjectParam));
    }

    initiateCatalogImport() {
        this.importForm.reset();
        this.showDialogImport = true;

    }

    cancelImportDialog() {
        this.showDialogImport = false;
        this.importForm.reset();
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
                this.loadProject(Number(this.idProjectParam));
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
        const period: Period = this.formItem.get('period').value
        this.indicatorExecutionService.getProjectIndicatorsImportTemplate(period.id).subscribe({
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
}
