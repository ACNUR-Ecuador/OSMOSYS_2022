import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { el } from '@fullcalendar/core/internal-common';
import { t } from '@fullcalendar/resource/internal-common';
import { log } from 'console';
import { ConfirmationService, MessageService, SelectItem } from 'primeng/api';
import { Table } from 'primeng/table';
import { AuditService } from 'src/app/services/audit.service';
import { EnumsService } from 'src/app/services/enums.service';
import { UtilsService } from 'src/app/services/utils.service';
import { Audit } from 'src/app/shared/model/OsmosysModel';
import { ColumnDataType, ColumnTable, EnumsType } from 'src/app/shared/model/UtilsModel';

@Component({
  selector: 'app-audit-administration',
  templateUrl: './audit-administration.component.html',
  styleUrls: ['./audit-administration.component.scss']
})
export class AuditAdministrationComponent implements OnInit {
  items: Audit[];
  cols: ColumnTable[];
  showDialog = false;
  formItem: FormGroup;
  auditActions: SelectItem[];
  states: SelectItem[];
  oldDataJson: any=[];
  newDataJson: any=[];
  auditTableData: any[] = [];
  filteredAuditTableData: any[] = [];
  isFiltered: boolean = false;
  tables:string[];
  selectedTable:string;
  _selectedColumns: ColumnTable[];
  showAuditTable=false;
  dialogVisible: boolean = false;
  dialogData: any[] = [];
  dialogTitle: string = '';
  specialProperties: string[] = ['Puntos Focales', 'Lugares de Ejecución', 'Indicadores de Ejecución'];

  constructor(private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private fb: FormBuilder,
    public utilsService: UtilsService,
    private enumsService: EnumsService,
    private auditService: AuditService) { }

  ngOnInit(): void {
    this.tables=["Proyecto", "Reporte", "Bloqueo de Mes Indicador", "Bloqueo de Mes Masivo"]
    this.selectedTable=this.tables[0]
    this.loadItems(this.selectedTable);
    this.cols = [
      { field: 'entity', header: 'Tabla', type: ColumnDataType.text },
      { field: 'projectCode', header: 'Código de Proyecto', type: ColumnDataType.text },
      { field: 'indicatorCode', header: 'Código de Indicador', type: ColumnDataType.text },
      { field: 'action', header: 'Acción', type: ColumnDataType.text },
      { field: 'responsibleUser.name', header: 'Usuario', type: ColumnDataType.text },
      { field: 'changeDate', header: 'Fecha de cambio', type: ColumnDataType.date },
      
    ];
    this._selectedColumns = this.cols.filter(value => value.field !== 'indicatorCode' );
    this.enumsService.getByType(EnumsType.AuditAction).subscribe(value => {
      this.auditActions = value;
  });
  this.enumsService.getByType(EnumsType.State).subscribe(value => {
      this.states = value;
  });
  }

  private loadItems(tableName:string) {
    this.auditService.getAuditsByTableName(tableName).subscribe({
      next: value => {
        const listItems = value;
        //ordeno las listas en forma descendente por la fecha
        this.items= [...listItems].sort((a, b) => {
        //@ts-ignore
        const dateA = new Date(a.changeDate.year, a.changeDate.monthValue - 1, a.changeDate.dayOfMonth, a.changeDate.hour, a.changeDate.minute, a.changeDate.second, a.changeDate.nano / 1000000);
        //@ts-ignore
        const dateB = new Date(b.changeDate.year, b.changeDate.monthValue - 1, b.changeDate.dayOfMonth, b.changeDate.hour, b.changeDate.minute, b.changeDate.second, b.changeDate.nano / 1000000);
        return dateB.getTime() - dateA.getTime();
      });
     
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

  convertDatetoString(changeDate:Date){
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
    const listItems = this.items.map(item => ({
      ...item,
      changeDate: this.convertDatetoString(item.changeDate)
    }));
    const filterItems=table.filteredValue.map(item => ({
      ...item,
      changeDate: this.convertDatetoString(item.changeDate)
    }));
    this.utilsService.exportTableAsExcel(this._selectedColumns,
        table.filteredValue ? filterItems : listItems,
        'audits');
}

cancelDialog() {
  this.showDialog = false;
  this.isFiltered=false;
  this.showAuditTable=false;
}

changesView(audit:Audit){
  if(audit.oldData !== ""){
    this.oldDataJson = JSON.parse(audit.oldData);
  }else{
    this.oldDataJson=[]
  }
    this.newDataJson = JSON.parse(audit.newData);
   
  if(audit.entity==="Reporte"){
    this.showAuditTable=true;
    this.prepareReportTableData();
  }if(audit.entity==="Bloqueo de Mes Masivo" || audit.entity==="Bloqueo de Mes Indicador"){
    this.showAuditTable=true;
    this.prepareMonthBlockTableData();
  }if(audit.entity==="Proyecto"){
    this.showAuditTable=true;
    this.prepareProjectAuditTableData();

  }
  
}

prepareProjectAuditTableData() {
  
  this.auditTableData=[]
  const oldData: any = {};
  const newData: any = {};
  const tableData=[];
  this.oldDataJson.forEach(item=>{
    oldData[item.label]=item.value
  })
  this.newDataJson.forEach(item=>{
    newData[item.label]=item.value
  })
  const allKeys = new Set([...Object.keys(oldData), ...Object.keys(newData)]);
  
  allKeys.forEach(key => {
    tableData.push({
      property: key,
      oldValue: oldData[key] || 'N/A',
      newValue: newData[key] || 'N/A'
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
      if(item[key]!=null || this.newDataJson[index][key]!=null ){
        tableData.push({
          property: formattedKey,
          oldValue: item[key] || 'N/A',  
          newValue: this.newDataJson[index][key] || 'N/A'  
        });
      }else{
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
    // Cuando desmarcamos el filtro, restauramos los datos originales (haciendo una copia de seguridad)
    this.filteredAuditTableData = [...this.auditTableData];
  }
}

onTableAuditChange(entity:any){
  this.selectedTable=entity
  this.loadItems(entity);
  if(this.selectedTable!=="Proyecto" && this.selectedTable!=="Bloqueo de Mes Masivo"){
    this._selectedColumns = this.cols;
  }else{
    this._selectedColumns = this.cols.filter(value => value.field !== 'indicatorCode' );
  }

}

showProjectListDetails(property: string, oldValue: string, newValue: string) {
  this.dialogData = [];
  this.dialogTitle = property;
  
  // Parse de los valores antiguos y nuevos
  let oldValueJson = oldValue !== 'N/A' ? JSON.parse(oldValue) : [];
  const newValueJson = JSON.parse(newValue);

  // Crear un mapa de los objetos por su ID para comparación eficiente
  const oldItemsMap = new Map(oldValueJson.map(item => [item.ID, item]));
  const newItemsMap = new Map(newValueJson.map(item => [item.ID, item]));

  // Iteramos sobre los elementos en el mapa de objetos antiguos
  const allIDs = new Set([...oldItemsMap.keys(), ...newItemsMap.keys()]);

  allIDs.forEach(id => {
    const oldItem = oldItemsMap.get(id);
    const newItem = newItemsMap.get(id);
    const tableData = [];

    // Si el objeto antiguo está en la lista pero no en la nueva (eliminado)
    if (oldItem && !newItem) {
      //@ts-ignore
      for (const key in oldItem) {
        let oldValue = oldItem[key] === 'true' ? 'verdadero' : 
                        (oldItem[key] === 'false' ? 'falso' : oldItem[key]);
        let newValue = (key === 'Estado') ? 'INACTIVO' : oldValue; // Estado a INACTIVO

        tableData.push({
          property: key,
          oldValue: oldValue === "null" ? 'ninguno' : oldValue,
          newValue: newValue === "null" ? 'ninguno' : newValue
        });
      }
    }
    // Si el objeto nuevo está en la lista pero no en la antigua (nuevo objeto)
    else if (!oldItem && newItem) {
      //@ts-ignore
      for (const key in newItem) {
        let newValue = newItem[key] === 'true' ? 'verdadero' : 
                       (newItem[key] === 'false' ? 'falso' : newItem[key]);
        let oldValue =  'N/A'; // Si es nuevo, el antiguo es N/A
        
        tableData.push({
          property: key,
          oldValue: oldValue === "null" ? 'ninguno' : oldValue,
          newValue: newValue === "null" ? 'ninguno' : newValue
        });
      }
    }
    // Si el objeto está en ambas listas (cambio o actualización)
    else if (oldItem && newItem) {
      //@ts-ignore
      for (const key in newItem) {
        let oldValue = oldItem[key] === 'true' ? 'verdadero' : 
                        (oldItem[key] === 'false' ? 'falso' : oldItem[key]);
        let newValue = newItem[key] === 'true' ? 'verdadero' : 
                       (newItem[key] === 'false' ? 'falso' : newItem[key]);

        tableData.push({
          property: key,
          oldValue: oldValue === "null" ? 'ninguno' : oldValue,
          newValue: newValue === "null" ? 'ninguno' : newValue
        });
      }
    }

    // Si se generaron datos en tableData, los agregamos a dialogData
    if (tableData.length > 0) {
      this.dialogData.push({
        id: id,  
        data: tableData
      });
    }
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
