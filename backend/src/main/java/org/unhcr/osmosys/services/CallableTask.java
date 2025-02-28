package org.unhcr.osmosys.services;

import com.sagatechs.generics.security.filters.AuthenticationBasicFilter;
import org.jboss.logging.Logger;

import java.util.concurrent.Callable;

public class CallableTask implements Callable<Long> {

        private static final Logger LOGGER = Logger.getLogger(AuthenticationBasicFilter.class);
        private int id;
        public CallableTask(int id) {
                this.id = id;
        }
        public Long call() {
                System.out.println("inicio");
                System.out.println("inicio"+this.id);
                long summation = 0;
                for (int i = 1; i <= id; i++) {
                        summation += i;
                        try {
                                Thread.sleep(100);
                        } catch (InterruptedException e) {
                              LOGGER.error("Error en CallableTask");
                        }
                }
                System.out.println("result"+summation);
                return summation;
        }
}