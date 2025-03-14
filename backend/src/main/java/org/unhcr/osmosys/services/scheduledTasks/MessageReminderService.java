package org.unhcr.osmosys.services.scheduledTasks;

import com.sagatechs.generics.appConfiguration.AppConfigurationKey;
import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.dao.UserDao;
import com.sagatechs.generics.security.model.RoleAssigment;
import com.sagatechs.generics.security.model.RoleType;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.service.EmailService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.services.*;
import org.unhcr.osmosys.webServices.model.ProjectResumeWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class MessageReminderService {
    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(MessageReminderService.class);


    @Inject
    AppConfigurationService appConfigurationService;

    @Inject
    OrganizacionService organizacionService;

    @Inject
    UtilsService utilsService;
    @Inject
    PeriodService periodService;

    @Inject
    EmailService emailService;
    @Inject
    UserService userService;
    @Inject
    ProjectService projectService;
    @Inject
   IndicatorExecutionService indicatorExecutionService;



    public void sendPartnersRemindersToFocalPoints() throws GeneralAppException {
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
           if (currentPeriod == null) {
            return;
        }
        Integer limitDay = this.appConfigurationService.getReportLimitDay();
        if (limitDay == null) {
            return;
        }
        Integer currentMonthYearOrder = this.utilsService.getCurrentMonthYearOrder();
        MonthEnum mesAReportar;
        if (currentMonthYearOrder == 1) {
            mesAReportar = MonthEnum.DICIEMBRE;
        } else {
            mesAReportar = MonthEnum.getMonthByNumber(currentMonthYearOrder - 1);
        }
        // obtengo los focal points de este periodo
        //List<User> focalPoints = this.projectService.getFocalPointByPeriodId(currentPeriod.getId());
        List<String> imProgramsEmail = new ArrayList<>();
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL));
        }
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL));
        }
        /*
        for (User focalPoint : focalPoints) {
            List<MessageAlertServiceV2.PartnerAlertDTO> alerts = new ArrayList<>();
            // obtengo los proyectos por cada focal point
            LOGGER.info(focalPoint.getName());
            List<ProjectResumeWeb> projects = this.projectService.getProjectResumenWebByPeriodIdAndFocalPointId(currentPeriod.getId(), focalPoint.getId());

            String message = "<p><strong>Estimado/a colega " + ":</strong></p>" +
                    "<p>Este es un recordatorio de que el periodo de reportes de indicadores para el mes de  " + mesAReportar.getLabel() +
                    " ha iniciado. Contamos con su ayuda para tener sus datos al día en el sistema OSMOSYS-ACNUR hasta el día " + limitDay + " de este mes. " +
                    " Los socios asignados para Ud. son " +projects.stream().map(ProjectResumeWeb::getOrganizationAcronym).collect(Collectors.joining(", "))+
                    ". En caso de que ya se hayan reportado los datos, por favor haga caso omiso de este correo.</p>" +
                    "<p>Este recordatorio ha sido generado automaticamente el por el sistema OSMOSYS. En caso de dudas por favor comunicarse con con su punto focal de ACNUR.</p>";

            String copyAddresses = CollectionUtils.isNotEmpty(imProgramsEmail) ? String.join(", ", imProgramsEmail) : null;
            this.emailService.sendEmailMessage(focalPoint.getEmail(), copyAddresses, "Recordatorio de reporte Indicadores OSMOSYS-Socios", message);
            // this.emailService.sendEmailMessage("salazart@unhcr.org", "salazart@unhcr.org", "Recordatorio de reporte Indicadors OSMOSYS", message);
       }
        List<User> partnerSupervisors = this.projectService.getPartnerSupervisorsByPeriodId(currentPeriod.getId());
        for (User partnerSupervisor : partnerSupervisors) {
            List<MessageAlertServiceV2.PartnerAlertDTO> alerts = new ArrayList<>();
            // obtengo los proyectos por cada partner supervisor
            LOGGER.info(partnerSupervisor.getName());
            List<ProjectResumeWeb> projects = this.projectService.getProjectResumenWebByPeriodIdAndPartnerSupervisorId(currentPeriod.getId(), partnerSupervisor.getId());

            String message = "<p><strong>Estimado/a colega " + ":</strong></p>" +
                    "<p>Este es un recordatorio de que el periodo de reportes de indicadores para el mes de  " + mesAReportar.getLabel() +
                    " ha iniciado. Contamos con su ayuda para tener sus datos al día en el sistema OSMOSYS-ACNUR hasta el día " + limitDay + " de este mes. " +
                    " Los proyectos asignados para Ud. son " +projects.stream().map(ProjectResumeWeb::getCode).collect(Collectors.joining(", "))+
                    ". En caso de que ya se hayan reportado los datos, por favor haga caso omiso de este correo.</p>" +
                    "<p>Este recordatorio ha sido generado automaticamente el por el sistema OSMOSYS. En caso de dudas por favor comunicarse con su punto focal de ACNUR.</p>";

            String copyAddresses = CollectionUtils.isNotEmpty(imProgramsEmail) ? String.join(", ", imProgramsEmail) : null;
            this.emailService.sendEmailMessage(partnerSupervisor.getEmail(), copyAddresses, "Recordatorio de reporte Indicadores OSMOSYS-Socios", message);
            // this.emailService.sendEmailMessage("salazart@unhcr.org", "salazart@unhcr.org", "Recordatorio de reporte Indicadors OSMOSYS", message);
        }
        */
        List<Organization> organizations = this.projectService.getActiveProjectsPartnersByPeriodId(currentPeriod.getId());
        for (Organization organization : organizations) {
            List<MessageAlertServiceV2.PartnerAlertDTO> alerts = new ArrayList<>();
            // obtengo los proyectos por cada organización
            LOGGER.info(organization.getAcronym());
            List<Project> projects = this.projectService.getProjectsByPeriodIdAndOrganizationId(currentPeriod.getId(), organization.getId());
            // Obtener los ProjectManagers de los proyectos (usuarios)
            List<User> projectManagers = projects.stream()
                    .flatMap(project -> project.getFocalPointAssignations().stream()) // Aplanar la lista de focalPointAssignations
                    .map(FocalPointAssignation::getFocalPointer)
                    .collect(Collectors.toList());
            //obtengo todos los responsables de reporte de la organización
            List<User> partnerUsers = userService.getActivePartnerUsers(organization.getId()).stream()
                    .filter(user -> user.getRoleAssigments().stream()
                            .anyMatch(roleAssigment -> roleAssigment.getRole().getRoleType().equals(RoleType.EJECUTOR_PROYECTOS) && roleAssigment.getState().equals(State.ACTIVO)))
                    .collect(Collectors.toList());
            StringBuilder projectManagerEmails = new StringBuilder();
            for(Project project: projects){
                projectManagerEmails.append("<li><strong>").append(project.getCode()).append("</strong>")
                        .append(" - ") // Separador entre los atributos
                        .append(project.getFocalPointAssignations().stream()
                                .map(focalPointAssignation -> focalPointAssignation.getFocalPointer().getEmail())
                                .findFirst()
                                .orElse(""))
                        .append("</li>"); // fin del item
            }
            String resultString = "</br>\n<ul>\n" + projectManagerEmails + "\n</ul>";
            for(User partnerUser: partnerUsers){
                LOGGER.debug("user: " + partnerUser.getEmail());
                String message= this.appConfigurationService.findValorByClave(AppConfigurationKey.PROJECTS_REMINDER_EMAIL);
                message=message.replace("%mesAReportar%",mesAReportar.getLabel())
                                .replace("%limitDay%",String.valueOf(limitDay))
                                .replace("%projectManagerEmails%",resultString);

                String copyAddresses = CollectionUtils.isNotEmpty(imProgramsEmail) ? String.join(", ", imProgramsEmail) : null;
                this.emailService.sendEmailMessage(partnerUser.getEmail(), copyAddresses, "Recordatorio de reporte de Indicadores OSMOSYS-Socios", message);

            }
        }
    }

    public void sendResultsManagerReminders() throws GeneralAppException {
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        if (currentPeriod == null) {
            return;
        }
        Integer limitDay = this.appConfigurationService.getResultManagerLimitDay();

        Integer currentMonthYearOrder = this.utilsService.getCurrentMonthYearOrder();
        int mes = currentMonthYearOrder % 100;  // Esto nos da los dos últimos dígitos (mes)

        // Determinar el trimestre a reportar
        String quarterReport;
        if (mes >= 1 && mes <= 3) {
            quarterReport = "Q4";  // Primer trimestre (Enero, Febrero, Marzo)
        } else if (mes >= 4 && mes <= 6) {
            quarterReport = "Q1"; // Segundo trimestre (Abril, Mayo, Junio)
        } else if (mes >= 7 && mes <= 9) {
            quarterReport = "Q2";  // Tercer trimestre (Julio, Agosto, Septiembre)
        } else if (mes >= 10 && mes <= 12) {
            quarterReport = "Q3";  // Cuarto trimestre (Octubre, Noviembre, Diciembre)
        } else {
            quarterReport = "QX";  // Si el mes no es válido (aunque no se espera que ocurra)
        }


        // obtengo los Usuarios resultManagers
        List<User> resultManagers = this.userService.getActiveResultManagerUsers();
        List<String> imProgramsEmail = new ArrayList<>();
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.IM_EMAIL));
        }
        if (StringUtils.isNotBlank(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL))) {
            imProgramsEmail.add(this.appConfigurationService.findValorByClave(AppConfigurationKey.PROGRAMS_EMAIL));
        }

        for (User resultManager : resultManagers) {
            LOGGER.info(resultManager.getName());
            // obtengo los indicadores de ejecución del periodo actual por cada result Manager
            List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionService.getIndicatorExecutionsByResultManagerAndPeriodId(resultManager.getId(), currentPeriod.getId());
            Set<Indicator> indicators= new HashSet<>();
            for(IndicatorExecution ie: indicatorExecutions){
                if(ie.getIndicator() != null){
                    indicators.add(ie.getIndicator());
                }
            }
            if(indicators.isEmpty()){
                continue;
            }
            String message= this.appConfigurationService.findValorByClave(AppConfigurationKey.RESULT_MANAGER_REMINDER_EMAIL);
            message=message.replace("%quarterReport%",quarterReport)
                    .replace("%limitDay%",String.valueOf(limitDay));

            String copyAddresses = CollectionUtils.isNotEmpty(imProgramsEmail) ? String.join(", ", imProgramsEmail) : null;
            this.emailService.sendEmailMessage(resultManager.getEmail(), copyAddresses, "Recordatorio de Validación de Indicadores OSMOSYS-Result Managers", message);
        }
    }

    public void sendPartnersReminders() throws GeneralAppException {
        LOGGER.info("Send partners reminders");
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        if (currentPeriod == null) {
            return;
        }
        Integer limitDay = this.appConfigurationService.getReportLimitDay();
        if (limitDay == null) {
            return;
        }
        Integer currentMonthYearOrder = this.utilsService.getCurrentMonthYearOrder();
        MonthEnum mesAReportar;
        if (currentMonthYearOrder == 1) {
            mesAReportar = MonthEnum.DICIEMBRE;
        } else {
            mesAReportar = MonthEnum.getMonthByNumber(currentMonthYearOrder - 1);
        }
        LOGGER.info("Mes a reportar: " + mesAReportar.getLabel());
        // recupero los socios de este periodo

        List<Organization> organizations = this.organizacionService.getByPeriodId(currentPeriod.getId());
        // para cada organizacion
        for (Organization organization : organizations) {
            // recupero los usuarios
            List<User> usersPartners = this.userService.getActivePartnerUsers(organization.getId());
            // recupero focal points
            List<User> focalPoints = this.userService.getFocalPointsByOrganizationIdAndPeriodId(organization.getId(), currentPeriod.getId());
            String usersEmails = usersPartners.stream().map(User::getEmail).collect(Collectors.joining(", "));
            String focalPointsEmails = focalPoints.stream().map(User::getEmail).collect(Collectors.joining(", "));

            String message = "<p style=\"text-align:justify\">Estimado/a colegas del socio " + organization.getDescription() + ":</p>" +
                    "<p style=\"text-align:justify\">" +
                    " Este es un recordatorio de que el periodo de reportes de indicadores para el mes de  " + mesAReportar.getLabel() +
                    " ha iniciado. Contamos con su ayuda para tener sus datos al día en el sistema OSMOSYS-ACNUR hasta el día " + limitDay + " de este mes. " +
                    "En caso de que ya se hayan reportado los datos, por favor haga caso omiso de este correo.</p>" +
                    "<p style=\"text-align:justify\">Este recordatorio ha sido generado automaticamente el por el sistema OSMOSYS. En caso de dudas por favor comunicarse con con su punto focal de ACNUR.</p>";
            LOGGER.debug("usersEmails: " + usersEmails);
            LOGGER.debug("focalPointsEmails: " + focalPointsEmails);

            this.emailService.sendEmailMessage(focalPointsEmails, null, "Recordatorio de reporte de Indicadores OSMOSYS", message);
            // this.emailService.sendEmailMessage("salazart@unhcr.org", "salazart@unhcr.org", "Recordatorio de reporte Indicadors OSMOSYS", message);


        }


    }

    public void sendDirectImplementationReminders() throws GeneralAppException {
        LOGGER.info("Send DI reminders");
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        if (currentPeriod == null) {
            return;
        }
        Integer limitDay = this.appConfigurationService.getReportLimitDay();
        if (limitDay == null) {
            return;
        }
        Integer currentMonthYearOrder = this.utilsService.getCurrentMonthYearOrder();
        MonthEnum mesAReportar;
        if (currentMonthYearOrder == 1) {
            mesAReportar = MonthEnum.DICIEMBRE;
        } else {
            mesAReportar = MonthEnum.getMonthByNumber(currentMonthYearOrder - 1);
        }
        LOGGER.info("Mes a reportar: " + mesAReportar.getLabel());
        // recupero los usuarios responsables y alternos de reporte
        List<User> users = this.userService.getActiveResponsableAndAlternsDirectImplementationUsers(currentPeriod.getId());

        // para cada organizacion
        for (User user : users) {
            String message= this.appConfigurationService.findValorByClave(AppConfigurationKey.DIRECT_IMPLS_REMINDER_EMAIL);
            LOGGER.debug("user: " + user.getEmail());
            message=message.replace("%userAcnur%",user.getName())
                    .replace("%mesAReportar%",mesAReportar.getLabel())
                    .replace("%limitDay%",String.valueOf(limitDay));

            this.emailService.sendEmailMessage(user.getEmail(), null, "Recordatorio de reporte Indicadors OSMOSYS", message);
            // this.emailService.sendEmailMessage("salazart@unhcr.org", null, "Recordatorio de reporte Indicadors OSMOSYS", message);


        }


    }
}
