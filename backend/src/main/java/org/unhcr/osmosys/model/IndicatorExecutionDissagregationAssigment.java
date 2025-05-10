package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.standardDissagregations.options.StandardDissagregationOption;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "osmosys", name = "indicator_execution_dissagregation_assigments",
        uniqueConstraints = @UniqueConstraint(name = "unique_indicator_execution_dissagregation_assigments", columnNames = {"indicator_execution_id", "disagregation_type_option_id"}))

public class IndicatorExecutionDissagregationAssigment extends BaseEntityIdState {
    public IndicatorExecutionDissagregationAssigment() {
        this.state = State.ACTIVO;
    }

    public IndicatorExecutionDissagregationAssigment(StandardDissagregationOption disagregationOption) {
        this.disagregationOption = disagregationOption;
        this.state = State.ACTIVO;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indicator_execution_id", nullable = false, foreignKey = @ForeignKey(name = "fk_indicator_execution_dissagregation"))
    private IndicatorExecution indicatorExecution;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disagregation_type_option_id", nullable = false, foreignKey = @ForeignKey(name = "fk_indicator_execution_standardDissOption"))
    private StandardDissagregationOption disagregationOption;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Override
    public Long getId() {
        return id;
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


    public StandardDissagregationOption getDisagregationOption() {
        return disagregationOption;
    }

    public void setDisagregationOption(StandardDissagregationOption disagregationOption) {
        this.disagregationOption = disagregationOption;
    }

    @Override
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "IndicatorExecutionDissagregationAssigment{" +
                "id=" + id +
                ", indicatorExecution=" + indicatorExecution +
                ", disagregationOption=" + disagregationOption +
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicatorExecutionDissagregationAssigment that = (IndicatorExecutionDissagregationAssigment) o;
        return Objects.equals(id, that.id) && Objects.equals(indicatorExecution, that.indicatorExecution) && Objects.equals(disagregationOption, that.disagregationOption) && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, indicatorExecution, disagregationOption, state);
    }
}
