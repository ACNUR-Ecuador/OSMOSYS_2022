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
        super(period, countryOfOriginDissagregationOption);
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dissagregation_option_id")
    private CountryOfOriginDissagregationOption dissagregationOption;



    @Override
    public CountryOfOriginDissagregationOption getDissagregationOption() {
        return dissagregationOption;
    }

    @Override
    public void setDissagregationOption(CountryOfOriginDissagregationOption dissagregationOption) {
        this.dissagregationOption = dissagregationOption;
    }


}
