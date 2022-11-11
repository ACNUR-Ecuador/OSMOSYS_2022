package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.enums.Frecuency;
import org.unhcr.osmosys.model.enums.TimeStateEnum;
import org.unhcr.osmosys.model.enums.TotalIndicatorCalculationType;
import org.unhcr.osmosys.webServices.model.MonthWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Stateless
public class UtilsService {

    @Inject
    DateUtils dateUtils;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(UtilsService.class);

    public BigDecimal calculetTotalExecution(TotalIndicatorCalculationType totalIndicatorCalculationType, List<BigDecimal> values) throws GeneralAppException {
        BigDecimal totalExecution;
        if (CollectionUtils.isEmpty(values)) {
            return null;

        } else {
            //noinspection DuplicatedCode
            switch (totalIndicatorCalculationType) {
                //TODO revisar otros valores desde mes
                case SUMA:
                    totalExecution = values.stream().reduce(BigDecimal::add).orElse(null);
                    break;
                case PROMEDIO:
                    BigDecimal total = values.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                    totalExecution = total.divide(new BigDecimal(values.size()), RoundingMode.HALF_UP);
                    break;
                case MAXIMO:
                    totalExecution = values.stream().reduce(BigDecimal::max).orElse(null);
                    break;
                case MINIMO:
                    totalExecution = values.stream().reduce(BigDecimal::min).orElse(null);
                    break;
                default:
                    throw new GeneralAppException("Tipo de calculo no soportado, por favor comuniquese con el administrador del sistema", Response.Status.INTERNAL_SERVER_ERROR);
            }
            return totalExecution;
        }
    }

    public Integer getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public Integer getCurrentMonthYearOrder(){
        return  Calendar.getInstance().get(Calendar.MONTH)+1;
    }

    public LocalDate getFirstDayReport(MonthWeb month, Frecuency frecuency) throws GeneralAppException {
        switch (frecuency) {
            case MENSUAL:
                return LocalDate.of(month.getYear(), month.getMonth().getOrder(), 1).plusMonths(1);
            case TRIMESTRAL:
                LocalDate monthFirstDayMonth = LocalDate.of(month.getYear(), month.getMonth().getOrder(), 1);
                return monthFirstDayMonth
                        .with(monthFirstDayMonth.getMonth().firstMonthOfQuarter().plus(2))
                        .plusMonths(1);
            case SEMESTRAL:
                LocalDate monthFirstDayMonthS = LocalDate.of(month.getYear(), month.getMonth().getOrder(), 1);
                return monthFirstDayMonthS
                        .with(monthFirstDayMonthS.getMonth().getValue() <= 6 ? java.time.Month.of(6) : java.time.Month.of(12))
                        .plusMonths(1);
            case ANUAL:
                return LocalDate.of(month.getYear(), 12, 1).plusMonths(1);
        }

        throw new GeneralAppException("Frecuecia no implementada", Response.Status.INTERNAL_SERVER_ERROR);
    }

    public TimeStateEnum getLateStateForMonth(MonthWeb month, Frecuency frecuency, int limitDayReport) throws GeneralAppException {
        LocalDate today = LocalDate.now();
        LocalDate firstDayReport = this.getFirstDayReport(month, frecuency);
        LocalDate lastDayReport = firstDayReport.withDayOfMonth(limitDayReport);
        if (dateUtils.checkBetweenInclusive(today, firstDayReport, lastDayReport)) {
            if (month.getTotalExecution() != null) {
                return TimeStateEnum.ON_TIME;
            } else {
                return TimeStateEnum.SOON_REPORT;
            }
        } else if (today.isAfter(firstDayReport)) {
            if (month.getTotalExecution() != null) {
                return TimeStateEnum.ON_TIME;
            } else {
                return TimeStateEnum.LATE;
            }
        } else {
            if (month.getTotalExecution() != null) {
                return TimeStateEnum.INVALID;
            } else {
                return TimeStateEnum.NO_TIME;
            }
        }
    }
}
