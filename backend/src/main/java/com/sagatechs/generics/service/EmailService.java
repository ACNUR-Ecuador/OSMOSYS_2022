package com.sagatechs.generics.service;

import com.sagatechs.generics.appConfiguration.AppConfigurationKey;
import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@ApplicationScoped
@LocalBean
public class EmailService {

    private final static Logger LOGGER = Logger.getLogger(EmailService.class);

    @Inject
    AppConfigurationService appConfigurationService;


    private Session session;


    private String mailUsername;
    private String mailPassword;
    private String adminEmailAdress;


    @PostConstruct
    public void init() {
        try {
            Properties prop = new Properties();


            prop.put("mail.smtp.host", this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_SMTP_HOST));
            prop.put("mail.smtp.port", this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_SMTP_PORT));
            prop.put("mail.smtp.auth", this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_SMTP));
            prop.put("mail.smtp.socketFactory.port", this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_SMTP_PORT));
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mailUsername = this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_USERNAME);
            mailPassword = this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_PASSOWRD);
            adminEmailAdress = this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_ADDRES);

            session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailUsername, mailPassword);
                }
            });
        } catch (Exception e) {
            LOGGER.error("Error en la configuración de la cuenta de correo electrónico del administrador.");
        }
    }

    public void sendEmailMessage(String destinationAdress, String destinationCopyAdress, String subject, String messageText) {
        if (StringUtils.trimToEmpty(System.getProperty("os.name")).toLowerCase().contains("windows") ||
                StringUtils.trimToEmpty(System.getProperty("os.name")).toLowerCase().contains("mac")) {
            destinationAdress = "gancino@unhcr.org";
            destinationCopyAdress = null;
        }
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(adminEmailAdress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationAdress));
            if (StringUtils.isNotBlank(destinationCopyAdress)) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(destinationCopyAdress));
            }
            message.setSubject(subject);
            message.setReplyTo(new javax.mail.Address[]
                    {
                            new javax.mail.internet.InternetAddress("gancino@unhcr.org")
                    });


            // MimeBodyPart mimeBodyPart = new MimeBodyPart();

            message.setContent(emailGenericTemplate(messageText), "text/html; charset=UTF-8");
            Transport.send(message);
            LOGGER.debug("----------------enviado");

        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }

    }
    public void sendEmailMessageWithAttachment(String destinationAdress, String destinationCopyAdress, String subject, String messageText, ByteArrayOutputStream attachment, String filename) {
        try {

            if(StringUtils.trimToEmpty(System.getProperty("os.name")).contains("windows") || StringUtils.trimToEmpty(System.getProperty("os.name")).contains("Windows")){
                destinationAdress="gancino@unhcr.org";
                destinationCopyAdress=null;
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(adminEmailAdress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationAdress));
            if (StringUtils.isNotBlank(destinationCopyAdress)) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(destinationCopyAdress));
            }
            message.setSubject(subject);
            message.setReplyTo(new javax.mail.Address[]
                    {
                            new javax.mail.internet.InternetAddress("gancino@unhcr.org")
                    });


            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            messageText= new String(messageText.getBytes(StandardCharsets.UTF_8),StandardCharsets.UTF_8);
            mimeBodyPart.setText(emailGenericTemplate(messageText));
            mimeBodyPart.setHeader("Content-Type","text/html");

            //message.setContent(messageText, "text/html; charset=UTF-8");

            DataSource aAttachment = new  ByteArrayDataSource(attachment.toByteArray(),"application/octet-stream");
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(aAttachment));
            attachmentPart.setHeader("Content-Type","text/plain; charset=\"utf-8\"; name=\""+filename+"\""); // Rewrite Header
            attachmentPart.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
            attachmentPart.setHeader("Content-Transfer-Encoding","base64");
            // Join parts
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);

            Transport.send(message);
            LOGGER.debug("----------------enviado");

        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }

    }

    @SuppressWarnings("unused")
    protected String readEmailFromHtml(String filePath, Map<String, String> input) {
        String msg = readContentFromFile(filePath);
        try {
            Set<Map.Entry<String, String>> entries = input.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
            }
        } catch (Exception exception) {
            LOGGER.error(ExceptionUtils.getStackTrace(exception));
        }
        return msg;
    }

    //Method to read HTML file as a String
    private String readContentFromFile(String fileName) {
        @SuppressWarnings("StringBufferMayBeStringBuilder")
        StringBuffer contents = new StringBuffer();

        try {
            //use buffering, reading one line at a time
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        contents.append(line);
                        contents.append(System.getProperty("line.separator"));
                    }
                } finally {
                    reader.close();
                }
            }
        } catch (IOException ex) {
            LOGGER.error(ExceptionUtils.getStackTrace(ex));
        }
        return contents.toString();
    }
    private String emailGenericTemplate(String messageTextContent) {
        String operationUrl= this.appConfigurationService.findValorByClave(AppConfigurationKey.APP_URL);
        return
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Notificación de Retraso en Indicadores</title>\n" +
                        "</head>\n" +
                        "<body style=\"margin: 0; padding: 0; background-color: #f4f4f4;\">\n" +
                        "    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"background-color: #f4f4f4; padding: 20px;\">\n" +
                        "        <tr>\n" +
                        "            <td align=\"center\">\n" +
                        "                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"600\" style=\"background-color: #ffffff; width: 100%; max-width: 600px;\">\n" +
                        "                    <!-- Header -->\n" +
                        "                    <tr>\n" +
                        "                        <td align=\"center\" style=\"padding: 20px; border-bottom: 2px solid #1D72BC;\">\n" +
                        "                            <img src=\"https://osmosys.unhcr.org/mex/assets/layout/images/logo_osmosys_blue.png\" alt=\"OSMOSYS Logo\" width=\"200\" style=\"display: block;\">\n" +
                        "                        </td>\n" +
                        "                    </tr>\n" +
                        "                    <!-- Content -->\n" +
                        "                    <tr>\n" +
                        "                        <td style=\"padding: 20px; font-family: Arial, sans-serif; font-size: 16px; color: #333; line-height: 1.5;\">\n" +
                        "                            "+messageTextContent +
                        "                        </td>\n" +
                        "                    </tr>\n" +
                        "                    <!-- Button -->\n" +
                        "                    <tr>\n" +
                        "                        <td align=\"center\" style=\"padding-bottom: 20px;\">\n" +
                        "                            <a href="+operationUrl+" target=\"_blank\" style=\"background-color: #1D72BC; color: #ffffff; text-decoration: none; font-weight: bold; padding: 12px 20px; display: inline-block; border-radius: 5px;\">Ir a OSMOSYS</a>\n" +
                        "                        </td>\n" +
                        "                    </tr>\n" +
                        "                    <!-- Footer -->\n" +
                        "                    <tr>\n" +
                        "                        <td align=\"center\" style=\"font-family: Arial, sans-serif; font-size: 12px; color: #777; padding: 10px; border-top: 1px solid #ddd;\">\n" +
                        "                            <p>© UNHCR - Hecho en El Salvador y Ecuador - 2025</p>\n" +
                        "                        </td>\n" +
                        "                    </tr>\n" +
                        "                </table>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "</body>\n" +
                        "</html>"
                ;
    }
}



