import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Period} from '../../shared/model/OsmosysModel';
import {PeriodService} from '../../shared/services/period.service';
import {MessageService} from 'primeng/api';
import {ColumnTable} from '../../shared/model/UtilsModel';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-partner-project-list-administration',
    templateUrl: './partner-project-list-administration.component.html',
    styleUrls: ['./partner-project-list-administration.component.scss']
})
export class PartnerProjectListAdministrationComponent implements OnInit {

    periodForm: FormGroup;
    periods: Period[];
    cols: ColumnTable[];
    items: any[];

    constructor(
        private fb: FormBuilder,
        private periodService: PeriodService,
        private messageService: MessageService,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private router: Router) {
    }

    ngOnInit(): void {
        this.createForms();
        this.loadPeriods();
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
        console.log(periodId);
    }

    private createForms() {
        this.periodForm = this.fb.group({
            selectedPeriod: new FormControl('')
        });
    }


    onPeriodChange(period: Period) {
        this.loadProjects(period.id);
    }

    exportExcel() {
        import('xlsx').then(xlsx => {
            const headers = this.cols.map(value => value.header);
            console.log(this.items);
            const itemsRenamed = this.utilsService.renameKeys(this.items, this.cols);
            console.log(itemsRenamed);
            const worksheet = xlsx.utils.json_to_sheet(itemsRenamed);
            const workbook = {Sheets: {data: worksheet}, SheetNames: ['data']};

            const excelBuffer: any = xlsx.write(workbook, {bookType: 'xlsx', type: 'array'});
            this.utilsService.saveAsExcelFile(excelBuffer, 'areas');
        });
    }

    createItem() {
        const period = this.periodForm.get('selectedPeriod').value;
        // this.router.navigate(['/administration/partnerProjectAdministration', {periodId: period.id}]);
        // this.router.navigateByUrl(['/administration/partnerProjectAdministration', {state: period}]);
        this.router.navigateByUrl('/administration/partnerProjectAdministration', {state: {period, project: null}});

    }
}
