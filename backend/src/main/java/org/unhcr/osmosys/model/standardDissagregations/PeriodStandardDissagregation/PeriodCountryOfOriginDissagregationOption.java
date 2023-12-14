package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.CountryOfOriginDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.StandardDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity(name = "PeriodCountryOfOriginOption")
@DiscriminatorValue("period_country_of_origin_option")
public class PeriodCountryOfOriginDissagregationOption extends PeriodStandardDissagregationOption<CountryOfOriginDissagregationOption> {

    public PeriodCountryOfOriginDissagregationOption() {
        super();
    }


    public PeriodCountryOfOriginDissagregationOption(Period period, CountryOfOriginDissagregationOption countryOfOriginDissagregationOption) {
        super(period, countryOfOriginDissagregationOption);
    }


}
