package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.CountryOfOriginDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.CountryOfOriginDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "period_country_of_origin_dissagregation_options")
public class PeriodCountryOfOriginDissagregationOption {

    public PeriodCountryOfOriginDissagregationOption() {
    }

    public PeriodCountryOfOriginDissagregationOption(Period period, CountryOfOriginDissagregationOption countryOfOriginDissagregationOption) {
        this.period = period;
        this.countryOfOriginDissagregationOption = countryOfOriginDissagregationOption;
        this.id = new CountryOfOriginDissagregationOptionPeriodId(period.getId(), countryOfOriginDissagregationOption.getId());
        this.state=State.ACTIVO;
    }

    @EmbeddedId
    private CountryOfOriginDissagregationOptionPeriodId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("periodId")
    private Period period;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("countryOfOriginDissagregationOptionId")
    private CountryOfOriginDissagregationOption countryOfOriginDissagregationOption;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    public CountryOfOriginDissagregationOptionPeriodId getId() {
        return id;
    }

    public void setId(CountryOfOriginDissagregationOptionPeriodId id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public CountryOfOriginDissagregationOption getCountryOfOriginDissagregationOption() {
        return countryOfOriginDissagregationOption;
    }

    public void setCountryOfOriginDissagregationOption(CountryOfOriginDissagregationOption countryOfOriginDissagregationOption) {
        this.countryOfOriginDissagregationOption = countryOfOriginDissagregationOption;
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

        if (!(o instanceof PeriodCountryOfOriginDissagregationOption)) return false;

        PeriodCountryOfOriginDissagregationOption that = (PeriodCountryOfOriginDissagregationOption) o;

        return new EqualsBuilder().append(period, that.period).append(countryOfOriginDissagregationOption, that.countryOfOriginDissagregationOption).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(period).append(countryOfOriginDissagregationOption).toHashCode();
    }
}
