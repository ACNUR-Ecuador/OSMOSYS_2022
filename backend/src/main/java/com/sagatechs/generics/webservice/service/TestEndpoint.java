package com.sagatechs.generics.webservice.service;


import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.utils.DateUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.ReportDao;
import org.unhcr.osmosys.model.Indicator;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.Project;
import org.unhcr.osmosys.model.cubeDTOs.FactDTO;
import org.unhcr.osmosys.model.enums.AreaType;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.reports.service.MessageAlertService;
import org.unhcr.osmosys.reports.service.ReportService;
import org.unhcr.osmosys.services.*;
import org.unhcr.osmosys.webServices.model.*;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


@SuppressWarnings("ALL")
@Path("test")
public class TestEndpoint {
    private static final Logger LOGGER = Logger.getLogger(TestEndpoint.class);

    @Inject
    UserService userService;

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
    MessageAlertService messageAlertService;

    @Inject
    GeneralIndicatorService generalIndicatorService;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Path("test")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String test2() throws GeneralAppException {
        this.messageAlertService.sendAlertReviewToDirectImplementation();
        return "ya";
    }

    @Path("sendAlertReviewToDirectImplementation")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String sendAlertReviewToDirectImplementation() throws GeneralAppException {
        this.messageAlertService.sendAlertReviewToDirectImplementation();
        return "ya";
    }

    @Path("sendAlertReviewToPartnersFocalPoints")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String sendAlertReviewToPartnersFocalPoints() throws GeneralAppException {
        this.messageAlertService.sendAlertReviewToPartnersFocalPoints();
        return "ya";
    }

    @Path("sendAlertToPartners")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String sendAlertToPartners() throws GeneralAppException {
        this.messageAlertService.sendAlertToPartners();
        return "ya";
    }
    @Path("sendAlertToDirectImplementation")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String sendAlertToDirectImplementation() throws GeneralAppException {
        this.messageAlertService.sendAlertToDirectImplementation();
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
        //this.indicatorExecutionService.createIndicatorExecForProjects(1l);
        return "ya";
    }

    @Path("createperformanceindicatorsID")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String createperformanceindicatorsID() throws GeneralAppException {
        // this.indicatorExecutionService.createIndicatorExecForID(1l);
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
        // this.indicatorExecutionService.createGeneralIndicatorForProject(p);


    }

    @Path("testreport")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response testcube() throws GeneralAppException, IOException {
        // List<IndicatorExecutionDetailedDTO> r = this.reportService.getAllIndicatorExecutionDetailed(1l);

        return null;//Response.ok(r.toByteArray()).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
    }

    @Path("testcube")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<FactDTO> testReport() throws GeneralAppException, IOException {
        return this.cubeService.getFactTableByPeriodYear(2022);
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

    @Path("updatedissagregations")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public void addDissagregations() throws GeneralAppException, UnsupportedEncodingException, MessagingException {
        List<String> codes = new ArrayList<>();
        codes.add("AE011");
        codes.add("AE0I1");
        codes.add("BE021");
        codes.add("BE031");
        codes.add("DE0A1");
        codes.add("DE0A2");
        codes.add("DE0A3");
        codes.add("DE0A4");
        codes.add("DE0A5");
        codes.add("DE0B2");
        codes.add("EE0D1");
        codes.add("EE0D2");
        codes.add("EE0D3");
        codes.add("EE0D4");
        codes.add("EE0D7");
        codes.add("EE0F2");
        codes.add("FE061");
        codes.add("FE062");
        codes.add("FE065");
        codes.add("FE068");
        codes.add("GE0R5");
        codes.add("GE0R6");
        codes.add("HE071");
        codes.add("HE074");
        codes.add("HE081");
        codes.add("HE082");
        codes.add("HE083");
        codes.add("HE084");
        codes.add("HE085");
        codes.add("HE087");
        codes.add("HE091");
        codes.add("HE0X1");
        codes.add("IE0G1");
        codes.add("IE0G5");
        codes.add("IE0G8");
        codes.add("IE0H4");
        codes.add("IE0H7");
        codes.add("IE0H8");
        codes.add("JE0V2");
        codes.add("JE0V4");
        codes.add("KE0O1");
        codes.add("KE0O2");
        codes.add("KE0O3");
        codes.add("KE0O4");
        codes.add("KE0P1");
        codes.add("KE0P3");
        codes.add("ME0K1");
        codes.add("ME0K10");
        codes.add("ME0K13");
        codes.add("ME0K14");
        codes.add("ME0K15");
        codes.add("ME0K2");
        codes.add("ME0K3");
        codes.add("ME0K4");
        codes.add("ME0K7");
        codes.add("ME0K9");
        codes.add("ME0L1");
        codes.add("ME0L2");
        codes.add("ME0L3");
        codes.add("ME0M1");
        codes.add("ME0M2");
        codes.add("ME0M3");
        codes.add("OE131");
        codes.add("OE141");
        codes.add("OE142");
        codes.add("PE051");
        codes.add("PE052");
        codes.add("PE053");

        List<Indicator> indicators = this.indicatorService.getByCodeList(codes);
        Period period = this.periodService.find(1L);
        LOGGER.info(indicators.size());
        for (Indicator indicator : indicators) {
            LOGGER.debug(indicator.getDissagregationsAssignationToIndicator().size());
            this.indicatorService.addDissagregationToIndicator(indicator, period, DissagregationType.GENERO_Y_EDAD);
        }

    }


    @Path("dissableDissagregations")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public void dissableDissagregations() throws GeneralAppException, UnsupportedEncodingException, MessagingException {

        List<String> codes = new ArrayList<>();
        codes.add("AE011");
        codes.add("AE0I1");
        codes.add("BE021");
        codes.add("BE031");
        codes.add("DE0A1");
        codes.add("DE0A2");
        codes.add("DE0A3");
        codes.add("DE0A4");
        codes.add("DE0A5");
        codes.add("DE0B2");
        codes.add("EE0D1");
        codes.add("EE0D2");
        codes.add("EE0D3");
        codes.add("EE0D4");
        codes.add("EE0D7");
        codes.add("EE0F2");
        codes.add("FE061");
        codes.add("FE062");
        codes.add("FE065");
        codes.add("FE068");
        codes.add("GE0R5");
        codes.add("GE0R6");
        codes.add("HE071");
        codes.add("HE074");
        codes.add("HE081");
        codes.add("HE082");
        codes.add("HE083");
        codes.add("HE084");
        codes.add("HE085");
        codes.add("HE087");
        codes.add("HE091");
        codes.add("HE0X1");
        codes.add("IE0G1");
        codes.add("IE0G5");
        codes.add("IE0G8");
        codes.add("IE0H4");
        codes.add("IE0H7");
        codes.add("IE0H8");
        codes.add("JE0V2");
        codes.add("JE0V4");
        codes.add("KE0O1");
        codes.add("KE0O2");
        codes.add("KE0O3");
        codes.add("KE0O4");
        codes.add("KE0P1");
        codes.add("KE0P3");
        codes.add("ME0K1");
        codes.add("ME0K10");
        codes.add("ME0K13");
        codes.add("ME0K14");
        codes.add("ME0K15");
        codes.add("ME0K2");
        codes.add("ME0K3");
        codes.add("ME0K4");
        codes.add("ME0K7");
        codes.add("ME0K9");
        codes.add("ME0L1");
        codes.add("ME0L2");
        codes.add("ME0L3");
        codes.add("ME0M1");
        codes.add("ME0M2");
        codes.add("ME0M3");
        codes.add("OE131");
        codes.add("OE141");
        codes.add("OE142");
        codes.add("PE051");
        codes.add("PE052");
        codes.add("PE053");

        List<Indicator> indicators = this.indicatorService.getByCodeList(codes);
        Period period = this.periodService.find(1L);
        LOGGER.info(indicators.size());
        List<DissagregationType> toDisable = new ArrayList<>();
        toDisable.add(DissagregationType.GENERO);
        toDisable.add(DissagregationType.EDAD);
        for (Indicator indicator : indicators) {
            LOGGER.debug(indicator.getDissagregationsAssignationToIndicator().size());

            this.indicatorService.dissableDissagregationsToIndicator(indicator, period, toDisable);
        }

    }


    @Path("updateGeneralIndicators")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String updateGeneralIndicators() throws GeneralAppException {
        GeneralIndicatorWeb generalIndicators = this.generalIndicatorService.getWebByPeriodId(1l);
        generalIndicators.getDissagregationAssignationsToGeneralIndicator()
                .forEach(dissagregationAssignationToGeneralIndicatorWeb -> {
                    dissagregationAssignationToGeneralIndicatorWeb.setState(State.INACTIVO);
                });
        DissagregationAssignationToGeneralIndicatorWeb dc = new DissagregationAssignationToGeneralIndicatorWeb();
        dc.setState(State.ACTIVO);
        dc.setDissagregationType(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_Y_GENERO);
        generalIndicators.getDissagregationAssignationsToGeneralIndicator().add(dc);
        DissagregationAssignationToGeneralIndicatorWeb dd = new DissagregationAssignationToGeneralIndicatorWeb();
        dd.setState(State.ACTIVO);
        dd.setDissagregationType(DissagregationType.DIVERSIDAD);
        generalIndicators.getDissagregationAssignationsToGeneralIndicator().add(dd);
        DissagregationAssignationToGeneralIndicatorWeb po = new DissagregationAssignationToGeneralIndicatorWeb();
        po.setState(State.ACTIVO);
        po.setDissagregationType(DissagregationType.PAIS_ORIGEN);
        generalIndicators.getDissagregationAssignationsToGeneralIndicator().add(po);
        this.generalIndicatorService.update(generalIndicators);
        return "terminado";
    }

    @Path("updateProductIndicatorsD1")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String updateProductIndicatorsD1() throws GeneralAppException {
        List<String> codes = new ArrayList<>();

        codes.add("AE011");
        codes.add("AE0I1");
        codes.add("BE021");
        codes.add("BE031");
        codes.add("DE0A1");
        codes.add("DE0A2");
        codes.add("DE0A3");
        codes.add("DE0A4");
        codes.add("DE0A5");
        codes.add("DE0B2");
        codes.add("EE0D1");
        codes.add("EE0D2");
        codes.add("EE0D3");
        codes.add("EE0D4");
        codes.add("EE0D7");
        codes.add("EE0F2");
        codes.add("FE061");
        codes.add("FE062");
        codes.add("FE065");
        codes.add("FE068");
        codes.add("GE0R2");
        codes.add("GE0R5");
        codes.add("GE0R6");
        codes.add("HE071");
        codes.add("HE074");
        codes.add("HE081");
        codes.add("HE082");
        codes.add("HE083");
        codes.add("HE084");
        codes.add("HE085");
        codes.add("HE087");
        codes.add("HE091");
        codes.add("HE0X1");
        codes.add("IE0G1");
        codes.add("IE0G5");
        codes.add("IE0G8");
        codes.add("IE0H4");
        codes.add("IE0H7");
        codes.add("IE0H8");
        codes.add("JE0V2");
        codes.add("JE0V4");
        codes.add("KE0O4");
        codes.add("KE0P3");
        codes.add("ME0K1");
        codes.add("ME0K10");
        codes.add("ME0K13");
        codes.add("ME0K14");
        codes.add("ME0K15");
        codes.add("ME0K2");
        codes.add("ME0K3");
        codes.add("ME0K4");
        codes.add("ME0K7");
        codes.add("ME0K9");
        codes.add("ME0L1");
        codes.add("ME0L2");
        codes.add("ME0L3");
        codes.add("OE131");
        codes.add("OE141");
        codes.add("OE142");
        codes.add("PE051");
        codes.add("PE052");
        codes.add("PE053");

        PeriodWeb period = this.modelWebTransformationService.periodToPeriodWeb(this.periodService.find(1L));
        List<Indicator> indicatorsToUpdate = this.indicatorService.getByCodeList(codes);
        List<IndicatorWeb> indicatorsToUpdateWeb = this.modelWebTransformationService.indicatorsToIndicatorsWeb(indicatorsToUpdate, true, true, true);

        for (IndicatorWeb indicatorWeb : indicatorsToUpdateWeb) {
            LOGGER.debug(indicatorWeb.getCode());
            indicatorWeb.getDissagregationsAssignationToIndicator().stream().filter(dissagregationAssignationToIndicatorWeb -> {
                return !dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_Y_GENERO)
                        && !dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.DIVERSIDAD)
                        && !dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.PAIS_ORIGEN);
            }).forEach(dissagregationAssignationToIndicatorWeb -> dissagregationAssignationToIndicatorWeb.setState(State.INACTIVO));

            DissagregationAssignationToIndicatorWeb dd1 = new DissagregationAssignationToIndicatorWeb();
            dd1.setState(State.ACTIVO);
            dd1.setDissagregationType(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_Y_GENERO);
            dd1.setPeriod(period);
            indicatorWeb.getDissagregationsAssignationToIndicator().add(dd1);

            Optional<DissagregationAssignationToIndicatorWeb> ddf = indicatorWeb.getDissagregationsAssignationToIndicator()
                    .stream()
                    .filter(dissagregationAssignationToIndicatorWeb ->
                            dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.DIVERSIDAD))
                    .findFirst();
            if (ddf.isPresent()) {
                ddf.get().setState(State.ACTIVO);
            } else {
                DissagregationAssignationToIndicatorWeb dd = new DissagregationAssignationToIndicatorWeb();
                dd.setState(State.ACTIVO);
                dd.setDissagregationType(DissagregationType.DIVERSIDAD);
                dd.setPeriod(period);
                indicatorWeb.getDissagregationsAssignationToIndicator().add(dd);
            }


            Optional<DissagregationAssignationToIndicatorWeb> dpf = indicatorWeb.getDissagregationsAssignationToIndicator()
                    .stream()
                    .filter(dissagregationAssignationToIndicatorWeb ->
                            dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.PAIS_ORIGEN))
                    .findFirst();
            if (dpf.isPresent()) {
                dpf.get().setState(State.ACTIVO);
            } else {
                DissagregationAssignationToIndicatorWeb dp = new DissagregationAssignationToIndicatorWeb();
                dp.setState(State.ACTIVO);
                dp.setDissagregationType(DissagregationType.PAIS_ORIGEN);
                dp.setPeriod(period);
                indicatorWeb.getDissagregationsAssignationToIndicator().add(dp);
            }

            this.indicatorService.update(indicatorWeb);
        }
        return "terminado";
    }

    @Path("updateProductIndicatorsD2")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String updateProductIndicatorsD2() throws GeneralAppException {
        PeriodWeb period = this.modelWebTransformationService.periodToPeriodWeb(this.periodService.find(1L));
        List<String> codes = new ArrayList<>();
        codes.add("KE0O1");
        codes.add("KE0O2");
        codes.add("KE0O3");
        codes.add("KE0O7");
        codes.add("KE0P1");

        List<Indicator> indicatorsToUpdate = this.indicatorService.getByCodeList(codes);
        List<IndicatorWeb> indicatorsToUpdateWeb = this.modelWebTransformationService.indicatorsToIndicatorsWeb(indicatorsToUpdate, true, true, true);

        for (IndicatorWeb indicatorWeb : indicatorsToUpdateWeb) {
            LOGGER.debug(indicatorWeb.getCode());
            indicatorWeb.getDissagregationsAssignationToIndicator().stream().filter(dissagregationAssignationToIndicatorWeb -> {
                return !dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO)
                        && !dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.DIVERSIDAD_EDAD_Y_GENERO);
            }).forEach(dissagregationAssignationToIndicatorWeb -> dissagregationAssignationToIndicatorWeb.setState(State.INACTIVO));

            DissagregationAssignationToIndicatorWeb dd1 = new DissagregationAssignationToIndicatorWeb();
            dd1.setState(State.ACTIVO);
            dd1.setDissagregationType(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO);
            dd1.setPeriod(period);
            indicatorWeb.getDissagregationsAssignationToIndicator().add(dd1);

            Optional<DissagregationAssignationToIndicatorWeb> ddf = indicatorWeb.getDissagregationsAssignationToIndicator()
                    .stream()
                    .filter(dissagregationAssignationToIndicatorWeb ->
                            dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.DIVERSIDAD_EDAD_Y_GENERO))
                    .findFirst();
            if (ddf.isPresent()) {
                ddf.get().setState(State.ACTIVO);
            } else {
                DissagregationAssignationToIndicatorWeb dd = new DissagregationAssignationToIndicatorWeb();
                dd.setState(State.ACTIVO);
                dd.setDissagregationType(DissagregationType.DIVERSIDAD_EDAD_Y_GENERO);
                dd.setPeriod(period);
                indicatorWeb.getDissagregationsAssignationToIndicator().add(dd);
            }
            this.indicatorService.update(indicatorWeb);
        }
        return "terminado";
    }


    @Path("updateProductIndicatorsD3")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String updateProductIndicatorsD3() throws GeneralAppException {
        List<String> codes = new ArrayList<>();
        codes.add("ME0M1");
        codes.add("ME0M2");
        codes.add("ME0M3");

        PeriodWeb period = this.modelWebTransformationService.periodToPeriodWeb(this.periodService.find(1L));

        List<Indicator> indicatorsToUpdate = this.indicatorService.getByCodeList(codes);
        List<IndicatorWeb> indicatorsToUpdateWeb = this.modelWebTransformationService.indicatorsToIndicatorsWeb(indicatorsToUpdate, true, true, true);

        for (IndicatorWeb indicatorWeb : indicatorsToUpdateWeb) {
            LOGGER.debug(indicatorWeb.getCode());
            indicatorWeb.getDissagregationsAssignationToIndicator().stream().filter(dissagregationAssignationToIndicatorWeb -> {
                return !dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO)
                        && !dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.DIVERSIDAD)
                        && !dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.PAIS_ORIGEN);
            }).forEach(dissagregationAssignationToIndicatorWeb -> dissagregationAssignationToIndicatorWeb.setState(State.INACTIVO));

            DissagregationAssignationToIndicatorWeb dd1 = new DissagregationAssignationToIndicatorWeb();
            dd1.setState(State.ACTIVO);
            dd1.setDissagregationType(DissagregationType.TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO);
            dd1.setPeriod(period);
            indicatorWeb.getDissagregationsAssignationToIndicator().add(dd1);

            Optional<DissagregationAssignationToIndicatorWeb> ddf = indicatorWeb.getDissagregationsAssignationToIndicator()
                    .stream()
                    .filter(dissagregationAssignationToIndicatorWeb ->
                            dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.DIVERSIDAD))
                    .findFirst();
            if (ddf.isPresent()) {
                ddf.get().setState(State.ACTIVO);
            } else {
                DissagregationAssignationToIndicatorWeb dd = new DissagregationAssignationToIndicatorWeb();
                dd.setState(State.ACTIVO);
                dd.setDissagregationType(DissagregationType.DIVERSIDAD);
                dd.setPeriod(period);
                indicatorWeb.getDissagregationsAssignationToIndicator().add(dd);
            }


            Optional<DissagregationAssignationToIndicatorWeb> dpf = indicatorWeb.getDissagregationsAssignationToIndicator()
                    .stream()
                    .filter(dissagregationAssignationToIndicatorWeb ->
                            dissagregationAssignationToIndicatorWeb.getDissagregationType().equals(DissagregationType.PAIS_ORIGEN))
                    .findFirst();
            if (dpf.isPresent()) {
                dpf.get().setState(State.ACTIVO);
            } else {
                DissagregationAssignationToIndicatorWeb dp = new DissagregationAssignationToIndicatorWeb();
                dp.setState(State.ACTIVO);
                dp.setDissagregationType(DissagregationType.PAIS_ORIGEN);
                dp.setPeriod(period);
                indicatorWeb.getDissagregationsAssignationToIndicator().add(dp);
            }

            this.indicatorService.update(indicatorWeb);
        }
        return "terminado";
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

}

