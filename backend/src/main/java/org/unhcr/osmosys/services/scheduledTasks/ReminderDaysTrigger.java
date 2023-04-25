package org.unhcr.osmosys.services.scheduledTasks;

import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.LastExecution;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.concurrent.Trigger;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Startup
@Singleton
@DependsOn("AppConfigurationService")
public class ReminderDaysTrigger {
    private final static Logger LOGGER = Logger.getLogger(ReminderDaysTrigger.class);
    @Resource
    ManagedScheduledExecutorService scheduler;

    @Inject
    AppConfigurationService appConfigurationService;

    @Inject
    MessageReminderService messageReminderService;


    @PostConstruct
    public void init() {
        List<Integer> reminderDays = this.appConfigurationService.getReminderDays();
        LOGGER.debug("ReminderDaysTrigger" + reminderDays);

        // this.scheduler.scheduleAtFixedRate(this::run, 500, 500, TimeUnit.MILLISECONDS);
        for (final Integer reminderDay : reminderDays) {
            Trigger trigg = new Trigger() {
                @Override
                public Date getNextRunTime(LastExecution lastExecutionInfo, Date taskScheduledTime) {
                    LOGGER.info("1 ReminderDays");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Calendar now = Calendar.getInstance();
                    Calendar next = Calendar.getInstance();


                    if (lastExecutionInfo == null) {
                        //now.set(Calendar.DAY_OF_MONTH, limitreport);
                        next.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), reminderDay, 6, 1, 1);
                        if(next.before(now)){
                            next.add(Calendar.MONTH, 1);
                        }

                        LOGGER.info("1 ReminderDaysTrigger firt times: " + formatter.format(now.getTime()));
                    } else {

                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.setTime(lastExecutionInfo.getRunStart());
                        LOGGER.debug("Ya existe: anterior: " + formatter.format(calendar.getTime()));
                        // calendar.add(Calendar.MONTH, 1);
                        calendar.add(Calendar.MONTH, 2);
                        LOGGER.debug("Ya existe: " + formatter.format(calendar.getTime()));
                        next = calendar;


                    }
                    LOGGER.info("next time" + formatter.format(next.getTime()));

                    return next.getTime();

                }

                @Override
                public boolean skipRun(LastExecution lastExecutionInfo, Date scheduledRunTime) {
                    return false;
                }
            };
            ScheduledFuture<?> f = this.scheduler.schedule(this::run, trigg);
            LOGGER.info(f.isDone());
        }


    }

    public void run() {

        try {
            this.messageReminderService.sendPartnersRemindersToFocalPoints();
        } catch (Exception e) {
            LOGGER.error("Error en envío de recordatorio de socios");
            LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }


        try {
            this.messageReminderService.sendDirectImplementationReminders();
        } catch (Exception e) {
            LOGGER.error("Error en envío de recordatorio de socios");
            LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }


        LOGGER.info("Envío de recordatorios-fin");
    }
}
