package com.sagatechs.generics.service;

import org.jboss.logging.Logger;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.concurrent.Future;

@Stateless
public class AsyncService {

    private final static Logger LOGGER = Logger.getLogger(AsyncService.class);

    @Inject
    EmailService emailService;

    @Asynchronous
    public Future sendEmail(String destinationAdress, String destinationCopyAdress, String subject, String messageText) {
        this.emailService.sendEmailMessage(destinationAdress, destinationCopyAdress, subject, messageText);
        return new AsyncResult<>("Hello, " +  "!");
    }

}



