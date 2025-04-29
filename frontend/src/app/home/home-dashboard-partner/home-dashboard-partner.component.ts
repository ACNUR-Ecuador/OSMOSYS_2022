import {AfterViewInit,  Component, ElementRef, Input, ViewChild} from '@angular/core';
import {IndicatorExecution, Month, Period, ProjectResume, Quarter} from "../../shared/model/OsmosysModel";
import {UserService} from "../../services/user.service";
import * as d3 from 'd3';

@Component({
    selector: 'app-home-dashboard-partner',
    templateUrl: './home-dashboard-partner.component.html',
    styleUrls: ['./home-dashboard-partner.component.scss']
})
export class HomeDashboardPartnerComponent implements  AfterViewInit {
    
    @ViewChild('chart', { static: true }) private chartContainer!: ElementRef;

    

    @Input()
    periods: Period[];
    @Input()
    currentPeriod: Period;
    @Input()
    isPartner: boolean;

    renderProject = false;

    constructor(
     
        public userService: UserService,
    ) {    }

    ngAfterViewInit(): void {
        let data = [
            { name: 'January', value: 30 },
            { name: 'February', value: 80 },
            { name: 'March', value: 45 },
            { name: 'April', value: 60 },
            { name: 'May', value: 20 },
            { name: 'June', value: 90 },
            { name: 'July', value: 55 },
            { name: 'Dic', value: 55 }

          ];
          let options = {
            primaryColor: 'steelblue',      
            secondaryColor: '#555'
            };
          
        this.createChart(data, options);

     }

     private createChart(
        data: { name: string; value: number; }[],
        options?: { primaryColor?: string; secondaryColor?: string }
      ): void {
        const primaryColor = options?.primaryColor || 'steelblue';
        const secondaryColor = options?.secondaryColor || 'blue';

        const element = this.chartContainer.nativeElement;
        const margin = { top: 20, right: 20, bottom: 50, left: 40 };
        const width = 600 - margin.left - margin.right;
        const height = 300 - margin.top - margin.bottom;
    
        const svg = d3.select(element)
          .append('svg')
          .attr('width', width + margin.left + margin.right)
          .attr('height', height + margin.top + margin.bottom)
          .append('g')
          .attr('transform', `translate(${margin.left},${margin.top})`);
    
        const x = d3.scaleBand()
          .domain(data.map(d => d.name))
          .range([0, width])
          .padding(0.2);
    
        const y = d3.scaleLinear()
          .domain([0, d3.max(data, d => d.value)!])
          .range([height, 0]);
    
        svg.append('g')
          .attr('transform', `translate(0, ${height})`)
          .call(d3.axisBottom(x));
    
        svg.append('g')
          .call(d3.axisLeft(y));

        const tooltip = d3.select('body').append('div')
          .attr('class', 'tooltip')
          .style('opacity', 0)
          .style('position', 'absolute')
          .style('background-color', 'white')
          .style('border', '1px solid #ccc')
          .style('padding', '8px')
          .style('font-size', '12px')
          .style('pointer-events', 'none')
          .style('box-shadow', '0 2px 6px rgba(0,0,0,0.2)')
          .style('border-radius', '4px');
    
        svg.selectAll('.bar')
          .data(data)
          .enter()
          .append('rect')
          .attr('class', 'bar')
          .attr('x', d => x(d.name)!)
          .attr('width', x.bandwidth())
          .attr('y', d => y(d.value))
          .attr('height', d => height - y(d.value))
          .attr('fill', primaryColor)
          .on('mouseover', function(event, d) {
            d3.select(this)
              .attr('fill', secondaryColor);

            tooltip.transition()
              .duration(200)
              .style('opacity', 0.9);
            tooltip.html(`<strong>${d.name}</strong><br/>Value: ${d.value}`)
              .style('left', (event.pageX + 10) + 'px')
              .style('top', (event.pageY - 28) + 'px');
          })
          .on('mouseout', function(event, d) {
            d3.select(this)
              .attr('fill', primaryColor);

            tooltip.transition()
              .duration(300)
              .style('opacity', 0);
          });

        svg.selectAll('.label')
          .data(data)
          .enter()
          .append('text')
          .attr('class', 'label')
          .attr('x', d => x(d.name)! + x.bandwidth() / 2)
          .attr('y', d => y(d.value) - 5)
          .attr('text-anchor', 'middle')
          .text(d => d.value);
      }
}
