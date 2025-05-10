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
        // this.cargarRoles();
        //this.cargarUsuarios();
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
        /*
        /---Result manager days---/
        instantiateConfigurationValues("Días en que se recuerda que se debe realizar la verificación de indicadores por parte de los Result Managers",
                "día del 1 al 30 para envío de recordatorios a los Result Managers",
                AppConfigurationKey.RESULT_MANAGER_REMINDER_DAY, "1");
        instantiateConfigurationValues("Día limite de verificación de indicadores para los Result Managers",
                "día máximo del mes para la verificación de indicadores por parte de los Result Managers",
                AppConfigurationKey.RESULT_MANAGER_LIMIT_DAY, "28");
        /---Correos de recordatorio---/
        instantiateConfigurationValues("Correo recordatorio del reporte de proyectos dirigido a los socios responsables de su reporte",
                "Correo de recordatorio del reporte de proyectos enviado en los 'REMINDER_DAYS'",
                AppConfigurationKey.PROJECTS_REMINDER_EMAIL, "<strong>Estimado/a colega:</strong><br>Este es un recordatorio de que el periodo de reporte de indicadores para el mes de %mesAReportar% ha iniciado. Contamos con su ayuda para tener sus datos al día en el sistema OSMOSYS hasta el día %limitDay% de este mes.  En caso de que ya se hayan reportado los datos, por favor haga caso omiso de este correo.<br>Este recordatorio ha sido generado automáticamente por el sistema OSMOSYS. En caso de dudas por favor comunicarse con el responsable correspondiente del proyecto: %projectManagerEmails%");

        instantiateConfigurationValues("Correo recordatorio del reporte de implementaciones directas dirigido a los responsables y alternos de reporte ACNUR",
                "Correo de recordatorio del reporte de implementaciones directas enviado en los 'REMINDER_DAYS'",
                AppConfigurationKey.DIRECT_IMPLS_REMINDER_EMAIL, "<strong>Estimado/a colega %userAcnur%:</strong><br>Este es un recordatorio de que el periodo de reporte de indicadores para el mes de %mesAReportar% ha iniciado. Contamos con su ayuda para tener sus datos al día en el sistema OSMOSYS hasta el día %limitDay% de este mes.  En caso de que ya haya reportado los datos, por favor haga caso omiso de este correo.<br>Este recordatorio ha sido generado automáticamente por el sistema OSMOSYS. En caso de dudas por favor comunicarse con la unidad de programas.");

        instantiateConfigurationValues("Correo recordatorio de la verificación de indicadores dirigido a los Result Managers",
                "Correo de recordatorio de la verificación de indicadores enviado en el 'RESULT_MANAGER_REMINDER_DAY'",
                AppConfigurationKey.RESULT_MANAGER_REMINDER_EMAIL, "<strong>Estimado/a colega:</strong><br>Este es un recordatorio de que el periodo de reporte de indicadores validación de indicadores para el periodo %quarterReport% ha iniciado. Agradecemos su colaboración para revisar y validar los datos reportados en el sistema OSMOSYS hasta el día %limitDay% de este mes.<br>En caso de que ya haya realizado la validación de los datos, le pedimos que ignore este mensaje.<br>Este recordatorio ha sido generado automáticamente por el sistema OSMOSYS. Si tiene alguna duda, por favor comuníquese con la Unidad de Programas.");
        /---Correos de notificación---/
        instantiateConfigurationValues("Correo de notificación enviado a usuarios nuevos con sus credenciales de acceso al sistema.",
                "Correo de notificación para usuarios nuevos",
                AppConfigurationKey.NEW_USER_NOTIFICATION_EMAIL, "<strong>Bienvenido/a:</strong><br>Se ha creado un nuevo usuario para su acceso OSMOSYS.<br>Puede acceder al sistema utilizando los siguientes datos:<br><br>Nombre de usuario: %username%<br>Contraseña: %password%<br><br>Al ingresar al sistema, comprende y acepta que la información presentada es de uso interno de la organización y no ha de ser reproducida/compartida con otros actores sin consentimiento por escrito por parte del equipo de ACNUR.<br><br>Si necesitas ayuda por favor contáctate con la unidad de programas y/o manejo de información del ACNUR. <strong>Se recomienda el uso de Google Chrome.</strong>");
        /---Correos de alerta---/
        instantiateConfigurationValues("Correo de alerta de retraso en el reporte de proyectos dirigido a los Mánager de proyectos.",
                "Correo de alerta de retraso en el reporte de proyectos para Mánager de proyectos enviado en los 'LIMIT_DAYS'",
                AppConfigurationKey.PROJECT_MANAGER_ALERT_EMAIL, "<strong>Estimado/a colega:</strong><br>Se ha encontrado que los socios <strong>%partnersList%</strong> no han reportado todos los indicadores correspondientes a sus proyectos este mes en el sistema OSMOSYS. Agradecemos hacer seguimiento con las organizaciones para asegurar que completen su reporte de forma adecuada. Este reporte ha sido generado automáticamente por el sistema OSMOSYS. En caso de dudas por favor comunicarse con la Unidad de Programas.");
        instantiateConfigurationValues("Correo de alerta de retraso en el reporte de proyectos dirigido al Supervisor de Proyecto Socio y los responsables de su reporte.",
                "Correo de alerta de retraso en el reporte de proyectos para Socios enviado en los 'LIMIT_DAYS'",
                AppConfigurationKey.PARTNERS_ALERT_EMAIL, "<strong>Estimado/a colega:</strong><br>Se han encontrado indicadores que no han sido reportados este mes por <strong>%partnerName%</strong>. Le agradecemos actualizar los datos en el sistema OSMOSYS. Recuerde que en caso de no haber implementado actividades relacionadas al indicador este mes, debe reportar el indicador en '0'. En el archivo adjunto puede encontrar el detalle de los indicadores que no han sido reportados.<br>Este reporte ha sido generado automáticamente por el sistema OSMOSYS. En caso de dudas por favor comunicarse con el responsable correspondiente del proyecto: %projectManagerEmails%");
        instantiateConfigurationValues("Correo de alerta de retraso en el reporte de implementaciones directas dirigido al Supervisor y los responsables de su reporte.",
                "Correo de alerta de retraso en el reporte de implementaciones directas para Colegas de ACNUR enviado en los 'LIMIT_DAYS'",
                AppConfigurationKey.DIRECT_IMPLS_ALERT_EMAIL, "<strong>Estimado/a %userName%:</strong><br>Se han encontrado indicadores de implementación directa con retrasos para los cuales usted es responsable. Le agradecemos actualizar los datos en el sistema OSMOSYS. Recuerde que en caso de no haber implementado actividades relacionadas al indicador este mes, debe reportar el indicador en '0'. En el archivo adjunto puede encontrar el detalle de los indicadores que no han sido reportados.<br>Este reporte ha sido generado automáticamente por el sistema OSMOSYS. En caso de dudas por favor comunicarse con la Unidad de Programas.");
        instantiateConfigurationValues("Correo de alerta de retraso en la validación de indicadores dirigido a los Mánager de Resultado.",
                "Correo de alerta de retraso en la validación de indicadores para Mánagers de Resultados enviado en el 'RESULT_MANAGER_LIMIT_DAY'",
                AppConfigurationKey.RESULT_MANAGER_ALERT_EMAIL, "<strong>Estimado/a colega</strong><br>Hemos identificado que aún no ha completado el proceso de validación de indicadores para el período %quarter%.<br>Agradecemos su apoyo en este proceso, el cual es fundamental para garantizar la calidad de los datos reportados en el sistema OSMOSYS.<br>En el archivo adjunto, encontrará el detalle de los indicadores que aún no han sido validados.<br>Este reporte ha sido generado automáticamente por el sistema OSMOSYS. En caso de dudas, por favor comuníquese con la Unidad de Programas.");
        */

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



