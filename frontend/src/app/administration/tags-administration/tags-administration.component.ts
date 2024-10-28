import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ConfirmationService, FilterService, MessageService, SelectItem } from 'primeng/api';
import { Table } from 'primeng/table';
import { EnumsService } from 'src/app/services/enums.service';
import { FilterUtilsService } from 'src/app/services/filter-utils.service';
import { PeriodService } from 'src/app/services/period.service';
import { TagService } from 'src/app/services/tag.service';
import { UtilsService } from 'src/app/services/utils.service';
import { Period, PeriodTagAsignation, Tag } from 'src/app/shared/model/OsmosysModel';
import { ColumnDataType, ColumnTable, EnumsState, EnumsType } from 'src/app/shared/model/UtilsModel';
import { TagPeriodTagAsignationsListPipe } from 'src/app/shared/pipes/tag-period-tag-asignations-list.pipe';

@Component({
  selector: 'app-tags-administration',
  templateUrl: './tags-administration.component.html',
  styleUrls: ['./tags-administration.component.scss']
})
export class TagsAdministrationComponent implements OnInit {
    items: Tag[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    states: SelectItem[];
    _selectedColumns: ColumnTable[];
    periodsItems: SelectItem<Period>[];
    inputNameValue: string = '';

  constructor(
      private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private tagService: TagService,
        private periodService: PeriodService,
        private tagPeriodTagAsignationListPipe: TagPeriodTagAsignationsListPipe,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private cd: ChangeDetectorRef
  
      ) { }

  ngOnInit(): void {
    this.loadItems();
    this.loadPeriods();
    //console.log(this.periodsItems)
    this.cols = [
        {field: 'id', header: 'id', type: ColumnDataType.numeric},
        {field: 'name', header: 'Nombre', type: ColumnDataType.text},
        {field: 'description', header: 'Descripción', type: ColumnDataType.text},
        {
          field: 'periodTagAsignations',
          header: 'Periodos',
          type: ColumnDataType.numeric,
          pipeRef: this.tagPeriodTagAsignationListPipe
        },        
      {field: 'state', header: 'Estado', type: ColumnDataType.text},
    ];
    this._selectedColumns = this.cols.filter(value => value.field !== 'id');

    this.registerFilters();
    this.formItem = this.fb.group({
      id: new FormControl(''),
      name: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      periods: new FormControl('', Validators.required),
      periodTagAsignations: new FormControl(''),
      state: new FormControl('', Validators.required),
  });
  
  this.enumsService.getByType(EnumsType.State).subscribe(value => {
      this.states = value;
  });
  }

  private registerFilters() {
    
    this.filterService.register('periodTagAsignationsFilter', (value, filter): boolean => {
        return this.filterUtilsService.periodTagAsignationsFilter(value, filter);
    });
}

  private loadItems() {
    this.tagService.getAll().subscribe({
        next: value => this.items = value,
        error: err => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los tags',
                detail: err.error.message,
                life: 3000
            });
        }
    });
}

private loadPeriods() {
  this.periodService.getByState(EnumsState.ACTIVE)
      .subscribe({
          next: value => {
              this.periodsItems = value.map(value1 => {
                  const selectItem: SelectItem = {
                      label: value1.year.toString(),
                      value: value1
                  };
                  return selectItem;
              });
          },
          error: err => {
              this.messageService.add({
                  severity: 'error',
                  summary: 'Error al cargar los periodos',
                  detail: err.error.message,
                  life: 3000
              });
          }
      });
}

exportExcel(table: Table) {
  this.utilsService.exportTableAsExcel(this._selectedColumns,
      table.filteredValue ? table.filteredValue : this.items,
      'tags');
}

createItem() {
  this.messageService.clear();
  this.utilsService.resetForm(this.formItem);
  this.submitted = false;
  this.showDialog = true;
  const newItem = new Tag();
  this.formItem.patchValue(newItem);
}

editItem(tag: Tag) {
  
  this.utilsService.resetForm(this.formItem);
  this.submitted = false;
  this.showDialog = true;
  const periodTagAsignations: PeriodTagAsignation[] =
            tag.periodTagAsignations;
        const assignedPeriods: Period[] = tag.periodTagAsignations.filter(value => {
            return value.state === EnumsState.ACTIVE;
        }).map(value => {
            return value.period;
        });


  this.formItem.patchValue(tag);
  this.formItem.get('periodTagAsignations').patchValue(periodTagAsignations);
  this.formItem.get('periods').patchValue(assignedPeriods);
  this.cd.detectChanges();
}

saveItem() {
  this.messageService.clear();
  let {
      id,
      name,
      description,
      periods,
      periodTagAsignations,
      state,
  }
      = this.formItem.value;
    name=this.removeAccents(name);
  const tag: Tag = {
      id,
      name,
      description,
      periodTagAsignations:[],
      state,
  };
  const periodsCasted = periods as Period[];
        let periodTagAsignationsCasted = periodTagAsignations as PeriodTagAsignation[];

        if (periodTagAsignations) {
          periodTagAsignationsCasted.forEach(value => value.state = EnumsState.INACTIVE);
        } else {
          periodTagAsignationsCasted = [];
        }
        for (const period of periodsCasted) {
            const periodTagAsignationF = periodTagAsignationsCasted.filter(value => {
                return value.period.id === period.id;
            }).pop();
            if (periodTagAsignationF) {
              periodTagAsignationF.state = EnumsState.ACTIVE;
            } else {
                const assignation: PeriodTagAsignation = new PeriodTagAsignation();
                assignation.period = period;
                assignation.state = EnumsState.ACTIVE;
                periodTagAsignationsCasted.push(assignation);
            }
        }
        tag.periodTagAsignations = periodTagAsignationsCasted;
  if (tag.id) {
      // tslint:disable-next-line:no-shadowed-variable
      this.tagService.update(tag)
          .subscribe({
              next: () => {
                  this.cancelDialog();
                  this.loadItems();
              },
              error: err => {
                  this.messageService.add({
                      severity: 'error',
                      summary: 'Error al actualizar los tags',
                      detail: err.error.message,
                      life: 3000
                  });
              }
          });
  } else {
      // tslint:disable-next-line:no-shadowed-variable
      this.tagService.save(tag)
          .subscribe({
              next: () => {
                  this.cancelDialog();
                  this.loadItems();
              },
              error: err => {
                  this.messageService.add({
                      severity: 'error',
                      summary: 'Error al guardar los tags',
                      detail: err.error.message,
                      life: 3000
                  });
                  console.log(err.error.message)
              }
          });
  }

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
  this._selectedColumns = this.cols.filter(col => val.includes(col));
}

toUpperCase() {
  this.inputNameValue = this.inputNameValue.toUpperCase();
}

removeAccents(texto: string): string {
  const accents: { [key: string]: string } = {
    'á': 'a', 'é': 'e', 'í': 'i', 'ó': 'o', 'ú': 'u',
    'Á': 'A', 'É': 'E', 'Í': 'I', 'Ó': 'O', 'Ú': 'U',
    'ñ': 'n', 'Ñ': 'N'
  };

  return texto.split('').map(char => accents[char] || char).join('');
}





}
