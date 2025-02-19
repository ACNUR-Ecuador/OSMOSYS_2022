import { HttpResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, Input, OnInit, Pipe, PipeTransform, ViewChild } from '@angular/core';
import {
    FormBuilder,
    FormControl,
    FormGroup,
    Validators,
} from '@angular/forms';
import {
    ConfirmationService,
    FilterService,
    MessageService,
    SelectItem,
} from 'primeng/api';
import { Table } from 'primeng/table';
import { EnumsService } from 'src/app/services/enums.service';
import { FilterUtilsService } from 'src/app/services/filter-utils.service';
import { IndicatorService } from 'src/app/services/indicator.service';
import { PeriodService } from 'src/app/services/period.service';
import { ReportsService } from 'src/app/services/reports.service';
import { PercentagePipe } from 'src/app/shared/pipes/percentage.pipe';
import { UtilsService } from 'src/app/services/utils.service';
import {
    Indicator,
    Month,
    Office,
    Period,
    Project,
    Quarter,
    QuarterPopulationTypeConfirmation,
    ResultManagerIndicator,
    ResultManagerIndicatorQuarter,
    ResultManagerIndicatorQuarterReport,
    ResultManagerQuarterImplementer,
    ResultManagerQuarterPopulationType,
    Statement,
} from 'src/app/shared/model/OsmosysModel';
import { User } from 'src/app/shared/model/User';
import {
    ColumnDataType,
    ColumnTable,
    EnumsState,
    EnumsType,
} from 'src/app/shared/model/UtilsModel';
import { ResultManagerService } from 'src/app/services/result-manager.service';
import { UserService } from 'src/app/services/user.service';
import { ResultManagerExecutionPipe } from 'src/app/shared/pipes/result-manager-execution.pipe';

@Component({
    selector: 'app-result-manager-indicator-list',
    templateUrl: './result-manager-indicator-list.component.html',
    styleUrls: ['./result-manager-indicator-list.component.scss'],
})
export class ResultManagerIndicatorListComponent implements OnInit {
    items: ResultManagerIndicator[];
    cols: ColumnTable[];
    showPopulationDialog:boolean = false;
    showImplementationDialog:boolean = false;
    private submitted = false;
    formItem: FormGroup;
    states: SelectItem[];
    _selectedColumns: ColumnTable[];
    periodsItems: SelectItem<Period>[];
    inputNameValue: string = '';
    indicators: Indicator[];
    selectedIndicators: Indicator[];
    operations: any[];
    tableData: ResultManagerQuarterPopulationType[]
    originalItems:ResultManagerIndicator[]
    confirmed:boolean;
    periodForm: FormGroup;
    periods: Period[];
    selectedQuarter:number;
    selectedIndicator:Indicator;
    implTable:any={}
    expandedRowKeys: { [key: number]: boolean } = {}; // Objeto para manejar las filas expandidas
    quarterReportForm:FormGroup

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private periodService: PeriodService,
        private indicatorService: IndicatorService,
        private filterService: FilterService,
        private filterUtilsService: FilterUtilsService,
        private cd: ChangeDetectorRef,
        private reportsService: ReportsService,
        private percentagePipe: PercentagePipe,
        private resultManagerService: ResultManagerService,
        private userService: UserService,
        private resultMangerExecutionPipe: ResultManagerExecutionPipe
    ) {}

    ngOnInit(): void {
       this.createForms()
       this.loadItems()
        this.cols = [
            { field: 'indicator.code', header: 'Código', type: ColumnDataType.text },
            {
                field: 'indicator.description',
                header: 'Indicador',
                type: ColumnDataType.text,
            },
            {
                field: 'anualTarget',
                header: 'Meta Anual',
                type: ColumnDataType.text,
            },
            {
                field: 'anualExecution',
                header: 'Ejecución Anual',
                type: ColumnDataType.text,
            },
            {
                field: '',
                header: 'Porcentaje Anual',
                pipeRef: this.resultMangerExecutionPipe,
                type: ColumnDataType.text,  
            },
            {
                field: 'confirmation',
                header: 'Confirmación',
                type: ColumnDataType.text,
            },
            {
                field: 'hasExecutions',
                header: 'Estado',
                type: ColumnDataType.text,
            },            
        ];

        this._selectedColumns = this.cols.filter(
            (value) => value.field !== 'id'
        );


        
    }

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter((col) => val.includes(col));
    } 


    createForms(){
        this.periodForm = this.fb.group({
            selectedPeriod: new FormControl('')
        });
        this.formItem = this.fb.group({
            id: new FormControl(''),
            indicator: new FormControl('', Validators.required),
            quarterYearOrder: new FormControl('', Validators.required),
            populationType: new FormControl('', [Validators.required]),
            isConfirmed: new FormControl('', Validators.required),
            period: new FormControl('',Validators.required),
         
        });
        this.quarterReportForm = this.fb.group({
            id: new FormControl(''),
            indicator: new FormControl('', Validators.required),
            quarterYearOrder: new FormControl('', Validators.required),
            allReportSumConfirmation: new FormControl(''),
            reportComment: new FormControl(''),
            newReportValue: new FormControl(''),
            period: new FormControl('',Validators.required),
         
        });
        

    }
    loadItems(){
        this.expandedRowKeys= {};
        this.loadPeriods()
    }

    loadPeriods() {
        this.periodService.getAll().subscribe(value => {
            this.periods = value;
            if (this.periods.length < 1) {
                this.messageService.add({severity: 'error', summary: 'No se encontraron años', detail: ''});
            } else {
                const currentYear = (new Date()).getFullYear();
                if (this.periods.some(e => e.year === currentYear)) {
                    this.periods.filter(p => p.year === currentYear).forEach(value1 => {
                        this.periodForm.get('selectedPeriod').patchValue(value1);
                        if (value1) {
                            this.loadResultManagerIndicators(value1.id);
                        }
                    });
                } else {
                    const smallestYear = Math.min(...this.periods.map(value1 => value1.year));
                    const smallestPeriod = this.periods.filter(value1 => {
                        return value1.year === smallestYear;
                    })[0];
                    this.periodForm.get('selectedPeriod').patchValue(smallestPeriod);
                    this.loadResultManagerIndicators(smallestPeriod.id);

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

    private loadResultManagerIndicators(periodId:number) {
        this.expandedRowKeys= {};
        const user=this.userService.getLogedUsername();
        this.resultManagerService.getAll(periodId, user.id).subscribe({
            next: (value) => {
                this.items=value
                this.originalItems=JSON.parse(JSON.stringify(this.items))
            },
            error: (err) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los indicadores del Manager de resultado',
                    detail: err.error.message,
                    life: 3000,
                });
            },
        });
    }

   

    onPeriodChange(period: Period) {
        this.loadResultManagerIndicators(period.id);
    }


    toggleRow(rowData: any) {
        if (this.expandedRowKeys[rowData.indicator.id]) {
            delete this.expandedRowKeys[rowData.indicator.id]; // Si está expandida, la colapsamos
        } else {
            this.expandedRowKeys[rowData.indicator.id] = true; // Si no está expandida, la expandimos
        }
    }
    
    
    saveItem() {
        let hasError = false;
        let errorMessage = '';
        this.tableData.forEach((item, index) => {
            const quarterPopulationTypeConfirmation: QuarterPopulationTypeConfirmation = {
                id: item.id,
                indicator: this.selectedIndicator,
                quarterYearOrder: this.selectedQuarter,
                populationType: item.populationType,
                period: this.periodForm.get('selectedPeriod').value,
                confirmed: item.confirmation
            };

            if (item.id) {
                // Actualizar
                this.resultManagerService.update(quarterPopulationTypeConfirmation).subscribe({
                    next: () => {
                        // Solo continuar con la siguiente iteración si no hubo errores
                        if (index === this.tableData.length - 1 && !hasError) {
                            this.cancelDialog();
                            //this.loadItems();
                            this.loadResultManagerIndicators(this.periodForm.get("selectedPeriod").value.id);
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Valores guardados exitosamente',
                                life: 3000
                            });
                        }
                    },
                    error: err => {
                        // En caso de error, mostrar inmediatamente el mensaje de error
                        hasError = true;
                        errorMessage = err.error.message;
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al guardar los valores',
                            detail: errorMessage,
                            life: 3000
                        });
                        // Detener las iteraciones restantes
                        return;
                    }
                });
            } else {
                // Guardar nuevo
                this.resultManagerService.save(quarterPopulationTypeConfirmation).subscribe({
                    next: () => {
                        // Solo continuar con la siguiente iteración si no hubo errores
                        if (index === this.tableData.length - 1 && !hasError) {
                            this.cancelDialog();
                            //this.loadItems();
                            this.loadResultManagerIndicators(this.periodForm.get("selectedPeriod").value.id);
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Valores guardados exitosamente',
                                life: 3000
                            });
                        }
                    },
                    error: err => {
                        // En caso de error, mostrar inmediatamente el mensaje de error
                        hasError = true;
                        errorMessage = err.error.message;
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error al guardar los valores',
                            detail: errorMessage,
                            life: 3000
                        });
                        // Detener las iteraciones restantes
                        return;
                    }
                });
            }
        });


    }

   
    cancelDialog() {
        this.showPopulationDialog = false;
        this.showImplementationDialog = false;
        this.submitted = false;
        this.items=JSON.parse(JSON.stringify(this.originalItems))
        this.selectedIndicator=null;
        this.selectedQuarter=null;
    }

     

  

    showDissPopulationView(data:ResultManagerIndicatorQuarter, indicator:Indicator){
        this.selectedQuarter=data.quarter
        this.selectedIndicator=indicator
        this.tableData = data.resultManagerQuarterPopulationType;
        this.showPopulationDialog=true
    }

    showImplementationTypeView(data: ResultManagerQuarterImplementer[]){
        this.showImplementationDialog=true;
        
        // Usamos un mapa para acumular las ejecuciones por partner y office
        let partnerExecMap = new Map();
        let officeExecMap = new Map();

        data.forEach(item => {
            const { indicatorExecution } = item;
            const project = indicatorExecution?.project;
            const office = indicatorExecution?.reportingOffice;
            const execution = item.quarterImplementerExecution;

            if (project?.id) {
                const org = project?.organization?.acronym;
                if (org) {
                    partnerExecMap.set(org, (partnerExecMap.get(org) || 0) + execution);
                }
            }

            if (office?.id) {
                const officeAcronym = office?.acronym;
                if (officeAcronym) {
                    officeExecMap.set(officeAcronym, (officeExecMap.get(officeAcronym) || 0) + execution);
                }
            }
        });

        // Convertimos los resultados acumulados en arrays
        const partnerImpl = Array.from(partnerExecMap, ([partner, execution]) => ({ partner, execution }));
        const officeImpl = Array.from(officeExecMap, ([office, execution]) => ({ office, execution }));

        this.implTable["partnerImpl"] = partnerImpl;
        this.implTable["officeImpl"] = officeImpl;

    }

    getAnualConfirmationRate(rmi:ResultManagerIndicator){
        let confirmationRate:string="0/0"
        const rmiq=rmi?.resultManagerIndicatorQuarter
        if(rmiq){
            let totalPopulationTypes:number;
            let confirmationsDone:number=0
            let fetchPopulationTypeLength=false
            rmiq.forEach(item => {
                if(!fetchPopulationTypeLength){
                    totalPopulationTypes=item.resultManagerQuarterPopulationType.length
                    fetchPopulationTypeLength=true
                }
                item.resultManagerQuarterPopulationType.forEach(element => {
                    if(element.confirmation){
                        confirmationsDone++;
                    }
                });

            });
            const totalConfirmations=totalPopulationTypes*4
            confirmationRate=`${confirmationsDone}/${totalConfirmations}`
          
        }

        let confirmationStatus:string
        const islate=rmi?.resultManagerIndicatorQuarter?.some(item=>
            this.getQuarterConfirmationRate(item).confirmationStatus==="late"
        )

        const isInProgress=rmi?.resultManagerIndicatorQuarter?.some(item=>
            this.getQuarterConfirmationRate(item).confirmationStatus==="inProgress"
              
        )

        const isCompleted=rmi?.resultManagerIndicatorQuarter?.every(item=>
            this.getQuarterConfirmationRate(item).confirmationStatus==="completed"
              
        )

        if(islate){
            confirmationStatus="late"
        }else if(isInProgress){
            confirmationStatus="inProgress"
        }else if(isCompleted){
            confirmationStatus="completed"
        }else{
            confirmationStatus="onTime"
        }

        const obj={
            confirmationRate,
            confirmationStatus
        }


        return obj
    }

    getQuarterConfirmationRate(rmiq:ResultManagerIndicatorQuarter){
        //console.log(rmqtp)
        let confirmationRate:string="0/0"
        const totalPopulationTypes:number=rmiq?.resultManagerQuarterPopulationType.length;
        let confirmationsDone:number=0
        rmiq.resultManagerQuarterPopulationType.forEach(element => {
            if(element.confirmation){
                confirmationsDone++;
            }
        });
        confirmationRate=`${confirmationsDone}/${totalPopulationTypes}`

        let confirmationStatus:string
        const isPeriodPast = new Date().getFullYear()>this.periodForm.get("selectedPeriod").value.year;
        

        if( confirmationsDone === totalPopulationTypes && totalPopulationTypes!=0){
            confirmationStatus="completed"
        }else if(rmiq.quarter<this.getCurrentQuarter() && confirmationsDone<totalPopulationTypes || isPeriodPast){
            confirmationStatus="late"
        }else if(rmiq.quarter === this.getCurrentQuarter() && confirmationsDone>0){
            confirmationStatus="inProgress"
        }else{
            confirmationStatus="onTime"
        }

        const obj={
            confirmationRate,
            confirmationStatus
        }

        return obj
    }

    getCurrentQuarter(): number {
        const currentMonth = new Date().getMonth();
        let currentQuarter:number;
        if (currentMonth >= 0 && currentMonth <= 2) {
          currentQuarter = 1;  // Primer trimestre: Enero, Febrero, Marzo
        } else if (currentMonth >= 3 && currentMonth <= 5) {
          currentQuarter = 2;  // Segundo trimestre: Abril, Mayo, Junio
        } else if (currentMonth >= 6 && currentMonth <= 8) {
          currentQuarter = 3;  // Tercer trimestre: Julio, Agosto, Septiembre
        } else {
          currentQuarter = 4;  // Cuarto trimestre: Octubre, Noviembre, Diciembre
        }

        return currentQuarter;

      }
      saveQuarterReport(quarterData:ResultManagerIndicatorQuarter,indicator:Indicator){
        const indicatorQuarterReport: ResultManagerIndicatorQuarterReport = {
            id: quarterData.id,
            indicator: indicator,
            quarterYearOrder: quarterData.quarter,
            allReportSumConfirmation: quarterData.allReportSumConfirmation,
            reportComment: quarterData.reportComment,
            newReportValue: quarterData.newReportValue,
            period: this.periodForm.get('selectedPeriod').value,
        };

        if (quarterData.id) {
            // Actualizar
            this.resultManagerService.updateIndicatorQuarterReport(indicatorQuarterReport).subscribe({
                next: () => {
                        //this.loadItems();
                        this.loadResultManagerIndicators(this.periodForm.get("selectedPeriod").value.id);
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Valores guardados exitosamente',
                            life: 3000
                        });
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar los valores',
                        detail: err.error.message,
                        life: 3000
                    });
                   
                }
            });
        } else {
            // Guardar nuevo
            this.resultManagerService.saveIndicatorQuarterReport(indicatorQuarterReport).subscribe({
                next: () => {
                        //this.loadItems();
                        this.loadResultManagerIndicators(this.periodForm.get("selectedPeriod").value.id);
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Valores guardados exitosamente',
                            life: 3000
                        });
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar los valores',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
        }


      }

     
    
      // Método que se llama al cerrar el OverlayPanel sin guardar
      cancelOverlayPanel() {
        this.items=JSON.parse(JSON.stringify(this.originalItems))
      }

      //validar formulario de Reporte Trimestral
      validateReportForm(quarterData:ResultManagerIndicatorQuarter,indicator:Indicator):boolean{
        let disabled:boolean=false
        if(indicator.quarterReportCalculation==="AGGREGATION_RULE"){
            if(quarterData.newReportValue===null){
                disabled=true
            }
            if(quarterData.reportComment===null || quarterData.reportComment===""){
                disabled=true
            }
        }else{
            if(quarterData.allReportSumConfirmation===null){
                disabled=true
            }
            if(quarterData.allReportSumConfirmation===true){
                quarterData.reportComment=null;
            }
            if(quarterData.allReportSumConfirmation===false && (quarterData.reportComment===null || quarterData.reportComment==="")){
                disabled=true
            }
            if(indicator.quarterReportCalculation===null){
                disabled=true;
            }
        }
            return disabled
      }

   

    

   
}
