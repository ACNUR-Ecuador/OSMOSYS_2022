package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.DiversityDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.StandardDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity(name = "PeriodDiversityOption")
@DiscriminatorValue("period_diversity_option")
public class PeriodDiversityDissagregationOption extends PeriodStandardDissagregationOption<DiversityDissagregationOption> {

    public PeriodDiversityDissagregationOption() {
    }

    public PeriodDiversityDissagregationOption(Period period, DiversityDissagregationOption diversityDissagregationOption) {
        super(period, diversityDissagregationOption);
    }

}
