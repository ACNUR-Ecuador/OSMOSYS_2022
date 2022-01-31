package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.unhcr.osmosys.model.Statement;
import org.unhcr.osmosys.model.enums.IndicatorType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class IndicatorExecutionAssigmentWeb implements Serializable {
    private Long id;
    private String commentary;
    private IndicatorWeb indicator;
    private State state;
    private PeriodWeb period;

    /*socios ii*/
    private ProjectWeb project;
    private StatementWeb projectStatement;
    private String activityDescription;
    /* implementación directa*/
    private OfficeWeb reportingOffice;
    private UserWeb assignedUser;
    private UserWeb assignedUserBackup;
    private List<CantonWeb> locations = new ArrayList<>();

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

    public IndicatorWeb getIndicator() {
        return indicator;
    }

    public void setIndicator(IndicatorWeb indicator) {
        this.indicator = indicator;
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
}
