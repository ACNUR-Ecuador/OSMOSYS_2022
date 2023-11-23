package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.AgeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.AgeDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "period_age_dissagregation_options")
public class PeriodAgeDissagregationOption {

    public PeriodAgeDissagregationOption() {
    }

    public PeriodAgeDissagregationOption(Period period, AgeDissagregationOption ageDissagregationOption) {
        this.period = period;
        this.ageDissagregationOption = ageDissagregationOption;
        this.id = new AgeDissagregationOptionPeriodId(period.getId(), ageDissagregationOption.getId());
        this.state=State.ACTIVO;
    }

    @EmbeddedId
    private AgeDissagregationOptionPeriodId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("periodId")
    private Period period;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("ageDissagregationOptionId")
    private AgeDissagregationOption ageDissagregationOption;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    public AgeDissagregationOptionPeriodId getId() {
        return id;
    }

    public void setId(AgeDissagregationOptionPeriodId id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public AgeDissagregationOption getAgeDissagregationOption() {
        return ageDissagregationOption;
    }

    public void setAgeDissagregationOption(AgeDissagregationOption ageDissagregationOption) {
        this.ageDissagregationOption = ageDissagregationOption;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PeriodAgeDissagregationOption)) return false;

        PeriodAgeDissagregationOption that = (PeriodAgeDissagregationOption) o;

        return new EqualsBuilder().append(period, that.period).append(ageDissagregationOption, that.ageDissagregationOption).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(period).append(ageDissagregationOption).toHashCode();
    }
}
