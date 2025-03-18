package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.model.enums.TimeStateEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class IndicatorExecutionWeb extends BaseWebEntity implements Serializable {

    public IndicatorExecutionWeb() {
        super();
    }

    private String activityDescription;
    private IndicatorType indicatorType;

    private BigDecimal target;
    private Boolean compassIndicator;
    private IndicatorWeb indicator;
    private PeriodWeb period;
    private BigDecimal totalExecution;
    private BigDecimal executionPercentage;

    private QuarterWeb lastReportedQuarter;
    private MonthWeb lastReportedMonth;

    private List<QuarterWeb> quarters = new ArrayList<>();
    private TimeStateEnum late;


    /******socios*****/
    private ProjectWeb project;
    private StatementWeb projectStatement;
    /* implementación directa*/
    private OfficeWeb reportingOffice;
    private UserWeb supervisorUser;
    private UserWeb assignedUser;
    private UserWeb assignedUserBackup;

    /**************budget**********/
    private Boolean keepBudget;
    private BigDecimal assignedBudget;
    private BigDecimal availableBudget;
    private BigDecimal totalUsedBudget;

    /**************administration*********/
    private List<CantonWeb> locations;
    private List<IndicatorExecutionDissagregationAssigmentWeb> dissagregationAssigments;


    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }


    public BigDecimal getTarget() {
        return target;
    }

    public void setTarget(BigDecimal target) {
        this.target = target;
    }

    public Boolean getCompassIndicator() {
        return compassIndicator;
    }

    public void setCompassIndicator(Boolean compassIndicator) {
        this.compassIndicator = compassIndicator;
    }

    public IndicatorWeb getIndicator() {
        return indicator;
    }

    public void setIndicator(IndicatorWeb indicator) {
        this.indicator = indicator;
    }

    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
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

    public List<QuarterWeb> getQuarters() {
        return quarters;
    }

    public void setQuarters(List<QuarterWeb> quarters) {
        this.quarters = quarters;
    }

    public ProjectWeb getProject() {
        return project;
    }

    public void setProject(ProjectWeb project) {
        this.project = project;
    }

    public StatementWeb getProjectStatement() {
        return projectStatement;
    }

    public void setProjectStatement(StatementWeb projectStatement) {
        this.projectStatement = projectStatement;
    }

    public OfficeWeb getReportingOffice() {
        return reportingOffice;
    }

    public void setReportingOffice(OfficeWeb reportingOffice) {
        this.reportingOffice = reportingOffice;
    }

    public UserWeb getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(UserWeb assignedUser) {
        this.assignedUser = assignedUser;
    }

    public UserWeb getAssignedUserBackup() {
        return assignedUserBackup;
    }

    public void setAssignedUserBackup(UserWeb assignedUserBackup) {
        this.assignedUserBackup = assignedUserBackup;
    }

    public List<CantonWeb> getLocations() {
        return locations;
    }

    public void setLocations(List<CantonWeb> locations) {
        this.locations = locations;
    }

    public TimeStateEnum getLate() {
        return late;
    }

    public void setLate(TimeStateEnum late) {
        this.late = late;
    }

    public UserWeb getSupervisorUser() {
        return supervisorUser;
    }

    public void setSupervisorUser(UserWeb supervisorUser) {
        this.supervisorUser = supervisorUser;
    }

    public Boolean getKeepBudget() {
        return keepBudget;
    }

    public void setKeepBudget(Boolean keepBudget) {
        this.keepBudget = keepBudget;
    }

    public BigDecimal getAssignedBudget() {
        return assignedBudget;
    }

    public void setAssignedBudget(BigDecimal assignedBudget) {
        this.assignedBudget = assignedBudget;
    }

    public BigDecimal getAvailableBudget() {
        return availableBudget;
    }

    public void setAvailableBudget(BigDecimal availableBudget) {
        this.availableBudget = availableBudget;
    }

    public BigDecimal getTotalUsedBudget() {
        return totalUsedBudget;
    }

    public void setTotalUsedBudget(BigDecimal totalUsedBudget) {
        this.totalUsedBudget = totalUsedBudget;
    }

    public List<IndicatorExecutionDissagregationAssigmentWeb> getDissagregationAssigments() {
        return dissagregationAssigments;
    }

    public void setDissagregationAssigments(List<IndicatorExecutionDissagregationAssigmentWeb> dissagregationAssigments) {
        this.dissagregationAssigments = dissagregationAssigments;
    }
}
