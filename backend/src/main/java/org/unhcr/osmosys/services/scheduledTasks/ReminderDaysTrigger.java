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

        Integer rmReminderDay = this.appConfigurationService.getResultManagerReminderDay();

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
        // Trigger trimestral para result Managers
        Trigger quarterlyTrigger = new Trigger() {
            @Override
            public Date getNextRunTime(LastExecution lastExecutionInfo, Date taskScheduledTime) {
                LOGGER.info("QuarterlyTrigger");

                // Obtenemos el año actual
                Calendar now = Calendar.getInstance();
                int currentMonth = now.get(Calendar.MONTH); // El mes actual (0-11)

                // Calculamos el primer día del siguiente trimestre
                Calendar nextQuarter = Calendar.getInstance();
                if (currentMonth < 3) {
                    // Primer trimestre (enero-marzo)
                    nextQuarter.set(now.get(Calendar.YEAR), 3, rmReminderDay, 6, 0, 0); // X de abril
                } else if (currentMonth < 6) {
                    // Segundo trimestre (abril-junio)
                    nextQuarter.set(now.get(Calendar.YEAR), 6, rmReminderDay, 6, 0, 0); // X de julio
                } else if (currentMonth < 9) {
                    // Tercer trimestre (julio-septiembre)
                    nextQuarter.set(now.get(Calendar.YEAR), 9, rmReminderDay, 6, 0, 0); // X de octubre
                } else {
                    // Cuarto trimestre (octubre-diciembre)
                    nextQuarter.set(now.get(Calendar.YEAR) + 1, 0, rmReminderDay, 6, 0, 0); // X de enero del siguiente año
                }

                LOGGER.info("Next quarterly run time: " + nextQuarter.getTime());
                return nextQuarter.getTime();
            }

            @Override
            public boolean skipRun(LastExecution lastExecutionInfo, Date scheduledRunTime) {
                return false;
            }
        };

        // Programar el trigger trimestral para ejecutar la tarea trimestral
        ScheduledFuture<?> quarterlyScheduled = this.scheduler.schedule(this::runQuarterlyTask, quarterlyTrigger);
        LOGGER.info(quarterlyScheduled.isDone());


    }

    public void run() {
        boolean sendEmailsEnabled = appConfigurationService.getSendEmailReminders();
        if (sendEmailsEnabled) {
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
        }else{
            LOGGER.info("Envío de correos de recordatorio deshabilitado según configuración.");
        }
    }
    public void runQuarterlyTask() {
        boolean sendEmailsEnabled = appConfigurationService.getSendEmailReminders();
        if (sendEmailsEnabled) {
            try {
                // Lógica de la tarea trimestral que se ejecutará cada trimestre
                this.messageReminderService.sendResultsManagerReminders();
            } catch (Exception e) {
                LOGGER.error("Error en envío de recordatorio trimestral");
                LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
                LOGGER.error(ExceptionUtils.getStackTrace(e));
            }

            LOGGER.info("Envío de recordatorio trimestral - fin");
        }else{
            LOGGER.info("Envío de correos de recordatorio deshabilitado según configuración.");
        }

    }

}
