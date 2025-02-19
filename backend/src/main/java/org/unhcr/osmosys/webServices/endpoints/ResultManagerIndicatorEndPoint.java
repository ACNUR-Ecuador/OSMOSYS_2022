package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.security.annotations.Secured;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.services.ProjectService;
import org.unhcr.osmosys.services.ResultManagerIndicatorQuarterReportService;
import org.unhcr.osmosys.services.ResultManagerIndicatorService;
import org.unhcr.osmosys.webServices.model.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/resultManagerIndicators")
@RequestScoped
public class ResultManagerIndicatorEndPoint {
    @Inject
    IndicatorExecutionService indicatorExecutionService;
    @Inject
    ResultManagerIndicatorService resultManagerIndicatorService;
    @Inject
    ResultManagerIndicatorQuarterReportService resultManagerIndicatorQuarterReportService;
    @Path("/{periodId}/{userId}")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ResultManagerIndicatorWeb> getResultManagerIndicators(@PathParam("periodId") Long periodId, @PathParam("userId") Long userId ) throws GeneralAppException {
        return indicatorExecutionService.getResultManagerIndicators(userId, periodId);
    }

    @Path("/")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long create(ResultManagerIndicatorDTO resultManagerIndicatorDTO) throws GeneralAppException {
        return this.resultManagerIndicatorService.save(resultManagerIndicatorDTO);
    }
    @Path("/")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long update(ResultManagerIndicatorDTO resultManagerIndicatorDTO) throws GeneralAppException {
        return this.resultManagerIndicatorService.update(resultManagerIndicatorDTO);
    }

    //quarter-report

    @Path("/quarterReport")
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long createQuarterReport(ResultManagerIndicatorQuarterReportDTO resultManagerIndicatorQuarterReportDTO) throws GeneralAppException {
        return this.resultManagerIndicatorQuarterReportService.save(resultManagerIndicatorQuarterReportDTO);
    }
    @Path("/quarterReport")
    @PUT
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Long updateQuarterReport(ResultManagerIndicatorQuarterReportDTO resultManagerIndicatorQuarterReportDTO) throws GeneralAppException {
        return this.resultManagerIndicatorQuarterReportService.update(resultManagerIndicatorQuarterReportDTO);
    }




}
