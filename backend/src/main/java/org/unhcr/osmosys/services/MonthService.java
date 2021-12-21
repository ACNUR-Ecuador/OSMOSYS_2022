package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.MonthDao;
import org.unhcr.osmosys.model.Canton;
import org.unhcr.osmosys.model.IndicatorValue;
import org.unhcr.osmosys.model.Month;
import org.unhcr.osmosys.model.Quarter;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.model.enums.QuarterEnum;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class MonthService {

    @Inject
    MonthDao monthDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    IndicatorValueService indicatorValueService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(MonthService.class);

    public Month getById(Long id) {
        return this.monthDao.find(id);
    }

    public Month saveOrUpdate(Month month) {
        if (month.getId() == null) {
            this.monthDao.save(month);
        } else {
            this.monthDao.update(month);
        }
        return month;
    }

    public List<Month> createMonthsForQuarter(Quarter quarter, LocalDate startDate, LocalDate endDate,
                                              List<DissagregationType> dissagregationTypes,
                                              List<Canton> cantones) throws GeneralAppException {
        QuarterEnum quarterEnum = quarter.getQuarter();
        List<MonthEnum> monthsEnums = MonthEnum.getMonthsByQuarter(quarterEnum);
        // solo los meses q enten dentro del periodo


        List<Month> months = new ArrayList<>();
        for (MonthEnum monthEnum : monthsEnums) {
            Period period = Period.between(startDate, endDate.plusDays(1));
            LocalDate firstDay = LocalDate.of(quarter.getYear(), monthEnum.getOrder(), 1);
            LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
            if (
                    firstDay.isBefore(endDate.plusDays(1)) && lastDay.isAfter(startDate.minusDays(1))
            ) {


                Month month = this.createMonth(quarter.getYear(), monthEnum, dissagregationTypes, cantones);
                months.add(month);
            }
        }
        return months;

    }

    public Month createMonth(Integer year, MonthEnum monthEnum, List<DissagregationType> dissagregationTypes, List<Canton> cantones) throws GeneralAppException {
        Month m = new Month();
        m.setState(State.ACTIVO);
        m.setYear(year);
        m.setMonth(monthEnum);

        Set<IndicatorValue> indicatorValues = new HashSet<>();
        for (DissagregationType dissagregationType : dissagregationTypes) {
            indicatorValues.addAll(this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(dissagregationType, cantones));
        }

        for (IndicatorValue indicatorValue : indicatorValues) {
            m.addIndicatorValue(indicatorValue);

        }
        return m;
    }

}
