import {Component, Input, OnInit} from '@angular/core';
import {Office} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, MessageService, SelectItem, TreeNode} from 'primeng/api';
import {UtilsService} from '../../shared/services/utils.service';
import {EnumsService} from '../../shared/services/enums.service';
import {OfficeService} from '../../shared/services/office.service';
import {OfficeOrganizationPipe} from '../../shared/pipes/officeOrganization.pipe';

@Component({
    selector: 'app-office-administration',
    templateUrl: './office-administration.component.html',
    styleUrls: ['./office-administration.component.scss']
})
export class OfficeAdministrationComponent implements OnInit {
    items: Office[];
    cols: ColumnTable[];
    showDialog = false;
    private submitted = false;
    formItem: FormGroup;
    private states: SelectItem[];
    officeTypes: SelectItem[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    parenteOffices: SelectItem[] = [];
    officeTree: TreeNode[];
    selectedNode: TreeNode;
    activeIndex1 = 1;


    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private officeService: OfficeService,
        private officeOrganizationPipe: OfficeOrganizationPipe
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'state', header: 'Código', type: ColumnDataType.text},
            {field: 'description', header: 'Descripción ', type: ColumnDataType.text},
            {field: 'acronym', header: 'Acrónimo', type: ColumnDataType.text},
            {field: 'type', header: 'Tipo', type: ColumnDataType.text},
            {field: 'parentOffice', header: 'Oficina Padre', type: ColumnDataType.text, pipeRef: this.officeOrganizationPipe}
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');

        this.formItem = this.fb.group({
            id: new FormControl(''),
            state: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
            acronym: new FormControl(''),
            type: new FormControl('', Validators.required),
            parentOffice: new FormControl(''),
        });
        this.states = this.enumsService.getByType(EnumsType.State);
        this.officeTypes = this.enumsService.getByType(EnumsType.OfficeType);
        this.officeService.getActive().subscribe(value => {
            this.parenteOffices = value.map(value1 => {
                const selectItem: SelectItem = {
                    label: this.officeOrganizationPipe.transform(value1),
                    value: value1
                };
                return selectItem;
            });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las oficinas activas',
                detail: error.error.message,
                life: 3000
            });
        });

        this.loadTree();
    }

    private loadTree() {
        this.officeService.getTree().subscribe(value => {

            /*
            this.officeTree = [{
                label: 'CEO',
                type: 'person',
                styleClass: 'p-person',
                expanded: true,
                data: {name: 'Walter White', 'avatar': 'walter.jpg'},
                children: [
                    {
                        label: 'CFO',
                        type: 'person',
                        styleClass: 'p-person',
                        expanded: true,
                        data: {name: 'Saul Goodman', 'avatar': 'saul.jpg'},
                        children: [{
                            label: 'Tax',
                            styleClass: 'department-cfo'
                        },
                            {
                                label: 'Legal',
                                styleClass: 'department-cfo'
                            }],
                    },
                    {
                        label: 'COO',
                        type: 'person',
                        styleClass: 'p-person',
                        expanded: true,
                        data: {name: 'Mike E.', 'avatar': 'mike.jpg'},
                        children: [{
                            label: 'Operations',
                            styleClass: 'department-coo'
                        }]
                    },
                    {
                        label: 'CTO',
                        type: 'person',
                        styleClass: 'p-person',
                        expanded: true,
                        data: {name: 'Jesse Pinkman', 'avatar': 'jesse.jpg'},
                        children: [{
                            label: 'Development',
                            styleClass: 'department-cto',
                            expanded: true,
                            children: [{
                                label: 'Analysis',
                                styleClass: 'department-cto'
                            },
                                {
                                    label: 'Front End',
                                    styleClass: 'department-cto'
                                },
                                {
                                    label: 'Back End',
                                    styleClass: 'department-cto'
                                }]
                        },
                            {
                                label: 'QA',
                                styleClass: 'department-cto'
                            },
                            {
                                label: 'R&D',
                                styleClass: 'department-cto'
                            }]
                    }
                ]
            }];
             */
        });
    }

    private officeTreeToNodeTree(officeTree: Office[]) {
        const treeNode: TreeNode[] = [];
        officeTree.forEach(value => {
            const node = this.officeToNodeTree(value);

            if (value && value.childOffices.length > 0) {
                node.children = [];
                value.childOffices.forEach(value1 => {
                    node.children.push(this.officeToNodeTree(value1));
                });
            }
            treeNode.push(node);
        });
        return treeNode;
    }

    private officeToNodeTree(office: Office) {
        const node: TreeNode = {
            label: office.acronym,
            type: 'person',
            styleClass: 'p-person',
            expanded: true,
            data: office
        };
        return node;
    }

    private loadItems() {
        this.officeService.getAll().subscribe(value => {
            this.items = value;
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las oficinas',
                detail: error.error.message,
                life: 3000
            });
        });
    }

    exportExcel() {
        import('xlsx').then(xlsx => {
            const itemsRenamed = this.utilsService.renameKeys(this.items, this.cols);
            const worksheet = xlsx.utils.json_to_sheet(itemsRenamed);
            const workbook = {Sheets: {data: worksheet}, SheetNames: ['data']};
            const excelBuffer: any = xlsx.write(workbook, {bookType: 'xlsx', type: 'array'});
            this.utilsService.saveAsExcelFile(excelBuffer, 'oficinas');
        });
    }


    createItem() {
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Office();
        this.formItem.patchValue(newItem);
    }

    editItem(office: Office) {
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(office);
    }


    saveItem() {
        this.messageService.clear();
        const {
            id,
            state,
            description,
            acronym,
            type,
            parentOffice,
            childOffices
        }
            = this.formItem.value;
        const office: Office = {
            id,
            state,
            description,
            acronym,
            type,
            parentOffice,
            childOffices
        };
        if (office.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.officeService.update(office).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
                this.loadTree();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al actualizar la oficina',
                    detail: error.error.message,
                    life: 3000
                });
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.officeService.save(office).subscribe(id => {
                this.cancelDialog();
                this.loadItems();
                this.loadTree();
            }, error => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al guardar la oficina',
                    detail: error.error.message,
                    life: 3000
                });
            });
        }

    }

    cancelDialog() {
        this.showDialog = false;
        this.submitted = false;
    }

    @Input() get selectedColumns(): any[] {
        return this._selectedColumns;
    }

    set selectedColumns(val: any[]) {
        // restore original order
        this._selectedColumns = this.cols.filter(col => val.includes(col));
    }

    onNodeSelect($event: any) {
        this.editItem($event.node.data);
    }
}
