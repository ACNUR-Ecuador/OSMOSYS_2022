package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.ResultManagerIndicatorQuarterReportDao;
import org.unhcr.osmosys.model.ResultManagerIndicator;
import org.unhcr.osmosys.model.ResultManagerIndicatorQuarterReport;
import org.unhcr.osmosys.webServices.model.ResultManagerIndicatorDTO;
import org.unhcr.osmosys.webServices.model.ResultManagerIndicatorQuarterReportDTO;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Stateless
public class ResultManagerIndicatorQuarterReportService {
    @Inject
    ResultManagerIndicatorQuarterReportDao resultManagerIndicatorQuarterReportDao;
    @Inject
    ModelWebTransformationService modelWebTransformationService;

    private final static Logger LOGGER = Logger.getLogger(ResultManagerIndicatorQuarterReportService.class);

    public ResultManagerIndicatorQuarterReport saveOrUpdate(ResultManagerIndicatorQuarterReport resultManagerIndicatorQuarterReport) {
        if (resultManagerIndicatorQuarterReport.getId() == null) {
            this.resultManagerIndicatorQuarterReportDao.save(resultManagerIndicatorQuarterReport);
        } else {
            this.resultManagerIndicatorQuarterReportDao.update(resultManagerIndicatorQuarterReport);
        }
        return resultManagerIndicatorQuarterReport;
    }

    public Long save(ResultManagerIndicatorQuarterReportDTO resultManagerIndicatorQuarterReportDTO) throws GeneralAppException {
        if (resultManagerIndicatorQuarterReportDTO == null) {
            throw new GeneralAppException("No se puede guardar un result manager indicator null", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorQuarterReportDTO.getId() != null) {
            throw new GeneralAppException("No se puede crear un result manager indicator con id", Response.Status.BAD_REQUEST);
        }
        this.validate(resultManagerIndicatorQuarterReportDTO);
        ResultManagerIndicatorQuarterReport resultManagerIndicatorQuarterReport = this.modelWebTransformationService.resultManIndQuarterReportDTOToResultManIndQuarterReport(resultManagerIndicatorQuarterReportDTO);
        this.saveOrUpdate(resultManagerIndicatorQuarterReport);
        return resultManagerIndicatorQuarterReport.getId();
    }

    public Long update(ResultManagerIndicatorQuarterReportDTO resultManagerIndicatorQuarterReportDTO) throws GeneralAppException {
        if (resultManagerIndicatorQuarterReportDTO == null) {
            throw new GeneralAppException("No se puede actualizar un area null", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorQuarterReportDTO.getId() == null) {
            throw new GeneralAppException("No se puede crear un area sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(resultManagerIndicatorQuarterReportDTO);
        ResultManagerIndicatorQuarterReport resultManagerIndicatorQuarterReport = this.modelWebTransformationService.resultManIndQuarterReportDTOToResultManIndQuarterReport(resultManagerIndicatorQuarterReportDTO);
        this.saveOrUpdate(resultManagerIndicatorQuarterReport);
        return resultManagerIndicatorQuarterReport.getId();
    }


    public void validate(ResultManagerIndicatorQuarterReportDTO resultManagerIndicatorQuarterReportDTO) throws GeneralAppException {
        if (resultManagerIndicatorQuarterReportDTO == null) {
            throw new GeneralAppException("Indicador no válido", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorQuarterReportDTO.getIndicator() == null) {
            throw new GeneralAppException("Indicador no válido", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorQuarterReportDTO.getQuarterYearOrder() < 1 && resultManagerIndicatorQuarterReportDTO.getQuarterYearOrder() > 5) {
            throw new GeneralAppException("Trimestre no válido", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorQuarterReportDTO.getPeriod() == null) {
            throw new GeneralAppException("Periodo no válido", Response.Status.BAD_REQUEST);
        }
    }


}
