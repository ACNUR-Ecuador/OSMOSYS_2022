package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.MonthService;
import org.unhcr.osmosys.webServices.model.MonthValuesWeb;
import org.unhcr.osmosys.webServices.model.MonthWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/months")
@RequestScoped
public class MonthEndpoint {

    @Inject
    MonthService monthService;


    @Path("/getMonthIndicatorValueByMonthId/{monthId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public MonthValuesWeb getMonthIndicatorValueByMonthId(@PathParam("monthId") Long monthId) {
        return this.monthService.getMonthValuesWeb(monthId, State.ACTIVO);
    }

    @Path("/getMonthsByIndicatorExecutionId/{indicatorExecutionId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MonthWeb> getMonthsIndicatorExecutionId(@PathParam("indicatorExecutionId") Long indicatorExecutionId) {
        return this.monthService.getMonthsIndicatorExecutionId(indicatorExecutionId, State.ACTIVO);
    }
}
