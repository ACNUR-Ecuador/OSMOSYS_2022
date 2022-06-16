package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.service.EmailService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.Project;
import org.unhcr.osmosys.services.PeriodService;
import org.unhcr.osmosys.services.ProjectService;
import org.unhcr.osmosys.services.UtilsService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class MessageAlertService {
    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(MessageAlertService.class);


    @Inject
    ReportService reportService;

    @Inject
    AppConfigurationService appConfigurationService;

    @Inject
    ProjectService projectService;

    @Inject
    PeriodService periodService;

    @Inject
    UtilsService utilsService;

    @Inject
    EmailService emailService;
    @Inject
    UserService userService;

    public void sendAlertToPartners() {
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        List<Project> projects = this.projectService.getByPeriodId(currentPeriod.getId());

        for (Project project : projects) {

            try {
                ByteArrayOutputStream report = this.reportService.getPartnerLateReportByProjectId(project.getId());

                if (report != null) {

                    String message =
                            "<p style=\"text-align:justify\">Estimado/a colega:</p>" +
                                    "<p style=\"text-align:justify\">Encuentre adjunto el reporte de indicadores con retraso para el proyecto " + project.getName() + "-" + project.getOrganization().getDescription() + ". Rogamos su ayuda para poner al d&iacute;a los datos del proyecto en el sistema OSMOSYS-ACNUR.</p>" +
                                    "<p style=\"text-align:justify\">Este reporte ha sido generado automaticamente el por el sistema OSMOSYS. En caso de dudas por favor comunicarse con su punto focal.</p>";

                    LOGGER.info("reporte retrasos socio: " + project.getName() + ":" + project.getOrganization().getAcronym());
                    String copyAddress = "ecuquosmosys@unhcr.org, " + project.getFocalPoint().getEmail();
                    LOGGER.info(copyAddress);
                    List<User> parterUsers = this.userService.getActivePartnerUsers(project.getOrganization().getId());
                    String destinationAdress = parterUsers.stream().map(User::getEmail).collect(Collectors.joining(", "));
                    LOGGER.info(destinationAdress);
                    this.emailService.sendEmailMessageWithAttachment(destinationAdress, copyAddress, "Reporte de retrasos " + project.getOrganization().getAcronym(), message, report, "Reporte_retrasos_socio.xlsx");
                } else {
                    LOGGER.info("No enviado por no retrasos report de retrasos de socio : ");
                    LOGGER.info("reporte retrasos socio: " + project.getName() + ":" + project.getOrganization().getAcronym());

                }
            } catch (Exception e) {
                LOGGER.error("Error al generar report de retrasos de socio : ");
                LOGGER.error("reporte retrasos socio: " + project.getName() + ":" + project.getOrganization().getAcronym());
                LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
            }


        }


    }

    public void sendAlertReviewToPartnersFocalPoints() {
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        List<Project> projects = this.projectService.getByPeriodId(currentPeriod.getId());

        for (Project project : projects) {

            try {
                ByteArrayOutputStream report = this.reportService.getPartnerLateReviewReportByProjectId(project.getId());

                if (report != null) {

                    String message =
                            "<p style=\"text-align:justify\">Estimado/a colega:</p>" +
                                    "<p style=\"text-align:justify\">Encuentre adjunto el reporte de indicadores que tienen retrazo en su validación para el proyecto " + project.getName() + "-" + project.getOrganization().getDescription() + ". " +
                                    " Le recodamos que como punto focal, contamos con su ayuda para validar los datos reportados por el socio y registrar esta validación en OSMOSYS. " +
                                    " Rogamos su ayuda para poner al d&iacute;a los datos del proyecto en el sistema OSMOSYS-ACNUR.</p>" +
                                    "<p style=\"text-align:justify\">Este reporte ha sido generado automaticamente el por el sistema OSMOSYS. En caso de dudas por favor comunicarse con su punto focal.</p>";

                    LOGGER.info("reporte retrasos socio: " + project.getName() + ":" + project.getOrganization().getAcronym());
                    String copyAddress = "ecuquosmosys@unhcr.org, " + project.getFocalPoint().getEmail();
                    LOGGER.info(copyAddress);

                    List<User> parterUsers = this.userService.getActivePartnerUsers(project.getOrganization().getId());
                    String destinationAdress = project.getFocalPoint().getEmail();
                    LOGGER.info(destinationAdress);
                    this.emailService.sendEmailMessageWithAttachment(destinationAdress, copyAddress, "Reporte de validación:" + project.getOrganization().getAcronym() + ":" + project.getFocalPoint().getName(), message, report, "Reporte_validacion_socio.xlsx");
                } else {
                    LOGGER.info("No enviado por todo validado de socio : ");
                    LOGGER.info("reporte retrasos socio: " + project.getName() + ":" + project.getOrganization().getAcronym());

                }
            } catch (Exception e) {
                LOGGER.error("Error al generar report de validacion de socio : ");
                LOGGER.error("reporte retrasos socio: " + project.getName() + ":" + project.getOrganization().getAcronym());
                LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
            }


        }


    }

    public void sendAlertToDirectImplementation() {
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        List<User> responsables = this.userService.getActiveResponsableDirectImplementationUsers(currentPeriod.getId());

        for (User responsable : responsables) {
            try {
                ByteArrayOutputStream report = this.reportService.getDirectImplementationLateReportByResponsableId(responsable.getId());

                if (report != null) {

                    String message =
                            "<p style=\"text-align:justify\">Estimado/a colega:</p>" +
                                    "<p style=\"text-align:justify\">Encuentre adjunto el reporte de indicadores con retraso en OSMOSYS para los que usted es responsable de reporte." +
                                    " Rogamos su ayuda para poner al d&iacute;a los datos de su oficina en el sistema OSMOSYS-ACNUR.</p>" +
                                    "<p style=\"text-align:justify\">Este reporte ha sido generado automaticamente el por el sistema OSMOSYS. En caso de dudas por favor comunicarse con la Unidad de Programas o Gestión de Información .</p>";

                    LOGGER.info("reporte retrasos: " + responsable.getName() + ":" + responsable.getOffice() != null ? responsable.getOffice().getAcronym() : "oficina no asignada");
                    String copyAddress = "ecuquosmosys@unhcr.org";
                    String destinationAdress = responsable.getEmail();
                    LOGGER.info(destinationAdress);
                    this.emailService.sendEmailMessageWithAttachment(destinationAdress, copyAddress,
                            "Reporte de retrasos " + responsable.getName() + ":" +
                                    (responsable.getOffice() != null ? responsable.getOffice().getAcronym() : ""),
                            message, report, "Reporte_retrasos_di.xlsx");
                } else {
                    LOGGER.info("No enviado por no retrasos colega DI: ");
                    LOGGER.info("reporte retrasos DI: " + responsable.getName() + ":" + responsable.getOffice() != null ? responsable.getOffice().getAcronym() : "oficina no asignada");

                }
            } catch (Exception e) {
                LOGGER.error("Error al generar report de retrasos de colega di : ");
                LOGGER.error("reporte retrasos socio: " + responsable.getName() + ":" + responsable.getOffice() != null ? responsable.getOffice().getAcronym() : "oficina no asignada");
                LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
            }
        }


    }

    public void sendAlertReviewToDirectImplementation() {
        Period currentPeriod = this.periodService.getByYear(this.utilsService.getCurrentYear());
        List<User> supervisors = this.userService.getActiveSupervisorsDirectImplementationUsers(currentPeriod.getId());

        for (User supervisor : supervisors) {
            try {
                ByteArrayOutputStream report = this.reportService.getDirectImplementationLateReviewReportBySupervisorId(supervisor.getId());

                if (report != null) {

                    String message =
                            "<p style=\"text-align:justify\">Estimado/a colega:</p>" +
                                    "<p style=\"text-align:justify\">Encuentre adjunto el reporte de indicadores con retraso en la validación de indicadores en OSMOSYS para los que usted es supervisor." +
                                    " Rogamos su ayuda para poner al d&iacute;a los datos de su oficina en el sistema OSMOSYS-ACNUR.</p>" +
                                    "<p style=\"text-align:justify\">Este reporte ha sido generado automaticamente el por el sistema OSMOSYS. En caso de dudas por favor comunicarse con la Unidad de Programas o Gestión de Información .</p>";

                    LOGGER.info("reporte retrasos: " + supervisor.getName() + ":" + supervisor.getOffice() != null ? supervisor.getOffice().getAcronym() : "oficina no asignada");
                    String copyAddress = "ecuquosmosys@unhcr.org";
                    String destinationAdress = supervisor.getEmail();
                    LOGGER.info(destinationAdress);
                    this.emailService.sendEmailMessageWithAttachment(destinationAdress, copyAddress,
                            "Reporte de retrasos validación " + supervisor.getName() + ":" +
                                    (supervisor.getOffice() != null ? supervisor.getOffice().getAcronym() : ""),
                            message, report, "Reporte_retrasos_validacion_di.xlsx");
                } else {
                    LOGGER.info("No enviado por no revision colega DI: ");
                    LOGGER.info("reporte revision DI: " + supervisor.getName() + ":" + supervisor.getOffice() != null ? supervisor.getOffice().getAcronym() : "oficina no asignada");

                }
            } catch (Exception e) {
                LOGGER.error("Error al generar report de revision de colega di : ");
                LOGGER.error("reporte revision socio: " + supervisor.getName() + ":" + supervisor.getOffice() != null ? supervisor.getOffice().getAcronym() : "oficina no asignada");
                LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
            }
        }


    }
}
