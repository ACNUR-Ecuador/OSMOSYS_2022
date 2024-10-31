import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
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
  private submitted = false;
  formItem: FormGroup;
  auditActions: SelectItem[];
  states: SelectItem[];
  oldDataJson: any;
  newDataJson: any;
  auditTableData: any[] = [];

  _selectedColumns: ColumnTable[];


  constructor(private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private fb: FormBuilder,
    public utilsService: UtilsService,
    private enumsService: EnumsService,
    private auditService: AuditService) { }

  ngOnInit(): void {
    this.loadItems();
    
    this.cols = [
      { field: 'id', header: 'id', type: ColumnDataType.numeric },
      { field: 'entity', header: 'Tabla', type: ColumnDataType.text },
      { field: 'recordId', header: 'Record id', type: ColumnDataType.text },
      { field: 'action', header: 'Acción', type: ColumnDataType.text },
      { field: 'responsibleUser.name', header: 'Usuario', type: ColumnDataType.text },
      { field: 'changeDate', header: 'Fecha de cambio', type: ColumnDataType.date },
      
    ];
    this._selectedColumns = this.cols.filter(value => value.field !== 'id');
    this.enumsService.getByType(EnumsType.AuditAction).subscribe(value => {
      this.auditActions = value;
  });
  this.enumsService.getByType(EnumsType.State).subscribe(value => {
      this.states = value;
  });
  }

  private loadItems() {
    this.auditService.getAll().subscribe({
      next: value => {this.items = value
        console.log(this.items)
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
    this.utilsService.exportTableAsExcel(this._selectedColumns,
        table.filteredValue ? table.filteredValue : this.items,
        'audits');
}

cancelDialog() {
  this.showDialog = false;
  this.submitted = false;
}

changesView(audit:Audit){
  this.showDialog = true;
  this.submitted = false;
  this.oldDataJson = JSON.parse(audit.oldData);
  this.newDataJson = JSON.parse(audit.newData);
  this.prepareAuditTableData();
}

prepareAuditTableData() {
  const allKeys = new Set([...Object.keys(this.oldDataJson), ...Object.keys(this.newDataJson)]);

  allKeys.forEach(key => {
    this.auditTableData.push({
      property: key,
      oldValue: this.oldDataJson[key] || 'N/A',
      newValue: this.newDataJson[key] || 'N/A'
    });
  });
}

@Input() get selectedColumns(): any[] {
  return this._selectedColumns;
}

set selectedColumns(val: any[]) {
  // restore original order
  this._selectedColumns = this.cols.filter(col => val.includes(col));
}

}
