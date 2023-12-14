package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.GenderDissagregationOption;

import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.StandardDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity(name = "PeriodGenderOption")
@DiscriminatorValue("period_gender_option")
public class PeriodGenderDissagregationOption
        extends PeriodStandardDissagregationOption<GenderDissagregationOption> {

    public PeriodGenderDissagregationOption() {
    }




    public PeriodGenderDissagregationOption(Period period, GenderDissagregationOption genderDissagregationOption) {

        super(period, genderDissagregationOption);

    }

}
