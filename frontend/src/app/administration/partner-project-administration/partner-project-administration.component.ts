import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {FilterService, MessageService, SelectItem} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {UserService} from '../../shared/services/user.service';
import {FilterUtilsService} from '../../shared/services/filter-utils.service';
import {Location} from '@angular/common';
import {Canton, Period, Project} from '../../shared/model/OsmosysModel';
import {OrganizationService} from '../../shared/services/organization.service';
import {CantonService} from '../../shared/services/canton.service';
import {ColumnDataType, ColumnTable, EnumsState, EnumsType} from '../../shared/model/UtilsModel';
import {EnumsService} from '../../shared/services/enums.service';
import {OfficeOrganizationPipe} from '../../shared/pipes/officeOrganization.pipe';
import {PeriodService} from '../../shared/services/period.service';
import {ProjectService} from '../../shared/services/project.service';

@Component({
    selector: 'app-partner-project-administration',
    templateUrl: './partner-project-administration.component.html',
    styleUrls: ['./partner-project-administration.component.scss']
})
export class PartnerProjectAdministrationComponent implements OnInit {
    public periods: Period[];
    public cantones: Canton[];

    public organizations: SelectItem[];
    public states: SelectItem[];
    public formItem: FormGroup;
    public formLocations: FormGroup;
    public idProjectParam;
    public idPeriodParam;
    public showLocationMenu = false;
    cols: ColumnTable[];
    showLocationsDialog = false;

    constructor(
        private route: ActivatedRoute,
        private activatedRoute: ActivatedRoute,
        private location: Location,
        private fb: FormBuilder,
        private messageService: MessageService,
        public utilsService: UtilsService,
        private userService: UserService,
        private enumsService: EnumsService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private organizationService: OrganizationService,
        private cantonService: CantonService,
        private periodService: PeriodService,
        private projectService: ProjectService,
        public officeOrganizationPipe: OfficeOrganizationPipe
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
        this.createForms();
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
                locations
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
                locations
            });
            this.periods = [];
            this.periods.push(period);
            console.log(this.formItem);
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar el proyecto',
                detail: error.error.message,
                life: 3000
            });
        });
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
            locations: new FormControl('')
        });

        this.formLocations = this.fb.group({
            locationsSelected: new FormControl('')
        });


    }

    saveItem() {

        console.log(this.formItem);

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
            locations
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

        console.log(project);

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


        console.log(this.formLocations.get('locationsSelected').value);
        console.log(this.formLocations.value);
    }

    cancelDialogLocations() {
        this.showLocationsDialog = false;

        console.log(this.formLocations.value);
        console.log(this.formLocations);
    }

    editLocations() {
        this.showLocationsDialog = true;
        if (!this.formItem.get('locations').value) {
            console.log('funca');
            this.formLocations.get('locationsSelected').patchValue([]);
        } else {
            this.formLocations.get('locationsSelected').patchValue(this.formItem.get('locations').value);
        }
        console.log(this.formLocations.get('locationsSelected').value);
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
}
