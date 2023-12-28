package org.unhcr.osmosys.model.standardDissagregations.periodOptions;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.options.PopulationTypeDissagregationOption;

import javax.persistence.*;

@Entity(name = "PeriodPopulationTypeOption")
@DiscriminatorValue("population_type")
public class PeriodPopulationTypeDissagregationOption extends PeriodStandardDissagregationOption<PopulationTypeDissagregationOption>{

    public PeriodPopulationTypeDissagregationOption() {
    }

    public PeriodPopulationTypeDissagregationOption(Period period, PopulationTypeDissagregationOption populationTypeDissagregationOption) {
        super(period,populationTypeDissagregationOption);
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dissagregation_option_id")
    private PopulationTypeDissagregationOption dissagregationOption;


    @Override
    public PopulationTypeDissagregationOption getDissagregationOption() {
        return dissagregationOption;
    }

    @Override
    public void setDissagregationOption(PopulationTypeDissagregationOption dissagregationOption) {
        this.dissagregationOption = dissagregationOption;
    }
}
