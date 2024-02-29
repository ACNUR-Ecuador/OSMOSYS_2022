package org.unhcr.osmosys.services;

import com.sagatechs.generics.webservice.service.TestEndpoint;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Stateless
public class TestService {

    @Resource(name = "DefaultManagedExecutorService")
    ManagedExecutorService executor;

    private static final Logger LOGGER = Logger.getLogger(TestService.class);
    public long tester() throws ExecutionException, InterruptedException {
        Future<Long> futureResult = executor.submit(new CallableTask(10));
        Future<Long> futureResult1 = executor.submit(new CallableTask(10));
        Future<Long> futureResult2 = executor.submit(new CallableTask(20));
        while (!futureResult.isDone() && !futureResult1.isDone() && !futureResult2.isDone()) {
            // Wait
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.error(ExceptionUtils.getStackTrace(e));
            }
            LOGGER.info("donw"+futureResult.isDone());
            LOGGER.info("donw"+futureResult1.isDone());
            LOGGER.info("donw"+futureResult2.isDone());
        }
        LOGGER.info("d: "+futureResult.get());
        LOGGER.info("d1: "+futureResult1.get());
        LOGGER.info("d2: "+futureResult2.get());

        return futureResult.get();
    }

}
