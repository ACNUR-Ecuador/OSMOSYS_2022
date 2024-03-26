package com.sagatechs.generics.webservice.service;


import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.utils.DateUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.ReportDao;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.standardDissagregations.options.*;
import org.unhcr.osmosys.model.standardDissagregations.periodOptions.*;
import org.unhcr.osmosys.reports.service.ReportService;
import org.unhcr.osmosys.services.*;
import org.unhcr.osmosys.services.dataImport.ProjectIndicatorsImportService;
import org.unhcr.osmosys.services.dataImport.ProjectsImportService;
import org.unhcr.osmosys.services.dataImport.StatementImportService;
import org.unhcr.osmosys.services.scheduledTasks.MessageReminderService;
import org.unhcr.osmosys.services.standardDissagregations.StandardDissagregationOptionService;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


@SuppressWarnings("ALL")
@Path("test")
public class TestEndpoint {
    private static final Logger LOGGER = Logger.getLogger(TestEndpoint.class);

    @Inject
    UserService userService;
    @Inject
    AppConfigurationService appConfigurationService;

    @Inject
    IndicatorService indicatorService;

    @Inject
    PeriodService periodService;

    @Inject
    ProjectService projectService;

    @Inject
    ReportService reportService;

    @Inject
    ReportDao reportDao;

    @Inject
    DateUtils dateUtils;

    @Inject
    CubeService cubeService;

    @Inject
    MessageReminderService messageReminderService;

    @Inject
    GeneralIndicatorService generalIndicatorService;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    IndicatorExecutionService indicatorExecutionService;


    @Inject
    TesterService testerService;

    @Inject
    StatementImportService statementImportService;

    @Inject
    ProjectsImportService projectsImportService;
    @Inject
    ProjectIndicatorsImportService projectIndicatorsImportService;
    @Inject
    MonthService monthService;

    @Inject
    IndicatorValueService indicatorValueService;

    @Inject
    StandardDissagregationOptionService standardDissagregationOptionService;


    @Path("health")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() throws GeneralAppException {

        return Response.ok().entity("Test Healthy").build();


    }

    @Path("timeTest")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response timeTest() throws GeneralAppException {

        try {
            // Pause the execution for 4 minutes (4 * 60 * 1000 milliseconds)
            Thread.sleep(4 * 60 * 1000);
            System.out.println("Method execution completed after 4 minutes.");
            return Response.ok().entity("Test time ok").build();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
            return Response.ok().entity("Test time fail").build();
        }



    }
    @Path("test")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String test2() throws GeneralAppException {
        // this.importService.catalogImport();
        LOGGER.info("inicio");
        Period period = this.periodService.getWithAllDataById(2L);
        LOGGER.info(period.getPeriodGenderDissagregationOptions());
        LOGGER.info(period.getPeriodPopulationTypeDissagregationOptions());
        LOGGER.info(period.getPeriodGenderDissagregationOptions());
// indicator values creation
/*        Period period = this.periodService.getWithDissagregationOptionsById(2L);


        List<IndicatorValue> r = this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(DissagregationType.DIVERSIDAD_EDAD, null, period);

        for (IndicatorValue indicatorValue : r) {
            LOGGER.info(indicatorValue);
        }*/

        return "result";
    }

    @Path("tester")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> tester() throws GeneralAppException {

        //return this.indicatorValueService.getDissagregationMapIndicatorValuesByMonthId(1772l);

        return null;

    }

    @Path("tester2")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorValueOptionsDTO> tester2() throws GeneralAppException {

        this.indicatorValueService.testerDTOS();
        return null;

    }


    @Path("setPeriod")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String setPeriod() throws GeneralAppException {
        // this.importService.catalogImport();
        LOGGER.info("inicio");
        Period period = this.periodService.find(2L);

        List<PopulationTypeDissagregationOption> options1 = this.standardDissagregationOptionService.getPopulationTypeOptionsByState(State.ACTIVO);
        for (PopulationTypeDissagregationOption populationTypeDissagregationOption : options1) {
            PeriodPopulationTypeDissagregationOption periodPopulationTypeDissagregationOption = new PeriodPopulationTypeDissagregationOption(period, populationTypeDissagregationOption);
            period.addPeriodPopulationTypeDissagregationOption(periodPopulationTypeDissagregationOption);
        }

        List<AgeDissagregationOption> options2 = this.standardDissagregationOptionService.getAgeDissagregationOptionByState(State.ACTIVO);
        for (AgeDissagregationOption options : options2) {
            PeriodAgeDissagregationOption periodOption = new PeriodAgeDissagregationOption(period, options);
            period.addPeriodAgeDissagregationOption(periodOption);
        }

        List<GenderDissagregationOption> options3 = this.standardDissagregationOptionService.getGenderDissagregationOptionByState(State.ACTIVO);
        for (GenderDissagregationOption options : options3) {
            PeriodGenderDissagregationOption periodOption = new PeriodGenderDissagregationOption(period, options);
            period.addPeriodGenderDissagregationOption(periodOption);
        }
        List<DiversityDissagregationOption> options4 = this.standardDissagregationOptionService.getDiversityeOptionsByState(State.ACTIVO);
        for (DiversityDissagregationOption options : options4) {
            PeriodDiversityDissagregationOption periodOption = new PeriodDiversityDissagregationOption(period, options);
            period.addPeriodDiversityDissagregationOption(periodOption);
        }

        List<CountryOfOriginDissagregationOption> options5 = this.standardDissagregationOptionService.getCountryOfOriginDissagregationOptionByState(State.ACTIVO);
        for (CountryOfOriginDissagregationOption options : options5) {
            PeriodCountryOfOriginDissagregationOption periodOption = new PeriodCountryOfOriginDissagregationOption(period, options);
            period.addPeriodCountryOfOriginDissagregationOption(periodOption);
        }
        this.periodService.saveOrUpdate(period);

        return "result";
    }

    @Path("sendAlertToPartners")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String sendAlertToPartners() throws GeneralAppException {
        this.messageReminderService.sendDirectImplementationReminders();
        return "ya";
    }


    @Path("setPass/{username}")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String setPass(@PathParam("username") String username) throws GeneralAppException {
        this.userService.changePasswordTest(username, "1234");
        return "ya!!!";
    }


    @Path("testAmazonEmail")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public void testAmazonEmail() throws GeneralAppException, UnsupportedEncodingException, MessagingException {
        // Replace sender@example.com with your "From" address.
        // This address must be verified.
        final String FROM = "noreply@imecuador.unhcr.org";
        final String FROMNAME = "noreply@imecuador.unhcr.org";

        // Replace recipient@example.com with a "To" address. If your account
        // is still in the sandbox, this address must be verified.
        final String TO = "salazart@unhcr.org";

        // Replace smtp_username with your Amazon SES SMTP user name.

        final String SMTP_USERNAME = "AKIAUDY3XMXWWVOMXJLU";

        // Replace smtp_password with your Amazon SES SMTP password.

        final String SMTP_PASSWORD = "BI5H+kwvGjPLfieVHRZNb0YBw1LBDQ1LikYI9fm0RNzF";

        // The name of the Configuration Set to use for this message.
        // If you comment out or remove this variable, you will also need to
        // comment out or remove the header below.
        // final String CONFIGSET = "ConfigSet";

        // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
        // See https://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
        // for more information.
        final String HOST = "email-smtp.eu-west-1.amazonaws.com";

        // The port you will connect to on the Amazon SES SMTP endpoint.
        final int PORT = 587;

        final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";

        final String BODY = String.join(
                System.getProperty("line.separator"),
                "<h1>Amazon SES SMTP Email Test</h1>",
                "<p>This email was sent by ",
                "OSMOSYS",
                " ."
        );


        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties.
        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information.
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY, "text/html");

        // Add a configuration set header. Comment or delete the
        // next line if you are not using a configuration set
        // msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);

        // Create a transport.
        Transport transport = session.getTransport();

        // Send the message.
        try {
            System.out.println("Sending...");

            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        } finally {
            // Close and terminate the connection.
            transport.close();
        }
    }


    @Path("updateAllPartnersTotals")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String updateAllPartnersTotals() throws GeneralAppException {
        this.indicatorExecutionService.updateAllPartnersTotals(1l);
        return "terimnado generales";
    }

    @Path("updateAllDirectImplementationTotals")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String updateAllDirectImplementationTotals() throws GeneralAppException {
        this.indicatorExecutionService.updateAllDirectImplementationTotals(1l);
        return "terimnado generales";
    }
    @Path("updateAppConf")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String updateAppConf() throws GeneralAppException {
        this.appConfigurationService.llenarAppConfigurationCache();
        return "terimnado generales";
    }


}

