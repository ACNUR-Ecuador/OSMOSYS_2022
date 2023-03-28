package org.unhcr.osmosys.services.scheduledTasks;

import org.jboss.logging.Logger;

import javax.ejb.Stateless;

@Stateless
public class MyService {
    private final static Logger LOGGER = Logger.getLogger(MyService.class);
    public void processSomething() {
        LOGGER.debug("xxxxxxxxx prueba schedule");
    }
}
