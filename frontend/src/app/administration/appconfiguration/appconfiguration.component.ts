import {Component, OnInit} from '@angular/core';
import {AppConfigurationService} from '../../services/app-configuration.service';
import {AppConfiguration} from '../../shared/model/OsmosysModel';
import {MessageService} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';

@Component({
    selector: 'app-appconfiguration',
    templateUrl: './appconfiguration.component.html',
    styleUrls: ['./appconfiguration.component.scss']
})
export class AppconfigurationComponent implements OnInit {

    items: AppConfiguration[];

    constructor(
        private appConfigurationService: AppConfigurationService,
        private messageService: MessageService,
        public utilsService: UtilsService
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
    }

    private loadItems() {
        this.appConfigurationService.getAll().subscribe({
            next: value => {
                this.items = value;
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    onRowEditSave(item: AppConfiguration) {

        this.appConfigurationService.update(item).subscribe({
            next: () => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Guardado correctamente',
                    life: 3000
                });
            }, error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al guardar',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });
    }

    onRowEditCancel() {
        this.loadItems();
    }
}
