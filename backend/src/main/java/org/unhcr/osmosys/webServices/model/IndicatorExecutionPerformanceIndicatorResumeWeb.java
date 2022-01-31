package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.IndicatorType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class IndicatorExecutionPerformanceIndicatorResumeWeb implements Serializable {

    private Long id;
    private String commentary;
    private BigDecimal target;
    private IndicatorWeb indicator;
    private StatementWeb projectStatement;
    private String activityDescription;
    private IndicatorType indicatorType;
    private State state;
    private BigDecimal totalExecution;
    private BigDecimal executionPercentage;
    private QuarterWeb lastReportedQuarter;
    private MonthWeb lastReportedMonth;
    private List<QuarterWeb> quarters = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public BigDecimal getTarget() {
        return target;
    }

    public void setTarget(BigDecimal target) {
        this.target = target;
    }



    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public BigDecimal getTotalExecution() {
        return totalExecution;
    }

    public void setTotalExecution(BigDecimal totalExecution) {
        this.totalExecution = totalExecution;
    }

    public List<QuarterWeb> getQuarters() {
        return quarters;
    }

    public void setQuarters(List<QuarterWeb> quarters) {
        this.quarters = quarters;
    }

    public BigDecimal getExecutionPercentage() {
        return executionPercentage;
    }

    public void setExecutionPercentage(BigDecimal executionPercentage) {
        this.executionPercentage = executionPercentage;
    }

    public IndicatorWeb getIndicator() {
        return indicator;
    }

    public void setIndicator(IndicatorWeb indicator) {
        this.indicator = indicator;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public QuarterWeb getLastReportedQuarter() {
        return lastReportedQuarter;
    }

    public void setLastReportedQuarter(QuarterWeb lastReportedQuarter) {
        this.lastReportedQuarter = lastReportedQuarter;
    }

    public MonthWeb getLastReportedMonth() {
        return lastReportedMonth;
    }

    public void setLastReportedMonth(MonthWeb lastReportedMonth) {
        this.lastReportedMonth = lastReportedMonth;
    }

    public StatementWeb getProjectStatement() {
        return projectStatement;
    }

    public void setProjectStatement(StatementWeb projectStatement) {
        this.projectStatement = projectStatement;
    }
}
