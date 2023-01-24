package org.unhcr.osmosys.services.scheduledTasks;

import com.sagatechs.generics.appConfiguration.AppConfigurationService;
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
import java.util.concurrent.ScheduledFuture;

@Startup
@Singleton
@DependsOn("AppConfigurationService")
public class ScheduledTasksTriger {
    private final static Logger LOGGER = Logger.getLogger(ScheduledTasksTriger.class);
    @Resource
    ManagedScheduledExecutorService scheduler;
    @Inject
    MyService myService;

    @Inject
    AppConfigurationService appConfigurationService;

    @PostConstruct
    public void init() {
        Integer limitreport = this.appConfigurationService.getReportLimitDay();
        LOGGER.debug("ccccccccccccccccccccccccccccc" + limitreport);

        // this.scheduler.scheduleAtFixedRate(this::run, 500, 500, TimeUnit.MILLISECONDS);
        Trigger trigg = new Trigger() {
            @Override
            public Date getNextRunTime(LastExecution lastExecutionInfo, Date taskScheduledTime) {
                LOGGER.info("1");
                Calendar now = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                LOGGER.info(formatter.format(now.getTime()));
                if(now.get(Calendar.DAY_OF_MONTH)>limitreport){
                    now.add(Calendar.MONTH,1);
                }
                now.set(Calendar.DAY_OF_MONTH,limitreport+1);
                now.set(Calendar.HOUR_OF_DAY,23);
                now.set(Calendar.MINUTE,34);
                now.set(Calendar.SECOND,30);

                LOGGER.info(formatter.format(now.getTime()));
                return now.getTime();

            }

            @Override
            public boolean skipRun(LastExecution lastExecutionInfo, Date scheduledRunTime) {
                return false;
            }
        };
        ScheduledFuture<?> f = this.scheduler.schedule(this::run, trigg);

        LOGGER.info(f.isDone());
    }

    public void run() {

        myService.processSomething();
    }
}
