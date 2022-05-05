package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(schema = "osmosys", name = "period_statement_asignations",
        uniqueConstraints = @UniqueConstraint(name = "period_statament_ids_unique", columnNames = {"period_id", "statement_id"})
)
public class PeriodStatementAsignation extends BaseEntity<Long> {


    public PeriodStatementAsignation() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_id", nullable = false, foreignKey = @ForeignKey(name = "period_statement_asignation_period"))
    private Period period;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statement_id", nullable = false, foreignKey = @ForeignKey(name = "period_statement_asignation_statement"))
    private Statement statement;


    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "periodStatementAsignation")
    private Set<PeriodStatementPopulationCoverage> periodStatementPopulationCoverages = new HashSet<>();

    // calculated
    @Column(name = "population_coverage")
    private Long populationCoverage;


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void addPeriodStatementPopulationCoverage(PeriodStatementPopulationCoverage periodStatementPopulationCoverage) {
        periodStatementPopulationCoverage.setPeriodStatementAsignation(this);
        if (!this.periodStatementPopulationCoverages.add(periodStatementPopulationCoverage)) {
            this.periodStatementPopulationCoverages.remove(periodStatementPopulationCoverage);
            this.periodStatementPopulationCoverages.add(periodStatementPopulationCoverage);
        }
    }

    public void removePeriodStatementPopulationCoverage(PeriodStatementPopulationCoverage periodStatementPopulationCoverage) {

        if (periodStatementPopulationCoverage.getId() != null) {
            periodStatementPopulationCoverage.setPopulationCoverage(0L);
            periodStatementPopulationCoverage.setState(State.INACTIVO);
        } else {
            this.periodStatementPopulationCoverages.remove(periodStatementPopulationCoverage);
        }
    }

    public Set<PeriodStatementPopulationCoverage> getPeriodStatementPopulationCoverages() {
        return periodStatementPopulationCoverages;
    }

    public void setPeriodStatementPopulationCoverages(Set<PeriodStatementPopulationCoverage> periodStatementPopulationCoverages) {
        this.periodStatementPopulationCoverages = periodStatementPopulationCoverages;
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

        PeriodStatementAsignation that = (PeriodStatementAsignation) o;

        return new EqualsBuilder().append(id, that.id).append(period, that.period).append(statement, that.statement).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(period).append(statement).toHashCode();
    }

    @Override
    public String toString() {
        return "PeriodStatementAsignation{" +
                "id=" + id +
                ", period=" + period +
                ", statement=" + statement +
                ", state=" + state +
                ", populationCoverage=" + populationCoverage +
                '}';
    }
}
