package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.threeten.extra.YearQuarter;
import org.unhcr.osmosys.daos.QuarterDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.enums.QuarterEnum;
import org.unhcr.osmosys.model.enums.TotalIndicatorCalculationType;
import org.unhcr.osmosys.webServices.model.QuarterResumeWeb;
import org.unhcr.osmosys.webServices.model.QuarterWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class QuarterService {

    @Inject
    QuarterDao quarterDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    MonthService monthService;

    @Inject
    DateUtils dateUtils;

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



    public Quarter createQuarter(YearQuarter yearQuarter,
                                 LocalDate startDate, LocalDate endDate,
                                 List<DissagregationType> dissagregationTypes,
                                 List<CustomDissagregation> customDissagregations,
                                 List<Canton> cantones
    ) throws GeneralAppException {
        Quarter q = new Quarter();
        q.setYear(yearQuarter.getYear());
        q.setTarget(null);
        q.setOrder(0);
        q.setQuarter(QuarterEnum.getByQuarterNumber(yearQuarter.getQuarterValue()));
        q.setState(State.ACTIVO);
        List<Month> ms = this.monthService.createMonthsForQuarter(q, startDate, endDate,
                dissagregationTypes, customDissagregations
                , cantones);
        for (Month month : ms) {
            q.addMonth(month);
        }
        return q;
    }


    public Set<Quarter> createQuarters(LocalDate startDate, LocalDate endDate, List<DissagregationType> dissagregationTypes, List<CustomDissagregation> customDissagregations,
                                       List<Canton> cantones) throws GeneralAppException {
        Set<Quarter> qs = new HashSet<>();
        List<YearQuarter> yearQuarters = this.dateUtils.calculateQuarter(startDate, endDate);
        if (yearQuarters.size() < 1) {
            throw new GeneralAppException("Error al crear los trimestres ", Response.Status.INTERNAL_SERVER_ERROR);
        }


        for (YearQuarter yearQuarter : yearQuarters) {
            Quarter q = this.createQuarter(yearQuarter, startDate, endDate, dissagregationTypes, customDissagregations, cantones);
            qs.add(q);
        }
        return qs;

    }

    public void updateQuarterTotals(Quarter quarter, TotalIndicatorCalculationType totalIndicatorCalculationType) throws GeneralAppException {
        for (Month month1 : quarter.getMonths()) {
            this.monthService.updateMonthTotals(month1, totalIndicatorCalculationType);
        }

        List<Month> months = quarter.getMonths().stream().filter(month -> {
            return month.getState().equals(State.ACTIVO);
        }).collect(Collectors.toList());

        List<BigDecimal> totalMonthValues = months.stream().filter(month -> {
            return month.getState().equals(State.ACTIVO);
        }).map(Month::getTotalExecution).filter(Objects::nonNull).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(totalMonthValues)) {
            quarter.setTotalExecution(null);
            quarter.setExecutionPercentage(null);
        } else {
            BigDecimal totalExecution = null;
            switch (totalIndicatorCalculationType) {
                case SUMA:
                    totalExecution = totalMonthValues.stream().reduce(BigDecimal::add).get();
                    break;
                case PROMEDIO:
                    BigDecimal total = totalMonthValues.stream().reduce(BigDecimal::add).get();
                    totalExecution = total.divide(new BigDecimal(totalMonthValues.size()), RoundingMode.HALF_UP);
                    break;
                case MAXIMO:
                    totalExecution = totalMonthValues.stream().reduce(BigDecimal::max).get();
                    break;
                case MINIMO:
                    totalExecution = totalMonthValues.stream().reduce(BigDecimal::min).get();
                    break;
                default:
                    throw new GeneralAppException("Tipo de calculo no soportado, por favor comuniquese con el administrador del sistema", Response.Status.INTERNAL_SERVER_ERROR);
            }
            quarter.setTotalExecution(totalExecution);

            if (quarter.getTotalExecution() != null && quarter.getTarget() != null) {
                if (quarter.getTarget().equals(BigDecimal.ZERO)) {
                    quarter.setTarget(BigDecimal.ZERO);
                } else {
                    quarter.setExecutionPercentage(quarter.getTotalExecution().divide(quarter.getTarget(), 4, RoundingMode.HALF_UP));
                }
            } else {
                quarter.setExecutionPercentage(null);
            }
        }
    }
}
