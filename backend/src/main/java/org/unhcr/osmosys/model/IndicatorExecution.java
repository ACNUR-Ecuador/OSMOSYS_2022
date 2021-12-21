package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.IndicatorType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "indicator_executions")

public class IndicatorExecution extends BaseEntity<Long> {


    public IndicatorExecution() {
        this.state=State.ACTIVO;
        this.totalExecution=BigDecimal.ZERO;
        this.target=null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @Column(name = "commentary", columnDefinition = "text")
    private String commentary;

    @Column(name = "target")
    private BigDecimal target;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "performance_indicator_id", foreignKey = @ForeignKey(name = "fk_indicator_executions_performance_indicators"))
    private Indicator indicator;

    @Column(name = "compassIndicator", nullable = false)
    private Boolean compassIndicator;

    @Column(name = "indicator_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private IndicatorType indicatorType;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12, unique = false)
    private State state;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", foreignKey = @ForeignKey(name = "fk_indicator_execution_period_id"))
    private Period period;

    @Column(name = "total_execution", nullable = false)
    private BigDecimal totalExecution;

    @OneToMany(mappedBy = "indicatorExecution", cascade = CascadeType.ALL)
    private Set<Quarter> quarters = new HashSet<>();

    @OneToMany(mappedBy = "indicatorExecution", fetch = FetchType.LAZY)
    private Set<DissagregationAssignationToIndicatorExecution> dissagregationsAssignationsToIndicatorExecutions = new HashSet<>();

    @OneToMany(mappedBy = "indicatorExecution", fetch = FetchType.LAZY)
    private Set<CustomDissagregationAssignationToIndicatorExecution> customDissagregationAssignationToIndicatorExecutions = new HashSet<>();

    /*socios ii*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = true, foreignKey = @ForeignKey(name = "fk_indicator_execution_project"))
    private Project project;

    /* implementación directa*/
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_office_id", foreignKey = @ForeignKey(name = "fk_indicator_execution_reporting_office"))
    private Office reportingOffice;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id", foreignKey = @ForeignKey(name = "fk_indicator_execution_user"))
    private User assignedUser;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_backup_id", foreignKey = @ForeignKey(name = "fk_indicator_execution_user_backup"))
    private User assignedUserBackup;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(schema ="osmosys" ,name = "indicator_executions_markers", joinColumns = @JoinColumn(name = "indicator_execution_id"), inverseJoinColumns = @JoinColumn(name = "marker_id"))
    private Set<Marker> markers = new HashSet<>();

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

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
        this.indicatorType = indicator.getIndicatorType();
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public void addDissagregationAssignationToIndicatorExecution(DissagregationAssignationToIndicatorExecution dissagregationAssignationToIndicatorExecution) {
        dissagregationAssignationToIndicatorExecution.setIndicatorExecution(this);
        dissagregationAssignationToIndicatorExecution.setState(State.ACTIVO);
        if (!this.dissagregationsAssignationsToIndicatorExecutions.add(dissagregationAssignationToIndicatorExecution)) {
            this.dissagregationsAssignationsToIndicatorExecutions.remove(dissagregationAssignationToIndicatorExecution);
            this.dissagregationsAssignationsToIndicatorExecutions.add(dissagregationAssignationToIndicatorExecution);
        }
    }


    public void removeDissagregationAssignationToIndicatorExecution(DissagregationAssignationToIndicatorExecution dissagregationAssignationToIndicatorExecution) {

        if (dissagregationAssignationToIndicatorExecution.getId() != null) {
            dissagregationAssignationToIndicatorExecution.setIndicatorExecution(this);
            dissagregationAssignationToIndicatorExecution.setState(State.INACTIVO);
        } else {
            this.dissagregationsAssignationsToIndicatorExecutions.remove(dissagregationAssignationToIndicatorExecution);
        }
    }

    public Set<DissagregationAssignationToIndicatorExecution> getDissagregationsAssignationsToIndicatorExecutions() {
        return dissagregationsAssignationsToIndicatorExecutions;
    }

    public void setDissagregationsAssignationsToIndicatorExecutions(Set<DissagregationAssignationToIndicatorExecution> dissagregationsAssignationsToIndicatorExecutions) {
        this.dissagregationsAssignationsToIndicatorExecutions = dissagregationsAssignationsToIndicatorExecutions;
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

    public void addQuarter(Quarter quarter){
        quarter.setIndicatorExecution(this);
        if(!this.quarters.add(quarter)){
            this.quarters.remove(quarter);
            this.quarters.add(quarter);
        }
    }

    public Set<CustomDissagregationAssignationToIndicatorExecution> getCustomDissagregationAssignationToIndicatorExecutions() {
        return customDissagregationAssignationToIndicatorExecutions;
    }

    public void setCustomDissagregationAssignationToIndicatorExecutions(Set<CustomDissagregationAssignationToIndicatorExecution> customDissagregationAssignationToIndicatorExecutions) {
        this.customDissagregationAssignationToIndicatorExecutions = customDissagregationAssignationToIndicatorExecutions;
    }

    public Set<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(Set<Marker> markers) {
        this.markers = markers;
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

    @Override
    public String toString() {
        return "IndicatorExecution{" +
                "id=" + id +
                ", commentary='" + commentary + '\'' +
                ", target=" + target +
                ", indicator=" + indicator +
                ", compassIndicator=" + compassIndicator +
                ", indicatorType=" + indicatorType +
                ", state=" + state +
                '}';
    }


}
