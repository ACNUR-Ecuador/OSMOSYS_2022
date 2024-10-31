package org.unhcr.osmosys.model.auditDTOs;



import java.math.BigDecimal;


public class IndicatorExecutionDTO {
    private Long id;
    private String activityDescription;
    private String target;
    private String projectStatementId;
    private String indicatorId;
    private String compassIndicator;
    private String IndicatorType;
    private String state;
    private String periodId;
    private String totalExecution;
    private String executionPercentage;
    private String projectId;
    private String reportingOfficeId;
    private String supervisorUserId;
    private String assignedUserId;
    private String assignedUserBackupId;
    private String keepBudget;
    private String assignedBudget;
    private String availableBudget;
    private String totalUsedBudget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getProjectStatementId() {
        return projectStatementId;
    }

    public void setProjectStatementId(String projectStatementId) {
        this.projectStatementId = projectStatementId;
    }

    public String getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(String indicatorId) {
        this.indicatorId = indicatorId;
    }

    public String getCompassIndicator() {
        return compassIndicator;
    }

    public void setCompassIndicator(String compassIndicator) {
        this.compassIndicator = compassIndicator;
    }

    public String getIndicatorType() {
        return IndicatorType;
    }

    public void setIndicatorType(String indicatorType) {
        IndicatorType = indicatorType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getTotalExecution() {
        return totalExecution;
    }

    public void setTotalExecution(String totalExecution) {
        this.totalExecution = totalExecution;
    }

    public String getExecutionPercentage() {
        return executionPercentage;
    }

    public void setExecutionPercentage(String executionPercentage) {
        this.executionPercentage = executionPercentage;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getReportingOfficeId() {
        return reportingOfficeId;
    }

    public void setReportingOfficeId(String reportingOfficeId) {
        this.reportingOfficeId = reportingOfficeId;
    }

    public String getSupervisorUserId() {
        return supervisorUserId;
    }

    public void setSupervisorUserId(String supervisorUserId) {
        this.supervisorUserId = supervisorUserId;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getAssignedUserBackupId() {
        return assignedUserBackupId;
    }

    public void setAssignedUserBackupId(String assignedUserBackupId) {
        this.assignedUserBackupId = assignedUserBackupId;
    }

    public String getKeepBudget() {
        return keepBudget;
    }

    public void setKeepBudget(String keepBudget) {
        this.keepBudget = keepBudget;
    }

    public String getAssignedBudget() {
        return assignedBudget;
    }

    public void setAssignedBudget(String assignedBudget) {
        this.assignedBudget = assignedBudget;
    }

    public String getAvailableBudget() {
        return availableBudget;
    }

    public void setAvailableBudget(String availableBudget) {
        this.availableBudget = availableBudget;
    }

    public String getTotalUsedBudget() {
        return totalUsedBudget;
    }

    public void setTotalUsedBudget(String totalUsedBudget) {
        this.totalUsedBudget = totalUsedBudget;
    }
}
