package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.GenderDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.GenderDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "period_gender_dissagregation_options")
public class PeriodGenderDissagregationOption {

    public PeriodGenderDissagregationOption() {
    }

    public PeriodGenderDissagregationOption(Period period, GenderDissagregationOption genderDissagregationOption) {
        this.period = period;
        this.genderDissagregationOption = genderDissagregationOption;
        this.id = new GenderDissagregationOptionPeriodId(period.getId(), genderDissagregationOption.getId());
        this.state=State.ACTIVO;
    }

    @EmbeddedId
    private GenderDissagregationOptionPeriodId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("periodId")
    private Period period;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("genderDissagregationOptionId")
    private GenderDissagregationOption genderDissagregationOption;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    public GenderDissagregationOptionPeriodId getId() {
        return id;
    }

    public void setId(GenderDissagregationOptionPeriodId id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public GenderDissagregationOption getGenderDissagregationOption() {
        return genderDissagregationOption;
    }

    public void setGenderDissagregationOption(GenderDissagregationOption genderDissagregationOption) {
        this.genderDissagregationOption = genderDissagregationOption;
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

        if (!(o instanceof PeriodGenderDissagregationOption)) return false;

        PeriodGenderDissagregationOption that = (PeriodGenderDissagregationOption) o;

        return new EqualsBuilder().append(period, that.period).append(genderDissagregationOption, that.genderDissagregationOption).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(period).append(genderDissagregationOption).toHashCode();
    }
}
