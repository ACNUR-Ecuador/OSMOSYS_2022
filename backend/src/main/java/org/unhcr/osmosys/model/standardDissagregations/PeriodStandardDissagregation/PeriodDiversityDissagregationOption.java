package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.DiversityDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.DiversityDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "period_diversity_dissagregation_options")
public class PeriodDiversityDissagregationOption {

    public PeriodDiversityDissagregationOption() {
    }

    public PeriodDiversityDissagregationOption(Period period, DiversityDissagregationOption diversityDissagregationOption) {
        this.period = period;
        this.diversityDissagregationOption = diversityDissagregationOption;
        this.id = new DiversityDissagregationOptionPeriodId(period.getId(), diversityDissagregationOption.getId());
        this.state=State.ACTIVO;
    }

    @EmbeddedId
    private DiversityDissagregationOptionPeriodId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("periodId")
    private Period period;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("diversityDissagregationOptionId")
    private DiversityDissagregationOption diversityDissagregationOption;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    public DiversityDissagregationOptionPeriodId getId() {
        return id;
    }

    public void setId(DiversityDissagregationOptionPeriodId id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public DiversityDissagregationOption getDiversityDissagregationOption() {
        return diversityDissagregationOption;
    }

    public void setDiversityDissagregationOption(DiversityDissagregationOption diversityDissagregationOption) {
        this.diversityDissagregationOption = diversityDissagregationOption;
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

        if (!(o instanceof PeriodDiversityDissagregationOption)) return false;

        PeriodDiversityDissagregationOption that = (PeriodDiversityDissagregationOption) o;

        return new EqualsBuilder().append(period, that.period).append(diversityDissagregationOption, that.diversityDissagregationOption).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(period).append(diversityDissagregationOption).toHashCode();
    }
}
