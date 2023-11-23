package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.AgeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.CountryOfOriginDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.AgeDissagregationOptionPeriodId;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.CountryOfOriginDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "period_country_of_origin_dissagregation_options")
public class PeriodCountryOfOriginDissagregationOption extends PeriodStandardDissagregationOption<CountryOfOriginDissagregationOption, CountryOfOriginDissagregationOptionPeriodId> {

    public PeriodCountryOfOriginDissagregationOption() {
    }


    public PeriodCountryOfOriginDissagregationOption(Period period, CountryOfOriginDissagregationOption countryOfOriginDissagregationOption) {
        super(period, countryOfOriginDissagregationOption, new CountryOfOriginDissagregationOptionPeriodId(period.getId(), countryOfOriginDissagregationOption.getId()));
    }

    @EmbeddedId
    private CountryOfOriginDissagregationOptionPeriodId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("dissagregationOptionId")
    private CountryOfOriginDissagregationOption dissagregationOption;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Override
    public CountryOfOriginDissagregationOption getDissagregationOption() {
        return dissagregationOption;
    }

    @Override
    public void setDissagregationOption(CountryOfOriginDissagregationOption dissagregationOption) {
        this.dissagregationOption = dissagregationOption;
    }

    @Override
    public CountryOfOriginDissagregationOptionPeriodId getId() {
        return this.id;
    }

    @Override
    public void setId(CountryOfOriginDissagregationOptionPeriodId id) {
        this.id = id;
    }
}
