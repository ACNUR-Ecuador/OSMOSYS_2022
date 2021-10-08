package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.PopulationType;

import javax.persistence.*;

@Entity
@Table(schema = "osmosys", name = "period_statement_population_coverages")
public class PeriodStatementPopulationCoverage extends BaseEntity<Long> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @Column(name = "population_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PopulationType populationType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_statement_asignation_id",nullable = false , foreignKey = @ForeignKey(name = "period_statement_asignation_population"))
    private PeriodStatementAsignation periodStatementAsignation;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "population_coverage")
    private Long populationCoverage;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PopulationType getPopulationType() {
        return populationType;
    }

    public void setPopulationType(PopulationType populationType) {
        this.populationType = populationType;
    }

    public PeriodStatementAsignation getPeriodStatementAsignation() {
        return periodStatementAsignation;
    }

    public void setPeriodStatementAsignation(PeriodStatementAsignation periodStatementAsignation) {
        this.periodStatementAsignation = periodStatementAsignation;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Long getPopulationCoverage() {
        return populationCoverage;
    }

    public void setPopulationCoverage(Long populationCoverage) {
        this.populationCoverage = populationCoverage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PeriodStatementPopulationCoverage that = (PeriodStatementPopulationCoverage) o;

        return new EqualsBuilder().append(id, that.id).append(populationType, that.populationType).append(periodStatementAsignation, that.periodStatementAsignation).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(populationType).append(periodStatementAsignation).toHashCode();
    }
}
