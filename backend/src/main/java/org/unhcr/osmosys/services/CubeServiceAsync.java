package org.unhcr.osmosys.services;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.CubeDao;
import org.unhcr.osmosys.model.cubeDTOs.*;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Stateless
public class CubeServiceAsync {
    private static final Logger LOGGER = Logger.getLogger(CubeServiceAsync.class);
    @Inject
    CubeDao cubeDao;


    @Asynchronous
    public Future<List<FactDTO>> getFactTablePageByPeriodYearAsync(Integer periodYear, int pageSize, int page) {
        LOGGER.info("page: " + page);
        List<FactDTO> rp = this.cubeDao.getFactTableByPeriodYearPaginated(periodYear, pageSize, page);
        return new AsyncResult<>(rp);
    }

}
