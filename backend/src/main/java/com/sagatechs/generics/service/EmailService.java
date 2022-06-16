package com.sagatechs.generics.service;

import com.sagatechs.generics.appConfiguration.AppConfigurationKey;
import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
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
import java.util.concurrent.Future;

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
        try {
            //destinationAdress = "salazart@unhcr.org";
            // destinationCopyAdress = null;
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(adminEmailAdress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationAdress));
            if (StringUtils.isNotBlank(destinationCopyAdress)) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(destinationCopyAdress));
            }
            message.setSubject(subject);
            message.setReplyTo(new javax.mail.Address[]
                    {
                            new javax.mail.internet.InternetAddress("ecuquosmosys@unhcr.org")
                    });


            MimeBodyPart mimeBodyPart = new MimeBodyPart();

            message.setContent(messageText, "text/html; charset=UTF-8");
            Transport.send(message);
            LOGGER.debug("----------------enviado");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void sendEmailMessageWithAttachment(String destinationAdress, String destinationCopyAdress, String subject, String messageText, ByteArrayOutputStream attachment, String filename) {
        try {
            //destinationAdress = "salazart@unhcr.org";
            // destinationCopyAdress = null;
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(adminEmailAdress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationAdress));
            if (StringUtils.isNotBlank(destinationCopyAdress)) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(destinationCopyAdress));
            }
            message.setSubject(subject);
            message.setReplyTo(new javax.mail.Address[]
                    {
                            new javax.mail.internet.InternetAddress("ecuquosmosys@unhcr.org")
                    });


            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            messageText= new String(messageText.getBytes(StandardCharsets.UTF_8),StandardCharsets.UTF_8);
            mimeBodyPart.setText(messageText);
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
            e.printStackTrace();
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
            exception.printStackTrace();
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
            ex.printStackTrace();
        }
        return contents.toString();
    }
}



