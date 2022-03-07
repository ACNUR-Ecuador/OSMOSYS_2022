package com.sagatechs.generics.webservice.service;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.utils.DateUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.Project;
import org.unhcr.osmosys.model.enums.AreaType;
import org.unhcr.osmosys.reports.service.ReportService;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.services.PeriodService;
import org.unhcr.osmosys.services.ProjectService;

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
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Properties;


@SuppressWarnings("ALL")
@Path("test")
public class TestEndpoint {
    private static final Logger LOGGER = Logger.getLogger(TestEndpoint.class);

    @Inject
    UserService userService;

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    PeriodService periodService;

    @Inject
    ProjectService projectService;

    @Inject
    ReportService reportService;

    @Inject
    DateUtils dateUtils;


    @Path("test")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String test2() throws GeneralAppException {
        this.reportService.indicatorsCatalogByPeriodId(1L);
        return "ya";
    }

    @Path("createGenerals")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String createGenerals() throws GeneralAppException {
        this.projectService.createProjectGeneralStatements(1l);
        return "ya";
    }

    @Path("createperformanceindicatorsPartners")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String createperformanceindicators() throws GeneralAppException {
        this.indicatorExecutionService.createIndicatorExecForProjects(1l);
        return "ya";
    }

    @Path("createperformanceindicatorsID")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String createperformanceindicatorsID() throws GeneralAppException {
        this.indicatorExecutionService.createIndicatorExecForID(1l);
        return "ya";
    }

    @Path("testenum")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AreaType[] test3() throws GeneralAppException {
        return AreaType.values();
    }

    @Path("setPass/{username}")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String setPass(@PathParam("username") String username) throws GeneralAppException {
        LOGGER.error(username);
        this.userService.changePasswordTest(username, "1234");
        return "ya!!!";
    }

    @Path("testYearQuarter")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public void testYearQuarter() throws GeneralAppException {
        LocalDate starDate = LocalDate.of(2022, Month.APRIL, 1);
        LocalDate endDate = LocalDate.of(2023, Month.APRIL, 30);

        Project p = new Project();
        p.setEndDate(endDate);
        p.setStartDate(starDate);
        p.setCode("test123");
        p.setName("test123");
        p.setState(State.ACTIVO);
        p.setPeriod(this.periodService.find(1L));
        this.indicatorExecutionService.createGeneralIndicatorForProject(p);


    }

    @Path("testreport")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public void testReport() throws GeneralAppException {
        this.reportService.indicatorExecutionsToLateProjectsReportsByPeriodYear(1L);
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
        final String TO = "godoya@unhcr.org";

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
}


