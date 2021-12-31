import {Component, Input, OnInit} from '@angular/core';
import {DissagregationType, EnumsType} from '../../../shared/model/UtilsModel';
import {IndicatorValue} from '../../../shared/model/OsmosysModel';
import {EnumsService} from '../../../shared/services/enums.service';
import {MessageService} from 'primeng/api';
import {UtilsService} from '../../../shared/services/utils.service';

@Component({
    selector: 'app-dissagregation-two-integer-dimentions',
    templateUrl: './dissagregation-two-integer-dimentions.component.html',
    styleUrls: ['./dissagregation-two-integer-dimentions.component.scss']
})
export class DissagregationTwoIntegerDimentionsComponent implements OnInit {
    @Input()
    dissagregationType: DissagregationType;
    @Input()
    values: IndicatorValue[];

    dissagregationOptionsColumns;
    dissagregationOptionsRows;
    valuesRows: any[];

    constructor(
        public enumsService: EnumsService,
        private messageService: MessageService,
        private utilsService: UtilsService,
    ) {
    }

    ngOnInit(): void {
        console.log(this.values);
        const dissagregations: EnumsType[] = this.utilsService.getDissagregationsByDissagregationTypes(this.dissagregationType);
        dissagregations.forEach((value, index) => {
            if (index === 0) {

                this.enumsService.getByType(value).subscribe(value => {
                    this.dissagregationOptionsRows = value;
                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las opciones ',
                        detail: error.error.message,
                        life: 3000
                    });
                });
            }
            if (index === 1) {

                this.enumsService.getByType(value).subscribe(value => {
                    this.dissagregationOptionsColumns = value;
                }, error => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las opciones ',
                        detail: error.error.message,
                        life: 3000
                    });
                });
            }
        });
    }

}
