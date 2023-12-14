package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.PopulationTypeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.StandardDissagregationOptionPeriodId;

import javax.persistence.*;


@Entity(name = "PeriodPopulationTypeOption")
@DiscriminatorValue("period_population_type_option")
public class PeriodPopulationTypeDissagregationOption extends PeriodStandardDissagregationOption<PopulationTypeDissagregationOption>{

    public PeriodPopulationTypeDissagregationOption() {
    }

    public PeriodPopulationTypeDissagregationOption(Period period, PopulationTypeDissagregationOption populationTypeDissagregationOption) {
        super(period,populationTypeDissagregationOption);
    }


}
