package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.model.enums.QuarterEnum;
import org.unhcr.osmosys.model.enums.TimeStateEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class QuarterWeb extends BaseWebEntity implements Serializable {


    public QuarterWeb() {super();
    }

    private QuarterEnum quarter;
    private String commentary;
    private Integer order;
    private Integer year;
    private BigDecimal target;
    private BigDecimal totalExecution;
    private BigDecimal executionPercentage;

    private List<MonthWeb> months = new ArrayList<>();
    private Boolean blockUpdate;

    private TimeStateEnum late;



    public QuarterEnum getQuarter() {
        return quarter;
    }

    public void setQuarter(QuarterEnum quarter) {
        this.quarter = quarter;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getTarget() {
        return target;
    }

    public void setTarget(BigDecimal target) {
        this.target = target;
    }

    public BigDecimal getTotalExecution() {
        return totalExecution;
    }

    public void setTotalExecution(BigDecimal totalExecution) {
        this.totalExecution = totalExecution;
    }



    public BigDecimal getExecutionPercentage() {
        return executionPercentage;
    }

    public void setExecutionPercentage(BigDecimal executionPercentage) {
        this.executionPercentage = executionPercentage;
    }

    public List<MonthWeb> getMonths() {
        return months;
    }

    public void setMonths(List<MonthWeb> months) {
        this.months = months;
    }

    public Boolean getBlockUpdate() {
        return blockUpdate;
    }

    public void setBlockUpdate(Boolean blockUpdate) {
        this.blockUpdate = blockUpdate;
    }

    public TimeStateEnum getLate() {
        return late;
    }

    public void setLate(TimeStateEnum late) {
        this.late = late;
    }
}
