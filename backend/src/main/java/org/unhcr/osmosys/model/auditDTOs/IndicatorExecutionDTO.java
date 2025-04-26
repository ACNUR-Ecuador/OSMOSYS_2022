package org.unhcr.osmosys.model.auditDTOs;


public class IndicatorExecutionDTO {
    private Long id;
    private String activityDescription;
    private String target;
    private String projectStatement;
    private String indicator;
    private String code;
    private String compassIndicator;
    private String indicatorType;
    private String state;
    private String period;
    private String totalExecution;
    private String executionPercentage;
    private String projectId;
    private String reportingOffice;
    private String supervisorUser;
    private String assignedUser;
    private String assignedUserBackup;
    private String keepBudget;
    private String assignedBudget;
    private String availableBudget;
    private String totalUsedBudget;
    private String locationAssigments;

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

    public String getProjectStatement() {
        return projectStatement;
    }

    public void setProjectStatement(String projectStatement) {
        this.projectStatement = projectStatement;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getCompassIndicator() {
        return compassIndicator;
    }

    public void setCompassIndicator(String compassIndicator) {
        this.compassIndicator = compassIndicator;
    }

    public String getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(String indicatorType) {
        this.indicatorType = indicatorType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
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

    public String getReportingOffice() {
        return reportingOffice;
    }

    public void setReportingOffice(String reportingOffice) {
        this.reportingOffice = reportingOffice;
    }

    public String getSupervisorUser() {
        return supervisorUser;
    }

    public void setSupervisorUser(String supervisorUser) {
        this.supervisorUser = supervisorUser;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getAssignedUserBackup() {
        return assignedUserBackup;
    }

    public void setAssignedUserBackup(String assignedUserBackup) {
        this.assignedUserBackup = assignedUserBackup;
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

    public String getLocationAssigments() {
        return locationAssigments;
    }

    public void setLocationAssigments(String locationAssigments) {
        this.locationAssigments = locationAssigments;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "{" +
                "\"ID\":" + id +
                ", \"Descripción de Actividad\":\"" + activityDescription + "\"" +
                ", \"Meta\":\"" + target + "\"" +
                ", \"Declaración de Proyecto\":\"" + projectStatement + "\"" +
                ", \"Indicador\":\"" + indicator + "\"" +
                ", \"Código de Indicador\":\"" + code + "\"" +
                ", \"Indicador Compass\":\"" + compassIndicator + "\"" +
                ", \"Tipo de Indicador\":\"" + indicatorType + "\"" +
                ", \"Estado\":\"" + state + "\"" +
                ", \"Periodo\":\"" + period + "\"" +
                ", \"Total de Ejecución\":\"" + totalExecution + "\"" +
                ", \"Porcentaje de Ejecución\":\"" + executionPercentage + "\"" +
                ", \"ID de Proyecto\":\"" + projectId + "\"" +
                ", \"Oficina de Reporte\":\"" + reportingOffice + "\"" +
                ", \"Supervisor\":\"" + supervisorUser + "\"" +
                ", \"Usuario Asignado\":\"" + assignedUser + "\"" +
                ", \"Usuario Backup Asignado\":\"" + assignedUserBackup + "\"" +
                ", \"Mantener Presupuesto\":\"" + keepBudget + "\"" +
                ", \"Presupuesto Asignado\":\"" + assignedBudget + "\"" +
                ", \"Presupuesto Disponible\":\"" + availableBudget + "\"" +
                ", \"Total de Presupuesto Usado\":\"" + totalUsedBudget + "\"" +
                ", \"Lugares Asignados\":\"" + locationAssigments + "\"" +
                '}';
    }
}
