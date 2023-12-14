package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import com.sagatechs.generics.persistence.model.State;
import org.hibernate.annotations.DiscriminatorOptions;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.StandardDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.StandardDissagregationOptionPeriodId;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@DiscriminatorOptions(force = true)
@Table(schema = "osmosys", name = "period_dissagregation_options")
public abstract class PeriodStandardDissagregationOption<T extends StandardDissagregationOption> {

    public PeriodStandardDissagregationOption() {

    }

    public PeriodStandardDissagregationOption(Period period, T dissagregationOption) {
        this.period = period;
        this.setDissagregationOption(dissagregationOption);
        this.setId(new StandardDissagregationOptionPeriodId(period.getId(), dissagregationOption.getId()));
        this.state = State.ACTIVO;
    }


    @EmbeddedId
    private StandardDissagregationOptionPeriodId id = new StandardDissagregationOptionPeriodId();


    public StandardDissagregationOptionPeriodId getId() {
        return id;
    }

    public void setId(StandardDissagregationOptionPeriodId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("periodId")
    @JoinColumn(name = "period_id")
    private Period period;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StandardDissagregationOption.class )
    @MapsId("dissagregationOptionId")
    @JoinColumn(name = "dissagregation_option_id")
    private T dissagregationOption;


    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    public T getDissagregationOption() {
        return dissagregationOption;
    }

    public void setDissagregationOption(T dissagregationOption) {
        this.dissagregationOption = dissagregationOption;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }


    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
