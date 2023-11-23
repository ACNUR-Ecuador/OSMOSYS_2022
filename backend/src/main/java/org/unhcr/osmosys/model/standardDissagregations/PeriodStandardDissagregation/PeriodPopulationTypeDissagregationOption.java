package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.PopulationTypeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.PopulationTypeDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "period_population_type_dissagregation_options")
public class PeriodPopulationTypeDissagregationOption {

    public PeriodPopulationTypeDissagregationOption() {
    }

    public PeriodPopulationTypeDissagregationOption(Period period, PopulationTypeDissagregationOption populationTypeDissagregationOption) {
        this.period = period;
        this.populationTypeDissagregationOption = populationTypeDissagregationOption;
        this.id = new PopulationTypeDissagregationOptionPeriodId(period.getId(), populationTypeDissagregationOption.getId());
        this.state=State.ACTIVO;
    }

    @EmbeddedId
    private PopulationTypeDissagregationOptionPeriodId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("periodId")
    private Period period;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("populationTypeDissagregationOptionId")
    private PopulationTypeDissagregationOption populationTypeDissagregationOption;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    public PopulationTypeDissagregationOptionPeriodId getId() {
        return id;
    }

    public void setId(PopulationTypeDissagregationOptionPeriodId id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public PopulationTypeDissagregationOption getPopulationTypeDissagregationOption() {
        return populationTypeDissagregationOption;
    }

    public void setPopulationTypeDissagregationOption(PopulationTypeDissagregationOption populationTypeDissagregationOption) {
        this.populationTypeDissagregationOption = populationTypeDissagregationOption;
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

        if (!(o instanceof PeriodPopulationTypeDissagregationOption)) return false;

        PeriodPopulationTypeDissagregationOption that = (PeriodPopulationTypeDissagregationOption) o;

        return new EqualsBuilder().append(period, that.period).append(populationTypeDissagregationOption, that.populationTypeDissagregationOption).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(period).append(populationTypeDissagregationOption).toHashCode();
    }
}
