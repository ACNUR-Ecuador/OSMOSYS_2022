package org.unhcr.osmosys.model.standardDissagregations.periodOptions;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.options.StandardDissagregationOption;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(schema = "dissagregations", name = "period_standard_dissagregation_options",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"period_id", "dissagregation_option_id"})})
public abstract class PeriodStandardDissagregationOption<T extends StandardDissagregationOption> extends BaseEntity<Long> {

    public PeriodStandardDissagregationOption() {

    }

    public PeriodStandardDissagregationOption(Period period, T dissagregationOption) {
        this.period = period;
        this.setDissagregationOption(dissagregationOption);
        this.state = State.ACTIVO;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private Period period;


    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

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

    public abstract T getDissagregationOption();

    public abstract void setDissagregationOption(T dissagregationOption);

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "PeriodStandardDissagregationOption{" +
                "period=" + period +
                ", option=" + this.getDissagregationOption() +
                '}';
    }


}
