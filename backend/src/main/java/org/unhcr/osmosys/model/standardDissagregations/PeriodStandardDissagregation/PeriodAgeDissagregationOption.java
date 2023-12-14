package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.AgeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.StandardDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity(name = "PeriodAgeOption")
@DiscriminatorValue("period_age_option")
public class PeriodAgeDissagregationOption extends PeriodStandardDissagregationOption<AgeDissagregationOption> {

    public PeriodAgeDissagregationOption() {
        super();
    }


    public PeriodAgeDissagregationOption(Period period, AgeDissagregationOption ageDissagregationOption) {
        super(period, ageDissagregationOption);
    }


}
