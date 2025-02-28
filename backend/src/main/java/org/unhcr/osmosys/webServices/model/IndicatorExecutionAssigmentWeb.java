package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.webservice.webModel.UserWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@SuppressWarnings("unused")
public class IndicatorExecutionAssigmentWeb extends BaseWebEntity implements Serializable {

    public IndicatorExecutionAssigmentWeb() {
        super();
    }

    private IndicatorWeb indicator;

    private PeriodWeb period;
    private String activityDescription;


    /*socios ii*/
    private ProjectWeb project;
    private StatementWeb projectStatement;

    private List<CantonWeb> locations = new ArrayList<>();
    /* implementación directa*/
    private OfficeWeb reportingOffice;
    private UserWeb assignedUser;
    private UserWeb supervisorUser;
    private UserWeb assignedUserBackup;
    /**************budget**********/
    private Boolean keepBudget;
    private BigDecimal assignedBudget;

    private Integer target;


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

    public ProjectWeb getProject() {
        return project;
    }

    public void setProject(ProjectWeb project) {
        this.project = project;
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

    public StatementWeb getProjectStatement() {
        return projectStatement;
    }

    public void setProjectStatement(StatementWeb projectStatement) {
        this.projectStatement = projectStatement;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndicatorExecutionAssigmentWeb)) return false;
        IndicatorExecutionAssigmentWeb that = (IndicatorExecutionAssigmentWeb) o;
        return Objects.equals(id, that.id) && Objects.equals(indicator, that.indicator) && state == that.state && Objects.equals(period, that.period) && Objects.equals(project, that.project) && Objects.equals(projectStatement, that.projectStatement) && Objects.equals(activityDescription, that.activityDescription) && Objects.equals(locations, that.locations) && Objects.equals(reportingOffice, that.reportingOffice) && Objects.equals(assignedUser, that.assignedUser) && Objects.equals(supervisorUser, that.supervisorUser) && Objects.equals(assignedUserBackup, that.assignedUserBackup) && Objects.equals(keepBudget, that.keepBudget) && Objects.equals(assignedBudget, that.assignedBudget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, indicator, state, period, project, projectStatement, activityDescription, locations, reportingOffice, assignedUser, supervisorUser, assignedUserBackup, keepBudget, assignedBudget);
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}
