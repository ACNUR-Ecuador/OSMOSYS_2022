package org.unhcr.osmosys.model.reportDTOs;

import java.math.BigDecimal;
import java.util.Objects;

public class IndicatorExecutionTagDTO {
    private Long performanceIndicatorId;
    private String indicator;
    private Long periodId;
    private String quarter;
    private String month;
    private Long monthOrder;
    private Long totalValue;

    public IndicatorExecutionTagDTO() {
    }

    public IndicatorExecutionTagDTO(Long performanceIndicatorId,
                                    String indicator,
                                    Long periodId,
                                    String quarter,
                                    String month,
                                    Long monthOrder,
                                    Long totalValue) {
        this.performanceIndicatorId = performanceIndicatorId;
        this.indicator = indicator;
        this.periodId = periodId;
        this.quarter = quarter;
        this.month = month;
        this.monthOrder = monthOrder;
        this.totalValue = totalValue;
    }

    public Long getPerformanceIndicatorId() {
        return performanceIndicatorId;
    }

    public void setPerformanceIndicatorId(Long performanceIndicatorId) {
        this.performanceIndicatorId = performanceIndicatorId;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getMonthOrder() {
        return monthOrder;
    }

    public void setMonthOrder(Long monthOrder) {
        this.monthOrder = monthOrder;
    }

    public Long getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Long totalValue) {
        this.totalValue = totalValue;
    }
}
