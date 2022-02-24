import {Component, OnInit} from '@angular/core';
import * as d3 from 'd3';
import {ExtendedFeature, ExtendedFeatureCollection} from 'd3';

@Component({
    selector: 'app-test-ploty',
    templateUrl: './test-ploty.component.html',
    styleUrls: ['./test-ploty.component.scss']
})
export class TestPlotyComponent implements OnInit {

    constructor() {
    }

    data;
    layout;
    config;

    public graph = {
        data: [
            {x: [1, 2, 3], y: [2, 6, 3], type: 'scatter', mode: 'lines+points', marker: {color: 'red'}},
            {x: [1, 2, 3], y: [2, 5, 3], type: 'bar'},
        ],
        layout: {width: 320, height: 240, title: 'A Fancy Plot'}
    };

    ngOnInit(): void {
        const url = 'https://docs.mapbox.com/mapbox-gl-js/assets/earthquakes.geojson';
        d3.json(url).then(raw => {
            const dataJson: any = raw;
            const lon = dataJson.features.map(f => f.geometry.coordinates[0]);
            const lat = dataJson.features.map(f => f.geometry.coordinates[1]);
            const z = dataJson.features.map(f => f.properties.mag);
            this.data = [
                {type: 'scattermapbox', lon: lon, lat: lat, z: z, hoverinfo: 'y'}
            ];
            this.layout = {
                mapbox: {style: 'dark', zoom: 2, center: {lon: -150, lat: 60}},
                margin: {t: 0, b: 0}
            };

            this.config = {
                mapboxAccessToken: 'pk.eyJ1Ijoic2ViYXNzdCIsImEiOiJja3hqZ2Y5MmozbjV6Mm5xaGE3NmFmbXdpIn0.DUNe9vcvZ7qC-NckuhKAMw'
            };
        });
    }

}
