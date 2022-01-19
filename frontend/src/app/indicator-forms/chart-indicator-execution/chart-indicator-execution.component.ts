import {Component, Input, OnInit} from '@angular/core';
import {EnumsState} from '../../shared/model/UtilsModel';
import * as ChartDataLabels from 'chartjs-plugin-datalabels';
import {IndicatorExecutionResumeWeb, Month} from '../../shared/model/OsmosysModel';

@Component({
    selector: 'app-chart-indicator-execution',
    templateUrl: './chart-indicator-execution.component.html',
    styleUrls: ['./chart-indicator-execution.component.scss']
})
export class ChartIndicatorExecutionComponent implements OnInit {
    @Input()
    indicatorExecution: IndicatorExecutionResumeWeb;
    @Input()
    month: Month;

    titleQuarter: string;
    chartDataQuarter: any;
    chartDataTrimestal: any;
    chartDataTotal: any;
    chartDataOptions: any;
    chartDataOptionsQuarter: any;
    plugin = ChartDataLabels;


    constructor() {
    }

    ngOnInit(): void {
        this.prepareChartData();
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

                    this.titleQuarter = 'Ejecución mensual para el trimestre ' + quarter.quarter + '-' + quarter.year;

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
        this.chartDataOptionsQuarter = {
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
                    display: false
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
