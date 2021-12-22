package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.IndicatorExecution;
import org.unhcr.osmosys.model.Month;
import org.unhcr.osmosys.model.enums.AreaType;
import org.unhcr.osmosys.model.enums.QuarterEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


public class QuarterResumeWeb implements Serializable {
    private Long id;
    private QuarterEnum quarter;
    private String commentary;
    private Integer order;
    private Integer year;
    private BigDecimal target;
    private BigDecimal totalExecution;
    private BigDecimal executionPercentage;
    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public BigDecimal getExecutionPercentage() {
        return executionPercentage;
    }

    public void setExecutionPercentage(BigDecimal executionPercentage) {
        this.executionPercentage = executionPercentage;
    }
}
