package org.unhcr.osmosys.model.standardDissagregations.periodOptions;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.options.CountryOfOriginDissagregationOption;

import javax.persistence.*;

@Entity(name = "PeriodCountryOfOriginOption")
@DiscriminatorValue("country_of_origin")
public class PeriodCountryOfOriginDissagregationOption extends PeriodStandardDissagregationOption<CountryOfOriginDissagregationOption> {

    public PeriodCountryOfOriginDissagregationOption() {
        super();
    }


    public PeriodCountryOfOriginDissagregationOption(Period period, CountryOfOriginDissagregationOption countryOfOriginDissagregationOption) {
        super(period);
        this.countryOfOriginDissagregationOption = countryOfOriginDissagregationOption;
    }


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CountryOfOriginDissagregationOption.class)
    @JoinColumn(name = "dissagregation_option_id")
    private CountryOfOriginDissagregationOption countryOfOriginDissagregationOption;

    public CountryOfOriginDissagregationOption getCountryOfOriginDissagregationOption() {
        return countryOfOriginDissagregationOption;
    }

    public void setCountryOfOriginDissagregationOption(CountryOfOriginDissagregationOption countryOfOriginDissagregationOption) {
        this.countryOfOriginDissagregationOption = countryOfOriginDissagregationOption;
    }

    @Override
    public CountryOfOriginDissagregationOption getDissagregationOption() {
        return countryOfOriginDissagregationOption;
    }

    @Override
    public void setDissagregationOption(CountryOfOriginDissagregationOption dissagregationOption) {
        this.countryOfOriginDissagregationOption = dissagregationOption;
    }


}
