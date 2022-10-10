package com.sagatechs.generics.webservice.service;


import com.sagatechs.generics.exceptions.GeneralAppException;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.cubeDTOs.FactDTO;
import org.unhcr.osmosys.services.CubeService;
import org.unhcr.osmosys.services.TestService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("ALL")
@Path("test2")
public class Test2Endpoint {

    private static final Logger LOGGER = Logger.getLogger(TestEndpoint.class);
    private LinkedBlockingQueue<AsyncResponse> responses = new LinkedBlockingQueue<>();

    @Inject
    CubeService cubeService;

    @Inject
    TestService testService;

    @Path("async")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public void lock(@Suspended final AsyncResponse asyncResponse) throws InterruptedException {
        String currentThreadName = getCurrentThreadName();
        LOGGER.info("Locking {0} with thread {1}.");

        asyncResponse.setTimeout(20, TimeUnit.SECONDS);
        asyncResponse.setTimeoutHandler((response) -> {
            responses.remove(response);
            LOGGER.info("inicio");
            List<FactDTO> r = this.cubeService.getFactTablePaginatedByPeriodYear(2022, 100000);
            LOGGER.info("fin: " + r.size());

            response.resume(Response.status(Response.Status.OK).entity(r).build());

        });

        responses.put(asyncResponse);
    }

    private String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }

    @Path("test")
    @GET
    @Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public long test2() throws GeneralAppException, ExecutionException, InterruptedException {
        // this.importService.catalogImport();
        LOGGER.info("inicio");
        List<FactDTO> result = this.cubeService.getFactTablePaginatedByPeriodYearAsync(2022,100000);
        return result.size();
    }
}

