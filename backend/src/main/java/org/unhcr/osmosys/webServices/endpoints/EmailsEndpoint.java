package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.scheduledTasks.MessageAlertServiceV2;
import org.unhcr.osmosys.services.scheduledTasks.MessageReminderService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/email")
@RequestScoped
public class EmailsEndpoint {

    @Inject
    MessageReminderService messageReminderService;

    @Inject
    MessageAlertServiceV2 messageAlertServiceV2;

    @Path("/recordatoryPartners")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void recordatoryPartners() throws GeneralAppException {
        this.messageReminderService.sendPartnersRemindersToFocalPoints();
    }

    @Path("/recordatoryId")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void recordatoryId() throws GeneralAppException {
        this.messageReminderService.sendDirectImplementationReminders();
    }

    @Path("/recordatoryResultManagers")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void recordatoryResultManagers() throws GeneralAppException {
        this.messageReminderService.sendResultsManagerReminders();
    }

    @Path("/alertPartners")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void alertPartners() throws GeneralAppException {
        this.messageAlertServiceV2.sendPartnersAlertsToPartners();
    }

    @Path("/alertsId")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void alertsId() throws GeneralAppException {
        this.messageAlertServiceV2.sendDirectImplementationAlertsToSupervisors();
    }

    @Path("/alertProjectManagers")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void alertProjectManagers() throws GeneralAppException {
        this.messageAlertServiceV2.sendPartnersAlertsToFocalPoints();
    }

    @Path("/alertResultManagers")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void alertResultManagers() throws GeneralAppException {
        this.messageAlertServiceV2.sendIndicatorAlertsToResultManagers();
    }


}
