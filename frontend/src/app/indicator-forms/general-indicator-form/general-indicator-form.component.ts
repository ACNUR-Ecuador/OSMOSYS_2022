import {Component, OnInit} from '@angular/core';
import {IndicatorExecutionResumeWeb, IndicatorValue, Month, MonthValues} from '../../shared/model/OsmosysModel';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {MonthService} from '../../shared/services/month.service';
import {MessageService} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {IndicatorExecutionService} from '../../shared/services/indicator-execution.service';
import {DissagregationType, EnumsState} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import * as ChartDataLabels from 'chartjs-plugin-datalabels';

@Component({
    selector: 'app-general-indicator-form',
    templateUrl: './general-indicator-form.component.html',
    styleUrls: ['./general-indicator-form.component.scss']
})
export class GeneralIndicatorFormComponent implements OnInit {
    indicatorExecution: IndicatorExecutionResumeWeb;
    monthId: number;
    monthValues: MonthValues;
    month: Month;
    monthValuesMap: Map<string, IndicatorValue[]>;
    formItem: FormGroup;


    render = false;
    showErrorResume = false;
    totalsValidation: Map<string, number> = null;

    oneDimentionDissagregations: DissagregationType[] = [];
    twoDimentionDissagregations: DissagregationType[] = [];
    noDimentionDissagregations: DissagregationType[] = [];

    chartDataQuarter: any;
    chartDataTrimestal: any;
    chartDataTotal: any;
    chartDataOptions: any;
    plugin = ChartDataLabels;

    constructor(public ref: DynamicDialogRef,
                public config: DynamicDialogConfig,
                public indicatorExecutionService: IndicatorExecutionService,
                public monthService: MonthService,
                public enumsService: EnumsService,
                public utilsService: UtilsService,
                private messageService: MessageService,
                private fb: FormBuilder
    ) {
    }

    ngOnInit(): void {
        this.indicatorExecution = this.config.data.indicatorExecution; //
        this.monthId = this.config.data.monthId; //
        this.formItem = this.fb.group({
            commentary: new FormControl('', Validators.required)
        });
        this.loadMonthValues(this.monthId);
    }

    loadMonthValues(monthId: number) {
        this.monthService.getMonthIndicatorValueByMonthId(monthId).subscribe(value => {
            this.monthValues = value as MonthValues;
            this.month = value.month;
            this.monthValuesMap = value.indicatorValuesMap;
            this.formItem.get('commentary').patchValue(this.month.commentary);
            this.setDimentionsDissagregations();
            this.prepareChartData();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar los valores del mes',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    saveMonth() {
        // console.log(this.monthValues);
        this.utilsService.setZerosMonthValues(this.monthValuesMap);
        const totalsValidation = this.utilsService.validateMonth(this.monthValuesMap);
        this.monthValues.month.commentary = this.formItem.get('commentary').value;
        if (totalsValidation) {
            this.showErrorResume = true;
            this.totalsValidation = totalsValidation;
        } else {
            this.totalsValidation = null;
            this.sendMonthValue();
        }
    }

    cancel() {
        console.log(this.monthValues);
        this.ref.close({test: 2});
    }

    private sendMonthValue() {
        this.indicatorExecutionService.updateMonthValues(this.indicatorExecution.id, this.monthValues).subscribe(value => {
            this.messageService.add({severity: 'success', summary: 'Guardado con éxito', detail: ''});
            this.ref.close({test: 1});
        }, error => {
            this.messageService.add({severity: 'error', summary: 'Error al guardar los valores:', detail: error.error.message});
        });
    }

    closeErrorDialog() {
        this.showErrorResume = false;
    }

    setDimentionsDissagregations(): void {
        const totalOneDimentions = this.utilsService.getOneDimentionsDissagregationTypes();
        const totalTwoDimentions = this.utilsService.getTwoDimentionsDissagregationTypes();
        const totalNoDimentions = this.utilsService.getNoDimentionsDissagregationTypes();
        this.monthValuesMap.forEach((value, key) => {
            if (value && value.length > 0) {
                if (totalOneDimentions.indexOf(DissagregationType[key]) >= 0) {
                    this.oneDimentionDissagregations.push(DissagregationType[key]);
                } else if (totalTwoDimentions.indexOf(DissagregationType[key]) >= 0) {
                    this.twoDimentionDissagregations.push(DissagregationType[key]);
                } else if (totalNoDimentions.indexOf(DissagregationType[key]) >= 0) {
                    this.noDimentionDissagregations.push(DissagregationType[key]);
                }
            }
        });
        this.render = true;
    }

    private prepareChartData() {

        const labels: string[] = [];
        const labelsTrimestre: string[] = [];
        const dataExecutionsTrimestre: number[] = [];
        const dataTargetsTrimestral: number[] = [];
        const dataExecutionsTrimestral: number[] = [];
        const dataTargetsTotal: number[] = [];
        const dataExecutionsTotal: number[] = [];

        this.indicatorExecution.quarters
            .filter(value => {
                return value.state === EnumsState.ACTIVE;
            })
            .sort((a, b) => a.order - b.order)
            .forEach(quarter => {
                labels.push(quarter.quarter + '-' + quarter.year);
                dataTargetsTrimestral.push(quarter.target);
                dataExecutionsTrimestral.push(quarter.totalExecution);
                const months = quarter.months.filter(value => {
                    return value.state === EnumsState.ACTIVE;
                });
                if (
                    months
                        .map(value => {
                            return value.id;
                        }).includes(this.month.id)
                ) {

                    months.forEach(value => {
                        labelsTrimestre.push(value.month);
                        dataExecutionsTrimestre.push(value.totalExecution);
                    });
                }
            });
        this.chartDataOptions = {
            plugins: {
                datalabels: {
                    align: 'end',
                    anchor: 'end',
                    borderRadius: 4,
                    backgroundColor: 'teal',
                    color: 'white',
                    font: {
                        weight: 'bold'
                    }
                },
                responsive: false,
                legend: {
                    labels: {
                        color: '#495057'
                    }
                },
                tooltips: {
                    mode: 'index',
                    intersect: true
                }
            },
            scales: {
                x: {
                    ticks: {
                        color: '#495057'
                    },
                    grid: {
                        color: '#ebedef'
                    }
                },
                y: {
                    type: 'linear',
                    display: true,
                    position: 'left',
                    ticks: {
                        min: 0,
                        max: 100,
                        color: '#495057'
                    },
                    grid: {
                        color: '#ebedef'
                    }
                }
            }
        };
        this.chartDataQuarter = {
            labels: labelsTrimestre,
            datasets: [
                {
                    type: 'bar',
                    label: 'Ejecución Mensual',
                    backgroundColor: '#66BB6A',
                    data: dataExecutionsTrimestre
                }
            ]
        };
        this.chartDataTrimestal = {
            labels,
            datasets: [{
                type: 'bar',
                label: 'Metas Trimestrales',
                backgroundColor: '#42A5F5',
                data: dataTargetsTrimestral
            }, {
                type: 'bar',
                label: 'Ejecución Trimestral',
                backgroundColor: '#66BB6A',
                data: dataExecutionsTrimestral
            }
            ]
        };
        dataTargetsTotal.push(this.indicatorExecution.target);
        dataExecutionsTotal.push(this.indicatorExecution.totalExecution);
        this.chartDataTotal = {
            labels: ['Ejecución Total'],
            datasets: [{
                type: 'bar',
                label: 'Meta Total',
                backgroundColor: '#42A5F5',
                data: dataTargetsTotal
            }, {
                type: 'bar',
                label: 'Ejecución Total',
                backgroundColor: '#66BB6A',
                data: dataExecutionsTotal
            }
            ]
        };

    }

}
