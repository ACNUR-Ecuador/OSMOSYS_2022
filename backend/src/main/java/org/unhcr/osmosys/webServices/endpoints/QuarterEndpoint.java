package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.QuarterService;
import org.unhcr.osmosys.webServices.model.QuarterResumeWeb;
import org.unhcr.osmosys.webServices.model.StartEndDatesWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Path("/quarters")
@RequestScoped
public class QuarterEndpoint {

    @Inject
    QuarterService quarterService;

    @Path("/getEmptyQuarters")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuarterResumeWeb> create(
            StartEndDatesWeb startEndDatesWeb
    ) throws GeneralAppException {
        return this.quarterService.createEmptyQuarters(startEndDatesWeb.getStartDate(), startEndDatesWeb.getEndDate());
    }

}
