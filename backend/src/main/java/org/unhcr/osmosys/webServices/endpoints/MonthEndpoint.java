package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.MonthService;
import org.unhcr.osmosys.webServices.model.MonthValuesWeb;
import org.unhcr.osmosys.webServices.model.MonthWeb;
import org.unhcr.osmosys.webServices.model.YearMonthDTO;

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

    @Path("/changeBlockedState/{monthId}/{blockinState}")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long changeMonthBlockedState(
            @PathParam("monthId") Long monthId,
            @PathParam("blockinState") Boolean blockinState
    ) {
        return this.monthService.changeMonthBlockedState(monthId, blockinState);
    }

    @Path("/massiveBlock")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void massiveBlock(
            YearMonthDTO yearMonthDTO) throws GeneralAppException {
        this.monthService.blockMonthsByYearAndoMonth(yearMonthDTO.getYear(), yearMonthDTO.getMonth(), true);
    }

    @Path("/massiveUnblock")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public void massiveUnblock(
            YearMonthDTO yearMonthDTO) throws GeneralAppException {
        this.monthService.blockMonthsByYearAndoMonth(yearMonthDTO.getYear(), yearMonthDTO.getMonth(), false);
    }

    @Path("/getYearMonthByPeriodId/{periodId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<YearMonthDTO> getYearMonthByPeriodId(@PathParam("periodId") Long periodId) {
        return this.monthService.getYearMonthDTOSByPeriodId(periodId);
    }

}
