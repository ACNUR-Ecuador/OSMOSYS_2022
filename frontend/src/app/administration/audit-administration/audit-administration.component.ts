import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ConfirmationService, MessageService, SelectItem } from 'primeng/api';
import { Table } from 'primeng/table';
import { AuditService } from 'src/app/services/audit.service';
import { EnumsService } from 'src/app/services/enums.service';
import { PeriodService } from 'src/app/services/period.service';
import { UtilsService } from 'src/app/services/utils.service';
import { Audit, Period } from 'src/app/shared/model/OsmosysModel';
import { ColumnDataType, ColumnTable, EnumsType } from 'src/app/shared/model/UtilsModel';

@Component({
  selector: 'app-audit-administration',
  templateUrl: './audit-administration.component.html',
  styleUrls: ['./audit-administration.component.scss']
})
export class AuditAdministrationComponent implements OnInit {
  items: Audit[];
  filteredData: Audit[];
  selectedDate: Date | null = null;
  cols: ColumnTable[];
  showDialog = false;
  formItem: FormGroup;
  auditActions: SelectItem[];
  oldDataJson: any = [];
  newDataJson: any = [];
  auditTableData: any[] = [];
  filteredAuditTableData: any[] = [];
  isFiltered: boolean = false;
  tables: SelectItem[];
  selectedTable: string;
  _selectedColumns: ColumnTable[];
  showAuditTable = false;
  dialogVisible: boolean = false;
  dialogData: any[] = [];
  dialogTitle: string = '';
  specialProperties: string[] = ['Responsables del Proyecto', 'Lugares de Ejecución', 'Indicadores de Ejecución'];
  tableDateForm: FormGroup;
  periods: Period[];
  months:any

  constructor(private messageService: MessageService,
    public utilsService: UtilsService,
    private enumsService: EnumsService,
    private auditService: AuditService,
    private periodService: PeriodService,
    private fb: FormBuilder,) { }

  ngOnInit(): void {
    this.tables = [
      {
        label:"Proyecto",
        value:"Proyecto"
      },
      {
        label:"Reporte",
        value:"Reporte"
      },
      {
        label:"Bloqueo de Mes por Indicador",
        value:"Bloqueo de Mes Indicador"
      },
      {
        label:"Bloqueo de Mes Masivo",
        value:"Bloqueo de Mes Masivo"
      }
    ]
    this.months=this.getMonths();
    this.loadPeriods();
    this.createForms();
    this.cols = [
      { field: 'entity', header: 'Auditoría', type: ColumnDataType.text },
      { field: 'projectCode', header: 'Número de Acuerdo', type: ColumnDataType.text },
      { field: 'indicatorCode', header: 'Código de Indicador', type: ColumnDataType.text },
      { field: 'action', header: 'Acción', type: ColumnDataType.text },
      { field: 'blockedMonth', header: 'Mes', type: ColumnDataType.text },
      { field: 'blockedYear', header: 'Año', type: ColumnDataType.text },
      { field: 'responsibleUser.name', header: 'Usuario', type: ColumnDataType.text },
      { field: 'changeDate', header: 'Fecha de cambio', type: ColumnDataType.date },

    ];
    this._selectedColumns = this.cols.filter(value => value.field !== 'indicatorCode' && value.field !== 'blockedMonth' && value.field !== 'blockedYear');
    this.enumsService.getByType(EnumsType.AuditAction).subscribe(value => {
      this.auditActions = value;
    });

  }


  loadPeriods() {
    this.periodService.getAll()
        .subscribe({
            next: value => {
                this.periods = value;
                if (this.periods.length < 1) {
                    this.messageService.add({severity: 'error', summary: 'No se encontraron años', detail: ''});
                }else {
                  const currentYear = (new Date()).getFullYear();
                  const currentMonth = (new Date()).getMonth()+1;
                  
                  if (this.periods.some(e => e.year === currentYear)) {
                      this.periods.filter(p => p.year === currentYear).forEach(value1 => {
                        this.tableDateForm.get('selectedTable').patchValue(this.tables[0]);
                          this.tableDateForm.get('selectedYear').patchValue(value1);

                          const currentMonthObj = this.months.find(month => month.value === currentMonth); // Buscar el mes actual en la lista
                          if (currentMonthObj) {
                              this.tableDateForm.get('selectedMonth').patchValue(currentMonthObj); // Asignar el mes al formulario
                          }
                      });
                  } else {
                      const smallestYear = Math.min(...this.periods.map(value1 => value1.year));
                      const smallestPeriod = this.periods.filter(value1 => {
                          return value1.year === smallestYear;
                      })[0];
                      this.tableDateForm.get('selectedTable').patchValue(this.tables[0]);
                      this.tableDateForm.get('selectedYear').patchValue(smallestPeriod);
                      const currentMonthObj = this.months.find(month => month.value === currentMonth); // Buscar el mes actual en la lista
                          if (currentMonthObj) {
                              this.tableDateForm.get('selectedMonth').patchValue(currentMonthObj); // Asignar el mes al formulario
                          }
                  }
                  
                  this.loadItems(this.tableDateForm.get('selectedTable').value,this.tableDateForm.get('selectedYear').value.year,this.tableDateForm.get('selectedMonth').value.value);
              }
            },
            error: error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los años',
                    detail: error.error.message,
                    life: 3000
                });
            }
        });
}

getMonths(): { value: number; label: string }[] {
  const formatter = new Intl.DateTimeFormat('es-ES', { month: 'long' }); // Cambia 'es-ES' por el idioma deseado
  return Array.from({ length: 12 }, (_, index) => {
      const date = new Date(0, index); // Crea una fecha para cada mes
      return { 
          value: index + 1, // El valor numérico del mes (1 a 12)
          label: formatter.format(date).charAt(0).toUpperCase() + formatter.format(date).slice(1) // Nombre del mes con capitalización
      };
  });
}

  private loadItems(tableName: string, year: number, month: number) {
    this.auditService.getAuditsByTableNameAndDate(tableName,year,month).subscribe({
      next: value => {
        const listItems = value;
        //ordeno las listas en forma descendente por la fecha
        this.items = [...listItems].sort((a, b) => {
          //@ts-ignore
          const dateA = new Date(a.changeDate.year, a.changeDate.monthValue - 1, a.changeDate.dayOfMonth, a.changeDate.hour, a.changeDate.minute, a.changeDate.second, a.changeDate.nano / 1000000);
          //@ts-ignore
          const dateB = new Date(b.changeDate.year, b.changeDate.monthValue - 1, b.changeDate.dayOfMonth, b.changeDate.hour, b.changeDate.minute, b.changeDate.second, b.changeDate.nano / 1000000);
          return dateB.getTime() - dateA.getTime();
        });

        this.filteredData = this.items


      },
      error: err => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error al cargar las auditorías',
          detail: err.error.message,
          life: 3000
        });
      }
    });

  }

  private createForms() {
    this.tableDateForm = this.fb.group({
                selectedTable:new FormControl(''),
                selectedYear: new FormControl(''),
                selectedMonth: new FormControl(''),
            });
    
    }



  // Método para manejar la selección de fecha y aplicar el filtro
  onDateFilterSelect(event: any, field: string): void {
    const selectedDate = event;
    if (selectedDate) {
      this.selectedDate = selectedDate;
      const filtered = this.items.filter(item => {
        const itemDate = new Date(item[field].year, item[field].monthValue - 1, item[field].dayOfMonth, item[field].hour, item[field].minute, item[field].second);
        const selectedDateOnly = new Date(selectedDate);

        return itemDate.getFullYear() === selectedDateOnly.getFullYear() &&
          itemDate.getMonth() === selectedDateOnly.getMonth() &&
          itemDate.getDate() === selectedDateOnly.getDate();
      });

      this.filteredData = filtered;

    } else {
      this.filteredData = [...this.items];
    }
  }

  clearDateFilter(field: string): void {
    this.selectedDate = null;
    this.filteredData = [...this.items];
  }

  convertDatetoString(changeDate: Date) {
    //@ts-ignore
    const date = new Date(changeDate.year, changeDate.monthValue - 1, changeDate.dayOfMonth, changeDate.hour, changeDate.minute, changeDate.second);
    const formattedDate = date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: false
    });
    return formattedDate;
  }

  exportExcel(table: Table) {
    const listItems = this.filteredData.map(item => ({
      ...item,
      changeDate: this.convertDatetoString(item.changeDate)
    }));
    let filterItems = []
    if (table.filteredValue) {
      filterItems = table.filteredValue.map(item => ({
        ...item,
        changeDate: this.convertDatetoString(item.changeDate)
      }));
    }

    this.utilsService.exportTableAsExcel(this._selectedColumns,
      table.filteredValue ? filterItems : listItems,
      'audits');
  }

  cancelDialog() {
    this.showDialog = false;
    this.isFiltered = false;
    this.showAuditTable = false;
  }

  changesView(audit: Audit) {
    if (audit.oldData !== "") {
      this.oldDataJson = JSON.parse(audit.oldData);
    } else {
      this.oldDataJson = []
    }
    this.newDataJson = JSON.parse(audit.newData);
    if (audit.entity === "Reporte") {
      this.showAuditTable = true;
      this.prepareReportTableData();
    } if (audit.entity === "Bloqueo de Mes Indicador") {
      this.showAuditTable = true;
      this.prepareMonthBlockTableData();
    } if (audit.entity === "Proyecto") {
      this.showAuditTable = true;
      this.prepareProjectAuditTableData();
    }

  }

  prepareProjectAuditTableData() {
    this.auditTableData = []
    const oldData: any = {};
    const newData: any = {};
    const tableData = [];
    this.oldDataJson.forEach(item => {
      oldData[item.label] = item.value
    })
    this.newDataJson.forEach(item => {
      newData[item.label] = item.value
    })
    const allKeys = new Set([...Object.keys(oldData), ...Object.keys(newData)]);

    allKeys.forEach(key => {
      let oldValue = oldData[key]
      let newValue = newData[key]
      if (this.specialProperties.includes(key)) {
        ({ oldValue, newValue } = this.transformProjectArrayJson(oldData[key], newData[key]));

      }
      tableData.push({
        property: key,
        oldValue: oldValue || 'N/A',
        newValue: newValue || 'N/A'
      });
    });
    this.auditTableData.push({
      id: 0,
      data: tableData
    });
    this.filteredAuditTableData = [...this.auditTableData];

  }

  prepareMonthBlockTableData() {
    this.auditTableData = [];
    this.oldDataJson.forEach((item, index) => {
      const tableData = [];

      const keys = Object.keys(item).sort();;
      keys.forEach(key => {
        const formattedKey = key.replace(/_/g, ' ');
        const oldValue = item[key] === true ? 'bloqueado' :
          (item[key] === false ? 'desbloqueado' : item[key]);
        const newValue = this.newDataJson[index][key] === true ? 'bloqueado' :
          (this.newDataJson[index][key] === false ? 'desbloqueado' : this.newDataJson[index][key]);

        tableData.push({
          property: formattedKey,
          oldValue: oldValue,
          newValue: newValue
        });
      });

      this.auditTableData.push({
        id: index,
        data: tableData
      });
    });

    this.filteredAuditTableData = [...this.auditTableData];
  }

  prepareReportTableData() {
    this.auditTableData = [];

    this.oldDataJson.forEach((item, index) => {
      const tableData = [];

      const keys = Object.keys(item).sort();;
      keys.forEach(key => {
        const formattedKey = key.replace(/_/g, ' ');
        if (item[key] != null || this.newDataJson[index][key] != null) {
          tableData.push({
            property: formattedKey,
            oldValue: item[key] || 'N/A',
            newValue: this.newDataJson[index][key] || 'N/A'
          });
        } else {
          return
        }

      });

      this.auditTableData.push({
        id: index,
        data: tableData
      });
    });

    this.filteredAuditTableData = [...this.auditTableData];
  }

  toggleFilter() {
    this.isFiltered = !this.isFiltered;

    if (this.isFiltered) {
      this.filteredAuditTableData = this.auditTableData.map(item => {
        const newItem = { ...item, data: [...item.data] };
        newItem.data = newItem.data.filter(row => row.oldValue !== row.newValue);
        return newItem;
      });

    } else {
      this.filteredAuditTableData = [...this.auditTableData];
    }
  }

  onTableAuditSubmit() {
    const {
      selectedTable,
      selectedYear,
      selectedMonth,
    }
      = this.tableDateForm.value;
    this.selectedTable = selectedTable
    this.loadItems(selectedTable,selectedYear.year,selectedMonth.value);
    if (this.selectedTable !== "Proyecto" && this.selectedTable !== "Bloqueo de Mes Masivo") {
      this._selectedColumns = this.cols.filter(value => value.field !== 'blockedMonth' && value.field !== 'blockedYear' );
    }else if(this.selectedTable === "Bloqueo de Mes Masivo"){
      this._selectedColumns = this.cols.filter(value => value.field !== 'indicatorCode' && value.field !== 'projectCode' );

    } else {
      this._selectedColumns = this.cols.filter(value => value.field !== 'indicatorCode' && value.field !== 'blockedMonth' && value.field !== 'blockedYear');
    }

  }

  transformProjectArrayJson(oldValue: string, newValue: string) {
    let oldValueJson = oldValue !== undefined ? JSON.parse(oldValue) : [];
    const newValueJson = JSON.parse(newValue);

    const oldItemsMap = new Map(oldValueJson.map(item => [item.ID, item]));
    const newItemsMap = new Map(newValueJson.map(item => [item.ID, item]));

    const allIDs = new Set([...oldItemsMap.keys(), ...newItemsMap.keys()]);

    allIDs.forEach(id => {
      let oldItem = oldItemsMap.get(id);
      let newItem = newItemsMap.get(id);

      // Si el objeto antiguo está en la lista pero no en la nueva (eliminado)
      if (oldItem && !newItem) {
        newItem = {};
        //@ts-ignore
        newItem.ID = oldItem.ID;
        //@ts-ignore
        for (const key in oldItem) {
          let oldVal = oldItem[key] === 'true' ? 'verdadero' :
            (oldItem[key] === 'false' ? 'falso' : oldItem[key]);
          let newVal = (key === 'Estado') ? 'INACTIVO' : oldVal;

          oldVal = oldVal === "null" ? 'ninguno' : oldVal;
          newVal = newVal === "null" ? 'ninguno' : newVal;

          oldItem[key] = oldVal;
          newItem[key] = newVal;
        }
        // Agregamos los objetos a sus respectivos arrays
        newValueJson.push(newItem);
      }

      // Si el objeto nuevo está en la lista pero no en la antigua (nuevo objeto)
      else if (!oldItem && newItem) {
        oldItem = {};
        //@ts-ignore
        oldItem.ID = newItem.ID;
        //@ts-ignore
        for (const key in newItem) {
          let newVal = newItem[key] === 'true' ? 'verdadero' :
            (newItem[key] === 'false' ? 'falso' : newItem[key]);
          let oldVal = 'N/A';

          newVal = newVal === "null" ? 'ninguno' : newVal;
          oldVal = oldVal === "null" ? 'ninguno' : oldVal;

          oldItem[key] = oldVal;
          newItem[key] = newVal;
        }
        // Agregamos los objetos a sus respectivos arrays
        oldValueJson.push(oldItem);
      }
      // Si el objeto está en ambas listas (cambio o actualización)
      else if (oldItem && newItem) {
        //@ts-ignore
        for (const key in newItem) {
          let oldVal = oldItem[key] === 'true' ? 'verdadero' :
            (oldItem[key] === 'false' ? 'falso' : oldItem[key]);
          let newVal = newItem[key] === 'true' ? 'verdadero' :
            (newItem[key] === 'false' ? 'falso' : newItem[key]);

          oldVal = oldVal === "null" ? 'ninguno' : oldVal;
          newVal = newVal === "null" ? 'ninguno' : newVal;

          oldItem[key] = oldVal;
          newItem[key] = newVal;
        }
      }
    });

    const sortedOldValueJson = oldValueJson.sort((a, b) => a.ID - b.ID);
    const sortedNewValueJson = newValueJson.sort((a, b) => a.ID - b.ID);

    return {
      oldValue: JSON.stringify(sortedOldValueJson),
      newValue: JSON.stringify(sortedNewValueJson)
    };
  }


  showProjectListDetails(property: string, oldValue: string, newValue: string) {
    this.dialogData = [];
    this.dialogTitle = property;

    let oldValueJson = oldValue !== undefined ? JSON.parse(oldValue) : [];
    const newValueJson = JSON.parse(newValue);

    oldValueJson.forEach((item, index) => {
      const tableData = [];

      const keys = Object.keys(item).sort();;
      keys.forEach(key => {
        const formattedKey = key.replace(/_/g, ' ');
        tableData.push({
          property: formattedKey,
          oldValue: item[key],
          newValue: newValueJson[index][key]
        });
      });

      this.dialogData.push({
        id: index,
        data: tableData
      });
    });
    this.dialogVisible = true;
  }



  @Input() get selectedColumns(): any[] {
    return this._selectedColumns;
  }

  set selectedColumns(val: any[]) {
    // restore original order
    this._selectedColumns = this.cols.filter(col => val.includes(col));
  }

}
