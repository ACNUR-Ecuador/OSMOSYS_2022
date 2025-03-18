package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.unhcr.osmosys.model.enums.IndicatorType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
@Entity
@Table(schema = "osmosys", name = "indicator_executions")

public class IndicatorExecution extends BaseEntityIdState {


    public IndicatorExecution() {
        this.state = State.ACTIVO;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @Column(name = "activity_description", columnDefinition = "text")
    private String activityDescription;

    @Column(name = "target")
    private BigDecimal target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_statement_id", foreignKey = @ForeignKey(name = "fk_indicator_executions_statement"))
    private Statement projectStatement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_indicator_id", foreignKey = @ForeignKey(name = "fk_indicator_executions_performance_indicators"))
    private Indicator indicator;

    @Column(name = "compassIndicator", nullable = false)
    private Boolean compassIndicator;

    @Column(name = "indicator_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private IndicatorType indicatorType;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", foreignKey = @ForeignKey(name = "fk_indicator_execution_period_id"))
    private Period period;

    @Column(name = "total_execution")
    private BigDecimal totalExecution;

    @Column(name = "execution_percentage")
    private BigDecimal executionPercentage;

    @OneToMany(mappedBy = "indicatorExecution", cascade = CascadeType.ALL)
    private Set<Quarter> quarters = new HashSet<>();

    /*socios ii*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_indicator_execution_project"))
    private Project project;

    /* implementación directa*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_office_id", foreignKey = @ForeignKey(name = "fk_indicator_execution_reporting_office"))
    private Office reportingOffice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_user_id", foreignKey = @ForeignKey(name = "fk_indicator_execution_supervisor"))
    private User supervisorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id", foreignKey = @ForeignKey(name = "fk_indicator_execution_user"))
    private User assignedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_backup_id", foreignKey = @ForeignKey(name = "fk_indicator_execution_user_backup"))
    private User assignedUserBackup;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "indicatorExecution", cascade = CascadeType.ALL)
    private Set<IndicatorExecutionLocationAssigment> indicatorExecutionLocationAssigments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "disagregationOption", cascade = CascadeType.ALL)
    private Set<IndicatorExecutionDissagregationAssigment> indicatorExecutionDissagregationAssigments = new HashSet<>();

    @Column(name = "keep_budget")
    private Boolean keepBudget;

    @Column(name = "assigned_budget")
    private BigDecimal assignedBudget;

    @Column(name = "available_budget")
    private BigDecimal availableBudget;

    @Column(name = "total_used_budget")
    private BigDecimal totalUsedBudget;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
        this.indicatorType = indicator.getIndicatorType();
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }



    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {

        this.project = project;
    }

    public Office getReportingOffice() {
        return reportingOffice;
    }

    public void setReportingOffice(Office reportingOffice) {
        this.reportingOffice = reportingOffice;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public User getAssignedUserBackup() {
        return assignedUserBackup;
    }

    public void setAssignedUserBackup(User assignedUserBackup) {
        this.assignedUserBackup = assignedUserBackup;
    }

    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }

    public Set<Quarter> getQuarters() {
        return quarters;
    }

    public void setQuarters(Set<Quarter> quarters) {
        this.quarters = quarters;
    }

    public void addQuarter(Quarter quarter) {
        quarter.setIndicatorExecution(this);
        if (!this.quarters.add(quarter)) {
            this.quarters.remove(quarter);
            this.quarters.add(quarter);
        }
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

    public Statement getProjectStatement() {
        return projectStatement;
    }

    public void setProjectStatement(Statement projectStatement) {
        this.projectStatement = projectStatement;
    }

    public User getSupervisorUser() {
        return supervisorUser;
    }

    public void setSupervisorUser(User supervisorUser) {
        this.supervisorUser = supervisorUser;
    }

    public Set<IndicatorExecutionLocationAssigment> getIndicatorExecutionLocationAssigments() {
        return indicatorExecutionLocationAssigments;
    }

    public void setIndicatorExecutionLocationAssigments(Set<IndicatorExecutionLocationAssigment> indicatorExecutionLocationAssigments) {
        this.indicatorExecutionLocationAssigments = indicatorExecutionLocationAssigments;
    }

    public void addIndicatorExecutionLocationAssigment(IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigment) {
        indicatorExecutionLocationAssigment.setIndicatorExecution(this);
        if (!this.indicatorExecutionLocationAssigments.add(indicatorExecutionLocationAssigment)) {
            this.indicatorExecutionLocationAssigments.remove(indicatorExecutionLocationAssigment);
            this.indicatorExecutionLocationAssigments.add(indicatorExecutionLocationAssigment);
        }
    }

    public Set<IndicatorExecutionDissagregationAssigment> getIndicatorExecutionDissagregationAssigments() {
        return indicatorExecutionDissagregationAssigments;
    }

    public void setIndicatorExecutionDissagregationAssigments(Set<IndicatorExecutionDissagregationAssigment> indicatorExecutionDissagregationAssigments) {
        this.indicatorExecutionDissagregationAssigments = indicatorExecutionDissagregationAssigments;
    }

    @Override
    public String toString() {
        return "IndicatorExecution{" +
                "id=" + id +
                ", activityDescription='" + activityDescription + '\'' +
                ", target=" + target +
                ", projectStatement=" + projectStatement +
                ", indicator=" + indicator +
                ", compassIndicator=" + compassIndicator +
                ", indicatorType=" + indicatorType +
                ", state=" + state +
                ", period=" + period +
                ", totalExecution=" + totalExecution +
                ", executionPercentage=" + executionPercentage +
                ", quarters=" + quarters +
                ", project=" + project +
                ", reportingOffice=" + reportingOffice +
                ", supervisorUser=" + supervisorUser +
                ", assignedUser=" + assignedUser +
                ", assignedUserBackup=" + assignedUserBackup +
                ", indicatorExecutionLocationAssigments=" + indicatorExecutionLocationAssigments +
                ", indicatorExecutionDissagregationAssigments=" + indicatorExecutionDissagregationAssigments +
                ", keepBudget=" + keepBudget +
                ", assignedBudget=" + assignedBudget +
                ", availableBudget=" + availableBudget +
                ", totalUsedBudget=" + totalUsedBudget +
                '}';
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
}
