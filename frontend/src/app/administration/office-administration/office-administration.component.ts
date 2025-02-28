import {Component, Input, OnInit} from '@angular/core';
import {Office} from '../../shared/model/OsmosysModel';
import {ColumnDataType, ColumnTable, EnumsType} from '../../shared/model/UtilsModel';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ConfirmationService, MessageService, SelectItem, TreeNode} from 'primeng/api';
import {UtilsService} from '../../services/utils.service';
import {EnumsService} from '../../services/enums.service';
import {OfficeService} from '../../services/office.service';
import {Table} from 'primeng/table';
import {OfficeOrganizationPipe} from '../../shared/pipes/office-organization.pipe';
import {UserService} from "../../services/user.service";
import {UserPipe} from "../../shared/pipes/user.pipe";

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
    states: SelectItem[];
    officeTypes: SelectItem[];
    // tslint:disable-next-line:variable-name
    _selectedColumns: ColumnTable[];
    parenteOffices: SelectItem[] = [];
    usersAdministratorOptions: SelectItem[] = [];
    officeTree: TreeNode[];
    selectedNode: TreeNode;
    activeIndex1 = 0;
    allParentOffices: SelectItem[] = [];

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private fb: FormBuilder,
        public utilsService: UtilsService,
        private enumsService: EnumsService,
        private officeService: OfficeService,
        private userService: UserService,
        private userPipe: UserPipe,
        private officeOrganizationPipe: OfficeOrganizationPipe
    ) {
    }

    ngOnInit(): void {
        this.loadItems();
        this.cols = [
            {field: 'id', header: 'Id', type: ColumnDataType.numeric},
            {field: 'description', header: 'Descripción ', type: ColumnDataType.text},
            {field: 'acronym', header: 'Acrónimo', type: ColumnDataType.text},
            {field: 'type', header: 'Tipo', type: ColumnDataType.text},
            {field: 'state', header: 'Estado', type: ColumnDataType.text},
            {
                field: 'parentOffice',
                header: 'Oficina Padre',
                type: ColumnDataType.text,
                pipeRef: this.officeOrganizationPipe
            }
        ];
        this._selectedColumns = this.cols.filter(value => value.field !== 'id');

        this.formItem = this.fb.group({
            id: new FormControl(''),
            state: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
            acronym: new FormControl('', Validators.required),
            type: new FormControl('', Validators.required),
            parentOffice: new FormControl(''),
            administrators: new FormControl(''),
        });
        this.enumsService.getByType(EnumsType.State).subscribe(value => {
            this.states = value;
        });
        this.enumsService.getByType(EnumsType.OfficeType).subscribe(value => {
            this.officeTypes = value;
        });

        this.userService.getActiveUNHCRUsers().subscribe({
            next: value => {
                this.usersAdministratorOptions = value.map(value1 => {
                    const selectItem: SelectItem = {
                        label: this.userPipe.transform(value1),
                        value: value1
                    };
                    return selectItem;
                });
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar los usuarios ',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });

        this.loadTree();
    }

    private loadTree() {
        this.officeService.getTree().subscribe(value => {
            this.officeTree = this.officeTreeToNodeTree(value);
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
                /*value.childOffices.forEach(value1 => {
                    node.children.push(this.officeToNodeTree(value1));
                });*/
                node.children = this.officeTreeToNodeTree(value.childOffices);
            }
            treeNode.push(node);
        });
        return treeNode;
    }

    // noinspection JSMethodCanBeStatic
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
        this.officeService.getAll()
            .subscribe({
                next: value => {
                    this.items = value;
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al cargar las oficinas',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
        this.officeService.getActive().subscribe({
            next: value => {
                this.parenteOffices = value.map(value1 => {
                    const selectItem: SelectItem = {
                        label: this.officeOrganizationPipe.transform(value1),
                        value: value1
                    };
                    return selectItem;
                });
                this.allParentOffices=this.parenteOffices
            },
            error: err => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error al cargar las oficinas activas',
                    detail: err.error.message,
                    life: 3000
                });
            }
        });

    }

    exportExcel(table: Table) {
        this.utilsService.exportTableAsExcel(this._selectedColumns,
            table.filteredValue ? table.filteredValue : this.items,
            'oficinas');
    }

    createItem() {
        this.parenteOffices=this.allParentOffices
        this.messageService.clear();
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        const newItem = new Office();
        this.formItem.patchValue(newItem);
    }

    editItem(office: Office) {
        this.parenteOffices=this.allParentOffices
        this.utilsService.resetForm(this.formItem);
        this.submitted = false;
        this.showDialog = true;
        this.formItem.patchValue(office);
        const officeRemoveItself=this.parenteOffices.filter(value1 =>{
            return value1.value.id !== office.id
            
        })
        this.parenteOffices=officeRemoveItself

        const officeType: string = office.type;
        this.onOfficeTypeChange(officeType)
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
            childOffices,
            administrators
        }
            = this.formItem.value;
        const office: Office = {
            id,
            state,
            description,
            acronym,
            type,
            parentOffice,
            childOffices,
            administrators
        };

        if (office.id) {
            // tslint:disable-next-line:no-shadowed-variable
            this.officeService.update(office).subscribe({
                next: () => {
                    this.cancelDialog();
                    this.loadItems();
                    this.loadTree();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Oficina guardada exitosamente',
                        life: 3000
                    });
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al actualizar la oficina',
                        detail: err.error.message,
                        life: 3000
                    });
                }
            });
        } else {
            // tslint:disable-next-line:no-shadowed-variable
            this.officeService.save(office).subscribe({
                next: () => {
                    this.cancelDialog();
                    this.loadItems();
                    this.loadTree();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Oficina guardada exitosamente',
                        life: 3000
                    });
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error al guardar la oficina',
                        detail: err.error.message,
                        life: 3000
                    });
                }
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

    onOfficeTypeChange(officeType: string) {
        if (officeType) {
            if (officeType === "BO" || officeType === "OFICINA_NACIONAL" || officeType === "OFICINA_MULTI_PAIS" ) {
                this.formItem.get('parentOffice').patchValue(null);
                this.formItem.get('parentOffice').clearValidators();
                this.formItem.get('parentOffice').updateValueAndValidity();
                this.formItem.get('parentOffice').disable();
            } else {
                this.formItem.get('parentOffice').setValidators([Validators.required]);
                this.formItem.get('parentOffice').enable();
                this.formItem.get('parentOffice').updateValueAndValidity();
            }
            
        }
    }

    convertoToOfficeTypeLabel(officeType:string){
        const officeTypeLabel=this.officeTypes.find(item => item.value === officeType)?.label
        if(officeTypeLabel){
            return officeTypeLabel
        }else{
            return ""
        }
    }
}

