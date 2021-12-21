package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.threeten.extra.YearQuarter;
import org.unhcr.osmosys.daos.QuarterDao;
import org.unhcr.osmosys.model.Canton;
import org.unhcr.osmosys.model.Month;
import org.unhcr.osmosys.model.Quarter;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.enums.QuarterEnum;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class QuarterService {

    @Inject
    QuarterDao quarterDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    MonthService monthService;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(QuarterService.class);

    public Quarter getById(Long id) {
        return this.quarterDao.find(id);
    }

    public Quarter saveOrUpdate(Quarter quarter) {
        if (quarter.getId() == null) {
            this.quarterDao.save(quarter);
        } else {
            this.quarterDao.update(quarter);
        }
        return quarter;
    }

    public List<YearQuarter> calculateQuarter(LocalDate startDate, LocalDate endDate) {
        List<YearQuarter> yqs = new ArrayList<>();
        YearQuarter yqStart = YearQuarter.from(startDate);
        YearQuarter yqStop = YearQuarter.from(endDate);
        YearQuarter yq = yqStart;
        while (yq.isBefore(yqStop.plusQuarters(1))) {  // Using Half-Open approach where the beginning is *inclusive* while the ending is *exclusive*.
            yqs.add(yq);  // Collect this quarter.
            // Set up next loop.
            yq = yq.plusQuarters(1);  // Move to next quarter.
        }

        for (YearQuarter yearQuarter : yqs) {
            LOGGER.error(yearQuarter.getQuarter().toString() + '-' + yearQuarter.getYear());
        }

        return yqs;
    }

    public Quarter createQuarter(YearQuarter yearQuarter, BigDecimal target,
                                 LocalDate startDate, LocalDate endDate,
                                 List<DissagregationType> dissagregationTypes,
                                 List<Canton> cantones
    ) throws GeneralAppException {
        Quarter q = new Quarter();
        q.setYear(yearQuarter.getYear());
        q.setTarget(target);
        q.setOrder(0);
        q.setQuarter(QuarterEnum.getByQuarterNumber(yearQuarter.getQuarterValue()));
        q.setState(State.ACTIVO);
        List<Month> ms = this.monthService.createMonthsForQuarter(q, startDate, endDate,
                dissagregationTypes
                , cantones);
        for (Month month : ms) {
            q.addMonth(month);
        }
        return q;
    }

    public Set<Quarter> createQuarters(LocalDate startDate, LocalDate endDate, List<DissagregationType> dissagregationTypes,
                                       List<Canton> cantones) throws GeneralAppException {
        Set<Quarter> qs = new HashSet<>();
        List<YearQuarter> yearQuarters = this.calculateQuarter(startDate, endDate);
        if (yearQuarters.size() < 1) {
            throw new GeneralAppException("Error al crear los trimestres ", Response.Status.INTERNAL_SERVER_ERROR);
        }


        for (YearQuarter yearQuarter : yearQuarters) {
            Quarter q = this.createQuarter(yearQuarter, BigDecimal.ZERO, startDate, endDate, dissagregationTypes, cantones);
            qs.add(q);
        }
        return qs;

    }

}
