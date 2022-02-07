package com.sagatechs.generics.appConfiguration;


import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.dao.RoleDao;
import com.sagatechs.generics.security.model.Role;
import com.sagatechs.generics.security.model.RoleType;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;


@SuppressWarnings("WeakerAccess")
@Singleton
@Startup
public class AppDataConfigLlenadoAutomatico {
    private static final Logger LOGGER = Logger.getLogger(AppDataConfigLlenadoAutomatico.class);


    @Inject
    AppConfigurationDao appConfigurationDao;

    @Inject
    RoleDao roleDao;

    private Role roleSuperAdministrador;

    @PostConstruct
    private void init() {
        LOGGER.debug("Iniciando llenado automatico");
       this.createAppConfigs();
        this.cargarRoles();
       // this.cargarUsuarios();*/
       // this.createFileAppConfigs();
        LOGGER.debug("Terminado llenado automático");
    }

    private void createFileAppConfigs() {
        LOGGER.info("createAppConfigs");


        instantiateConfigurationValues("Directorio de archivos",
                "Directio de archivo",
                AppConfigurationKey.FILE_DIRECTORY, "/opt/fileAttachments/");
    }

    private void cargarRoles() {

        this.roleSuperAdministrador = new Role(RoleType.SUPER_ADMINISTRADOR, State.ACTIVO);
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
        instantiateConfigurationValues("alert days to report of month, comma separeted ",
                "alert days to report of month, comma separeted ",
                AppConfigurationKey.ALERT_DAYS, "5");
        instantiateConfigurationValues("warning days of month to report, comma separated",
                "warning days of month to report, comma separated",
                AppConfigurationKey.WARNING_DAYS, "10");
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

    private Role instantiateRole(Role role) {
        Role roleTmp = this.roleDao.findByRoleType(role.getRoleType());
        if (roleTmp == null) {
            return this.roleDao.save(role);
        } else {
            return role;
        }
    }

}
