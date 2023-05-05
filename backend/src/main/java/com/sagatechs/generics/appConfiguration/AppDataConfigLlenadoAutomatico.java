package com.sagatechs.generics.appConfiguration;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.dao.RoleDao;
import com.sagatechs.generics.security.model.Role;
import com.sagatechs.generics.security.model.RoleType;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.webservice.webModel.RoleWeb;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.services.OfficeService;
import org.unhcr.osmosys.services.OrganizacionService;
import org.unhcr.osmosys.webServices.model.OrganizationWeb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("WeakerAccess")
@Singleton
@Startup
public class AppDataConfigLlenadoAutomatico {
    private static final Logger LOGGER = Logger.getLogger(AppDataConfigLlenadoAutomatico.class);


    @Inject
    AppConfigurationDao appConfigurationDao;

    @Inject
    RoleDao roleDao;

    @Inject
    OrganizacionService organizacionService;

    @Inject
    OfficeService officeService;

    @Inject
    UserService userService;

    @PostConstruct
    private void init() throws GeneralAppException {
        LOGGER.debug("Iniciando llenado automatico");
        this.createAppConfigs();
        this.cargarRoles();
        this.cargarUsuarios();
        // this.createFileAppConfigs();
        LOGGER.debug("Terminado llenado automático");
    }

    private void cargarUsuarios() throws GeneralAppException {
        User superUser = this.userService.getUNHCRUsersByUsername("salazart");

        if (superUser == null) {
            UserWeb superUserWeb = new UserWeb();
            superUserWeb.setUsername("salazart");
            superUserWeb.setName("Sebastián Salazar");
            superUserWeb.setEmail("salazart@unhcr.org");
            superUserWeb.setState(State.ACTIVO);
            OrganizationWeb org = this.organizacionService.getWebByAcronym("ACNUR");
            superUserWeb.setOrganization(org);
            List<RoleWeb> roles = new ArrayList<>();
            for (RoleType roleType : RoleType.values()) {
                if (!roleType.equals(RoleType.PUNTO_FOCAL) && !roleType.equals(RoleType.ADMINISTRADOR_OFICINA)) {
                    RoleWeb roleWeb= new RoleWeb();
                    roleWeb.setName(roleType.name());
                    roleWeb.setState(State.ACTIVO);
                    roles.add(roleWeb);
                }
            }
            superUserWeb.setRoles(roles);
            superUserWeb.setOffice(this.officeService.getWebById(1L));
            this.userService.createUser(superUserWeb);
            this.userService.changePasswordTest("salazart","1234");


        }


    }

    private void createFileAppConfigs() {
        LOGGER.info("createAppConfigs");
        instantiateConfigurationValues("Directorio de archivos",
                "Directio de archivo",
                AppConfigurationKey.FILE_DIRECTORY, "/opt/fileAttachments/");
    }

    private void cargarRoles() {

        Role roleSuperAdministrador = new Role(RoleType.SUPER_ADMINISTRADOR, State.ACTIVO);
        this.instantiateRole(roleSuperAdministrador);

    }

    @SuppressWarnings("unused")
    private void createAppConfigs() {
        LOGGER.info("createAppConfigs");


        instantiateConfigurationValues("Configuración de correo electrónico. Autenticación SMTP",
                "True si requiere autemticación smtp, false caso contrario",
                AppConfigurationKey.EMAIL_SMTP, "true");
        instantiateConfigurationValues("Configuración de correo electrónico. Requiere tls",
                "True si requiere tls smtp, false caso contrario",
                AppConfigurationKey.EMAIL_TLS, "true");
        instantiateConfigurationValues("Configuración de correo electrónico. Dirección servidor SMTP",
                "Dirección del servidor smtp",
                AppConfigurationKey.EMAIL_SMTP_HOST, "smtp.gmail.com");
        instantiateConfigurationValues("Configuración de correo electrónico. Puerto SMTP",
                "Puerto del servicio  smtp",
                AppConfigurationKey.EMAIL_SMTP_PORT, "465");
        instantiateConfigurationValues("Configuración de correo electrónico. Nombre de usuario",
                "Nombre de usuario de correo electrónico",
                AppConfigurationKey.EMAIL_USERNAME, "im.ecuador.acnur@gmail.com");
        instantiateConfigurationValues("Configuración de correo electrónico. Contraseña",
                "Contraseña de correo",
                AppConfigurationKey.EMAIL_PASSOWRD, "A1a2a3a4a5a6$");
        instantiateConfigurationValues("Configuración de correo electrónico. Dirrección de correo electrónico",
                "Dirrección de correo electrónico",
                AppConfigurationKey.EMAIL_ADDRES, "im.ecuador.acnur@gmail.com");
        instantiateConfigurationValues("path to folder files",
                "path to folder files",
                AppConfigurationKey.FILE_DIRECTORY, "FILE_DIRECTORY");
        instantiateConfigurationValues("Días en que se recuerda que se debe realizar el deporte",
                "número del 1 al 30, separados por coma",
                AppConfigurationKey.REMINDER_DAYS, "1,10");
        instantiateConfigurationValues("day limit to report",
                "día máximo del mes para el reporte de indicadores",
                AppConfigurationKey.REPORT_LIMIT, "10");
    }

    @SuppressWarnings("SameParameterValue")
    private void instantiateConfigurationValues(String nombre, String descripcion, AppConfigurationKey key,
                                                String valor) {
        AppConfiguration conf = this.appConfigurationDao.findByKey(key);
        if (conf == null) {
            AppConfiguration appConfigAppURL = new AppConfiguration(nombre, descripcion, key, valor);
            appConfigurationDao.save(appConfigAppURL);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private Role instantiateRole(Role role) {
        Role roleTmp = this.roleDao.findByRoleType(role.getRoleType());
        if (roleTmp == null) {
            return this.roleDao.save(role);
        } else {
            return role;
        }
    }

}



