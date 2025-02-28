package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.ResultManagerIndicatorDao;
import org.unhcr.osmosys.model.ResultManagerIndicator;
import org.unhcr.osmosys.webServices.model.ResultManagerIndicatorDTO;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Stateless
public class ResultManagerIndicatorService {
    @Inject
    ResultManagerIndicatorDao resultManagerIndicatorDao;
    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(ResultManagerIndicatorService.class);

    public ResultManagerIndicator saveOrUpdate(ResultManagerIndicator resultManagerIndicator) {
        if (resultManagerIndicator.getId() == null) {
            this.resultManagerIndicatorDao.save(resultManagerIndicator);
        } else {
            this.resultManagerIndicatorDao.update(resultManagerIndicator);
        }
        return resultManagerIndicator;
    }

    public Long save(ResultManagerIndicatorDTO resultManagerIndicatorDto) throws GeneralAppException {
        if (resultManagerIndicatorDto == null) {
            throw new GeneralAppException("No se puede guardar un result manager indicator null", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorDto.getId() != null) {
            throw new GeneralAppException("No se puede crear un result manager indicator con id", Response.Status.BAD_REQUEST);
        }
        this.validate(resultManagerIndicatorDto);
        ResultManagerIndicator resultManagerIndicator = this.modelWebTransformationService.resultManagerDtoToResultManager(resultManagerIndicatorDto);
        this.saveOrUpdate(resultManagerIndicator);
        return resultManagerIndicator.getId();
    }

    public Long update(ResultManagerIndicatorDTO resultManagerIndicatorDto) throws GeneralAppException {
        if (resultManagerIndicatorDto == null) {
            throw new GeneralAppException("No se puede actualizar un area null", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorDto.getId() == null) {
            throw new GeneralAppException("No se puede crear un area sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(resultManagerIndicatorDto);
        ResultManagerIndicator resultManagerIndicator = this.modelWebTransformationService.resultManagerDtoToResultManager(resultManagerIndicatorDto);
        this.saveOrUpdate(resultManagerIndicator);
        return resultManagerIndicator.getId();
    }


    public void validate(ResultManagerIndicatorDTO resultManagerIndicatorDto) throws GeneralAppException {
        if (resultManagerIndicatorDto == null) {
            throw new GeneralAppException("Indicador no válido", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorDto.getIndicator() == null) {
            throw new GeneralAppException("Indicador no válido", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorDto.getQuarterYearOrder() < 1 && resultManagerIndicatorDto.getQuarterYearOrder() > 5) {
            throw new GeneralAppException("Trimestre no válido", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorDto.getPopulationType() == null) {
            throw new GeneralAppException("Tipo de población no válido", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorDto.getReportValue() != null && resultManagerIndicatorDto.getReportValue()<0 ) {
            throw new GeneralAppException("Valor de reporte no válido", Response.Status.BAD_REQUEST);
        }
        if (resultManagerIndicatorDto.getPeriod() == null) {
            throw new GeneralAppException("Periodo no válido", Response.Status.BAD_REQUEST);
        }
    }






}
