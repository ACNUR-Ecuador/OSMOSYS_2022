package org.unhcr.osmosys.services.scheduledTasks;

import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import com.sagatechs.generics.exceptions.GeneralAppException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.services.MonthService;

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
import java.util.concurrent.ScheduledFuture;

@Startup
@Singleton
@DependsOn("AppConfigurationService")
public class ReportDayLimitTriger {
    private final static Logger LOGGER = Logger.getLogger(ReportDayLimitTriger.class);
    @Resource
    ManagedScheduledExecutorService scheduler;
    @Inject
    MonthService monthService;

    @Inject
    AppConfigurationService appConfigurationService;

    @Inject
    MessageAlertServiceV2 messageAlertService;


    @PostConstruct
    public void init() {
        Integer limitreport = this.appConfigurationService.getReportLimitDay();
        LOGGER.debug("ReportDayLimitTrigger" + limitreport);

        // this.scheduler.scheduleAtFixedRate(this::run, 500, 500, TimeUnit.MILLISECONDS);
        Trigger trigg = new Trigger() {


            @Override
            public Date getNextRunTime(LastExecution lastExecutionInfo, Date taskScheduledTime) {
                LOGGER.debug("1 ReportDayLimitTrigger");
                LOGGER.debug(lastExecutionInfo);
                LOGGER.debug(taskScheduledTime);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Calendar now = Calendar.getInstance();
                Calendar next = Calendar.getInstance();


                if (lastExecutionInfo == null) {
                    //now.set(Calendar.DAY_OF_MONTH, limitreport);
                    next.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), limitreport + 1, 6, 1, 1);
                    if (next.before(now)) {
                        next.add(Calendar.MONTH, 1);
                    }

                    LOGGER.debug("1 ReportDayLimitTrigger firt times");
                } else {
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTime(lastExecutionInfo.getRunStart());
                    calendar.add(Calendar.MONTH, 1);
                    next = calendar;

                }
                LOGGER.debug("next time" + formatter.format(next.getTime()));

                return next.getTime();

            }

            @Override
            public boolean skipRun(LastExecution lastExecutionInfo, Date scheduledRunTime) {
                return false;
            }
        };
        ScheduledFuture<?> f = this.scheduler.schedule(this::run, trigg);

        LOGGER.debug(f.isDone());
    }

    public void run() {

        try {
            this.monthService.blockMonthsAutomaticaly();
        } catch (GeneralAppException e) {
            LOGGER.error("Error en bloqueo automático de meses");
            LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }

        LOGGER.debug("Envío de alertas socios");
        try {
            messageAlertService.sendPartnersAlertsToFocalPoints();
        } catch (GeneralAppException e) {
            LOGGER.error("ERROR en el envío de alertas a socios");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        try {
            messageAlertService.sendDirectImplementationAlertsToSupervisors();
        } catch (GeneralAppException e) {
            LOGGER.error("ERROR en el envío de alertas a ID");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        LOGGER.debug("Envío de alertas-fin");
    }
}
