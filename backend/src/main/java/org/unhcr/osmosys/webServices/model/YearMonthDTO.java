package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.model.enums.SourceType;
import org.unhcr.osmosys.model.enums.TimeStateEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;


public class YearMonthDTO implements Serializable {

    public YearMonthDTO() {
    }

    public YearMonthDTO(Integer year, String month, Integer monthYearOrder) {
        this.year = year;
        this.month = MonthEnum.valueOf(month);

    }

    private Integer year;
    private MonthEnum month;
    private Integer monthYearOrder;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public MonthEnum getMonth() {
        return month;
    }

    public void setMonth(MonthEnum month) {
        this.month = month;
    }

    public Integer getMonthYearOrder() {
        return monthYearOrder;
    }

    public void setMonthYearOrder(Integer monthYearOrder) {
        this.monthYearOrder = monthYearOrder;
    }
}
