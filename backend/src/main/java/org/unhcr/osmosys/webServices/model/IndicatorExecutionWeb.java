package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.IndicatorType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


public class IndicatorExecutionWeb implements Serializable {
    private Long id;
    private String commentary;
    private BigDecimal target;
    private IndicatorWeb indicator;
    private Boolean compassIndicator;
    private IndicatorType indicatorType;
    private State state;
    private PeriodWeb period;
    private BigDecimal totalExecution;
    private BigDecimal executionPercentage;

    /*socios ii*/
    private ProjectWeb project;
    /* implementación directa*/
    private OfficeWeb reportingOffice;
    private UserWeb assignedUser;
    private UserWeb assignedUserBackup;
    private Set<MarkerWeb> markers = new HashSet<>();

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

    public IndicatorWeb getIndicator() {
        return indicator;
    }

    public void setIndicator(IndicatorWeb indicator) {
        this.indicator = indicator;
    }

    public Boolean getCompassIndicator() {
        return compassIndicator;
    }

    public void setCompassIndicator(Boolean compassIndicator) {
        this.compassIndicator = compassIndicator;
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

    public Set<MarkerWeb> getMarkers() {
        return markers;
    }

    public void setMarkers(Set<MarkerWeb> markers) {
        this.markers = markers;
    }
}
