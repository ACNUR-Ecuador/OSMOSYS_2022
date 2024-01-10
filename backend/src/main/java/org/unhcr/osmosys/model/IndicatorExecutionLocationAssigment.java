package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(schema = "osmosys", name = "indicator_execution_location_assigments",
        uniqueConstraints = @UniqueConstraint(name = "unique_indicator_execution_location_assigments", columnNames = {"indicator_execution_id", "canton_id"}))
public class IndicatorExecutionLocationAssigment extends BaseEntityIdState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indicator_execution_id", nullable = false, foreignKey = @ForeignKey(name = "fk_indicator_execution_location"))
    private IndicatorExecution indicatorExecution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canton_id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_canton"))
    private Canton location;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof IndicatorExecutionLocationAssigment)) return false;

        IndicatorExecutionLocationAssigment that = (IndicatorExecutionLocationAssigment) o;

        return new EqualsBuilder().append(id, that.id).append(indicatorExecution, that.indicatorExecution).append(location, that.location).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(indicatorExecution).append(location).toHashCode();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IndicatorExecution getIndicatorExecution() {
        return indicatorExecution;
    }

    public void setIndicatorExecution(IndicatorExecution indicatorExecution) {
        this.indicatorExecution = indicatorExecution;
    }

    public Canton getLocation() {
        return location;
    }

    public void setLocation(Canton location) {
        this.location = location;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}