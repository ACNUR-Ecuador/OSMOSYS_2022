import {Component, OnInit} from '@angular/core';
import {ConfirmationService, MessageService, SelectItem} from "primeng/api";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {UtilsService} from "../../services/utils.service";
import {UserService} from "../../services/user.service";
import {OfficeService} from "../../services/office.service";
import {OfficeOrganizationPipe} from "../../shared/pipes/office-organization.pipe";
import {EnumsService} from "../../services/enums.service";
import {EnumsState, EnumsType} from "../../shared/model/UtilsModel";
import {User} from "../../shared/model/User";

@Component({
    selector: 'app-user-profile',
    templateUrl: './user-profile.component.html',
    styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
    userForm: FormGroup;

    offices: SelectItem[];

    constructor(private messageService: MessageService,
                private confirmationService: ConfirmationService,
                private fb: FormBuilder,
                public utilsService: UtilsService,
                private userService: UserService,
                private officeService: OfficeService,
                private officeOrganizationPipe: OfficeOrganizationPipe,
                private enumsService: EnumsService) {
    }

    ngOnInit(): void {
        this.createForm();
        this.loadOptions();
        this.loadUser();
    }

    private loadOptions() {
        this.officeService.getActive().subscribe(offices => {
            this.offices = offices
                .sort((a, b) => a.acronym.localeCompare(b.acronym))
                .map(value => {
                    return {
                        label: this.officeOrganizationPipe.transform(value),
                        value
                    };
                });
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar las oficinas',
                detail: error.error.message,
                life: 3000
            });
        });

    }

    private loadUser() {
        this.userService.getById(this.userService.getLogedUsername().id).subscribe(value => {
            const currentUser = value;
            this.userForm.get('id').patchValue(currentUser.id);
            this.userForm.get('name').patchValue(currentUser.name, Validators.required);
            this.userForm.get('username').patchValue(currentUser.username,
                [Validators.required, Validators.maxLength(50), Validators.minLength(3)]);
            this.userForm.get('email').patchValue(currentUser.email, [Validators.required, Validators.maxLength(255), Validators.email]);
            this.userForm.get('organization').patchValue(currentUser.organization.acronym);
            if (!currentUser.organization || currentUser.organization.acronym === 'ACNUR') {
                // ES ACNUR
                this.userForm.get('office').patchValue(currentUser.office);
                this.userForm.get('office').enable();
            } else {
                this.userForm.get('office').disable();
            }
            const roles = currentUser.roles.filter(value1 => {
                return value1.state === EnumsState.ACTIVE;
            }).map(value1 => {
                return value1.name;
            }).map(value1 => {
                return this.enumsService.resolveLabel(EnumsType.RoleType, value1);
            }).join(' - ');
            this.userForm.get('roles').patchValue(roles);
            this.userForm.get('roles').disable();
        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al cargar el usuario',
                detail: error.error.message,
                life: 3000
            });
        });

    }

    private createForm() {
        this.userForm = this.fb.group({
            id: new FormControl(''),
            name: new FormControl('', Validators.required),
            username: new FormControl({value: '', disabled: true}, Validators.required),
            email: new FormControl('', Validators.required),
            organization: new FormControl({value: '', disabled: true}),
            office: new FormControl('', Validators.required),
            roles: new FormControl(''),
            state: new FormControl('')
        });
    }

    save() {
        const {
            name,
            email,
            office
        } = this.userForm.value;
        const user: User = this.userService.getLogedUsername();
        user.name = name;
        user.email = email;
        user.office = office;
        user.state = EnumsState.ACTIVE;
        this.userService.updateUser(user).subscribe(() => {
            this.messageService.add({
                severity: 'success',
                summary: 'Usuario actualizado correctamente',
                life: 3000
            });

        }, error => {
            this.messageService.add({
                severity: 'error',
                summary: 'Error al actualizar el perfil',
                detail: error.error.message,
                life: 3000
            });
        });


    }

    cancel() {
        this.loadUser();

    }
}
