package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.AgeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.StandardDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.AgeDissagregationOptionPeriodId;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.StandardDissagregationOptionPeriodId;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public abstract class PeriodStandardDissagregationOption<T extends StandardDissagregationOption, PK extends StandardDissagregationOptionPeriodId> {

    public PeriodStandardDissagregationOption() {

    }

    public PeriodStandardDissagregationOption(Period period, T dissagregationOption, PK id) {
        this.period = period;
        this.setDissagregationOption(dissagregationOption);
        this.setId(id);
        this.state = State.ACTIVO;
    }



    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("periodId")
    private Period period;


    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    public abstract PK getId();

    public abstract void setId(PK id);

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
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodStandardDissagregationOption<?, ?> that = (PeriodStandardDissagregationOption<?, ?>) o;
        return Objects.equals(period, that.period) && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(period, state);
    }
}
